package com.zjrb.zjxw.detailproject.bean;

import com.zjrb.coreprojectlibrary.domain.base.BaseInnerData;

/**
 * Created by wanglinjie.
 * create time:2017/8/26  下午2:41
 */

public class DraftShareBean extends BaseInnerData {

    /**
     * obtained : 5
     * balance : 105
     * popup : true
     * task : 分享新闻
     */

    private ScoreNotifyBean score_notify;

    public ScoreNotifyBean getScore_notify() {
        return score_notify;
    }

    public void setScore_notify(ScoreNotifyBean score_notify) {
        this.score_notify = score_notify;
    }

    public static class ScoreNotifyBean {
        private int obtained;
        private int balance;
        private boolean popup;
        private String task;

        public int getObtained() {
            return obtained;
        }

        public void setObtained(int obtained) {
            this.obtained = obtained;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public boolean isPopup() {
            return popup;
        }

        public void setPopup(boolean popup) {
            this.popup = popup;
        }

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }
    }
}
