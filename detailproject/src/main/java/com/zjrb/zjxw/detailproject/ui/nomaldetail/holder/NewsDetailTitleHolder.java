package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.uimode.widget.MaskImageView;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;

import static com.zjrb.zjxw.detailproject.utils.global.C.DATE_FORMAT_2;

/**
 * 新闻详情页title - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailTitleHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.iv_top_bg)
    MaskImageView mIvTopBg;
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
    @BindView(R2.id.tv_summary)
    TextView tvSummary;

    private boolean isRedBoat;//是否是红船号
    private boolean isVideoDetail;//是否是视频稿


    public NewsDetailTitleHolder(ViewGroup parent, boolean isRedBoat, boolean isVideoDetail) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
        this.isRedBoat = isRedBoat;
        this.isVideoDetail = isVideoDetail;
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
        if (mData != null && mData.getArticle() != null) {
            if (mData.getArticle().isNative_live()) {
                if (mData.getArticle().getNative_live_info() != null && !TextUtils.isEmpty(mData.getArticle().getNative_live_info().getTitle())) {
                    mTvTitle.setVisibility(View.VISIBLE);
                    mTvTitle.setText(mData.getArticle().getNative_live_info().getTitle());
                } else {
                    mTvTitle.setVisibility(View.GONE);
                }
            } else {
                if (!TextUtils.isEmpty(mData.getArticle().getDoc_title())) {
                    mTvTitle.setVisibility(View.VISIBLE);
                    mTvTitle.setText(mData.getArticle().getDoc_title());
                } else {
                    mTvTitle.setVisibility(View.GONE);
                }
            }
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
        //红船号稿件
        if (isRedBoat) {
            if (mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getSource())) {
                mTvReporter.setVisibility(View.VISIBLE);
                mTvReporter.setText(mData.getArticle().getSource());
            } else mTvReporter.setVisibility(View.GONE);
        } else {
            //来源及记者(发稿允许不填写)
            if (mData != null && mData.getArticle() != null) {
                mTvReporter.setVisibility(View.VISIBLE);
                if (isVideoDetail) {
                    if (mData.getArticle().getNative_live_info() != null && !TextUtils.isEmpty(mData.getArticle().getNative_live_info().getReporter())) {
                        mTvReporter.setText(mData.getArticle().getNative_live_info().getReporter());
                    } else {
                        mTvReporter.setVisibility(View.GONE);
                    }
                } else {
                    String source = mData.getArticle().getSource();
                    String author = mData.getArticle().getAuthor();
                    if (TextUtils.isEmpty(source) && TextUtils.isEmpty(author)) {
                        mTvReporter.setVisibility(View.GONE);
                    } else {
                        if (!TextUtils.isEmpty(source)) {
                            source += " ";
                        }
                        mTvReporter.setText((source == null ? "" : source) + mData.getArticle().getAuthor());
                    }
                }
            } else {
                mTvReporter.setVisibility(View.GONE);
            }
        }

        //频道显示
        if (!isRedBoat && mData != null && mData.getArticle() != null) {
            if (isVideoDetail) {
                mTvTime.setText(TimeUtils.getTime(mData.getArticle().getLive_start(), DATE_FORMAT_2) + " - " + TimeUtils.getTime(mData.getArticle().getLive_end(), DATE_FORMAT_2));
            } else {
                mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            }
            if (!TextUtils.isEmpty(mData.getArticle().getSource_channel_name())) {
                mTvChannelName.setVisibility(View.VISIBLE);
                mTvChannelName.setText(mData.getArticle().getSource_channel_name());
            } else {
                mTvChannelName.setVisibility(View.GONE);
            }
        } else {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            mTvChannelName.setVisibility(View.GONE);
        }

        //阅读数
        if (!isRedBoat && mData != null && mData.getArticle() != null && !TextUtils.isEmpty(mData.getArticle().getRead_count_general())) {
            if (isVideoDetail) {
                mTvReadNum.setVisibility(View.INVISIBLE);
            } else {
                mTvReadNum.setVisibility(View.VISIBLE);
                mTvReadNum.setText(mData.getArticle().getRead_count_general());
            }
        } else {
            mTvReadNum.setVisibility(View.INVISIBLE);
        }

        //简介
        if (mData != null && mData.getArticle() != null && mData.getArticle().getNative_live_info() != null && !TextUtils.isEmpty(mData.getArticle().getNative_live_info().getIntro())) {
            tvSummary.setVisibility(View.VISIBLE);
            tvSummary.setText(mData.getArticle().getNative_live_info().getIntro());
        } else {
            tvSummary.setVisibility(View.GONE);
        }
    }

    private Bundle bundle;

    @OnClick({R2.id.tv_channel_name})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (mData != null && mData.getArticle() != null) {
            DataAnalyticsUtils.get().ClickMiddleChannel(mData);
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