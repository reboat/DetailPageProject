package com.zjrb.zjxw.detailproject.bean;

import android.support.annotation.NonNull;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.Serializable;

/**
 * 详情页内分享bean
 * Created by wanglinjie.
 * create time:2017/7/21  下午4:20
 */
public final class DetailShareBean implements Serializable {
    private static final long serialVersionUID = -2639717195612937715L;
    //文字描述
    public String content;
    //分享平台
    private SHARE_MEDIA platform;

    public SHARE_MEDIA getPlatform() {
        return platform;
    }

    public DetailShareBean(@NonNull String content, SHARE_MEDIA platform) {
        this.content = content;
        this.platform = platform;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


}