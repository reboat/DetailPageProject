package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 稿件收藏 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftCollectTask extends APIPostTask<Void> {

    public DraftCollectTask(LoadingCallBack<Void> callBack) {
        super(callBack);
    }

    /**
     * @param params id:稿件id(int)
     *               action:false 取消收藏  true:收藏
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("id", params[0]);
        put("action", params[1]);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.DRAFT_COLLECT;
    }
}
