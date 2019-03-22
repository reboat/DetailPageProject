package com.zjrb.zjxw.detailproject.ui.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjrb.zjxw.detailproject.callback.DetailInterface;

/**
 * 网络监听
 * Created by wanglinjie.
 * create time:2019/3/22  下午2:29
 */
public class NetWorkChangeReceiver extends BroadcastReceiver {
    private DetailInterface.NetWorkInterFace interFace;

    public NetWorkChangeReceiver(DetailInterface.NetWorkInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        interFace.networkBC(intent);
    }
}
