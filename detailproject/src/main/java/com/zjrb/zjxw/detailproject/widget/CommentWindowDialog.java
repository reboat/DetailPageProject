package com.zjrb.zjxw.detailproject.widget;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.JsonUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.callback.LocationCallBack;
import com.zjrb.zjxw.detailproject.task.CommentSubmitTask;
import com.zjrb.zjxw.detailproject.utils.LoadingDialogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.UserBiz;
import cn.daily.news.biz.core.constant.Constants;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.model.BaseData;
import cn.daily.news.biz.core.model.ResourceBiz;
import cn.daily.news.biz.core.network.compatible.APICallManager;
import cn.daily.news.biz.core.ui.toast.ZBToast;

/**
 * 评论框
 * <p>
 * Created by wanglinjie.
 * create time:2017/09/27  下午12:28
 */
public class CommentWindowDialog extends DialogFragment implements TextWatcher {

    @BindView(R2.id.iv_send_comment)
    ImageView ivSendComment;
    @BindView(R2.id.et_input_comment)
    EditText etInputComment;
    @BindView(R2.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R2.id.tv_replay)
    TextView mTvReplay;

    /**
     * id - 新闻列表传过来的id
     */
    public static final String ID = "id";

    /**
     * parentId
     */
    public static final String PARENT_ID = "parent_id";

    /**
     * 被回复人
     */
    public static final String REPLAYER = "replayer";

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

    protected Dialog dialog;

    private LocationCallBack locationCallBack;

    private Analytics mBean;


    /**
     * 评论标签
     */
    private String comment_tag = "";

    /**
     * 创建实例
     *
     * @return 实例对象
     * @see CommentDialogBean.Builder 建通过Builder创建
     */
    public static CommentWindowDialog newInstance(CommentDialogBean bean) {
        //JS回调
        CommentWindowDialog fragment = new CommentWindowDialog();
        Bundle args = new Bundle();
        args.putSerializable(CommentWindowDialog.ID, bean.getId());
        args.putSerializable(CommentWindowDialog.PARENT_ID, bean.getParent_id());
        args.putSerializable(CommentWindowDialog.REPLAYER, bean.getReplayer());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 设置网脉数据
     *
     * @param bean
     * @return
     */
    public CommentWindowDialog setWMData(Analytics bean) {
        mBean = bean;
        return this;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            articleId = (String) getArguments().getSerializable(CommentWindowDialog.ID);
            parentId = (String) getArguments().getSerializable(CommentWindowDialog.PARENT_ID);
            nickName = (String) getArguments().getSerializable(CommentWindowDialog.REPLAYER);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_detail_comment_window, null);
        ButterKnife.bind(this, view);
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initView();

        initWindow();
        return dialog;
    }

    public CommentWindowDialog setLocationCallBack(LocationCallBack locationCallBack) {
        this.locationCallBack = locationCallBack;
        return this;
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
     * 初始化草稿时，也需要加标签判定
     * 初始化编辑框，上一次的数据记录
     */
    private void initView() {
        LatelyComment lately = LatelyComment.getLately();
        etInputComment.addTextChangedListener(this);
        if (lately != null && TextUtils.equals(articleId, lately.id)
                && TextUtils.equals(parentId, lately.parentId)
                && !TextUtils.isEmpty(lately.text)) {
            //调用afterTextChanged
            etInputComment.setText(lately.text);
            etInputComment.setSelection(lately.text.length());
        } else {
            //无草稿有标签，则自动带上标签
            if (!TextUtils.isEmpty(SPHelper.get().get("comment_tag", ""))) {
                comment_tag = SPHelper.get().get("comment_tag", "");
                etInputComment.setText(comment_tag);
            }
        }

        etInputComment.setFilters(new InputFilter[]{new MaxTextLenthFilter(251)});
        if (!TextUtils.isEmpty(nickName)) {
            mTvReplay.setText(getString(R.string.module_core_replay_to) + nickName);
        } else {
            mTvReplay.setText(getString(R.string.module_core_send_comment));
        }
    }

    @OnClick({R2.id.iv_send_comment, R2.id.iv_close_window})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (view.getId() == R.id.iv_send_comment) {
            if (mContent.length() < 5) {
                T.showShortNow(UIUtils.getActivity(), "评论不能少于5个字");
            } else {
                if (UserBiz.get().isLoginUser()) {
                    LoadingDialogUtils.newInstance().getLoginingDialog();
                }
                submitComment(mContent); // 提交评论
            }
        } else if (view.getId() == R.id.iv_close_window) {
            dismissAllowingStateLoss();
        }
    }

    /**
     * 如果评论未提交，则保留到缓存
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //评论框中内容与标签不等时才保存草稿
        if (!TextUtils.isEmpty(mContent.trim()) && !mContent.equals(comment_tag)) {
            LatelyComment.putLately(mContent.trim(), articleId, parentId); // 缓存评论内容
        }
        //评论未提交，则将有效标签保存到本地
        if (!TextUtils.isEmpty(comment_tag)) {
            SPHelper.get().put("comment_tag", comment_tag).commit();
        }
        APICallManager.get().cancel(this);
    }

    /**
     * @param content 提交评论
     */
    private void submitComment(String content) {
        if (articleId != null && !articleId.isEmpty() && content != null && !content.isEmpty()) {
            new CommentSubmitTask(new LoadingCallBack<BaseData>() {

                @Override
                public void onSuccess(BaseData data) {
                    //提交评论成功时，才会保存评论标签
                    SPHelper.get().put("comment_tag", comment_tag).commit();
                    if (UserBiz.get().isLoginUser()) {
                        LoadingDialogUtils.newInstance().dismissLoadingDialog(true, "评论成功");
                    } else {
                        ZBToast.showShort(UIUtils.getContext(), "评论成功", R.mipmap
                                .module_core_comment_send_clickable);
                    }
                    LatelyComment.clear(articleId, parentId);
                    etInputComment.getText().clear();
                    //刷新评论
                    if (listen != null) {
                        listen.onUpdateComment();
                    }
                    try {
                        dismissAllowingStateLoss(); // 有崩溃过
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //网脉，发表评论成功
                    if (mBean != null) {
                        mBean.send();
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errMsg, int errCode) {
                    if (UserBiz.get().isLoginUser()) {
                        LoadingDialogUtils.newInstance().dismissLoadingDialog(false, errMsg);

                    } else {
                        ZBToast.showShort(UIUtils.getContext(), errMsg, R.mipmap
                                .module_core_comment_send_failed);
                    }
                }

            }).setTag(this).exe(articleId, content, parentId, locationCallBack == null ? "" + "," + "" + "," + "" : locationCallBack.onGetLocation());
        } else {
            if (UserBiz.get().isLoginUser()) {
                LoadingDialogUtils.newInstance().dismissLoadingDialog(false, "评论内容不能为空");
            } else {
                T.showShort(UIUtils.getContext(), "评论内容不能为空");
            }
        }

    }


    private int mCursor;// 用于记录变化时光标的位置

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mCursor = start + after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * 有输入变化就会调用
     * >=5字符才能提交
     *
     * @param s
     */
    @Override
    public void afterTextChanged(final Editable s) {
        if (s.length() >= 5) {
            ivSendComment.setSelected(true);
            ivSendComment.setEnabled(true);
            ivSendComment.setAlpha(1f);
        } else {
            ivSendComment.setSelected(false);
            ivSendComment.setEnabled(false);
        }

        etInputComment.removeTextChangedListener(this);
        etInputComment.setText(doCommentTag(s.toString()) != null ? doCommentTag(s.toString()) : s);
        etInputComment.addTextChangedListener(this);
        etInputComment.setSelection(mCursor);

        tvCommentNum.setText(s.length() + "/250");
        mContent = s.toString();
    }


    /**
     * 评论标签算法
     *
     * @param s
     * @return
     */
    private SpannableString doCommentTag(String s) {
        SpannableString spannableString = new SpannableString(s);
        ResourceBiz sp = SPHelper.get().getObject(Constants.Key.INITIALIZATION_RESOURCES);
        //如果正则为空，则清除标签
        if (sp == null || TextUtils.isEmpty(sp.comment_pattern)) {
            SPHelper.get().put("comment_tag", "").commit();
            comment_tag = "";
            return spannableString;
        }
        Pattern datePattern = Pattern.compile(sp.comment_pattern);
        if (datePattern == null) return null;

        if (TextUtils.isEmpty(s)) return null;
        Matcher dateMatcher = datePattern.matcher(s);
        while (dateMatcher.find()) {
            comment_tag = dateMatcher.group();
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(getcolor())), dateMatcher.start(), dateMatcher.end(), 0);
            break;
        }
        return spannableString;
    }

    private String getcolor() {
        if (ThemeMode.isNightMode()) {
            return "#4b7aae";
        } else {
            return "#036ce2";
        }
    }

    private updateCommentListener listen;

    public CommentWindowDialog setListen(updateCommentListener listen) {
        this.listen = listen;
        return this;
    }

    /**
     * 刷新评论回调
     */
    public interface updateCommentListener {

        void onUpdateComment();
    }


    /**
     * 最近未提交的评论
     *
     * @author a_liYa
     * @date 2017/10/31 上午8:52.
     */
    private static class LatelyComment {
        String text;
        String id;
        String parentId;

        private static final String KEY_LATELY_EDITING = "comment_lately_editing";

        public LatelyComment(String text, String id, String parentId) {
            this.text = text;
            this.id = id;
            this.parentId = parentId;
        }

        // 保存最近未提交的评论
        static void putLately(String text, String id, String parentId) {
            SPHelper.get()
                    .put(KEY_LATELY_EDITING,
                            JsonUtils.toJsonString(new LatelyComment(text, id, parentId))).commit();
        }

        // 获取最近未提交的评论
        static LatelyComment getLately() {
            LatelyComment data = null;
            try {
                data = JsonUtils.parseObject(
                        SPHelper.get().get(KEY_LATELY_EDITING, ""), LatelyComment.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        // 清空
        static void clear(String id, String parentId) {
            LatelyComment lately = getLately();
            // 同一个评论才清空
            if (lately != null && TextUtils.equals(lately.id, id)
                    && TextUtils.equals(lately.parentId, parentId)) {
                SPHelper.get().put(KEY_LATELY_EDITING, "").commit();
            }
        }

    }

}
