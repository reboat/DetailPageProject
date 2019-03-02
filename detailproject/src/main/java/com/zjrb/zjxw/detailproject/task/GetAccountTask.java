package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.NewAccountBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 获取用户详情
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class GetAccountTask extends APIGetTask<NewAccountBean> {

    public GetAccountTask(LoadingCallBack<NewAccountBean> callBack) {
        super(callBack);
    }

    @Override
    public void onSetupParams(Object... params) {
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.ACCOUNT_DETAIL;
    }
}
