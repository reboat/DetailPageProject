package com.zjrb.zjxw.detailproject.apibean.bean;


import com.zjrb.daily.news.bean.ArticleItemBean;

import java.util.List;

import cn.daily.news.biz.core.model.BaseData;

/**
 * 专题列表bean
 * Created by wanglinjie.
 * create time:2017/8/24  下午7:44
 */
public class SubjectListBean extends BaseData {

    private static final long serialVersionUID = -4208158912844658664L;
    private List<ArticleItemBean> article_list;

    public List<ArticleItemBean> getArticle_list() {
        return article_list;
    }

    public void setArticle_list(List<ArticleItemBean> article_list) {
        this.article_list = article_list;
    }

}