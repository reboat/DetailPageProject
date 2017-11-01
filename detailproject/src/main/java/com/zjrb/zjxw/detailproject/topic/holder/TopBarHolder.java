package com.zjrb.zjxw.detailproject.topic.holder;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.topic.utils.ArgbUtils;
import com.zjrb.zjxw.detailproject.topic.utils.AttrUtils;
import com.zjrb.zjxw.detailproject.topic.widget.ColorImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public void setData(DraftDetailBean data) {
        mArticle = data != null ? data.getArticle() : null;
    }

    @OnClick({R2.id.iv_top_back, R2.id.iv_top_share})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_top_back) {
            if (view.getContext() instanceof Activity) {
                ((Activity) view.getContext()).finish();
            }
        } else if (view.getId() == R.id.iv_top_share) {
            if (mArticle != null) {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setImgUri(mArticle.getFirstPic())
                        .setTextContent(mArticle.getSummary())
                        .setTitle(mArticle.getDoc_title())
                        .setTargetUrl(mArticle.getUrl()));
            }
        }
    }

    public void setFraction(float fraction) {
        itemView.setBackgroundColor(ArgbUtils.evaluate(fraction, Color.TRANSPARENT,
                AttrUtils.getColor(getTheme(), R.attr.module_detail_color_ffffff_202124)));
        mTopLine.setAlpha(fraction);
        mIvTopBack.setFraction(fraction);
        mIvTopShare.setFraction(fraction);
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
