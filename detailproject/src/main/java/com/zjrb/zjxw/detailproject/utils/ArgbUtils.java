package com.zjrb.zjxw.detailproject.utils;

import android.animation.ArgbEvaluator;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 颜色转换工具
 *
 * @author a_liYa
 * @date 2017/11/1 13:37.
 */
public class ArgbUtils {

    private static ArgbEvaluator sArgb = new ArgbEvaluator();

    private static Interpolator sInterpolator = new AccelerateDecelerateInterpolator();

    public static int evaluate(float fraction, int startValue, int endValue) {
        return (int) sArgb.evaluate(sInterpolator.getInterpolation(fraction), startValue, endValue);
    }
}

