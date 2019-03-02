package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;

import cn.daily.news.biz.core.model.AccountBean;

/**
 * 用户信息bean
 * Created by wanglinjie.
 * create time:2018/8/8  下午2:50
 */
public class NewAccountBean implements Serializable {

    private static final long serialVersionUID = 9197257698964195930L;
    /**
     * id : 59a9272bf7bf513f18a7bf9b
     * nick_name : 今天明天_1
     * ref_code : row3j7
     * ref_user_uid :
     * ref_user_code :
     * mobile : 139****6707
     * invitation_number : 0
     * image_url : http://pic44.nipic.com/20140717/12432466_121957328000_2.jpg
     * total_score : 0
     */

    private AccountBean account;

    public AccountBean getAccount() {
        return account;
    }

    public void setAccount(AccountBean account) {
        this.account = account;
    }

}
