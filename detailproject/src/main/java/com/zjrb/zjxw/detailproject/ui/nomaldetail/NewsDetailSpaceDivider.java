package com.zjrb.zjxw.detailproject.ui.nomaldetail;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjrb.core.recycleView.adapter.BaseRecyclerAdapter;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;

/**
 * 普通详情页 - 分割线
 * <p>
 * Created by wanglinjie.
 * create time:2017/10/12 下午5:26.
 */
public class NewsDetailSpaceDivider extends ListSpaceDivider {

    public NewsDetailSpaceDivider(double heightDip, int attrId) {
        super(heightDip, attrId, true);
        mIncludeLastItem = false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
        int position = parent.getChildAdapterPosition(view);
        BaseRecyclerAdapter adapter = (BaseRecyclerAdapter) parent.getAdapter();
        if (adapter == null) return;
        if (!adapter.isInnerPosition(position)) {
            position = adapter.cleanPosition(position);
            int dataSize = adapter.getDataSize();
            if (position < dataSize - 1 && position > 1) {
                Object data = adapter.getData(position);
                Object nextData = adapter.getData(position + 1);
                if (data instanceof RelatedNewsBean/* && nextData instanceof RelatedNewsBean*/) {
                    outRect.set(0, 0, 0, mDividerHeight);
                }
            }
        }
    }

    @Override
    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        BaseRecyclerAdapter adapter = (BaseRecyclerAdapter) parent.getAdapter();
        if (adapter == null) return;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }
            if (!mIncludeLastItem && position == adapter.getItemCount() - 1 - adapter.getFooterCount()) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }

            position = adapter.cleanPosition(position);
            Object data = adapter.getData(position);
            if (position < adapter.getDataSize() - 1) {
                Object nextData = adapter.getData(position + 1);
                if (data instanceof RelatedNewsBean/* && nextData instanceof RelatedNewsBean*/) {
                    // 文章 - 文章
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + mDividerHeight;
                    c.drawRect(left + mLeftMargin, top, right - mRightMargin, bottom, mPaint);
                }
            }
        }
    }
}
