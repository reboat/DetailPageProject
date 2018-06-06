package com.zjrb.zjxw.detailproject.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjrb.zjxw.detailproject.interFace.VideoBCnterFace;

/**
 * 订阅同步广播
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class VideoReceiver extends BroadcastReceiver {

    private VideoBCnterFace interFace;

    public VideoReceiver(VideoBCnterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        interFace.videoBC(intent);
    }

}
