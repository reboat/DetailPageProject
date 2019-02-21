package com.zjrb.zjxw.detailproject.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.daily.news.biz.core.share.BaseDialogFragment;

/**
 * 底部DialogFragment对话框基类
 * Created by wangzhen on 2017/6/26.
 */
public class BaseBottomDialogFragment extends BaseDialogFragment {
    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initWindow();
    }

    /**
     * 设置底部弹出框的窗口样式
     */
    private void initWindow() {
        if (getDialog() == null) return;
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(wlp);
        }
    }
}
