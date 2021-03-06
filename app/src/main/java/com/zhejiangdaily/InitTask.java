package com.zhejiangdaily;

import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
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
    protected void onSetupParams(Object... params) {

    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.GET_SESSIONID;
    }
}
