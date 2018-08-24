package com.zjrb.zjxw.detailproject.global;

import android.content.Context;
import android.view.View;

import com.aliya.dailyplayer.Extra;
import com.aliya.dailyplayer.PlayerCallback;
import com.aliya.dailyplayer.ui.PlayerView;
import com.trs.tasdk.entity.ObjectType;
import com.utovr.gson.Gson;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import java.lang.ref.SoftReference;

import cn.daily.news.analytics.Analytics;

/**
 * 视频埋点记录类
 * Created by wanglinjie.
 * create time:2017/11/22  下午3:08
 */

public final class
PlayerAnalytics implements PlayerCallback {

    private static SoftReference<PlayerAnalytics> sAnalyticsSoft;

    public final static PlayerAnalytics get() {
        PlayerAnalytics analytics;
        if (sAnalyticsSoft == null || (analytics = sAnalyticsSoft.get()) == null) {
            sAnalyticsSoft = new SoftReference<>(analytics = new PlayerAnalytics());
        }
        return analytics;
    }

    @Override
    public void onPause(PlayerView view) {
        analytics(view, "点击视频播放框上暂停按钮", "A0042", "400004","VideoPlayer","暂停");
    }

    @Override
    public void onPlay(PlayerView view) {
        analytics(view, "点击视频播放框上播放按钮", "A0041", "400010","VideoPlayer","播放");
    }

    @Override
    public void onFullscreenChange(boolean isFullscreen, PlayerView view) {
        if (isFullscreen) {
            analytics(view, "点击全屏播放按钮", "A0043", "400005","VideoPlayer","全屏播放");
        } else {
            analytics(view, "点击关闭全屏播放按钮", "A0044", "400006","VideoPlayer","关闭全屏播放");
        }
    }

    @Override
    public void onMuteChange(boolean isMute, PlayerView view) {
        if (isMute) {
            analytics(view, "点击开启静音按钮", "A0045", "400007","VideoPlayer","开启静音");
        } else {
            analytics(view, "点击关闭静音按钮", "A0046", "400008","VideoPlayer","关闭静音");
        }
    }

    private void analytics(PlayerView view, String eventName, String eventCode, String umengID,String scEventName,String ClickType) {
        String s = Extra.getExtraData(view);
        DraftDetailBean.ArticleBean extra = (DraftDetailBean.ArticleBean) new Gson().fromJson(s,DraftDetailBean.ArticleBean.class);
        if (extra != null) {
            Analytics.newBuilder(getContext(view), eventCode, umengID,scEventName,false)
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
                    .newsID(extra.getMlf_id() + "")
                    .selfNewsID(extra.getId() + "")
                    .newsTitle(extra.getDoc_title())
                    .selfChannelID(extra.getChannel_id())
                    .channelName(extra.getChannel_name())
                    .pageType("视频页面/新闻详情页面")
                    .clickButtonType(ClickType)
                    .build().send();
        }
    }

    public Context getContext(View view) {
        if (view != null) {
            View parent = (View) view.getParent();
            if (parent != null) {
                return parent.getContext();
            }
        }
        return UIUtils.getContext();
    }

}