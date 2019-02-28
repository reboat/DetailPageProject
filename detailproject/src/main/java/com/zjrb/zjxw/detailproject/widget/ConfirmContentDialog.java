package com.zjrb.zjxw.detailproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * 带标题和内容的对话框
 * JS专用
 *
 * @author Administrator
 * @since 2015年10月22日 18:22:23
 */
public class ConfirmContentDialog extends Dialog {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView choose1;
    private TextView choose2;
    private Context mContext;
    private View view;

    //TODO WLJ 需要恢复
//    private WebJsCallBack callback;
    private String mId;

    //后来添加
    public ConfirmContentDialog(@NonNull Context context) {
        super(context);
    }


//    public ConfirmContentDialog(Context context, WebJsCallBack callback, String id) {
//        super(context, R.style.confirm_dialog);
//        mContext = context;
//        this.callback = callback;
//        mId = id;
//        initView();
//    }

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(
                R.layout.module_detail_dialog_confirm_content_layout, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        choose1 = (TextView) view.findViewById(R.id.choose1);
        choose2 = (TextView) view.findViewById(R.id.choose2);

//        choose1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(callback != null){
//                    callback.callback_zjxw_js_showAlert(mId, choose1.getText().toString());
//                }
//                dismiss();
//            }
//        });
//        choose2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(callback != null){
//                    callback.callback_zjxw_js_showAlert(mId, choose2.getText().toString());
//                }
//                dismiss();
//            }
//        });
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
    public ConfirmContentDialog setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public ConfirmContentDialog setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        } else {
            tvContent.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置choose1
     *
     * @param str
     */
    public ConfirmContentDialog setChoose1(String str) {
        if (!TextUtils.isEmpty(str)) {
            choose1.setText(str);
        } else {
            choose1.setText("");
        }
        return this;
    }

    /**
     * 设置choose2
     *
     * @param str
     */
    public ConfirmContentDialog setChoose2(String str) {
        if (!TextUtils.isEmpty(str)) {
            choose2.setText(str);
        } else {
            choose2.setText("");
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
        params.width = UIUtils.getScreenW() * 4 / 6;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

}
