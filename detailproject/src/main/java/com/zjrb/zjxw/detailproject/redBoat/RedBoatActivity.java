package com.zjrb.zjxw.detailproject.redBoat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.task.RedBoatTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class RedBoatActivity extends BaseActivity implements View.OnClickListener, RedBoatAdapter.CommonOptCallBack {

    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ry_container)
    RelativeLayout mContainer;

    //稿件ID
    public String mArticleId;
    private String mFromChannel;

    private DefaultTopBarHolder1 topHolder;
    private Analytics.AnalyticsBuilder builder;
    private DraftDetailBean mNewsDetail;
    private RedBoatAdapter mAdapter;
    private Analytics mAnalytics;
    private float mScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
        getIntentData(getIntent());
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        return topHolder.getView();
    }

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
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new RedBoatTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                Map map = new HashMap();
                map.put("relatedColumn", draftDetailBean.getArticle().getColumn_id());
                map.put("subject", "");
                builder = new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                        .setEvenName("页面停留时长/阅读深度")
                        .setObjectID(draftDetailBean.getArticle().getGuid() + "")
                        .setObjectName(draftDetailBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(draftDetailBean.getArticle().getChannel_id())
                        .setClassifyName(draftDetailBean.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setSelfObjectID(draftDetailBean.getArticle().getId() + "");

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    @OnClick({R2.id.iv_top_bar_back, R2.id.iv_top_share})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_top_bar_back) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                new Analytics.AnalyticsBuilder(getContext(), "800001", "800001")
                        .setEvenName("点击返回")
                        .setObjectID(mNewsDetail.getArticle().getGuid() + "")
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
            finish();
        } else if (v.getId() == R.id.iv_top_share) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null && !TextUtils.isEmpty(mNewsDetail.getArticle().getUrl())) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mNewsDetail.getArticle().getGuid() + "")
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
                //分享操作
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setArticleId(mNewsDetail.getArticle().getId() + "")
                        .setImgUri(mNewsDetail.getArticle().getFirstPic())
                        .setAnalyticsBean(bean)
                        .setTextContent(mNewsDetail.getArticle().getSummary())
                        .setTitle(mNewsDetail.getArticle().getDoc_title())
                        .setTargetUrl(mNewsDetail.getArticle().getUrl()));
                //点击分享操作
                new Analytics.AnalyticsBuilder(getContext(), "800018", "800018")
                        .setEvenName("点击分享")
                        .setObjectID(mNewsDetail.getArticle().getGuid() + "")
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }

    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
    }

    @Override
    public void onReadingScaleChange(float scale) {
        mScale = scale;
    }
}
