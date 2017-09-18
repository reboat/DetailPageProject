package com.zjrb.zjxw.detailproject.photodetail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zjrb.core.ui.fragment.ImagePreviewFragment;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.photodetail.ImageMoreFragment;

/**
 * 图片预览 ViewPager 适配器，图集需要展示更多图集
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class ImagePrePagerAdapter extends FragmentStatePagerAdapter {

    private DraftDetailBean mBean;

    public ImagePrePagerAdapter(FragmentManager fm, DraftDetailBean bean) {
        super(fm);
        this.mBean = bean;
    }

    @Override
    public int getCount() {
        return mBean == null ? 0 : mBean.getArticle().getAlbum_image_count();
    }

    @Override
    public Fragment getItem(int position) {
        //最后一张是更多图集,需要参数传入
        if (mBean.getArticle().getAlbum_image_count() > 1 && (position == (mBean.getArticle().getAlbum_image_count() - 1))) {
            return ImageMoreFragment.newInstance(mBean);
        }
        return ImagePreviewFragment.newInstance(
                mBean.getArticle().getAlbum_image_list().get(position).getImage_url()
        );
    }

}
