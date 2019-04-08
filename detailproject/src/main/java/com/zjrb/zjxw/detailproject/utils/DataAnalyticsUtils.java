package com.zjrb.zjxw.detailproject.utils;

import android.content.Context;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.apibean.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.callback.DetailWMHelperInterFace;

import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 数据统计工具类
 * Created by wanglinjie.
 * create time:2019/3/19  下午2:15
 */
final public class DataAnalyticsUtils implements DetailWMHelperInterFace.NewsDetailWM,
        DetailWMHelperInterFace.DetailCommentBuild, DetailWMHelperInterFace.CommentWM,
        DetailWMHelperInterFace.PersionalWM, DetailWMHelperInterFace.AtlasDetailWM,
        DetailWMHelperInterFace.SpercialDetailWM, DetailWMHelperInterFace.LiveWM {
    private volatile static DataAnalyticsUtils mInstance;

    public static DataAnalyticsUtils get() {
        if (mInstance == null) {
            synchronized (DataAnalyticsUtils.class) {
                if (mInstance == null) {
                    mInstance = new DataAnalyticsUtils();
                }
            }
        }
        return mInstance;
    }

    /**********详情页***********************************************************************************************************************************/
    @Override
    public void SubscribeAnalytics(DraftDetailBean bean, String eventNme, String eventCode, String scEventName, String operationType) {
        new Analytics.AnalyticsBuilder(getContext(), eventCode, eventCode, scEventName, false)
                .setEvenName(eventNme)
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.ColumnType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("customObjectType", "RelatedColumnType")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pageType("新闻详情页")
                .operationType(operationType)
                .build()
                .send();
    }

    @Override
    public Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021", "ViewAppNewsDetail", true)
                .setEvenName("页面停留时长/阅读深度")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setUrl(bean.getArticle().getUrl())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "").newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .pubUrl(bean.getArticle().getUrl());
    }

    @Override
    public void ClickInCommentList(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800004", "800004", "AppTabClick", false)
                .setEvenName("点击进入评论列表")
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("评论按钮")
                .build()
                .send();
    }

    @Override
    public void ClickPriseIcon(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0021", "A0021", "Support", false)
                .setEvenName("点击点赞")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getArticle().getUrl())
                .setEventDetail("文章")
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "").newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .supportType("文章")
                .build()
                .send();
    }

    @Override
    public void ClickMoreIcon(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800005", "800005", "AppTabClick", false)
                .setEvenName("点击更多")
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void ClickCommentBox(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800002", "800002", "AppTabClick", false)
                .setEvenName("点击评论输入框")
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("评论输入框")
                .build()
                .send();
    }

    @Override
    public void ClickShare(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "800018", "AppTabClick", false)
                .setEvenName("点击分享")
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public void ClickBack(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800001", "800001", "AppTabClick", false)
                .setEvenName("点击返回")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "").pageType("新闻详情页").clickTabName("返回")
                .build()
                .send();
    }

    @Override
    public Analytics CreateCommentAnalytics(DraftDetailBean bean) {
        Analytics analytics = new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0023", "A0023", "Comment", false)
                .setEvenName("文章评论成功")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setUrl(bean.getArticle().getUrl())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setEventDetail("文章")
                .setSelfObjectID(bean.getArticle().getId() + "").newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .commentType("文章")
                .build();
        return analytics;
    }

    @Override
    public void ClickRelatedContent(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800012", "800012", "RelatedContentClick", false)
                .setEvenName("点击正文底部频道名称")
                .setObjectID(bean.getArticle().getChannel_id())
                .setObjectName(bean.getArticle().getChannel_name())
//                .setObjectType(ObjectType.ColumnType)
                .setClassifyID(bean.getArticle().getSource_channel_id())
                .setClassifyName(bean.getArticle().getSource_channel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "").newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClick("所属频道")
                .build()
                .send();
    }

    @Override
    public void ClickMiddleChannel(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800012", "800012", "RelatedContentClick", false)
                .setEvenName("点击稿件标题下频道名称")
                .setObjectID(bean.getArticle().getChannel_id())
                .setObjectName(bean.getArticle().getChannel_name())
//                .setObjectType(ObjectType.ColumnType)
                .setClassifyID(bean.getArticle().getSource_channel_id())
                .setClassifyName(bean.getArticle().getSource_channel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClick("所属频道")
                .build()
                .send();
    }

    @Override
    public void ClickCommentAll(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800013", "800013", "AppTabClick", false)
                .setEvenName("点击精选的全部按钮")
                .setObjectID(bean.getArticle().getChannel_id())
                .setObjectName(bean.getArticle().getChannel_name())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getSource_channel_id())
                .setClassifyName(bean.getArticle().getSource_channel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("话题详情页")
                .clickTabName("全部")
                .build()
                .send();
    }

    @Override
    public void ClickRelatedNews(DraftDetailBean bean, RelatedNewsBean beanRelate) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800009", "800009", "RelatedContentClick", false)
                .setEvenName("点击相关新闻列表")
                .setObjectID(beanRelate.getMlf_id())
                .setObjectName(beanRelate.getTitle())
                .setObjectType(ObjectType.NewsType)
                .setUrl(beanRelate.getUri_scheme())
                .setClassifyID("")
                .setClassifyName("")
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(beanRelate.getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClick("相关新闻")
                .build()
                .send();
    }

    @Override
    public void ClickRelatedSpecial(DraftDetailBean bean, RelatedSubjectsBean beanRelate) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800010", "800010", "RelatedContentClick", false)
                .setEvenName("点击相关专题列表")
                .setObjectID(beanRelate.getMlf_id())
                .setObjectName(beanRelate.getTitle())
                .setObjectType(ObjectType.NewsType)
                .setUrl(beanRelate.getUri_scheme())
                .setClassifyID("")
                .setClassifyName("")
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "SubjectType")
                        .put("subject", beanRelate.getId() + "")
                        .toString())
                .setSelfObjectID(beanRelate.getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClick("相关专题")
                .build()
                .send();
    }

    @Override
    public void ClickMoreComment(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800004", "800004", "AppTabClick", false)
                .setEvenName("点击查看更多评论")
                .setPageType("新闻详情页")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .clickTabName("全部")
                .build()
                .send();
    }

    /**********评论页***********************************************************************************************************************************/
    @Override
    public void AppTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "800018", "AppTabClick", false)
                .setEvenName("点击分享")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("评论页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("评论列表页")
                .clickTabName("分享")
                .build()
                .send();

    }

    @Override
    public void UpdateComment(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0023", "A0023", "Comment", false)
                .setEvenName("发表评论")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("评论页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("评论列表页")
                .commentType("文章")
                .build()
                .send();
    }

    @Override
    public void AppTabCommentClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800002", "800002", "AppTabClick", false)
                .setEvenName("点击评论输入框")
//                .setObjectID(bean.getArticle().getMlf_id() + "")
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("评论列表页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .pageType("评论列表页")
                .clickTabName("评论输入框")
                .build()
                .send();
    }

    @Override
    public void DeletedComment(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0123", "A0123", "CommentDeleted", false)
                .setEvenName("删除评论")
                .setObjectID(bean.getArticle().getMlf_id())
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getArticle().getUrl())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType(pageType)
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .setAttachObjectId(id)
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType(scPageType)
                .build()
                .send();
    }

    @Override
    public void HotCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800003", "800003", "Comment", false)
                .setEvenName("热门评论点击回复")
//                .setObjectID(bean.getArticle().getMlf_id())
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType(pageType)
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
//                .setAttachObjectId(id)
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType(scPageType)
                .commentType("评论")
                .build()
                .send();
    }

    @Override
    public void NewCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800003", "800003", "Comment", false)
                .setEvenName("最新评论点击回复")
//                .setObjectID(bean.getArticle().getMlf_id())
//                .setObjectName(bean.getArticle().getDoc_title())
//                .setObjectType(ObjectType.NewsType)
//                .setClassifyID(bean.getArticle().getChannel_id())
//                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType(pageType)
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
//                        .put("subject", "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
//                .setAttachObjectId(id)
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType(scPageType)
                .commentType("评论")
                .build()
                .send();
    }

    @Override
    public void CommentPrise(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0021", "A0021", "CommentPrised", false)
                .setEvenName("评论点赞")
                .setObjectID(bean.getArticle().getMlf_id())
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType(pageType)
                .setUrl(bean.getArticle().getUrl())
                .setEventDetail("评论")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .setAttachObjectId(id)
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType(scPageType)
                .build()
                .send();
    }

    @Override
    public Analytics CreateCommentSend(DraftDetailBean bean, String pageType, String scPageType, String id) {
        Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "A0023", "A0023", "Comment", false)
                .setEvenName("回复评论成功")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getArticle().getUrl())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType(pageType)
                .setEventDetail("评论")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .setAttachObjectId(id)
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType(scPageType)
                .commentType("文章")
                .build();
        return analytics;
    }

    /**********官员页***********************************************************************************************************************************/
    @Override
    public void ClickMore(Context context) {
        new Analytics.AnalyticsBuilder(context, "800014", "800014", "AppTabClick", false)
                .setEvenName("点击更多查看官员详细信息")
                .setPageType("官员页面")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("customObjectType", "OfficerType")
//                        .toString())
                .pageType("官员页面")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void OfficialDetailClick(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210003", "210003", "OfficialDetailClick", false)
                .setEvenName("点击官员任职履历标签")
//                .setObjectID(bean.getOfficer().getId() + "")
                .setPageType("官员页面")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("customObjectType", "OfficerType")
//                        .toString())
//                .setSearch("任职履历")
                .officialName(bean.getOfficer().getName())
                .officialID(bean.getOfficer().getId() + "")
                .pageType("官员页面")
                .build()
                .send();
    }

    @Override
    public void OfficialNewsClick(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210004", "210004", "OfficialDetailClick", false)
                .setEvenName("点击官员相关新闻标签")
//                .setObjectID(bean.getOfficer().getId() + "")
                .setPageType("官员页面")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("customObjectType", "OfficerType")
//                        .toString())
//                .setSearch("任职履历")
                .officialName(bean.getOfficer().getName())
                .officialID(bean.getOfficer().getId() + "")
                .pageType("官员页面")
                .build()
                .send();
    }

    @Override
    public void OfficalClickShare(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "800018", "AppTabClick", false)
                .setEvenName("点击分享")
//                .setObjectID(bean.getOfficer().getId() + "")
//                .setPageType("官员页面")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", "OfficerType")
//                        .toString())
                .pageType("官员页面")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public Analytics.AnalyticsBuilder CreateOfficalAnalytic(OfficalDetailBean data) {
        Analytics.AnalyticsBuilder builder;
        builder = new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "800033", "OfficialDetailPageStay", true)
                .setEvenName("官员详情页停留")
                .setObjectID(data.getOfficer().getId() + "")
                .setObjectName(data.getOfficer().getName())
                .setPageType("官员页面")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", "OfficerType")
//                        .toString())
                .pageType("官员页面");
        return builder;

    }

    @Override
    public void RelateNewsClick(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210005", "210005", "OfficialDetailClick", false)
                .setEvenName("点击官员相关新闻标签")
                .setObjectID(bean.getOfficer().getId() + "")
                .setObjectID(bean.getOfficer().getId())
                .setObjectName(bean.getOfficer().getCurrent_title())
                .setUrl(bean.getOfficer().getUrl())
                .setPageType("官员页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "OfficerType")
                        .toString())
                .officialName(bean.getOfficer().getName())
                .officialID(bean.getOfficer().getId() + "")
                .pageType("官员页面")
                .build()
                .send();
    }

    /**********图集页***********************************************************************************************************************************/
    @Override
    public void ClickDownLoad(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0025", "A0025", "PictureRelatedOperation", false)
                .setEvenName("点击下载按钮")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.PictureType)
                .setUrl(bean.getArticle().getUrl())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("图集详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集详情页")
                .operationType("点击图片下载")
                .build()
                .send();
    }

    @Override
    public void AtlasSlide(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "A0010", "PictureRelatedOperation", false)
                .setEvenName("图片浏览(左右滑动)")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getArticle().getUrl())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("图集详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集详情页")
                .operationType("图片浏览(左右滑动)")
                .build()
                .send();
    }

    @Override
    public void ClickMoreImage(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "A0010", "AppContentClick", false)
                .setEvenName("打开更多图集页面)")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getArticle().getUrl())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("更多图集页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("图集列表")
                .objectType("图集新闻列表")
                .pubUrl(bean.getArticle().getUrl())
                .build()
                .send();
    }

    @Override
    public void ClickShareTab(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "800018", "AppTabClick", false)
                .setEvenName("点击分享")
                .setPageType("图集详情页")
                .pageType("图集详情页")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public void ClickMoreImgItem(RelatedNewsBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800011", "800011", "AppContentClick", false)
                .setEvenName("更多图集页面，点击单个图集稿件)")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getTitle())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getUri_scheme())
                .setClassifyID("")
                .setClassifyName("")
                .setPageType("更多图集页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getTitle())
                .pageType("图集列表页")
                .objectType("图集新闻列表")
                .pubUrl(bean.getUri_scheme())
                .build()
                .send();
    }

    /**********专题页***********************************************************************************************************************************/

    @Override
    public void ClickChannel(DraftDetailBean.ArticleBean mArticle, SpecialGroupBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900001", "900001", "SubjectDetailClick", false)
                .setEvenName("专题详情页，分类标签点击")
//                .setObjectType(ObjectType.NewsType)
//                .setObjectID(mArticle.getMlf_id() + "")
//                .setObjectName(mArticle.getDoc_title())
                .setPageType("专题详情页")
                .setEventDetail("分类标签")
//                .setSearch(bean.getGroup_name())
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", mArticle.getColumn_id() + "")
//                        .put("subject", mArticle.getId() + "")
//                        .toString())
//                .setSelfObjectID(mArticle.getId() + "")
                .newsID(mArticle.getMlf_id() + "")
                .selfNewsID(mArticle.getId() + "")
                .newsTitle(mArticle.getDoc_title())
                .selfChannelID(mArticle.getChannel_id())
                .channelName(mArticle.getChannel_name())
                .pageType("专题详情页")
                .classTagClick(bean.getGroup_name())
                .build()
                .send();
    }

    @Override
    public void ClickSpecialItem(ArticleItemBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "200007", "AppContentClick", false)
                .setEvenName("专题新闻新闻列表点击")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getUrl())
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", "SubjectType")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("专题列表页")
                .objectType("专题新闻列表")
                .pubUrl(bean.getUrl())
                .build()
                .send();
    }

    @Override
    public void ClickCollect(DraftDetailBean.ArticleBean mArticle) {
        if (!mArticle.isFollowed()) {
            new Analytics.AnalyticsBuilder(getContext(), "A0124", "A0124", "Collect", false)
                    .setEvenName("取消收藏")
                    .setObjectID(mArticle.getMlf_id() + "")
                    .setObjectName(mArticle.getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setUrl(mArticle.getUrl())
                    .setClassifyID(mArticle.getChannel_id())
                    .setClassifyName(mArticle.getChannel_name())
                    .setPageType("专题详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", "SubjectType")
                            .put("subject", mArticle.getId() + "")
                            .toString())
                    .setSelfObjectID(mArticle.getId() + "").newsID(mArticle.getMlf_id() + "")
                    .selfNewsID(mArticle.getId() + "")
                    .newsTitle(mArticle.getDoc_title())
                    .selfChannelID(mArticle.getChannel_id())
                    .channelName(mArticle.getChannel_name())
                    .pageType("专题详情页")
                    .operationType("取消收藏")
                    .build()
                    .send();
        } else {
            new Analytics.AnalyticsBuilder(getContext(), "A0024", "A0024", "Collect", false)
                    .setEvenName("点击收藏")
                    .setObjectID(mArticle.getMlf_id() + "")
                    .setObjectName(mArticle.getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setUrl(mArticle.getUrl())
                    .setClassifyID(mArticle.getChannel_id())
                    .setClassifyName(mArticle.getChannel_name())
                    .setPageType("专题详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", "SubjectType")
                            .put("subject", mArticle.getId() + "")
                            .toString())
                    .setSelfObjectID(mArticle.getId() + "").newsID(mArticle.getMlf_id() + "")
                    .selfNewsID(mArticle.getId() + "")
                    .newsTitle(mArticle.getDoc_title())
                    .selfChannelID(mArticle.getChannel_id())
                    .channelName(mArticle.getChannel_name())
                    .pageType("专题详情页")
                    .operationType("收藏")
                    .build()
                    .send();
        }
    }

    @Override
    public void SpecialClickMore(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900002", "900002", "AppTabClick", false)
                .setEvenName("专题详情页，更多按钮点击")
                .setPageType("专题详情页")
//                .setClassifyID(bean.getArticle().getMlf_id() + "")
//                .setClassifyName(bean.getArticle().getDoc_title())
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("relatedColumn", "SubjectType")
//                        .put("subject", bean.getArticle().getId() + "")
//                        .toString())
//                .setSelfObjectID(bean.getArticle().getId() + "")
                .pageType("专题详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void SpecialFocusImgClick(DraftDetailBean.ArticleBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900003", "900003", "AppContentClick", false)
                .setEvenName("专题详情页，焦点图点击")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getChannel_id())
                .setUrl(bean.getUrl())
                .setClassifyName(bean.getChannel_name())
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", "SubjectType")
                        .put("subject", bean.getId() + "")
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("专题详情页")
                .objectType("焦点图")
                .pubUrl(bean.getSubject_focus_url())
                .build()
                .send();
    }

    @Override
    public Analytics pageStayTimeSpecial(DraftDetailBean.ArticleBean mArticle) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021", "ViewAppNewsDetail", true)
                .setEvenName("专题详情页停留时长")
                .setObjectID(mArticle.getMlf_id() + "")
                .setObjectName(mArticle.getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(mArticle.getUrl())
                .setClassifyID(mArticle.getChannel_id())
                .setClassifyName(mArticle.getChannel_name())
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mArticle.getColumn_id() + "")
                        .put("subject", mArticle.getId() + "")
                        .toString())
                .setSelfObjectID(mArticle.getId() + "")
                .newsID(mArticle.getMlf_id() + "")
                .selfNewsID(mArticle.getId() + "")
                .newsTitle(mArticle.getDoc_title())
                .selfChannelID(mArticle.getChannel_id())
                .channelName(mArticle.getChannel_name())
                .pageType("专题详情页")
                .pubUrl(mArticle.getUrl())
                .build();
    }

    @Override
    public void SpecialMoreClickSpecialItem(com.zjrb.daily.news.bean.ArticleItemBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "200007", "AppContentClick", false)
                .setEvenName("点击更多进入专题列表页面后，新闻列表点击")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "SubjectType")
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .pageType("专题详情页")
                .objectType("专题新闻列表")
                .pubUrl(bean.getUrl())
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .build()
                .send();
    }

    @Override
    public void SpecialCommentNewsClick(HotCommentsBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "200007", "AppContentClick", false)
                .setEvenName("评论关联新闻点击")
                .setObjectID(bean.getId() + "")
                .setObjectName(bean.getList_title())
                .setObjectType(ObjectType.NewsType)
                .setUrl(bean.getUrl())
//                .setClassifyID(bean.getChannel_id())
//                .setClassifyName(bean.getChannel_name())
                .setPageType("专题详情页")
//                .setOtherInfo(Analytics.newOtherInfo()
//                        .put("customObjectType", "SubjectType")
//                        .toString())
//                .setSelfObjectID(bean.getId() + "")
                .pageType("专题详情页")
                .objectType("专题新闻列表")
                .pubUrl(bean.getUrl())
//                .newsID(bean.getMlf_id() + "")
//                .selfNewsID(bean.getId() + "")
//                .newsTitle(bean.getDoc_title())
//                .selfChannelID(bean.getChannel_id())
//                .channelName(bean.getChannel_name())
                .build()
                .send();
    }

    /**********直播页***********************************************************************************************************************************/
    @Override
    public void VideoTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800041", "800041", "AppContentClick", false)
                .setEvenName("点击视频tab")
                .setObjectType(ObjectType.NewsType)
                .setPageType("视频详情页")
                .pageType("视频详情页")
                .objectType("视频详情页")
                .build()
                .send();
    }

    @Override
    public void LiveTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800044", "800044", "AppContentClick", false)
                .setEvenName("点击直播间tab")
                .setObjectType(ObjectType.NewsType)
                .setPageType("直播详情页")
                .pageType("直播详情页")
                .objectType("直播详情页")
                .build()
                .send();

    }

    @Override
    public void SummaryTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800043", "800043", "AppContentClick", false)
                .setEvenName("点击简介tab")
                .setObjectType(ObjectType.NewsType)
                .setPageType("直播详情页")
                .pageType("直播详情页")
                .objectType("直播详情页")
                .build()
                .send();
    }

    @Override
    public void VideoCommentTabCLick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800042", "800042", "AppContentClick", false)
                .setEvenName("点击评论tab")
                .setObjectType(ObjectType.NewsType)
                .setPageType("视频详情页")
                .pageType("视频详情页")
                .objectType("视频详情页")
                .build()
                .send();
    }

    @Override
    public void LiveCommentTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800045", "800045", "AppContentClick", false)
                .setEvenName("点击评论tab")
                .setObjectType(ObjectType.NewsType)
                .setPageType("直播详情页")
                .pageType("直播详情页")
                .objectType("直播详情页")
                .build()
                .send();
    }

    @Override
    public void SortClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800046", "800046", "AppContentClick", false)
                .setEvenName("排序方式切换")
                .setObjectType(ObjectType.NewsType)
                .setEventDetail("排序方式")
                .setPageType("直播详情页")
                .pageType("直播详情页")
                .objectType("直播详情页")
                .build()
                .send();
    }
}
