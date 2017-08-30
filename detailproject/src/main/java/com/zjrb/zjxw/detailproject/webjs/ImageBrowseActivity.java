package com.zjrb.zjxw.detailproject.webjs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseActivity;
import com.zjrb.coreprojectlibrary.ui.anim.viewpager.DepthPageTransformer;
import com.zjrb.coreprojectlibrary.ui.widget.photoview.HackyViewPager;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.photodetail.ImagePreviewFragment;

/**
 * 图片浏览 Activity
 * <p>
 * 使用方式:
 * Intent ikey = new Intent(getActivity(), ImageBrowseActivity.class);
 * ikey.putExtra(ImageBrowseActivity.EXTRA_IMAGE_URLS, new String[]{});
 * ikey.putExtra(ImageBrowseActivity.EXTRA_IMAGE_INDEX, 0); // 从0开始
 *
 * @author a_liYa
 * @date 16/8/4 下午5:05.
 */
public class ImageBrowseActivity extends BaseActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private String viewpager_indicator = "%1$d/%2$d";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private boolean isLongPicMode;

    public static Intent newIntent(Context ctx, String[] urls, int index) {
        Intent intent = new Intent(ctx, ImageBrowseActivity.class);
        intent.putExtra(ImageBrowseActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImageBrowseActivity.EXTRA_IMAGE_INDEX, index);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_image_browse);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        isLongPicMode = getIntent().getBooleanExtra(Key.IS_LONG_PICTURE_MODE, false);

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = String.format(viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = String.format(viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return ImagePreviewFragment.newInstance(new ImagePreviewFragment.ParamsEntity.Builder()
                    .setUrl(url)
                    .setTapClose(true)
                    .setLongPictureMode(isLongPicMode)
                    .build());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}