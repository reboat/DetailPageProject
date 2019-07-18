package com.zjrb.zjxw.detailproject.ui.redBoat;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.utils.StringUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.PromoteResponse;
import com.zjrb.zjxw.detailproject.apibean.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.apibean.task.PromoteTask;
import com.zjrb.zjxw.detailproject.apibean.task.RedBoatTask;
import com.zjrb.zjxw.detailproject.callback.DetailInterface;
import com.zjrb.zjxw.detailproject.ui.boardcast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.ui.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.ui.topbar.RedBoatTopBarHolder;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialogLink;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.ArrayList;
import java.util.List;

import bean.ZBJTOpenAppShareMenuBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.network.compatible.APICallBack;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.dialog.RankTipDialog;
import cn.daily.news.biz.core.ui.toast.ZBToast;
import cn.daily.news.biz.core.web.AndroidBug5497Workaround;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;
import port.JsInterfaceCallBack;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 类描述：红船号详情页
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */

public class RedBoatActivity extends DailyActivity implements RedBoatAdapter.CommonOptCallBack, DetailInterface.SubscribeSyncInterFace {

    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ry_container)
    RelativeLayout mContainer;

    //稿件ID
    public String mArticleId;
    private String mFromChannel;

    private RedBoatTopBarHolder topHolder;
    private Analytics.AnalyticsBuilder builder;
    private DraftDetailBean mNewsDetail;
    private RedBoatAdapter mAdapter;
    private Analytics mAnalytics;
    private float mScale;
    private SubscribeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
        AndroidBug5497Workaround.assistActivity(this);
        mReceiver = new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        getIntentData(getIntent());
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = new RedBoatTopBarHolder(view, this);
        return topHolder.getView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
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
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAdapter != null) {
//            mAdapter.onWebViewResume();
//        }
        //新华智云
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.comeIn)
                    .setTargetID(mNewsDetail.getArticle().getId() + "")
                    .setUrl(mNewsDetail.getArticle().getUrl())
                    .build()
                    .send();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mAdapter != null) {
//            mAdapter.onWebViewPause();
//        }
        //新华智云
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            new Analytics.AnalyticsBuilder(getContext(), Analytics.AnalyticsBuilder.SHWEventType.leave)
                    .setTargetID(mNewsDetail.getArticle().getId() + "")
                    .setUrl(mNewsDetail.getArticle().getUrl())
                    .build()
                    .send();
        }

    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new RedBoatTask(new LoadingCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;

                builder = DataAnalyticsUtils.get().pageStayTime(draftDetailBean);
                mAnalytics = builder.build();
                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                fillData(mNewsDetail);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                    topHolder.getIvmore().setVisibility(View.INVISIBLE);
                    showEmptyNewsDetail();
                } else {
                    ZBToast.showShort(RedBoatActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        DraftDetailBean.ArticleBean article = data.getArticle();
        if (article != null) {
            //经liya和新科确认，暂不管新增guid（long）字段存储数据库
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }
        topHolder.setViewVisible(topHolder.getIvmore(), View.VISIBLE);

        //中间栏目布局处理
        if (!TextUtils.isEmpty(article.getColumn_name())) {
            //栏目名称
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.VISIBLE);
            topHolder.setViewVisible(topHolder.getSubscribe(), View.VISIBLE);
            topHolder.getTitleView().setText(article.getColumn_name());
            //栏目头像
            GlideApp.with(topHolder.getIvIcon()).load(article.getColumn_logo()).placeholder(R.mipmap.ic_top_bar_redboat_icon)
                    .error(R.mipmap.ic_top_bar_redboat_icon).centerCrop().into(topHolder.getIvIcon());
            //订阅状态 采用select
            if (article.isColumn_subscribed() && BizUtils.isRankEnable()) {
                topHolder.getSubscribe().setVisibility(View.INVISIBLE);
                topHolder.getSubscribe().setSelected(true);
                topHolder.rankActionView.setVisibility(View.VISIBLE);
                if (article.rank_hited) {
                    topHolder.rankActionView.setText("拉票");
                } else {
                    topHolder.rankActionView.setText("打榜");
                }
            } else {
                topHolder.getSubscribe().setSelected(false);
            }
        } else {
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.INVISIBLE);
            topHolder.setViewVisible(topHolder.getSubscribe(), View.INVISIBLE);
        }
        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.color._dddddd_343434));
        mAdapter = new RedBoatAdapter(datas);
        mAdapter.setEmptyView(new EmptyPageHolder(mRvContent,
                EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")).itemView);
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.INVISIBLE);
        topHolder.setViewVisible(topHolder.getSubscribe(), View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @OnClick(R2.id.rank_action_view)
    public void onRankActionClick(final TextView view) {
        if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
            final DraftDetailBean.ArticleBean bean = mNewsDetail.getArticle();
            if (bean.rank_hited) {

                String shareName = String.format("我正在为起航号“%s”拉赞助力，快来和我一起为它加油！", bean.getColumn_name());
                String shareDes = String.format("点击查看起航号“%s”榜上排名", bean.getColumn_name());
                String shareUrl = "https://zj.zjol.com.cn/";

                OutSizeAnalyticsBean analyticsBean = OutSizeAnalyticsBean
                        .getInstance()
                        .setPageType("新闻详情页")
                        .setColumn_id(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .setColumn_name(mNewsDetail.getArticle().getColumn_name())
                        .setObjectType(ObjectType.C90);

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setAnalyticsBean(analyticsBean)
                        .setCardPageType("卡片详情")
                        .setTitle(shareName)
                        .setTextContent(shareDes).setTargetUrl(shareUrl)
                        .setShareType("栏目")
                        .setNewsCard(false)
                        .setCardUrl(bean.rank_card_url);
                if (!StringUtils.isEmpty(bean.getColumn_logo())) {
                    shareBean.setImgUri(bean.getColumn_logo());
                } else {
                    shareBean.setPicId(R.mipmap.ic_launcher);
                }
                shareBean.setPicId(R.mipmap.ic_launcher);
                UmengShareUtils.getInstance().startShare(shareBean);

                new Analytics.AnalyticsBuilder(getActivity(), "A0062", "", false)
                        .name("点击拉票")
                        .pageType("新闻详情页")
                        .columnID(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .columnName(mNewsDetail.getArticle().getColumn_name())
                        .seObjectType(ObjectType.C90)
                        .build()
                        .send();


            } else {
                new PromoteTask(new APICallBack<PromoteResponse>() {
                    @Override
                    public void onError(String errMsg, int errCode) {
                        super.onError(errMsg, errCode);
                        if (errCode == 53003) {
                            ZBToast.showShort(RedBoatActivity.this, errMsg);
                        } else {
                            ZBToast.showShort(RedBoatActivity.this, "打榜失败");
                        }
                    }

                    @Override
                    public void onSuccess(final PromoteResponse data) {
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                ZBToast.showShort(getContext(), data.toast);
                                bean.rank_hited = true;
                                view.setText("拉票");
                            }
                        });
                    }
                }).exe(mNewsDetail.getArticle().getColumn_id());
            }
        }
    }

    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_more, R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title, R2.id.iv_top_subscribe_icon})
    public void onClick(final View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_top_bar_back) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickBack(mNewsDetail);
            }
            finish();
            //红船号点击更多
        } else if (v.getId() == R.id.iv_top_more) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                DataAnalyticsUtils.get().ClickMoreIcon(mNewsDetail);
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.C01)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id() + "")
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setColumn_id(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                        .setColumn_name(mNewsDetail.getArticle().getColumn_name())
                        .setUrl(mNewsDetail.getArticle().getUrl())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mNewsDetail.getArticle().getId() + "");

                //更新预分享
                UmengShareBean mJsShareBean = SPHelper.get().getObject(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
                ZBJTOpenAppShareMenuBean menuBean = null;
                JsInterfaceCallBack jsCallBack = null;
                boolean isUpdateShare = false;
                if (mJsShareBean != null && mAdapter != null && mAdapter.getWebViewHolder() != null && mAdapter.getWebViewHolder().getJsInterfaceImp() != null) {
                    menuBean = mJsShareBean.getBean();
                    isUpdateShare = true;
                    jsCallBack = mAdapter.getWebViewHolder().getJsInterfaceImp().getmCallback();
                }

                UmengShareBean shareBean = UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setBean(menuBean)
                        .setShareUpdate(isUpdateShare)
                        .setJsCallback(jsCallBack)
                        .setCardUrl(mNewsDetail.getArticle().getCard_url())
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(mNewsDetail.getArticle().getUrl())
                        .setAnalyticsBean(bean).setEventName("NewsShare")
                        .setShareType("文章");

                MoreDialogLink.newInstance(mNewsDetail).setShareBean(shareBean).show(getSupportFragmentManager(), "MoreDialog");
            }
            //点击订阅/取消订阅
        } else if (v.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            if (topHolder.getSubscribe().isSelected()) {
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号取消订阅", "A0114", "SubColumn", "取消订阅");
                new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        ZBToast.showShort(getApplicationContext(), "取消订阅成功");
                        SyncSubscribeColumn(false, mNewsDetail.getArticle().getColumn_id());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        ZBToast.showShort(RedBoatActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "订阅号订阅", "A0014", "SubColumn", "订阅");
                if (!topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new LoadingCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {

                            if (BizUtils.isRankEnable()) {
                                if (!mNewsDetail.getArticle().rank_hited) {
                                    RankTipDialog.Builder builder = new RankTipDialog.Builder()
                                            .setLeftText("取消")
                                            .setRightText("打榜")
                                            .setMessage("订阅成功，来为它打榜，助它荣登榜首吧！")
                                            .setOnLeftClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    new Analytics.AnalyticsBuilder(getActivity(), "200037", "", false)
                                                            .name("点击取消打榜")
                                                            .pageType("弹框")
                                                            .build()
                                                            .send();
                                                }
                                            })
                                            .setOnRightClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    sendActionRequest(mNewsDetail.getArticle().getColumn_id());
                                                    new Analytics.AnalyticsBuilder(getActivity(), "200038", "", false)
                                                            .name("点击打榜")
                                                            .pageType("弹框")
                                                            .build()
                                                            .send();
                                                }
                                            });
                                    RankTipDialog dialog = new RankTipDialog(RedBoatActivity.this);
                                    dialog.setBuilder(builder);
                                    dialog.show();

                                    new Analytics.AnalyticsBuilder(v.getContext(), "A0061", "", false)
                                            .name("点击打榜")
                                            .pageType("新闻详情页")
                                            .columnID(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                                            .columnName(mNewsDetail.getArticle().getColumn_name())
                                            .seObjectType(ObjectType.C90)
                                            .build()
                                            .send();
                                } else {
                                    ZBToast.showShort(getApplicationContext(), "订阅成功");
                                    topHolder.rankActionView.setText("拉票");
                                    new Analytics.AnalyticsBuilder(v.getContext(), "A0062", "", false)
                                            .name("点击拉票")
                                            .pageType("新闻详情页")
                                            .columnID(String.valueOf(mNewsDetail.getArticle().getColumn_id()))
                                            .columnName(mNewsDetail.getArticle().getColumn_name())
                                            .seObjectType(ObjectType.C90)
                                            .build()
                                            .send();
                                }

                                topHolder.rankActionView.setVisibility(View.VISIBLE);
                                topHolder.getSubscribe().setVisibility(View.INVISIBLE);
                            }

                            topHolder.getSubscribe().setSelected(true);
                            SyncSubscribeColumn(true, mNewsDetail.getArticle().getColumn_id());
                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            ZBToast.showShort(RedBoatActivity.this, "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (v.getId() == R.id.tv_top_bar_title || v.getId() == R.id.iv_top_subscribe_icon) {
            DataAnalyticsUtils.get().SubscribeAnalytics(mNewsDetail, "点击进入栏目详情页", "800031", "ToDetailColumn", "");
            if (!TextUtils.isEmpty(mNewsDetail.getArticle().getColumn_url())) {
                Nav.with(UIUtils.getContext()).to(mNewsDetail.getArticle().getColumn_url());
            }
        }
    }

    private void sendActionRequest(final int column_id) {
        new PromoteTask(new APICallBack<PromoteResponse>() {
            @Override
            public void onError(String errMsg, int errCode) {
                super.onError(errMsg, errCode);
                if (errCode == 53003) {
                    ZBToast.showShort(RedBoatActivity.this, errMsg);
                }
            }

            @Override
            public void onSuccess(final PromoteResponse data) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ZBToast.showShort(RedBoatActivity.this, data.toast);
                        mNewsDetail.getArticle().rank_hited = true;
                        topHolder.rankActionView.setText("拉票");
                        BizUtils.syncRankState(RedBoatActivity.this, column_id, data.delta_count);
                    }
                });
            }
        }).exe(column_id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.pagePercent(mScale + "");
            }
            builder.readPercent(mScale + "");
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }

        }
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
    }

    @Override
    public void onReadingScaleChange(float scale) {
        mScale = scale;
    }

    /**
     * 同步红船号订阅栏目
     */
    private void SyncSubscribeColumn(boolean isSubscribe, int columnid) {
        Intent intent = new Intent("subscribe_success");
        intent.putExtra("subscribe", isSubscribe);
        intent.putExtra("id", (long) columnid);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void subscribeSync(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && "subscribe_success".equals(intent.getAction())) {
            long id = intent.getLongExtra("id", 0);
            boolean subscribe = intent.getBooleanExtra("subscribe", false);
            //确定是该栏目需要同步
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && id == mNewsDetail.getArticle().getColumn_id()) {
                topHolder.getSubscribe().setSelected(subscribe);
            }

        }
    }
}
