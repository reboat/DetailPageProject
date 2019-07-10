package com.zjrb.zjxw.detailproject.apibean.task;

import com.core.network.callback.ApiCallback;
import com.zjrb.zjxw.detailproject.apibean.bean.PromoteResponse;

import cn.daily.news.biz.core.network.compatible.APIPostTask;

//打榜接口
public class PromoteTask extends APIPostTask<PromoteResponse> {
    public PromoteTask(ApiCallback<PromoteResponse> callback) {
        super(callback);
    }

    @Override
    public void onSetupParams(Object... params) {
        put("column_id", params[0]);
    }

    @Override
    public String getApi() {
        return "/api/redboat_rank/promote";
    }
}
