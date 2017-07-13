package com.zjrb.zjxw.detailproject.photodetail;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zjrb.coreprojectlibrary.common.base.BaseFragment;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.common.global.IKey;
import com.zjrb.coreprojectlibrary.common.listener.IOnImageTapListener;
import com.zjrb.coreprojectlibrary.ui.widget.load.LoadingIndicatorView;
import com.zjrb.coreprojectlibrary.ui.widget.photoview.PhotoViewAttacher;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片详情预览
 *
 * @author a_liYa
 * @date 16/10/23 下午10:23.
 */
public class ImagePreviewFragment extends BaseFragment implements PhotoViewAttacher
        .OnViewTapListener {
    @BindView(R2.id.iv_pre_image)
    ImageView mIvPreImage;
    @BindView(R2.id.liv_loading)
    LoadingIndicatorView mLivLoading;

    private PhotoViewAttacher mAttacher;
    private String mUrl;
    private boolean isTapClose = false;

    private static final String TAP_CLOSE = "tap_close";

    /**
     * 创建实例，轻触图片不关闭页面
     *
     * @param url 图片Url
     * @return 实例对象
     */
    public static ImagePreviewFragment newInstance(String url) {
        return newInstance(url, false);
    }

    /**
     * 创建实例
     *
     * @param url        图片Url
     * @param isTapClose 是否轻触关闭页面 true：轻触关闭
     * @return 实例对象
     */
    public static ImagePreviewFragment newInstance(String url, boolean isTapClose) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();

        Bundle args = new Bundle();
        args.putString(IKey.FRAGMENT_ARGS, url);
        args.putBoolean(TAP_CLOSE, isTapClose);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(IKey.FRAGMENT_ARGS);
            isTapClose = getArguments().getBoolean(TAP_CLOSE);
        }
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(R.layout.module_detail_image_preview);
        ButterKnife.bind(this, getContentView());
        init();

    }

    private void init() {
        mAttacher = new PhotoViewAttacher(mIvPreImage);
        mAttacher.setOnViewTapListener(this);
        if (isTapClose) {
            mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    getActivity().onBackPressed();
                }
            });
        }
        mLivLoading.setVisibility(View.VISIBLE);

        GlideApp.with(ImagePreviewFragment.this)
                .load(mUrl)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // 缓存原始资源，解决Gif加载慢
                .listener(new RequestListener() {

                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean
                            isFirstResource) {
                        showShortToast("图片加载失败!");
                        mLivLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target,
                                                   boolean isFromMemoryCache, boolean
                                                           isFirstResource) {
                        mLivLoading.setVisibility(View.GONE);
                        mAttacher.update();
                        return false;
                    }
                }).into(mIvPreImage);
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (getActivity() instanceof IOnImageTapListener) {
            ((IOnImageTapListener) getActivity()).onImageTap(view);
        }
    }
}
