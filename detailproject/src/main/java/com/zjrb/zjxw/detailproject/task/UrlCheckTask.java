package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.UrlCheckBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * Date: 2018/5/30 下午4:16
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 分享白名单校验接口
 */
public class UrlCheckTask extends APIGetTask<UrlCheckBean> {

    public UrlCheckTask(LoadingCallBack<UrlCheckBean> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {
        put("url", params[0]);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.URL_CHECK;
    }
}
