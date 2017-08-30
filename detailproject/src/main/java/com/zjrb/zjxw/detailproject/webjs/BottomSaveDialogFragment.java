package com.zjrb.zjxw.detailproject.webjs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.ui.UmengUtils.BaseBottomDialogFragment;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片保存对话框
 * Created by wangzhen on 2017/6/26.
 */
public class BottomSaveDialogFragment extends BaseBottomDialogFragment {

    @BindView(R2.id.tv_save)
    TextView tvSave;
    @BindView(R2.id.tv_cancel)
    TextView tvCancel;
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

    @OnClick({R2.id.tv_save, R2.id.tv_cancel})
    public void onClick(View v) {
        if (mDialog != null) mDialog.dismiss();
        if (mListener == null) return;
        switch (v.getId()) {
            case R2.id.tv_save:
                mListener.onSave();
                break;
            case R2.id.tv_cancel:
                break;
        }
    }

    public void setSaveListener(OnSaveDialogClickListener listener) {
        this.mListener = listener;
    }

    public interface OnSaveDialogClickListener {
        void onSave();
    }
}
