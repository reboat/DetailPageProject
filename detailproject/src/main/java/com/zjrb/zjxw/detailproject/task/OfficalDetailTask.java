package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;

/**
 * 官员详情 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class OfficalDetailTask extends APIPostTask<OfficalDetailBean> {

    public OfficalDetailTask(LoadingCallBack<OfficalDetailBean> callBack) {
        super(callBack);
    }

    @Override
    protected void onSetupParams(Object... params) {
        put("id", params[0]);
        put("start", params[1]);
        put("size", params[2]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.DRAFT_PRAISEL;
    }
}
