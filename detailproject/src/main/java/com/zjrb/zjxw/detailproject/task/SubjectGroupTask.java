package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;

/**
 * 专题分组列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class SubjectGroupTask extends APIPostTask<SubjectListBean> {

    public SubjectGroupTask(LoadingCallBack<SubjectListBean> callBack) {
        super(callBack);
    }

    @Override
    protected void onSetupParams(Object... params) {
        put("group_id", params[0]);
        put("start", params[0]);
        put("size", params[0]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.DRAFT_DETAIL;
    }
}
