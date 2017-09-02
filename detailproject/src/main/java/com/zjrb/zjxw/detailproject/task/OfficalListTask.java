package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;

/**
 * 所有官员列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class OfficalListTask extends APIPostTask<OfficalListBean> {

    public OfficalListTask(LoadingCallBack<OfficalListBean> callBack) {
        super(callBack);
    }

    /**
     * @param params start:官员id
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("start", params[0]);
        put("size", params[1]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.DRAFT_PRAISEL;
    }
}
