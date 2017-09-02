package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.domain.base.BaseInnerData;

/**
 * 频道订阅
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class ColumnSubscribeTask extends APIPostTask<BaseInnerData> {
    public ColumnSubscribeTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params column_id:栏目id(int)
     *               do_subscribe:订阅/取消订阅(boolean)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("column_id", params[0]);
        put("do_subscribe", params[1]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.COLUMN_SUBSCRIBE;
    }
}
