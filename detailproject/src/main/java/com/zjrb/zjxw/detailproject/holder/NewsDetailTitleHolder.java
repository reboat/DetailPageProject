package com.zjrb.zjxw.detailproject.holder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

/**
 * 新闻详情页title - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailTitleHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.iv_top_bg)
    ImageView mIvTopBg;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_reporter)
    TextView mTvReporter;
    @BindView(R2.id.tv_channel_name)
    TextView mTvChannelName;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.tv_read_num)
    TextView mTvReadNum;

    public NewsDetailTitleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);

        //顶部焦点图(可以不填写)
        if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getArticle_pic())) {
            mIvTopBg.setVisibility(View.VISIBLE);
            GlideApp.with(mIvTopBg).load(mData.getArticle().getArticle_pic()).apply(AppGlideOptions.bigOptions()).centerCrop().into(mIvTopBg);
        } else {
            mIvTopBg.setVisibility(View.GONE);
        }

        //标题(必填)
        if (mData != null && mData.getArticle() != null && mData.getArticle().getDoc_title() != null) {
            mTvTitle.setText(mData.getArticle().getDoc_title());
        }

        //来源及记者(发稿允许不填写)
        if (mData != null && mData.getArticle() != null && TextUtils.isEmpty(mData.getArticle().getSource()) && TextUtils.isEmpty(mData.getArticle().getAuthor())) {
            mTvReporter.setVisibility(View.GONE);
        } else {
            String source = mData.getArticle().getSource();
            if (!TextUtils.isEmpty(source)) {
                source += " ";
            }
            mTvReporter.setVisibility(View.VISIBLE);
            mTvReporter.setText(source + mData.getArticle().getAuthor());
        }

        //稿件发布时间/栏目名称(发稿允许不填写)
        if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getSource_channel_name()) && !TextUtils.isEmpty(mData.getArticle().getSource_channel_id())) {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1) + "  |");
            mTvChannelName.setText(mData.getArticle().getSource_channel_name());
        } else {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            mTvChannelName.setVisibility(View.GONE);
        }

        if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getRead_count_general())) {
            mTvReadNum.setVisibility(View.VISIBLE);
            mTvReadNum.setText(mData.getArticle().getRead_count_general());
        } else {
            mTvReadNum.setVisibility(View.INVISIBLE);
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.tv_channel_name})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (mData != null && mData.getArticle() != null) {
            new Analytics.AnalyticsBuilder(itemView.getContext(), "800012", "800012")
                    .setEvenName("点击稿件标题下频道名称")
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
                    .setSelfObjectID(mData.getArticle().getId() + "")
                    .build()
                    .send();
        }
        if (view.getId() == R.id.tv_channel_name) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(IKey.CHANNEL_NAME, mData.getArticle().getSource_channel_name());
            bundle.putString(IKey.CHANNEL_ID, mData.getArticle().getSource_channel_id());
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.SUBSCRIBE_PATH);
        }
    }

}