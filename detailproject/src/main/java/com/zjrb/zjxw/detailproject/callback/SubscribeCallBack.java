package com.zjrb.zjxw.detailproject.callback;

import android.content.Context;

import com.zjrb.zjxw.detailproject.apibean.bean.SubscribeResponse;

import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.ui.toast.ZBToast;

public class SubscribeCallBack extends APICallBack<SubscribeResponse> {
    private Context mContext;
    private boolean isCancel;

    public SubscribeCallBack(Context context, boolean isCancel) {
        mContext = context;
        this.isCancel = isCancel;
    }

    @Override
    public void onSuccess(SubscribeResponse data) {
        String tip = data.normal_column ? (isCancel ? "取消订阅成功" : "订阅成功") : (isCancel ? "取消关注成功" : "关注成功");
        ZBToast.showShort(mContext, tip);

    }

    @Override
    public void onError(String errMsg, int errCode) {
        super.onError(errMsg, errCode);
        ZBToast.showShort(mContext, "操作失败");
    }
}
