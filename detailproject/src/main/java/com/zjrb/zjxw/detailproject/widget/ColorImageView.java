package com.zjrb.zjxw.detailproject.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.aliya.uimode.intef.UiModeChangeListener;
import com.zjrb.zjxw.detailproject.utils.ArgbUtils;

/**
 * 可变色 - ImageView
 *
 * @author a_liYa
 * @date 2017/11/1 15:08.
 */
public class ColorImageView extends AppCompatImageView implements UiModeChangeListener {

    private int mApplyMaskColor = NO_COLOR;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PorterDuffXfermode xfermodeMask = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    private int startAttrId;
    private int endAttrId;
    private float mFraction;

    private static final int NO_COLOR = Color.TRANSPARENT;

    public ColorImageView(Context context) {
        this(context, null);
    }

    public ColorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mApplyMaskColor != NO_COLOR) {
            int saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(),
                    null, Canvas.ALL_SAVE_FLAG);
            super.onDraw(canvas);
            mPaint.setXfermode(xfermodeMask);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }

    public void setApplyMaskColor(int color) {
        mApplyMaskColor = color;
        mPaint.setColor(mApplyMaskColor);
        invalidate();
    }

    public void setAttrId(@ColorRes int startId, @ColorRes int endId) {
        startAttrId = startId;
        endAttrId = endId;
    }

    public void setFraction(float fraction) {
        if (startAttrId == 0 || endAttrId == 0) {
            return;
        }

        mFraction = fraction;
        setApplyMaskColor(ArgbUtils.evaluate(fraction,
                ContextCompat.getColor(getContext(), startAttrId),
                ContextCompat.getColor(getContext(), endAttrId)));
    }

    private Resources.Theme getTheme() {
        return getContext().getTheme();
    }

    private OnUiModeChangeListener mOnUiModeChangeListener;

    public void setOnUiModeChangeListener(OnUiModeChangeListener listener) {
        mOnUiModeChangeListener = listener;
    }

    @Override
    public void onUiModeChange() {
        setFraction(mFraction);
        if (mOnUiModeChangeListener != null) {
            mOnUiModeChangeListener.onUiModeChange(mFraction);
        }
    }

    public interface OnUiModeChangeListener {

        void onUiModeChange(float fraction);

    }

}
