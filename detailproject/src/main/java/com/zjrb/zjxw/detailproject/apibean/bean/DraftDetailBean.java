package com.zjrb.zjxw.detailproject.apibean.bean;


import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

import cn.daily.news.biz.core.model.BaseData;

/**
 * 稿件详情bean
 * Created by wanglinjie.
 * create time:2017/7/17  上午9:57
 */

public class DraftDetailBean extends BaseData {

    private static final long serialVersionUID = -1884972278517660728L;
    private ArticleBean article;
    public boolean isShareItem;

    public ArticleBean getArticle() {
        return article;
    }

    public void setArticle(ArticleBean article) {
        this.article = article;
    }

    public static class ArticleBean implements Serializable {

        private static final long serialVersionUID = -5637772409457637056L;
        private int id;
        private int mlf_id;
        public long guid;
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
        private String card_url; // 新闻卡片图片分享链接
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
        private long live_start;
        private long live_end;
        private String live_url;
        private String video_url;
        private int video_duration;
        private double video_size;
        private long topic_start;
        private long topic_end;
        private String subject_focus_url;
        private String subject_focus_image;
        private String summary;
        private boolean followed;
        private boolean column_subscribed;
        private int comment_level;
        private boolean like_enabled;
        private boolean liked;
        private List<String> list_pics;
        private List<String> web_subject_list_pics;
        private String subject_focus_description;

        private String column_logo;

        private String source_channel_id;
        private String source_channel_name;
        private String channel_code;
        private String source;

        private String from_channel;
        private String timeline;
        private int video_type;
        private boolean topped;
        private boolean native_live;
        public boolean traced;//专题是否追踪 6.1版本添加
        public boolean allow_repeat_like;//是否允许重复点赞 6.1版本添加
        public boolean live_bullet_screen;//是否显示弹幕按钮 6.1添加

        //6.1版本 打榜相关
        public boolean rank_hited;
        public String rank_share_url;
        public String rank_card_url;
        public int hit_rank_count;
        public String hit_rank_count_general;
        /**
         * image_url : http://www.zjol.com.cn/picture.jpg
         * description : 图集文字说明
         */

        private List<AlbumImageListBean> album_image_list;
        private List<String> topic_hosts;
        private List<String> topic_guests;

        private List<SpecialGroupBean> subject_groups;
        /**
         * live_id : 100060
         * title : 花灯水灯 七彩汤圆 浙里元宵浙样过
         * reporter : 浙视频
         * intro : 2月19日，农历元月十五元宵节，浙视频记者带你感受浙江杭州、宁波、嘉兴、绍兴四地不同的元宵习俗，手把手教你七彩汤圆，看非遗表演，游乌镇水上灯会，来浙里和我们一起闹元宵。
         * 直播时间：2019年2月19日14:00-18:00
         * （记者 周莎莎 钱璐斌 徐文迪 周旭辉 柳蓬 孙坤 马佳妮 贺元凯 应磊 郑培庚 赵晶晶 王志杰 编辑 彭鹏 张敏娴 导播 吴耀斌）
         * cover : https://mzvideo.8531.cn/1550504273529客户端.jpg?5c6ad151eaf5b&imageMogr2/crop/!1917x1078a0a1
         * stream_url : https://pili-live-hls.8531.cn/mlflive/live100060-0-1550504360.m3u8
         * stream_status : 2
         * playback_url : https://v-spyx.8531.cn/live/4143/1550572143
         * live_duration : 234
         */

        //直播原生信息
        private NativeLiveInfoBean native_live_info;

        //栏目详情页url
        private String column_url;

        public String getFrom_channel() {
            return from_channel;
        }

        public void setFrom_channel(String from_channel) {
            this.from_channel = from_channel;
        }

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

        public long getGuid() {
            return guid;
        }

        public void setGuid(long guid) {
            this.guid = guid;
        }

        public String getCard_url() {
            return card_url;
        }

        public void setCard_url(String card_url) {
            this.card_url = card_url;
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
         * 群众之声列表
         */
        private List<SubjectVoiceMassBean> subject_comment_list;

        /**
         * 话题互动列表
         */
        private List<HotCommentsBean> topic_comment_list;

        /**
         * 话题互动精选
         */
        private List<HotCommentsBean> topic_comment_select;

        public boolean isNative_live() {
            return native_live;
        }

        public void setNative_live(boolean native_live) {
            this.native_live = native_live;
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
            return topic_start;
        }

        public void setTopic_date_start(long topic_date_start) {
            this.topic_start = topic_date_start;
        }

        public long getTopic_date_end() {
            return topic_end;
        }

        public void setTopic_date_end(long topic_date_end) {
            this.topic_end = topic_date_end;
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

        public List<String> getWeb_subject_list_pics() {
            return web_subject_list_pics;
        }

        public void setWeb_subject_list_pics(List<String> web_subject_list_pics) {
            this.web_subject_list_pics = web_subject_list_pics;
        }

        //专题列表图第一张
        public String getFirstSubjectPic() {
            if (!web_subject_list_pics.isEmpty()) {
                return web_subject_list_pics.get(0);
            }
            return "";
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

        public List<SubjectVoiceMassBean> getSubject_comment_list() {
            return subject_comment_list;
        }

        public void setSubject_comment_list(List<SubjectVoiceMassBean> subject_comment_list) {
            this.subject_comment_list = subject_comment_list;
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

        public String getTimeline() {
            return timeline;
        }

        public void setTimeline(String timeline) {
            this.timeline = timeline;
        }

        public int getVideo_type() {
            return video_type;
        }

        public void setVideo_type(int video_type) {
            this.video_type = video_type;
        }

        public NativeLiveInfoBean getNative_live_info() {
            return native_live_info;
        }

        public void setNative_live_info(NativeLiveInfoBean native_live_info) {
            this.native_live_info = native_live_info;
        }

        public boolean isTopped() {
            return topped;
        }

        public void setTopped(boolean topped) {
            this.topped = topped;
        }


        public String getColumn_url() {
            return column_url;
        }

        public void setColumn_url(String column_url) {
            this.column_url = column_url;
        }

        public long getLive_start() {
            return live_start;
        }

        public void setLive_start(long live_start) {
            this.live_start = live_start;
        }

        public long getLive_end() {
            return live_end;
        }

        public void setLive_end(long live_end) {
            this.live_end = live_end;
        }

        //原生直播
        public static class NativeLiveInfoBean implements Serializable {
            private static final long serialVersionUID = -6337097775485082698L;
            private long live_id;
            private String title;
            private String reporter;
            private String intro;
            private String cover;
            private String stream_url;
            private int stream_status;//0 未开始 1 进行中 2 已结束
            private String playback_url;
            private int live_duration;

            public long getLive_id() {
                return live_id;
            }

            public void setLive_id(long live_id) {
                this.live_id = live_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getReporter() {
                return reporter;
            }

            public void setReporter(String reporter) {
                this.reporter = reporter;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getStream_url() {
                return stream_url;
            }

            public void setStream_url(String stream_url) {
                this.stream_url = stream_url;
            }

            public int getStream_status() {
                return stream_status;
            }

            public void setStream_status(int stream_status) {
                this.stream_status = stream_status;
            }

            public String getPlayback_url() {
                return playback_url;
            }

            public void setPlayback_url(String playback_url) {
                this.playback_url = playback_url;
            }

            public int getLive_duration() {
                return live_duration;
            }

            public void setLive_duration(int live_duration) {
                this.live_duration = live_duration;
            }
        }
    }

}
