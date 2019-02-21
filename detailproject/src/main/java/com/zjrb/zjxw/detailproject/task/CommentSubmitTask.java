package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.model.BaseData;
import cn.daily.news.biz.core.network.compatible.APIPostTask;

/**
 * 提交文章评论
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:18
 */

public class CommentSubmitTask extends APIPostTask<BaseData> {

    public CommentSubmitTask(LoadingCallBack<BaseData> callBack) {
        super(callBack);
    }

    /**
     * @param params channel_article_id：稿件id(int)
     *               content：评论内容(string)
     *               parent_id：被回复的评论id(int)
     *               location: 评论所在的地理位置  国家,省,市  以逗号分开
     */
    @Override
    public void onSetupParams(Object... params) {
        put("channel_article_id", params[0]);
        put("content", params[1]);
        put("parent_id", params[2]);
        put("location", params[3]);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.COMMENT_SUBMIT;
    }
}
