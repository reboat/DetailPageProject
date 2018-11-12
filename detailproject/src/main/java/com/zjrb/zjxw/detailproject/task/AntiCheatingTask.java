package com.zjrb.zjxw.detailproject.task;

import com.zjrb.core.api.APIManager;
import com.zjrb.core.api.base.APIGetTask;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.zjxw.detailproject.bean.AntiCheatingBean;

public class AntiCheatingTask extends APIGetTask<AntiCheatingBean> {

    public AntiCheatingTask(LoadingCallBack<AntiCheatingBean> callBack){
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
