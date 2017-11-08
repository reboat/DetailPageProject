package com.zjrb.zjxw.detailproject.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 相关新闻holder
 * Created by wanglinjie.
 * create time:2017/10/26  上午8:58
 */

public class NewsRelateNewsHolder extends BaseRecyclerViewHolder<RelatedNewsBean> {
    @BindView(R2.id.iv_pic)
    ImageView mImg;
    @BindView(R2.id.tv_title)
    TextView mTitle;

    public NewsRelateNewsHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_subject, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (!TextUtils.isEmpty(mData.getPic())) {
            mImg.setVisibility(View.VISIBLE);
            GlideApp.with(mImg).load(mData.getPic()).placeholder(PH.zheSmall()).centerCrop().apply(AppGlideOptions.smallOptions()).into(mImg);
        } else {
            mImg.setVisibility(View.GONE);
        }

        if (mData.getTitle() != null) {
            mTitle.setText(mData.getTitle());
        }
    }

}
