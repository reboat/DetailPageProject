package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliya.dailyplayer.sub.DailyPlayerManager;
import com.zjrb.daily.news.global.biz.Format;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.callback.OnListPlayListener;

/**
 * 视频直播holder
 * Created by wanglinjie.
 * create time:2019/3/26  上午11:16
 */
public class DetailVideoHolder extends SuperDetailVideoHolder {

    TextView mTvDuration;

    protected View layoutPlay;
    private OnListPlayListener mOnListPlayListener;

    public DetailVideoHolder(ViewGroup parent, final VideoDetailActivity.MyListPlayCallBack listPlayListener) {
        super(parent);
        mOnListPlayListener = listPlayListener;
        layoutPlay = mViewStubVideo.inflate();
        mTvDuration = layoutPlay.findViewById(R.id.tv_duration);
        layoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyPlayerManager.Builder builder = new DailyPlayerManager.Builder(itemView.getContext())
                        .setImageUrl(mData.getVideo_cover())
                        .setPlayUrl(mData.getVideo_url())
                        .setOnPlayerManagerCallBack(listPlayListener)
                        .setPlayContainer(mVideoContainer);
                DailyPlayerManager.get().listPlay(builder);
                if (mOnListPlayListener!=null){
                    mOnListPlayListener.onListPlay(mVideoContainer);
                }
            }
        });
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

}
