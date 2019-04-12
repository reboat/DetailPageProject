package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.uimode.widget.MaskImageView;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.TimeUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.glide.AppGlideOptions;

/**
 * 类描述：红船号详情页title - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */

public class RedBoatDetailTitleHolder extends BaseRecyclerViewHolder<DraftDetailBean> {
    @BindView(R2.id.iv_top_bg)
    MaskImageView ivTopBg;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_reporter)
    TextView tvReporter;
    @BindView(R2.id.tv_time)
    TextView tvTime;

    public RedBoatDetailTitleHolder(@NonNull ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_redboat_title_top, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (mData == null) return;
        DraftDetailBean.ArticleBean articleBean = mData.getArticle();
        if (articleBean == null) return;

        if (!TextUtils.isEmpty(articleBean.getArticle_pic())) {
            ivTopBg.setVisibility(View.VISIBLE);
            GlideApp.with(ivTopBg).load(articleBean.getArticle_pic()).apply(
                    AppGlideOptions.bigOptions()).centerCrop().into(ivTopBg);
        }
        tvTitle.setText("" + articleBean.getDoc_title());
        tvReporter.setText("" + articleBean.getSource());
        tvTime.setText("" + TimeUtils.getTime(mData.getArticle().getPublished_at(), C.DATE_FORMAT_1));
    }
}