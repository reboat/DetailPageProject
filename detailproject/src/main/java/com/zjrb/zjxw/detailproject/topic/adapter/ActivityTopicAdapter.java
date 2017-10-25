package com.zjrb.zjxw.detailproject.topic.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedSubjectHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsActivityCommentHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsActivityHotCommentHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsActivityMiddleHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsActivityTopHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsPlaceHolder;

import java.util.List;

/**
 * 话题稿Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class ActivityTopicAdapter extends BaseRecyclerAdapter {
    //顶部标题
    public static final int VIEW_TYPE_TOP = 1;
    //webview
    public static final int VIEW_TYPE_WEB_VIEW = 2;
    //订阅频道
    public static final int VIEW_TYPE_MIDDLE = 3;
    //相关专题
    public static final int VIEW_TYPE_RELATE_SUBJECT = 4;
    //评论精选
    public static final int VIEW_TYPE_BEAUT_COMMENT = 5;
    //互动评论
    public static final int VIEW_TYPE_COMMENT = 6;

    //订阅
    public static final String PAYLOADS_SUBSCRIBE = "update_subscribe";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;

    private boolean isShowAll; // true：已经显示全部

    public ActivityTopicAdapter(List datas) {
        super(datas);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TOP:
                return new NewsActivityTopHolder(parent);
            case VIEW_TYPE_WEB_VIEW:
                return new NewsDetailWebViewHolder(parent);
            case VIEW_TYPE_MIDDLE:
                return new NewsActivityMiddleHolder(parent);
            case VIEW_TYPE_RELATE_SUBJECT:
                return new NewsDetailRelatedSubjectHolder(parent);
            case VIEW_TYPE_BEAUT_COMMENT:
                return new NewsActivityHotCommentHolder(parent);
            case VIEW_TYPE_COMMENT:
                return new NewsActivityCommentHolder(parent);
            default:
                return new NewsPlaceHolder(parent);
        }
    }

    @Override
    public int getAbsItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_TOP;
            case 1:
                return VIEW_TYPE_WEB_VIEW;
            case 2:
                mMiddleHolderPosition = position;
                return VIEW_TYPE_MIDDLE;
            case 3:
                return VIEW_TYPE_RELATE_SUBJECT;
            case 4:
                return VIEW_TYPE_BEAUT_COMMENT;
            default:
                if (position == 5) {
                    return VIEW_TYPE_COMMENT;
                }
                break;

        }
        return 0;
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

        //订阅
        if (!TextUtils.isEmpty(detailBean.getArticle().getColumn_name())) {
            datas.add(detailBean);
        }
        //添加相关专题
        List<RelatedSubjectsBean> subjectList = detailBean.getArticle().getRelated_subjects();
        if (subjectList != null && subjectList.size() > 0) {
            datas.add(detailBean);
        }

        //添加相关新闻
        List<RelatedNewsBean> articles = detailBean.getArticle().getRelated_news();
        if (articles != null && articles.size() > 0) {
            datas.add(detailBean);
        }
        //添加精选评论
        if (detailBean.getArticle().getTopic_comment_select() != null) {
            List<HotCommentsBean> selectBeanList = detailBean.getArticle().getTopic_comment_select();
            if (selectBeanList != null && selectBeanList.size() > 0) {
                datas.add(detailBean);
            }
        }
        //添加互动评论
        if (detailBean.getArticle().getTopic_comment_list() != null) {
            List<HotCommentsBean> commentBeenList = detailBean.getArticle().getTopic_comment_list();
            if (commentBeenList != null && commentBeenList.size() > 0) {
                datas.add(detailBean);
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
     * 订阅
     */
    public interface IBindSubscribe {

        void bindSubscribe();
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