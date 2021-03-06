package com.zjrb.zjxw.detailproject.subject;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialAdapter;

/**
 * 专题详情页 - 分割线
 *
 * @author a_liYa
 * @date 2017/10/12 下午5:26.
 */
public class SpecialSpaceDivider extends ListSpaceDivider {

    public SpecialSpaceDivider(double heightDip, int attrId) {
        super(heightDip, attrId, true);
        mIncludeLastItem = false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        outRect.set(0, 0, 0, 0);
        int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter() instanceof SpecialAdapter) {
            SpecialAdapter adapter = (SpecialAdapter) parent.getAdapter();
            if (!adapter.isInnerPosition(position)) {
                position = adapter.cleanPosition(position);
                int dataSize = adapter.getDataSize();
                if (position < dataSize - 1) {
                    Object data = adapter.getData(position);
                    Object nextData = adapter.getData(position + 1);
                    if ((data instanceof ArticleItemBean && nextData instanceof ArticleItemBean)
                            || (data instanceof ArticleItemBean && nextData instanceof
                            SpecialGroupBean)) {
                        // 文章 - 文章
                        outRect.set(0, 0, 0, mDividerHeight);
                    }
                }
            }
        }
    }

    @Override
    protected void drawHorizontal(Canvas c, RecyclerView parent) {
        if (parent.getAdapter() == null) return;
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        int footerCount = 0;
        SpecialAdapter adapter = null;
        if (parent.getAdapter() instanceof SpecialAdapter) {
            adapter = (SpecialAdapter) parent.getAdapter();
            footerCount = adapter.getFooterCount();
        }
        final int itemCount = parent.getAdapter().getItemCount();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                continue;
            }
            if (!mIncludeLastItem && position == itemCount - 1 - footerCount) {
                continue;
            }
            if (adapter != null && adapter.isInnerPosition(position)) {
                continue;
            }

            position = adapter.cleanPosition(position);
            Object data = adapter.getData(position);
            if (position < adapter.getDataSize() - 1) {
                Object nextData = adapter.getData(position + 1);
                if ((data instanceof ArticleItemBean && nextData instanceof ArticleItemBean) ||
                        (data instanceof ArticleItemBean && nextData instanceof SpecialGroupBean)) {
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
