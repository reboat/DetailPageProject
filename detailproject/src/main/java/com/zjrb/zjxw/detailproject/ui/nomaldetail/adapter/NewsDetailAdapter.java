package com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsDetailMiddleHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsDetailTitleHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsDetailWebViewHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsRelateNewsHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsRelateNewsTextHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsRelateSubjectHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsStringClickMoreHolder;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.holder.NewsStringTextHolder;
import com.zjrb.zjxw.detailproject.ui.topic.holder.NewsPlaceHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import java.util.List;

import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;

/**
 * 普通新闻详情页Adapter
 * Created by wanglinjie.
 * create time:2017/7/10  下午5:39
 */
public class NewsDetailAdapter extends BaseRecyclerAdapter implements OnItemClickListener {
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
    //相关新闻无图片
    public static final int VIEW_TYPE_RELATE_NEWS_TEXT = 6;
    //热门评论
    public static final int VIEW_TYPE_COMMENT = 7;
    //字符串
    public static final int VIEW_TYPE_STRING = 8;
    //点击查看更多评论
    public static final int VIEW_TYPE_STRING_CLICK_MORE = 9;
    //占位
    public static final int VIEW_REPLEASE = 10;
    //恢复
    public static final String PAYLOADS_RESUME = "on_resume";
    //暂停
    public static final String PAYLOADS_PAUSE = "on_pause";

    public static final int NO_POSITION = -1;
    private int mWebViewHolderPosition = NO_POSITION;
    private NewsDetailWebViewHolder webviewHolder;
    // true：已经显示全部
    private boolean isShowAll;
    private boolean isRedBoat = false;//是否是红船号的适配器
    private DraftDetailBean detailBean;
    private Bundle bundle;
    private boolean isVideoDetail;

    public NewsDetailAdapter(List datas, boolean isVideoDetail) {
        super(datas);
        if(!datas.isEmpty()){
            detailBean = (DraftDetailBean) datas.get(0);
        }
        this.isVideoDetail = isVideoDetail;
        setOnItemClickListener(this);
    }

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new NewsDetailTitleHolder(parent, isRedBoat,isVideoDetail);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            webviewHolder = new NewsDetailWebViewHolder(parent);
            return webviewHolder;
        } else if (viewType == VIEW_TYPE_MIDDLE) {
            return new NewsDetailMiddleHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_SUBJECT) {
            return new NewsRelateSubjectHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_NEWS) {
            return new NewsRelateNewsHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_NEWS_TEXT) {
            return new NewsRelateNewsTextHolder(parent);
        } else if (viewType == VIEW_TYPE_COMMENT) {
            //热门评论
            DetailCommentHolder holder =  new DetailCommentHolder(parent, String.valueOf(detailBean.getArticle().getId()), detailBean);
            holder.setCommentType("热门评论");
            return holder;
        } else if (viewType == VIEW_TYPE_STRING) {
            return new NewsStringTextHolder(parent);
        } else if (viewType == VIEW_TYPE_STRING_CLICK_MORE) {
            return new NewsStringClickMoreHolder(parent);
        } else if (viewType == VIEW_REPLEASE) {
            return new NewsPlaceHolder(parent);
        }

        return new NewsPlaceHolder(parent);
    }

    @Override
    public int getAbsItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP;
            //直播没有webview
        } else if (position == 1 && getData(position) instanceof DraftDetailBean && !((DraftDetailBean) getData(position)).getArticle().isNative_live()) {
            mWebViewHolderPosition = position;
            return VIEW_TYPE_WEB_VIEW;
        } else if (position == 2 && getData(position) instanceof DraftDetailBean && !TextUtils.isEmpty(((DraftDetailBean) getData(position)).getArticle().getSource_channel_name())) {
            return VIEW_TYPE_MIDDLE;
        } else if (getData(position) instanceof String && !getData(position).toString().equals("点击查看更多评论") && !getData(position).toString().equals("占位")) {
            return VIEW_TYPE_STRING;
        } else if (getData(position) instanceof RelatedSubjectsBean) {
            return VIEW_TYPE_RELATE_SUBJECT;
        } else if (getData(position) instanceof RelatedNewsBean && !TextUtils.isEmpty(((RelatedNewsBean) getData(position)).getPic())) {
            return VIEW_TYPE_RELATE_NEWS;
        } else if (getData(position) instanceof RelatedNewsBean && TextUtils.isEmpty(((RelatedNewsBean) getData(position)).getPic())) {
            return VIEW_TYPE_RELATE_NEWS_TEXT;
        } else if (getData(position) instanceof HotCommentsBean) {
            return VIEW_TYPE_COMMENT;
        } else if (getData(position) instanceof String && getData(position).toString().equals("点击查看更多评论")) {
            return VIEW_TYPE_STRING_CLICK_MORE;
        } else if (getData(position) instanceof String && getData(position).toString().equals("占位")) {
            return VIEW_REPLEASE;
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
                if (PAYLOADS_RESUME.equals(payload)) {
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

    public NewsDetailWebViewHolder getWebViewHolder() {
        return webviewHolder;
    }

    /**
     * 当webview加载出后再显示相关转移等显示全部
     * 对于直播稿，该方法不会调用
     */
    public void showAll() {
        //只加载一次
        if (isShowAll) return;
        isShowAll = true;

        int oldSize = datas.size();
        detailBean = (DraftDetailBean) datas.get(0);
        //添加中间项
        if (!TextUtils.isEmpty(detailBean.getArticle().getSource_channel_name())) {
            datas.add(detailBean);
        }
        //添加相关专题
        List<RelatedSubjectsBean> subjectList = detailBean.getArticle().getRelated_subjects();
        if (subjectList != null && subjectList.size() > 0) {
            datas.add("相关专题");
            datas.addAll(subjectList);
        }

        //添加相关新闻
        List<RelatedNewsBean> articles = detailBean.getArticle().getRelated_news();
        if (articles != null && articles.size() > 0) {
            datas.add("相关新闻");
            datas.addAll(articles);
        }

        //添加热门评论
        List<HotCommentsBean> hotCommentsBeen = detailBean.getArticle().getHot_comments();
        if (hotCommentsBeen != null && hotCommentsBeen.size() > 0 && !isVideoDetail) {
            datas.add("热门评论");
            datas.addAll(hotCommentsBeen);
            datas.add("点击查看更多评论");
        }
        //有相关新闻/相关专题/热门评论
        datas.add("占位");
        notifyItemRangeChanged(oldSize, datas.size() - oldSize);
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
     * 详情页item点击
     *
     * @param itemView .
     * @param position .
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (datas.get(position) instanceof RelatedNewsBean) {
            String url = ((RelatedNewsBean) datas.get(position)).getUri_scheme();
            if (!TextUtils.isEmpty(url)) {
                DataAnalyticsUtils.get().ClickRelatedNews(detailBean, (RelatedNewsBean) datas.get(position));
                Nav.with(UIUtils.getActivity()).to(url);
            }
        } else if (datas.get(position) instanceof RelatedSubjectsBean) {
            String url = ((RelatedSubjectsBean) datas.get(position)).getUri_scheme();
            if (!TextUtils.isEmpty(url)) {
                DataAnalyticsUtils.get().ClickRelatedSpecial(detailBean, (RelatedSubjectsBean) datas.get(position));
                Nav.with(UIUtils.getActivity()).to(url);
            }
        } else if (datas.get(position) instanceof String && datas.get(position).toString().equals("点击查看更多评论")) {
            //进入评论列表页面
            DataAnalyticsUtils.get().ClickMoreComment(detailBean);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable(IKey.NEWS_DETAIL, detailBean);
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
        }

    }

    /**
     * 删除评论
     *
     * @param position
     */
    public void remove(int position) {
        getData().remove(cleanPosition(position));
        notifyItemRemoved(position);
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
         * WebView加载完毕操作
         */
        void onOptPageFinished();

        /**
         * 点击频道操作
         */
        void onOptClickChannel();

        /**
         * 稿件阅读百分比变化
         *
         * @param scale
         */
        void onReadingScaleChange(float scale);

    }

}