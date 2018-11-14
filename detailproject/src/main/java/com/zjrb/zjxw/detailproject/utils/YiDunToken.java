package com.zjrb.zjxw.detailproject.utils;

import android.os.Handler;

import com.zjrb.core.api.callback.APICallBack;
import com.zjrb.zjxw.detailproject.bean.AntiCheatingBean;
import com.zjrb.zjxw.detailproject.task.AntiCheatingTask;

import cn.daily.news.biz.core.global.Key;
import cn.daily.news.biz.core.utils.YiDunUtils;

public class YiDunToken {
    /**
     * 3秒后上传携带易盾反作弊token的网络请求
     * @param id 稿件id
     */
    public static void synYiDunToken(String id){
        if(id == null || id == ""){
            return;
        }
        final String ArticleId = id;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AntiCheatingTask(new APICallBack<AntiCheatingBean>(){
                    @Override
                    public void onSuccess(AntiCheatingBean bean){
                        return;
                    }
                }).setTag(this).exe(ArticleId, YiDunUtils.getToken(Key.YiDun.Type.READING));
            }
        },3000);
    }
}
