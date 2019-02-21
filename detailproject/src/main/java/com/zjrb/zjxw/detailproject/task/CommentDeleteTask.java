package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIExpandCallBack;
import cn.daily.news.biz.core.network.compatible.APIPostTask;

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
    public void onSetupParams(Object... params) {
        put("comment_id", params[0]);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.COMMENT_DELETE;
    }
}
