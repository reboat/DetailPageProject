package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIPostTask;

/**
 * 稿件点赞 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftPraiseTask extends APIPostTask<Void> {

    public DraftPraiseTask(LoadingCallBack<Void> callBack) {
        super(callBack);
    }

    /**
     * @param params id:稿件id(int)
     *               action:false 取消收藏  true:收藏
     *               urlScheme:标识红船号稿件与普通稿件
     */
    @Override
    public void onSetupParams(Object... params) {
        put("id", params[0]);
        put("action",params[1]);
        put("url_scheme", params[2]);
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.DRAFT_LIKE;
    }
}
