package com.zjrb.zjxw.detailproject.comment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
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

public class CommentWindowActivity extends BaseActivity implements
        TextWatcher, View.OnTouchListener {
    public int articleId = -1;
    public int mlfId = -1;
    public int parentId = -1;
    public boolean isFromCommentAct = false;

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

    /**
     * 评论内容
     */
    private String mContent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_comment_window);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initOnclick();
        initEditText();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (intent.hasExtra(Key.ARTICLE_ID)) {
                articleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
            }
            if (intent.hasExtra(Key.MLF_ID)) {
                mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
            }
            if (intent.hasExtra(Key.PARENT_ID)) {
                parentId = Integer.parseInt(data.getQueryParameter(Key.PARENT_ID));
            }
            if (intent.hasExtra(Key.FROM_TYPE)) {
                isFromCommentAct = data.getBooleanQueryParameter(Key.FROM_TYPE, false);
            }
        }
    }


    /**
     * 初始化编辑框
     */
    private void initEditText() {
        String content = SPHelper.get().get(Key.ARTICLE_COMMENT_EDITING, "");
        etInputComment.setText(content);
        etInputComment.setSelection(content.length());
    }

    @Override
    public void finish() {
        SPHelper.get().put(Key.ARTICLE_COMMENT_EDITING, mContent.trim()).commit(); // 缓存数据
        super.finish();
        overridePendingTransition(0, 0);
    }

    /**
     * 初始化监听
     */
    private void initOnclick() {
        etInputComment.addTextChangedListener(this);
        activityCommentWindow.setOnTouchListener(this);
    }

    private boolean submiting;

    @OnClick({R2.id.iv_send_comment, R2.id.iv_close_window})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R2.id.iv_send_comment) {
            if (submiting) return; // 提交中
            submiting = true;
            submitComment(mContent); // 提交评论
        } else {
            finish();
        }
    }


    /**
     * @param content 提交评论
     */
    private void submitComment(String content) {
        new CommentSubmitTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData stateBean) {
                handlerResult(stateBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
                submiting = false;
            }
        }).setTag(this).exe(articleId, content, parentId);
    }

    /**
     * @param stateBean UI展现
     */
    private void handlerResult(BaseInnerData stateBean) {
        if (stateBean.getResultCode() == 0) {
            T.showShort(getBaseContext(), "评论成功");
            setResult(RESULT_OK);
            if (!isFromCommentAct) {
                Nav.with(CommentWindowActivity.this).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
                        .buildUpon()
                        .build(), RESULT_OK);
            }
            finish();
        } else {
            T.showShort(getBaseContext(), stateBean.getResultMsg());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0 && s.length() <= 250) {
            ivSendComment.setEnabled(true);
            tvCommentNum.setText(s.length() + "/ 250");
        } else {
            ivSendComment.setEnabled(false);
            T.showShort(this, "文字过长");
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

}
