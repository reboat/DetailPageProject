package com.zjrb.zjxw.detailproject.redBoat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.RedBoatTopBarHolder;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.widget.web.ZBJsInterface;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.broadCast.SubscribeReceiver;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.interFace.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.interFace.SubscribeSyncInterFace;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.RedBoatTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 类描述：红船号详情页
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/12 2007
 */

public class RedBoatActivity extends BaseActivity implements View.OnClickListener, RedBoatAdapter.CommonOptCallBack, DetailWMHelperInterFace.RedBoatWM, SubscribeSyncInterFace {

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
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
        mReceiver =  new SubscribeReceiver(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, new IntentFilter("subscribe_success"));
        getIntentData(getIntent());
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createRedBoatTopBar(view, this);
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
        if (mAdapter != null) {
            mAdapter.onWebViewResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onWebViewPause();
        }
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new RedBoatTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;

                builder = pageStayTime(draftDetailBean);
                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    topHolder.getShareView().setVisibility(View.GONE);
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(RedBoatActivity.this, errMsg);
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
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);

        //中间栏目布局处理
        if (!TextUtils.isEmpty(article.getColumn_name())) {
            //栏目名称
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.VISIBLE);
            topHolder.getTitleView().setText(article.getColumn_name());
            //栏目头像
            GlideApp.with(topHolder.getIvIcon()).load(article.getColumn_logo()).placeholder(R.mipmap.ic_top_bar_redboat_icon)
                    .error(R.mipmap.ic_top_bar_redboat_icon).centerCrop().into(topHolder.getIvIcon());
            //订阅状态 采用select
            if (article.isColumn_subscribed()) {
                topHolder.getSubscribe().setText("已订阅");
                topHolder.getSubscribe().setSelected(true);
            } else {
                topHolder.getSubscribe().setText("+订阅");
                topHolder.getSubscribe().setSelected(false);
            }
        } else {
            topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        }
        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.attr.dc_dddddd));
        mAdapter = new RedBoatAdapter(datas,
                !TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url()) ? true : false);
        mAdapter.setEmptyView(new EmptyPageHolder(mRvContent,
                EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")).itemView);
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        topHolder.setViewVisible(topHolder.getFitRelativeLayout(), View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_share, R2.id.tv_top_bar_subscribe_text, R2.id.tv_top_bar_title})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_top_bar_back) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                ClickBack(mNewsDetail);
            }
            finish();
            //红船号点击更多
        } else if (v.getId() == R.id.iv_top_share) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                MoreDialog.newInstance(mNewsDetail).setWebViewCallBack(mAdapter.getWebViewHolder(), mAdapter.getWebViewHolder()).show(getSupportFragmentManager(), "MoreDialog");
            }
            //点击订阅/取消订阅
        } else if (v.getId() == R.id.tv_top_bar_subscribe_text) {
            //已订阅状态->取消订阅
            //TODO 针对红船号详情页，需要做红船号订阅栏目的同步
            if (topHolder.getSubscribe().isSelected()) {
                SubscribeAnalytics("点击取消订阅栏目", "A0014");
                new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                    @Override
                    public void onSuccess(Void baseInnerData) {
                        topHolder.getSubscribe().setSelected(false);
                        topHolder.getSubscribe().setText("+订阅");
                        SyncSubscribeColumn(false, mNewsDetail.getArticle().getColumn_id());
                    }

                    @Override
                    public void onError(String errMsg, int errCode) {
                        T.showShortNow(RedBoatActivity.this, "取消订阅失败");
                    }

                }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), false);
            } else {//未订阅状态->订阅
                SubscribeAnalytics("点击订阅栏目", "A0014");
                if (!topHolder.getSubscribe().isSelected()) {
                    new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

                        @Override
                        public void onSuccess(Void baseInnerData) {
                            topHolder.getSubscribe().setSelected(true);
                            topHolder.getSubscribe().setText("已订阅");
                            SyncSubscribeColumn(true, mNewsDetail.getArticle().getColumn_id());
                        }

                        @Override
                        public void onError(String errMsg, int errCode) {
                            T.showShortNow(RedBoatActivity.this, "订阅失败");
                        }

                    }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);
                }

            }
            //进入栏目
        } else if (v.getId() == R.id.tv_top_bar_title) {
            SubscribeAnalytics("点击进入栏目详情页", "800031");
            Bundle bundle = new Bundle();
            bundle.putString(IKey.ID, String.valueOf(mNewsDetail.getArticle().getColumn_id()));
            Nav.with(UIUtils.getContext()).setExtras(bundle)
                    .toPath("/subscription/detail");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.setPercentage(mScale + "");
            }
            mAnalytics = builder.build();
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
            String subscriptionText = subscribe ? "已订阅" : "+订阅";
            //确定是该栏目需要同步
            if (id == mNewsDetail.getArticle().getColumn_id()) {
                topHolder.getSubscribe().setSelected(subscribe);
                topHolder.getSubscribe().setText(subscriptionText);
                if (subscribe) {
                    SubscribeAnalytics("点击订阅栏目", "A0014");
                } else {
                    SubscribeAnalytics("点击取消订阅栏目", "A0114");
                }
            }
        }
    }

    /**
     * 埋点相关
     *
     * @param bean
     * @return
     */
    @Override
    public Analytics.AnalyticsBuilder pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                .setEvenName("页面停留时长/阅读深度")
                .setObjectID(bean.getArticle().getGuid() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "");
    }

    @Override
    public void ClickBack(DraftDetailBean bean) {
        new Analytics.AnalyticsBuilder(getContext(), "800001", "800001")
                .setEvenName("点击返回")
                .setObjectID(bean.getArticle().getGuid() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .build()
                .send();

    }

    @Override
    public void SubscribeAnalytics(String eventNme, String eventCode) {
        new Analytics.AnalyticsBuilder(getContext(), eventCode, eventCode)
                .setEvenName(eventNme)
                .setObjectID(mNewsDetail.getArticle().getGuid() + "")
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

}
