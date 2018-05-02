package com.zjrb.zjxw.detailproject.topic.holder;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.ui.widget.CircleImageView;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.topic.utils.ArgbUtils;
import com.zjrb.zjxw.detailproject.topic.utils.AttrUtils;
import com.zjrb.zjxw.detailproject.topic.widget.ColorImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * top bar view holder
 *
 * @author a_liYa
 * @date 2017/11/1 09:46.
 */
public class TopBarHolder implements ColorImageView.OnUiModeChangeListener {

    @BindView(R2.id.iv_top_back)
    ColorImageView mIvTopBack;
    @BindView(R2.id.iv_top_share)
    ColorImageView mIvTopShare;
    @BindView(R2.id.top_line)
    View mTopLine;
    @BindView(R2.id.frl_title)
    FitWindowsRelativeLayout mRelativeLayout;
    @BindView(R2.id.iv_top_subscribe_icon)
    CircleImageView mIvIcon;
    @BindView(R2.id.tv_top_bar_title)
    TextView tvTitle;
    @BindView(R2.id.tv_top_bar_subscribe_text)
    TextView tvSubscribe;

    public View itemView;
    private DraftDetailBean.ArticleBean mArticle;

    public TopBarHolder(View itemView) {
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
        mIvTopBack.setAttrId(R.attr.module_detail_color_ffffff_7a7b7d,
                R.attr.module_detail_color_484848_7a7b7d);
        mIvTopShare.setAttrId(R.attr.module_detail_color_ffffff_7a7b7d,
                R.attr.module_detail_color_484848_7a7b7d);
        mIvTopBack.setOnUiModeChangeListener(this);
    }

    /**
     * 获取订阅控件
     * @return
     */
    public TextView getSubscribe() {
        return tvSubscribe;
    }

    /**
     * 订阅布局
     * @return
     */
    public FitWindowsRelativeLayout getSubscribeRelativeLayout(){
        return mRelativeLayout;
    }

    public void setData(DraftDetailBean data) {
        mArticle = data != null ? data.getArticle() : null;
        //中间栏目布局处理
        if (mArticle != null) {
            if (!TextUtils.isEmpty(mArticle.getColumn_name())) {
                //栏目名称
                mRelativeLayout.setVisibility(View.VISIBLE);
                tvTitle.setText(mArticle.getColumn_name());
                //栏目头像
                if (!TextUtils.isEmpty(mArticle.getColumn_logo())) {
                    GlideApp.with(mIvIcon).load(mArticle.getColumn_logo()).centerCrop().into(mIvIcon);
                }
                //订阅状态 采用select
                if (mArticle.isColumn_subscribed()) {
                    tvSubscribe.setText("已订阅");
                    tvSubscribe.setSelected(true);
                } else {
                    tvSubscribe.setText("+订阅");
                    tvSubscribe.setSelected(false);
                }
            } else {
                mRelativeLayout.setVisibility(View.GONE);
            }
        }
    }

    public void setFraction(float fraction) {
        itemView.setBackgroundColor(ArgbUtils.evaluate(fraction, Color.TRANSPARENT,
                AttrUtils.getColor(getTheme(), R.attr.module_detail_color_ffffff_202124)));
        mTopLine.setAlpha(fraction);
        mIvTopBack.setFraction(fraction);
        mIvTopShare.setFraction(fraction);
        mRelativeLayout.setAlpha(fraction);
        mIvIcon.setAlpha(fraction);
        tvTitle.setAlpha(fraction);
        tvSubscribe.setAlpha(fraction);
    }

    private Resources.Theme getTheme() {
        return itemView.getContext().getTheme();
    }

    public int getHeight() {
        return itemView.getHeight();
    }

    public void setShareVisible(boolean visible) {
        mIvTopShare.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUiModeChange(float fraction) {
        setFraction(fraction);
    }
}
