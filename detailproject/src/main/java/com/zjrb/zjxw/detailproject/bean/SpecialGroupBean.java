package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 专题组 - JavaBean
 *
 * @author a_liYa
 * @date 2017/10/11 10:43.
 */
public class SpecialGroupBean implements Serializable {

    private String group_id;
    private String group_name;
    private boolean group_has_more;

    private List<ArticleItemBean> group_articles;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public boolean isGroup_has_more() {
        return group_has_more;
    }

    public void setGroup_has_more(boolean group_has_more) {
        this.group_has_more = group_has_more;
    }

    public List<ArticleItemBean> getGroup_articles() {
        return group_articles;
    }

    public void setGroup_articles(List<ArticleItemBean> group_articles) {
        this.group_articles = group_articles;
    }

}
