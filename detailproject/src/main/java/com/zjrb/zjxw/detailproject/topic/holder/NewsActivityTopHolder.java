package com.zjrb.zjxw.detailproject.topic.holder;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 话题详情页顶部holder
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsActivityTopHolder extends BaseRecyclerViewHolder<DraftDetailBean> {

    @BindView(R2.id.iv_cover)
    ImageView mIvCover;
    @BindView(R2.id.fl_cover_alpha_place_holder)
    public View mFlCoverAlphaPlaceHolder;
    @BindView(R2.id.tv_cover_title)
    public TextView mTvCoverTitle;
    @BindView(R2.id.tv_host)
    public TextView mTvHost;
    @BindView(R2.id.tv_guest)
    public TextView mTvGuest;
    @BindView(R2.id.ll_cover_title)
    public LinearLayout mLlFixedTitle;
    @BindView(R2.id.ll_cover)
    FrameLayout mLlCover;

    public NewsActivityTopHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_activity_top, parent, false));
        ButterKnife.bind(this, itemView);
        mColor484848 = ContextCompat.getColor(itemView.getContext(), R.color.color_484848);
    }

    /**
     * 文字颜色渐变
     */
    private int mGradientTxtColor;
    //获取某一个百分比间的颜色,radio取值[0,1]
    private boolean isInitCoverShade = false;
    private int mCoverShadeRedRange;
    private int mCoverShadeBlueRange;
    private int mCoverShadeGreenRange;
    private int mCoverShadeRedStart;
    private int mCoverShadeBlueStart;
    private int mCoverShadeGreenStart;

    private int mColor484848;

    public int getColor(float radio) {
        if (!isInitCoverShade) {
            mCoverShadeRedStart = Color.red(Color.WHITE);
            mCoverShadeBlueStart = Color.blue(Color.WHITE);
            mCoverShadeGreenStart = Color.green(Color.WHITE);
            mCoverShadeRedRange = Color.red(mColor484848) - mCoverShadeRedStart;
            mCoverShadeBlueRange = Color.blue(mColor484848) - mCoverShadeBlueStart;
            mCoverShadeGreenRange = Color.green(mColor484848) - mCoverShadeGreenStart;
            isInitCoverShade = true;
        }

        return Color.argb(255,
                (int) (mCoverShadeRedStart + (mCoverShadeRedRange * radio + 0.5)),
                (int) (mCoverShadeGreenStart + (mCoverShadeGreenRange * radio + 0.5)),
                (int) (mCoverShadeBlueStart + (mCoverShadeBlueRange * radio + 0.5)));
    }

    public void setCoverTxtColor(float radio) {
        mGradientTxtColor = getColor(radio);
        mTvGuest.setTextColor(mGradientTxtColor);
        mTvHost.setTextColor(mGradientTxtColor);
        mTvCoverTitle.setTextColor(mGradientTxtColor);
    }

    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        //题图
        if (!TextUtils.isEmpty(mData.getArticle().getArticle_pic())) {
            mLlCover.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = mLlCover.getLayoutParams();
            params.width = UIUtils.getScreenW();
            params.height = UIUtils.getScreenH();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //TODO WLJ
                params.height -= 60;//UIUtils.getStatusBarHeight();
            }
            mLlCover.setLayoutParams(params);
            GlideApp.with(mIvCover).load(mData.getArticle().getArticle_pic()).centerCrop().into(mIvCover);
        } else {
            mLlCover.setVisibility(View.GONE);
        }

        //标题
        FrameLayout.LayoutParams coverLp = (FrameLayout.LayoutParams) mLlFixedTitle.getLayoutParams();
        coverLp.gravity = Gravity.BOTTOM | Gravity.LEFT;
        mLlFixedTitle.setLayoutParams(coverLp);
        if (mData.getArticle().getList_title() != null) {
            mTvCoverTitle.setText(mData.getArticle().getList_title());
        }

        //主持人
        if (!mData.getArticle().isTopic_hostsEmpty()) {
            mTvHost.setVisibility(View.VISIBLE);
            mTvHost.setText("主持人：");
            for (String host :
                    mData.getArticle().getTopic_hosts()) {
                mTvHost.append(host);
            }
        }

        //嘉宾
        if (!mData.getArticle().isTopic_guestsEmpty()) {
            mTvGuest.setVisibility(View.VISIBLE);
            mTvGuest.setText("嘉宾：");
            for (String guest :
                    mData.getArticle().getTopic_guests()) {
                mTvGuest.append(guest);
            }
        }
    }

}