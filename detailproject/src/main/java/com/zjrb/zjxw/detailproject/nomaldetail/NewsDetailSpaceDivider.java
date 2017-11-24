package com.zjrb.zjxw.detailproject.nomaldetail;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;

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
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        outRect.set(0, 0, 0, 0);
        int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter() instanceof NewsDetailAdapter) {
            NewsDetailAdapter adapter = (NewsDetailAdapter) parent.getAdapter();
            if (!adapter.isInnerPosition(position)) {
                position = adapter.cleanPosition(position);
                int dataSize = adapter.getDataSize();
                if (position < dataSize - 1 && position > 1) {
                    Object data = adapter.getData(position);
                    Object nextData = adapter.getData(position + 1);
                    if (data instanceof RelatedNewsBean && nextData instanceof RelatedNewsBean) {
                        // 文章 - 文章
                        outRect.set(0, 0, 0, mDividerHeight);
                    }
//                    else if (data instanceof HotCommentsBean && nextData instanceof HotCommentsBean) {
//                        //评论
//                        outRect.set(0, 0, 0, mDividerHeight);
//                    }
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
        NewsDetailAdapter adapter = null;
        if (parent.getAdapter() instanceof NewsDetailAdapter) {
            adapter = (NewsDetailAdapter) parent.getAdapter();
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
                if (data instanceof RelatedNewsBean && nextData instanceof RelatedNewsBean) {
                    // 文章 - 文章
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                            .getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + mDividerHeight;
                    c.drawRect(left + mLeftMargin, top, right - mRightMargin, bottom, mPaint);
                }
//                else if (data instanceof HotCommentsBean && nextData instanceof HotCommentsBean) {
//                    // 文章 - 文章
//                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
//                            .getLayoutParams();
//                    int top = child.getBottom() + params.bottomMargin;
//                    int bottom = top + mDividerHeight;
//                    c.drawRect(left + 57, top, right - mRightMargin, bottom, mPaint);
//                }
            }
        }
    }
}
