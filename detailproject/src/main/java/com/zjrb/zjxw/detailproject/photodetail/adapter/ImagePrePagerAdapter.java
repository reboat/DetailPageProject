package com.zjrb.zjxw.detailproject.photodetail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import com.zjrb.zjxw.detailproject.photodetail.ImagePreviewFragment;

import java.util.List;

/**
 * 图片预览 ViewPager 适配器
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ImagePrePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mDatas;

    public ImagePrePagerAdapter(FragmentManager fm, List<String> fileList) {
        super(fm);
        this.mDatas = fileList;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImagePreviewFragment.newInstance(
                new ImagePreviewFragment.ParamsEntity.Builder()
                        .setUrl(mDatas.get(position))
                        .build()
        );
    }

}
