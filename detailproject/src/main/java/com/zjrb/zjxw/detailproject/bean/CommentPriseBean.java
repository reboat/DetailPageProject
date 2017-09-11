package com.zjrb.zjxw.detailproject.bean;

import com.zjrb.core.domain.base.BaseInnerData;

/**
 * 评论点赞bean
 * Created by wanglinjie.
 * create time:2017/9/8  下午4:46
 */

public class CommentPriseBean extends BaseInnerData {

    /**
     * obtained : 5
     * balance : 122
     * popup : true
     * task : 发表评论
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
