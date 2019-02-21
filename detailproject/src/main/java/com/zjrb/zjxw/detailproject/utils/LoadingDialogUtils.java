package com.zjrb.zjxw.detailproject.utils;

import android.app.Activity;

import com.zjrb.core.load.LoadingIndicatorDialog;
import com.zjrb.core.utils.UIUtils;

/**
 * Created by wanglinjie.
 * create time:2018/1/22  下午5:28
 */

public class LoadingDialogUtils {

    private LoadingIndicatorDialog loginDialog;
    private static LoadingDialogUtils mDialog;

    private LoadingDialogUtils() {
    }

    public static LoadingDialogUtils newInstance() {
        if (mDialog == null) {
            synchronized (LoadingDialogUtils.class) {
                if (mDialog == null) {
                    mDialog = new LoadingDialogUtils();
                }
            }
        }
        return mDialog;

    }


    /**
     * 加载框
     */
    public LoadingDialogUtils getLoginingDialog() {
        Activity activity = UIUtils.getActivity();
        loginDialog = new LoadingIndicatorDialog(activity);
        if (!activity.isDestroyed()) {
            loginDialog.show();
        }
        return this;
    }

    /**
     * 登录加载框
     */
    public LoadingDialogUtils getLoginingDialog(String s) {
        Activity activity = UIUtils.getActivity();
        loginDialog = new LoadingIndicatorDialog(activity);
        if (!activity.isDestroyed()) {
            loginDialog.show();
            loginDialog.setToastText(s);
        }
        return this;
    }

    /**
     * 关闭dialog
     */
    public LoadingDialogUtils dismissLoadingDialog(boolean isSuccess) {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.finish(isSuccess);
        }
        return this;
    }

    /**
     * 关闭dialog
     */
    public LoadingDialogUtils dismissLoadingDialog(boolean isSuccess,String s) {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.finish(isSuccess,s);
        }
        return this;
    }

    /**
     * 关闭dialog,no text
     */
    public LoadingDialogUtils dismissLoadingDialogNoText() {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.finish();
        }
        return this;
    }
}
