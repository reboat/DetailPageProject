package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.support.annotation.CallSuper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.daily.news.R;
import com.zjrb.daily.news.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;

/**
 * 视频直播holder抽象类
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:16
 */
public abstract class SuperDetailVideoHolder extends BaseRecyclerViewHolder<NativeLiveBean.ListBean> {

    @BindView(R2.id.tv_timeline)
    TextView mTvTimeline;
    @BindView(R2.id.iv_top)
    ImageView ivTop;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.view_stub_video)
    ViewStub mViewStubVideo;
    @BindView(R2.id.view_stub_live)
    ViewStub mViewStubLive;
    @BindView(R2.id.video_container)
    public FrameLayout mVideoContainer;
    @BindView(R2.id.ll_net_hint)
    public LinearLayout llNetHint;
    @BindView(R2.id.tv_net_hint)
    public TextView tvNetHint;

    boolean isLive;

    public SuperDetailVideoHolder(ViewGroup parent) {
        super(parent, R.layout.module_detail_live_video);
        ButterKnife.bind(this, itemView);
        setListener();
    }

    private void setListener() {
        //视频播放
        mViewStubVideo.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                isLive = false;
            }
        });
        //直播播放
        mViewStubLive.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                isLive = true;
            }
        });
    }

    @CallSuper
    @Override
    public void bindView() {
        if (mData == null) return;

        //视频背景图
        GlideApp.with(mIvPicture).load(mData.getVideo_cover()).apply(AppGlideOptions.bigOptions())
                .into(mIvPicture);
        //发布时间
        mTvTimeline.setText(StringUtils.long2String(mData.getCreated_at(), "yyyy.MM.dd ") + mData.getCreated_at_general());
        //置顶
        if (mData.isStick_top()) {
            ivTop.setVisibility(View.VISIBLE);
        } else {
            ivTop.setVisibility(View.GONE);
        }
        //内容
        mTvTitle.setText(mData.getContent());
    }


}
