package com.zjrb.zjxw.detailproject.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.global.C;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页(带视频的标题) - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/21  上午10:14
 */
public class NewsDetailTitleVideoHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.iv_top_bg)
    ImageView mIvTopBg;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_reporter)
    TextView mTvReporter;
    @BindView(R2.id.fit_top_layout)
    FitWindowsLinearLayout mFitTopLayout;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.top_container)
    RelativeLayout MtopContainer;

    public NewsDetailTitleVideoHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        //顶部焦点图
        mIvTopBg.setVisibility(View.GONE);

        //标题
        if (mData.getArticle().getList_title() != null && !mData.getArticle().getList_title().isEmpty()) {
            mTvTitle.setText(mData.getArticle().getList_title());
        } else {
            mTvTitle.setVisibility(View.GONE);
        }
        //记者
        if (mData.getArticle().getAuthor() != null && !mData.getArticle().getAuthor().isEmpty()) {
            mTvReporter.setText(mData.getArticle().getAuthor());
        } else {
            mTvReporter.setVisibility(View.GONE);
        }

        //稿件发布时间/栏目名称
        if (mData.getArticle().getColumn_name() != null && !mData.getArticle().getColumn_name().isEmpty()) {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1) + "|");
            mTvColumnName.setText(mData.getArticle().getColumn_name());
        } else {
            mTvTime.setText(TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
            mTvColumnName.setVisibility(View.GONE);
        }
    }

}
