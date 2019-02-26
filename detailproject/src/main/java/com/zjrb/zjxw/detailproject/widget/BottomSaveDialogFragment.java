package com.zjrb.zjxw.detailproject.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片保存对话框
 * Created by wangzhen on 2017/6/26.
 */
public class BottomSaveDialogFragment extends BaseBottomDialogFragment {

    private AlertDialog mDialog;
    private OnSaveDialogClickListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.BottomDialog);
        View contentView = View.inflate(getContext(), R.layout.module_detail_bottom_save, null);
        ButterKnife.bind(this, contentView);
        builder.setView(contentView);
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(true);
        return mDialog;
    }

    /**
     * @param v 图片保存和取消
     */
    @OnClick({R2.id.tv_save, R2.id.tv_cancel})
    public void onClick(View v) {
        if (mDialog != null) mDialog.dismiss();
        if (mListener == null) return;
        if (v.getId() == R.id.tv_save) {
            mListener.onSave();
        }
    }

    public void setSaveListener(OnSaveDialogClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 保存图片回调
     */
    public interface OnSaveDialogClickListener {
        void onSave();
    }
}
