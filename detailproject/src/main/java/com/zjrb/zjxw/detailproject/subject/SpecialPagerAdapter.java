package com.zjrb.zjxw.detailproject.subject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.zjrb.daily.news.ui.adapter.TabPagerAdapter;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;

import java.util.List;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:
 */


public class SpecialPagerAdapter extends TabPagerAdapter<SpecialGroupBean> {
    public SpecialPagerAdapter(FragmentManager fm, @NonNull List<SpecialGroupBean> list) {
        super(fm, list);
    }

    @Override
    public Fragment newFragment(SpecialGroupBean specialGroupBean) {
        return SpecialListFragment.fragment(specialGroupBean);
    }

    @Override
    public String toKey(SpecialGroupBean specialGroupBean) {
        return specialGroupBean.getGroup_name()+specialGroupBean.getGroup_id();
    }

    @Override
    protected boolean isUpdateEntity(SpecialGroupBean newVal, SpecialGroupBean oldVal) {
        return !TextUtils.equals(newVal.getGroup_name(), oldVal.getGroup_name());
    }

    @Override
    protected int getId(SpecialGroupBean data) {
        return Integer.valueOf(data.getGroup_id());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getGroup_name();
    }
}
