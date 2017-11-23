package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.zjrb.core.common.biz.TouchSlopHelper;

/**
 * 底部 bar view holder
 *
 * @author a_liYa
 * @date 2017/11/1 18:51.
 */
public class FloorBarHolder implements TouchSlopHelper.OnTouchSlopListener {

    private View mFloorBar;

    private boolean isUp = true; // 缓存 isUp 的状态
    private boolean visible;

    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;
    /**
     * 底部评论框显示动画
     */
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public FloorBarHolder(View floorBar) {
        mFloorBar = floorBar;
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
    }

    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            if (visible) {
                onTouchSlop(isUp); // 显示时 恢复 isUp 状态
            } else {
                onTouchSlop(true);
            }
            this.visible = visible;
        }
    }

    /**
     * 滑动显示/隐藏底部评论栏
     *
     * @param isUp
     */
    @Override
    public void onTouchSlop(boolean isUp) {
        this.isUp = isUp;
        if (!visible) { // visible=false时不处理
            return;
        }
        if (!isUp && mFloorBar.getVisibility() != View.VISIBLE) {
            mFloorBar.setVisibility(View.VISIBLE);
        }

        mFloorBar.animate().setInterpolator(mInterpolator)
                .setDuration(200)
                .translationY(!isUp ? 0 : mFloorBar.getHeight() + getMarginBottom());
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        if (mTouchSlopHelper != null) {
            mTouchSlopHelper.onTouchEvent(ev);
        }
    }

    private int getMarginBottom() {
        if (mFloorBar.getLayoutParams() instanceof MarginLayoutParams) {
            return ((MarginLayoutParams) mFloorBar.getLayoutParams()).bottomMargin;
        }
        return 0;
    }

}
