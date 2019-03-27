package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliya.view.ratio.RatioRelativeLayout;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.ImageBrowseActivity;
import port.SerializableHashMap;

/**
 * 直播间多图holder
 * Created by wanglinjie.
 * create time:2019/3/26  下午3:54
 */
public class VideoDetailPicHolder extends BaseRecyclerViewHolder<NativeLiveBean.ListBean> {

    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.iv_top)
    ImageView ivTop;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_img1)
    ImageView ivImg1;
    @BindView(R2.id.iv_img2)
    ImageView ivImg2;
    @BindView(R2.id.iv_img3)
    ImageView ivImg3;
    @BindView(R2.id.ly_imgs)
    LinearLayout lyImgs;
    @BindView(R2.id.rv_img1)
    RatioRelativeLayout ratioRelativeLayout1;
    @BindView(R2.id.rv_img2)
    RatioRelativeLayout ratioRelativeLayout2;
    private Bundle bundle;
    private SerializableHashMap map;

    public VideoDetailPicHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_video_live_holder, parent, false));
        ButterKnife.bind(this, itemView);
    }


    @Override
    public void bindView() {
        tvTime.setText(StringUtils.long2String(mData.getCreated_at(), "yyyy.MM.dd"));
        if (mData.isStick_top()) {
            ivTop.setVisibility(View.VISIBLE);
        } else {
            ivTop.setVisibility(View.GONE);
        }
        tvTitle.setText(mData.getContent());
        if (mData.getPics() != null && mData.getPics().size() > 0) {
            lyImgs.setVisibility(View.VISIBLE);
            for (int i = 0; i < mData.getPics().size(); i++) {
                if (i == 0) {
                    ratioRelativeLayout1.setVisibility(View.VISIBLE);
                    GlideApp.with(ivImg1).load(mData.getPics().get(0)).apply(AppGlideOptions.smallOptions())
                            .into(ivImg1);
                } else if (i == 2) {
                    ivImg2.setVisibility(View.VISIBLE);
                    GlideApp.with(ivImg2).load(mData.getPics().get(1)).apply(AppGlideOptions.smallOptions())
                            .into(ivImg2);
                } else {
                    ratioRelativeLayout2.setVisibility(View.VISIBLE);
                    GlideApp.with(ivImg3).load(mData.getPics().get(2)).apply(AppGlideOptions.smallOptions())
                            .into(ivImg3);
                }
            }
        } else {
            lyImgs.setVisibility(View.GONE);
        }

    }

    //点击图片预览
    @OnClick({R2.id.tv_channel_subscribe})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        int index;
        if (view.getId() == R.id.iv_img1) {
            index = 0;
        } else if (view.getId() == R.id.iv_img2) {
            index = 1;
        } else {
            index = 2;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putStringArray(ImageBrowseActivity.EXTRA_IMAGE_URLS, (String[]) mData.getPics().toArray());
        bundle.putInt(ImageBrowseActivity.EXTRA_IMAGE_INDEX, index);
        if (map != null && map.getMap() != null && map.getMap().size() > index && !map.getMap().get(index)) {
            map.getMap().put(index, true);
        }
        bundle.putSerializable(ImageBrowseActivity.EXTRA_IMAGE_SRCS, map);

        Nav.with(itemView.getContext()).setExtras(bundle).toPath(RouteManager.IMAGE_BROWSE_ACTIVITY);
    }


}
