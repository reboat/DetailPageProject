package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.daily.news.R2;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:
 */


public class SpecialMoreActivity extends DailyActivity {
    @BindView(R2.id.iv_share)
    ImageView ivShare;
    @BindView(R2.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.view_pager)
    CompatViewPager viewPager;

    /**
     * 专题id
     */
    private DraftDetailBean mDraftDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData(getIntent());
        super.onCreate(savedInstanceState);
        setContentView( R.layout.module_news_activity_activity_special_more);
        ButterKnife.bind(this);
        initView();
    }

    private void bindData(List<SpecialGroupBean> groupBeanList) {
        if (groupBeanList==null){
            groupBeanList = new ArrayList<>();
            // TODO: 2019/3/12 模拟数据
//            for (int i = 0; i < 10; i++) {
//                SpecialGroupBean specialGroupBean = new SpecialGroupBean();
//                specialGroupBean.setGroup_name("更多");
//                specialGroupBean.setGroup_id(""+i);
//                groupBeanList.add(specialGroupBean);
//            }
        }
        viewPager.setAdapter(new SpecialPagerAdapter(getSupportFragmentManager(),groupBeanList));
        tabLayout.setViewPager(viewPager);
    }

    private void initView() {
        if (mDraftDetailBean!=null){
            bindData(mDraftDetailBean.getArticle().getSubject_groups());
        }
    }

    private void getIntentData(Intent intent) {
        if (intent != null&&intent.getExtras()!=null&&intent.getExtras().getSerializable(IKey.NEWS_DETAIL)!=null) {
            mDraftDetailBean = (DraftDetailBean) intent.getExtras().getSerializable(IKey.NEWS_DETAIL);
        }
    }

}
