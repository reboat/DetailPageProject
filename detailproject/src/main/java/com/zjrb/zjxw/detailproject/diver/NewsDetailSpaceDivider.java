package com.zjrb.zjxw.detailproject.diver;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjrb.coreprojectlibrary.ui.widget.divider.AbsSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.holder.NewsDetailRelatedNewsHolder;

/**
 * 新闻详情页自定义分割线
 * Created by wanglinjie.
 * create time:2017/7/17  下午2:56
 */

public class NewsDetailSpaceDivider extends AbsSpaceDivider {
    private int mSpace;
    // 画笔
    private Paint mPaint;

    public NewsDetailSpaceDivider() {
        super(UIUtils.getColor(R.color.dc_000000));
        mSpace = UIUtils.dip2px(0.5f);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        super.getItemOffsets(outRect, view, parent, state);

        //TODO 支持多种  WLJ
        if (parent.getChildViewHolder(view) instanceof NewsDetailRelatedNewsHolder) {
            outRect.set(0, 0, 0, mSpace);
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        mPaint.setColor(getUiModeColor(UIUtils.getContext()));
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (parent.getChildViewHolder(child) instanceof NewsDetailRelatedNewsHolder) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mSpace;
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
