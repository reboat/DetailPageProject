//package com.zjrb.zjxw.detailproject.addetai;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.aliya.view.fitsys.FitWindowsRecyclerView;
//import com.zjrb.core.common.base.BaseActivity;
//import com.zjrb.core.common.base.toolbar.TopBarFactory;
//import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
//import com.zjrb.core.db.SPHelper;
//import com.zjrb.core.ui.holder.EmptyPageHolder;
//import com.zjrb.core.ui.widget.web.ZBJsInterface;
//import com.zjrb.core.utils.click.ClickTracker;
//import com.zjrb.daily.ad.Ad;
//import com.zjrb.daily.ad.listener.AdDataListener;
//import com.zjrb.daily.ad.model.AdModel;
//import com.zjrb.daily.ad.model.AdType;
//import com.zjrb.zjxw.detailproject.R;
//import com.zjrb.zjxw.detailproject.R2;
//import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
//import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailSpaceDivider;
//import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.daily.news.analytics.Analytics;
//
///**
// * Created by wanglinjie.
// * create time:2019/1/3  上午9:27
// */
//public class AdDetailActivity extends BaseActivity implements NewsDetailAdapter.CommonOptCallBack, AdDataListener {
//    @BindView(R2.id.rv_content)
//    FitWindowsRecyclerView mRvContent;
//    @BindView(R2.id.ry_empty)
//    ConstraintLayout mRlEmpty;
//    @BindView(R2.id.iv_icon)
//    ImageView mIvEmptyIcon;
//    @BindView(R2.id.tv_content)
//    TextView mTvEmptyContent;
//
//    /**
//     * 广告软文id
//     */
//    private String mSlotId;
//
//    /**
//     * 详情页适配器
//     */
//    private NewsDetailAdapter mAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTranslucenceStatusBarBg();
//        setContentView(R.layout.module_detail_activity_ad_detail);
//        ButterKnife.bind(this);
//        init();
//    }
//
//    private void init() {
//        getIntentData(getIntent());
//        loadData();
//    }
//
//    /**
//     * 页面复用调用
//     *
//     * @param intent
//     */
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getIntentData(intent);
//        loadData();
//    }
//
//    /**
//     * @param intent 获取传递数据
//     */
//    private void getIntentData(Intent intent) {
//        if (intent != null) {
//            Uri data = intent.getData();
//            if (data != null) {
//                if (data.getQueryParameter("slotId") != null) {
//                    mSlotId = data.getQueryParameter("slotId");
//                }
//            }
//        }
//    }
//
//
//    private DefaultTopBarHolder1 topHolder;
//
//    @Override
//    protected View onCreateTopBar(ViewGroup view) {
//        topHolder = TopBarFactory.createDefault1(view, this);
//        return topHolder.getView();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }
//
//
//    Analytics.AnalyticsBuilder builder;
//
//    /**
//     * 请求详情页数据
//     */
//    private void loadData() {
//        if (!TextUtils.isEmpty(mSlotId)) {
//            mRlEmpty.setVisibility(View.GONE);
//            Ad.create(this)
//                    .setSlotId(mSlotId)
//                    .setSspId(1)//自定义系统id，一般不需要
//                    .setType(AdType.LOOP)
//                    .setPosition(1)//展示位置
//                    .setAdDataListener(this)
//                    .build();
//        } else {
//            mRlEmpty.setVisibility(View.VISIBLE);
//            mTvEmptyContent.setText("该文章不存在");
//
//        }
//    }
//
//    /**
//     * 填充数据
//     *
//     * @param mode
//     */
//    public void fillData(AdModel mode) {
//        mRlEmpty.setVisibility(View.GONE);
//        //设置标题
//        if (mode != null && !TextUtils.isEmpty(mode.getTitle())) {
//            topHolder.setViewVisible(topHolder.getTitleView(), View.VISIBLE);
//            topHolder.getTitleView().setText(mode.getTitle());
//        }
//        List datas = new ArrayList<>();
//        datas.add(setDetailBean(mode));
//
//        mRvContent.setLayoutManager(new LinearLayoutManager(this));
//        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.color._dddddd_7a7b7d));
//        mAdapter = new NewsDetailAdapter(datas, false, true);
//        mAdapter.setEmptyView(
//                new EmptyPageHolder(mRvContent,
//                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
//                ).itemView);
//        mRvContent.setAdapter(mAdapter);
//    }
//
//    /**
//     * 设置数据格式
//     * @param mode
//     * @return
//     */
//    private DraftDetailBean setDetailBean(AdModel mode) {
//        DraftDetailBean bean = new DraftDetailBean();
//        if (mode != null && !TextUtils.isEmpty(mode.getContent())) {
//            DraftDetailBean.ArticleBean articleBean = new DraftDetailBean.ArticleBean();
//            bean.setArticle(articleBean);
//            articleBean.setContent(mode.getContent());
//        }
//        return bean;
//    }
//
//
//    /**
//     * WebView加载完毕
//     */
//    @Override
//    public void onOptPageFinished() {
//        mAdapter.showAll();
//    }
//
//    @Override
//    public void onOptClickChannel() {
//
//    }
//
//    private float mScale;
//
//    /**
//     * 阅读百分比
//     *
//     * @param scale
//     */
//    @Override
//    public void onReadingScaleChange(float scale) {
//        mScale = scale;
//    }
//
//    @OnClick({R2.id.iv_top_bar_back})
//    public void onClick(View view) {
//        if (ClickTracker.isDoubleClick()) return;
//        if (view.getId() == R.id.iv_top_bar_back) {
//            finish();
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mAdapter != null) {
//            mAdapter.onWebViewResume();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mAdapter != null) {
//            mAdapter.onWebViewPause();
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
//        super.onDestroy();
//        if (builder != null) {
//            //阅读深度
//            builder.setPercentage(mScale + "");
//            builder.readPercent(mScale + "");
//            Analytics mAnalytics = builder.build();
//            if (mAnalytics != null) {
//                mAnalytics.sendWithDuration();
//            }
//        }
//    }
//
//    @Override
//    public void update(AdModel model) {
//        fillData(model);
//    }
//
//    /**
//     * 软文请求失败
//     *
//     * @param slotId
//     * @param errorCode
//     */
//    @Override
//    public void adFail(String slotId, int errorCode) {
//        mRlEmpty.setVisibility(View.VISIBLE);
//        mTvEmptyContent.setText("该文章不存在");
//    }
//}
