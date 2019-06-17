package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 获取详情页
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class DraftDetailTask extends APIGetTask<DraftDetailBean> {
    boolean isRedBoat;

    public void isRedBoat(boolean isRedBoat) {
        this.isRedBoat = isRedBoat;
    }

    public DraftDetailTask(LoadingCallBack<DraftDetailBean> callBack) {
        super(callBack);
    }

    /**
     * @param params id:稿件id(int)
     */
    @Override
    public void onSetupParams(Object... params) {
        if (params != null && params.length > 0) {
            if (isRedBoat) put("article_id", params[0]);
            else {
                put("id", params[0]);
                put("from_channel", params[1]);
            }
        }
    }

    @Override
    public String getApi() {
        if (isRedBoat) return APIManager.endpoint.REDBOAT_NEWS_DETAIL;
        else return APIManager.endpoint.NEWS_DETAIL;
    }
}
