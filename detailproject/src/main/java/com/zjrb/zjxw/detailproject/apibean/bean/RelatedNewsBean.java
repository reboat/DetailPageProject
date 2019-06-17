package com.zjrb.zjxw.detailproject.apibean.bean;


import java.io.Serializable;

/**
 * 详情页相关新闻
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:12
 */

public class RelatedNewsBean implements Serializable {
    private static final long serialVersionUID = 5145682637366413595L;
    private int mlf_id;
    private int id;
    private String title;
    private String pic;
    private String url;
    private String column_name;
    private String like_count_general;

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
        return url;
    }

    public void setUri_scheme(String url) {
        this.url = url;
    }

    public int getMlf_id() {
        return mlf_id;
    }

    public void setMlf_id(int mlf_id) {
        this.mlf_id = mlf_id;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getLike_count_general() {
        return like_count_general;
    }

    public void setLike_count_general(String like_count_general) {
        this.like_count_general = like_count_general;
    }
}
