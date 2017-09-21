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
    //图片ID
    public int resId;
    //文字描述
    public String content;
    //分享平台
    private SHARE_MEDIA platform;

    public SHARE_MEDIA getPlatform() {
        return platform;
    }

    public DetailShareBean(int resId, @NonNull String content, SHARE_MEDIA platform) {
        this.resId = resId;
        this.content = content;
        this.platform = platform;
    }

    public int getResId() {
        return resId;
    }

    public String getContent() {
        return content;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setContent(String content) {
        this.content = content;
    }


}