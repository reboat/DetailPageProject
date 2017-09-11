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
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.db.BundleHelper;
import com.zjrb.core.domain.eventbus.EventBase;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.eventBus.PersionalDetailTabEvent;
import com.zjrb.zjxw.detailproject.eventBus.PersionalInfoTabEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.TabPagerAdapterImpl;
import com.zjrb.zjxw.detailproject.persionaldetail.fragment.PersionalDetailInfoFragment;
import com.zjrb.zjxw.detailproject.persionaldetail.fragment.PersionalRelateFragment;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员履历
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalDetailActivity extends BaseActivity {
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
    @BindView(R2.id.nsv)
    LinearLayout nsv;

    /**
     * 官员ID
     */
    public int official_id = -1;

    private TabPagerAdapterImpl pagerAdapter;
    private OfficalDetailBean bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_persional);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        //先加载数据
        loadData();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            official_id = Integer.parseInt(data.getQueryParameter(Key.OFFICIAL_ID));
        }
    }

    private List<SubjectItemBean> mockTest() {
        List<SubjectItemBean> list = new ArrayList<>();
        SubjectItemBean b = new SubjectItemBean();
        b.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setList_title("标题1");
        SubjectItemBean b1 = new SubjectItemBean();
        b1.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b1.setList_title("标题1");
        SubjectItemBean b2 = new SubjectItemBean();
        b2.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b2.setList_title("标题1");
        SubjectItemBean b3 = new SubjectItemBean();
        b3.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b3.setList_title("标题1");
        SubjectItemBean b4 = new SubjectItemBean();
        b4.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b4.setList_title("标题1");
        SubjectItemBean b5 = new SubjectItemBean();
        b5.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b5.setList_title("标题1");
        SubjectItemBean b6 = new SubjectItemBean();
        b6.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b6.setList_title("标题1");
        SubjectItemBean b7 = new SubjectItemBean();
        b7.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b7.setList_title("标题1");
        SubjectItemBean b8 = new SubjectItemBean();
        b8.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b8.setList_title("标题1");
        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        list.add(b6);
        list.add(b7);
        list.add(b8);
        return list;
    }

    private OfficalDetailBean.OfficerBean mockTest2() {
        OfficalDetailBean.OfficerBean officer = new OfficalDetailBean.OfficerBean();
        List<OfficalDetailBean.OfficerBean.ResumesBean> resumes = new ArrayList<>();
        OfficalDetailBean.OfficerBean.ResumesBean b = new OfficalDetailBean.OfficerBean.ResumesBean();
        b.setLocation("浙江");
        b.setMonth(8);
        b.setSameYear(false);
        b.setTitle("省委书记");
        b.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b1 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b1.setLocation("浙江");
        b1.setMonth(8);
        b1.setSameYear(false);
        b1.setTitle("省委书记");
        b1.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b2 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b2.setLocation("浙江");
        b2.setMonth(8);
        b2.setSameYear(false);
        b2.setTitle("省委书记");
        b2.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b3 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b3.setLocation("浙江");
        b3.setMonth(8);
        b3.setSameYear(false);
        b3.setTitle("省委书记");
        b3.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b4 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b4.setLocation("浙江");
        b4.setMonth(8);
        b4.setSameYear(false);
        b4.setTitle("省委书记");
        b4.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b5 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b5.setLocation("浙江");
        b5.setMonth(8);
        b5.setSameYear(false);
        b5.setTitle("省委书记");
        b5.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b6 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b6.setLocation("浙江");
        b6.setMonth(8);
        b6.setSameYear(false);
        b6.setTitle("省委书记");
        b6.setYear(2014);
        OfficalDetailBean.OfficerBean.ResumesBean b7 = new OfficalDetailBean.OfficerBean.ResumesBean();
        b7.setLocation("浙江");
        b7.setMonth(8);
        b7.setSameYear(false);
        b7.setTitle("省委书记");
        b7.setYear(2014);
        resumes.add(b);
        resumes.add(b1);
        resumes.add(b2);
        resumes.add(b3);
        resumes.add(b4);
        resumes.add(b5);
        resumes.add(b6);
        resumes.add(b7);
        officer.setResumes(resumes);
        return officer;

    }

    /**
     * 加载数据
     */
    private void loadData() {
        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean data) {
                initView(data);
                init();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }
        }).setTag(this).exe(official_id + "", "", "");
    }

    /**
     * 初始化官员详情页基础数据
     */
    private void initView(OfficalDetailBean b) {
        bean = b;
        bean.setArticle_list(mockTest());
        bean.setOfficer(mockTest2());
        if (b != null && b.getOfficer() != null) {
            OfficalDetailBean.OfficerBean bean = b.getOfficer();
            //头像
            GlideApp.with(ivAvatar).load(bean.getList_pic()).circleCrop().into(ivAvatar);
            //姓名
            tvName.setText(bean.getName());
            //性别
            tvSex.setText(bean.getGender());
            //描述
            tvContent.setText(bean.getDescription());
        }
    }


    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventBase event) {
//        if (event instanceof PersionalInfoTabEvent) {
//            EventBus.getDefault().removeStickyEvent(event);
//            tv1.setText("相关新闻");
//            tv1.setTextColor(getResources().getColor(R.color.bc_ffffff));
//            tv2.setTextColor(getResources().getColor(R.color.bc_f44b50));
//            ((ViewGroup) v1.getParent()).setBackgroundResource(R.drawable.border_persional_detail_tab_left);
//            ((ViewGroup) v2.getParent()).setBackgroundResource(R.drawable.module_detail_subscribe_red_right);
//        }
//
//        //切换到历史求助页面
//        if (event instanceof PersionalDetailTabEvent) {
//            EventBus.getDefault().removeStickyEvent(event);
//            tv2.setText("个人履历");
//            tv2.setTextColor(getResources().getColor(R.color.bc_ffffff));
//            tv1.setTextColor(getResources().getColor(R.color.bc_f44b50));
//            ((ViewGroup) v2.getParent()).setBackgroundResource(R.drawable.border_persional_detail_tab_right);
//            ((ViewGroup) v1.getParent()).setBackgroundResource(R.drawable.module_detail_subscribe_red_left);
//        }

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
        Bundle bundlePersionalRelate = BundleHelper.creatBundle(Key
                .FRAGMENT_ARGS, PersionalRelateFragment.TYPE_NEWS);
        bundlePersionalRelate.putSerializable(Key.OFFICIAL_ID, official_id);
        bundlePersionalRelate.putSerializable(Key.FRAGMENT_PERSIONAL_RELATER, bean);
        pagerAdapter.addTabInfo(PersionalRelateFragment.class, "相关新闻", bundlePersionalRelate);

        //传递官员详情页履历
        Bundle bundlePersionalDetailInfo = BundleHelper.creatBundle(Key
                .FRAGMENT_ARGS, PersionalDetailInfoFragment.TYPE_INFO);
        bundlePersionalDetailInfo.putSerializable(Key.FRAGMENT_PERSIONAL_INFO, bean);
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
                ((ViewGroup) parent).setBackgroundResource(R.drawable.module_detail_subscribe_red_left);
            } else {
                v2 = LayoutInflater.from(this).inflate(R.layout.module_detail_tab_layout,
                        viewpager, false);
                tv2 = (TextView) v2.findViewById(R.id.tv_item_tab_score_title);
                tv2.setText(pagerAdapter.getPageTitle(i));
                tab.setCustomView(v2);
                ViewParent parent = v2.getParent();
                ((ViewGroup) parent).setBackgroundResource(R.drawable.module_detail_subscribe_red_right);
            }

        }

    }

}
