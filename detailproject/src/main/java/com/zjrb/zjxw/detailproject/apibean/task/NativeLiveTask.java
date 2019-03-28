package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.NativeLiveBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 直播列表
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class NativeLiveTask extends APIGetTask<NativeLiveBean> {
    public NativeLiveTask(LoadingCallBack<NativeLiveBean> callBack) {
        super(callBack);
    }

    @Override
    public void onSetupParams(Object... params) {
        put("live_id", params[0]);
        if (params.length > 0) {
            if (params[1] != null && (int)params[1] != -1) {
                put("start", params[1]);
            }
            if (params.length > 2 && params[3] != null) {
                put("reverse", params[3]);
            }
        }
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.REALTIME_LIST;
    }
}
