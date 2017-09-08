package com.zjrb.zjxw.detailproject.task;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 获取详情页
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class DraftDetailTask extends APIGetTask<DraftDetailBean> {

    public DraftDetailTask(LoadingCallBack<DraftDetailBean> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * id:稿件id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("id", "739652");
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.NEWS_DETAIL;
    }
}
