package com.zjrb.zjxw.detailproject.task;


import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.zjxw.detailproject.global.APIManager;

import cn.daily.news.biz.core.model.BaseData;
import cn.daily.news.biz.core.network.compatible.APIPostTask;

/**
 * 文章分享
 * Created by wanglinjie.
 * create time:2017/9/28  下午10:18
 */

public class ArticShareTask extends APIPostTask<BaseData> {
    public ArticShareTask(LoadingCallBack<BaseData> callBack) {
        super(callBack);
    }

    /**
     * @param params 参数
     */
    @Override
    public void onSetupParams(Object... params) {
        put("article_id", params[0]);
        put("url_scheme", params[1]); // 标识普通稿件或者红船号稿件
    }

    @Override
    public String getApi() {
        return APIManager.endpoint.ARTIC_SHARE;
    }
}
