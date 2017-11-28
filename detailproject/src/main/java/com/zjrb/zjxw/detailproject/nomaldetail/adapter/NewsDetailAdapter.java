package com.zjrb.zjxw.detailproject.nomaldetail.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.base.BaseRecyclerAdapter;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailMiddleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailTitleHolder;
import com.zjrb.zjxw.detailproject.holder.NewsDetailWebViewHolder;
import com.zjrb.zjxw.detailproject.holder.NewsRelateNewsHolder;
import com.zjrb.zjxw.detailproject.holder.NewsRelateNewsTextHolder;
import com.zjrb.zjxw.detailproject.holder.NewsRelateSubjectHolder;
import com.zjrb.zjxw.detailproject.holder.NewsStringClickMoreHolder;
import com.zjrb.zjxw.detailproject.holder.NewsStringTextHolder;
import com.zjrb.zjxw.detailproject.topic.holder.NewsPlaceHolder;

import java.util.List;

import cn.daily.news.analytics.Analytics;

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

    //订阅
    public static final String PAYLOADS_SUBSCRIBE = "update_subscribe";
    //恢复
    public static final String PAYLOADS_RESUME = "on_resume";
    //暂停
    public static final String PAYLOADS_PAUSE = "on_pause";

    public static final int NO_POSITION = -1;
    private int mMiddleHolderPosition = NO_POSITION;
    private int mWebViewHolderPosition = NO_POSITION;
    // true：已经显示全部
    private boolean isShowAll;
    private boolean mHasVideoUrl = false;

    public NewsDetailAdapter(List datas, boolean hasVideoUrl) {
        super(datas);
        mHasVideoUrl = hasVideoUrl;
        setOnItemClickListener(this);
    }

    private NewsDetailWebViewHolder webviewHolder;

    @Override
    public BaseRecyclerViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TOP) {
            return new NewsDetailTitleHolder(parent);
        } else if (viewType == VIEW_TYPE_WEB_VIEW) {
            return webviewHolder = new NewsDetailWebViewHolder(parent, mHasVideoUrl);
        } else if (viewType == VIEW_TYPE_MIDDLE) {
            return new NewsDetailMiddleHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_SUBJECT) {
            return new NewsRelateSubjectHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_NEWS) {
            return new NewsRelateNewsHolder(parent);
        } else if (viewType == VIEW_TYPE_RELATE_NEWS_TEXT) {
            return new NewsRelateNewsTextHolder(parent);
        } else if (viewType == VIEW_TYPE_COMMENT) {
            return new DetailCommentHolder(parent, String.valueOf(detailBean.getArticle().getId()), detailBean);
        } else if (viewType == VIEW_TYPE_STRING) {
            return new NewsStringTextHolder(parent);
        } else if (viewType == VIEW_TYPE_STRING_CLICK_MORE) {
            return new NewsStringClickMoreHolder(parent);
        }
        return new NewsPlaceHolder(parent);
    }

    /**
     * 获取webviewholder
     *
     * @return
     */
    public NewsDetailWebViewHolder getWebViewHolder() {
        return webviewHolder;
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
        } else if (getData(position) instanceof String && !getData(position).toString().equals("点击查看更多评论")) {
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


    private DraftDetailBean detailBean;

    /**
     * 显示全部
     */
    public void showAll() {
        //只加载一次
        if (isShowAll) return;
        isShowAll = true;
        int oldSize = datas.size();
        //添加标题头
        detailBean = (DraftDetailBean) datas.get(0);

        //中间
        datas.add(detailBean);
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
        if (hotCommentsBeen != null && hotCommentsBeen.size() > 0) {
            datas.add("热门评论");
            datas.addAll(hotCommentsBeen);
            datas.add("点击查看更多评论");
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

    private Bundle bundle;

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
                if (detailBean != null && detailBean.getArticle() != null) {
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "800009", "800009")
                            .setEvenName("点击相关新闻列表")
                            .setObjectID(detailBean.getArticle().getMlf_id() + "")
                            .setObjectName(detailBean.getArticle().getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(detailBean.getArticle().getChannel_id())
                            .setClassifyName(detailBean.getArticle().getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", detailBean.getArticle().getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(detailBean.getArticle().getId() + "")
                            .build()
                            .send();
                }
                Nav.with(UIUtils.getActivity()).to(url);
            }
        } else if (datas.get(position) instanceof RelatedSubjectsBean) {
            String url = ((RelatedSubjectsBean) datas.get(position)).getUri_scheme();
            if (!TextUtils.isEmpty(url)) {
                if (detailBean != null && detailBean.getArticle() != null) {
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "800010", "800010")
                            .setEvenName("点击相关专题列表")
                            .setObjectID(detailBean.getArticle().getMlf_id() + "")
                            .setObjectName(detailBean.getArticle().getDoc_title())
                            .setObjectType(ObjectType.NewsType)
                            .setClassifyID(detailBean.getArticle().getChannel_id())
                            .setClassifyName(detailBean.getArticle().getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("customObjectType", "SubjectType")
                                    .put("subject", ((RelatedSubjectsBean) datas.get(position)).getId() + "")
                                    .toString())
                            .setSelfObjectID(detailBean.getArticle().getId() + "")
                            .build()
                            .send();
                }
                Nav.with(UIUtils.getActivity()).to(url);
            }
        } else if (datas.get(position) instanceof String && datas.get(position).toString().equals("点击查看更多评论")) {
            //进入评论列表页面
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

        /**
         * 稿件阅读百分比变化
         *
         * @param scale
         */
        void onReadingScaleChange(float scale);

    }

}