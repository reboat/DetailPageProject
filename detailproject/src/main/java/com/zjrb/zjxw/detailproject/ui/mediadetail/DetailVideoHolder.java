package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.dailyplayer.PlayerManager;
import com.aliya.dailyplayer.utils.Recorder;
import com.google.gson.Gson;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.global.biz.Format;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.utils.PlayerAnalytics;

import butterknife.OnClick;
import cn.daily.news.update.util.NetUtils;

/**
 * 视频直播holder
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:16
 */
public class DetailVideoHolder extends SuperDetailVideoHolder {

    TextView mTvDuration;

    protected View layoutPlay;

    public DetailVideoHolder(ViewGroup parent) {
        super(parent);
        layoutPlay = mViewStubVideo.inflate();
        mTvDuration = layoutPlay.findViewById(R.id.tv_duration);
    }

    @Override
    public void bindView() {
        super.bindView();
        if (mData == null) return;
        //视频时长
        if (mData.getVideo_duration() > 0) {
            mTvDuration.setText(Format.duration(mData.getVideo_duration() * 1000));
            mTvDuration.setVisibility(View.VISIBLE);
        } else {
            mTvDuration.setVisibility(View.GONE);
        }
    }

    @OnClick({R2.id.iv_type_video, R2.id.ll_net_hint})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.iv_type_video) {
            if (!TextUtils.isEmpty(mData.getVideo_url())) {
                if (NetUtils.isAvailable(itemView.getContext())) {
                    if (NetUtils.isMobile(itemView.getContext())) {
                        if (Recorder.get().isAllowMobileTraffic(mData.getVideo_url())) {
                            PlayerManager.get().play(mVideoContainer, mData.getVideo_url(), new Gson().toJson(mData));
                            PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                        } else {
                            llStart.setVisibility(View.GONE);
                            llNetHint.setVisibility(View.VISIBLE);
                            tvNetHint.setText("用流量播放");
                            tvNetHint.setVisibility(View.VISIBLE);
                        }
                        return;
                    }
                    if (NetUtils.isWifi(itemView.getContext())) {
                        PlayerManager.get().play(mVideoContainer, mData.getVideo_url(), new Gson().toJson(mData));
                        PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                        return;
                    }
                    PlayerManager.get().play(mVideoContainer, mData.getVideo_url(), new Gson().toJson(mData));
                    PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
                }
            }
        } else {
            PlayerManager.get().play(mVideoContainer, mData.getVideo_url(), new Gson().toJson(mData));
            PlayerManager.setPlayerCallback(mVideoContainer, PlayerAnalytics.get());
            if (NetUtils.isMobile(itemView.getContext())) {
                Recorder.get().allowMobileTraffic(mData.getVideo_url());
            }
        }

    }

}
