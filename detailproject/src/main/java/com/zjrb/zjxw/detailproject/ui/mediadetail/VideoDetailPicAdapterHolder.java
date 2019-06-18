package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;

/**
 * 直播间多图holder
 * Created by wanglinjie.
 * create time:2019/3/26  下午3:54
 */
public class VideoDetailPicAdapterHolder extends BaseRecyclerViewHolder<String> {

    @BindView(R2.id.iv_image)
    ImageView mIvImage;

    public VideoDetailPicAdapterHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_video_img_preview, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        //无图片时用占位图
        GlideApp.with(mIvImage).load(mData).placeholder(R.mipmap.news_place_holder_zhe_small).error(R.mipmap.news_place_holder_zhe_small).centerCrop().into(mIvImage);
    }

}
