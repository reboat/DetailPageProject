package com.zjrb.zjxw.detailproject.subject.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.PageItem;
import com.zjrb.core.common.glide.AppGlideOptions;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.GridSpaceDivider;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.subject.adapter.ChannelAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专题详情页 - header
 *
 * @author a_liYa
 * @date 2017/10/11 下午2:31.
 */
public class HeaderSpecialHolder extends PageItem implements OnItemClickListener, View
        .OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R2.id.iv_subject)
    ImageView ivSubject;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_summary)
    TextView tvSummary;
    @BindView(R2.id.tv_indicator)
    TextView mTvIndicator;
    @BindView(R2.id.recycler_tab)
    RecyclerView mRecyclerTab;
    @BindView(R2.id.iv_focus)
    ImageView ivTopicPic;
    @BindView(R2.id.tv_focus)
    TextView tvTitle2;
    @BindView(R2.id.layout_focus)
    FrameLayout mLayoutFocus;
    @BindView(R2.id.layout_indicator)
    LinearLayout mLayoutIndicator;
    @BindView(R2.id.iv_indicator)
    ImageView mIvIndicator;

    private boolean isOpen;
    private RecyclerView mRecyclerTabCopy;

    private ChannelAdapter mChannelAdapter;
    private OnClickChannelListener mOnClickChannelListener;

    private DraftDetailBean.ArticleBean mArticle;

    public static final int MAX_DEFAULT_LINES = 3;

    public HeaderSpecialHolder(RecyclerView parent, RecyclerView copy,
                               OnClickChannelListener listener) {
        super(parent, R.layout.module_detail_special_header);
        ButterKnife.bind(this, itemView);
        mRecyclerTabCopy = copy;
        mOnClickChannelListener = listener;
        initView();
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mRecyclerTab.getTop() + itemView.getTop() > 0
                        && itemView.getRootView() != itemView) {
                    mRecyclerTabCopy.setVisibility(View.GONE);
                } else {
                    if (mChannelAdapter != null && mChannelAdapter.getDataSize() > 1)
                        mRecyclerTabCopy.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        mRecyclerTab.addItemDecoration(new GridSpaceDivider(12));
        mRecyclerTabCopy.addItemDecoration(new GridSpaceDivider(12));

        mRecyclerTab.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));
        mRecyclerTabCopy.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));

        tvSummary.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mLayoutIndicator.setOnClickListener(this);
        mLayoutFocus.setOnClickListener(this);
    }

    @NonNull
    private String getString(int resId) {
        return itemView.getContext().getString(resId);
    }

    public void setData(DraftDetailBean data) {
        if (data == null || data.getArticle() == null) {
            return;
        }
        mArticle = data.getArticle();

        mChannelAdapter = new ChannelAdapter(mArticle.getSubject_groups());
        mChannelAdapter.setOnItemClickListener(this);
        mRecyclerTab.setAdapter(mChannelAdapter);
        mRecyclerTabCopy.setAdapter(mChannelAdapter);

        if (mChannelAdapter == null || mChannelAdapter.getDataSize() < 2) {
            mRecyclerTab.setVisibility(View.GONE);
            mRecyclerTabCopy.setVisibility(View.GONE);
        }

        //题图可以为空
        GlideApp.with(ivSubject).
                load(mArticle.getSubject_pic())
                .apply(AppGlideOptions.bigOptions())
                .into(ivSubject);

        //标题不能为空
        tvTitle.setText(mArticle.getList_title());

        //摘要可以为空
        if (TextUtils.isEmpty(mArticle.getSummary())) {
            tvSummary.setVisibility(View.GONE);
        } else {
            tvSummary.setVisibility(View.VISIBLE);
            tvSummary.setText(mArticle.getSummary());
        }

        //专题焦点图可以为空
        if (TextUtils.isEmpty(mArticle.getSubject_focus_image())) {
            mLayoutFocus.setVisibility(View.GONE);
        } else {
            GlideApp.with(ivTopicPic).load(mArticle.getSubject_focus_image())
                    .placeholder(PH.zheBig())
                    .error(PH.zheBig())
                    .centerCrop()
                    .into(ivTopicPic);

            //专题焦点图摘要可以为空
            if (TextUtils.isEmpty(mArticle.getSubject_focus_description())) {
                tvTitle2.setVisibility(View.GONE);
            } else {
                tvTitle2.setVisibility(View.VISIBLE);
                tvTitle2.setText(mArticle.getSubject_focus_description());
            }
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (mOnClickChannelListener != null) {
            mOnClickChannelListener.onClickChannel(mChannelAdapter.getData(position));
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;

        if (v.getId() == R.id.layout_indicator) {
            if (isOpen = !isOpen) {
                tvSummary.setMaxLines(Integer.MAX_VALUE);
            } else {
                tvSummary.setMaxLines(MAX_DEFAULT_LINES);
            }
        } else if (v.getId() == R.id.layout_focus) {
            if (!TextUtils.isEmpty(mArticle.getSubject_focus_url())) {
                Nav.with(v.getContext()).to(mArticle.getSubject_focus_url());
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        if (tvSummary.getLineCount() > MAX_DEFAULT_LINES) {
            if (mLayoutIndicator.getVisibility() == View.GONE) {
                mLayoutIndicator.setVisibility(View.VISIBLE);
                tvSummary.setMaxLines(MAX_DEFAULT_LINES);
            }
        }
        if (mLayoutIndicator.getVisibility() == View.VISIBLE) {
            if (tvSummary.getLineCount() > MAX_DEFAULT_LINES) {
                mTvIndicator.setText(getString(R.string.module_detail_text_up));
                mIvIndicator.setSelected(true);
            } else {
                mTvIndicator.setText(getString(R.string.module_detail_text_down));
                mIvIndicator.setSelected(false);
            }
        }
    }

    public interface OnClickChannelListener {

        void onClickChannel(SpecialGroupBean bean);

    }

}
