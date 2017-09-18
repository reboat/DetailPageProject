package com.zjrb.zjxw.detailproject.webjs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.zjrb.core.utils.click.ClickTracker;

import java.util.ArrayList;
import java.util.List;


/**
 * JavascriptInterface
 *
 * @author a_liYa
 * @date 2017/4/5 16:41.
 */
public class WebJsInterface {

    public static final String JS_NAME = "Android";

    private Context mContext;

    private String[] mImgSrcs;

    public WebJsInterface(Context context) {
        mContext = context;
    }

    public WebJsInterface(Context context, String[] imgSrcs) {
        mContext = context;
        mImgSrcs = imgSrcs;
    }

    /**
     * @param index 选择图片
     */
    @JavascriptInterface
    public void imageBrowse(int index) {
        if (ClickTracker.isDoubleClick()) {
            return;
        }
        if (mContext != null && mImgSrcs != null && mImgSrcs.length > 0) {
            Intent intent = ImageBrowseActivity.newIntent(mContext, mImgSrcs, index);
            if (!(mContext instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            mContext.startActivity(intent);
        }
    }

    public void setImgSrcs(String[] imgSrcs) {
        mImgSrcs = imgSrcs;
    }
}
