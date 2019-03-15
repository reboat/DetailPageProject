package com.zjrb.zjxw.detailproject.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

/**
 * 新闻详情页中间内容
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailMiddleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnAttachStateChangeListener {
    @BindView(R2.id.tv_channel_name)
    TextView mTvChannelName;
    @BindView(R2.id.ry_channel)
    RelativeLayout mRyChannel;

    /**
     * 分享适配器
     */
    public NewsDetailMiddleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_middle, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.removeOnAttachStateChangeListener(this);
        itemView.addOnAttachStateChangeListener(this);
        //频道名称
        if (!TextUtils.isEmpty(mData.getArticle().getSource_channel_name()) && !TextUtils.isEmpty(mData.getArticle().getSource_channel_id())) {
            itemView.setVisibility(View.VISIBLE);
            mTvChannelName.setText(mData.getArticle().getSource_channel_name());
        } else {
            itemView.setVisibility(View.GONE);
            itemView.setOnClickListener(null);
        }

    }

    /**
     * @param view 频道订阅/栏目  点击
     */
    @OnClick({R2.id.tv_channel_subscribe})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            if (view.getId() == R.id.tv_channel_subscribe) {
                if (mData != null && mData.getArticle() != null) {
                    new Analytics.AnalyticsBuilder(itemView.getContext(), "800012", "800012", "RelatedContentClick", false)
                            .setEvenName("点击正文底部频道名称")
                            .setObjectID(mData.getArticle().getChannel_id())
                            .setObjectName(mData.getArticle().getChannel_name())
                            .setObjectType(ObjectType.ColumnType)
                            .setClassifyID(mData.getArticle().getSource_channel_id())
                            .setClassifyName(mData.getArticle().getSource_channel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mData.getArticle().getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfObjectID(mData.getArticle().getId() + "").newsID(mData.getArticle().getMlf_id() + "")
                            .selfNewsID(mData.getArticle().getId() + "")
                            .newsTitle(mData.getArticle().getDoc_title())
                            .selfChannelID(mData.getArticle().getChannel_id())
                            .channelName(mData.getArticle().getChannel_name())
                            .pageType("新闻详情页")
                            .relatedContentClick("所属频道")
                            .build()
                            .send();
                }
                callback.onOptClickChannel();
            }
        }
    }


    @Override
    public void onViewAttachedToWindow(View v) {
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
    }
}