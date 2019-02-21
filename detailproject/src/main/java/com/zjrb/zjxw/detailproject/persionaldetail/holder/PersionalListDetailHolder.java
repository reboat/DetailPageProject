package com.zjrb.zjxw.detailproject.persionaldetail.holder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.global.RouteManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 官员列表头部详情holder1
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */
public class PersionalListDetailHolder extends BaseRecyclerViewHolder<OfficalListBean.OfficerListBean> {


    @BindView(R2.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.tv_job)
    TextView mTvJob;

    public PersionalListDetailHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_persional_holder1, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //需要显示占位图
        GlideApp.with(mIvAvatar).load(mData.getPhoto()).placeholder(PH.zheSmall()).centerCrop().apply(AppGlideOptions.smallOptions()).into(mIvAvatar);
        //姓名
        if (mData.getName() != null) {
            mTvName.setText(mData.getName());
        }
        //职位
        if (mData.getTitle() != null) {
            mTvJob.setText(mData.getTitle());
        }
    }

    private Bundle bundle;

    /**
     * 点击跳转到官员详情页
     *
     * @param view
     */
    @OnClick({R2.id.lly_reporter})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.lly_reporter) {
            clickMore(itemView.getContext());
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString(IKey.ID, String.valueOf(mData.getId()));
            Nav.with(itemView.getContext()).setExtras(bundle).toPath(RouteManager.PERSIONAL_DETAIL);
        }
    }

    /**
     * 点击更多埋点
     *
     * @param context
     */
    private void clickMore(Context context) {
        new Analytics.AnalyticsBuilder(context, "800014", "800014", "AppTabClick", false)
                .setEvenName("点击更多查看官员详细信息")
                .setPageType("官员页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "OfficerType")
                        .toString())
                .pageType("官员页面")
                .clickTabName("更多")
                .build()
                .send();
    }
}
