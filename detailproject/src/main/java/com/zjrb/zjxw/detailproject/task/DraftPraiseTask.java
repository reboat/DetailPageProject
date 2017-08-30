package com.zjrb.zjxw.detailproject.task;


import com.zjrb.coreprojectlibrary.api.base.APIPostTask;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;
import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;

/**
 * 稿件点赞 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftPraiseTask extends APIPostTask<BaseInnerData> {

    public DraftPraiseTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * id:稿件id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("id", params[0]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.DRAFT_PRAISEL;
    }
}
