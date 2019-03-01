package com.zjrb.zjxw.detailproject.web;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.WebView;

import com.commonwebview.webview.CommonWebView;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.global.RouteManager;
import com.zjrb.zjxw.detailproject.nomaldetail.ImageBrowseActivity;
import com.zjrb.zjxw.detailproject.widget.AudioDialog;

import java.util.Iterator;
import java.util.Set;

import bean.ZBJTGetAppInfoRspBean;
import bean.ZBJTGetLocalRspBean;
import bean.ZBJTGetValueFromLocalBean;
import bean.ZBJTGetValueFromLocalRspBean;
import bean.ZBJTModifyUserInfoRspBean;
import bean.ZBJTOpenAppMobileBean;
import bean.ZBJTOpenAppMobileRspBean;
import bean.ZBJTOpenAppShareMenuBean;
import bean.ZBJTOpenAppShareMenuRspBean;
import bean.ZBJTReturnBean;
import bean.ZBJTSelectImageBean;
import bean.ZBJTSelectImageRspBean;
import bean.ZBJTStartRecordRspBean;
import bean.ZBJTUploadFileBean;
import bean.ZBJTUploadFileRspBean;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.nav.Nav;
import port.JsInterface;
import port.JsInterfaceCallBack;
import port.SerializableHashMap;
import port.ZBJTJSInterFace;

/**
 * 实现ZBJTJSInterFace接口则表示支持云平台新标准，不继承则需要自己实现一套
 * 继承Js实现类，如果不继承，则没有注入功能
 * Created by wanglinjie.
 * create time:2019/2/25  下午2:42
 */
public class JsInterfaceImp extends JsInterface implements ZBJTJSInterFace {

    private CommonWebView webview;
    private Bundle bundle;
    private JsInterfaceCallBack mCallback;

    public JsInterfaceImp(WebView webView, String jsObject, Context ctx) {
        super(webView, jsObject, ctx);
        this.webview = (CommonWebView) webView;
        mCallback = new JsInterfaceCallBack(webview);
    }

    //点击超链接图片逻辑
    @Override
    public void imageABrowseCB(int index, String url, SerializableHashMap map) {
        if (webview != null && getAImgSrcs() != null && getAImgSrcs().size() > 0) {
            String imgUrl = "";
            String newsUrl = "";
            //在省流量模式下且未被加载过的
            if (SettingManager.getInstance().isProvincialTraffic() && map1 != null &&
                    map1.getMap() != null && map1.getMap().size() > index && !map1.getMap().get(index)) {
                if (getAImgSrcs().get(index) != null && !getAImgSrcs().get(index).isEmpty()) {
                    Set keys = getAImgSrcs().get(index).keySet();
                    if (keys != null) {
                        Iterator iterator = keys.iterator();
                        while (iterator.hasNext()) {
                            imgUrl = iterator.next().toString();
                        }
                    }
                }
                if (!TextUtils.isEmpty(imgUrl) && (imgUrl.contains("?w=") || imgUrl.contains("?width="))) {
                    imgUrl = imgUrl.split("[?]")[0];
                    webview.setReplaceAPic(index, imgUrl);
                }
                map1.getMap().put(index, true);
            }
        }
    }

    //点击图片逻辑
    @Override
    public void imageBrowseCB(int index, String url, SerializableHashMap map) {
        if (webview != null && getImgSrcs() != null && getImgSrcs().length > 0) {
            //在省流量模式下且未被加载过的
            if (SettingManager.getInstance().isProvincialTraffic() && map != null &&
                    map.getMap() != null && map.getMap().size() > index && !map.getMap().get(index)) {
                webview.setReplacePic(index, getImgSrcs()[index]);
                map.getMap().put(index, true);
            } else {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putStringArray(ImageBrowseActivity.EXTRA_IMAGE_URLS, getImgSrcs());
                bundle.putInt(ImageBrowseActivity.EXTRA_IMAGE_INDEX, index);
                if (map != null && map.getMap() != null && map.getMap().size() > index && !map.getMap().get(index)) {
                    map.getMap().put(index, true);
                }
                bundle.putSerializable(ImageBrowseActivity.EXTRA_IMAGE_SRCS, map);

                Nav.with(webview.getFragment()).setExtras(bundle).toPath(RouteManager.IMAGE_BROWSE_ACTIVITY, WebViewImpl.IMAGE_PREVIEW_CODE);
            }
        }
    }

    //浙报集团通用JSSDK实现
    @Override
    public void openAppShareMenu(WebView webview, ZBJTOpenAppShareMenuBean bean, ZBJTOpenAppShareMenuRspBean beanRsp, String callback) {

    }

    @Override
    public void updateAppShareData(WebView webview, ZBJTOpenAppShareMenuBean bean, ZBJTReturnBean beanRsp, String callback) {

    }

    @Override
    public void selectImage(WebView webview, ZBJTSelectImageBean bean, ZBJTSelectImageRspBean beanRsp, String callback) {

    }

    @Override
    public void startRecord(WebView webview, ZBJTStartRecordRspBean beanRsp, String callback) {
        AudioDialog.newInstance()
                .setZBJTStartRecordRspBean(beanRsp)
                .setJSCallBack(callback).
                setCallBack(mCallback).
                show(((FragmentActivity) UIUtils.getActivity()).
                        getSupportFragmentManager(), "MoreDialog");
    }

    @Override
    public void getAppInfo(WebView webview, ZBJTGetAppInfoRspBean BeanRsp, String callback) {

    }

    @Override
    public void getLocation(WebView webview, ZBJTGetLocalRspBean BeanRsp, String callback) {

    }

    @Override
    public void uploadFile(WebView webview, ZBJTUploadFileBean bean, ZBJTUploadFileRspBean beanRsp, String callback) {

    }

    @Override
    public void closeWindow(WebView webview, ZBJTReturnBean beanRsp, String callback) {

    }

    @Override
    public void saveValueToLocal(WebView webview, ZBJTGetValueFromLocalBean bean, ZBJTReturnBean beanRsp, String callback) {

    }

    @Override
    public void getValueFromLocal(WebView webview, ZBJTGetValueFromLocalRspBean beanRsp, String callback) {

    }

    @Override
    public void login(WebView webview, ZBJTReturnBean beanRsp, String callback) {

    }

    @Override
    public void getUserInfo(WebView webview, String json, String callback) {

    }

    @Override
    public void openAppMobile(WebView webVebview, ZBJTOpenAppMobileBean bean, ZBJTOpenAppMobileRspBean beanRsp, String callback) {

    }

    @Override
    public void modifyUserInfo(WebView webview, ZBJTModifyUserInfoRspBean beanRsp, String callback) {

    }
}
