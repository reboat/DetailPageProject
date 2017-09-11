package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 评论删除
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class CommentDeleteTask extends APIPostTask<BaseInnerData> {
    public CommentDeleteTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * comment_id:评论id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("comment_id", "59b2458ed795400852356dba");
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.COMMENT_DELETE;
    }
}
