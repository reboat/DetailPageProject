package com.zjrb.zjxw.detailproject.photodetail;


import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.utils.ImageScanerUtils;
import com.zjrb.zjxw.detailproject.widget.photoview.PhotoView;
import com.zjrb.zjxw.detailproject.widget.photoview.PhotoViewAttacher;

import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.ui.widget.ScanerBottomFragment;
import cn.daily.news.update.util.NetUtils;

/**
 * 图片详情预览
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class ImagePreviewFragment extends DailyFragment implements PhotoViewAttacher.OnViewTapListener, View.OnClickListener, View.OnLongClickListener, ImageScanerUtils.ScanerImgCallBack {

    private View mProgressBarContainer;
    private TextView mTipView;
    private PhotoView mIvPreImage;
    PhotoViewAttacher mAttacher;

    //图片url
    private String mUrl;

    private int id = 0;

    private static final String ARGS = "args";
    private static final String FLAG = "flag";
    private static final String ID = "mlf_id";
    private boolean isFromAtlas = false;

    public static ImagePreviewFragment newInstance(String url) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();

        Bundle args = new Bundle();
        args.putString(ARGS, url);
        fragment.setArguments(args);
        return fragment;
    }

    public static ImagePreviewFragment newInstance(String url, boolean isFromAtlas, int mlfid) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();

        Bundle args = new Bundle();
        args.putString(ARGS, url);
        args.putBoolean(FLAG, isFromAtlas);
        args.putInt(ID, mlfid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARGS);
            if (getArguments().containsKey(FLAG)) {
                isFromAtlas = getArguments().getBoolean(FLAG);
            }
            if (getArguments().containsKey(ID)) {
                id = getArguments().getInt(ID);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_image_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvPreImage = view.findViewById(R.id.iv_pre_image);
        mProgressBarContainer = view.findViewById(R.id.iv_pre_progressBar_container);
        mProgressBarContainer.setOnClickListener(this);
        mTipView = view.findViewById(R.id.iv_pre_tip);
        init();
    }

    private void init() {
        mAttacher = new PhotoViewAttacher(mIvPreImage);
        mAttacher.setOnLongClickListener(this);
        mAttacher.setOnViewTapListener(this);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                if (!isFromAtlas) {
                    getActivity().onBackPressed();
                }
            }
        });
        ViewCompat.setTransitionName(mIvPreImage, "shared_view_name");

        if (NetUtils.isMobile(UIUtils.getApp()) && SettingManager.getInstance().isProvincialTraffic()) {
            mTipView.setText("点击加载");
        } else {
            loadImage();
        }
    }

    private void loadImage() {
        mTipView.setText("加载中...");

        Uri uri = Uri.parse(mUrl);
        if (uri != null) {
            try {
                mUrl = uri.buildUpon().appendQueryParameter("support_spare", String.valueOf(false)).build().toString();
            } catch (Exception e) {
            }
        }
        GlideApp.with(mIvPreImage).load(mUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                mProgressBarContainer.setVisibility(View.VISIBLE);
                mTipView.setText("重新加载");
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                mProgressBarContainer.setVisibility(View.GONE);
                return false;
            }
        }).into(mIvPreImage);
    }

    @Override
    public void onClick(View v) {
        loadImage();
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (getActivity() instanceof OnImageTapListener) {
            ((OnImageTapListener) getActivity()).onImageTap(view);
        }
    }

    /**
     * 图集长按监听
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        ImageScanerUtils.get().setmCallBack(this);
        ImageScanerUtils imgUtils = ImageScanerUtils.get();
        if (imgUtils != null) {
            ImageScanerUtils.get().getBitmap(imgUtils, mUrl);
        }
//        scanerImg(mUrl);
        return false;
    }

//    private ThreadManager.ThreadPoolProxy pool;

//    /**
//     * 二维码图片解析,已经下载过了，不需要拿url去解析二维码
//     */
//    private void scanerImg(final String imgUrl) {
//        pool = ThreadManager.getSinglePool();
//        pool.execute(new Runnable() {
//            @Override
//            public void run() {
//                ImageScanerUtils imgUtils = ImageScanerUtils.get();
//                Bitmap b = UIUtils.drawable2Bitmap(mIvPreImage.getDrawable());
//                Result result = null;
//                if (b != null) {
//                    result = imgUtils.handleQRCodeFormBitmap(b);
//                }
//                //链接mage
//                if (result != null) {//是二维码
//                    scanerAnalytics(imgUrl, true);
//                    ScanerBottomFragment.newInstance().showDialog((AppCompatActivity) UIUtils
//                            .getActivity()).isScanerImg(true).setActivity(getActivity()).setImgUrl(result.getText()).setMlfId(id);
//                } else {//不是二维码
//                    ScanerBottomFragment.newInstance().showDialog((AppCompatActivity) UIUtils
//                            .getActivity()).isScanerImg(false).setActivity(getActivity()).setImgUrl(imgUrl).setMlfId(id);
//                }
//
//            }
//        });
//    }

//    /**
//     * 二维码识别相关埋点
//     * 通过相册进入没有mlf_id
//     */
//    private void scanerAnalytics(String imgUrl, boolean isScanerImg) {
//        if (isScanerImg) {
//            new Analytics.AnalyticsBuilder(getContext(), "800024", "800024", "PictureRelatedOperation", false)
//                    .setEvenName("识别二维码图片")
//                    .setObjectID(id)
//                    .setObjectType(ObjectType.PictureType)
//                    .setPageType("图片预览页")
//                    .setOtherInfo(Analytics.newOtherInfo()
//                            .put("mediaURL", imgUrl)
//                            .toString())
//                    .pageType("新闻详情页")
//                    .operationType("识别二维码")
//                    .build()
//                    .send();
//        }
//    }

    /**
     * 清除线程池
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (pool != null) {
//            pool.stop();
//        }
    }

    //二维码识别回调处理
    @Override
    public void onScanerImgCallBack(String imgUrl, boolean isScanerImg) {
        ScanerBottomFragment.newInstance().showDialog((AppCompatActivity) UIUtils
                .getActivity()).isScanerImg(isScanerImg).setActivity(UIUtils.getActivity()).setImgUrl(imgUrl);
    }

    /**
     * 在图片上轻触监听
     *
     * @author a_liYa
     * @date 16/11/3 16:36.
     */
    public interface OnImageTapListener {

        /**
         * 轻触回调
         *
         * @param view 被触摸View
         */
        void onImageTap(View view);

    }

}
