package com.zjrb.zjxw.detailproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;

/**
 * 评论删除对话框对话框
 *
 * @author Administrator
 * @since 2015年10月22日 18:22:23
 */
public class ConfirmDialog extends Dialog {

    private Context mContext;
    private View view;
    private TextView tv_title;
    private TextView button_cancel;
    private TextView button_ok;
    private OnConfirmListener mListener;

    public interface OnConfirmListener {
        void onCancel();

        void onOK();
    }

    public ConfirmDialog(Context context) {
        super(context, R.style.confirm_dialog);
        mContext = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(
                R.layout.module_detail_dialog_confirm_layout, null);
        tv_title = view.findViewById(R.id.tv_confirm_title);
        button_cancel = view.findViewById(R.id.Button_Cancel);
        button_ok = view.findViewById(R.id.Button_OK);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancel();
                }
                dismiss();
            }
        });
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onOK();
                }
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view != null) {
            setContentView(view);
            configDialog();
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public ConfirmDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 配置对话框
     */
    private void configDialog() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = UIUtils.getScreenW() * 4 / 6;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public ConfirmDialog setOnConfirmListener(ConfirmDialog.OnConfirmListener listener) {
        mListener = listener;
        return this;
    }
}
