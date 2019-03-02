package com.zjrb.zjxw.detailproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zjrb.core.base.BaseActivity;
import com.zjrb.core.permission.IPermissionCallBack;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.UIUtils;

import java.util.Hashtable;
import java.util.List;

import cn.daily.news.biz.core.utils.PathUtil;
import cn.daily.news.update.util.DownloadUtil;

/**
 * 图片二维码解析工具类
 * Created by wanglinjie.
 * create time:2018/4/19  下午3:19
 */

public class ImageScanerUtils {

    private volatile static ImageScanerUtils instance;

    private ScanerImgCallBack mCallBack;
    private String imgUrl = "";

    private ImageScanerUtils() {
    }

    public interface ScanerImgCallBack {
        /**
         * 用于传递图片地址
         */
        void onScanerImgCallBack(String imgUrl, boolean isScanerImg);
    }


    public static ImageScanerUtils get() {
        if (instance == null) {
            synchronized (ImageScanerUtils.class) {
                if (instance == null) {
                    instance = new ImageScanerUtils();
                }
            }
        }
        return instance;
    }

    public void setmCallBack(ScanerImgCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 根据地址获取网络图片
     *
     * @param imgUtils
     */

    public void getBitmap(final ImageScanerUtils imgUtils, String imgUrl) {
        this.imgUrl = imgUrl;
        try {
            PermissionManager.get().request((BaseActivity) UIUtils.getActivity(), new
                    IPermissionCallBack() {
                        @Override
                        public void onGranted(boolean isAlreadyDef) {
                            scanerImg(imgUtils);
                        }

                        @Override
                        public void onDenied(List<String> neverAskPerms) {
                            PermissionManager.showAdvice(UIUtils.getContext(),
                                    "需要读写内存卡权限");
                        }

                        @Override
                        public void onElse(List<String> deniedPerms, List<String>
                                neverAskPerms) {

                        }
                    }, Permission.STORAGE_READE, Permission.STORAGE_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 识别二维码
     *
     * @param imgUtils
     */
    private void scanerImg(final ImageScanerUtils imgUtils) {
        try {
            DownloadUtil.get()
                    .setDir(PathUtil.getImagePath())
                    .setListener(new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onLoading(int progress) {

                        }

                        @Override
                        public void onSuccess(String path) {
                            if (!path.isEmpty()) {
                                //TODO 这里需要优化，耗时过长，需要开启子线程
                                Bitmap bitmap = BitmapFactory.decodeFile(path);
                                Result result = imgUtils.handleQRCodeFormBitmap(bitmap);
                                //链接
                                if (mCallBack != null) {
                                    if (result != null) {//是二维码
                                        mCallBack.onScanerImgCallBack(result.getText(), true);
                                    } else {//不是二维码
                                        mCallBack.onScanerImgCallBack(imgUrl, false);
                                    }
                                }

                            }
                        }

                        //图片下载失败
                        @Override
                        public void onFail(String err) {
                            if (mCallBack != null) {
                                mCallBack.onScanerImgCallBack(imgUrl, false);
                            }
                        }
                    })
                    .download(imgUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 校验二维码
     *
     * @param bitmap
     * @return 调用方式：ImageScanerUtils.handleQRCodeFormBitmap(getBitmap(sUrl));
     */
    private Result handleQRCodeFormBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        //获取图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] data = new int[width * height];
        bitmap.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();

        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        Result result = null;
        try {
            try {
                result = reader.decode(bitmap1, hints);
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

}
