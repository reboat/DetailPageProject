package com.zjrb.zjxw.detailproject.ui.persionaldetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.utils.BundleHelper;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.apibean.task.OfficalDetailTask;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter.TabPagerAdapterImpl;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.fragment.PersionalDetailInfoFragment;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.fragment.PersionalRelateFragment;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.toolsbar.BIZTopBarFactory;
import cn.daily.news.biz.core.ui.toolsbar.holder.DefaultTopBarHolder1;

/**
 * 官员详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalDetailActivity extends DailyActivity implements ViewPager
        .OnPageChangeListener {
    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_sex)
    TextView tvSex;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.ly_container)
    FitWindowsLinearLayout mLyContailer;

    /**
     * 官员ID
     */
    public String official_id;

    private TabPagerAdapterImpl pagerAdapter;
    private OfficalDetailBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_persional);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        viewpager.addOnPageChangeListener(this);
        //先加载数据
        loadData();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null && data.getQueryParameter(IKey.ID) != null) {
                official_id = data.getQueryParameter(IKey.ID);
            }
            if (intent.hasExtra(IKey.ID)) {
                official_id = intent.getStringExtra(IKey.ID);
            }
        }
    }

    Analytics.AnalyticsBuilder builder;

    /**
     * 加载数据
     */
    private void loadData() {
        new OfficalDetailTask(new LoadingCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean data) {
                if (data == null) return;
                builder = DataAnalyticsUtils.get().CreateOfficalAnalytic(data);
                initView(data);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mLyContailer)).exe(official_id);
    }

    /**
     * 初始化官员详情页基础数据
     */
    private void initView(OfficalDetailBean b) {
        bean = b;
        bean.setOfficer(b.getOfficer());
        if (b != null && b.getOfficer() != null) {
            OfficalDetailBean.OfficerBean bean = b.getOfficer();
            //头像
            GlideApp.with(ivAvatar).load(bean.getPhoto()).placeholder(PH.zheSmall()).apply(AppGlideOptions.smallOptions()).into(ivAvatar);
            //姓名
            if (bean.getName() != null) {
                tvName.setText(bean.getName());
            }
            //性别
            if (bean.getGender_text() != null) {
                tvSex.setText(bean.getGender_text());
            }
            //描述
            if (bean.getDescription() != null) {
                tvContent.setText(bean.getDescription());
            }
            init();
        }
    }

    private DefaultTopBarHolder1 topbarHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topbarHolder = BIZTopBarFactory.createDefault1(view, this);
        topbarHolder.setViewVisible(topbarHolder.getShareView(), View.VISIBLE);
        return topbarHolder.getView();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (builder != null) {
            Analytics mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick({R2.id.lly_reporter, R2.id.iv_top_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.lly_reporter) {
            //跳转到官员详情页H5
            if (bean != null && bean.getOfficer() != null && !TextUtils.isEmpty(bean.getOfficer().getDetail_url())) {
                Nav.with(this).to(bean.getOfficer().getDetail_url());
            }
        } else {//点击分享
            if (bean != null && bean.getOfficer() != null && !TextUtils.isEmpty(bean.getOfficer().getShare_url())) {
                DataAnalyticsUtils.get().OfficalClickShare(bean);
                //分享
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setAnalyticsBean(getWMData())
                        .setImgUri(bean.getOfficer().getList_pic())
                        .setTextContent(getString(R.string.module_detail_share_content_from))
                        .setTitle(bean.getOfficer().getName())
                        .setTargetUrl(bean.getOfficer().getShare_url())
                        .setShareContentID(bean.getOfficer().getId() + "")
                        .setShareType("官员")
                        .setEventName("PageShare")
                );
            }

        }
    }


    /**
     * 设置网脉数据
     */
    private OutSizeAnalyticsBean getWMData() {
        return OutSizeAnalyticsBean.getInstance()
                .setObjectID(bean.getOfficer().getId() + "")
                .setPageType("官员页面")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("customObjectType", "OfficerType")
                        .toString());
    }


    private TextView tv1;
    private TextView tv2;
    private View v1;
    private View v2;

    /**
     * 官员详情页添加官员新闻和官员履历
     * 将数据传递到fragment
     */
    private void init() {
        pagerAdapter = new TabPagerAdapterImpl(getSupportFragmentManager(), this);

        //传递官员详情页相关新闻
        Bundle bundlePersionalRelate = BundleHelper.creatBundle(IKey
                .FRAGMENT_ARGS, PersionalRelateFragment.TYPE_NEWS);
        bundlePersionalRelate.putSerializable(IKey.OFFICIAL_ID, official_id);
        bundlePersionalRelate.putSerializable(IKey.FRAGMENT_PERSIONAL_RELATER, bean);
        pagerAdapter.addTabInfo(PersionalRelateFragment.class, "相关新闻", bundlePersionalRelate);

        //传递官员详情页履历
        Bundle bundlePersionalDetailInfo = BundleHelper.creatBundle(IKey
                .FRAGMENT_ARGS, PersionalDetailInfoFragment.TYPE_INFO);
        bundlePersionalDetailInfo.putSerializable(IKey.FRAGMENT_PERSIONAL_INFO, bean);
        pagerAdapter.addTabInfo(PersionalDetailInfoFragment.class, "任职履历", bundlePersionalDetailInfo);

        viewpager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewpager);
//        tabLayout.setupWithViewPager(viewpager);

//        for (int i = 0; i < pagerAdapter.getCount(); i++) {
//
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//
//            if (i == 0) {
//                v1 = LayoutInflater.from(this).inflate(R.layout.module_detail_tab_layout,
//                        viewpager, false);
//
//                tv1 = v1.findViewById(R.id.tv_item_tab_score_title);
//                tv1.setText(pagerAdapter.getPageTitle(i));
//                tab.setCustomView(v1);
//                tv1.setTextColor(getResources().getColor(R.color._222222));
//            } else {
//                v2 = LayoutInflater.from(this).inflate(R.layout.module_detail_tab_layout,
//                        viewpager, false);
//                tv2 = v2.findViewById(R.id.tv_item_tab_score_title);
//                tv2.setText(pagerAdapter.getPageTitle(i));
//                tv2.setTextColor(getResources().getColor(R.color._666666));
//                tab.setCustomView(v2);
//            }
//
//        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        if (position == 0) {
//            tv1.setTextColor(getResources().getColor(R.color._222222));
//            tv2.setTextColor(getResources().getColor(R.color._666666));
//        } else {
//            tv1.setTextColor(getResources().getColor(R.color._666666));
//            tv2.setTextColor(getResources().getColor(R.color._222222));
//        }
        if (bean != null && bean.getOfficer() != null) {
            DataAnalyticsUtils.get().OfficialDetailClick(bean);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
