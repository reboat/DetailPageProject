package com.zjrb.zjxw.detailproject.holder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjrb.zjxw.detailproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页 - top视频 逻辑处理
 *
 * @author a_liYa
 * @date 2017/5/17 17:20.
 */
public class NewsDetailVideoHolder implements View.OnClickListener, View
        .OnAttachStateChangeListener {

    @BindView(R.id.iv_image)
    ImageView mIvImage;
    @BindView(R.id.iv_type_video)
    ImageView mIvTypeVideo;

    private ViewGroup mVideoParent;
    private DraftDetailBean mData;

    public NewsDetailVideoHolder(ViewGroup videoParent) {
        mVideoParent = videoParent;
        mVideoParent.setOnClickListener(this);
        ButterKnife.bind(this, videoParent);

        VideoManager.addVideoOnAttachStateChangeListener(mVideoParent, this);
    }
    public void bind(DraftDetailBean data) {
        mData = data;
        Glide.with((Activity) mVideoParent.getContext()).load(mData.getTitleBackgroundImage())
                .crossFade()
                .placeholder(R.drawable.ic_load_error)
                .error(R.drawable.ic_load_error)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // 缓存原始资源，解决Gif加载慢
                .into(mIvImage);
    }


    @Override
    public void onClick(View v) {
        if (v == mVideoParent) {
            VideoManager.get().play(mVideoParent, mData.getLinkUrl(),
                    ExtraEntity.createBuild()
                            .setTitle(mData.getTitle())
                            .setDuration(mData.getVideoDuration())
                            .setSize(mData.getVideoSize())
                            .setShareUrl(mData.getShareUrl())
                            .setId((int) mData.getId())
                            .setSummary(mData.getSummary())
                            .build()
            );
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        mIvTypeVideo.setVisibility(View.GONE);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        mIvTypeVideo.setVisibility(View.VISIBLE);
    }
}
