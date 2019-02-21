package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;

/**
 * 话题详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsActivityMiddleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnAttachStateChangeListener, TopicAdapter.IBindSubscribe {
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_column_subscribe)
    TextView mTvColumnSubscribe;
    @BindView(R2.id.iv_column_logo)
    ImageView mIvColumnLogo;

    public NewsActivityMiddleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_activity_middle_holder_layout, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        //栏目LOGO
        GlideApp.with(mIvColumnLogo).load(mData.getArticle().getColumn_logo()).placeholder(PH.zheSmall()).centerCrop().apply(AppGlideOptions.smallOptions()).into(mIvColumnLogo);

        //栏目名
        if (mData.getArticle().getColumn_name() != null) {
            mTvColumnName.setText(mData.getArticle().getColumn_name());
        }

        //是否已订阅
        if (!mData.getArticle().isColumn_subscribed()) {
            mTvColumnSubscribe.setVisibility(View.VISIBLE);
            mTvColumnSubscribe.setText(itemView.getContext().getString(R.string.module_detail_subscribe));
        } else {
            mTvColumnSubscribe.setVisibility(View.GONE);
        }
    }

    /**
     * @param view 频道订阅  点击
     */
    @OnClick({R2.id.ry_container, R2.id.tv_column_subscribe})
    public void onViewClicked(View view) {
        if (ClickTracker.isDoubleClick()) return;
        TopicAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof TopicAdapter.CommonOptCallBack) {
            callback = (TopicAdapter.CommonOptCallBack) itemView.getContext();
            //栏目订阅
            if (view.getId() == R.id.tv_column_subscribe) {
                if (mData != null && mData.getArticle() != null && !mData.getArticle().isColumn_subscribed()) {
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "A0114", "A0114","SubColumn",false)
                            .setEvenName("点击\"订阅\"")
                            .setObjectID(mData.getArticle().getColumn_id()+"")
                            .setObjectName(mData.getArticle().getColumn_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("customObjectType", "RelatedColumnType")
                                    .toString())
                            .columnID(mData.getArticle().getColumn_id()+"")
                            .columnName(mData.getArticle().getColumn_name())
                            .pageType("订阅首页")
                            .operationType("取消订阅")
                            .build()
                            .send();
                    callback.onOptSubscribe();
                }
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
    }

    /**
     * 局部刷新订阅状态
     */
    @Override
    public void bindSubscribe() {
        mTvColumnSubscribe.setVisibility(View.GONE);
    }
}