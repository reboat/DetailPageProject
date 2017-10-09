package com.zjrb.zjxw.detailproject.link;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.umeng.socialize.UMShareAPI;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder2;
import com.zjrb.core.common.biz.SettingBiz;
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.WebFullScreenContainer;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailNightThemeEvent;
import com.zjrb.zjxw.detailproject.eventBus.NewsDetailTextZoomEvent;
import com.zjrb.zjxw.detailproject.global.C;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.global.RouteManager;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.utils.WebBiz;
import com.zjrb.zjxw.detailproject.webjs.WebJsInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 链接稿 - 通用WebView - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class BrowserLinkActivity extends BaseActivity implements View.OnClickListener, TouchSlopHelper.OnTouchSlopListener {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.floor_bar)
    FitWindowsFrameLayout mFloorBar;
    @BindView(R2.id.fy_container)
    FrameLayout mContainer;
    @BindView(R2.id.fy_webview_container)
    FrameLayout mWebViewContainer;
    @BindView(R2.id.view_exise)
    LinearLayout mViewExise;

    private WebJsInterface mWebJsInterface;
    private String mUrl;
    private WebSettings settings;
    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_browser);
        ButterKnife.bind(this);
        initIntent(savedInstanceState);
        getIntentData(getIntent());
        initWebView();
        mWebView.loadUrl(mUrl);

    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null && data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
    }

    /**
     * 顶部标题
     */
    private DefaultTopBarHolder2 topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder.setViewVisible(topBarHolder.getRightText(), View.GONE);
        topBarHolder.setViewVisible(topBarHolder.getShareView(), View.VISIBLE);
        return topBarHolder.getView();
    }


    private void initIntent(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString(IKey.DATA);
        }
        if (TextUtils.isEmpty(mUrl)) {
            Intent intent = getIntent();
            if (intent != null) {
                mUrl = intent.getStringExtra(IKey.DATA);
                if (TextUtils.isEmpty(mUrl)) {
                    mUrl = getIntent().getData().toString();
                }
            }
        }
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null) return;
                mNewsDetail = draftDetailBean;
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    //别的错误
                    mWebViewContainer.setVisibility(View.GONE);
                    mViewExise.setVisibility(View.VISIBLE);
                }
            }
        }).setTag(this).exe(mArticleId);
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {

        //点赞数量
        mMenuPrised.setSelected(data.getArticle().isLiked());
        if (data.getArticle().getComment_count() <= 0) {
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            if (data.getArticle().getComment_count() < 9999) {
                mTvCommentsNum.setText(data.getArticle().getComment_count() + "");
            } else if (data.getArticle().getComment_count() > 9999) {
                mTvCommentsNum.setText(BizUtils.numFormat(data.getArticle().getComment_count(), 10000, 1) + "");
            }
        }
        //评论分级
        BizUtils.setCommentSet(mTvComment, mNewsDetail.getArticle().getComment_level());
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        int id = view.getId();
        if (R.id.iv_top_bar_back == id) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                onBackPressed();
            }
            //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
        } else if (id == R.id.iv_top_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setTextContent(getString(R.string.module_detail_share_content_from))
                    .setTitle("")
                    .setTargetUrl(mUrl)
            );
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null &&
                    BizUtils.isCanComment(this, mNewsDetail.getArticle().getComment_level())) {
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).show(getSupportFragmentManager(), "CommentWindowDialog");
                return;
            }
            //重新加载
        } else if (view.getId() == R.id.view_exise) {
            loadData();
        }
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

            @Override
            public void onError(String errMsg, int errCode) {
                //用户未登录
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(UIUtils.getApp()).onActivityResult(requestCode, resultCode, data);

    }


    /**
     * @param event 删除评论后刷新列表
     */
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

    /**
     * 设置CSS和JS
     */
    private void setCssJSWebView() {
        String htmlCode = AppUtils.getAssetsText(C.HTML_RULE_PATH);
        String uiModeCssUri = ThemeMode.isNightMode()
                ? C.NIGHT_CSS_URI : C.DAY_CSS_URI;
        String htmlBody = WebBiz.parseHandleHtml(TextUtils.isEmpty(mNewsDetail.getArticle().getContent()) ? "" : mNewsDetail.getArticle().getContent(),
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

    /**
     * 初始化webview
     */
    private void initWebView() {
        mWebView.setFocusable(false);

        // 隐藏到滚动条
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setScrollContainer(false);
        //注入支持的本地方法
        mWebJsInterface = WebJsInterface.getInstance(this);
        mWebView.addJavascriptInterface(mWebJsInterface, WebJsInterface.JS_NAME);

        // 夜间模式
        if (ThemeMode.isNightMode()) {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_202124_night));
        } else {
            mWebView.setBackgroundColor(UIUtils.getActivity().getResources().getColor(R.color.bc_ffffff));
        }

        settings = mWebView.getSettings();
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
                if (BrowserLinkActivity.this instanceof NewsDetailAdapter.CommonOptCallBack) {
                    ((NewsDetailAdapter.CommonOptCallBack) BrowserLinkActivity.this)
                            .onOptPageFinished();
                } else if (BrowserLinkActivity.this instanceof ActivityTopicAdapter.CommonOptCallBack) {
                    ((ActivityTopicAdapter.CommonOptCallBack) BrowserLinkActivity.this)
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
                BrowserLinkActivity.this
                        .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                container = new WebFullScreenContainer(BrowserLinkActivity.this);
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                decor.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                videoView = view;
                customViewCallback = callback;
            }

            @Override
            public void onHideCustomView() {
                if (videoView == null) {
                    return;
                }
                BrowserLinkActivity.this
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

    /**
     * 获取根布局
     *
     * @return
     */
    protected FrameLayout getDecorView() {
        return (FrameLayout) getWindow().getDecorView();
    }


    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * @param isUp 控制底部floorBar
     */
    @Override
    public void onTouchSlop(boolean isUp) {
        int translationY = !isUp ? 0 : mFloorBar.getHeight() + getFloorBarMarginBottom();
        mFloorBar.animate().setInterpolator(mInterpolator)
                .setDuration(200)
                .translationY(translationY);
    }

    /**
     * @return 获取底部栏间距
     */
    private int getFloorBarMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = mFloorBar.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }


    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(mNewsDetail.getArticle().getColumn_id()))).commit();
    }

}
