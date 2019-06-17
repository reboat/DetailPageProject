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
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.zjxw.detailproject.R;


/**
 * 全屏对话框
 *
 * @author Administrator
 * @since 2016年05月11日 15:30:23
 */
public class FullScreenDialog extends Dialog {

    private Context mContext;
    private View view;
    private TextView tv_title;
    private TextView tv_message;
    private ImageView button_close;

    public FullScreenDialog(Context context) {
        super(context, R.style.fullscreen_dialog);
        mContext = context;
        initView();
    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.module_detail_dialog_fullscreen_layout, null);
        tv_title = view.findViewById(R.id.tv_dialog_fullscreen_title);
        tv_message = view.findViewById(R.id.tv_dialog_fullscreen_message);
        button_close = view.findViewById(R.id.btn_dialog_fullscreen_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //TextView可滚动,但不流畅
//        tv_message.setMovementMethod(ScrollingMovementMethod.get());
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
    public FullScreenDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置内容
     *
     * @param message
     */
    public FullScreenDialog setMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            tv_message.setText(message);
        }
        return this;
    }

    /**
     * 配置对话框
     */
    private void configDialog() {
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        //设置对话框居中
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = DisplayUtil.getScreenWidth(mContext) * 5 / 6;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
