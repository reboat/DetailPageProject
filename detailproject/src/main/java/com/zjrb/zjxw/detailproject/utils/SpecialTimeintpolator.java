package com.zjrb.zjxw.detailproject.utils;

import android.view.animation.LinearInterpolator;

/**
 * 专题稿顶部栏图标差值器
 * Created by wanglinjie.
 * create time:2019/3/18  下午2:22
 */
public class SpecialTimeintpolator extends LinearInterpolator {
    private float mFraction;
    public SpecialTimeintpolator(float fraction) {
        mFraction = fraction;
    }

    @Override
    public float getInterpolation(float input) {
        return mFraction;
//        return (float) (Math.pow(2, -10 * input)
//                * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
