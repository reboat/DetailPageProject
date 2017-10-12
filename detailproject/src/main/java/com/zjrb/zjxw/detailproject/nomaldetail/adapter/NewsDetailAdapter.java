package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailCommentHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailMiddleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedNewsHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedSubjectHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;

import java.util.List;

/**
 * 普通新闻详情页Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsDetailAdapter extends BaseRecyclerAdapter {
    //顶部标题，视频等
    public static final int VIEW_TYPE_TOP = 1;
    //webview
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
    //恢复
    public static final String PAYLOADS_RESUME = "on_resume";
    //暂停
    public static final String PAYLOADS_PAUSE = "on_pause";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;
    private int mWebViewHolderPosition = NO_POSITION;

    private boolean isShowAll; // true：已经显示全部

    public NewsDetailAdapter(List datas) {
        super(datas);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new NewsDetailTitleHolder(parent);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            return new NewsDetailWebViewHolder(parent);
        } else if (viewType == VIEW_TYPE_MIDDLE) {
            return new NewsDetailMiddleHolder(parent);
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
        if (isShowAll) return;
        isShowAll = true;
        int oldSize = datas.size();
        //添加标题头
        DraftDetailBean detailBean = (DraftDetailBean) datas.get(0);

        //添加相关专题
        List<RelatedSubjectsBean> subjectList = detailBean.getArticle().getRelated_subjects();
        if (subjectList != null && subjectList.size() > 0) {
            datas.addAll(subjectList);
        }

        //添加相关新闻
        List<RelatedNewsBean> articles = detailBean.getArticle().getRelated_news();
        if (articles != null && articles.size() > 0) {
            datas.addAll(articles);
        }
        //添加热门评论
        if(detailBean.getArticle().getHot_comments() != null){
            List<HotCommentsBean> hotCommentsBeen = detailBean.getArticle().getHot_comments().getComments();
            if (hotCommentsBeen != null && hotCommentsBeen.size() > 0) {
                datas.addAll(hotCommentsBeen);
            }
        }

        notifyItemRangeChanged(oldSize, datas.size() - oldSize);
    }

    /**
     * 刷新订阅部分item
     */
    public void updateSubscribeInfo() {
        notifyItemChanged(0, PAYLOADS_SUBSCRIBE);
        if (mMiddleHolderPosition != NO_POSITION) {
            notifyItemChanged(mMiddleHolderPosition, PAYLOADS_SUBSCRIBE);
        }

    }

    /**
     * webview恢复监听
     */
    public void onWebViewResume() {
        if (mWebViewHolderPosition != NO_POSITION) {
            notifyItemChanged(mWebViewHolderPosition, PAYLOADS_RESUME);
        }
    }

    /**
     * webview暂停监听
     */
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

    /**
     * 视频生命周期监听
     */
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

        /**
         * 点击频道操作
         */
        void onOptClickChannel();

    }

}