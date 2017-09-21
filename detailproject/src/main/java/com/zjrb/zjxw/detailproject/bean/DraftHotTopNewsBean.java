package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 稿件热门排行版
 * Created by wanglinjie.
 * create time:2017/9/19  下午3:31
 */

public class DraftHotTopNewsBean implements Serializable {

    private List<HotNewsBean> article_list;

    public List<HotNewsBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<HotNewsBean> article_list) {
        this.article_list = article_list;
    }


    public static class HotNewsBean {

        /**
         * id : 123456
         * list_title : 浙江省第十四次党代会胜利闭幕
         * list_style : 1
         * list_pic : http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg
         * list_tag : 独家
         * doc_type : 0
         * read_count : 10000
         * like_count : 10000
         * comment_count : 10000
         * read_count_general : 12万+
         * like_count_general : 1万
         * comment_count_general : 123
         * channel_id : 507f191e810c19729de860ea
         * channel_name : 头条
         * column_id : 507f191e810c19729de860ea
         * column_name : 党代会风采
         * url : https://zj.zjol.com.cn/news.html?id=123456
         * followed : true
         * column_subscribed : true
         * comment_level : 0
         * like_enabled : true
         * web_link : http://www.baidu.com
         */

        private int id;
        private String list_title;
        private int list_style;
        private String list_pic;
        private String list_tag;
        private int doc_type;
        private int read_count;
        private int like_count;
        private int comment_count;
        private String read_count_general;
        private String like_count_general;
        private String comment_count_general;
        private String channel_id;
        private String channel_name;
        private String column_id;
        private String column_name;
        private String url;
        private boolean followed;
        private boolean column_subscribed;
        private int comment_level;
        private boolean like_enabled;
        private String web_link;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getList_title() {
            return list_title;
        }

        public void setList_title(String list_title) {
            this.list_title = list_title;
        }

        public int getList_style() {
            return list_style;
        }

        public void setList_style(int list_style) {
            this.list_style = list_style;
        }

        public String getList_pic() {
            return list_pic;
        }

        public void setList_pic(String list_pic) {
            this.list_pic = list_pic;
        }

        public String getList_tag() {
            return list_tag;
        }

        public void setList_tag(String list_tag) {
            this.list_tag = list_tag;
        }

        public int getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(int doc_type) {
            this.doc_type = doc_type;
        }

        public int getRead_count() {
            return read_count;
        }

        public void setRead_count(int read_count) {
            this.read_count = read_count;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public String getRead_count_general() {
            return read_count_general;
        }

        public void setRead_count_general(String read_count_general) {
            this.read_count_general = read_count_general;
        }

        public String getLike_count_general() {
            return like_count_general;
        }

        public void setLike_count_general(String like_count_general) {
            this.like_count_general = like_count_general;
        }

        public String getComment_count_general() {
            return comment_count_general;
        }

        public void setComment_count_general(String comment_count_general) {
            this.comment_count_general = comment_count_general;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public String getColumn_id() {
            return column_id;
        }

        public void setColumn_id(String column_id) {
            this.column_id = column_id;
        }

        public String getColumn_name() {
            return column_name;
        }

        public void setColumn_name(String column_name) {
            this.column_name = column_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isFollowed() {
            return followed;
        }

        public void setFollowed(boolean followed) {
            this.followed = followed;
        }

        public boolean isColumn_subscribed() {
            return column_subscribed;
        }

        public void setColumn_subscribed(boolean column_subscribed) {
            this.column_subscribed = column_subscribed;
        }

        public int getComment_level() {
            return comment_level;
        }

        public void setComment_level(int comment_level) {
            this.comment_level = comment_level;
        }

        public boolean isLike_enabled() {
            return like_enabled;
        }

        public void setLike_enabled(boolean like_enabled) {
            this.like_enabled = like_enabled;
        }

        public String getWeb_link() {
            return web_link;
        }

        public void setWeb_link(String web_link) {
            this.web_link = web_link;
        }
    }

}
