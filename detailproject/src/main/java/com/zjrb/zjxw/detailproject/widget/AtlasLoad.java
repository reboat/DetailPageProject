package com.zjrb.zjxw.detailproject.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.core.network.api.ApiTask;
import com.zjrb.core.R;
import com.zjrb.core.utils.click.ClickTracker;

import cn.daily.news.biz.core.constant.APICode;
import cn.daily.news.biz.core.network.compatible.ILoad;

/**
 * 浙江新闻 - LoadViewHolder
 *
 * @author a_liYa
 * @date 2016/12/13 20:38.
 */
public class AtlasLoad implements View.OnClickListener, ILoad {

    // 根布局
    private View rootView;
    private FrameLayout fitSysHelper;

    // 失败view
    private ViewStub failedStub;
    private View failedView;

    // 网络错误
    private ViewStub networkStub;
    private View networkView;

    // 加载中view
    private View loadView;

    // 页面被替换view
    private View pageView;

    private View tipView;

    private ApiTask apiTask;

    private int pageViewIndex = -1;

    /**
     * 构造函数
     *
     * @param pageView   需要替换成加载动画的View
     * @param pageParent 需要追加加载动画的父容器（前提时pageView没有）
     */
    public AtlasLoad(@NonNull View pageView, ViewGroup pageParent) {
        this.pageView = pageView;
        if (pageView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) pageView.getParent();
            rootView = inflate(R.layout.module_detail_atlas_layout_global_load, parent, false);
            initView();

            // 替换成加载布局
            pageViewIndex = parent.indexOfChild(pageView);
            parent.removeView(pageView);

            // 为了适配fitSystemWindow=true属性
            fitSysHelper.addView(pageView);

            parent.addView(rootView, pageViewIndex, pageView.getLayoutParams());

        } else if (pageParent != null) {
            rootView = inflate(R.layout.module_detail_atlas_layout_global_load, pageParent, false);
            initView();

            // 为了适配fitSystemWindow=true属性
            fitSysHelper.addView(pageView);

            pageView.setVisibility(View.GONE);
            pageParent.addView(rootView);
        }

    }

    private void initView() {
        loadView = rootView.findViewById(R.id.layout_loading);
        failedStub = rootView.findViewById(R.id.view_stub_failed);
        networkStub = rootView.findViewById(R.id.view_stub_network_error);
        fitSysHelper = rootView.findViewById(R.id.fit_sys_helper);
        tipView = rootView.findViewById(R.id.iv_pre_tip);
    }

    // 显示失败页
    public void showFailed(int errCode) {
        loadView.setVisibility(View.GONE);
        tipView.setVisibility(View.GONE);
        switch (errCode) {
            case APICode.error.CANCEL_DRAFT: // 撤稿
                finishLoad();
                break;
            case APICode.code.CONNECT_EXCEPTION: // 网络连接异常
                if (networkView == null) {
                    networkView = networkStub.inflate();
                    networkView.findViewById(R.id.layout_network_error).setOnClickListener(this);
                } else {
                    networkView.setVisibility(View.VISIBLE);
                }
                break;
            default: // 普通失败
                if (failedView == null) {
                    failedView = failedStub.inflate();
                    failedView.findViewById(R.id.layout_failed).setOnClickListener(this);
                } else {
                    failedView.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    public void setAPITask(ApiTask apiBaseTask) {
        apiTask = apiBaseTask;
    }


    // 显示加载中
    public void showLoading() {
        failedStub.setVisibility(View.GONE);
        networkStub.setVisibility(View.GONE);
        loadView.setVisibility(View.VISIBLE);
        tipView.setVisibility(View.VISIBLE);
    }

    // 关闭Load页面
    public void finishLoad() {
        if (rootView == null) return;

        final ViewGroup parent = (ViewGroup) rootView.getParent();
        if (pageView.getParent() != null && pageView.getParent() instanceof ViewGroup) {
            ((ViewGroup) pageView.getParent()).removeView(pageView);
        }
        if (pageView.getVisibility() != View.VISIBLE) {
            pageView.setVisibility(View.VISIBLE);
        }
        fitSysHelper.removeAllViews();
        if (parent != null) {
            parent.addView(pageView, pageViewIndex);
        }
        rootView.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (rootView != null) {
                    rootView.setVisibility(View.INVISIBLE);
                    if (parent != null) {
                        try {
                            parent.removeView(rootView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.layout_failed || v.getId() == R.id.layout_network_error) {
            showLoading();
            if (apiTask != null) {
                apiTask.retryExe();
            }
        }
    }

    private static View inflate(@LayoutRes int resource, ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, attachToRoot);
    }
}
