package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 所有官员列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class OfficalListTask extends APIGetTask<OfficalListBean> {

    public OfficalListTask(LoadingCallBack<OfficalListBean> callBack) {
        super(callBack);
    }

    /**
     * @param params start:官员id
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("start", "5");
        put("size", params[1]);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.OFFICAL_LIST;
    }
}
