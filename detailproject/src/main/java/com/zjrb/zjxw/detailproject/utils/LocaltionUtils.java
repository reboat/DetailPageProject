package com.zjrb.zjxw.detailproject.utils;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.zjrb.core.permission.AbsPermSingleCallBack;
import com.zjrb.core.permission.IPermissionOperate;
import com.zjrb.core.permission.Permission;
import com.zjrb.core.permission.PermissionManager;
import com.zjrb.core.utils.UIUtils;

import java.util.List;

import bean.ZBJTGetLocalRspBean;
import port.JsInterfaceCallBack;

/**
 * JS定位辅助工具类
 * Created by wanglinjie.
 * create time:2019/3/1  下午6:20
 */
public class LocaltionUtils {

    private volatile static LocaltionUtils mInstance;

    public static LocaltionUtils get() {
        if (mInstance == null) {
            synchronized (LocaltionUtils.class) {
                if (mInstance == null) {
                    mInstance = new LocaltionUtils();
                }
            }
        }
        return mInstance;
    }

    private AMapLocationClient mLocationClient;

    //获取定位
    public void getLocaltion(final JsInterfaceCallBack jsInterfaceCallBack, final ZBJTGetLocalRspBean BeanRsp, final String callback) {
        PermissionManager.get().request((IPermissionOperate) UIUtils.getActivity(), new
                AbsPermSingleCallBack() {
                    @Override
                    public void onGranted(boolean isAlreadyDef) {
                        mLocationClient = new AMapLocationClient(UIUtils.getApp());
                        mLocationClient.startLocation();
                        mLocationClient.setLocationListener(new AMapLocationListener() {
                            @Override
                            public void onLocationChanged(AMapLocation aMapLocation) {
                                if (aMapLocation != null) {
                                    ZBJTGetLocalRspBean.DataBean bean = BeanRsp.getData();
                                    if (aMapLocation.getErrorCode() == AMapLocation
                                            .LOCATION_SUCCESS) {
                                        if (!TextUtils.isEmpty(aMapLocation.getCity())) {
                                            bean.setLatitude(aMapLocation.getLatitude()+"");
                                            bean.setLontitude(aMapLocation.getLongitude()+"");
                                            bean.setAccuracy(aMapLocation.getAccuracy()+"");
                                            bean.setCity(aMapLocation.getCity());
                                            bean.setAddress(aMapLocation.getAddress());
                                            bean.setSpeed(aMapLocation.getSpeed()+"");
                                            bean.setTimestamp(aMapLocation.getTime()+"");
                                            BeanRsp.setData(bean);
                                            BeanRsp.setCode("1");
                                        } else {
                                            BeanRsp.setCode("0");
                                        }
                                        jsInterfaceCallBack.getLocation(BeanRsp,callback);
                                    } else {

                                        // Gps定位失败，通过ip定位
                                        BeanRsp.setCode("0");
                                        jsInterfaceCallBack.getLocation(BeanRsp,callback);
                                    }
                                    mLocationClient.stopLocation();
                                    mLocationClient.onDestroy();
                                    mLocationClient = null;
                                }
                            }
                        });
                    }

                    @Override
                    public void onDenied(List<String> neverAskPerms) {
                        BeanRsp.setCode("0");
                        jsInterfaceCallBack.getLocation(BeanRsp,callback);
                    }
                }, Permission.LOCATION_COARSE);
    }
}
