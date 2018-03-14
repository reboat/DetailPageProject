package com.zjrb.zjxw.detailproject.redBoat;

import android.view.View;

import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailActivity;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

/**
 * 类描述：红船号详情页
 */

public class RedBoatActivity2 extends NewsDetailActivity {

    public void fillData(DraftDetailBean data) {
        super.fillData(data);
        mFloorBar.setVisibility(View.GONE);
        topHolder.setViewVisible(topHolder.getShareView(), View.GONE);
    }

    /**
     * 设置是否是红船号的详情页
     */
    @Override
    public void configTak(DraftDetailTask task) {
//        task.isRedBoat(true);
    }

    /**
     * WebView加载完毕
     */
    @Override
    public void onOptPageFinished() {
    }

}
