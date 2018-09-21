package com.zjrb.zjxw.detailproject.global;

import android.content.Context;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import cn.daily.news.analytics.Analytics;
import daily.zjrb.com.daily_vr.AnalyCallBack;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * @author: lujialei
 * @date: 2018/5/15
 * @describe:
 */


public class VrAnaly implements AnalyCallBack {

    public DraftDetailBean.ArticleBean getBean() {
        return bean;
    }

    public void setBean(DraftDetailBean.ArticleBean bean) {
        this.bean = bean;
    }

    private DraftDetailBean.ArticleBean bean;

    public VrAnaly(DraftDetailBean.ArticleBean bean) {
        this.bean = bean;
    }

    @Override
    public void onStart() {
        analytics(getContext(), bean, "点击视频播放框上播放按钮", "A0041", "400010", "VideoPlayer", "播放");
    }

    @Override
    public void onPause() {
        analytics(getContext(), bean, "点击视频播放框上暂停按钮", "A0042", "400004", "VideoPlayer", "暂停");
    }

    @Override
    public void onFullScreen() {
        analytics(getContext(), bean, "点击全屏播放按钮", "A0043", "400005", "VideoPlayer", "全屏播放");
    }

    @Override
    public void smallScreen() {
        analytics(getContext(), bean, "点击关闭全屏播放按钮", "A0044", "400006", "VideoPlayer", "关闭全屏播放");
    }

    @Override
    public void openVolumn() {
        analytics(getContext(), bean, "点击关闭静音按钮", "A0046", "400008", "VideoPlayer", "关闭静音");
    }

    @Override
    public void mute() {
        analytics(getContext(), bean, "点击开启静音按钮", "A0045", "400007", "VideoPlayer", "开启静音");
    }

    @Override
    public void openGyroscope() {
        new Analytics.AnalyticsBuilder(getContext(), "A0047", "400015", "VideoPlayer", false)
                .setEvenName("点击开启陀螺仪控制按钮")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.VideoType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("视频页面/新闻详情页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getColumn_id() + "")
                        .put("subject", "")
                        .put("mediaURL", bean.getVideo_url())
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("视频页面/新闻详情页面")
                .clickButtonType("开启陀螺仪控制")
                .build()
                .send();
    }

    @Override
    public void closeGyroscope() {
        new Analytics.AnalyticsBuilder(getContext(), "A0048", "400016", "VideoPlayer", false)
                .setEvenName("点击关闭陀螺仪控制按钮")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.VideoType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("视频页面/新闻详情页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getColumn_id() + "")
                        .put("subject", "")
                        .put("mediaURL", bean.getVideo_url())
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("视频页面/新闻详情页面")
                .clickButtonType("关闭陀螺仪控制")
                .build()
                .send();
    }

    @Override
    public void openDoubelScreen() {
        new Analytics.AnalyticsBuilder(getContext(), "A0049", "400017", "VideoPlayer", false)
                .setEvenName("点击开启分屏开关按钮")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.VideoType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("视频页面/新闻详情页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getColumn_id() + "")
                        .put("subject", "")
                        .put("mediaURL", bean.getVideo_url())
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("视频页面/新闻详情页面")
                .clickButtonType("开启分屏开关")
                .build()
                .send();
    }

    @Override
    public void closeDoubelScreen() {
        new Analytics.AnalyticsBuilder(getContext(), "A0050", "400018", "VideoPlayer", false)
                .setEvenName("点击关闭分屏开关按钮")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.VideoType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("视频页面/新闻详情页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getColumn_id() + "")
                        .put("subject", "")
                        .put("mediaURL", bean.getVideo_url())
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .newsID(bean.getMlf_id() + "")
                .selfNewsID(bean.getId() + "")
                .newsTitle(bean.getDoc_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("视频页面/新闻详情页面")
                .clickButtonType("关闭分屏开关")
                .build()
                .send();
    }


    private void analytics(Context context, DraftDetailBean.ArticleBean extra, String eventName, String eventCode, String umengID, String scEventName, String ClickType) {
        if (extra != null) {
            Analytics.newBuilder(context, eventCode, umengID, scEventName, false)
                    .setObjectID(extra.getMlf_id())
                    .setObjectName(extra.getDoc_title())
                    .setObjectType(ObjectType.VideoType)
                    .setClassifyID(extra.getChannel_id())
                    .setClassifyName(extra.getChannel_name())
                    .setPageType("新闻详情页面")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", extra.getColumn_id() + "")
                            .put("subject", "")
                            .put("mediaURL", extra.getVideo_url())
                            .toString())
                    .setSelfObjectID(extra.getId())
                    .setEvenName(eventName)
                    .newsID(bean.getMlf_id() + "")
                    .selfNewsID(bean.getId() + "")
                    .newsTitle(bean.getDoc_title())
                    .selfChannelID(bean.getChannel_id())
                    .channelName(bean.getChannel_name())
                    .pageType("视频页面/新闻详情页面")
                    .clickButtonType(ClickType)
                    .build().send();
        }
    }
}
