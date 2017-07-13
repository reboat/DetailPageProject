package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.common.global.SPKey;
import com.zjrb.coreprojectlibrary.db.SPHelper;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/30.
 * 提交评论
 */
public class CommentWindowActivity extends BaseActivity implements View.OnClickListener,
        TextWatcher, View.OnTouchListener {
    @BindView(R2.id.et_input_comment)
    EditText etInputComment;
    @BindView(R2.id.bt_send)
    Button btSend;
    @BindView(R2.id.activity_comment_window)
    FrameLayout mActivityCommentWindow;
    private int articleId;   //稿件id
    private int mlfId;   //稿件id
    private int parentId;    //被回复用户的评论id
    private int parentCommentUserId;    //被回复用户
    // 是否来自评论列表Activity
    private boolean isFromCommentAct;
    /**
     * 评论内容
     */
    private String mContent = "";

    public static Intent getIntent(int articleId, boolean isFromCommentAct, int mlfId) {
        return IntentHelper.get(CommentWindowActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.MLF_ID, mlfId)
                .put(IKey.BOOL_TAG, isFromCommentAct)
                .put(IKey.MLF_ID, mlfId)
                .intent();
    }

    public static Intent getIntent(int articleId, int parentId, int parentCommentUserId, boolean
            isFromCommentAct, int mlfId) {
        return IntentHelper.get(CommentWindowActivity.class)
                .put(IKey.ARTICLE_ID, articleId)
                .put(IKey.MLF_ID, mlfId)
                .put(IKey.PARENT_ID, parentId)
                .put(IKey.USER_ID, parentCommentUserId)
                .put(IKey.BOOL_TAG, isFromCommentAct)
                .intent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initState(savedInstanceState);
        setContentView(R.layout.module_detail_comment_window);
        ButterKnife.bind(this);
        initOnclick();
        initEditText();
    }

    private void initEditText() {
        String content = SPHelper.get().get(SPKey.ARTICLE_COMMENT_EDITING, "");
        etInputComment.setText(content);
        etInputComment.setSelection(content.length());
    }

    private void initState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            articleId = getIntent().getIntExtra(IKey.ARTICLE_ID, -1);
            mlfId = getIntent().getIntExtra(IKey.MLF_ID, -1);
            parentId = getIntent().getIntExtra(IKey.PARENT_ID, -1);
            parentCommentUserId = getIntent().getIntExtra(IKey.USER_ID, -1);
            isFromCommentAct = getIntent().getBooleanExtra(IKey.BOOL_TAG, false);
        } else {
            articleId = savedInstanceState.getInt(IKey.ARTICLE_ID, -1);
            mlfId = savedInstanceState.getInt(IKey.MLF_ID, -1);
            parentId = savedInstanceState.getInt(IKey.PARENT_ID, -1);
            parentCommentUserId = savedInstanceState.getInt(IKey.USER_ID, -1);
            isFromCommentAct = savedInstanceState.getBoolean(IKey.BOOL_TAG, false);
        }
    }

    @Override
    public void finish() {
        SPHelper.get().put(SPKey.ARTICLE_COMMENT_EDITING, mContent.trim()).commit(); // 缓存数据
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean isShowToolBar() {
        return false;
    }

    private void initOnclick() {
        btSend.setOnClickListener(this);
        etInputComment.addTextChangedListener(this);
        mActivityCommentWindow.setOnTouchListener(this);
    }

    private boolean submiting;

    @Override
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;

        mContent = mContent.trim();
        switch (v.getId()) {
            case R2.id.bt_send://提交
                if (submiting) return; // 提交中
                submiting = true;
                submitComment(mContent); // 提交评论
                break;
        }
    }

    private void submitComment(String content) {
        new SubmitCommentTask(new APIExpandCallBack<SubmitCommentStateBean>() {

            @Override
            public void onSuccess(SubmitCommentStateBean stateBean) {
                mContent = "";
                handlerResult(stateBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                showShortToast(errMsg);
            }

            @Override
            public void onAfter() {
                submiting = false;
            }
        }).setTag(this).exe(articleId, content);
    }


    private void handlerResult(SubmitCommentStateBean stateBean) {
        if (stateBean.getResultCode() == 0) {
            showShortToast("评论成功");
            setResult(RESULT_OK);
            if (!isFromCommentAct) {
                startActivity(CommentActivity.newIntent(articleId, 1, BizUtils.comment.ALLOW, mlfId));
            }
            finish();
        } else {
            showShortToast(stateBean.getResultMsg());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btSend.setEnabled(s.length() > 0);
        mContent = s.toString();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { // 触摸空白区域关闭该页面
        finish();
        return false;
    }

}
