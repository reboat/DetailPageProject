package com.zjrb.zjxw.detailproject.ui.link;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.commonwebview.webview.CommonWebView;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.CommonDraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.apibean.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialogLink;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.LinkControl;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.DefaultTopBarHolder4;
import cn.daily.news.biz.core.utils.RouteManager;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import cn.daily.news.biz.core.web.WebViewImpl;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 普通链接稿 - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class BrowserLinkActivity extends DailyActivity {

    @BindView(R2.id.web_view)
    CommonWebView mWebView;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;

    @BindView(R2.id.ry_container)
    FitWindowsRelativeLayout mContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.iv_close)
    ImageView mClose;

    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.fl_comment)
    RelativeLayout mFyContainer;
    @BindView(R2.id.ly_comment_num)
    RelativeLayout ly_comment_num;
    @BindView(R2.id.menu_setting_relpace)
    ImageView ivSettingReplace;
    @BindView(R2.id.menu_setting)
    ImageView ivSetting;
    @BindView(R2.id.menu_prised_relpace)
    ImageView ivPrisedRelpace;

    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    private String mFromChannel;

    private WebViewImpl webImpl;
    private JsMultiInterfaceImp jsInterfaceImp;
    //是否是链接稿
    private boolean isBrowserLink = false;
    //不识别scheme地址
    private String browserUri = "";
    //链接稿url堆栈管理
    private Stack<WebStack> mWebStacks = new Stack<>();
    private WebStack mWebStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_browser);
        ButterKnife.bind(this);
        mFloorBar.setVisibility(View.GONE);
        getIntentData(getIntent());
        initArgs(savedInstanceState);
        initWebview(mWebStack.urlLink);
//        addWebStack(mWebStack);
        loadData();
    }

    //初始化webview相关设置
    private void initWebview(String linkUrl) {
        webImpl = new WebViewImpl();
        webImpl.setWebViewJsObject(C.JS_OBJ_NAME);
        webImpl.setLinkUrl(linkUrl);
        jsInterfaceImp = new JsMultiInterfaceImp(mWebView, webImpl.getWebViewJsObject(), getContext());
        webImpl.setJsObject(jsInterfaceImp);
        mWebView.setHelper(webImpl);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mFloorBar.setVisibility(View.GONE);

        //复用压栈
        WebStack webStack = new WebStack();
        webStack.urlLink = intent.getStringExtra(IKey.DATA);
        Uri data = intent.getData();
        if (data != null) {
            webStack.urlLink = data.toString();
        }
        if (!TextUtils.isEmpty(webStack.urlLink)) {
            mWebStack = webStack;
            addWebStack(mWebStack);
        }
        getIntentData(intent);
        loadData();
    }

    //初始化堆栈
    private void initArgs(Bundle savedInstanceState) {
        mWebStack = new WebStack();
        if (savedInstanceState != null) {
            mWebStack.urlLink = savedInstanceState.getString(IKey.DATA);
        }
        if (TextUtils.isEmpty(mWebStack.urlLink)) {
            Intent intent = getIntent();
            if (intent != null) {
                mWebStack.urlLink = intent.getStringExtra(IKey.DATA);
                //路由取值
                if (TextUtils.isEmpty(mWebStack.urlLink)) {
                    Uri data = getIntent().getData();
                    if (data != null) {
                        mWebStack.urlLink = data.toString();
                    }
                }
            }
        }
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                if (data.getQueryParameter(IKey.ID) != null) {
                    mArticleId = data.getQueryParameter(IKey.ID);
                }
                if (data.getQueryParameter(IKey.FROM_CHANNEL) != null) {
                    mFromChannel = data.getQueryParameter(IKey.FROM_CHANNEL);
                }
                //判断是否为链接稿
                if (data.getPath().equals("/link.html")) {
                    isBrowserLink = true;
                    browserUri = data.toString();
                }
            }

        }
    }

    /**
     * 顶部标题
     */
    private DefaultTopBarHolder4 topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = BIZTopBarFactory.createDefault4(view, this);
        return topBarHolder.getView();
    }

    Analytics.AnalyticsBuilder builder;

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (isBrowserLink) {
            if (mArticleId == null || mArticleId.isEmpty()) return;
            new DraftDetailTask(new APICallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean draftDetailBean) {
                    if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                    mNewsDetail = draftDetailBean;
                    builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                    if(mNewsDetail.getArticle().getDoc_type() == 3){
                        mWebStack.urlLink = mNewsDetail.getArticle().getWeb_link();
                    }else{
                        mWebStack.urlLink = mNewsDetail.getArticle().getUrl();
                    }
                    fillData(mNewsDetail);
                    YiDunToken.synYiDunToken(mArticleId);
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(String errMsg, int errCode) {
                    //撤稿
                    if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                        showEmptyNewsDetail();
                    } else {
                        ZBToast.showShort(BrowserLinkActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
        } else {
            //非原生内部稿件
            new CommonDraftDetailTask(new APICallBack<DraftDetailBean>() {
                @Override
                public void onSuccess(DraftDetailBean draftDetailBean) {
                    if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                    mNewsDetail = draftDetailBean;
                    builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                    if(mNewsDetail.getArticle().getDoc_type() == 3){
                        mWebStack.urlLink = mNewsDetail.getArticle().getWeb_link();
                    }else{
                        mWebStack.urlLink = mNewsDetail.getArticle().getUrl();
                    }
                    fillData(mNewsDetail);
                    YiDunToken.synYiDunToken(mArticleId);
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errMsg, int errCode) {
                    //撤稿
                    if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                        showEmptyNewsDetail();
                    } else {
                        ZBToast.showShort(BrowserLinkActivity.this, errMsg);
                    }
                }
            }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(browserUri);
        }

    }

    /**
     * 顶部导航条在数据加载结束后再显示
     *
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        mView.setVisibility(View.GONE);
        // 记录阅读记录
        if (data != null && data.getArticle() != null) {
            DraftDetailBean.ArticleBean article = data.getArticle();
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }
        if (topBarHolder != null) {
            topBarHolder.setViewVisible(topBarHolder.getSettingView(), View.VISIBLE);
        }
        initViewState(data);
        mWebView.loadUrl(mWebStack.urlLink);
    }


    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //是否允许点赞
        if (data.getArticle().isLike_enabled()) {
            topBarHolder.getIvPrised().setVisibility(View.VISIBLE);
            topBarHolder.getIvPrised().setSelected(data.getArticle().isLiked());
        } else {
            topBarHolder.getIvPrised().setVisibility(View.GONE);
        }

        //禁止评论
        if (data.getArticle().getComment_level() == 0) {
            topBarHolder.getIvComment().setVisibility(View.GONE);
            topBarHolder.getTvCommentsNum().setVisibility(View.GONE);
        } else {
            topBarHolder.getIvComment().setVisibility(View.VISIBLE);
            //大致评论数量
            if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                topBarHolder.getTvCommentsNum().setVisibility(View.VISIBLE);
                topBarHolder.getTvCommentsNum().setText(data.getArticle().getComment_count_general());
            } else {
                topBarHolder.getTvCommentsNum().setVisibility(View.INVISIBLE);
            }
        }
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_back, R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_prised_relpace, R2.id.menu_setting, R2.id.iv_close, R2.id.menu_setting_relpace})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        int id = view.getId();
        if (R.id.iv_back == id) {
            //堆栈为空则直接返回
            if (mWebStacks.isEmpty()) {
                finish();
                closeActivity();
            } else {
                //返回删除以后栈顶对象
                bindWebStack(mWebStacks.pop());
            }
        } else if (R.id.iv_close == id) {//点击X
            finish();
            closeActivity();
        } else if (view.getId() == R.id.menu_comment) { //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickInCommentList(mNewsDetail);
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised || view.getId() == R.id.menu_prised_relpace) {
            DataAnalyticsUtils.get().ClickPriseIcon(mNewsDetail);
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting || view.getId() == R.id.menu_setting_relpace) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.C01)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false).setNewsCard(true)
                        .setCardUrl(mNewsDetail.getArticle().getCard_url())
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(mWebStack.urlLink)
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章");

                MoreDialogLink.newInstance(mNewsDetail).setShareBean(shareBean).show(getSupportFragmentManager(), "MoreDialog");
            }
        }
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
            ZBToast.showShort(this, getString(R.string.module_detail_you_have_liked));
            return;
        }
        new DraftPraiseTask(new LoadingCallBack<Void>() {

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                if (errCode == 50013) {
                    mNewsDetail.getArticle().setLiked(true);
                    mMenuPrised.setSelected(true);
                    ZBToast.showShort(getBaseContext(), "已点赞成功");
                } else {
                    ZBToast.showShort(getBaseContext(), errMsg);
                }
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                ZBToast.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true, mNewsDetail.getArticle().getUrl());
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        if (topBarHolder != null) {
            topBarHolder.setViewVisible(topBarHolder.getSettingView(), View.GONE);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        //新华智云
//        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
//            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
//                    .setTargetID(mNewsDetail.getArticle().getId() + "")
//                    .setUrl(mNewsDetail.getArticle().getUrl())
//                    .build()
//                    .send();
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        //新华智云
//        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
//            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
//                    .setTargetID(mNewsDetail.getArticle().getId() + "")
//                    .setUrl(mNewsDetail.getArticle().getUrl())
//                    .build()
//                    .send();
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeActivity();
    }

    //关闭页面
    private void closeActivity() {
        if (builder != null) {
            Analytics mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        //清空webview依赖
        if (mWebView.getParent() != null && mWebView.getParent() instanceof ViewGroup) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
        }
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    //异常销毁保存数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mWebStack.urlLink)) {
            outState.putString(IKey.DATA, mWebStack.urlLink);
        }
    }

    //添加堆栈
    public void addWebStack(WebStack webStack) {
        if (webStack != null) {
            if (mWebStack != null) { // 上一个加入栈
                mWebStacks.push(mWebStack);
            }
            bindWebStack(webStack);
        }
    }

    //渲染链接
    private void bindWebStack(WebStack webStack) {
        mWebStack = webStack;
        //堆栈中链接信息大于1个时才显示
        if (mWebStacks.size() > 1) {
            mClose.setVisibility(View.VISIBLE);
        } else {
            mClose.setVisibility(View.INVISIBLE);
        }
        mWebView.clearHistory();
        mWebView.loadUrl(webStack.urlLink);
    }

    @Override
    public void onBackPressed() {
        if (mWebStacks.isEmpty()) {
            finish();
            closeActivity();
        } else {
            //返回删除以后栈顶对象
            bindWebStack(mWebStacks.pop());
        }
    }

    private static class WebStack {
        private String urlLink;
    }
}