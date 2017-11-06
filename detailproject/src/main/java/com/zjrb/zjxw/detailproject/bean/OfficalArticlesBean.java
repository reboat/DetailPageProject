package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;

/**
 * 官员列表稿件bean
 * Created by wanglinjie.
 * create time:2017/8/21  上午9:22
 */

public class OfficalArticlesBean implements Serializable {
    private String url;
    private String list_title;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return list_title;
    }

    public void setTitle(String title) {
        this.list_title = title;
    }
}
