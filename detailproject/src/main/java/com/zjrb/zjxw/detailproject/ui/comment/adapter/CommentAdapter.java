package com.zjrb.zjxw.detailproject.ui.comment.adapter;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.load.LoadMoreListener;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.FooterLoadMore;
import com.zjrb.core.recycleView.LoadMore;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.apibean.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommentListTask;
import com.zjrb.zjxw.detailproject.ui.mediadetail.VideoDetailTextHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;

import java.util.ArrayList;
import java.util.List;

import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * 评论页Adapter(与视频/直播详情评论共用)
 * Created by wanglinjie.
 * create time:2017/7/19  上午10:14
 */
public class CommentAdapter extends BaseRecyclerAdapter implements LoadMoreListener<CommentRefreshBean> {

    //热门评论
    private int COMMENT_HOT = 1;
    //最新评论
    private int COMMENT_NEW = 2;
    //评论
    private int COMMENT_CONTENT = 3;
    //稿件ID
    private String articleId;
    //是否是精选评论
    private boolean is_select_list;
    private final FooterLoadMore<CommentRefreshBean> mLoadMore;
    private View mView;
    TextView mCommentNum;
    //最新评论数
    private int commentCount;
    //热门评论数
    private int hotCommentNUm = 0;
    private DraftDetailBean mBean;
    private CommentRefreshBean mDatas;
    private boolean isVideoDetail = false;

    public CommentAdapter(CommentRefreshBean datas, ViewGroup parent, View view, String articleId, boolean is_select_list, DraftDetailBean bean, int commentCount) {
        super(null);
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        mView = view;
        mCommentNum = mView.findViewById(R.id.tv_comment_num);
        this.commentCount = commentCount;
        this.articleId = articleId;
        this.is_select_list = is_select_list;
        setData(datas);
        mBean = bean;
        mDatas = datas;
    }

    //视频详情页设置数据
    public CommentAdapter(CommentRefreshBean datas, ViewGroup parent, DraftDetailBean bean, boolean isVideoDetail) {
        super(null);
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        mBean = bean;
        mDatas = datas;
        if (mBean != null && mBean.getArticle() != null && mBean.getArticle().getHot_comments() != null) {
            hotCommentNUm = mBean.getArticle().getHot_comments().size();
        }
        if(mDatas != null){
            this.commentCount = mDatas.getComment_count();
        }
        this.isVideoDetail = isVideoDetail;
        this.articleId = String.valueOf(bean.getArticle().getId());
        setData(datas, bean);
    }

    //设置视频详情数据
    public void setData(CommentRefreshBean data, DraftDetailBean bean) {
        cancelLoadMore();
        if(data != null){
            commentCount = data.getComment_count();
        }
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        List list = new ArrayList();
        if (bean != null && hotCommentNUm > 0) {
            list.add("热门评论");
            for (HotCommentsBean hotCommentsBean :
                    bean.getArticle().getHot_comments()) {
                //打上热门评论标签
                hotCommentsBean.setHotComment(true);
                list.add(hotCommentsBean);
            }
        }
        if (data != null && data.getComments() != null && data.getComments().size() > 0) {
            list.add("最新评论");
            for (HotCommentsBean hotBean :
                    data.getComments()) {
                //最新评论标签
                hotBean.setHotComment(false);
                list.add(hotBean);
            }

        }
        setData(list.size() > 0 ? list : null);
        updateCommentTab();
    }


    //设置评论详情数据
    public void setData(CommentRefreshBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);
        setData(data != null ? data.getComments() : null);
        updateHead();
    }

    //加载更多添加数据
    private void addData(List<HotCommentsBean> data) {
        addData(data, false);
        if (!isVideoDetail) {
            updateHead();
        } else {
            addVideoComment();
        }
        notifyDataSetChanged();
    }

    private boolean noMore(CommentRefreshBean data) {
        return !data.isHas_more();
    }

    private void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    private VideoDetailTextHolder hotHolder, NewsHolder;

    public VideoDetailTextHolder getNewsHolder() {
        return NewsHolder;
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == COMMENT_HOT) {
            return hotHolder = new VideoDetailTextHolder(parent, hotCommentNUm);
        } else if (viewType == COMMENT_NEW) {
            return NewsHolder = new VideoDetailTextHolder(parent, commentCount);
        } else {
            DetailCommentHolder holder = new DetailCommentHolder(UIUtils.inflate(R.layout.module_detail_item_comment, parent, false), articleId, "评论页", mBean);
            holder.setCommentType("最新评论");
            return holder;
        }
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (getData(position) instanceof String && getData(position).toString().equals("热门评论")) {
            return COMMENT_HOT;
        } else if (getData(position) instanceof String && getData(position).toString().equals("最新评论")) {
            return COMMENT_NEW;
        } else if (getData(position) instanceof HotCommentsBean) {
            return COMMENT_CONTENT;
        }
        return super.getAbsItemViewType(position);
    }

    //加载更多成功
    @Override
    public void onLoadMoreSuccess(CommentRefreshBean data, LoadMore loadMore) {
        if (noMore(data)) {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
        if (data != null) {
            addData(data.getComments());
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<CommentRefreshBean> callback) {
        new CommentListTask(callback, is_select_list).setTag(this).exe(articleId, getLastOneTag());
    }

    /**
     * @return 获取最后一次刷新的时间戳
     */
    private Long getLastOneTag() {
        int size = getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = getData(size - count++);
                if (data instanceof HotCommentsBean) {
                    return ((HotCommentsBean) data).getSort_number();
                }
            }
        }
        return null;
    }

    //删除评论，如果清空，则需要删除热门评论/最新评论等标签
    private boolean isHotComment = false;

    public void remove(int position) {
        if (getData(position) instanceof HotCommentsBean) {
            HotCommentsBean bean = (HotCommentsBean) getData(position);
            if (bean.isHotComment()) {
                isHotComment = true;
            } else {
                isHotComment = false;
            }
        }
        getData().remove(cleanPosition(position));
        if (!isVideoDetail) {
            --commentCount;
            updateHead();
        } else {
            deleteVideoComment(isHotComment);
        }
        if (getDataSize() == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRemoved(position);
        }
    }

    //最新评论数
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    private void deleteVideoComment(boolean isHotComment) {
        if (isHotComment) {//热门评论数为0则可以清空第一项标签
            if (hotCommentNUm > 0) {
                --hotCommentNUm;
            }
            if (hotCommentNUm == 0) {
                getData().remove(0);
            }
        } else {
            if (commentCount > 0) {
                --commentCount;
            }
            if (commentCount == 0) {
                if (hotCommentNUm == 0) {
                    getData().remove(0);
                } else {
                    getData().remove(hotCommentNUm + 1);
                }
            }
        }
        SyncCommentNum(hotCommentNUm + commentCount);
        updateCommentTab();
    }

    //添加
    private void addVideoComment() {
        NewsHolder.setText(commentCount + "");
    }

    /**
     * 刷新评论列表文案头部
     */
    private void updateHead() {
        if (getDataSize() == 0) {
            mView.setVisibility(View.GONE);
        } else {
            mView.setVisibility(View.VISIBLE);
            if (commentCount > 99999) {
                mCommentNum.setText("99999+");
            } else {
                mCommentNum.setText(commentCount + "");
            }
        }
    }

    /**
     * 更新tab、热门、最新评论数
     */
    private void updateCommentTab() {
        if (NewsHolder != null && mDatas != null && commentCount > 0) {
            if (commentCount > 99999) {
                NewsHolder.getCommentNumView().setText("99999+");
            } else {
                NewsHolder.setCommentNum(commentCount);
                NewsHolder.getCommentNumView().setText(commentCount + "");
            }
        }
        if (hotHolder != null && mDatas != null && hotCommentNUm > 0) {
            if (hotCommentNUm > 99999) {
                hotHolder.getCommentNumView().setText("99999+");
            } else {
                hotHolder.setCommentNum(hotCommentNUm);
                hotHolder.getCommentNumView().setText(hotCommentNUm + "");
            }
        }
    }

    //删除评论数更新广播
    private void SyncCommentNum(int commentNum) {
        Intent intent = new Intent("sync_comment_num");
        if (commentNum == 0) {
            intent.putExtra("video_comment_title", "评论");
        } else {
            intent.putExtra("video_comment_title", "评论 (" + commentNum + ")");
        }
        LocalBroadcastManager.getInstance(UIUtils.getActivity()).sendBroadcast(intent);
    }
}