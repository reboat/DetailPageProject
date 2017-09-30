package com.zjrb.zjxw.detailproject.webjs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.zjrb.core.api.APIManager;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.biz.UserBiz;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;


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

    private String mText;

    private volatile static WebJsInterface mInstance;

    /**
     * @return 单例获取初始化实例
     */
    public static WebJsInterface getInstance(Context mContext) {
        if (mInstance == null) {
            synchronized (WebJsInterface.class) {
                if (mInstance == null) {
                    mInstance = new WebJsInterface(mContext);
                }
            }
        }
        return mInstance;
    }

    private WebJsInterface(Context context) {
        mContext = context;
    }

    private WebJsInterface(Context context, String[] imgSrcs) {
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


    /**
     * @param imgSrcs 获取网页图集
     */
    public void setImgSrcs(String[] imgSrcs) {
        mImgSrcs = imgSrcs;
    }

    /**
     * @param text 获取网页文案
     */
    public void setHtmlText(String text) {
        mText = text;
    }

    public String getHtmlText() {
        return mText;
    }

    /**
     * 获取详情页中的图片集合
     *
     * @return
     */
    public String[] getmImgSrcs() {
        return mImgSrcs;
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


    private Bundle bundle;

    /**
     * 显示评论列表页面
     *
     * @param newsId    稿件ID
     * @param newsTitle 新闻标题
     * @param newsType  新闻或者活动的类型
     */
    @JavascriptInterface
    public void zjxw_js_showCommentList(String newsId, String newsTitle, int newsType) {

        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(IKey.ID, newsId);
        bundle.putString(IKey.TITLE, newsTitle);
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/detail/CommentActivity");
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
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(UIUtils.getContext(), "收藏成功");
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), "收藏失败");
            }

        }).setTag(this).exe(newsId, true);
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
        T.showShortNow(UIUtils.getContext(), msg);
    }

    /**
     * @return 获取sessionID
     */
    @JavascriptInterface
    public String zjxw_js_getSessionID() {
        return UserBiz.get().getSessionId();
    }

    /**
     * @return 获取accountID
     */
    @JavascriptInterface
    public String zjxw_js_getAccountID() {
        return UserBiz.get().getAccountID();
    }

    /**
     * @return 返回versioncode
     */
    @JavascriptInterface
    public String zjxw_js_getAppVersionCode() {
        return String.valueOf(AppUtils.getVersionCode());
    }

    /**
     * @return 获取昵称
     */
    @JavascriptInterface
    public String zjxw_js_getScreenName() {
        return UserBiz.get().getAccount().getNick_name();
    }

    /**
     * 跳转到兑吧积分商城首页或者商品信息详情页面
     *
     * @param param
     */
    @JavascriptInterface
    public void zjxw_js_showZmallWeb(String param) {
        Nav.with(UIUtils.getContext()).to(param);
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
        return UserBiz.get().isLoginUser();
    }

    /**
     * @return 获取服务器地址
     */
    @JavascriptInterface
    public String zjxw_js_getServerHost() {
        return APIManager.getBaseUri();
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

    /**
     * 弹出评论框，添加评论功能，客户端将评论结果传递给JS端
     *
     * @param newsId
     * @param inputContent
     */
    @JavascriptInterface
    public void zjxw_js_inputComment(String newsId, String inputContent) {
//        jsCallInputComment(newsId, inputContent);
    }

    /**
     * 同名方法不能映射成react method
     *
     * @param shareTitle   分享的标题
     * @param shareSummary 分享的简介
     * @param shareLink    分享的链接(可以为"")
     * @param shareImage   分享的小图片(可以为"")
     * @param uid          分享内容的id(分享内容的类型(可以为新闻/专题/活动的类型(0,4,7)，类型为string类型，非必填))
     * @param uidType      分享内容的类型(可以为新闻/专题/活动的类型(0,4,7)，类型为int类型，非必填)
     */
    @JavascriptInterface
    public void zjxw_js_reweet(String shareTitle, String shareSummary, String shareLink,
                               String shareImage, String uid, int uidType) {
//        jsReweet(shareTitle, shareSummary, shareLink, shareImage, uid, uidType);
    }

    /**
     * 注意：这些参数中，都不能含有":"
     *
     * @param id      为alert的id(用以JS端区分接收，由JS端定义)
     * @param title   选择框的标题
     * @param msg     选择框的内容
     * @param choice1 选择框的选项1
     * @param choice2 选择框的选项2
     */
    @JavascriptInterface
    public void zjxw_js_showAlert(String id, String title, String msg, String choice1, String choice2) {
//        jsShowAlert(id, title, msg, choice1, choice2);
    }

}
