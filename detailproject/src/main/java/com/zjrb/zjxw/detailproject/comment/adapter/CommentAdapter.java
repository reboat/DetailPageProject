package com.zjrb.zjxw.detailproject.comment.adapter;

import android.view.ViewGroup;

import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.common.manager.APICallManager;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.task.CommentListTask;

import java.util.List;

/**
 * 评论页Adapter
 * Created by wanglinjie.
 * create time:2017/7/19  上午10:14
 */
public class CommentAdapter extends BaseRecyclerAdapter implements LoadMoreListener<CommentRefreshBean> {

    private String articleId;
    private boolean is_select_list;
    private final FooterLoadMore<CommentRefreshBean> mLoadMore;


    public CommentAdapter(CommentRefreshBean datas, ViewGroup parent, String articleId, boolean is_select_list) {
        super(null);
        mLoadMore = new FooterLoadMore<>(parent, this);
        setFooterLoadMore(mLoadMore.itemView);
        this.articleId = articleId;
        this.is_select_list = is_select_list;
        setData(datas);
    }

    public void setData(CommentRefreshBean data) {
        cancelLoadMore();
        mLoadMore.setState(noMore(data) ? LoadMore.TYPE_NO_MORE : LoadMore.TYPE_IDLE);

        setData(data != null ? data.getComments() : null);
    }

    public void addData(List<HotCommentsBean> data) {
        addData(data, false);
        notifyDataSetChanged();
    }

    public boolean noMore(CommentRefreshBean data) {
        return data == null || data.getComments() == null
                || data.getComments().size() < C.PAGE_SIZE;
    }

    public void cancelLoadMore() {
        APICallManager.get().cancel(this);
    }

    @Override
    public DetailCommentHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailCommentHolder(UIUtils.inflate(R.layout.module_detail_item_comment, parent, false), articleId);
    }

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
}