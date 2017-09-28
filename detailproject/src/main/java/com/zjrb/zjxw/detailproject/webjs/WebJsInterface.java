package com.zjrb.zjxw.detailproject.webjs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.zjrb.core.utils.click.ClickTracker;


/**
 * JavascriptInterface
 * <p>
 * Created by wanglinjie.
 * create time:2017/9/19  上午11:34
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


    /**
     * 图片选择框
     */
    @JavascriptInterface
    public void zjxw_js_selectImage() {
    }

    /**
     * 开始录音
     */
    @JavascriptInterface
    public void zjxw_js_startRecord() {
    }

    /**
     * 文件上传
     *
     * @param serverURL
     * @param localURL
     * @param fileName
     * @param inputName
     */
    @JavascriptInterface
    public void zjxw_js_fileUpload(String serverURL, String localURL, String fileName, String inputName) {
    }

    /**
     * 开始定位
     */
    @JavascriptInterface
    public void zjxw_js_getLocationInfo() {
    }

    /**
     * 显示评论列表页面
     *
     * @param newsId
     * @param newsTitle
     * @param newsType
     */
    @JavascriptInterface
    public void zjxw_js_showCommentList(String newsId, String newsTitle, int newsType) {
    }

    /**
     * 外部直接进入的APP返回的时候要判断，如果还打开了其他外部直接进入的页面 ，那么就finish，如果没有打开过，那么就直接启动主界面，
     * 如果是之前正常进入的，那么就简单的finish
     */
    @JavascriptInterface
    public void zjxw_js_close() {
    }

    /**
     * 收藏/需要判定登录
     *
     * @param newsId
     * @param type
     */
    @JavascriptInterface
    public void zjxw_js_follow(String newsId, int type) {
    }

    /**
     * 登录
     */
    @JavascriptInterface
    public void zjxw_js_login() {
    }

    /**
     * Toast弹框
     *
     * @param msg
     */
    @JavascriptInterface
    public void zjxw_js_showTips(String msg) {
    }

    /**
     * @return 获取sessionID
     */
    @JavascriptInterface
    public String zjxw_js_getSessionID() {
        return "";
    }

    /**
     * @return 获取accountID
     */
    @JavascriptInterface
    public String zjxw_js_getAccountID() {
        return "";
    }

    /**
     * @return 返回versioncode
     */
    @JavascriptInterface
    public String zjxw_js_getAppVersionCode() {
        return "";
    }

    /**
     * @return 获取昵称
     */
    @JavascriptInterface
    public String zjxw_js_getScreenName() {
        return "";
    }

    /**
     * 跳转到兑吧积分商城首页或者商品信息详情页面
     *
     * @param param
     */
    @JavascriptInterface
    public void zjxw_js_showZmallWeb(String param) {
    }

    /**
     * 跳转到积分列表
     */
    @JavascriptInterface
    public void zjxw_js_showTaskList() {
    }

    /**
     * @return 用户是否登录
     */
    @JavascriptInterface
    public boolean zjxw_js_isUserLogin() {
        return true;
    }

    /**
     * @return 获取服务器地址
     */
    @JavascriptInterface
    public String zjxw_js_getServerHost() {
        return "";
    }

    /**
     * 利用客户端进行数据Key-Value存储
     *
     * @param type  0-内存存储，1-文件存储
     * @param key
     * @param value
     * @return
     */
    @JavascriptInterface
    public int zjxw_js_setKeyValue(int type, String key, String value) {
        return 1;
    }

    /**
     * 利用客户端进行数据Key-Value取值
     *
     * @param type
     * @param key
     * @param context
     */
    @JavascriptInterface
    public void zjxw_js_getKeyValue(int type, String key, String context) {
    }

}
