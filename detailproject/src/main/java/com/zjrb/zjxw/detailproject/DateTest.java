package com.zjrb.zjxw.detailproject;


import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectVoiceMassBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglinjie.
 * create time:2019/3/12  下午5:27
 */
public class DateTest {
    private static DateTest mInstance;

    private DateTest() {
    }

    public static DateTest newInstance() {
        if (mInstance == null) {
            synchronized (DateTest.class) {
                if (mInstance == null) {
                    mInstance = new DateTest();
                }
            }
        }
        return mInstance;
    }

    //专题稿评论列表
    public List<SubjectVoiceMassBean> subjectDataTest() {
        SubjectVoiceMassBean bean = new SubjectVoiceMassBean();
        //一块评论
        List<HotCommentsBean> list = new ArrayList<>();
        //评论1
        HotCommentsBean a1 = new HotCommentsBean();
        a1.setAccount_id("1");
        a1.setAccount_type(3);
        a1.setChannel_article_id(121);
        a1.setContent("这是一篇测试文章");
        a1.setCreated_at(System.currentTimeMillis());
        a1.setId("123123");
        a1.setLike_count(9999);
        a1.setLiked(false);
        a1.setOwn(true);
        a1.setLocation("杭州市");
        a1.setNick_name("测试账号1");
        a1.setParent_account_id("2324");
        a1.setParent_account_type(2);
        a1.setParent_content("这是回复内容");
        a1.setParent_created_at(System.currentTimeMillis() + 1);
        a1.setParent_like_count(878);
        a1.setParent_liked(false);
        a1.setParent_own(false);
        a1.setParent_status(2);
        a1.setParent_nick_name("回复者111");
        a1.setUrl("http://10.100.62.28:8090/pages/viewpage.action?pageId=14779074");
        a1.setList_title("这是一篇测试标题");
        HotCommentsBean a2 = a1;
        HotCommentsBean a3 = a1;
        HotCommentsBean a4 = a1;
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        bean.setComment_list(list);
        //文章

        SubjectVoiceMassBean bean1 = bean;
        SubjectVoiceMassBean bean2 = bean;
        SubjectVoiceMassBean bean3 = bean;
        List<SubjectVoiceMassBean> allList = new ArrayList<>();
        allList.add(bean);
        allList.add(bean1);
        allList.add(bean2);
        allList.add(bean3);
        return allList;
    }
}
