package com.zjrb.zjxw.detailproject.bean;


import com.zjrb.core.domain.base.BaseData;

import java.io.Serializable;
import java.util.List;

/**
 * 官员详情bean
 * Created by wanglinjie.
 * create time:2017/8/24  下午8:58
 */

public class OfficalDetailBean extends BaseData {

    /**
     * id : 1
     * name : 车俊
     * gender : 男
     * hometown : 籍贯
     * nation : 民族
     * university : 毕业院校
     * degree : 学历
     * title : 省委书记
     * birthday : 150000000000
     * ccp_date : 150000000000
     * work_date : 150000000000
     * description : 官员简介说明
     * photo : http://www.123123.com/photo.png
     * list_pic : http://www.123123.com/photo.png
     * resumes : [{"year":2017,"month":7,"location":"杭州","title":"职务"},{"year":2017,"month":6,"location":"杭州","title":"职务"}]
     */

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

    private List<SubjectItemBean> article_list;

    public OfficerBean getOfficer() {
        return officer;
    }

    public void setOfficer(OfficerBean officer) {
        this.officer = officer;
    }

    public List<SubjectItemBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<SubjectItemBean> article_list) {
        this.article_list = article_list;
    }

    /**
     * 官员详情个人履历
     */
    public static class OfficerBean implements Serializable {
        private int id;
        private String name;
        private String gender;
        private String hometown;
        private String nation;
        private String university;
        private String degree;
        private String title;
        private long birthday;
        private long ccp_date;
        private long work_date;
        private String description;
        private String photo;
        private String list_pic;
        /**
         * year : 2017
         * month : 7
         * location : 杭州
         * title : 职务
         */

        private List<ResumesBean> resumes;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        /**
         * 官员详情页任职履历
         */
        public static class ResumesBean {
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
