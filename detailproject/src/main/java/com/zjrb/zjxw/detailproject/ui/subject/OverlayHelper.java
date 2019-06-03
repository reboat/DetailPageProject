package com.zjrb.zjxw.detailproject.ui.subject;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.zjrb.core.recycleView.OverlayViewHolder;
import com.zjrb.core.utils.L;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
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

    private SpecialGroupBean bean;
    private DraftDetailBean.ArticleBean articleBean;

    public void setSpecialGroupBean(SpecialGroupBean bean) {
        this.bean = bean;
    }

    public void setArticDetail(DraftDetailBean.ArticleBean articleBean) {
        this.articleBean = articleBean;
    }

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
            // 悬浮组名位置
            int overlayPosition = RecyclerView.NO_POSITION;
            //去除悬浮
            int overlayEndPosition = RecyclerView.NO_POSITION;
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

            //专题tab显示时机
            if (startPosition != RecyclerView.NO_POSITION) {
                List data = adapter.getData();
                if (data != null) {
                    int index = adapter.cleanPosition(startPosition);
                    while (--index >= 0) {
                        if (adapter.isOverlayViewType(index)) {
                            overlayPosition = index;
                            break;
                        } else if (adapter.isVoiceOfMassType(index)) {
                            overlayEndPosition = index;
                            break;
                        }
                    }
                }
            }

            //专题tab显示的位置
            if (overlayPosition != RecyclerView.NO_POSITION) {
                if (mGroupCopy.getVisibility() == View.GONE) {
                    mGroupCopy.setVisibility(View.VISIBLE);
                }
                if (bean != null && bean.isClickChannel()) {
                    Object data;
                    data = bean;
                    bean.setClickChannel(false);
                    updateChannelTab(data);
                    return;
                }

                if (mOverlayPosition != overlayPosition) {
                    mOverlayPosition = overlayPosition;
                    Object data;
                    if (bean != null && bean.isClickChannel()) {
                        data = bean;
                        bean.setClickChannel(false);
                    } else {
                        if (articleBean.getSubject_groups().size() == 1) {
                            data = adapter.getData(mOverlayPosition);
                        } else {
                            data = adapter.getData(mOverlayPosition);
                        }
                    }
                    updateChannelTab(data);
                }
                //专题tab滑动到群众之声时隐藏
            } else if (overlayEndPosition != RecyclerView.NO_POSITION) {
                if (mGroupCopy.getVisibility() == View.VISIBLE) {
                    mGroupCopy.setVisibility(View.GONE);
                }
            } else {
                if (bean != null && bean.isClickChannel()) {
                    if (firstVisibleItemPosition == 0) {
                        if (mGroupCopy.getVisibility() == View.GONE) {
                            mGroupCopy.setVisibility(View.VISIBLE);
                        }
                        bean.setClickChannel(false);
                        Object data = bean;
                        updateChannelTab(data);
                    }
                } else {
                    if (mGroupCopy.getVisibility() == View.VISIBLE) {
                        mGroupCopy.setVisibility(View.GONE);
                    }
                    if (mOverlayPosition != RecyclerView.NO_POSITION) {
                        mOverlayPosition = RecyclerView.NO_POSITION;
                        updateChannelTab(null);
                    }
                }

            }
        }
    }

    public void updateChannelTab(Object data) {
        if (data != null && data instanceof SpecialGroupBean) {
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
