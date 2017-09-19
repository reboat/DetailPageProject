package com.zjrb.zjxw.detailproject.bean;

/**
 * 话题互动评论列表
 * Created by wanglinjie.
 * create time:2017/9/19  上午11:34
 */

public class TopicCommentListBean {

    /**
     * id : 597ad67b2c1d4007315ce9b1
     * channel_article_id : 27
     * content : 浙江
     * nick_name : 读者+4
     * created_at : 1501222523000
     * like_count : 0
     * account_id : 0596d77b9b2e3f43afc63ad5a
     * account_type : 0
     * status : 2
     * own : true
     * liked : false
     * portrait_url : https://img6.bdstatic.com/img/image/smallpic/yangmixiaotugengxin.jpg
     * parent_id : 597ad67b2c1d4007315ce9b1
     * parent_content : 浙江
     * parent_nick_name : 读者+QONqts
     * parent_created_at : 1501222523000
     * parent_like_count : 1
     * parent_account_id : 59841424f9253e20e02c186c
     * parent_liked : false
     * parent_portrait_url : https://www.baidu.com/s?rs1481483f05744f83a726e
     */

    private String id;
    private int channel_article_id;
    private String content;
    private String nick_name;
    private long created_at;
    private int like_count;
    private String account_id;
    private int account_type;
    private int status;
    private boolean own;
    private boolean liked;
    private String portrait_url;
    private String parent_id;
    private String parent_content;
    private String parent_nick_name;
    private long parent_created_at;
    private int parent_like_count;
    private String parent_account_id;
    private boolean parent_liked;
    private String parent_portrait_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChannel_article_id() {
        return channel_article_id;
    }

    public void setChannel_article_id(int channel_article_id) {
        this.channel_article_id = channel_article_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_content() {
        return parent_content;
    }

    public void setParent_content(String parent_content) {
        this.parent_content = parent_content;
    }

    public String getParent_nick_name() {
        return parent_nick_name;
    }

    public void setParent_nick_name(String parent_nick_name) {
        this.parent_nick_name = parent_nick_name;
    }

    public long getParent_created_at() {
        return parent_created_at;
    }

    public void setParent_created_at(long parent_created_at) {
        this.parent_created_at = parent_created_at;
    }

    public int getParent_like_count() {
        return parent_like_count;
    }

    public void setParent_like_count(int parent_like_count) {
        this.parent_like_count = parent_like_count;
    }

    public String getParent_account_id() {
        return parent_account_id;
    }

    public void setParent_account_id(String parent_account_id) {
        this.parent_account_id = parent_account_id;
    }

    public boolean isParent_liked() {
        return parent_liked;
    }

    public void setParent_liked(boolean parent_liked) {
        this.parent_liked = parent_liked;
    }

    public String getParent_portrait_url() {
        return parent_portrait_url;
    }

    public void setParent_portrait_url(String parent_portrait_url) {
        this.parent_portrait_url = parent_portrait_url;
    }
}
