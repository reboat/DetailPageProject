package com.zjrb.zjxw.detailproject.holder;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zjrb.coreprojectlibrary.common.global.C;
import com.zjrb.coreprojectlibrary.ui.widget.fitsys.FitSysWinLinearLayout;
import com.zjrb.coreprojectlibrary.utils.TimeUtils;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.adapter.NewsDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页title - ViewHolder
 *
 * @author a_liYa
 * @date 2017/5/15 14:21.
 */
public class NewsDetailTitleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        View.OnLayoutChangeListener, NewsDetailAdapter.IBindSubscribe {
    public static final int BLUR_RADIUS = 20; // 高斯模糊半径
    @BindView(R2.id.iv_top_bg)
    ImageView mIvTopBg;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_time)
    TextView mTvTime;
    @BindView(R2.id.fit_top_layout)
    FitSysWinLinearLayout mFitTopLayout;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_subscribe)
    TextView mTvSubscribe;
    @BindView(R2.id.fl_column)
    FrameLayout mFlColumn;
    @BindView(R2.id.top_container)
    RelativeLayout mTopContainer;

    public NewsDetailTitleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_news_detail_top, parent, false));
        ButterKnife.bind(this, itemView);
        mTopContainer.addOnLayoutChangeListener(this);
    }

    @Override
    public void bindView() {
        itemView.setClickable(false);
        blurTopBg();
        UIUtils.dispatchApplyWindowInsets(mFitTopLayout);
        mTvTitle.setText(mData.getTitle());
        mTvTime.setText(TimeUtils.getTime(mData.getPublishTime(), C.DATE_FORMAT_1));
        mTvColumnName.setText(mData.getColumnName());
        bindSubscribe();
    }

    private void blurTopBg() {
        Glide.with(itemView.getContext()).load(mData.getTitleBackgroundImage()).asBitmap()
                .transform(new BlurTransformation(UIUtils.getApp(), BLUR_RADIUS,
                        ContextCompat.getColor(itemView.getContext(), R.color.night_mode_mask)))
                .listener(new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean
                            isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target,
                                                   boolean isFromMemoryCache, boolean
                                                           isFirstResource) {
                        mIvTopBg.setBackground(null); // 默认黑色半透明背景去掉
                        return false;
                    }
                })
                .into(mIvTopBg);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        if (v == mTopContainer) { // 通过监听TopContainer的布局变换来改变IvTopBg的宽高
            ViewGroup.LayoutParams lp = mIvTopBg.getLayoutParams();
            if (lp.height != bottom - top) {
                lp.height = bottom - top;
                mIvTopBg.requestLayout();
            }
        }
    }

    @OnClick({R.id.tv_subscribe, R.id.tv_admire, R.id.tv_column_name})
    public void onViewClicked(View view) {
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            switch (view.getId()) {
                case R.id.tv_subscribe:
                    if (mData.isSubscribed()) {
                        callback.onOptCancelSubscribe();
                    } else {
                        callback.onOptSubscribe();
                    }
                    break;
                case R.id.tv_admire:
                    callback.onOptAward();
                    break;
                case R.id.tv_column_name:
                    callback.onOptClickColumn();
                    break;
            }
        }
    }

    @Override
    public void bindSubscribe() {
        mTvSubscribe.setText(0 == mData.getSubscribed() ? "订阅" : "已订阅");
    }
}