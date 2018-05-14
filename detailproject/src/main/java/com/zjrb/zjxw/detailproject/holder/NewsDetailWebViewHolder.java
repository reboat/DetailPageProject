package com.zjrb.zjxw.detailproject.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
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
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.biz.ResourceBiz;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.fragment.ScanerBottomFragment;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.webjs.LongClickCallBack;
import com.zjrb.core.utils.webjs.WebJsInterface;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.WebBiz;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;

/**
 * 新闻详情页 WebView - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */
public class NewsDetailWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.ILifecycle, View.OnAttachStateChangeListener, MoreDialog.IWebViewDN,
        MoreDialog.IWebViewTextSize, View.OnLayoutChangeListener, LongClickCallBack {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;
    private WebJsInterface mWebJsInterface;

    /**
     * WebView的高度
     */
    private int mWebViewHeight;
    /**
     * 当前稿件阅读进度
     */
    private float mReadingScale;
    //是否来自红船号详情页
    private boolean mHasVideoUrl = false;

    public boolean isRedBoatActivity() {
        return isRedBoatActivity;
    }

    public void setRedBoatActivity(boolean redBoatActivity) {
        isRedBoatActivity = redBoatActivity;
    }

    private boolean isRedBoatActivity = false;

    public NewsDetailWebViewHolder(ViewGroup parent, boolean hasVideoUrl) {
        super(UIUtils.inflate(R.layout.module_detail_layout_web, parent, false));
        ButterKnife.bind(this, itemView);
        mHasVideoUrl = hasVideoUrl;
        initWebView();
        itemView.addOnAttachStateChangeListener(this);
        mWebView.addOnLayoutChangeListener(this);
        mWebView.setLongClickCallBack(this);
    }

    /**
     * 设置网脉数据
     *
     * @param bean
     */
    private OutSizeAnalyticsBean getWMData(DraftDetailBean.ArticleBean bean) {
        String mlf_id;
        if (isRedBoatActivity) {
            mlf_id = bean.getGuid() + "";
        } else {
            mlf_id = bean.getMlf_id() + "";
        }
        return OutSizeAnalyticsBean.getInstance()
                .setEventCode("A0010")
                .setUmCode("A0010")
                .setObjectID(mlf_id)
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.PictureType)
                .setClassifyID(bean.getChannel_id())
                .setClassifyName(bean.getChannel_name())
                .setPageType("图片预览页")
                .setSelfobjectID(bean.getId() + "");
    }

    /**
     * 如需要动态加载css,可直接传入url
     * 新增接口拉取css和js
     */
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

    private String css_js;
    private int audioCount = 0;

    /**
     * 设置CSS和JS
     */
    private void setCssJSWebView() {
        String htmlCode = AppUtils.getAssetsText(C.HTML_RULE_PATH);
        String uiModeCssUri = ThemeMode.isNightMode()
                ? C.NIGHT_CSS_URI : C.DAY_CSS_URI;
        String htmlBody = WebBiz.parseHandleHtml(TextUtils.isEmpty(mData.getArticle().getContent
                        ()) ? "" : mData.getArticle().getContent(),
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

    private String htmlResult;

    private WebSettings settings;

    private void initWebView() {
        // 隐藏到滚动条
        mWebView.hasVideoUrl(mHasVideoUrl);
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
            if (context instanceof NewsDetailAdapter.CommonOptCallBack) {
                ((NewsDetailAdapter.CommonOptCallBack) context).onOptPageFinished();
                return;
            } else if (context instanceof TopicAdapter.CommonOptCallBack) {
                ((TopicAdapter.CommonOptCallBack) context).onOptPageFinished();
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
                if (recyclerView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
                    NewsDetailAdapter.CommonOptCallBack callback = (NewsDetailAdapter
                            .CommonOptCallBack) recyclerView.getContext();
                    callback.onReadingScaleChange(mReadingScale);
                } else if (recyclerView.getContext() instanceof TopicAdapter.CommonOptCallBack) {
                    TopicAdapter.CommonOptCallBack callback = (TopicAdapter.CommonOptCallBack)
                            recyclerView.getContext();
                    callback.onReadingScaleChange(mReadingScale);
                }
            }
        }
    };

    /**
     * 添加到window
     *
     * @param v
     */
    @Override
    public void onViewAttachedToWindow(View v) {
        onResume();
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).addOnScrollListener(mScrollListener);
        }
    }

    /**
     * 从window移除
     *
     * @param v
     */
    @Override
    public void onViewDetachedFromWindow(View v) {
        onPause();
        if (v == itemView && itemView.getParent() instanceof RecyclerView) {
            ((RecyclerView) itemView.getParent()).addOnScrollListener(mScrollListener);
        }
    }

    /**
     * 切换夜间模式
     */
    @Override
    public void onChangeTheme() {

        if (ThemeMode.isNightMode()) {
            final String execUrl = "javascript:applyNightTheme();";
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(execUrl);
                }
            });
        } else {
            final String execUrl = "javascript:applyDayTheme();";
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(execUrl);
                }
            });
        }

        mWebView.callback_zjxw_js_isAppOpenNightTheme(ThemeMode.isNightMode());
    }

    /**
     * 设置webview文字大小
     *
     * @param textSize
     */
    @Override
    public void onChangeTextSize(float textSize) {
        //设置缩放比例
        int zoom = Math.round(SettingBiz.get().getHtmlFontScale() * 100);
        settings.setTextZoom(zoom);
    }

    /**
     * 计算webview高度
     *
     * @param v
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param oldLeft
     * @param oldTop
     * @param oldRight
     * @param oldBottom
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {
        mWebViewHeight = bottom - top;
    }

    /**
     * 长按识别二维码弹框
     *
     * @param imgUrl
     */
    @Override
    public void onLongClickCallBack(String imgUrl, boolean isScanerImg) {
        ScanerBottomFragment.newInstance().showDialog((AppCompatActivity) UIUtils
                .getActivity()).isScanerImg(isScanerImg).setActivity(UIUtils.getActivity()).setImgUrl(imgUrl);
    }

    /**
     * 关闭线程池
     */
    public void stopThreadPool() {
        if (mWebView != null) {
            mWebView.stopThreadPool();
        }
    }
}
