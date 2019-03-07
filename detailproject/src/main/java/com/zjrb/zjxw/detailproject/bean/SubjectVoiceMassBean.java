package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 群众之声
 * Created by wanglinjie.
 * create time:2019/3/7  上午8:50
 */
public class SubjectVoiceMassBean implements Serializable {

    private static final long serialVersionUID = -1266292793583101824L;
    /**
     * channel_article_id : 123456
     * list_title : 浙江省第十四次党代会胜利闭幕
     * url : https://zj.zjol.com.cn/news/123456.html
     * comment_list : [{"id":"5ad979fece672205923dc9a4","channel_article_id":802937,"content":"你jjlwwm","nick_name":"读友_2ejvxo","created_at":1524201982000,"sort_number":1524201982478,"like_count":0,"account_id":"5ad95eeece672205923dc98e","account_type":3,"own":true,"status":2,"liked":false,"portrait_url":"https://stc-new.8531.cn/assets/20171207/1512637911001_5a2905d7b4a13d1851b04e0f.png?w=174&h=174","location":"浙江省杭州市"},{"id":"5ad979fece672205923dc9a4","channel_article_id":802937,"content":"你jjlwwm","nick_name":"读友_2ejvxo","created_at":1524201982000,"sort_number":1524201982478,"like_count":0,"account_id":"5ad95eeece672205923dc98e","account_type":3,"own":true,"status":2,"liked":false,"portrait_url":"https://stc-new.8531.cn/assets/20171207/1512637911001_5a2905d7b4a13d1851b04e0f.png?w=174&h=174","location":"浙江省杭州市","parent_id":"5ad95cb7ce672205923dc985","parent_content":"gjjjl","parent_nick_name":"读友_2ejveo","parent_created_at":1524194488000,"parent_like_count":0,"parent_account_id":"5abb69399c880e69b8b07117","parent_account_type":3,"parent_own":false,"parent_status":3,"parent_liked":false,"parent_portrait_url":"https://stc-new.8531.cn/assets/20171207/1512637911001_5a2905d7b4a13d1851b04e0f.png?w=174&h=174","parent_location":"浙江省杭州市"}]
     */

    private int channel_article_id;
    private String list_title;
    private String url;
    /**
     * id : 5ad979fece672205923dc9a4
     * channel_article_id : 802937
     * content : 你jjlwwm
     * nick_name : 读友_2ejvxo
     * created_at : 1524201982000
     * sort_number : 1524201982478
     * like_count : 0
     * account_id : 5ad95eeece672205923dc98e
     * account_type : 3
     * own : true
     * status : 2
     * liked : false
     * portrait_url : https://stc-new.8531.cn/assets/20171207/1512637911001_5a2905d7b4a13d1851b04e0f.png?w=174&h=174
     * location : 浙江省杭州市
     */

    private List<HotCommentsBean> comment_list;

    public int getChannel_article_id() {
        return channel_article_id;
    }

    public void setChannel_article_id(int channel_article_id) {
        this.channel_article_id = channel_article_id;
    }

    public String getList_title() {
        return list_title;
    }

    public void setList_title(String list_title) {
        this.list_title = list_title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<HotCommentsBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<HotCommentsBean> comment_list) {
        this.comment_list = comment_list;
    }
}
