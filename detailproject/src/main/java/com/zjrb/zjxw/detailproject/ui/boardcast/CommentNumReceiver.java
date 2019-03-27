package com.zjrb.zjxw.detailproject.ui.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zjrb.zjxw.detailproject.callback.DetailInterface;

/**
 * Created by wanglinjie.
 * create time:2019/3/26  上午9:49
 */
public class CommentNumReceiver extends BroadcastReceiver {
    private DetailInterface.CommentInterFace interFace;

    public CommentNumReceiver(DetailInterface.CommentInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        interFace.syncCommentNum(intent);
    }
}
