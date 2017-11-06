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
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.WebBiz;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页 WebView - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */
public class NewsDetailWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.ILifecycle, View.OnAttachStateChangeListener, MoreDialog.IWebViewDN, MoreDialog.IWebViewTextSize {

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
        mWebJsInterface = mWebView.getWebJs();
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
                        if (mWebJsInterface != null && imgSrcs != null && imgSrcs.length > 0) {
                            for (int i = 0; i < imgSrcs.length; i++) {
                                if (!TextUtils.isEmpty(imgSrcs[i]) && imgSrcs[i].contains("?w=")) {
                                    imgSrcs[i] = imgSrcs[i].split("[?]")[0];
                                }
                            }
                            mWebJsInterface.setImgSrcs(imgSrcs);
                        }
                    }
                }, new WebBiz.TextCallBack() {

                    @Override
                    public void callBack(String text) {
                        if (mWebJsInterface != null && !TextUtils.isEmpty(text)) {
                            mWebJsInterface.setHtmlText(text);
                        }
                    }
                });
        ResourceBiz sp = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);
        String css_js = "";
        String css = "<link id=\"ui_mode_link\" charset=\"UTF-8\" href=\"%1$s\" rel=\"stylesheet\" type=\"text/css\"/>";
        String html = "<script type=\"text/javascript\" charset=\"UTF-8\" src=\"%1$s\"></script>";
        //TODO WLJ 这里样式和后台下发的css有冲突
        css_js += String.format(css, uiModeCssUri);
        css_js += String.format(html, "file:///android_asset/js/basic.js");
        //测试用js
        css_js +=  String.format(css, "http://192.168.1.100/zb/20171026/static/css/zjxw.v2.css");
        css_js += String.format(html, "http://192.168.1.100/zb/20171026/static/js/client.js");
        //CSS
        if (sp != null && sp.css != null && !sp.css.isEmpty()) {
            for (int i = 0; i < sp.css.size(); i++) {
                css_js += String.format(css, sp.css.get(i));
            }
        }
        //JS
        if (sp != null && sp.js != null && !sp.js.isEmpty()) {
            for (int i = 0; i < sp.js.size(); i++) {
                css_js += String.format(html, sp.js.get(i));
            }
        }

        String htmlResult = String.format(htmlCode, css_js, htmlBody);
        mWebView.loadDataWithBaseURL(null, htmlResult, "text/html", "utf-8", null);
    }

    private WebSettings settings;

    private void initWebView() {
        // 隐藏到滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollContainer(false);
        // 夜间模式
        if (ThemeMode.isNightMode()) {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_202124_night));
        } else {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_ffffff));
        }

        settings = mWebView.getSettings();
        //TODO  WLJ 临时去除缓存
        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启DOM storage API 功能
        settings.setDomStorageEnabled(false);
        // 开启database storage API功能
        settings.setDatabaseEnabled(false);
        settings.setAppCacheEnabled(false);
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
                } else if (itemView.getContext() instanceof TopicAdapter.CommonOptCallBack) {
                    ((TopicAdapter.CommonOptCallBack) itemView.getContext())
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

    @Override
    public void onViewAttachedToWindow(View v) {
        onResume();
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        onPause();
    }

    /**
     * 切换夜间模式
     */
    @Override
    public void onChangeTheme() {
        setCssJSWebView();
    }

    @Override
    public void onChangeTextSize(float textSize) {
        //设置缩放比例
        int zoom = Math.round(SettingBiz.get().getHtmlFontScale() * 100);
        settings.setTextZoom(zoom);
    }
}
