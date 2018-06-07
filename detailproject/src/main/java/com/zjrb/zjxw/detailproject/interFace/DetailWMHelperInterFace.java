package com.zjrb.zjxw.detailproject.interFace;

import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import cn.daily.news.analytics.Analytics;

/**
 * 详情页埋点接口封装
 * Created by wanglinjie.
 * create time:2018/5/31  下午2:23
 */

public interface DetailWMHelperInterFace {

    /**
     * 红船号埋点
     */
    interface RedBoatWM {
        /**
         * 统计页面停留时长
         *
         * @param bean
         * @return
         */
        Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean);

        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

        /**
         * 订阅相关埋点
         *
         * @param eventNme
         * @param eventCode
         */
        void SubscribeAnalytics(String eventNme, String eventCode);
    }

    /**
     * 新闻详情页埋点
     */
    interface NewsDetailWM {

        /**
         * 订阅相关埋点
         *
         * @param eventNme
         * @param eventCode
         */
        void SubscribeAnalytics(String eventNme, String eventCode);


        /**
         * 统计页面停留时长
         *
         * @param bean
         * @return
         */
        Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean);

        /**
         * 进入评论列表
         *
         * @param bean
         */
        void ClickInCommentList(DraftDetailBean bean);

        /**
         * 点击点赞
         *
         * @param bean
         */
        void ClickPriseIcon(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 点击进入评论框
         *
         * @param bean
         */
        void ClickCommentBox(DraftDetailBean bean);

        /**
         * 点击分享
         *
         * @param bean
         */
        void ClickShare(DraftDetailBean bean);

        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);
    }

    /**
     * 图集详情页埋点
     */
    interface AtlasDetailWM {
        /**
         * 统计页面停留时长
         *
         * @param bean
         * @return
         */
        Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean);

        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

        /**
         * 点击进入评论框
         *
         * @param bean
         */
        void ClickCommentBox(DraftDetailBean bean);

        /**
         * 点击点赞
         *
         * @param bean
         */
        void ClickPriseIcon(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 点击下载
         *
         * @param bean
         */
        void ClickDownLoad(DraftDetailBean bean);

        /**
         * 图集滑动
         *
         * @param bean
         */
        void AtlasSlide(DraftDetailBean bean);

        /**
         * 打开更多图集
         *
         * @param bean
         */
        void ClickMoreImage(DraftDetailBean bean);
    }

    /**
     * 专题稿埋点
     */
    interface SpercialDetailWM {
        /**
         * 统计页面停留时长
         *
         * @param bean
         * @return
         */
        Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 点击焦点图
         *
         * @param bean
         */
        void ClickFocusImage(DraftDetailBean bean);

        /**
         * 点击专题列表
         *
         * @param bean
         */
        void ClickSpecialItem(DraftDetailBean bean);

        /**
         * 点击分享
         *
         * @param bean
         */
        void ClickShare(DraftDetailBean bean);

        /**
         * 点击收藏（收藏/取消收藏）
         *
         * @param isCollect
         * @param bean
         */
        void ClickCollect(boolean isCollect, DraftDetailBean bean);

        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

        /**
         * 分类标签点击
         *
         * @param bean
         */
        void ClickItemTag(DraftDetailBean bean);

    }

    /**
     * 话题稿埋点
     */
    interface TopicDetailWM {
        /**
         * 订阅相关埋点
         *
         * @param eventNme
         * @param eventCode
         */
        void SubscribeAnalytics(String eventNme, String eventCode);

        /**
         * 点击相关专题
         *
         * @param bean
         */
        void ClickRelatSubjectItem(DraftDetailBean bean);

        /**
         * 点击正文底部频道
         *
         * @param bean
         */
        void ClickChannel(DraftDetailBean bean);

        /**
         * 统计页面停留时长
         *
         * @param bean
         * @return
         */
        Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean);

        /**
         * 点击点赞
         *
         * @param bean
         */
        void ClickPriseIcon(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 点击进入评论框
         *
         * @param bean
         */
        void ClickCommentBox(DraftDetailBean bean);

        /**
         * 点击分享
         *
         * @param bean
         */
        void ClickShare(DraftDetailBean bean);

        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

    }

    /**
     * 链接稿埋点
     */
    interface LinkDetailWM {
        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

        /**
         * 进入评论列表
         *
         * @param bean
         */
        void ClickInCommentList(DraftDetailBean bean);

        /**
         * 点击点赞
         *
         * @param bean
         */
        void ClickPriseIcon(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 长按识别二维码
         *
         * @param bean
         */
        void LongClickScaner(DraftDetailBean bean);
    }

    /**
     * 直播链接稿埋点
     */
    interface LiveDetailWM {
        /**
         * 点击返回
         *
         * @param bean
         */
        void ClickBack(DraftDetailBean bean);

        /**
         * 点击分享
         *
         * @param bean
         */
        void ClickShare(DraftDetailBean bean);

        /**
         * 进入评论列表
         *
         * @param bean
         */
        void ClickInCommentList(DraftDetailBean bean);

        /**
         * 点击点赞
         *
         * @param bean
         */
        void ClickPriseIcon(DraftDetailBean bean);

        /**
         * 点击更多
         *
         * @param bean
         */
        void ClickMoreIcon(DraftDetailBean bean);

        /**
         * 点击进入评论框
         *
         * @param bean
         */
        void ClickCommentBox(DraftDetailBean bean);

        /**
         * 订阅相关埋点
         *
         * @param eventNme
         * @param eventCode
         */
        void SubscribeAnalytics(String eventNme, String eventCode);
    }
}
