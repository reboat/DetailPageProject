package com.zjrb.zjxw.detailproject.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.webjs.WebJsInterface;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.utils.WebBiz;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;

/**
 * 类描述：红船号详情页WebView - ViewHolder
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/16 0900
 */

public class RedBoatWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        RedBoatAdapter.ILifecycle, View.OnAttachStateChangeListener, View
        .OnLayoutChangeListener {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;

    private WebSettings settings = null;
    private WebJsInterface mWebJsInterface;
    private int audioCount = 0;
    //WebView的高度
    private int mWebViewHeight;
    //当前稿件阅读进度
    private float mReadingScale;
    private String css_js;
    private String htmlResult;

    public RedBoatWebViewHolder(@NonNull ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_web, parent, false));
        ButterKnife.bind(this, itemView);
        initWebView();
        itemView.addOnAttachStateChangeListener(this);
        mWebView.addOnLayoutChangeListener(this);
    }

    private void initWebView() {
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollContainer(false);

        // 夜间模式
        if (ThemeMode.isNightMode()) {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color
                    .bc_202124_night));
        } else {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color
                    .bc_ffffff));
        }

        settings = mWebView.getSettings();
        settings.setTextZoom(Math.round(SettingBiz.get().getHtmlFontScale() * 100));
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(false);
        settings.setDatabaseEnabled(false);
        settings.setAppCacheEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    //点击话题链接
                    if (url.contains("topic.html?id=")) {
                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800016", "800016")
                                .setEvenName("点击话题标签")
                                .setPageType("新闻详情页")
                                .build()
                                .send();

                        //官员名称
                    } else if (url.contains("gy.html?id=")) {
                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800017", "800017")
                                .setEvenName("点击官员名称")
                                .setPageType("新闻详情页")
                                .setOtherInfo(Analytics.newOtherInfo()
                                        .put("customObjectType", "OfficerType")
                                        .toString())
                                .build()
                                .send();
                    } else {
                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800015", "800015")
                                .setEvenName("链接点击")
                                .setPageType("新闻详情页")
                                .setOtherInfo(Analytics.newOtherInfo()
                                        .put("mediaURL", url)
                                        .toString())
                                .build()
                                .send();
                    }

                    Nav.with(itemView.getContext()).to(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onWebPageComplete();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

        });

        //选择webview加载时机
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 30) {
                    onWebPageComplete();
                }
            }
        });
    }

    /**
     * webview加载
     */
    private void onWebPageComplete() {
        Context context = itemView.getContext();
        while (context instanceof ContextThemeWrapper) {
            if (context instanceof RedBoatAdapter.CommonOptCallBack) {
                ((RedBoatAdapter.CommonOptCallBack) context).onOptPageFinished();
                return;
            }
            context = ((ContextThemeWrapper) context).getBaseContext();
        }
    }

    /**
     * webview同步页面生命周期
     */
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
            if (audioCount > 0) {
                final String execUrl = "javascript:musicPause();";
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl(execUrl);
                    }
                });
            }
        }
    }

    @Override
    public void bindView() {
        mWebJsInterface = mWebView.getWebJs();
        //设置网脉数据
        if (mWebJsInterface != null && mData != null && mData.getArticle() != null) {
            mWebJsInterface.setOutSizeAnalyticsBean(getWMData(mData.getArticle()));
        }
        if (mData.getArticle().getContent() == null) return;
        itemView.setOnClickListener(null);
        setCssJSWebView();
    }

    /**
     * 设置网脉数据
     *
     * @param bean
     */
    private OutSizeAnalyticsBean getWMData(DraftDetailBean.ArticleBean bean) {
        return OutSizeAnalyticsBean.getInstance()
                .setEventCode("A0010")
                .setUmCode("A0010")
                .setObjectID(bean.getGuid() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.PictureType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("图片预览页")
                .setSelfobjectID(bean.getId() + "");
    }

    /**
     * 设置CSS和JS
     */
    private void setCssJSWebView() {
        String htmlCode = AppUtils.getAssetsText(C.HTML_RULE_PATH);
        String uiModeCssUri = ThemeMode.isNightMode() ? C.NIGHT_CSS_URI : C.DAY_CSS_URI;
        String htmlBody = WebBiz.parseHandleHtml(
                TextUtils.isEmpty(mData.getArticle().getContent()) ? "" : mData.getArticle().getContent(),
                new WebBiz.ImgSrcsCallBack() {
                    @Override
                    public void callBack(String[] imgSrcs) {
                        if (mWebJsInterface != null && imgSrcs != null && imgSrcs.length > 0) {
                            for (int i = 0; i < imgSrcs.length; i++) {
                                if (!TextUtils.isEmpty(imgSrcs[i]) && (imgSrcs[i].contains("?w=")
                                        || imgSrcs[i].contains("?width="))) {
                                    imgSrcs[i] = imgSrcs[i].split("[?]")[0];
                                }
                            }
                            mWebJsInterface.setImgSrcs(imgSrcs);
                        }
                    }
                }, new WebBiz.ImgASrcsCallBack() {//超链接
                    @Override
                    public void callBack(List<Map<String, String>> imgSrcs) {
                        if (mWebJsInterface != null && imgSrcs != null && imgSrcs.size() > 0) {
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
                }, new WebBiz.AudioCallBack() {

                    @Override
                    public void callBack(int count) {
                        audioCount = count;
                    }
                });
        ResourceBiz sp = SPHelper.get().getObject(SPHelper.Key.INITIALIZATION_RESOURCES);

        css_js = "";
        String css = "<link id=\"ui_mode_link\" charset=\"UTF-8\" href=\"%1$s\" " +
                "rel=\"stylesheet\" type=\"text/css\"/>";
        String html = "<script type=\"text/javascript\" charset=\"UTF-8\" src=\"%1$s\"></script>";
        css_js += String.format(css, uiModeCssUri);
        css_js += String.format(html, "file:///android_asset/js/basic.js");

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

        htmlResult = String.format(htmlCode, css_js, htmlBody);
        mWebView.loadDataWithBaseURL(null, htmlResult, "text/html", "utf-8", null);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        onResume();
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).addOnScrollListener(mScrollListener);
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        onPause();
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).addOnScrollListener(mScrollListener);
        }
    }

    /**
     * 阅读深度记录
     */
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mWebViewHeight > 0) {
                //当前阅读进度
                float tempScale = (recyclerView.getHeight() - mWebView.getTop()) * 1f /
                        mWebViewHeight;
                //取最大阅读进度
                if (tempScale > mReadingScale) {
                    mReadingScale = tempScale;
                }
                if (mReadingScale > 1) mReadingScale = 1;
                if (recyclerView.getContext() instanceof RedBoatAdapter.CommonOptCallBack) {
                    RedBoatAdapter.CommonOptCallBack callback = (RedBoatAdapter
                            .CommonOptCallBack) recyclerView.getContext();
                    callback.onReadingScaleChange(mReadingScale);
                }
            }
        }
    };

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {
        mWebViewHeight = bottom - top;
    }
}