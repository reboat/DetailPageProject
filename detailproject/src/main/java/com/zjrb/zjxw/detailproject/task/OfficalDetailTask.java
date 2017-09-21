package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.global.APIManager;
import com.zjrb.zjxw.detailproject.global.C;

/**
 * 官员详情 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class OfficalDetailTask extends APIGetTask<OfficalDetailBean> {

    public OfficalDetailTask(LoadingCallBack<OfficalDetailBean> callBack) {
        super(callBack);
    }

    /**
     * @param params 参数
     *               start:上一次请求最后一条新闻的ID
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("id", "5");
        //下拉刷新默认不传时间戳
        if (params.length > 1) {
            put("start", params[1]);
        }
        put("size", C.PAGE_SIZE_OFFICAL);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.OFFICAL_DETAIL;
    }
}
