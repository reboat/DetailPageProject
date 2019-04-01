package com.zjrb.zjxw.detailproject.ui.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjrb.zjxw.detailproject.callback.DetailInterface;

/**
 * 直播间点击顺序/倒序
 * Created by wanglinjie.
 * create time:2019/3/26  上午9:49
 */
public class RefreshHeadReceiver extends BroadcastReceiver {
    private DetailInterface.RefreshHeadInterFace interFace;

    public RefreshHeadReceiver(DetailInterface.RefreshHeadInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        interFace.refresh(intent);
    }
}
