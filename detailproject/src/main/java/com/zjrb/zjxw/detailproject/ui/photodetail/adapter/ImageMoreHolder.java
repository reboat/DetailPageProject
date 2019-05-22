package com.zjrb.zjxw.detailproject.ui.photodetail.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;

/**
 * Created by wanglinjie.
 * create time:2019/5/22  上午10:33
 */
public class ImageMoreHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {

    @BindView(R2.id.iv_image)
    ImageView mIvImage;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    private DraftDetailBean bean;

    public ImageMoreHolder(ViewGroup parent, DraftDetailBean bean) {
        super(UIUtils.inflate(R.layout.module_detail_image_more_item, parent, false));
        ButterKnife.bind(this, itemView);
        this.bean = bean;
    }

    @Override
    public void bindView() {
        //无图片时用占位图
        GlideApp.with(mIvImage).load(mData.getPic()).centerCrop().placeholder(PH.zheSmall()).apply(AppGlideOptions.smallOptions()).into(mIvImage);
        //文案
        if (!TextUtils.isEmpty(mData.getTitle())) {
            mTvTitle.setText(mData.getTitle());
            mTvTitle.setSelected(ReadNewsDaoHelper.alreadyRead(mData.getId()));
        }
    }


    @OnClick({R2.id.ry_container})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.ry_container && mData != null && !TextUtils.isEmpty(mData.getUri_scheme())) {
            DataAnalyticsUtils.get().ClickMoreImgItem(mData, bean);
        }
        if (mTvTitle != null) {
            mTvTitle.setSelected(true);
            ReadNewsDaoHelper.addAlreadyRead(mData.getId());
        }
        Nav.with(UIUtils.getActivity()).to(mData.getUri_scheme());
    }

}

