package com.zjrb.zjxw.detailproject.holder;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.WebFullScreenContainer;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.webjs.WebJsInterface;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailNightThemeEvent;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailTextZoomEvent;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;
import com.zjrb.zjxw.detailproject.utils.WebBiz;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页 WebView - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */
public class NewsDetailWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.ILifecycle, View.OnAttachStateChangeListener {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;
    private WebJsInterface mWebJsInterface;

    public NewsDetailWebViewHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_web, parent, false));
        ButterKnife.bind(this, itemView);
        initWebView();
        itemView.addOnAttachStateChangeListener(this);
    }

    /**
     * 如需要动态加载css,可直接传入url
     * 新增接口拉取css和js
     */
    @Override
    public void bindView() {
        if (mData.getArticle().getContent() == null) return;
        itemView.setOnClickListener(null);
        setCssJSWebView();
    }

    /**
     * 设置CSS和JS
     */
    private void setCssJSWebView() {
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
        ResourceBiz sp = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);
        String css_js = "";
        String css = "<link id=\"ui_mode_link\" href=\"%1$s\" rel=\"stylesheet\" type=\"text/css\"/>";
        String html = "<script type=\"text/javascript\" src=\"%1$s\"></script>";
        //CSS
        if (sp == null ||sp.css == null || sp.css.isEmpty()) {
            css_js += String.format(css, uiModeCssUri);
        } else {
            for (int i = 0; i < sp.css.size(); i++) {
                css_js += String.format(css, sp.css.get(i));
            }
        }

        //JS
        css_js += String.format(html, "file:///android_asset/js/basic.js");
        if (sp == null || sp.js == null || sp.js.isEmpty()) {
            css_js += String.format(html, "file:///android_asset/js/basic.js");
        } else {
            for (int i = 0; i < sp.js.size(); i++) {
                css_js += String.format(html, sp.js.get(i));
            }
        }

        String htmlResult = String.format(htmlCode, css_js, htmlBody);
        mWebView.loadDataWithBaseURL(null, htmlResult, "text/html", "utf-8", null);
    }

    private WebSettings settings;

    private void initWebView() {
        //TODO WLJ 删除无用代码
        mWebView.setFocusable(false);

        // 隐藏到滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollContainer(false);
        //注入支持的本地方法
        mWebJsInterface = new WebJsInterface(itemView.getContext());
        mWebView.addJavascriptInterface(mWebJsInterface, WebJsInterface.JS_NAME);
        mWebJsInterface.setWebJsCallBack(mWebView);
        // 夜间模式
        if (ThemeMode.isNightMode()) {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_202124_night));
        } else {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_ffffff));
        }

        settings = mWebView.getSettings();
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Nav.with(itemView.getContext()).to(url);
                }
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
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    /**
     * @param event 删除评论后刷新列表
     */
    //TODO  WLJ 优化  回调
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(Object event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (event != null) {
            if (event instanceof NewsDetailTextZoomEvent) {
                //设置缩放比例
                int zoom = Math.round(SettingBiz.get().getHtmlFontScale() * 100);
                settings.setTextZoom(zoom);
            } else if (event instanceof NewsDetailNightThemeEvent) {
                //设置夜间模式
                setCssJSWebView();
            }
        }

    }

    @Override
    public void onViewAttachedToWindow(View v) {
        onResume();
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        onPause();
    }

}
