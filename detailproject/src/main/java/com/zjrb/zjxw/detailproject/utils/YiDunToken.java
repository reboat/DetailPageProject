package com.zjrb.zjxw.detailproject.utils;

import android.os.Handler;
import android.text.TextUtils;

import com.zjrb.zjxw.detailproject.apibean.task.AntiCheatingTask;

import cn.daily.news.biz.core.constant.Key;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.utils.YiDunUtils;


public class YiDunToken {
    /**
     * 3秒后上传携带易盾反作弊token的网络请求
     *
     * @param id 稿件id
     */
    public static void synYiDunToken(final String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AntiCheatingTask(new APICallBack<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        return;
                    }
                }).exe(id, YiDunUtils.getToken(Key.YiDun.Type.READING));
            }
        }, 3000);
    }
}
