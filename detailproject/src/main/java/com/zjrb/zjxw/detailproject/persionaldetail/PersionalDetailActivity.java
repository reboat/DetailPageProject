package com.zjrb.zjxw.detailproject.persionaldetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.db.BundleHelper;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.TabPagerAdapterImpl;
import com.zjrb.zjxw.detailproject.persionaldetail.fragment.PersionalDetailInfoFragment;
import com.zjrb.zjxw.detailproject.persionaldetail.fragment.PersionalRelateFragment;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 官员详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalDetailActivity extends BaseActivity implements ViewPager
        .OnPageChangeListener {
    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R2.id.tv_name)
    TextView tvName;
    @BindView(R2.id.tv_sex)
    TextView tvSex;
    @BindView(R2.id.ly_name)
    LinearLayout lyName;
    @BindView(R2.id.tv_content)
    TextView tvContent;
    @BindView(R2.id.lly_reporter)
    RelativeLayout llyReporter;
    @BindView(R2.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R2.id.viewpager)
    ViewPager viewpager;
    @BindView(R2.id.ly_container)
    LinearLayout mLyContailer;

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
        }
    }

    /**
     * 加载数据
     */
    private void loadData() {
        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean data) {
                if (data == null) return;
                initView(data);
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
        bean.setArticle_list(b.getArticle_list());
        bean.setOfficer(b.getOfficer());
        if (b != null && b.getOfficer() != null) {
            OfficalDetailBean.OfficerBean bean = b.getOfficer();
            //头像
            GlideApp.with(ivAvatar).load(bean.getPhoto()).placeholder(PH.zheSmall()).circleCrop().into(ivAvatar);
            //姓名
            if (bean.getName() != null) {
                tvName.setText(bean.getName());
            }
            //性别
            if (bean.getGender() != null) {
                tvSex.setText(bean.getGender());
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
        topbarHolder = TopBarFactory.createDefault1(view, this);
        topbarHolder.setViewVisible(topbarHolder.getShareView(), View.VISIBLE);
        return topbarHolder.getView();
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
            Nav.with(this).to(bean.getOfficer().getDetail_url());
        } else {
            //分享
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(bean.getOfficer().getPhoto())
                    .setTextContent(bean.getOfficer().getName())
                    .setTitle(getString(R.string.module_detail_share_content_from))
                    .setTargetUrl(bean.getOfficer().getShare_url()));
        }
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
        tabLayout.setupWithViewPager(viewpager);

        for (int i = 0; i < pagerAdapter.getCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);

            if (i == 0) {
                v1 = LayoutInflater.from(this).inflate(R.layout.module_detail_tab_layout,
                        viewpager, false);

                tv1 = (TextView) v1.findViewById(R.id.tv_item_tab_score_title);
                tv1.setText(pagerAdapter.getPageTitle(i));
                tab.setCustomView(v1);
                ViewParent parent = v1.getParent();
                ((ViewGroup) parent).setBackgroundResource(R.drawable.module_detail_related_red_left);
            } else {
                v2 = LayoutInflater.from(this).inflate(R.layout.module_detail_tab_layout,
                        viewpager, false);
                tv2 = (TextView) v2.findViewById(R.id.tv_item_tab_score_title);
                tv2.setText(pagerAdapter.getPageTitle(i));
                tv2.setTextColor(getResources().getColor(R.color.tc_f44b50));
                tab.setCustomView(v2);
                ViewParent parent = v2.getParent();
                ((ViewGroup) parent).setBackgroundResource(R.drawable.module_detail_related_red_right_stroke);
            }

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            ((ViewGroup) v1.getParent()).setBackgroundResource(R.drawable.module_detail_related_red_left);
            ((ViewGroup) v2.getParent()).setBackgroundResource(R.drawable.module_detail_related_red_right_stroke);
            tv1.setTextColor(getResources().getColor(R.color.tc_ffffff));
            tv2.setTextColor(getResources().getColor(R.color.tc_f44b50));
        } else {
            ((ViewGroup) v1.getParent()).setBackgroundResource(R.drawable.module_detail_related_red_left_stroke);
            ((ViewGroup) v2.getParent()).setBackgroundResource(R.drawable.module_detail_related_red_right);
            tv1.setTextColor(getResources().getColor(R.color.tc_f44b50));
            tv2.setTextColor(getResources().getColor(R.color.tc_ffffff));
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
