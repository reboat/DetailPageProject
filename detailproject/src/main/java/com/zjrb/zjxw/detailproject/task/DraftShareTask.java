package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.DraftShareBean;

/**
 * 稿件分享
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class DraftShareTask extends APIPostTask<DraftShareBean> {
    public DraftShareTask(LoadingCallBack<DraftShareBean> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * timestamp:时间戳(long)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("timestamp", params[0]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.COLUMN_SUBSCRIBE;
    }
}
