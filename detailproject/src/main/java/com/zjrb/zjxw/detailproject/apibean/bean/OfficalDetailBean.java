package com.zjrb.zjxw.detailproject.apibean.bean;


import java.io.Serializable;
import java.util.List;

import cn.daily.news.biz.core.model.BaseData;

/**
 * 官员详情bean
 * Created by wanglinjie.
 * create time:2017/8/24  下午8:58
 */

public class OfficalDetailBean extends BaseData {

    private static final long serialVersionUID = 6547375043486125354L;
    private OfficerBean officer;
    /**
     * id : 123456
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
     * url : https://zj.zjol.com.cn/news.html?id=123456
     * followed : true
     * column_subscribed : true
     * comment_level : 0
     * like_enabled : true
     * web_link : http://www.baidu.com
     * album_count : 6
     * activity_status : 0
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

    public OfficerBean getOfficer() {
        return officer;
    }

    public void setOfficer(OfficerBean officer) {
        this.officer = officer;
    }

    /**
     * 官员详情个人履历
     */
    public static class OfficerBean implements Serializable {
        private static final long serialVersionUID = 8064689528555545024L;
        private int id;
        private String name;
        private String gender;
        private String hometown;
        private String gender_text;
        private String nation;
        private String university;
        private String degree;
        private long birthday;
        private long ccp_date;
        private long work_date;
        private String description;
        private String photo;//官员详情页
        private String list_pic;//所有官员列表
        private String url;
        private String current_location;
        private String current_title;
        private String top_title;
        private String share_url;
        private List<ArticleItemBean> article_list;
        private String detail_url;
        /**
         * year : 2017
         * month : 7
         * location : 杭州
         * title : 职务
         */

        public List<ArticleItemBean> getArticle_list() {
            return article_list;
        }

        public void setArticle_list(List<ArticleItemBean> article_list) {
            this.article_list = article_list;
        }

        private List<ResumesBean> resumes;

        public String getDetail_url() {
            return detail_url;
        }

        public void setDetail_url(String detail_url) {
            this.detail_url = detail_url;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getTop_title() {
            return top_title;
        }

        public void setTop_title(String top_title) {
            this.top_title = top_title;
        }


        public String getCurrent_title() {
            return current_title;
        }

        public void setCurrent_title(String current_title) {
            this.current_title = current_title;
        }


        public String getCurrent_location() {
            return current_location;
        }

        public void setCurrent_location(String current_location) {
            this.current_location = current_location;
        }


        public String getGender_text() {
            return gender_text;
        }

        public void setGender_text(String gender_text) {
            this.gender_text = gender_text;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getUniversity() {
            return university;
        }

        public void setUniversity(String university) {
            this.university = university;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public long getCcp_date() {
            return ccp_date;
        }

        public void setCcp_date(long ccp_date) {
            this.ccp_date = ccp_date;
        }

        public long getWork_date() {
            return work_date;
        }

        public void setWork_date(long work_date) {
            this.work_date = work_date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getList_pic() {
            return list_pic;
        }

        public void setList_pic(String list_pic) {
            this.list_pic = list_pic;
        }

        public List<ResumesBean> getResumes() {
            return resumes;
        }

        public void setResumes(List<ResumesBean> resumes) {
            this.resumes = resumes;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * 官员详情页任职履历
         */
        public static class ResumesBean implements Serializable{
            private int year;
            private int month;
            private String location;
            private String title;
            //是否是同一年
            private boolean isSameYear;

            public boolean isSameYear() {
                return isSameYear;
            }

            public void setSameYear(boolean sameYear) {
                isSameYear = sameYear;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
