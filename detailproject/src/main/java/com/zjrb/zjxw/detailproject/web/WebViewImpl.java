package com.zjrb.zjxw.detailproject.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.commonwebview.webview.ChromeClientWrapper;
import com.commonwebview.webview.CommonWebView;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.utils.ImageScanerUtils;
import com.zjrb.zjxw.detailproject.utils.SettingBiz;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import bean.MediaEntity;
import cn.daily.news.biz.core.UserBiz;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.ui.widget.ScanerBottomFragment;
import port.SerializableHashMap;
import port.WebviewCBHelper;

/**
 * 扩展类，可以实现webview高级功能
 * Created by wanglinjie.
 * create time:2018/11/19  下午4:56
 */
public class WebViewImpl extends WebviewCBHelper implements ImageScanerUtils.ScanerImgCallBack {

    /**
     * 选择照片 - result_code
     */
    private final static int FILE_CHOOSER_RESULT_CODE = 10;
    /**
     * 登录 - result_code
     */
    public final static int LOGIN_RESULT_CODE = 20;

    public final static int IMAGE_PREVIEW_CODE = 21;

    /**
     * 实名认证 - result_code
     */
    public final static int VERIFICATION_RESULT_CODE = 22;
    /**
     * 修改绑定手机号 - result_code
     */
    public final static int MODIFICATION_RESULT_CODE = 23;

    /**
     * 修改用户信息
     */
    public final static int DELIVERYNAME_RESULT_CODE = 24;
    public final static int DELIVERYADDRESS_RESULT_CODE = 25;

    //JS绑定对象名
    private String JSObject;

    @Override
    public String getUserAgent() {
        return C.http.userAgent();
    }

    //如果不绑定对象，则属于正常的webview加载链接
    //使用浙江新闻通用版本则使用ZBJTJsBridge.PREFIX_JS_METHOD_NAME
    @Override
    public String getWebViewJsObject() {
        return JSObject;
    }

    public void setWebViewJsObject(String JSObject) {
        this.JSObject = JSObject;
    }

    //是否开启非省流量模式
    @Override
    public boolean isProvinTrafficMode() {
        return SettingManager.getInstance().isProvincialTraffic();
    }

    //webview设置
    @Override
    public void setWebviewConfig(CommonWebView webview) {
        super.setWebviewConfig(webview);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(false);
        // 开启database storage API功能
        webview.getSettings().setDatabaseEnabled(false);
        webview.getSettings().setAppCacheEnabled(false);
        webview.getSettings().setTextZoom(Math.round(SettingBiz.get().getHtmlFontScale() * 100));
        // 夜间模式
        if (ThemeMode.isNightMode()) {
            webview.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color._ffffff_202124));
        } else {
            webview.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color
                    ._ffffff_191919));
        }
    }

    //链接点击路由跳转
    @Override
    public void shouldOverrideUrlLoading(WebView view, String url) {
        Nav.with(view.getContext()).to(url);
    }

    //二维码扫描业务
    @Override
    public void OnScanerImg(String imgUrl, boolean isStream) {
        //长按回调设置
        ImageScanerUtils.get().setmCallBack(this);
        String mImgUrl;
        if (imgUrl.isEmpty()) return;
        if (imgUrl.contains("?w=") || imgUrl.contains("?width=")) {
            mImgUrl = imgUrl.split("[?]")[0];
        } else {
            mImgUrl = imgUrl;
        }
        ImageScanerUtils imgUtils = ImageScanerUtils.get();
        if (imgUtils != null) {
            ImageScanerUtils.get().getBitmap(imgUtils, mImgUrl);
        }
    }

    /**
     * 长按二维码解析出结果后的处理逻辑
     *
     * @param imgUrl
     * @param isScanerImg
     */
    @Override
    public void onScanerImgCallBack(String imgUrl, boolean isScanerImg) {
        ScanerBottomFragment.newInstance().showDialog((AppCompatActivity) UIUtils
                .getActivity()).isScanerImg(isScanerImg).setActivity(UIUtils.getActivity()).setImgUrl(imgUrl);
    }

    @Override
    public void NavToImageSelect(Fragment fragment, int requestCode) {
        Nav.with(fragment)
                .toPath("/core/MediaSelectActivity", requestCode);
    }

    @Override
    public void openFileResultCallBack(int requestCode, int resultCode, Intent data, ChromeClientWrapper wrapper, ValueCallback<Uri> mUploadMessage, ValueCallback<Uri[]> mUploadMessage21) {
        if (requestCode == ChromeClientWrapper.FILE_CHOOSER_RESULT_CODE) {
            if (null != mUploadMessage21) {
                Uri[] uris = null;
                if (data != null) {
                    ArrayList<MediaEntity> list = data.getParcelableArrayListExtra("key_data");
                    if (list != null && !list.isEmpty()) {
                        uris = new Uri[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            uris[i] = list.get(i).getUri();
                        }
                    }
                }
                mUploadMessage21.onReceiveValue(uris);
                if (wrapper != null) {
                    wrapper.setmUploadMessage(null);
                }
            } else if (null != mUploadMessage) {
                Uri result = null;
                if (data != null && Activity.RESULT_OK == resultCode) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        ArrayList<MediaEntity> list = data.getParcelableArrayListExtra("key_data");
                        if (list != null && !list.isEmpty()) {
                            result = list.get(0).getUri();
                        }
                    } else {
                        result = data.getData();
                    }
                }
                mUploadMessage.onReceiveValue(result);
                if (wrapper != null) {
                    wrapper.setmUploadMessage21(null);
                }
            }
        }
    }

    //业务返回逻辑处理
    @Override
    public void OnResultCallBack(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //图片选择器
            case FILE_CHOOSER_RESULT_CODE:
                if (data != null) {
                    ArrayList<MediaEntity> list = data.getParcelableArrayListExtra("key_data");
                    if (list != null && !list.isEmpty()) {
                        if (list.get(0) != null && list.get(0).getUri() != null && !TextUtils.isEmpty(list.get(0).getUri().toString())) {
//                            callback_zjxw_js_selectImage(list.get(0).getUri().toString());
                        } else {
//                            callback_zjxw_js_selectImage("");
                        }
                    }
                }

                break;
            case LOGIN_RESULT_CODE:
                // 登录页面关闭
                if (UserBiz.get().isLoginUser()) {
//                    callback_zjxw_js_login("SUCCESS");
                } else {
//                    callback_zjxw_js_login("FAIL");
                }
                break;
            case VERIFICATION_RESULT_CODE:
                // 实名认证
                if (UserBiz.get().isCertification()) {
//                    callback_zjxw_js_bindmobile("SUCCESS");
                } else {
//                    callback_zjxw_js_bindmobile("FAIL");
                }
                break;
            case MODIFICATION_RESULT_CODE:
                //修改手机号
                if (resultCode == Activity.RESULT_OK) {
//                    callback_zjxw_js_modifyMobile("SUCCESS");
                } else {
//                    callback_zjxw_js_modifyMobile("FAIL");
                }
                break;
            //替换图片
            case IMAGE_PREVIEW_CODE:
                if (resultCode == 1 && data != null && data.hasExtra("map")) {
                    HashMap<Integer, Boolean> map = ((SerializableHashMap) data
                            .getSerializableExtra("map")).getMap();
                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(i)) {
//                            setPreviewIndex(i);
                        }
                    }
                }
                break;
            case DELIVERYNAME_RESULT_CODE:
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
//                        callback_zjxw_js_modifyDeliveryName("SUCCESS", data.getStringExtra("deliver_name"));
                    } else {
//                        callback_zjxw_js_modifyDeliveryName("FAIL", data.getStringExtra("deliver_name"));
                    }
                }
                break;
            case DELIVERYADDRESS_RESULT_CODE:
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
//                        callback_zjxw_js_modifyDeliveryAddress("SUCCESS", data.getStringExtra("deliver_address"));
                    } else {
//                        callback_zjxw_js_modifyDeliveryName("FAIL", data.getStringExtra("deliver_address"));
                    }
                }
                break;
        }
    }

    //webview加载结束逻辑
    @Override
    public void onWebPageComplete(Context ctx) {
        if (ctx instanceof NewsDetailAdapter.CommonOptCallBack) {
            ((NewsDetailAdapter.CommonOptCallBack) ctx).onOptPageFinished();
        } else if (ctx instanceof TopicAdapter.CommonOptCallBack) {
            ((TopicAdapter.CommonOptCallBack) ctx).onOptPageFinished();
        }
    }

    //省流量模式业务逻辑
    @Override
    public WebResourceResponse doProvinTraffic(String url) {
        WebResourceResponse response = null;
        if (url.toString().contains("?width=") || url.toString().contains("?w=")) {
            try {
                InputStream image = UIUtils.getContext().getResources().openRawResource
                        (+R.mipmap.module_core_replace);
                response = new WebResourceResponse("image/png", "UTF-8", image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    //视频全屏操作
    @Override
    public void doFullVideo() {
//        if (mHasUrl) {
        LocalBroadcastManager.getInstance(UIUtils.getContext())
                .sendBroadcast(
                        new Intent(UIUtils.getString(R.string.intent_action_close_video)));
//        }

    }

    //退出全屏操作
    @Override
    public void exitFullVideo() {
//        if (mHasUrl) {
        LocalBroadcastManager.getInstance(UIUtils.getContext())
                .sendBroadcast(
                        new Intent(UIUtils.getString(R.string.intent_action_open_video)));
//        }
    }
}
