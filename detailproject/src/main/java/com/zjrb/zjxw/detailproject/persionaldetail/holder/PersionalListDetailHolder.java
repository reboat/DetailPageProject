package com.zjrb.zjxw.detailproject.persionaldetail.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员列表详情holder1
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class PersionalListDetailHolder extends BaseRecyclerViewHolder<OfficalArticlesBean> {


    @BindView(R2.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.tv_job)
    TextView mTvJob;
    @BindView(R2.id.ly_name)
    LinearLayout mLyName;
    @BindView(R2.id.lly_reporter)
    RelativeLayout mLlyReporter;

    public PersionalListDetailHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_persional_holder1, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        GlideApp.with(mIvAvatar).load(mData.getPhoto()).centerCrop().into(mIvAvatar);
        if(mData.getName() != null){
            mTvName.setText(mData.getName());
        }
        if(mData.getJob() != null){
            mTvJob.setText(mData.getJob());
        }
    }
}
