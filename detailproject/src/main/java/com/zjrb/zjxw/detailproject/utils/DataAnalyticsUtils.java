package com.zjrb.zjxw.detailproject.utils;

import android.content.Context;

import com.zjrb.core.utils.UIUtils;
import com.zjrb.passport.listener.IFailure;
import com.zjrb.zjxw.detailproject.apibean.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.callback.DetailWMHelperInterFace;

import java.util.List;
import java.util.UUID;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;

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
        new Analytics.AnalyticsBuilder(getContext(), eventCode, scEventName, false)
                .name(eventNme)
                .seObjectType(ObjectType.C90)
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnName(bean.getArticle().getColumn_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .channelName(bean.getArticle().getChannel_name())
                .selfChannelID(bean.getArticle().getChannel_id())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("新闻详情页")
                .operationType(operationType)
                .build()
                .send();
    }

    @Override
    public Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "ViewAppNewsDetail", true)
                .name("页面停留时长")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .ilurl(bean.getArticle().getUrl())
                .seObjectType(ObjectType.C01)
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pageType("新闻详情页")
                .pubUrl(bean.getArticle().getUrl());
    }

    @Override
    public void ClickInCommentList(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800004", "AppTabClick", false)
                .name("点击进入评论列表")
                .pageType("新闻详情页")
                .clickTabName("评论按钮")
                .build()
                .send();
    }

    @Override
    public void ClickPriseIcon(DraftDetailBean bean) {
        //新华智云点赞
//        new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.praise)
//                .setTargetID(bean.getArticle().getId() + "")
//                .setUrl(bean.getArticle().getUrl())
//                .build()
//                .send();
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0021", "Support", false)
                .name("点击点赞")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getArticle().getUrl())
                .action("文章")
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name()).columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("新闻详情页")
                .supportType("文章")
                .build()
                .send();
    }

    @Override
    public void ClickMoreIcon(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800005", "AppTabClick", false)
                .name("点击更多")
                .pageType("新闻详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void ClickCommentBox(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800002", "AppTabClick", false)
                .name("点击评论输入框")
                .pageType("新闻详情页")
                .clickTabName("评论输入框")
                .build()
                .send();
    }

    @Override
    public void ClickShare(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "AppTabClick", false)
                .name("点击分享")
                .pageType("新闻详情页")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public void ClickBack(DraftDetailBean bean) {
        if (bean==null||bean.getArticle()==null){
            return;
        }
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800001", "AppTabClick", false)
                .name("点击返回")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .pageType("新闻详情页")
                .clickTabName("返回")
                .build()
                .send();
    }

    @Override
    public Analytics CreateCommentAnalytics(DraftDetailBean bean,boolean isCommentListPage) {
        String pageType = "新闻详情页";
        if(isCommentListPage){
            pageType = "评论列表页";
        }
        Analytics analytics = new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0023", "Comment", false)
                .name("文章评论成功")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .ilurl(bean.getArticle().getUrl())
                .action("文章")
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType(pageType)
                .commentType("文章")
                .build();
        return analytics;
    }

    @Override
    public void ClickRelatedContent(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800012", "RelatedContentClick", false)
                .name("点击正文底部频道名称")
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .referClassID(bean.getArticle().getSource_channel_id())
                .referClassName(bean.getArticle().getSource_channel_name())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClickType("所属频道")
                .build()
                .send();
    }

    @Override
    public void ClickMiddleChannel(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800012", "RelatedContentClick", false)
                .name("点击稿件标题下频道名称")
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .referClassName(bean.getArticle().getSource_channel_name())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .relatedContentClickType("所属频道")
                .build()
                .send();
    }

    @Override
    public void ClickCommentAll(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800013", "AppTabClick", false)
                .name("点击精选的全部按钮")
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .seObjectType(ObjectType.C01)
                .referClassID(bean.getArticle().getSource_channel_id())
                .referClassName(bean.getArticle().getSource_channel_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .objectID(bean.getArticle().getMlf_id() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("话题详情页")
                .clickTabName("全部")
                .build()
                .send();
    }

    @Override
    public void ClickRelatedNews(DraftDetailBean bean, RelatedNewsBean beanRelate) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800009", "RelatedContentClick", false)
                .name("点击相关新闻列表")
                .selfObjectID(bean.getArticle().getId() + "")
                .objectShortName(beanRelate.getTitle())
                .seObjectType(ObjectType.C01)
                .ilurl(beanRelate.getUri_scheme())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .objectID(bean.getArticle().getMlf_id() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("新闻详情页")
                .relatedContentClickType("相关新闻")
                .build()
                .send();
    }

    @Override
    public void ClickRelatedSpecial(DraftDetailBean bean, RelatedSubjectsBean beanRelate) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800010", "RelatedContentClick", false)
                .name("点击相关专题列表")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(beanRelate.getTitle())
                .seObjectType(ObjectType.C01)
                .ilurl(beanRelate.getUri_scheme())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("新闻详情页")
                .relatedContentClickType("相关专题")
                .build()
                .send();
    }

    @Override
    public void ClickMoreComment(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800004", "AppTabClick", false)
                .name("点击查看更多评论")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pageType("新闻详情页")
                .clickTabName("查看更多评论")
                .build()
                .send();
    }

    /**********评论页***********************************************************************************************************************************/
    @Override
    public void AppTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "AppTabClick", false)
                .name("点击分享")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .selfObjectID(bean.getArticle().getId() + "")
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
    }

    @Override
    public void AppTabCommentClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800002", "AppTabClick", false)
                .name("点击评论输入框")
                .pageType("评论列表页")
                .clickTabName("评论输入框")
                .build()
                .send();
    }

    @Override
    public void DeletedComment(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0123", "CommentDeleted", false)
                .name("删除评论")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getArticle().getUrl())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType(scPageType)
                .build()
                .send();
    }

    @Override
    public void HotCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800003", "AppTabClick", false)
                .name("热门评论点击回复")
                .pageType(scPageType)
                .clickTabName("回复评论")
                .commentType("评论")
                .build()
                .send();
    }

    @Override
    public void NewCommentClick(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800003", "AppTabClick", false)
                .name("最新评论点击回复")
                .pageType(scPageType)
                .commentType("评论")
                .clickTabName("回复评论")
                .build()
                .send();
    }

    @Override
    public void CommentPrise(DraftDetailBean bean, String pageType, String scPageType, String id) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0021", "Support", false)
                .name("评论点赞")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pageType(pageType)
                .ilurl(bean.getArticle().getUrl())
                .action("评论")
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .pubUrl(bean.getArticle().getUrl())
                .supportType("评论")
                .pageType(scPageType)
                .build()
                .send();
    }

    @Override
    public Analytics CreateCommentSend(DraftDetailBean bean, String pageType, String scPageType, String id) {
        Analytics analytics = new Analytics.AnalyticsBuilder(getContext(), "A0023", "Comment", false)
                .name("回复评论成功")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getArticle().getUrl())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .action("评论")
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType(scPageType)
                .commentType("评论")
                .build();
        return analytics;
    }

    /**********官员页***********************************************************************************************************************************/
    @Override
    public void ClickMore(Context context) {
        new Analytics.AnalyticsBuilder(context, "800014", "AppTabClick", false)
                .name("点击更多查看官员详细信息")
                .pageType("官员页面")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void OfficialDetailClick(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210003", "AppTabClick", false)
                .name("点击官员任职履历标签")
                .officialName(bean.getOfficer().getName())
                .officialID(bean.getOfficer().getId() + "")
                .pageType("官员页面")
                .clickTabName("任职履历")
                .build()
                .send();
    }

    @Override
    public void OfficialNewsClick(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210004", "AppTabClick", false)
                .name("点击官员相关新闻标签")
                .officialName(bean.getOfficer().getName())
                .officialID(bean.getOfficer().getId() + "")
                .clickTabName("相关新闻")
                .pageType("官员页面")
                .build()
                .send();
    }

    @Override
    public void OfficalClickShare(OfficalDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "AppTabClick", false)
                .name("点击分享")
                .pageType("官员页面")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public Analytics.AnalyticsBuilder CreateOfficalAnalytic(OfficalDetailBean data) {
        Analytics.AnalyticsBuilder builder;
        builder = new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "OfficialDetailPageStay", true)
                .name("页面停留时长")
                .officialID(data.getOfficer().getId() + "")
                .officialName(data.getOfficer().getName())
                .pageType("官员页面");
        return builder;

    }

    @Override
    public void RelateNewsClick(OfficalDetailBean bean, int position) {
        List<ArticleItemBean> list = bean.getOfficer().getArticle_list();
        if (!list.isEmpty() && list.get(position) != null) {
            new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "210005", "AppContentClick", false)
                    .name("官员相关新闻列表点击")
                    .seObjectType(ObjectType.C01)
                    .objectID(bean.getOfficer().getArticle_list().get(position).getMlf_id() + "")
                    .selfObjectID(bean.getOfficer().getArticle_list().get(position).getId() + "")
                    .officialID(bean.getOfficer().getId()+"")
                    .officialName(bean.getOfficer().getName())
                    .objectShortName(bean.getOfficer().getArticle_list().get(position).getList_title())
                    .ilurl(bean.getOfficer().getArticle_list().get(position).getUrl())
                    .officialName(bean.getOfficer().getName())
                    .officialID(bean.getOfficer().getId() + "")
                    .pubUrl(bean.getOfficer().getArticle_list().get(position).getUrl())
                    .newsID(bean.getOfficer().getArticle_list().get(position).getMlf_id() + "")
                    .selfNewsID(bean.getOfficer().getArticle_list().get(position).getId() + "")
                    .newsTitle(bean.getOfficer().getArticle_list().get(position).getList_title())
                    .pageType("官员页面")
                    .objectType("官员相关新闻列表")
                    .build()
                    .send();
        }
    }

    /**********图集页***********************************************************************************************************************************/
    @Override
    public void ClickDownLoad(DraftDetailBean bean) {
        DraftDetailBean.ArticleBean articleBean=bean.getArticle();
        new Analytics.AnalyticsBuilder(getContext(), "A0025", "PictureRelatedOperation", false)
                .name("点击下载按钮")
                .selfObjectID(String.valueOf(articleBean.getId()))
                .columnID(String.valueOf(articleBean.getColumn_id()))
                .classShortName(articleBean.getChannel_name())
                .objectShortName(articleBean.getDoc_title())
                .classID(articleBean.getChannel_id())
                .pageType("图集详情页")
                .ilurl(articleBean.getUrl())
                .seObjectType(ObjectType.C11)
                .objectID(String.valueOf(articleBean.getMlf_id()))
                .columnName(articleBean.getColumn_name())
                .selfNewsID(String.valueOf(articleBean.getId()))
                .pubUrl(articleBean.getUrl())
                .selfChannelID(articleBean.getChannel_id())
                .newsID(String.valueOf(articleBean.getMlf_id()))
                .newsTitle(articleBean.getDoc_title())
                .channelName(articleBean.getChannel_name())
                .operationType("保存图片")
                .build()
                .send();
    }

    @Override
    public void AtlasSlide(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "PicturePageStay", false)
                .name("图片浏览(左右滑动)")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getArticle().getUrl())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name()).columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("图集详情页")
                .operationType("图片浏览(左右滑动)")
                .build()
                .send();
    }

    @Override
    public void ClickMoreImage(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "A0010", "PicturePageStay", false)
                .name("打开\"更多图集\"页面)")
                .objectID(bean.getArticle().getMlf_id() + "")
                .objectShortName(bean.getArticle().getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getArticle().getUrl())
                .classID(bean.getArticle().getChannel_id())
                .classShortName(bean.getArticle().getChannel_name())
                .columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .selfObjectID(bean.getArticle().getId() + "")
                .newsID(bean.getArticle().getMlf_id() + "")
                .selfNewsID(bean.getArticle().getId() + "")
                .newsTitle(bean.getArticle().getDoc_title())
                .selfChannelID(bean.getArticle().getChannel_id())
                .channelName(bean.getArticle().getChannel_name()).columnID(bean.getArticle().getColumn_id() + "")
                .columnName(bean.getArticle().getColumn_name())
                .pubUrl(bean.getArticle().getUrl())
                .pageType("更多图集页")
                .pubUrl(bean.getArticle().getUrl())
                .build()
                .send();
    }

    @Override
    public void ClickShareTab(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800018", "AppTabClick", false)
                .name("点击分享")
                .pageType("图集详情页")
                .clickTabName("分享")
                .build()
                .send();
    }

    @Override
    public void ClickMoreImgItem(RelatedNewsBean bean,DraftDetailBean detailBean) {
        if(detailBean != null && detailBean.getArticle() != null){
            new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800011", "AppContentClick", false)
                    .name("更多图集页面，点击单个图集稿件")
                    .objectID(bean.getMlf_id() + "")
                    .objectShortName(bean.getTitle())
                    .seObjectType(ObjectType.C01)
                    .ilurl(bean.getUri_scheme())
                    .classID(detailBean.getArticle().getChannel_id())
                    .classShortName(detailBean.getArticle().getChannel_name())
                    .selfObjectID(bean.getId() + "")
                    .newsID(bean.getMlf_id() + "")
                    .selfNewsID(bean.getId() + "")
                    .newsTitle(bean.getTitle())
                    .selfChannelID(detailBean.getArticle().getChannel_id())
                    .channelName(detailBean.getArticle().getChannel_name())
                    .pageType("更多图集页")
                    .objectType("图集新闻列表")
                    .pubUrl(bean.getUri_scheme())
                    .build()
                    .send();
        }
    }

    /**********专题页***********************************************************************************************************************************/

    @Override
    public void ClickChannel(DraftDetailBean.ArticleBean mArticle, SpecialGroupBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900001", "SubjectDetailClick", false)
                .name("专题详情页，分类标签点击")
                .action(bean.getGroup_name())
                .pageType("专题详情页")
                .classTagClick(bean.getGroup_name())
                .build()
                .send();
    }

    @Override
    public void ClickSpecialItem(ArticleItemBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "AppContentClick", false)
                .name("专题新闻新闻列表点击")
                .objectID(bean.getMlf_id() + "")
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getUrl())
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .selfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pubUrl(bean.getUrl())
                .pageType("专题详情页")
                .objectType("专题新闻列表")
                .pubUrl(bean.getUrl())
                .build()
                .send();
    }

    @Override
    public void ClickCollect(DraftDetailBean.ArticleBean mArticle) {
        if (!mArticle.isFollowed()) {
            new Analytics.AnalyticsBuilder(getContext(), "A0024", "Collect", false)
                    .name("点击收藏")
                    .objectID(mArticle.getMlf_id() + "")
                    .objectShortName(mArticle.getDoc_title())
                    .seObjectType(ObjectType.C01)
                    .ilurl(mArticle.getUrl())
                    .classID(mArticle.getChannel_id())
                    .classShortName(mArticle.getChannel_name())
                    .selfObjectID(mArticle.getId() + "")
                    .newsID(mArticle.getMlf_id() + "")
                    .selfNewsID(mArticle.getId() + "")
                    .newsTitle(mArticle.getDoc_title())
                    .selfChannelID(mArticle.getChannel_id())
                    .channelName(mArticle.getChannel_name()).columnID(mArticle.getColumn_id() + "")
                    .columnName(mArticle.getColumn_name()).pubUrl(mArticle.getUrl())
                    .pubUrl(mArticle.getUrl())
                    .pageType("专题详情页")
                    .operationType("收藏")
                    .build()
                    .send();
        } else {
            new Analytics.AnalyticsBuilder(getContext(), "A0124", "Collect", false)
                    .name("取消收藏")
                    .objectID(mArticle.getMlf_id() + "")
                    .objectShortName(mArticle.getDoc_title())
                    .seObjectType(ObjectType.C01)
                    .ilurl(mArticle.getUrl())
                    .classID(mArticle.getChannel_id())
                    .classShortName(mArticle.getChannel_name())
                    .selfObjectID(mArticle.getId() + "")
                    .newsID(mArticle.getMlf_id() + "")
                    .selfNewsID(mArticle.getId() + "")
                    .newsTitle(mArticle.getDoc_title())
                    .selfChannelID(mArticle.getChannel_id())
                    .channelName(mArticle.getChannel_name())
                    .columnID(mArticle.getColumn_id() + "")
                    .columnName(mArticle.getColumn_name())
                    .pubUrl(mArticle.getUrl())
                    .pageType("专题详情页")
                    .operationType("取消收藏")
                    .build()
                    .send();
        }
    }

    @Override
    public void SpecialClickMore(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900002", "AppTabClick", false)
                .name("专题详情页，更多按钮点击")
                .pageType("专题详情页")
                .clickTabName("更多")
                .build()
                .send();
    }

    @Override
    public void SpecialFocusImgClick(DraftDetailBean.ArticleBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "900003", "AppContentClick", false)
                .name("专题详情页，焦点图点击")
                .objectID(bean.getMlf_id() + "")
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C01)
                .classID(bean.getChannel_id())
                .ilurl(bean.getUrl())
                .classShortName(bean.getChannel_name())
                .selfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pubUrl(bean.getUrl())
                .pageType("专题详情页")
                .objectType("焦点图")
                .pubUrl(bean.getSubject_focus_url())
                .build()
                .send();
    }

    @Override
    public Analytics pageStayTimeSpecial(DraftDetailBean.ArticleBean mArticle) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "SubjectDetailPageStay", true)
                .name("专题详情页停留时长")
                .objectID(mArticle.getMlf_id() + "")
                .objectShortName(mArticle.getDoc_title())
                .seObjectType(ObjectType.C01)
                .ilurl(mArticle.getUrl())
                .classID(mArticle.getChannel_id())
                .classShortName(mArticle.getChannel_name())
                .selfObjectID(mArticle.getId() + "")
                .newsID(mArticle.getMlf_id() + "")
                .selfNewsID(mArticle.getId() + "")
                .newsTitle(mArticle.getDoc_title())
                .selfChannelID(mArticle.getChannel_id())
                .channelName(mArticle.getChannel_name())
                .pubUrl(mArticle.getUrl())
                .pageType("专题详情页")
                .pubUrl(mArticle.getUrl())
                .build();
    }

    @Override
    public void SpecialMoreClickSpecialItem(com.zjrb.daily.news.bean.ArticleItemBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "AppContentClick", false)
                .name("点击更多进入专题列表页面后，新闻列表点击")
                .objectID(bean.getMlf_id() + "")
                .objectShortName(bean.getDoc_title())
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .selfObjectID(bean.getId() + "")
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
    public void SpecialCommentNewsClick(HotCommentsBean bean, DraftDetailBean detailBean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "200007", "AppContentClick", false)
                .name("评论关联新闻点击")
                .objectID(detailBean.getArticle().getMlf_id() + "")
                .objectShortName(detailBean.getArticle().getDoc_title())
                .classID(detailBean.getArticle().getChannel_id())
                .classShortName(detailBean.getArticle().getChannel_name())
                .selfObjectID(bean.getId() + "")
                .seObjectType(ObjectType.C01)
                .ilurl(bean.getUrl())
                .pageType("专题详情页")
                .newsID(detailBean.getArticle().getMlf_id() + "")
                .selfNewsID(detailBean.getArticle().getId() + "")
                .newsTitle(detailBean.getArticle().getDoc_title())
                .objectType("评论新闻链接")
                .pubUrl(bean.getUrl())
                .build()
                .send();
    }

    /**********直播页***********************************************************************************************************************************/
    @Override
    public void VideoTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800041", "AppTabClick", false)
                .name("点击视频tab")
                .seObjectType(ObjectType.C01)
                .pageType("视频详情页")
                .objectType("视频详情页")
                .clickTabName("视频")
                .build()
                .send();
    }

    @Override
    public void LiveTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800044", "AppTabClick", false)
                .name("点击直播间tab")
                .seObjectType(ObjectType.C01)
                .pageType("直播详情页")
                .objectType("直播详情页")
                .clickTabName("直播间")
                .build()
                .send();

    }

    @Override
    public void SummaryTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800043", "AppTabClick", false)
                .name("点击简介tab")
                .seObjectType(ObjectType.C01)
                .pageType("直播详情页")
                .objectType("直播详情页")
                .clickTabName("简介")
                .build()
                .send();
    }

    @Override
    public void VideoCommentTabCLick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800042", "AppTabClick", false)
                .name("点击评论tab")
                .seObjectType(ObjectType.C01)
                .pageType("视频详情页")
                .objectType("视频详情页")
                .clickTabName("评论")
                .build()
                .send();
    }

    @Override
    public void LiveCommentTabClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800045", "AppTabClick", false)
                .name("点击评论tab")
                .seObjectType(ObjectType.C01)
                .pageType("直播详情页")
                .objectType("直播详情页")
                .clickTabName("评论")
                .build()
                .send();
    }

    @Override
    public void SortClick(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(UIUtils.getActivity(), "800046", "AppTabClick", false)
                .name("排序方式切换")
                .seObjectType(ObjectType.C01)
                .action("排序方式")
                .pageType("直播详情页")
                .objectType("直播详情页")
                .build()
                .send();
    }
}
