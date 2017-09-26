package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;

/**
 * 评论弹出框bean
 * Created by wanglinjie.
 * create time:2017/7/21  下午4:20
 */
public final class CommentDialogBean implements Serializable {
    public String id;
    public String parent_id;
    public String replayer;

    /**
     * 回复文章构造器
     *
     * @param id
     */
    public CommentDialogBean(String id) {
        this.id = id;
    }

    /**
     * 回复评论构造器
     *
     * @param id
     * @param parent_id
     * @param replayer
     */
    public CommentDialogBean(String id, String parent_id, String replayer) {
        this.id = id;
        this.parent_id = parent_id;
        this.replayer = replayer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getReplayer() {
        return replayer;
    }

    public void setReplayer(String replayer) {
        this.replayer = replayer;
    }

}