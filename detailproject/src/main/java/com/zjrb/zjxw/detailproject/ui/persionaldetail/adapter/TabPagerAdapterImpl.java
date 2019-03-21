package com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装TabLayout + ViewPager 适配器的 FragmentPagerAdapter简单实现类
 * <p>
 * (适用页面少,自动保存到内存)
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:12
 */
public class TabPagerAdapterImpl extends FragmentPagerAdapter {

    private List<TabInfo> mTabList;
    private Context mContext;

    public TabPagerAdapterImpl(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mTabList = new ArrayList<>();
    }

    public TabPagerAdapterImpl addTabInfo(Class<? extends Fragment> clz, String title, Bundle args) {
        mTabList.add(new TabInfo(clz, title, args));
        return this;
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo tabInfo = mTabList.get(position);
        if (tabInfo.fragment == null) {
            tabInfo.fragment = Fragment.instantiate(mContext, tabInfo.clz.getName(),
                    tabInfo.args);
        }
        return tabInfo.fragment;
    }

    @Override
    public int getCount() {
        return mTabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabList.get(position).title;
    }


    /**
     * TabInfo Tab相关参数
     *
     * @author wanglinjie
     * @date 2016/10/9 14:34.
     */
    private static class TabInfo {

        public Class<? extends Fragment> clz;
        public String title;
        public Fragment fragment;
        public Bundle args;

        public TabInfo(Class<? extends Fragment> clz, String title, Bundle args) {
            this.clz = clz;
            this.title = title;
            this.args = args;
        }
    }

}
