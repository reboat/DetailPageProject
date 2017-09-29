package com.zjrb.zjxw.detailproject.holder;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.ui.widget.WebFullScreenContainer;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;
import com.zjrb.zjxw.detailproject.utils.WebBiz;
import com.zjrb.zjxw.detailproject.webjs.WebJsInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页 WebView - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */
public class NewsDetailWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.ILifecycle, View.OnAttachStateChangeListener, View
        .OnLayoutChangeListener {

    @BindView(R2.id.web_view)
    WebView mWebView;
    private WebJsInterface mWebJsInterface;

    /**
     * WebView的高度
     */
    private int mWebViewHeight;
    /**
     * WebView滚动到屏幕内最大值（用于计算阅读百分比）
     */
    private int mWebViewMaxScroll;

    public NewsDetailWebViewHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_web, parent, false));
        ButterKnife.bind(this, itemView);
        initWebView();
        itemView.addOnAttachStateChangeListener(this);
        mWebView.addOnLayoutChangeListener(this);
    }

    /**
     * 如需要动态加载css,可直接传入url
     * 新增接口拉取css和js
     */
    @Override
    public void bindView() {
        if (mData.getArticle().getContent() == null) return;
        itemView.setOnClickListener(null);
        String htmlCode = AppUtils.getAssetsText(C.HTML_RULE_PATH);
        String uiModeCssUri = ThemeMode.isNightMode()
                ? C.NIGHT_CSS_URI : C.DAY_CSS_URI;
        String htmlBody = WebBiz.parseHandleHtml(TextUtils.isEmpty(mData.getArticle().getContent()) ? "" : mData.getArticle().getContent(),
                new WebBiz.ImgSrcsCallBack() {
                    @Override
                    public void callBack(String[] imgSrcs) {
                        mWebJsInterface.setImgSrcs(imgSrcs);
                    }
                }, new WebBiz.TextCallBack() {

                    @Override
                    public void callBack(String text) {
                        mWebJsInterface.setHtmlText(text);
                    }
                });
        //TODO WLJ  服务器如果有返回则使用服务器
        String htmlResult = String.format(htmlCode, uiModeCssUri, htmlBody);
        mWebView.loadDataWithBaseURL(null, htmlResult, "text/html", "utf-8", null);
    }

    private void initWebView() {
        mWebView.setFocusable(false);

        // 隐藏到滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollContainer(false);
        //注入支持的本地方法
//        mWebJsInterface = new WebJsInterface(itemView.getContext());
        mWebJsInterface = WebJsInterface.getInstance(itemView.getContext());
        mWebView.addJavascriptInterface(mWebJsInterface, WebJsInterface.JS_NAME);

        // 夜间模式
        if (ThemeMode.isNightMode()) {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_202124_night));
        } else {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_ffffff));
        }

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); // 启用支持javaScript
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 没网使用缓存
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 禁止横向滑动
        settings.setDomStorageEnabled(true);//开启DOM storage API功能
        settings.setTextZoom(Math.round(SettingBiz.get().getHtmlFontScale() * 100)); // 字体缩放倍数
        settings.setUseWideViewPort(true); // 视频全屏点击支持回调
        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccess(true); // 允许访问文件

        // WebView在安卓5.0之前默认允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // 因为加载的是html文本，所以onPageStart时机比较合适
                if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
                    ((NewsDetailAdapter.CommonOptCallBack) itemView.getContext())
                            .onOptPageFinished();
                } else if (itemView.getContext() instanceof ActivityTopicAdapter.CommonOptCallBack) {
                    ((ActivityTopicAdapter.CommonOptCallBack) itemView.getContext())
                            .onOptPageFinished();
                }
            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            private FrameLayout container;
            private View videoView;
            private CustomViewCallback customViewCallback;

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (videoView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                FrameLayout decor = getDecorView();
                ((Activity) itemView.getContext())
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                container = new WebFullScreenContainer(itemView.getContext());
                container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                decor.addView(container, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                videoView = view;
                customViewCallback = callback;
            }

            @Override
            public void onHideCustomView() {
                if (videoView == null) {
                    return;
                }
                ((Activity) itemView.getContext())
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                FrameLayout decor = getDecorView();
                decor.removeView(container);
                container = null;
                videoView = null;
                customViewCallback.onCustomViewHidden();
                mWebView.setVisibility(View.VISIBLE);
            }


        });
    }

    protected FrameLayout getDecorView() {
        return (FrameLayout) ((BaseActivity) itemView.getContext()).getWindow().getDecorView();
    }

    @Override
    public void onResume() {
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    // RecyclerView滚动监听
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mWebViewMaxScroll =
                    Math.max(recyclerView.getHeight() - mWebView.getTop(), mWebViewHeight);
        }

    };

    @Override
    public void onViewAttachedToWindow(View v) {
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).addOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).removeOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {
        mWebViewHeight = bottom - top;
    }
}
