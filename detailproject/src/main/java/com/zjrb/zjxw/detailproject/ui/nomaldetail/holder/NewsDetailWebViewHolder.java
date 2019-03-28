package com.zjrb.zjxw.detailproject.ui.nomaldetail.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.commonwebview.webview.CommonWebView;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.recycleView.BaseRecyclerViewHolder;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.ui.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.utils.DetailWebViewImpl;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.SettingBiz;
import com.zjrb.zjxw.detailproject.utils.global.C;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.constant.Constants;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.model.ResourceBiz;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import webutils.CssJsUtils;

/**
 * 新闻详情页 WebView - ViewHolder
 * Created by wanglinjie.
 * create time:2017/7/18  上午09:14
 */
public class NewsDetailWebViewHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        NewsDetailAdapter.ILifecycle, View.OnAttachStateChangeListener, MoreDialog.IWebViewDN,
        MoreDialog.IWebViewTextSize, View.OnLayoutChangeListener {

    @BindView(R2.id.web_view)
    CommonWebView mWebView;
    private DetailWebViewImpl webImpl;
    private JsMultiInterfaceImp jsInterfaceImp;
    /**
     * WebView的高度
     */
    private int mWebViewHeight;
    /**
     * 当前稿件阅读进度
     */
    private float mReadingScale;

    public NewsDetailWebViewHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_web, parent, false));
        ButterKnife.bind(this, itemView);
        itemView.addOnAttachStateChangeListener(this);
        mWebView.addOnLayoutChangeListener(this);
        //初始化设置
        webImpl = new DetailWebViewImpl();
        //必须要先设置绑定对象
        webImpl.setWebViewJsObject(C.JS_OBJ_NAME);
        jsInterfaceImp = new JsMultiInterfaceImp(mWebView, webImpl.getWebViewJsObject(), itemView.getContext());
        webImpl.setJsObject(jsInterfaceImp);
        mWebView.setHelper(webImpl);
    }

    /**
     * 如需要动态加载css,可直接传入url
     * 新增接口拉取css和js
     */
    @Override
    public void bindView() {
        String htmlBody = "";
        String htmlResult;
        String htmlCode = AppUtils.getAssetsText(C.HTML_RULE_PATH);
        String uiModeCssUri = ThemeMode.isNightMode()
                ? C.NIGHT_CSS_URI : C.DAY_CSS_URI;
        //可以用webview组件中的方法代替
        if (!TextUtils.isEmpty(mData.getArticle().getContent())) {
            if(mData.getArticle().isNative_live()){
                //简介
                htmlBody = jsInterfaceImp.setAttrHtmlSrc(mData.getArticle().getNative_live_info().getIntro());
            }else{
                htmlBody = jsInterfaceImp.setAttrHtmlSrc(mData.getArticle().getContent());
            }
        }
        ResourceBiz sp = SPHelper.get().getObject(Constants.Key.INITIALIZATION_RESOURCES);
        if(sp != null){
            htmlResult = CssJsUtils.get(itemView.getContext()).setmHelper(webImpl).detailInjectCssJs(htmlCode, htmlBody, uiModeCssUri, "file:///android_asset/js/basic.js", sp.css, sp.js);
        }else{
            htmlResult = CssJsUtils.get(itemView.getContext()).setmHelper(webImpl).detailInjectCssJs(htmlCode, htmlBody, uiModeCssUri, "file:///android_asset/js/basic.js", null, null);
        }
        mWebView.loadDataWithBaseURL(null, htmlResult, "text/html", "utf-8", null);
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
        mWebView.getSettings().setTextZoom(zoom);
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

}
