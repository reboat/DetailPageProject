package com.zjrb.zjxw.detailproject.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 专题列表bean
 * Created by wanglinjie.
 * create time:2017/8/24  下午7:44
 */

public class SubjectListBean implements Serializable {

    private List<SubjectItemBean> article_list;

    public List<SubjectItemBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<SubjectItemBean> article_list) {
        this.article_list = article_list;
    }

}
