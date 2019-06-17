package com.zjrb.zjxw.detailproject.apibean.bean;

import java.io.Serializable;

/**
 * Created by wanglinjie.
 * create time:2019/3/7  上午10:49
 */
public class SubjectCommentTitleBean implements Serializable {
    private static final long serialVersionUID = -8347400778205123575L;
    private int channel_article_id;
    private String list_title;
    private String url;

    public int getChannel_article_id() {
        return channel_article_id;
    }

    public void setChannel_article_id(int channel_article_id) {
        this.channel_article_id = channel_article_id;
    }

    public String getList_title() {
        return list_title;
    }

    public void setList_title(String list_title) {
        this.list_title = list_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
