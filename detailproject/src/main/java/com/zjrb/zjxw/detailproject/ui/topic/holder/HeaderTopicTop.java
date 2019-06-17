package com.zjrb.zjxw.detailproject.ui.topic.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.PageItem;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;

/**
 * 话题 - top
 * Created by wanglinjie.
 * create time:2018/1/11 19:14.
 */
public class HeaderTopicTop extends PageItem implements View.OnAttachStateChangeListener, View
        .OnLayoutChangeListener {

    @BindView(R2.id.iv_cover)
    ImageView mIvCover; // 话题封面图

    private TopBarHolder mTopBarHolder;
    private OverlyHolder mCoverOverlyHolder; // 封面
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
                    - mCoverOverlyHolder.getHeight() - mTopBarHolder.getHeight();
            float scale;
            if (maxRange > 0) {
                scale = (-1f * itemView.getTop()) / maxRange;
            } else {
                scale = 1;
            }
            scale = Math.min(1, Math.max(0, scale));

            if (fraction != scale) {
                fraction = scale;
                mTopBarHolder.setFraction(fraction);
                mCoverOverlyHolder.setFraction(fraction);
                mOverlyHolder.setVisible(scale == 1);
                float limitScale = mFloorBarHolder.getRangeHeight() / recyclerView.getHeight();
                mFloorBarHolder.setCanVisible(scale > limitScale);
            }
        }

    };

    public HeaderTopicTop(RecyclerView parent) {
        super(parent, R.layout.module_detail_activity_top);
        ButterKnife.bind(this, itemView);
        mCoverOverlyHolder = new OverlyHolder(findViewById(R.id.layout_fixed));
        itemView.addOnAttachStateChangeListener(this);
        mRecycler = parent;
    }

    public void setData(DraftDetailBean data) {

        mTopBarHolder.setData(data);
        mOverlyHolder.setData(data);
        mCoverOverlyHolder.setData(data);

        if (data != null && data.getArticle() != null) {
            mArticle = data.getArticle();
            mTopBarHolder.itemView.removeOnLayoutChangeListener(this);
            if (TextUtils.isEmpty(mArticle.getArticle_pic())) {
                mIvCover.setVisibility(View.GONE);
                mTopBarHolder.itemView.addOnLayoutChangeListener(this);
                MarginLayoutParams lp = (MarginLayoutParams) mRecycler.getLayoutParams();
                lp.setMargins(0, mTopBarHolder.getHeight(), 0, 0);
                itemView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                mFloorBarHolder.setVisible(true);
            } else {
                mFloorBarHolder.setVisible(false);
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
