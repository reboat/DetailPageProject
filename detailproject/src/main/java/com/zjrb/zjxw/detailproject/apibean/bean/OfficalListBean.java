package com.zjrb.zjxw.detailproject.apibean.bean;



import java.util.List;

import cn.daily.news.biz.core.model.BaseData;

/**
 * 所有官员列表bean
 * Created by wanglinjie.
 * create time:2017/8/18  下午4:49
 */

public class OfficalListBean extends BaseData {
    private static final long serialVersionUID = -2245509129127203027L;
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
     * articles : [{"title":"标题信息","url":"https://zj.zjol.com.cn/news.html?id=123456"}]
     */

    private List<OfficerListBean> officer_list;

    public List<OfficerListBean> getOfficer_list() {
        return officer_list;
    }

    public void setOfficer_list(List<OfficerListBean> officer_list) {
        this.officer_list = officer_list;
    }

    public static class OfficerListBean {
        private int id;
        private String gender_text;
        private String name;
        private String gender;
        private String hometown;
        private String nation;
        private String university;
        private String degree;
        private String current_title;
        private long birthday;
        private long ccp_date;
        private long work_date;
        private String description;
        private String photo;
        private String list_pic;

        public String getGender_text() {
            return gender_text;
        }

        public void setGender_text(String gender_text) {
            this.gender_text = gender_text;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getDetail_url() {
            return detail_url;
        }

        public void setDetail_url(String detail_url) {
            this.detail_url = detail_url;
        }

        private String share_url;
        private String detail_url;
        /**
         * title : 标题信息
         * url : https://zj.zjol.com.cn/news.html?id=123456
         */

        private List<OfficalArticlesBean> article_list;

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
            return current_title;
        }

        public void setTitle(String title) {
            this.current_title = title;
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

        public List<OfficalArticlesBean> getArticles() {
            return article_list;
        }

        public void setArticles(List<OfficalArticlesBean> articles) {
            this.article_list = articles;
        }

    }

}
