package com.zjrb.zjxw.detailproject.bean;

import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;

import java.util.List;

/**
 * 专题详情页item
 * Created by wanglinjie.
 * create time:2017/8/1  下午9:14
 */

public class SubjectItemBean extends BaseInnerData {
    /**
     * id : 123456
     * mlf_id : 12346788
     * list_title : 浙江省第十四次党代会胜利闭幕
     * list_style : 1
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
     * url : https://zj.zjol.com.cn/news/123456.html
     * web_link : http://www.baidu.com
     * author : 浙江新闻客户端记者 xxxx
     * article_pic : http://www.zjol.com.cn/picture.jpg
     * published_at : 14000000000
     * content : BLAHBLAHBLAH
     * album_image_count : 6
     * album_image_list : [{"image_url":"http://www.zjol.com.cn/picture.jpg","description":"图集文字说明"}]
     * activity_status : 0
     * activity_date_begin : 1450000000000
     * activity_date_end : 1450000000000
     * activity_register_count : 10
     * activity_announced : true
     * live_type : 0
     * live_status : 0
     * live_url : https://live.zjol.com.cn
     * video_url : https://v-cdn.zjol.com.cn/12345.mp4
     * video_duration : 123
     * video_size : 1.2312312312312313E37
     * topic_date_begin : 1450000000000
     * topic_date_end : 1450000000000
     * topic_hosts : ["a","b","c"]
     * topic_guests : ["a","b","c"]
     * subject_focus_url : http://zj.zjol.com.cn/topic.html?id=123456
     * subject_focus_image : http://www.baidu.com/logo.png
     * subject_summary : 这是专题摘要，专题摘要
     * subject_pic : http://www.baidu.com/logo.png
     * followed : true
     * column_subscribed : true
     * comment_level : 0
     * like_enabled : true
     * liked : true
     */
    private int size;
    private int position;
    private int id;
    private int mlf_id;
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
    private String web_link;
    private String author;
    private String article_pic;
    private long published_at;
    private String content;
    private int album_image_count;
    private int activity_status;
    private long activity_date_begin;
    private long activity_date_end;
    private int activity_register_count;
    private boolean activity_announced;
    private int live_type;
    private int live_status;
    private String live_url;
    private String video_url;
    private int video_duration;
    private double video_size;
    private long topic_date_begin;
    private long topic_date_end;
    private String subject_focus_url;
    private String subject_focus_image;
    private String subject_summary;
    private String subject_pic;
    private int topic_status;
    private boolean followed;
    private boolean column_subscribed;
    private int comment_level;
    private boolean like_enabled;
    private boolean liked;
    private List<String> list_pics;
    /**
     * image_url : http://www.zjol.com.cn/picture.jpg
     * description : 图集文字说明
     */

    private List<String> topic_hosts;
    private List<String> topic_guests;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMlf_id() {
        return mlf_id;
    }

    public void setMlf_id(int mlf_id) {
        this.mlf_id = mlf_id;
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

    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticle_pic() {
        return article_pic;
    }

    public void setArticle_pic(String article_pic) {
        this.article_pic = article_pic;
    }

    public long getPublished_at() {
        return published_at;
    }

    public void setPublished_at(long published_at) {
        this.published_at = published_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAlbum_image_count() {
        return album_image_count;
    }

    public void setAlbum_image_count(int album_image_count) {
        this.album_image_count = album_image_count;
    }

    public int getActivity_status() {
        return activity_status;
    }

    public void setActivity_status(int activity_status) {
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

    public int getLive_type() {
        return live_type;
    }

    public void setLive_type(int live_type) {
        this.live_type = live_type;
    }

    public int getLive_status() {
        return live_status;
    }

    public void setLive_status(int live_status) {
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

    public double getVideo_size() {
        return video_size;
    }

    public void setVideo_size(double video_size) {
        this.video_size = video_size;
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

    public String getSubject_focus_url() {
        return subject_focus_url;
    }

    public void setSubject_focus_url(String subject_focus_url) {
        this.subject_focus_url = subject_focus_url;
    }

    public String getSubject_focus_image() {
        return subject_focus_image;
    }

    public void setSubject_focus_image(String subject_focus_image) {
        this.subject_focus_image = subject_focus_image;
    }

    public String getSubject_summary() {
        return subject_summary;
    }

    public void setSubject_summary(String subject_summary) {
        this.subject_summary = subject_summary;
    }

    public String getSubject_pic() {
        return subject_pic;
    }

    public void setSubject_pic(String subject_pic) {
        this.subject_pic = subject_pic;
    }

    public int getTopic_status() {
        return topic_status;
    }

    public void setTopic_status(int topic_status) {
        this.topic_status = topic_status;
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

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public List<String> getList_pics() {
        return list_pics;
    }

    public void setList_pics(List<String> list_pics) {
        this.list_pics = list_pics;
    }


    public List<String> getTopic_hosts() {
        return topic_hosts;
    }

    public void setTopic_hosts(List<String> topic_hosts) {
        this.topic_hosts = topic_hosts;
    }

    public List<String> getTopic_guests() {
        return topic_guests;
    }

    public void setTopic_guests(List<String> topic_guests) {
        this.topic_guests = topic_guests;
    }

}
