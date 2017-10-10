package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 专题稿件bean
 * Created by wanglinjie.
 * create time:2017/8/1  下午5:24
 */

public class SubjectNewsBean implements Serializable {

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
    public static class GroupArticlesBean implements  Serializable{

        private int group_id;
        private String group_name;

        private List<ArticleItemBean> articleList;

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

        public List<ArticleItemBean> getArticleList() {
            return articleList;
        }

        public void setArticleList(List<ArticleItemBean> articleList) {
            this.articleList = articleList;
        }
    }
}
