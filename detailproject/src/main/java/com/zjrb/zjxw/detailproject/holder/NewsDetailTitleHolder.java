package com.zjrb.zjxw.detailproject.holder;

import android.support.percent.PercentFrameLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.common.global.C;
import com.zjrb.coreprojectlibrary.utils.TimeUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页title - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailTitleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnLayoutChangeListener {
    @BindView(R2.id.iv_top_bg)
    ImageView mIvTopBg;
    @BindView(R2.id.pf_title_pic)
    PercentFrameLayout mPfTitlePic;
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

    public NewsDetailTitleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top, parent, false));
        ButterKnife.bind(this, itemView);
        mTopContainer.addOnLayoutChangeListener(this);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        if (mData.getArticle_pic() == null || !mData.getArticle_pic().isEmpty()) {
            mPfTitlePic.setVisibility(View.GONE);
        } else {
            GlideApp.with(mIvTopBg).load(mData.getArticle_pic()).into(mIvTopBg);
        }
        mTvTitle.setText(mData.getList_title());
        mTvReporter.setText(TimeUtils.getTime(mData.getPublished_at(), C.DATE_FORMAT_1));
        mTvColumnName.setText(mData.getColumn_name());
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        if (v == mTopContainer) {
            ViewGroup.LayoutParams lp = mIvTopBg.getLayoutParams();
            if (lp.height != bottom - top) {
                lp.height = bottom - top;
                mIvTopBg.requestLayout();
            }
        }
    }

}