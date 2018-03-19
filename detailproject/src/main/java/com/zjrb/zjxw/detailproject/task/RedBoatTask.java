package com.zjrb.zjxw.detailproject.task;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 类描述：红船号
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/16 1652
 */

public class RedBoatTask extends APIGetTask<DraftDetailBean> {
    public RedBoatTask(LoadingCallBack<DraftDetailBean> callback) {
        super(callback);
    }

    @Override
    protected void onSetupParams(Object... params) {
        if (params != null && params.length > 0) {
            put("artilce_id", params[0]);
        }
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.REDBOAT_NEWS_DETAIL;
    }
}
