package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;

/**
 * 文章评论列表 Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class CommentListTask extends APIGetTask<CommentRefreshBean> {


    public CommentListTask(LoadingCallBack<CommentRefreshBean> callBack) {
        super(callBack);
    }

    /**
     * @param params
     * channel_article_id：稿件id(int)
     * start：最后一条评论时间戳(long)
     * size：分页条数(int)
     */
    @Override
    protected void onSetupParams(Object... params) {
        put("channel_article_id", params[0]);
        put("start",params[1]);
        put("size",params[2]);
    }

    @Override
    protected String getApi() {
        return "";
//        return APIManager.endpoint.ARTICLE_COMMENT_LIST;
    }
}
