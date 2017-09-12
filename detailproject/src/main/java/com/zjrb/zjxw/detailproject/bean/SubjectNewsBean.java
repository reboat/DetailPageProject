package com.zjrb.zjxw.detailproject.bean;


import java.util.List;

/**
 * 专题稿件bean
 * Created by wanglinjie.
 * create time:2017/8/1  下午5:24
 */

public class SubjectNewsBean {

    private List<GroupArticlesBean> group_articles;

    public List<GroupArticlesBean> getGroup_articles() {
        return group_articles;
    }

    public void setGroup_articles(List<GroupArticlesBean> group_articles) {
        this.group_articles = group_articles;
    }

    /**
     * 专题详情页分组数据
     */
    public static class GroupArticlesBean {

        private int group_id;
        private String group_name;

        private List<SubjectItemBean> articleList;

        public int getGroupId() {
            return group_id;
        }

        public void setGroupId(int groupId) {
            this.group_id = groupId;
        }

        public String getGroupName() {
            return group_name;
        }

        public void setGroupName(String groupName) {
            this.group_name = groupName;
        }

        public List<SubjectItemBean> getArticleList() {
            return articleList;
        }

        public void setArticleList(List<SubjectItemBean> articleList) {
            this.articleList = articleList;
        }
    }
}
