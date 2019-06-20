package com.zjrb.zjxw.detailproject.utils;

import android.content.Context;

import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.redBoat.RedBoatActivity;
import com.zjrb.zjxw.detailproject.ui.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.ui.topic.adapter.TopicAdapter;

import cn.daily.news.biz.core.web.WebViewImpl;

/**
 * 详情页webjs拓展类
 * Created by wanglinjie.
 * create time:2019/3/5  下午5:18
 */
public class DetailWebViewImpl extends WebViewImpl {
    @Override
    public void onWebPageComplete(Context ctx) {
        if (ctx instanceof NewsDetailAdapter.CommonOptCallBack) {
            ((NewsDetailAdapter.CommonOptCallBack) ctx).onOptPageFinished();
        } else if (ctx instanceof TopicAdapter.CommonOptCallBack) {
            ((TopicAdapter.CommonOptCallBack) ctx).onOptPageFinished();
        } else if( ctx instanceof RedBoatAdapter.CommonOptCallBack){
            ((RedBoatAdapter.CommonOptCallBack)ctx).onOptPageFinished();
        }
    }

}
