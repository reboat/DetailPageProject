package com.zjrb.zjxw.detailproject.link;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.daily.news.location.DataLocation;
import com.daily.news.location.LocationManager;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LocationCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.CommonTopBarHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.ZBWebView;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

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
public class LiveLinkActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.web_view)
    ZBWebView mWebView;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
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
        setContentView(R.layout.module_detail_activity_browser);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        loadData();
    }


    private String mFromChannel;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
        if (topBarHolder != null) {
            topBarHolder.getShareView().setVisibility(View.VISIBLE);
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
            }


        }
    }

    /**
     * 顶部标题
     */
    private CommonTopBarHolder topBarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topBarHolder = TopBarFactory.createCommonTopBar(view, this);
        topBarHolder.setViewVisible(topBarHolder.getShareView(), View.VISIBLE);
        return topBarHolder.getView();
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                mNewsDetail = draftDetailBean;
                if (mNewsDetail.getArticle().getDoc_type() == 8) {
                    url = mNewsDetail.getArticle().getLive_url();
                }
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(LiveLinkActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        mFloorBar.setVisibility(View.VISIBLE);
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
        //中间栏目布局处理
        if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_name())) {
            //栏目名称
            topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.VISIBLE);
            topBarHolder.getTitleView().setText(mNewsDetail.getArticle().getColumn_name());
            //栏目头像
            if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_logo())) {
                GlideApp.with(topBarHolder.getIvIcon()).load(mNewsDetail.getArticle().getColumn_logo()).centerCrop().into(topBarHolder.getIvIcon());
            }
            //订阅状态 采用select
            if (mNewsDetail.getArticle().isColumn_subscribed()) {
                topBarHolder.getSubscribe().setText("已订阅");
                topBarHolder.getSubscribe().setSelected(true);
            } else {
                topBarHolder.getSubscribe().setText("+订阅");
                topBarHolder.getSubscribe().setSelected(false);
            }
        } else {
            topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.GONE);
        }

        //显示标题展示WebView内容等
        mWebView.hasVideoUrl(false);
        mWebView.loadUrl(url);

        //是否点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mFyContainer.setVisibility(View.VISIBLE);
            mMenuComment.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
                mTvCommentsNum.setVisibility(View.VISIBLE);
                mTvCommentsNum.setText(data.getArticle().getComment_count_general());
            }
        }
    }


    private Bundle bundle;

    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_share, R2.id.menu_comment,
            R2.id.menu_prised, R2.id.menu_setting, R2.id.tv_comment,
            R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        int id = view.getId();
        if (R.id.iv_top_bar_back == id) {
            onBackPressed();
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
            //分享(无法获取链接稿第一张图，设置为浙江新闻LOGO)
        } else if (id == R.id.iv_top_share) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(url)) {
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

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(url)
                        .setAnalyticsBean(bean)
                );

                //点击分享操作
                new Analytics.AnalyticsBuilder(getActivity(), "800018", "800018")
                        .setEvenName("点击分享")
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
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
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
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
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

            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
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
            MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                //进入评论编辑页面(不针对某条评论)
                new Analytics.AnalyticsBuilder(getActivity(), "800002", "800002")
                        .setEvenName("点击评论输入框")
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

                //评论发表成功
                Analytics analytics = new Analytics.AnalyticsBuilder(getActivity(), "A0023", "A0023")
                        .setEvenName("发表评论，且发送成功")
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
                        .build();
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).setWMData(analytics).setLocationCallBack(new PraiseLocationCallBack()).show(getSupportFragmentManager(), "CommentWindowDialog");
            }
        } else if (view.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topBarHolder.getSubscribe().isSelected()) {
                subscribeAnalytics("点击取消订阅栏目","A0014");
                new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topBarHolder.getSubscribe().setSelected(false);
                        topBarHolder.getSubscribe().setText("+订阅");
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(LiveLinkActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                subscribeAnalytics("点击订阅栏目","A0014");
                if (!topBarHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topBarHolder.getSubscribe().setSelected(true);
                            topBarHolder.getSubscribe().setText("已订阅");
                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            T.showShortNow(LiveLinkActivity.this, "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (view.getId() == R.id.tv_top_bar_title) {
            subscribeAnalytics("点击进入栏目详情页","800031");
            Bundle bundle = new Bundle();
            bundle.putString(IKey.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()));
            Nav.with(UIUtils.getContext()).setExtras(bundle)
                    .toPath("/subscription/detail");
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
        mFloorBar.setVisibility(View.GONE);
        mView.setVisibility(View.VISIBLE);
        topBarHolder.getShareView().setVisibility(View.GONE);
        topBarHolder.setViewVisible(topBarHolder.getFitRelativeLayout(), View.GONE);
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
        mWebView.destroy();
    }

    /**
     * 订阅相关埋点
     *
     * @param eventNme
     */
    private void subscribeAnalytics(String eventNme,String eventCode) {
        new Analytics.AnalyticsBuilder(getContext(), eventCode, eventCode)
                .setEvenName(eventNme)
                .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                .setObjectName(mNewsDetail.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "RelatedColumnType")
                        .toString())
                .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                .build()
                .send();
    }

    /**
     * 点击评论时,获取用户所在位置
     */
    static class PraiseLocationCallBack implements LocationCallBack {

        @Override
        public String onGetLocation() {
            if (LocationManager.getInstance().getLocation() != null) {
                DataLocation.Address address = LocationManager.getInstance().getLocation().getAddress();
                if (address != null) {
                    return address.getCountry() + "," + address.getProvince() + "," + address.getCity();
                } else {
                    return "" + "," + "" + "," + "";
                }
            } else {
                return "" + "," + "" + "," + "";
            }
        }
    }

}