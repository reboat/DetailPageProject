package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

/**
 * 评论删除
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class CommentDeleteTask extends APIPostTask<Void> {
    public CommentDeleteTask(LoadingCallBack<Void> callBack) {
        super(callBack);
    }

    /**
     * @param params comment_id:评论id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("comment_id", params[0]);
    }

    @Override
    protected String getApi() {
        return APIManager.endpoint.COMMENT_DELETE;
    }
}
