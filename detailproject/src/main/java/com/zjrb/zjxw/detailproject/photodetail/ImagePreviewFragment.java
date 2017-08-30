package com.zjrb.zjxw.detailproject.photodetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zjrb.coreprojectlibrary.common.base.BaseFragment;
import com.zjrb.coreprojectlibrary.common.glide.GlideApp;
import com.zjrb.coreprojectlibrary.common.listener.IOnImageTapListener;
import com.zjrb.coreprojectlibrary.common.permission.IPermissionCallBack;
import com.zjrb.coreprojectlibrary.common.permission.IPermissionOperate;
import com.zjrb.coreprojectlibrary.common.permission.Permission;
import com.zjrb.coreprojectlibrary.common.permission.PermissionManager;
import com.zjrb.coreprojectlibrary.ui.widget.load.LoadingIndicatorView;
import com.zjrb.coreprojectlibrary.ui.widget.photoview.PhotoView;
import com.zjrb.coreprojectlibrary.ui.widget.photoview.PhotoViewAttacher;
import com.zjrb.coreprojectlibrary.utils.DownloadUtil;
import com.zjrb.coreprojectlibrary.utils.PathUtil;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.webjs.BottomSaveDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片详情预览
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ImagePreviewFragment extends BaseFragment implements PhotoViewAttacher
        .OnViewTapListener, View.OnLongClickListener{

    @BindView(R2.id.iv_pre_image)
    PhotoView mIvPreImage;
    @BindView(R2.id.container_image_preview)
    FrameLayout containerImagePreview;
    @BindView(R2.id.liv_loading)
    LoadingIndicatorView mLivLoading;
    private PhotoViewAttacher mAttacher;
    private String mUrl;
    private boolean isTapClose = false;
    /**
     * 是否是长图，true可长下滑动
     */
    private boolean isLongPictureMode;
    private static final String TAP_CLOSE = "tap_close";

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static ImagePreviewFragment newInstance(ParamsEntity entity) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle args = new Bundle();
        args.putString(Key.FRAGMENT_ARGS, entity.getUrl());
        args.putBoolean(Key.TAP_CLOSE, entity.isTapClose());
        args.putBoolean(Key.IS_LONG_PICTURE_MODE, entity.isLongPictureMode());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(Key.FRAGMENT_ARGS);
            isTapClose = getArguments().getBoolean(Key.TAP_CLOSE);
            isLongPictureMode = getArguments().getBoolean(Key.IS_LONG_PICTURE_MODE, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.module_detail_image_preview, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }


    private void init() {
        //长图模式，将PhotoView添加到ScrollView中
        if (isLongPictureMode) {
            containerImagePreview.removeView(mIvPreImage);
            ViewGroup.LayoutParams lpScroll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ViewGroup.LayoutParams lpImage = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //启动长图模式
            mIvPreImage.enableLongPictureMode();
            ScrollView scrollView = new ScrollView(getContext());
            scrollView.addView(mIvPreImage, lpImage);
            containerImagePreview.addView(scrollView, lpScroll);
        }
        mAttacher = new PhotoViewAttacher(mIvPreImage);
        mAttacher.setOnViewTapListener(this);
        mAttacher.setOnLongClickListener(this);
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
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // 缓存原始资源，解决Gif加载慢
                .listener(new RequestListener() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        T.showShort(getContext(), "图片加载失败!");
                        mLivLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
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

    @Override
    public boolean onLongClick(View v) {
        if (isLongPictureMode) {
            BottomSaveDialogFragment dialogFragment = new BottomSaveDialogFragment();
            dialogFragment.setSaveListener(new BottomSaveDialogFragment.OnSaveDialogClickListener() {
                @Override
                public void onSave() {
                    try {
                        if (TextUtils.isEmpty(mUrl)) return;
                        if (getActivity() instanceof IPermissionOperate) {
                            IPermissionOperate callback = (IPermissionOperate) getActivity();
                            PermissionManager.get().request(callback, new IPermissionCallBack() {
                                @Override
                                public void onGranted(boolean isAlreadyDef) {
                                    download(mUrl);
                                }

                                @Override
                                public void onDenied(List<String> neverAskPerms) {
                                    PermissionManager.showAdvice(getContext(), "保存图片需要开启存储权限");
                                }

                                @Override
                                public void onElse(List<String> deniedPerms, List<String> neverAskPerms) {

                                }
                            }, Permission.STORAGE_WRITE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialogFragment.show(getFragmentManager(), "BottomSaveDialogFragment");
            return true;
        }
        return false;
    }

    private void download(String url) {
        DownloadUtil.get()
                .setDir(PathUtil.getImagePath())
                .setListener(new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onLoading(int progress) {

                    }

                    @Override
                    public void onSuccess(String path) {
                        T.showShort(getContext(), "保存成功");
                    }

                    @Override
                    public void onFail(String err) {
                        T.showShort(getContext(), "保存失败");
                    }
                })
                .download(url);
    }

    public static class ParamsEntity {
        private String mUrl;
        private boolean isTapClose = false;
        /**
         * 是否是长图，true可长下滑动
         */
        private boolean isLongPictureMode = false;

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String mUrl) {
            this.mUrl = mUrl;
        }

        public boolean isTapClose() {
            return isTapClose;
        }

        public void setTapClose(boolean tapClose) {
            isTapClose = tapClose;
        }

        public boolean isLongPictureMode() {
            return isLongPictureMode;
        }

        public void setLongPictureMode(boolean longPictureMode) {
            isLongPictureMode = longPictureMode;
        }

        public static class Builder {
            private String mUrl;
            private boolean isTapClose = false;
            private boolean isFromIce = false;
            private boolean isLongPictureMode = false;

            public String getUrl() {
                return mUrl;
            }

            public Builder setUrl(String mUrl) {
                this.mUrl = mUrl;
                return this;
            }

            public boolean isTapClose() {
                return isTapClose;
            }

            public Builder setTapClose(boolean tapClose) {
                isTapClose = tapClose;
                return this;
            }

            public boolean isFromIce() {
                return isFromIce;
            }

            public Builder setFromIce(boolean fromIce) {
                isFromIce = fromIce;
                return this;
            }

            public boolean isLongPictureMode() {
                return isLongPictureMode;
            }

            public Builder setLongPictureMode(boolean longPictureMode) {
                isLongPictureMode = longPictureMode;
                return this;
            }

            public ParamsEntity build() {
                ParamsEntity entity = new ParamsEntity();
                entity.setUrl(this.mUrl);
                entity.setTapClose(this.isTapClose);
                entity.setLongPictureMode(this.isLongPictureMode);
                return entity;
            }
        }
    }
}
