package com.zjrb.zjxw.detailproject.callback;

import android.view.View;

import com.aliya.dailyplayer.sub.PlayAnalyCallBack;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;

/**
 * @author: lujialei
 * @date: 2019/5/5
 * @describe:
 */


public class DetailPlayerCallBack implements PlayAnalyCallBack {
    private DraftDetailBean.ArticleBean bean;

    public DetailPlayerCallBack(DraftDetailBean.ArticleBean articleItemBean) {
        this.bean = articleItemBean;
    }

    @Override
    public void onPlay(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0041","VideoPlayer",false)
                .name("点击视频播放框上播放按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("新闻详情页")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .clickButtonType("播放")
                .build().send();
    }

    @Override
    public void onPause(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0042","VideoPlayer",false)
                .name("点击视频播放框上暂停按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("新闻详情页")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .clickButtonType("暂停")
                .build().send();
    }

    @Override
    public void onFullScreen(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0043","VideoPlayer",false)
                .name("点击视频播放框上全屏按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("新闻详情页")
                .clickButtonType("全屏播放")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .build().send();
    }

    @Override
    public void onRect(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0044","VideoPlayer",false)
                .name("点击视频播放框上关闭全屏按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .pageType("新闻详情页")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .clickButtonType("关闭全屏播放")
                .build().send();
    }

    @Override
    public void onMute(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0045","VideoPlayer",false)
                .name("点击开启静音按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .pageType("新闻详情页")
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .clickButtonType("开启静音")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .build().send();
    }

    @Override
    public void onVolumn(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0046","VideoPlayer",false)
                .name("点击关闭静音按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .pageType("新闻详情页")
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .clickButtonType("关闭静音")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .build().send();
    }

    @Override
    public void onShare(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "400013","VideoPlayer",false)
                .name("视频播放器分享按钮点击")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .pageType("新闻详情页")
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .clickButtonType("分享")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .build().send();
    }

    @Override
    public void onReplay(View view) {
        String newsId = String.valueOf(bean.getMlf_id());
        if (bean.getDoc_type()==10){
            newsId = String.valueOf(bean.guid);
        }
        Analytics.newBuilder(UIUtils.getContext(), "A0053","VideoPlayer",false)
                .name("点击重新播放按钮")
                .objectID(String.valueOf(bean.getMlf_id()))
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .classShortName(bean.getChannel_name())
                .pageType("新闻详情页")
                .relatedColumn(bean.getColumn_id()+"")
                .selfObjectID(bean.getId()+"")
                .newsID(newsId)
                .selfNewsID(String.valueOf(bean.getId()))
                .newsTitle(bean.getList_title())
                .selfChannelID(bean.getChannel_id())
                .channelName(bean.getChannel_name())
                .clickButtonType("重新播放")
                .ilurl(bean.getUrl())
                .pubUrl(bean.getUrl())
                .columnID(bean.getColumn_id()+"")
                .columnName(bean.getColumn_name())
                .build().send();
    }

    @Override
    public void onDanmuOpen(View view) {
        new Analytics.AnalyticsBuilder(view.getContext(), "A0055", "", false)
                .name("点击开启弹幕")
                .selfObjectID(bean.getId()+"")
                .columnID(bean.getColumn_id()+"")
                .classShortName(bean.getChannel_name())
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .pageType("直播详情页")
                .ilurl(bean.getUrl())
                .objectID(String.valueOf(bean.getMlf_id()))
                .columnName(bean.getColumn_name())
                .build()
                .send();
    }

    @Override
    public void onDanmuClose(View view) {
        new Analytics.AnalyticsBuilder(view.getContext(), "A0155", "", false)
                .name("点击关闭弹幕")
                .selfObjectID(bean.getId()+"")
                .columnID(bean.getColumn_id()+"")
                .classShortName(bean.getChannel_name())
                .objectShortName(bean.getDoc_title())
                .seObjectType(ObjectType.C21)
                .classID(bean.getChannel_id())
                .pageType("直播详情页")
                .ilurl(bean.getUrl())
                .objectID(String.valueOf(bean.getMlf_id()))
                .columnName(bean.getColumn_name())
                .build()
                .send();
    }
}
