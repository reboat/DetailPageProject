package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 所有官员列表 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class OfficalListTask extends APIGetTask<OfficalListBean> {

    public OfficalListTask(LoadingCallBack<OfficalListBean> callBack) {
        super(callBack);
    }

    /**
     * @param params start:分页起始ID
     */
    @Override
    public void onSetupParams(Object... params) {
        if (params.length > 0 && params[0] != null) {
            put("start", params[0]);
        }
        put("size", 20);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.OFFICAL_LIST;
    }
}
