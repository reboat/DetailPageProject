package com.zjrb.zjxw.detailproject.bean;


import com.zjrb.core.domain.base.BaseInnerData;

import java.util.List;

/**
 * 专题列表bean
 * Created by wanglinjie.
 * create time:2017/8/24  下午7:44
 */

public class SubjectListBean extends BaseInnerData {

    /**
     * id : 123456
     * list_title : 浙江省第十四次党代会胜利闭幕
     * list_style : 0
     * list_pics : ["http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg","http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg","http://zjnews.zjol.com.cn/ztjj/ztddh/sddhmtbb/201706/W020170616654583491994.jpg"]
     * list_tag : 独家
     * doc_type : 0
     * read_count : 10000
     * like_count : 10000
     * comment_count : 10000
     * channel_id : 507f191e810c19729de860ea
     * channel_name : 头条
     * column_id : 507f191e810c19729de860ea
     * column_name : 党代会风采
     * sort_number : 12345678901234
     * url : https://zj.zjol.com.cn/news.html?id=123456
     * followed : true
     * column_subscribed : true
     * comment_level : 0
     * like_enabled : true
     * web_link : http://www.baidu.com
     * album_count : 6
     * activity_status : ONGOING
     * activity_date_begin : 1450000000000
     * activity_date_end : 1450000000000
     * activity_register_count : 10
     * activity_announced : true
     * live_type : VIDEO
     * live_status : ONGING
     * live_url : https://live.zjol.com.cn
     * video_url : https://v-cdn.zjol.com.cn/12345.mp4
     * video_duration : 123
     * topic_date_begin : 1450000000000
     * topic_date_end : 1450000000000
     */

    private List<ArticleListBean> article_list;

    public List<ArticleListBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<ArticleListBean> article_list) {
        this.article_list = article_list;
    }

    public static class ArticleListBean {
        private int id;
        private String list_title;
        private int list_style;
        private String list_tag;
        private int doc_type;
        private int read_count;
        private int like_count;
        private int comment_count;
        private String channel_id;
        private String channel_name;
        private String column_id;
        private String column_name;
        private long sort_number;
        private String url;
        private boolean followed;
        private boolean column_subscribed;
        private int comment_level;
        private boolean like_enabled;
        private String web_link;
        private int album_count;
        private String activity_status;
        private long activity_date_begin;
        private long activity_date_end;
        private int activity_register_count;
        private boolean activity_announced;
        private String live_type;
        private String live_status;
        private String live_url;
        private String video_url;
        private int video_duration;
        private long topic_date_begin;
        private long topic_date_end;
        private List<String> list_pics;

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

        public long getSort_number() {
            return sort_number;
        }

        public void setSort_number(long sort_number) {
            this.sort_number = sort_number;
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

        public int getAlbum_count() {
            return album_count;
        }

        public void setAlbum_count(int album_count) {
            this.album_count = album_count;
        }

        public String getActivity_status() {
            return activity_status;
        }

        public void setActivity_status(String activity_status) {
            this.activity_status = activity_status;
        }

        public long getActivity_date_begin() {
            return activity_date_begin;
        }

        public void setActivity_date_begin(long activity_date_begin) {
            this.activity_date_begin = activity_date_begin;
        }

        public long getActivity_date_end() {
            return activity_date_end;
        }

        public void setActivity_date_end(long activity_date_end) {
            this.activity_date_end = activity_date_end;
        }

        public int getActivity_register_count() {
            return activity_register_count;
        }

        public void setActivity_register_count(int activity_register_count) {
            this.activity_register_count = activity_register_count;
        }

        public boolean isActivity_announced() {
            return activity_announced;
        }

        public void setActivity_announced(boolean activity_announced) {
            this.activity_announced = activity_announced;
        }

        public String getLive_type() {
            return live_type;
        }

        public void setLive_type(String live_type) {
            this.live_type = live_type;
        }

        public String getLive_status() {
            return live_status;
        }

        public void setLive_status(String live_status) {
            this.live_status = live_status;
        }

        public String getLive_url() {
            return live_url;
        }

        public void setLive_url(String live_url) {
            this.live_url = live_url;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public int getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(int video_duration) {
            this.video_duration = video_duration;
        }

        public long getTopic_date_begin() {
            return topic_date_begin;
        }

        public void setTopic_date_begin(long topic_date_begin) {
            this.topic_date_begin = topic_date_begin;
        }

        public long getTopic_date_end() {
            return topic_date_end;
        }

        public void setTopic_date_end(long topic_date_end) {
            this.topic_date_end = topic_date_end;
        }

        public List<String> getList_pics() {
            return list_pics;
        }

        public void setList_pics(List<String> list_pics) {
            this.list_pics = list_pics;
        }
    }
}
