package com.zhejiangdaily;

import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIPostTask;

/**
 * Created by wanglinjie.
 * create time:2017/9/5  下午3:39
 */

public class InitTask extends APIPostTask<SessionIdBean> {

    public InitTask(LoadingCallBack<SessionIdBean> callBack) {
        super(callBack);
    }

    @Override
    public void onSetupParams(Object... params) {

    }

    @Override
    public String getApi() {
        return APIManager.endpoint.API_INIT;
    }
}
