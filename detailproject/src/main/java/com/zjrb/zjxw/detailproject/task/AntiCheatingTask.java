package com.zjrb.zjxw.detailproject.task;

import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;

public class AntiCheatingTask extends APIGetTask<Void> {

    public AntiCheatingTask(LoadingCallBack<Void> callBack){
        super(callBack);
    }

    @Override
    protected void onSetupParams(Object... params){
        put("article_id",params[0]);
        put("check_token",params[1]);
    }

    @Override
    protected String getApi(){
        return "/api/anti_cheating/read_news";
    }

}
