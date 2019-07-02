package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.utils.global.APIManager;

import cn.daily.news.biz.core.network.compatible.APIPostTask;

/**
 * 稿件点赞 - Task
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class DraftMultyPraiseTask extends APIPostTask<Void> {

    public DraftMultyPraiseTask(LoadingCallBack<Void> callBack) {
        super(callBack);
    }

    /**
     * @param params id:稿件id(int)
     *               action:false 取消收藏  true:收藏
     *               urlScheme:标识红船号稿件与普通稿件
     */
    @Override
    public void onSetupParams(Object... params) {
        put("uri_scheme", params[0]);
        put("count",params[1]);
    }

    @Override
    public String getApi() {
        return "/api/favorite/repeat_like";
    }
}
