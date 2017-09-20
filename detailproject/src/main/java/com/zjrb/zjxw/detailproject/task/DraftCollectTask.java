package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 稿件收藏 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftCollectTask extends APIPostTask<BaseInnerData> {

    public DraftCollectTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * id:稿件id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("id", "65527");
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.DRAFT_COLLECT;
    }
}
