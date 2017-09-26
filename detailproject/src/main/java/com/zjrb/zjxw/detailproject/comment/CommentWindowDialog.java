package com.zjrb.zjxw.detailproject.comment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.ui.UmengUtils.BaseDialogFragment;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentDialogBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.task.CommentSubmitTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提交评论
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */

public class CommentWindowDialog extends BaseDialogFragment implements
        TextWatcher, View.OnTouchListener {
    @BindView(R2.id.iv_close_window)
    ImageView ivCloseWindow;
    @BindView(R2.id.iv_send_comment)
    ImageView ivSendComment;
    @BindView(R2.id.et_input_comment)
    EditText etInputComment;
    @BindView(R2.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R2.id.fy_comment_content)
    FrameLayout fyCommentContent;
    @BindView(R2.id.activity_comment_window)
    RelativeLayout activityCommentWindow;
    @BindView(R2.id.tv_replay)
    TextView mTvReplay;

    /**
     * 稿件ID
     */
    private String articleId;
    /**
     * 被回复ID
     */
    private String parentId;
    /**
     * 被回复人
     */
    private String nickName;
    /**
     * 评论内容
     */
    private String mContent = "";

    /**
     * 弹出框
     */
    protected Dialog dialog;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static CommentWindowDialog newInstance(CommentDialogBean bean) {
        CommentWindowDialog fragment = new CommentWindowDialog();
        Bundle args = new Bundle();
        args.putSerializable(Key.ID, bean.getId());
        args.putSerializable(Key.PARENT_ID, bean.getParent_id());
        args.putSerializable(Key.REPLAYER, bean.getReplayer());
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            articleId = (String) getArguments().getSerializable(Key.ID);
            parentId = (String) getArguments().getSerializable(Key.PARENT_ID);
            nickName = (String) getArguments().getSerializable(Key.REPLAYER);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_detail_comment_window, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initView();
        initOnclick();
        initWindow();
        return dialog;
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


    /**
     * 初始化编辑框
     */
    private void initView() {
        String content = SPHelper.get().get(Key.ARTICLE_COMMENT_EDITING, "");
        etInputComment.setText(content);
        etInputComment.setSelection(content.length());
        ivCloseWindow.setImageResource(R.mipmap.module_detail_comment_close);
        ivSendComment.setImageResource(R.mipmap.module_detail_comment_send_disclick);
        if (nickName != null && !nickName.isEmpty()) {
            mTvReplay.setText(getString(R.string.module_detail_replay_to) + nickName);
        } else {
            mTvReplay.setText(getString(R.string.module_detail_send_comment));
        }
    }

    /**
     * 关闭弹窗
     */
    public void finish() {
        //缓存评论内容
        SPHelper.get().put(Key.ARTICLE_COMMENT_EDITING, mContent.trim()).commit();
        dismissFragmentDialog();
    }

    /**
     * 初始化监听
     */
    private void initOnclick() {
        etInputComment.addTextChangedListener(this);
        activityCommentWindow.setOnTouchListener(this);
    }

    /**
     * 是否正在提交评论
     */
    private boolean submiting = false;

    @OnClick({R2.id.iv_send_comment, R2.id.iv_close_window})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.iv_send_comment) {
            if (submiting) return; // 提交中
            submiting = true;
            submitComment(mContent); // 提交评论
        } else {
            finish();
        }
    }


    /**
     * @param content 提交评论
     *                parentId:被回复的评论ID
     */
    private void submitComment(String content) {
        if (articleId != null && !articleId.isEmpty() && content != null && !content.isEmpty()) {
            new CommentSubmitTask(new APIExpandCallBack<BaseInnerData>() {

                @Override
                public void onSuccess(BaseInnerData stateBean) {
                    if (stateBean == null) return;
                    handlerResult(stateBean);
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    T.showShort(UIUtils.getContext(), errMsg);
                }

                @Override
                public void onAfter() {
                    submiting = false;
                }
            }).setTag(this).exe(articleId, content, parentId);
        } else {
            submiting = false;
            T.showShort(UIUtils.getContext(), getString(R.string.module_detail_comment_content_empty));
        }
    }

    /**
     * 关闭弹框
     */
    public void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * @param stateBean UI展现
     */
    private void handlerResult(BaseInnerData stateBean) {
        if (stateBean.getResultCode() == 0) {
            T.showShort(UIUtils.getContext(), "评论成功");
            finish();
        } else {
            T.showShort(UIUtils.getContext(), stateBean.getResultMsg());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 评论文本输入监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && s.length() <= 249) {
            ivSendComment.setEnabled(true);
            tvCommentNum.setText(s.length() + "/ 250");
        } else {
            ivSendComment.setEnabled(false);
            T.showShort(UIUtils.getContext(), "文字过长");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        ivSendComment.setEnabled(s.length() > 0);
        mContent = s.toString();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        finish();
        return false;
    }

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }
}
