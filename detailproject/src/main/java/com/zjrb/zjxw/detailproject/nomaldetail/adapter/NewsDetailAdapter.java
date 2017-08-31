package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerAdapter;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.permission.IPermissionOperate;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailArticleGeneralViewHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailCommentHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailMiddleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedNewsHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedSubjectHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleVideoHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;

import java.util.List;

/**
 * 新闻详情页Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsDetailAdapter extends BaseRecyclerAdapter {
    //顶部标题，视频等
    public static final int VIEW_TYPE_TOP = 1;
    //网页
    public static final int VIEW_TYPE_WEB_VIEW = 2;
    //订阅 频道
    public static final int VIEW_TYPE_MIDDLE = 3;
    //相关推荐
    public static final int VIEW_TYPE_RELATE_SUBJECT = 4;
    //相关新闻
    public static final int VIEW_TYPE_RELATE_NEWS = 5;
    //热门评论
    public static final int VIEW_TYPE_COMMENT = 6;

    //订阅
    public static final String PAYLOADS_SUBSCRIBE = "update_subscribe";
    //点赞
//    public static final String PAYLOADS_FABULOUS = "update_fabulous";
    //恢复
    public static final String PAYLOADS_RESUME = "on_resume";
    //暂停
    public static final String PAYLOADS_PAUSE = "on_pause";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;
    private int mWebViewHolderPosition = NO_POSITION;
    private IPermissionOperate permissionOp;

    /**
     * true:视频
     * false:非视频
     */
    private boolean isVideoType;

    public NewsDetailAdapter(List datas, boolean isVideoType, IPermissionOperate mPermissionOp) {
        super(datas);
        this.isVideoType = isVideoType;
        this.permissionOp = mPermissionOp;
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return isVideoType ?
                    new NewsDetailTitleVideoHolder(parent) : new NewsDetailTitleHolder(parent);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            return new NewsDetailWebViewHolder(parent);
        } else if (viewType == VIEW_TYPE_MIDDLE) {
            return new NewsDetailMiddleHolder(parent, permissionOp);
        } else if (viewType == VIEW_TYPE_RELATE_SUBJECT) {
            return new NewsDetailRelatedSubjectHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_NEWS) {
            return new NewsDetailRelatedNewsHolder(parent);
        } else if (viewType == VIEW_TYPE_COMMENT) {
            return new NewsDetailCommentHolder(parent);
        }
        return null;
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == 1) {
            mWebViewHolderPosition = position;
            return VIEW_TYPE_WEB_VIEW;
        } else if (position == 2) {
            mMiddleHolderPosition = position;
            return VIEW_TYPE_MIDDLE;
        } else if (position == 3) {
            return VIEW_TYPE_RELATE_SUBJECT;
        } else if (position == 4) {
            return VIEW_TYPE_RELATE_NEWS;
        } else if (position == 5) {
            return VIEW_TYPE_COMMENT;
        }
        return super.getAbsItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        boolean isNeedSuperBind = true;
        if (payloads != null && !payloads.isEmpty()) {
            isNeedSuperBind = false;
            for (int i = 0; i < payloads.size(); i++) {
                Object payload = payloads.get(i);
                if (PAYLOADS_SUBSCRIBE.equals(payload)) {
                    if (holder instanceof IBindSubscribe) {
                        ((IBindSubscribe) holder).bindSubscribe();
                    }
                } else if (PAYLOADS_RESUME.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle) holder).onResume();
                    }
                } else if (PAYLOADS_PAUSE.equals(payload)) {
                    if (holder instanceof ILifecycle) {
                        ((ILifecycle) holder).onPause();
                    }
                } else {
                    isNeedSuperBind = true;
                }
            }
        }
        if (isNeedSuperBind)
            super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 显示全部
     */
    public void showAll() {
        int oldSize = datas.size();
        DraftDetailBean detailBean = (DraftDetailBean) datas.get(0);
        datas.add(detailBean);
        //添加相关专题
        List<RelatedSubjectsBean> subjectList = detailBean.getRelated_subjects();
        if (subjectList != null) {
            for (int i = 0; i < subjectList.size(); i++) {
                datas.add(subjectList.get(i));
            }
        }
        //添加相关新闻
        List<RelatedNewsBean> articles = detailBean.getRelated_news();
        if (articles != null) {
            for (int i = 0; i < articles.size(); i++) {
                datas.add(articles.get(i));
            }
        }
        //添加热门评论
        List<HotCommentsBean> hotCommentsBeen = detailBean.getHot_comments();
        if (hotCommentsBeen != null) {
            for (int i = 0; i < hotCommentsBeen.size(); i++) {
                datas.add(hotCommentsBeen.get(i));
            }
        }
        datas.add(null);
        notifyItemRangeChanged(oldSize, datas.size() - oldSize);
    }

    public void updateSubscribeInfo() {
        notifyItemChanged(0, PAYLOADS_SUBSCRIBE);
        if (mMiddleHolderPosition != NO_POSITION) {
            notifyItemChanged(mMiddleHolderPosition, PAYLOADS_SUBSCRIBE);
        }

    }

    public void onWebViewResume() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_RESUME);
        }
    }

    public void onWebViewPause() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_PAUSE);
        }
    }

    /**
     * 订阅
     */
    public interface IBindSubscribe {

        void bindSubscribe();
    }

    public interface ILifecycle {

        void onResume();

        void onPause();
    }

    /**
     * 公共操作回调
     *
     * @author a_liYa
     * @date 2017/5/15 下午8:53.
     */
    public interface CommonOptCallBack {
        /**
         * 订阅操作
         */
        void onOptSubscribe();

        /**
         * WebView加载完毕操作
         */
        void onOptPageFinished();

        /**
         * 点击栏目操作
         */
        void onOptClickColumn();

    }

}