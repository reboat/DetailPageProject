package com.zjrb.zjxw.detailproject.apibean.task;


import com.core.network.callback.ApiCallback;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
    直播订阅
 */
public class SpecialUnDoFollowTask extends APIGetTask<Void> {

    public SpecialUnDoFollowTask(ApiCallback<Void> mCallBack) {
        super(mCallBack);
    }

    @Override
    public void onSetupParams(Object... params) {
        if (params!=null&&params.length>0&&params[0]!=null){
            put("uri_scheme",params[0]);
        }
    }

    @Override
    public String getApi() {
        return "/api/favorite/subject_cancel_subscribe";
    }

}
