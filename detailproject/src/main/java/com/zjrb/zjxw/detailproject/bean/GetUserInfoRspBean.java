package com.zjrb.zjxw.detailproject.bean;

import java.io.Serializable;

/**
 * 获取当前用户信息
 * Created by wanglinjie.
 * create time:2019/3/2  上午10:39
 */
public class GetUserInfoRspBean implements Serializable {

    private static final long serialVersionUID = 5334396590102828241L;
    private String code;

    private GetUserInfoRspBean.DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GetUserInfoRspBean.DataBean getData() {
        return data;
    }

    public void setData(GetUserInfoRspBean.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private NewAccountBean bean;
        private String login;
        private String mobile;
        private String timestamp;
        private String signature;

        public NewAccountBean getBean() {
            return bean;
        }

        public void setBean(NewAccountBean bean) {
            this.bean = bean;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

}
