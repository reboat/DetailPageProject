package com.zjrb.zjxw.detailproject.task;


import com.zjrb.coreprojectlibrary.api.base.APIPostTask;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;
import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;

/**
 * 评论点赞
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */

public class CommentPraiseTask extends APIPostTask<BaseInnerData> {
    public CommentPraiseTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * comment_id:评论id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("comment_id", params[0]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.ARTICLE_COMMENT_PRAISE;
    }
}
