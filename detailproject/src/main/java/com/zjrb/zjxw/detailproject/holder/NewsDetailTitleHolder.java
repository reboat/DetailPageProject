package com.zjrb.zjxw.detailproject.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.C;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R2.id.fit_top_layout)
    FitWindowsLinearLayout mFitTopLayout;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.top_container)
    RelativeLayout mTopContainer;
    @BindView(R2.id.tv_time)
    TextView mTvTime;

    public NewsDetailTitleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);

        //顶部焦点图(可以不填写)
        if (mData.getArticle().getArticle_pic() == null || mData.getArticle().getArticle_pic().isEmpty()) {
            mIvTopBg.setVisibility(View.GONE);
        } else {
            GlideApp.with(mIvTopBg).load(mData.getArticle().getArticle_pic()).centerCrop().into(mIvTopBg);
        }

        //标题(必填)
        if (mData.getArticle().getList_title() != null) {
            mTvTitle.setText(mData.getArticle().getList_title());
        }

        //记者(发稿允许不填写)
        if (mData.getArticle().getAuthor() != null && !mData.getArticle().getAuthor().isEmpty()) {
            mTvReporter.setText(mData.getArticle().getAuthor());
        } else {
            mTvReporter.setVisibility(View.GONE);
        }

        //稿件发布时间/栏目名称(发稿允许不填写)
        if (mData.getArticle().getColumn_name() != null && !mData.getArticle().getColumn_name().isEmpty()) {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1) + "|");
            mTvColumnName.setText(mData.getArticle().getColumn_name());
        } else {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            mTvColumnName.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.tv_column_name})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.tv_column_name) {
            //TODO  WLJ  进入频道列表页面
        }
    }

}