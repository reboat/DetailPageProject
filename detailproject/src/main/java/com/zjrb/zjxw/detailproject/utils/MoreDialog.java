package com.zjrb.zjxw.detailproject.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.BaseDialogFragment;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailNightThemeEvent;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailTextZoomEvent;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wanglinjie on 2017/9/21.
 */

public class MoreDialog extends BaseDialogFragment implements RadioGroup.OnCheckedChangeListener {

    protected Dialog dialog;
    @BindView(R2.id.iv_module_core_more_collect)
    ImageView ivCollect;
    @BindView(R2.id.rb_module_core_more_set_font_size_small)
    RadioButton rbSmall;
    @BindView(R2.id.rb_module_core_more_set_font_size_normal)
    RadioButton rbNormal;
    @BindView(R2.id.rb_module_core_more_set_font_size_big)
    RadioButton rbBig;
    @BindView(R2.id.rg_module_core_more_set_font_size)
    RadioGroup rgSetFontSize;
    @BindView(R2.id.tv_module_core_more_set_font_size_preview)
    TextView tvPreview;

    private DraftDetailBean mBean;

    /**
     * @return
     */
    private static MoreDialog fragment = null;

    public static MoreDialog newInstance(DraftDetailBean bean) {
        fragment = new MoreDialog();
        Bundle args = new Bundle();
        args.putSerializable(IKey.FRAGMENT_ARGS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mBean = (DraftDetailBean) getArguments().getSerializable(IKey.FRAGMENT_ARGS);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        View view = View.inflate(getContext(), R.layout.module_core_dialog_more_layout, null);
        ButterKnife.bind(this, view);
        rgSetFontSize.setOnCheckedChangeListener(this);
        if (mBean.getArticle().isFollowed()) {
            ivCollect.getDrawable().setLevel(getResources().getInteger(R.integer.level_collect_on));
        } else {
            ivCollect.getDrawable().setLevel(getResources().getInteger(R.integer.level_collect_off));
        }
        initPreview();
        builder.setView(view);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        initWindow();
        return dialog;
    }

    /**
     * 设置底部弹出框的窗口样式
     */
    private void initWindow() {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = getDialog();
        if (null != dialog) {
            dialog.getWindow().setLayout(-1, -2);
        }
    }


    public void dismissFragmentDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 弹框预览效果
     */
    private void initPreview() {
        float size = SettingBiz.get().getHtmlFontScale();
        initArticleTextSize(size);
    }

    /**
     * 初始化文章字体大小
     *
     * @param textSize
     */
    private void initArticleTextSize(float textSize) {
        if (textSize == C.FONT_SCALE_LARGE) {
            rbBig.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_LARGE);
            tvPreview.setScaleY(C.FONT_SCALE_LARGE);
        } else if (textSize == C.FONT_SCALE_STANDARD) {
            rbNormal.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_STANDARD);
            tvPreview.setScaleY(C.FONT_SCALE_STANDARD);
        } else {
            rbSmall.setChecked(true);
            tvPreview.setScaleX(C.FONT_SCALE_SMALL);
            tvPreview.setScaleY(C.FONT_SCALE_SMALL);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissFragmentDialog();
    }

    @OnClick({R2.id.ll_module_core_more_collect, R2.id.ll_module_core_more_night, R2.id.ll_module_core_more_feed_back, R2.id.btn_dialog_close})
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_module_core_more_collect) {
            newsTopicCollect();
        } else if (i == R.id.ll_module_core_more_night) {
            ThemeMode.setUiMode(!ThemeMode.isNightMode());
            EventBus.getDefault().postSticky(new NewsDetailNightThemeEvent(!ThemeMode.isNightMode()));
            dismissFragmentDialog();

        } else if (i == R.id.ll_module_core_more_feed_back) {
            Nav.with(UIUtils.getContext()).toPath(RouteManager.FEED_BACK);
            dismissFragmentDialog();

        } else if (i == R.id.btn_dialog_close) {
            dismissFragmentDialog();
        }
    }

    /**
     * 稿件收藏
     */
    private void newsTopicCollect() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                if (!mBean.getArticle().isFollowed()) {
                    ivCollect.getDrawable().setLevel(getResources().getInteger(R.integer.level_collect_on));
                    mBean.getArticle().setFollowed(true);
                    T.showShort(UIUtils.getApp(), "已收藏成功");
                } else {
                    ivCollect.getDrawable().setLevel(getResources().getInteger(R.integer.level_collect_off));
                    mBean.getArticle().setFollowed(false);
                    T.showShort(UIUtils.getApp(), "已取消收藏");
                }

                dismissFragmentDialog();

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getApp(), "收藏失败");
                dismissFragmentDialog();
            }

        }).setTag(this).exe(mBean.getArticle().getId(), !mBean.getArticle().isFollowed());
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_module_core_more_set_font_size_small) {
            tvPreview.setScaleX(C.FONT_SCALE_SMALL);
            tvPreview.setScaleY(C.FONT_SCALE_SMALL);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_SMALL);
            EventBus.getDefault().postSticky(new NewsDetailTextZoomEvent(C.FONT_SCALE_SMALL));

        } else if (checkedId == R.id.rb_module_core_more_set_font_size_normal) {
            tvPreview.setScaleX(C.FONT_SCALE_STANDARD);
            tvPreview.setScaleY(C.FONT_SCALE_STANDARD);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_STANDARD);
            EventBus.getDefault().postSticky(new NewsDetailTextZoomEvent(C.FONT_SCALE_STANDARD));

        } else if (checkedId == R.id.rb_module_core_more_set_font_size_big) {
            tvPreview.setScaleX(C.FONT_SCALE_LARGE);
            tvPreview.setScaleY(C.FONT_SCALE_LARGE);
            SettingBiz.get().setHtmlFontScale(C.FONT_SCALE_LARGE);
            EventBus.getDefault().postSticky(new NewsDetailTextZoomEvent(C.FONT_SCALE_LARGE));
        }
    }

}
