package com.zjrb.zjxw.detailproject.link;

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
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder4;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.fragment.ScanerBottomFragment;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.ui.widget.web.ZBJsInterface;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.core.utils.webjs.LongClickCallBack;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.interFace.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialogLink;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 链接稿 - Activity
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/08  上午10:14
 */
public class BrowserLinkActivity extends BaseActivity implements View.OnClickListener, LongClickCallBack, DetailWMHelperInterFace.LinkDetailWM {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.ry_container)
    FitWindowsFrameLayout mContainer;
    @BindView(R2.id.v_container)
    FrameLayout mView;

    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    /**
     * 网页地址
     */
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_activity_browser_link);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        mWebView.setLongClickCallBack(this);
        initWebview();
        loadData();
    }

    private String mFromChannel;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
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
            }

        }
    }

    /**
     * 重新拦截webview点击事件
     */
    private void initWebview() {
        mWebView.setWebViewClient(new WebViewClient() {
            private boolean isRedirect; // true : 重定向

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url)) {
                    Uri uri = Uri.parse(url);
                    if (uri != null && !TextUtils.equals(uri.getScheme(), "http") && !TextUtils.equals(uri.getScheme(), "https")) {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
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
                    if (isRedirect) { // 重定向
                        view.loadUrl(url);
                    } else { // 点击跳转
                        if (ClickTracker.isDoubleClick()) return true;
                        if (Nav.with(getContext()).to(url)) {
                            return true;
                        }
                    }

                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isRedirect = false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isRedirect = true;
            }

        });
    }

    /**
     * 顶部标题
     */
    private DefaultTopBarHolder4 topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = TopBarFactory.createDefault4(view, this);
        topBarHolder.setViewVisible(topBarHolder.getTitleView(), View.GONE);
        return topBarHolder.getView();
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                mNewsDetail = draftDetailBean;
                //可能被重定向了
                if (mNewsDetail.getArticle().getDoc_type() == 3) {
                    url = mNewsDetail.getArticle().getWeb_link();
                } else {
                    url = mNewsDetail.getArticle().getUrl();
                }
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(BrowserLinkActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
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


        //显示标题展示WebView内容等
        mWebView.hasVideoUrl(false);
        mWebView.loadUrl(url);
        if (topBarHolder != null) {
            topBarHolder.setViewVisible(topBarHolder.getSettingView(), View.VISIBLE);
        }
        //是否点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mMenuComment.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                mTvCommentsNum.setVisibility(View.VISIBLE);
                mTvCommentsNum.setText(data.getArticle().getComment_count_general());
            }
        }
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_back, R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        int id = view.getId();
        if (R.id.iv_back == id) {
            onBackPressed();
            //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                ClickInCommentList();
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            ClickPriseIcon();

            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                ClickMoreIcon();
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(url)
                        .setAnalyticsBean(bean);

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
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

            @Override
            public void onError(String errMsg, int errCode) {
                if (errCode == 50013) {
                    mNewsDetail.getArticle().setLiked(true);
                    mMenuPrised.setSelected(true);
                    T.showShort(getBaseContext(), "已点赞成功");
                } else {
                    T.showShort(getBaseContext(), errMsg);
                }
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清除线程池
        if (mWebView != null) {
            mWebView.stopThreadPool();
        }
        mWebView.destroy();
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
    }

    /**
     * 长按识别二维码
     *
     * @param imgUrl
     * @param isScanerImg
     */
    @Override
    public void onLongClickCallBack(String imgUrl, boolean isScanerImg) {
        scanerAnalytics(imgUrl, isScanerImg);
        ScanerBottomFragment.newInstance().showDialog(this).isScanerImg(isScanerImg).setActivity(this).setImgUrl(imgUrl);
    }


    /**
     * 二维码识别相关埋点
     */
    private void scanerAnalytics(String imgUrl, boolean isScanerImg) {
        if (mNewsDetail != null && isScanerImg) {
            new Analytics.AnalyticsBuilder(getContext(), "800024", "800024")
                    .setEvenName("识别二维码图片")
                    .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                    .setObjectName(mNewsDetail.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                    .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("mediaURL", imgUrl)
                            .toString())
                    .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickBack() {
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "800001", "800001")
                    .setEvenName("点击返回")
                    .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                    .setObjectName(mNewsDetail.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                    .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickInCommentList() {
        new Analytics.AnalyticsBuilder(getActivity(), "800004", "800004")
                .setEvenName("点击评论，进入评论列表")
                .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                .setObjectName(mNewsDetail.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                .build()
                .send();
    }

    @Override
    public void ClickPriseIcon() {
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getActivity(), "A0021", "A0021")
                    .setEvenName("点击点赞")
                    .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                    .setObjectName(mNewsDetail.getArticle().getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                    .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickMoreIcon() {
        new Analytics.AnalyticsBuilder(getActivity(), "800005", "800005")
                .setEvenName("点击更多")
                .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                .setObjectName(mNewsDetail.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                .build()
                .send();
    }

}