package com.zjrb.zjxw.detailproject.widget.photoview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.aliya.view.fitsys.FitHelper;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager implements FitHelper.FitWindowsProxy {

    private FitHelper helper;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            helper = new FitHelper(context, attrs, this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (helper != null) {
            helper.fitChildView(child);
        }
    }

    @Override
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        if (helper != null) {
            helper.setWindowInsets(insets);
        }
        return super.dispatchApplyWindowInsets(insets);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (helper != null) {
            return helper.fitSystemWindows(insets);
        }
        return super.fitSystemWindows(insets);
    }

    @Override
    public boolean fitSystemWindowsProxy(Rect insets, boolean callSuper) {
        return callSuper ? super.fitSystemWindows(insets) : fitSystemWindows(insets);
    }

}
