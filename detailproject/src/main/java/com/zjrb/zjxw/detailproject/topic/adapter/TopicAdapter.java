package com.zjrb.zjxw.detailproject.topic.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;
import com.zjrb.zjxw.detailproject.holder.NewsNoCommentTextHolder;
import com.zjrb.zjxw.detailproject.holder.NewsRelateSubjectHolder;
import com.zjrb.zjxw.detailproject.holder.NewsStringTextHolder;
import com.zjrb.zjxw.detailproject.holder.NewsTextMoreHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsActivityMiddleHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsPlaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 话题稿Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class TopicAdapter extends BaseRecyclerAdapter implements OnItemClickListener {
    //顶部标题
    public static final int VIEW_TYPE_TOP = 1;
    //webview
    public static final int VIEW_TYPE_WEB_VIEW = 2;
    //订阅频道
    public static final int VIEW_TYPE_MIDDLE = 3;
    //相关专题
    public static final int VIEW_TYPE_RELATE_SUBJECT = 4;
    //互动评论
    public static final int VIEW_TYPE_COMMENT = 5;
    //互动
    public static final int VIEW_TYPE_STRING = 6;
    //精选
    public static final int VIEW_TYPE_TEXT_INTERACT = 7;
    //暂无评论
    public static final int VIEW_TYPE_NO_COMMENT = 8;

    //订阅
    public static final String PAYLOADS_SUBSCRIBE = "update_subscribe";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;

    private boolean isShowAll; // true：已经显示全部

    private View loadMore;

    public TopicAdapter(DraftDetailBean data, FooterLoadMore loadMore) {
        super(null);
        setData(data);
        setOnItemClickListener(this);
        this.loadMore = loadMore.getLoadMore();
    }


    public void setData(DraftDetailBean data) {
        List list = new ArrayList<>();
        // webView层
        list.add(data);

        setData(list);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_WEB_VIEW:
                return new NewsDetailWebViewHolder(parent);
            case VIEW_TYPE_MIDDLE:
                return new NewsActivityMiddleHolder(parent);
            case VIEW_TYPE_STRING:
                return new NewsStringTextHolder(parent);
            case VIEW_TYPE_RELATE_SUBJECT:
                return new NewsRelateSubjectHolder(parent);
            case VIEW_TYPE_TEXT_INTERACT:
                return new NewsTextMoreHolder(parent, detailBean.getArticle()
                        .isTopic_comment_has_more());
            case VIEW_TYPE_COMMENT:
                return new DetailCommentHolder(parent, String.valueOf(detailBean.getArticle()
                        .getId()));
            case VIEW_TYPE_NO_COMMENT:
                return new NewsNoCommentTextHolder(parent);
            default:
                return new NewsPlaceHolder(parent);
        }
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_WEB_VIEW;
        } else if (getData(position) instanceof DraftDetailBean) {
            mMiddleHolderPosition = position;
            return VIEW_TYPE_MIDDLE;
        } else if (getData(position) instanceof String && !getData(position).toString().equals
                ("精选") && !getData(position).toString().equals
                ("暂无评论")) {
            return VIEW_TYPE_STRING;
        } else if (getData(position) instanceof RelatedSubjectsBean) {
            return VIEW_TYPE_RELATE_SUBJECT;
        } else if (getData(position) instanceof HotCommentsBean) {
            return VIEW_TYPE_COMMENT;
        } else if ((getData(position) instanceof String) && getData(position).toString().equals
                ("精选")) {
            return VIEW_TYPE_TEXT_INTERACT;
        } else if ((getData(position) instanceof String) && getData(position).toString().equals
                ("暂无评论")) {
            loadMore.setVisibility(View.GONE);
            return VIEW_TYPE_NO_COMMENT;
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
    private DraftDetailBean detailBean;

    public void showAll() {
        if (isShowAll) return;
        isShowAll = true;
        int oldSize = datas.size();
        //添加标题头
        detailBean = (DraftDetailBean) datas.get(0);

        //订阅
        if (!TextUtils.isEmpty(detailBean.getArticle().getColumn_name())) {
            datas.add(detailBean);
        }

        //添加相关专题
        List<RelatedSubjectsBean> subjectList = detailBean.getArticle().getRelated_subjects();
        if (subjectList != null && subjectList.size() > 0) {
            datas.add("相关专题");
            datas.addAll(subjectList);
        }

        //添加精选评论
        List<HotCommentsBean> selectCommentsBean = detailBean.getArticle()
                .getTopic_comment_select();
        if (selectCommentsBean != null && selectCommentsBean.size() > 0) {
            datas.add("精选");
            datas.addAll(selectCommentsBean);
        }

        //互动评论
        List<HotCommentsBean> listCommentBean = detailBean.getArticle().getTopic_comment_list();
        if (listCommentBean != null && listCommentBean.size() > 0) {
            datas.add("互动");
            datas.addAll(listCommentBean);
        }

        if (listCommentBean == null || listCommentBean.size() == 0) {
            datas.add("互动");
            datas.add("暂无评论");
        }
        notifyItemRangeChanged(oldSize, datas.size() - oldSize);
    }

    /**
     * 刷新订阅部分item
     */
    public void updateSubscribeInfo() {

        notifyItemChanged(2, PAYLOADS_SUBSCRIBE);
        if (mMiddleHolderPosition != NO_POSITION) {
            notifyItemChanged(mMiddleHolderPosition, PAYLOADS_SUBSCRIBE);
        }

    }

    private Bundle bundle;

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (datas.get(position) instanceof RelatedSubjectsBean) {
            String url = ((RelatedSubjectsBean) datas.get(position)).getUri_scheme();
            if (!TextUtils.isEmpty(url)) {
                Nav.with(UIUtils.getActivity()).to(url);
            }

        } else if (datas.get(position) instanceof String && detailBean.getArticle()
                .isTopic_comment_has_more()) {
            //进入精选列表
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable(IKey.NEWS_DETAIL, detailBean);
            bundle.putBoolean(IKey.IS_SELECT_LIST, true);
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager
                    .COMMENT_ACTIVITY_PATH);
        } else if (datas.get(position) instanceof DraftDetailBean) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon()
                    .appendQueryParameter("id", String.valueOf(((DraftDetailBean) datas.get(position)).getArticle().getColumn_id())).build().toString());
        }
    }

    public void remove(int position) {
        getData().remove(cleanPosition(position));
        notifyItemRemoved(position);
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

    }

}