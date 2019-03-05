package com.zhejiangdaily;

import com.zjrb.zjxw.detailproject.global.APIManager;

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
        return APIManager.endpoint.GET_SESSIONID;
    }
}
