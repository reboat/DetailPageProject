package com.zjrb.zjxw.detailproject.bean;


import android.text.TextUtils;

import com.zjrb.core.domain.base.BaseData;

import java.io.Serializable;
import java.util.List;

/**
 * 稿件详情bean
 * Created by wanglinjie.
 * create time:2017/7/17  上午9:57
 */

public class DraftDetailBean extends BaseData {

    private ArticleBean article;

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    public static class ArticleBean implements Serializable {

        private int id;
        private int mlf_id;
        private String list_title;
        /**
         * 详情页标题
         */
        private String doc_title;
        private int list_style;
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
        private int column_id;
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
        private long topic_date_start;
        private long topic_date_end;
        private String subject_focus_url;
        private String subject_focus_image;
        private String summary;
        private boolean followed;
        private boolean column_subscribed;
        private int comment_level;
        private boolean like_enabled;
        private boolean liked;
        private List<String> list_pics;
        private String subject_focus_description;

        private String column_logo;

        private String source_channel_id;
        private String source_channel_name;
        private String channel_code;
        private String source;
        /**
         * image_url : http://www.zjol.com.cn/picture.jpg
         * description : 图集文字说明
         */

        private List<AlbumImageListBean> album_image_list;
        private List<String> topic_hosts;
        private List<String> topic_guests;

        private List<SpecialGroupBean> subject_groups;

        public boolean isTopic_comment_has_more() {
            return topic_comment_has_more;
        }

        public void setTopic_comment_has_more(boolean topic_comment_has_more) {
            this.topic_comment_has_more = topic_comment_has_more;
        }

        public String getDoc_title() {
            return doc_title;
        }

        public void setDoc_title(String doc_title) {
            this.doc_title = doc_title;
        }

        /**
         * 话题互动评论精选是否有更多
         */
        private boolean topic_comment_has_more;
        /**
         * id : 597ad67b2c1d4007315ce9b1
         * channel_article_id : 27
         * content : 浙江
         * nick_name : 读者+4
         * created_at : 1501222523000
         * like_count : 0
         * account_id : 0596d77b9b2e3f43afc63ad5a
         * status : 2
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

        private List<HotCommentsBean> hot_comments;

        /**
         * id : 12345
         * title : 相关新闻标题
         * pic : https://www.zjol.com.cn/picture.jpg
         * url : https://zj.zjol.com.cn/news/123456.html
         */

        private List<RelatedNewsBean> related_news;
        /**
         * id : 12345
         * title : 相关专题标题1
         * pic : https://www.zjol.com.cn/picture.jpg
         * url : https://zj.zjol.com.cn/subject/123456.html
         */

        private List<RelatedSubjectsBean> related_subjects;


        /**
         * 话题互动列表
         */
        private List<HotCommentsBean> topic_comment_list;

        /**
         * 话题互动精选
         */
        private List<HotCommentsBean> topic_comment_select;

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

        public int getColumn_id() {
            return column_id;
        }

        public void setColumn_id(int column_id) {
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

        public long getTopic_date_start() {
            return topic_date_start;
        }

        public void setTopic_date_start(long topic_date_start) {
            this.topic_date_start = topic_date_start;
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

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
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

        public String getFirstPic() {
            if (list_pics != null && list_pics.size() > 0) {
                if (!TextUtils.isEmpty(list_pics.toString())) {
                    return list_pics.get(0);
                }
            }
            return "";
        }

        public void setList_pics(List<String> list_pics) {
            this.list_pics = list_pics;
        }

        public List<AlbumImageListBean> getAlbum_image_list() {
            return album_image_list;
        }

        public void setAlbum_image_list(List<AlbumImageListBean> album_image_list) {
            this.album_image_list = album_image_list;
        }

        public List<String> getTopic_hosts() {
            return topic_hosts;
        }

        public boolean isTopic_hostsEmpty() {
            if (topic_hosts != null && topic_hosts.size() != 0 && !topic_hosts.get(0).isEmpty()) {
                return false;
            } else {
                return true;
            }
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

        public boolean isTopic_guestsEmpty() {
            if (topic_guests != null && topic_guests.size() != 0 && !topic_guests.get(0).isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        public List<SpecialGroupBean> getSubject_groups() {
            return subject_groups;
        }

        public void setSubject_groups(List<SpecialGroupBean> subject_groups) {
            this.subject_groups = subject_groups;
        }

        public List<HotCommentsBean> getHot_comments() {
            return hot_comments;
        }

        public void setHot_comments(List<HotCommentsBean> hot_comments) {
            this.hot_comments = hot_comments;
        }

        public List<RelatedNewsBean> getRelated_news() {
            return related_news;
        }

        public void setRelated_news(List<RelatedNewsBean> related_news) {
            this.related_news = related_news;
        }

        public List<RelatedSubjectsBean> getRelated_subjects() {
            return related_subjects;
        }

        public void setRelated_subjects(List<RelatedSubjectsBean> related_subjects) {
            this.related_subjects = related_subjects;
        }

        public String getSubject_focus_description() {
            return subject_focus_description;
        }

        public void setSubject_focus_description(String subject_focus_description) {
            this.subject_focus_description = subject_focus_description;
        }

        public List<HotCommentsBean> getTopic_comment_select() {
            return topic_comment_select;
        }

        public void setTopic_comment_select(List<HotCommentsBean> topic_comment_select) {
            this.topic_comment_select = topic_comment_select;
        }

        public List<HotCommentsBean> getTopic_comment_list() {
            return topic_comment_list;
        }

        public void setTopic_comment_list(List<HotCommentsBean> topic_comment_list) {
            this.topic_comment_list = topic_comment_list;
        }

        public String getColumn_logo() {
            return column_logo;
        }

        public void setColumn_logo(String column_logo) {
            this.column_logo = column_logo;
        }


        public String getSource_channel_id() {
            return source_channel_id;
        }

        public void setSource_channel_id(String source_channel_id) {
            this.source_channel_id = source_channel_id;
        }

        public String getSource_channel_name() {
            return source_channel_name;
        }

        public void setSource_channel_name(String source_channel_name) {
            this.source_channel_name = source_channel_name;
        }

        public String getChannel_code() {
            return channel_code;
        }

        public void setChannel_code(String channel_code) {
            this.channel_code = channel_code;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        /**
         * docType
         * 2-9分别代表普通、链接、图集、专题、话题、活动、直播、视频
         */
        public static final class type {

            // 普通
            public static final int NOMAL = 2;

            // 链接
            public static final int LINK = 3;

            // 图集
            public static final int ALBUM = 4;
            // 专题
            public static final int SUBJECT = 5;

            // 话题
            public static final int TOPIC = 6;

            // 活动
            public static final int ACTIVITY = 7;

            // 直播
            public static final int LIVE = 8;

            // 视频
            public static final int VIDEO = 9;
        }

    }
}
