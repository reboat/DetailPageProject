package com.zjrb.zjxw.detailproject.holder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页 - top视频 逻辑处理
 * Created by wanglinjie.
 * create time:2017/7/25  上午10:31
 */
//TODO  WLJ 未实现
public class NewsDetailVideoHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements View
        .OnAttachStateChangeListener {

    @BindView(R2.id.iv_image)
    ImageView mIvImage;
    @BindView(R2.id.iv_type_video)
    ImageView mIvTypeVideo;

    private ViewGroup mVideoParent;
    private DraftDetailBean mData;

    public NewsDetailVideoHolder(ViewGroup videoParent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_top_video, videoParent, false));
        ButterKnife.bind(this, itemView);
//        VideoManager.addVideoOnAttachStateChangeListener(mVideoParent, this);
    }

    public void bind(DraftDetailBean data) {
        mData = data;
        GlideApp.with((Activity) mVideoParent.getContext()).load(mData.getArticle_pic())
                .placeholder(R.drawable.module_detail_load_error)
                .error(R.drawable.module_detail_load_error)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // 缓存原始资源，解决Gif加载慢
                .into(mIvImage);
    }


    @OnClick({})
    public void onClick(View v) {
        if (v == mVideoParent) {
//            VideoManager.get().play(mVideoParent, mData.getVideo_url(),
//                    ExtraEntity.createBuild()
//                            .setTitle(mData.getList_title())
//                            .setDuration(mData.getVideo_duration())
//                            .setSize(mData.getVideoSize())
//                            .setShareUrl(mData.getUri_scheme())
//                            .setId((int) mData.getId())
//                            .build()
//            );
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

    @Override
    public void bindView() {

    }
}
