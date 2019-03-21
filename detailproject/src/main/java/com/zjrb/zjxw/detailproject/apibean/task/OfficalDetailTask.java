package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

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
    public void onSetupParams(Object... params) {
        put("id", params[0]);
        //下拉刷新默认不传时间戳
        if (params.length > 1 && params[1] != null) {
            put("start", params[1]);
        }
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.OFFICAL_DETAIL;
    }
}
