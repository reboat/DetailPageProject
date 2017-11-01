package com.zjrb.zjxw.detailproject.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import com.zjrb.core.common.base.page.PageItem;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 话题 - top
 *
 * @author a_liYa
 * @date 2017/10/31 19:14.
 */
public class HeaderTopicTop extends PageItem implements View.OnAttachStateChangeListener, View
        .OnLayoutChangeListener {

    @BindView(R2.id.iv_cover)
    ImageView mIvCover;

    private TopBarHolder mTopBarHolder;
    private OverlyHolder mInnerOverlyHolder;
    private OverlyHolder mOverlyHolder;
    private FloorBarHolder mFloorBarHolder;

    private RecyclerView mRecycler;
    private DraftDetailBean.ArticleBean mArticle;

    private RecyclerView.OnScrollListener mOnScrollListener
            = new RecyclerView.OnScrollListener() {

        float fraction = -1;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int maxRange = itemView.getHeight()
                    - mInnerOverlyHolder.getHeight() - mTopBarHolder.getHeight();
            float scale;
            if (maxRange > 0) {
                scale = (-1f * itemView.getTop()) / maxRange;
            } else {
                scale = 1;
            }
            if (scale < 0) {
                scale = 0;
            } else if (scale > 1) {
                scale = 1;
            }
            if (fraction != scale) {
                fraction = scale;
                mTopBarHolder.setFraction(fraction);
                mInnerOverlyHolder.setFraction(fraction);
                mOverlyHolder.setVisible(scale == 1);
                mFloorBarHolder.setVisible(scale == 1);
            }
        }

    };

    public HeaderTopicTop(RecyclerView parent) {
        super(parent, R.layout.module_detail_activity_top);
        ButterKnife.bind(this, itemView);
        mInnerOverlyHolder = new OverlyHolder(findViewById(R.id.layout_fixed));
        itemView.addOnAttachStateChangeListener(this);
        mRecycler = parent;
    }

    public void setData(DraftDetailBean data) {

        mTopBarHolder.setData(data);
        mOverlyHolder.setData(data);
        mInnerOverlyHolder.setData(data);

        if (data != null && data.getArticle() != null) {
            mArticle = data.getArticle();
            mTopBarHolder.itemView.removeOnLayoutChangeListener(this);
            if (TextUtils.isEmpty(mArticle.getArticle_pic())) {
                mIvCover.setVisibility(View.GONE);
                mTopBarHolder.itemView.addOnLayoutChangeListener(this);
                MarginLayoutParams lp = (MarginLayoutParams) mRecycler.getLayoutParams();
                lp.setMargins(0, mTopBarHolder.getHeight(), 0, 0);
                itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                mIvCover.setVisibility(View.VISIBLE);
                MarginLayoutParams lp = (MarginLayoutParams) mRecycler.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                itemView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                GlideApp.with(mIvCover)
                        .load(mArticle.getArticle_pic())
                        .apply(AppGlideOptions.bigOptions())
                        .into(mIvCover);
            }

        }
    }

    public void setTopBar(TopBarHolder topBarHolder) {
        mTopBarHolder = topBarHolder;
    }

    public void setOverlayHolder(OverlyHolder overlyHolder) {
        mOverlyHolder = overlyHolder;
    }

    public void setFloorBarHolder(FloorBarHolder floorBarHolder) {
        mFloorBarHolder = floorBarHolder;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        mRecycler.removeOnScrollListener(mOnScrollListener);
        mRecycler.addOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        mRecycler.removeOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int
            oldTop, int oldRight, int oldBottom) {
        MarginLayoutParams lp = (MarginLayoutParams) mRecycler.getLayoutParams();
        lp.setMargins(0, bottom - top, 0, 0);
    }

}
