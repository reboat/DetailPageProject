package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;

/**
 * 详情页相关专题
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:12
 */

public class RelatedSubjectsBean implements Serializable {
    private int id;
    private String title;
    private String pic;
    private String uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUri_scheme() {
        return uri;
    }

    public void setUri_scheme(String uri) {
        this.uri = uri;
    }
}
