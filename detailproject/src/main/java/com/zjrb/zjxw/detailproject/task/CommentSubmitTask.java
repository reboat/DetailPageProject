package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIPostTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.domain.base.BaseInnerData;

/**
 * 提交文章评论
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:18
 */

public class CommentSubmitTask extends APIPostTask<BaseInnerData> {
    public CommentSubmitTask(LoadingCallBack<BaseInnerData> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * channel_article_id：稿件id(int)
     * content：评论内容(string)
     * parent_id：被回复的评论id(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("channel_article_id", params[0]);
        put("content", params[1]);
        put("parent_id", params[2]);
    }

    @Override
    protected String getApi() {
        return null;
//        return APIManager.endpoint.ARTICLE_COMMENT;
    }
}
