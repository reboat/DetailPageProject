package com.zjrb.zjxw.detailproject.web;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.webkit.WebView;

import com.commonwebview.webview.ChromeClientWrapper;
import com.commonwebview.webview.CommonWebView;
import com.commonwebview.webview.WebLifecycleFragment;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.Hashing;
import com.zjrb.core.utils.JsonUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.bean.GetUserInfoRspBean;
import com.zjrb.zjxw.detailproject.bean.NewAccountBean;
import com.zjrb.zjxw.detailproject.global.RouteManager;
import com.zjrb.zjxw.detailproject.nomaldetail.ImageBrowseActivity;
import com.zjrb.zjxw.detailproject.task.GetAccountTask;
import com.zjrb.zjxw.detailproject.utils.LocaltionUtils;
import com.zjrb.zjxw.detailproject.utils.NetUtils;
import com.zjrb.zjxw.detailproject.widget.AudioDialog;

import java.io.File;
import java.util.ArrayList;
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
import cn.daily.news.biz.core.UserBiz;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APIUploadTask;
import cn.daily.news.biz.core.network.compatible.ProgressCallBack;
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

    public JsInterfaceCallBack getmCallback() {
        return mCallback;
    }

    /**
     * 预览回来设置详情页图片加载
     * @param index
     */
    public void setPreviewIndex(int index) {
        if (getImgSrcs() != null && getImgSrcs().length >= index
                && map != null && map.getMap() != null
                && map.getMap().size() >= index) {
            webview.setReplacePic(index, getImgSrcs()[index]);
            map.getMap().put(index, true);
        }
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
    public void openAppShareMenu(ZBJTOpenAppShareMenuBean bean, ZBJTOpenAppShareMenuRspBean beanRsp, String callback) {

    }

    @Override
    public void updateAppShareData(ZBJTOpenAppShareMenuBean bean, ZBJTReturnBean beanRsp, String callback) {

    }

    //JSSDK中的选择照片
    @Override
    public void selectImage(ZBJTSelectImageBean bean, ZBJTSelectImageRspBean beanRsp, String callback) {
        if (webview != null && webview.getFragment() != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("callback", callback);
            Nav.with(webview.getFragment()).setExtras(bundle).toPath(RouteManager.ZB_SELECT_IMG,
                    ChromeClientWrapper.FILE_CHOOSER_RESULT_CODE);
        }
    }

    @Override
    public void startRecord(ZBJTStartRecordRspBean beanRsp, String callback) {
        AudioDialog.newInstance()
                .setZBJTStartRecordRspBean(beanRsp)
                .setJSCallBack(callback).
                setCallBack(mCallback).
                show(((FragmentActivity) UIUtils.getActivity()).
                        getSupportFragmentManager(), "MoreDialog");
    }

    @Override
    public void getAppInfo(ZBJTGetAppInfoRspBean BeanRsp, String callback) {
        ZBJTGetAppInfoRspBean.DataBean bean = BeanRsp.getData();
        bean.setApp(UIUtils.getAppName());
        bean.setVersion(AppUtils.getVersion());
        bean.setNetworkType(NetUtils.getNetworkState(UIUtils.getApp()));
        bean.setDevice(android.os.Build.BRAND);
        bean.setSystem("Android");
        bean.setSystemVersion(android.os.Build.VERSION.RELEASE);
        bean.setTimestamp(System.currentTimeMillis() + "");

        //参数签名算法
        String signature = Hashing.sha256(String.format("%s&&%s&&%s", AppUtils.getUniquePsuedoID(),
                bean.getTimestamp(),
                "MJ<?TH4&9w^"));

        bean.setSignature(signature);
        if (bean.getUuid().equals("1")) {
            bean.setUuid(AppUtils.getUniquePsuedoID());
        }
        BeanRsp.setData(bean);
        BeanRsp.setCode("1");
        mCallback.getAppInfo(BeanRsp, callback);

    }

    @Override
    public void getLocation(final ZBJTGetLocalRspBean BeanRsp, final String callback) {
        LocaltionUtils.get().getLocaltion(mCallback, BeanRsp, callback);
    }

    /**
     * 文件名截取localUrl最后一个反斜线然后拼接新的newName,不自动添加后缀
     *
     * @param oldName
     * @param newName
     */
    private String chageFileName(String oldName, String newName) {
        File file = new File(oldName);
        String path = oldName.substring(0, oldName.lastIndexOf("/") + 1) + newName;
        File newFile = new File(path);
        boolean flag = file.renameTo(newFile);
        return flag == true ? path : oldName;
    }

    @Override
    public void uploadFile(final ZBJTUploadFileBean bean, final ZBJTUploadFileRspBean beanRsp, final String callback) {
        String localUrl = "";
        if (!TextUtils.isEmpty(bean.getLocalUrl()) && !TextUtils.isEmpty(bean.getFileName())) {
            localUrl = chageFileName(bean.getLocalUrl(), bean.getFileName());
        }
        // inputName为空时,取"file",否则使用下发的值
        if (TextUtils.isEmpty(bean.getInputName())) {
            bean.setInputName("file");
        }
        final String finalInputName = bean.getInputName();
        new APIUploadTask<String>(new ProgressCallBack<String>() {
            @Override
            public void onSuccess(String v) {
                beanRsp.setCode("1");
                ZBJTUploadFileRspBean.DataBean bean = beanRsp.getData();
                bean.setResponse(v);
                beanRsp.setData(bean);
                mCallback.uploadFile(beanRsp, callback);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                beanRsp.setCode(errCode + "");
                mCallback.uploadFile(beanRsp, callback);
            }
        }) {
            @Override
            public void onSetupParams(Object... params) {
                putFile(finalInputName, (String) params[0]);
            }

            @Override
            public String getApi() {
                return bean.getServerUrl();
            }
        }.exe(localUrl);
    }

    @Override
    public void closeWindow(ZBJTReturnBean beanRsp, String callback) {
        Context context = webview.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                beanRsp.setCode("1");
                mCallback.closeWindow(beanRsp, callback);
                ((Activity) context).finish();
                break;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
    }

    @Override
    public void saveValueToLocal(ZBJTGetValueFromLocalBean bean, ZBJTReturnBean beanRsp, String callback) {
        //磁盘存储
        if (bean.getOption().equals("0")) {
            UIUtils.getMapForJs().put(bean.getKey(), bean.getValue());
        } else if (bean.getOption().equals("1")) {//内存存储
            SPHelper.get().put(bean.getKey(), bean.getValue());
        }
        beanRsp.setCode("1");
        mCallback.saveValueToLocal(beanRsp, callback);
    }

    @Override
    public void getValueFromLocal(ZBJTGetValueFromLocalRspBean beanRsp, String callback) {
        String value = "";
        if (beanRsp.getData().getOption().equals("0")) {
            value = UIUtils.getMapForJs().get(beanRsp.getData().getKey());
        } else if (beanRsp.getData().getOption().equals("1")) {
            value = SPHelper.get().get(beanRsp.getData().getKey(), "");
        }
        beanRsp.getData().setValue(value);
        beanRsp.setCode("1");
        mCallback.getValueFromLocal(beanRsp, callback);
    }

    @Override
    public void login(ZBJTReturnBean beanRsp, String callback) {
        if (webview != null && webview.getFragment() != null) {
            WebLifecycleFragment fragment = webview.getFragment();
            //传递数据
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable("ZBJTReturnBean", beanRsp);
            bundle.putString("callback", callback);

            Nav.with(fragment).setExtras(bundle).toPath(RouteManager.LOGIN_ACTIVITY, WebViewImpl.LOGIN_RESULT_CODE);
            fragment.addOnActivityResultCallback((WebLifecycleFragment.OnActivityResultCallback) webview.getHelper());
        }
    }

    @Override
    public void getUserInfo(final String json, final String callback) {
        final ArrayList<String> list = (ArrayList) JsonUtils.parseArray(json, String.class);
        final GetUserInfoRspBean rspBean = new GetUserInfoRspBean();
        new GetAccountTask(new LoadingCallBack<NewAccountBean>() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                rspBean.setCode("0");
                mCallback.getUserInfo(JsonUtils.toJsonString(rspBean), callback);
            }

            @Override
            public void onSuccess(NewAccountBean bean) {

                if (bean != null) {
                    String formatString = "";
                    String time = System.currentTimeMillis() + "";
                    //循环构造算法
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            formatString += String.format(formatString + "%s&",
                                    list.get(i));
                        }
                        formatString += formatString + "&%s&&%s";
                    } else {
                        formatString = "%s&&%s";
                    }
                    //最后两个字段'&%s&&%s'
                    String signature = Hashing.sha256(String.format(formatString,
                            time,
                            "KO>N<O5&3^L1%23YH0H1#G91*2H"));

                    UserBiz.get().setAccount(bean.getAccount());
                    rspBean.setCode("1");
                    rspBean.getData().setBean(bean);
                    if (UserBiz.get().isLoginUser()) {
                        rspBean.getData().setLogin("true");
                    } else {
                        rspBean.getData().setLogin("false");
                    }
                    if (UserBiz.get().isCertification()) {
                        rspBean.getData().setMobile("true");
                    } else {
                        rspBean.getData().setMobile("false");
                    }
                    rspBean.getData().setSignature(signature);
                } else {
                    rspBean.setCode("0");
                }
                mCallback.getUserInfo(JsonUtils.toJsonString(rspBean), callback);

            }
        }).setTag(webview.getContext()).exe();
    }

    //实名登录跳转
    private void rounteAppMobile(ZBJTOpenAppMobileRspBean beanRsp, String callback, String control) {
        if (webview != null && webview.getFragment() != null) {
            WebLifecycleFragment fragment = webview.getFragment();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable("ZBJTOpenAppMobileRspBean", beanRsp);
            bundle.putString("callback", callback);
            bundle.putBoolean(IKey.IS_COMMENT_ACTIVITY, true); // 云平台调取手机号页面,不显示"跳过"
            if (control.equals("0")) {
                Nav.with(fragment).setExtras(bundle).toPath(RouteManager.ZB_MOBILE_VERIFICATION, WebViewImpl.VERIFICATION_RESULT_CODE);
            } else {
                Nav.with(fragment).setExtras(bundle).toPath(RouteManager.ZB_CHANGE_MOBILE, WebViewImpl.VERIFICATION_RESULT_CODE);
            }
            fragment.addOnActivityResultCallback((WebLifecycleFragment.OnActivityResultCallback) webview.getHelper());
        }
    }

    @Override
    public void openAppMobile(ZBJTOpenAppMobileBean bean, ZBJTOpenAppMobileRspBean beanRsp, String callback) {
        if (bean.getControl().equals("0")) {
            beanRsp.setCode("1");
            beanRsp.getData().setMobile(UserBiz.get().getAccount().getMobile());
            if (UserBiz.get().isCertification()) {
                mCallback.openAppMobile(beanRsp, callback);
                return;
            } else {
                //进行页面跳转
                rounteAppMobile(beanRsp, callback, "0");
            }
        } else if (bean.getControl().equals("1")) {
            beanRsp.getData().setMobile(UserBiz.get().getAccount().getMobile());
            beanRsp.setCode("0");
            if (!UserBiz.get().isCertification()) {
                mCallback.openAppMobile(beanRsp, callback);
                return;
            } else {
                //进行页面跳转
                rounteAppMobile(beanRsp, callback, "1");
            }
        }

    }

    @Override
    public void modifyUserInfo(ZBJTModifyUserInfoRspBean beanRsp, String callback) {
        if (beanRsp.getData().getOption().equals("name")) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable("ZBJTModifyUserInfoRspBean", beanRsp);
            bundle.putString("callback", callback);
            bundle.putString("option", "name");
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.ZB_MODIFY_USERNAME, WebViewImpl.DELIVERYNAME_RESULT_CODE);
        } else if (beanRsp.getData().getOption().equals("address")) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSerializable("ZBJTModifyUserInfoRspBean", beanRsp);
            bundle.putString("callback", callback);
            bundle.putString("option", "address");
            Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.ZB_MODIFY_USERADDRESS, WebViewImpl.DELIVERYADDRESS_RESULT_CODE);
        }

    }
}
