package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;

/**
 * 所有官员列表新闻bean
 * Created by wanglinjie.
 * create time:2017/8/21  上午9:22
 */

public class OfficalArticlesBean implements Serializable {
    private int type;
    private int officalId;
    private String job;
    private String url;
    private String photo;
    private String name;
    private String title;

    public int getOfficalId() {
        return officalId;
    }

    public void setOfficalId(int officalId) {
        this.officalId = officalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
