package com.zjrb.zjxw.detailproject.ui.subject;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.zjrb.core.recycleView.OverlayViewHolder;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.ui.subject.adapter.ChannelAdapter;
import com.zjrb.zjxw.detailproject.ui.subject.adapter.SpecialAdapter;

import java.util.List;

/**
 * 专题悬浮助手
 * Created by wanglinjie.
 * create time:2017/8/27 15:34.
 */
public class OverlayHelper extends RecyclerView.OnScrollListener {

    private RecyclerView mRecyclerTabCopy;
    private FrameLayout mGroupCopy;
    private OverlayViewHolder mOverlayHolder;

    private int mOverlayPosition = RecyclerView.NO_POSITION;

    public OverlayHelper(RecyclerView recycler, RecyclerView recyclerCopy, FrameLayout groupCopy) {
        mRecyclerTabCopy = recyclerCopy;
        mGroupCopy = groupCopy;
        SpecialAdapter adapter = (SpecialAdapter) recycler.getAdapter();
        mOverlayHolder = adapter.onCreateOverlayViewHolder(recycler, 0);
        mGroupCopy.addView(mOverlayHolder.itemView);
        recycler.addOnScrollListener(this);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView.getAdapter() instanceof SpecialAdapter) {
            SpecialAdapter adapter = (SpecialAdapter) recyclerView.getAdapter();
            // 悬浮组名
            int overlayPosition = RecyclerView.NO_POSITION;
            int startPosition;
            LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = startPosition = lm.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = lm.findLastVisibleItemPosition();
            for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                if (recyclerView.findViewHolderForAdapterPosition(i) != null) {
                    int top = recyclerView.findViewHolderForAdapterPosition(i).itemView.getTop();
                    boolean visible = mRecyclerTabCopy.getVisibility() == View.VISIBLE;
                    if (top > (visible ? mRecyclerTabCopy.getHeight() : 0)) {
                        startPosition = i;
                        break;
                    }
                }
            }
            if (startPosition != RecyclerView.NO_POSITION) {
                List data = adapter.getData();
                if (data != null) {
                    int index = adapter.cleanPosition(startPosition);
                    while (--index >= 0) {
                        if (adapter.isOverlayViewType(index)) {
                            overlayPosition = index;
                            break;
                        }
                    }
                }
            }

            if (overlayPosition != RecyclerView.NO_POSITION) {
                mGroupCopy.setVisibility(View.VISIBLE);
                if (mOverlayPosition != overlayPosition) {
                    mOverlayPosition = overlayPosition;
                    Object data = adapter.getData(mOverlayPosition);
                    updateChannelTab(data);
                }
            } else {
                mGroupCopy.setVisibility(View.GONE);
                if (mOverlayPosition != RecyclerView.NO_POSITION) {
                    mOverlayPosition = RecyclerView.NO_POSITION;
                    updateChannelTab(null);
                }
            }
        }
    }

    public void updateChannelTab(Object data) {
        if (data != null) {
            mOverlayHolder.setData(data);
        }
        if (mRecyclerTabCopy.getAdapter() instanceof ChannelAdapter) {
            ChannelAdapter adapter = (ChannelAdapter) mRecyclerTabCopy.getAdapter();
            if (data instanceof SpecialGroupBean) {
                adapter.setSelectedData((SpecialGroupBean) data);
            } else {
                adapter.setSelectedData(null);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
