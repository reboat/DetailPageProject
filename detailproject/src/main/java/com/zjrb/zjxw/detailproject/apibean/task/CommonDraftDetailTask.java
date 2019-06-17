package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 通用非原生内部稿
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class CommonDraftDetailTask extends APIGetTask<DraftDetailBean> {

    public CommonDraftDetailTask(LoadingCallBack<DraftDetailBean> callBack) {
        super(callBack);
    }

    /**
     * @param params id:稿件id(int)
     */
    @Override
    public void onSetupParams(Object... params) {
        put("uri_scheme", params[0]);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.COMMON_DETAIL;
    }
}
