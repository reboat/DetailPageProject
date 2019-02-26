package com.zjrb.zjxw.detailproject.widget;


import java.io.Serializable;

/**
 * 评论Dialog - 所需信息
 *
 * @author a_liYa
 * @date 2017/9/27 下午4:06.
 */
public final class CommentDialogBean implements Serializable {

    public String id; // 稿件 id
    public String parent_id; // 被回复的评论id
    public String replayer; // 被回复评论的用户昵称

    /**
     * 回复文章构造器
     *
     * @param id
     */
    public CommentDialogBean(String id) {
        this.id = id;
    }

    //TODO WLJ
//    /**
//     * JS专用
//     *
//     * @param id
//     * @param callback
//     */
//    private WebJsCallBack callback;

//    public CommentDialogBean(String id, WebJsCallBack callback) {
//        this.id = id;
//        this.callback = callback;
//    }

//    public WebJsCallBack getCallback() {
//        return callback;
//    }

    public CommentDialogBean(String id, String parent_id, String replayer) {
        this.id = id;
        this.parent_id = parent_id;
        this.replayer = replayer;
    }

    private CommentDialogBean(Builder builder) {
        setId(builder.id);
        setParent_id(builder.parent_id);
        setReplayer(builder.replayer);
    }

    public static Builder newBuilder() {
        return new Builder();
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


    public static final class Builder {
        private String id;
        private String parent_id;
        private String replayer;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder parent_id(String val) {
            parent_id = val;
            return this;
        }

        public Builder replayer(String val) {
            replayer = val;
            return this;
        }

        public CommentDialogBean build() {
            return new CommentDialogBean(this);
        }
    }
}