package com.zjrb.zjxw.detailproject.callback;

import android.content.Context;

import com.zjrb.zjxw.detailproject.apibean.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;

import cn.daily.news.analytics.Analytics;

/**
 * 详情页埋点契约接口封装
 * Created by wanglinjie.
 * create time:2018/5/31  下午2:23
 */

public interface DetailWMHelperInterFace {

    /**
     * 详情页埋点
     */
    interface NewsDetailWM {

        /**
         * 订阅相关埋点
         *
         * @param eventNme
         * @param eventCode
         * @param scEventName
         * @param operationType
         */
        void SubscribeAnalytics(DraftDetailBean bean, String eventNme, String eventCode, String scEventName, String operationType);

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

        /**
         * 点击正文底部频道名称
         *
         * @param bean
         */
        void ClickRelatedContent(DraftDetailBean bean);

        /**
         * 点击稿件标题下频道名称
         *
         * @param bean
         */
        void ClickMiddleChannel(DraftDetailBean bean);

        /**
         * 点击精选的全部按钮
         *
         * @param bean
         */
        void ClickCommentAll(DraftDetailBean bean);

        /**
         * 相关新闻点击
         *
         * @param bean
         * @param beanRelate
         */
        void ClickRelatedNews(DraftDetailBean bean, RelatedNewsBean beanRelate);

        /**
         * 相关专题点击
         *
         * @param bean
         * @param beanRelate
         */
        void ClickRelatedSpecial(DraftDetailBean bean, RelatedSubjectsBean beanRelate);

        /**
         * 点击查看更多评论
         *
         * @param bean
         */
        void ClickMoreComment(DraftDetailBean bean);
    }

    /**
     * 创建评论埋点
     */
    interface DetailCommentBuild {
        Analytics CreateCommentAnalytics(DraftDetailBean bean);
    }

    /**
     * 图集详情页埋点
     */
    interface AtlasDetailWM {
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

        /**
         * 点击分享按钮
         *
         * @param bean
         */
        void ClickShareTab(DraftDetailBean bean);

        /**
         * 更多图集页面，点击单个图集稿件
         *
         * @param bean
         */
        void ClickMoreImgItem(RelatedNewsBean bean);
    }

    /**
     * 专题稿埋点
     */
    interface SpercialDetailWM {

        /**
         * 时长统计
         *
         * @param mArticle
         * @return
         */
        Analytics pageStayTimeSpecial(DraftDetailBean.ArticleBean mArticle);

        /**
         * 分类品频道点击
         *
         * @param bean
         */
        void ClickChannel(DraftDetailBean.ArticleBean mArticle, SpecialGroupBean bean);

        /**
         * 点击专题列表
         *
         * @param bean
         */
        void ClickSpecialItem(ArticleItemBean bean);

        /**
         * 点击收藏（收藏/取消收藏）
         */
        void ClickCollect(DraftDetailBean.ArticleBean mArticle);

        /**
         * 专题详情页，更多按钮点击
         *
         * @param bean
         */
        void SpecialClickMore(DraftDetailBean bean);

        /**
         * 专题详情页，焦点图点击
         *
         * @param bean
         */
        void SpecialFocusImgClick(DraftDetailBean.ArticleBean bean);

        /**
         * 点击更多进入专题列表页面后，新闻列表点击
         *
         * @param bean
         */
        void SpecialMoreClickSpecialItem(com.zjrb.daily.news.bean.ArticleItemBean bean);

        /**
         * 评论关联新闻点击
         *
         * @param bean
         */
        void SpecialCommentNewsClick(HotCommentsBean bean);
    }

    /**
     * 评论页埋点
     */
    interface CommentWM {
        /**
         * 评论框点击
         *
         * @param bean
         */
        void AppTabCommentClick(DraftDetailBean bean);

        /**
         * 点击分享
         *
         * @param bean
         */
        void AppTabClick(DraftDetailBean bean);

        /**
         * 发表评论
         *
         * @param bean
         */
        void UpdateComment(DraftDetailBean bean);

        /**
         * 删除评论
         *
         * @param bean
         */
        void DeletedComment(DraftDetailBean bean, String pageType, String scPageType, String id);

        /**
         * 热门评论点击
         *
         * @param bean
         */
        void HotCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id);

        /**
         * 最新评论点击回复
         */
        void NewCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id);

        /**
         * 评论点赞
         *
         * @param bean
         * @param pageType
         * @param scPageType
         * @param id
         */
        void CommentPrise(DraftDetailBean bean, String pageType, String scPageType, String id);

        /**
         * 创建发送评论对象
         *
         * @param bean
         * @return
         */
        Analytics CreateCommentSend(DraftDetailBean bean, String pageType, String scPageType, String id);
    }

    /**
     * 官员页埋点
     */
    interface PersionalWM {
        /**
         * 官员详情页点击更多
         *
         * @param context
         */
        void ClickMore(Context context);

        /**
         * 点击官员任职履历标签
         */
        void OfficialDetailClick(OfficalDetailBean bean);

        /**
         * 点击官员相关新闻
         *
         * @param bean
         */
        void OfficialNewsClick(OfficalDetailBean bean);

        /**
         * 官员页面点击分享
         */
        void OfficalClickShare(OfficalDetailBean bean);

        /**
         * 官员相关新闻列表点击
         */
        void RelateNewsClick(OfficalDetailBean bean);

        /**
         * 打开单个官员详情页
         *
         * @return
         */
        Analytics.AnalyticsBuilder CreateOfficalAnalytic(OfficalDetailBean data);
    }

    interface LiveWM {

        /**
         * 视频TAB点击
         *
         * @param bean
         */
        void VideoTabClick(DraftDetailBean bean);

        /**
         * 直播TAB点击
         *
         * @param bean
         */
        void LiveTabClick(DraftDetailBean bean);

        /**
         * 简介TAB点击
         *
         * @param bean
         */
        void SummaryTabClick(DraftDetailBean bean);

        /**
         * 视频评论TAB点击
         *
         * @param bean
         */
        void VideoCommentTabCLick(DraftDetailBean bean);

        /**
         * 直播评论点击
         *
         * @param bean
         */
        void LiveCommentTabClick(DraftDetailBean bean);

        /**
         * 排序方式点击
         *
         * @param bean
         */
        void SortClick(DraftDetailBean bean);


    }
}
