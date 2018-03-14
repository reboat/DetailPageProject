package com.zjrb.zjxw.detailproject.task;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.api.okhttp.CachePolicy;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.APIManager;

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
    protected void onSetupParams(Object... params) {
        if (params != null && params.length > 0) {
            if (isRedBoat) put("artilce_id", params[0]);
            else {
                put("id", params[0]);
                put("from_channel", params[1]);
//            cachePolicy(CachePolicy.NO_CACHE);
            }
        }
    }

    @Override
    protected String getApi() {
        if (isRedBoat) return APIManager.endpoint.RED_BOAT_DETAIL;
        else return APIManager.endpoint.NEWS_DETAIL;
    }
}
