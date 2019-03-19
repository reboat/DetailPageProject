package com.zjrb.zjxw.detailproject.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjrb.zjxw.detailproject.callback.DetailInterface;

/**
 * 视频同步广播
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class VideoReceiver extends BroadcastReceiver {

    private DetailInterface.VideoBCnterFace interFace;

    public VideoReceiver(DetailInterface.VideoBCnterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        interFace.videoBC(intent);
    }

}
