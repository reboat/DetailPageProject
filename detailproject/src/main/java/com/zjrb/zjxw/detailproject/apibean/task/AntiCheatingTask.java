package com.zjrb.zjxw.detailproject.apibean.task;


import com.zjrb.core.load.LoadingCallBack;

import cn.daily.news.biz.core.network.compatible.APIGetTask;

/**
 * 网易易盾
 */
public class AntiCheatingTask extends APIGetTask<Void> {

    public AntiCheatingTask(LoadingCallBack<Void> callBack){
        super(callBack);
    }

    @Override
    public void onSetupParams(Object... params){
        put("article_id",params[0]);
        put("check_token",params[1]);
    }

    @Override
    public String getApi(){
        return "/api/anti_cheating/read_news";
    }

}
