package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 底部 bar view holder
 *
 * @author a_liYa
 * @date 2017/11/1 18:51.
 */
public class FloorBarHolder {

    private View mFloorBar;

    private boolean mVisible; // 是否可以显示

    /**
     * 底部评论框显示动画
     */
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    public FloorBarHolder(View floorBar) {
        mFloorBar = floorBar;
    }

    public void setCanVisible(boolean visible) {
        if (this.mVisible != visible) {
            if (visible) {
                if (mFloorBar.getVisibility() != View.VISIBLE) {
                    mFloorBar.setVisibility(View.VISIBLE);
                }
            }
            mFloorBar.animate().setInterpolator(mInterpolator)
                    .setDuration(200)
                    .translationY(visible ? 0 : mFloorBar.getHeight() + getMarginBottom());

            this.mVisible = visible;
        }
    }

    public void setVisible(boolean visible) {
        mFloorBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public float getRangeHeight() {
        int height = mFloorBar.getHeight();
        if (height == 0) {
            mFloorBar.measure(0,0);
            height = mFloorBar.getMeasuredHeight();
        }
        return height * 1.5f;
    }

    private int getMarginBottom() {
        if (mFloorBar.getLayoutParams() instanceof MarginLayoutParams) {
            return ((MarginLayoutParams) mFloorBar.getLayoutParams()).bottomMargin;
        }
        return 0;
    }

}
