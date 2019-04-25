package com.zjrb.zjxw.detailproject.ui.subject.holder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliya.uimode.widget.MaskImageView;
import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.recycleView.PageItem;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.GridSpaceDivider;
import com.zjrb.core.utils.L;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.ui.subject.adapter.ChannelAdapter;
import com.zjrb.zjxw.detailproject.ui.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.utils.ArgbUtils;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.glide.AppGlideOptions;
import cn.daily.news.biz.core.glide.PH;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 专题详情页专用头部
 * Created by wanglinjie.
 * create time:2017/8/27 下午2:31.
 */
public class HeaderSpecialHolder extends PageItem implements OnItemClickListener, View
        .OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    //背景由全透明变成白色
    private static final int BC_START = Color.parseColor("#00ffffff");
    private static final int ATTR_BC_END = R.color._ffffff;

    //ToolsBar颜色全透明变成白色
    private static final int ATTR_TTC_START = R.color._00ffffff;
    private static final int ATTR_TTC_END = R.color._ffffff;

    @BindView(R2.id.iv_subject)
    MaskImageView ivSubject;
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
    @BindView(R2.id.tv_read)
    TextView tvRead;

    private boolean isOpen;
    //将标签列表进行复制显示
    private RecyclerView mRecyclerTabCopy;
    //聚合阅读
    private TextView mTvReadCopy;
    //标题
    private FrameLayout fyContainer;
    //返回键、收藏、分享
    private ImageView ivback, ivCollect, ivShare;
    private FrameLayout mGroupCopy;
    private ChannelAdapter mChannelAdapter;
    private OnClickChannelListener mOnClickChannelListener;
    private DraftDetailBean.ArticleBean mArticle;

    public static final int MAX_DEFAULT_LINES = 3;

    //头部动效
    public HeaderSpecialHolder(RecyclerView parent, TextView tvReadCopy, RecyclerView copy, FrameLayout view, FrameLayout groupCopy, OnClickChannelListener
            listener) {
        super(parent, R.layout.module_detail_special_header);
        ButterKnife.bind(this, itemView);
        mTvReadCopy = tvReadCopy;
        mGroupCopy = groupCopy;
        mRecyclerTabCopy = copy;
        fyContainer = view;
        ivback = fyContainer.findViewById(R.id.iv_top_bar_back);
        ivCollect = fyContainer.findViewById(R.id.iv_top_collect);
        ivShare = fyContainer.findViewById(R.id.iv_top_share);
        mOnClickChannelListener = listener;
        initView();
        //在群众之声之前隐藏
        parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            float fraction = -1;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int overlayEndPosition = showOrHideOverlay(recyclerView);
                int maxRange = itemView.getHeight()
                        - ivSubject.getHeight() - fyContainer.getHeight();
                float scale;
                if (maxRange > 0) {
                    scale = (-1f * itemView.getTop()) / maxRange;
                } else {
                    scale = 1;
                }
                scale = Math.min(1, Math.max(0, scale));
                if (fraction != scale) {
                    fraction = scale;
                    setFraction(fraction);
                }

                //吸顶显示时机
                if (mRecyclerTab.getTop() + itemView.getTop() + dy - fyContainer.getBottom() > 0
                        && itemView.getRootView() != itemView) {

                    if (mTvReadCopy.getVisibility() != View.GONE) {
                        mTvReadCopy.setVisibility(View.GONE);
                    }
                    if (mRecyclerTabCopy.getVisibility() != View.GONE) {
                        mRecyclerTabCopy.setVisibility(View.GONE);
                        ivback.setImageResource(R.mipmap.module_biz_write_back);
                        ivCollect.setImageResource(R.drawable.module_biz_ic_special_collect);
                        ivShare.setImageResource(R.mipmap.module_biz_atlas_share);
                    }
                } else {
                    //需要在群众之声消失
                    if (mChannelAdapter != null && mChannelAdapter.getDataSize() > 1) {
                        if (overlayEndPosition != RecyclerView.NO_POSITION) {
                            if(mRecyclerTabCopy.getVisibility() == View.VISIBLE){
                                mRecyclerTabCopy.setVisibility(View.GONE);
                            }
                            if(mTvReadCopy.getVisibility() == View.VISIBLE){
                                mTvReadCopy.setVisibility(View.GONE);
                            }
                        } else {
                            if(mRecyclerTabCopy.getVisibility() == View.GONE){
                                mRecyclerTabCopy.setVisibility(View.VISIBLE);
                            }
                            if(mTvReadCopy.getVisibility() == View.GONE){
                                mTvReadCopy.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    if (mGroupCopy.getVisibility() == View.VISIBLE) {
                        ivback.setImageResource(R.mipmap.module_biz_top_bar_back);
                        ivCollect.setImageResource(R.drawable.module_biz_ic_special_collect_anim);
                        ivShare.setImageResource(R.mipmap.module_biz_topbar_share);
                    } else {
                        ivback.setImageResource(R.mipmap.module_biz_write_back);
                        ivCollect.setImageResource(R.drawable.module_biz_ic_special_collect);
                        ivShare.setImageResource(R.mipmap.module_biz_atlas_share);
                    }
                }
            }
        });
    }

    //显示隐藏时机
    private int showOrHideOverlay(RecyclerView recyclerView) {
        if (recyclerView.getAdapter() instanceof SpecialAdapter) {
            SpecialAdapter adapter = (SpecialAdapter) recyclerView.getAdapter();
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
                        if (adapter.isVoiceOfMassType(index)) {
                            overlayEndPosition = index;
                            break;
                        }
                    }
                }
            }
            //群众之声位置
            return overlayEndPosition;
        } else {
            return RecyclerView.NO_POSITION;
        }
    }

    private void initView() {
        mRecyclerTab.addItemDecoration(new GridSpaceDivider(8));
        mRecyclerTabCopy.addItemDecoration(new GridSpaceDivider(8));

        mRecyclerTab.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));
        mRecyclerTabCopy.setLayoutManager(new GridLayoutManager(itemView.getContext(), 4));

        tvSummary.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mLayoutIndicator.setOnClickListener(this);
        mLayoutFocus.setOnClickListener(this);
    }

    //设置背景和toolsbar颜色渐变
    public void setFraction(float fraction) {
        GradientDrawable bg;
        if (itemView.getBackground() instanceof GradientDrawable) {
            bg = (GradientDrawable) itemView.getBackground();
            bg.mutate();
        } else {
            bg = new GradientDrawable();
            bg.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        }
        bg.setColors(new int[]{
                ArgbUtils.evaluate(fraction,
                        Color.TRANSPARENT, ContextCompat.getColor(itemView.getContext(), ATTR_BC_END)),
                ArgbUtils.evaluate(fraction,
                        BC_START, ContextCompat.getColor(itemView.getContext(), ATTR_BC_END))});
        // 背景
        itemView.setBackground(bg);

        // ToolBar渐变
        fyContainer.setBackgroundColor(ArgbUtils.evaluate(fraction,
                ContextCompat.getColor(fyContainer.getContext(), ATTR_TTC_START),
                ContextCompat.getColor(fyContainer.getContext(), ATTR_TTC_END)));
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

        //分组标签为1个时，不显示
        if (mChannelAdapter == null || mChannelAdapter.getDataSize() < 2) {
            mRecyclerTab.setVisibility(View.GONE);
            tvRead.setVisibility(View.GONE);
            mRecyclerTabCopy.setVisibility(View.GONE);
            mTvReadCopy.setVisibility(View.GONE);
        } else {
            mRecyclerTabCopy.setVisibility(View.INVISIBLE);
            mTvReadCopy.setVisibility(View.INVISIBLE);
        }

        //题图可以为空
        if (TextUtils.isEmpty(mArticle.getArticle_pic())) {
            ivSubject.setVisibility(View.GONE);
        } else {
            ivSubject.setVisibility(View.VISIBLE);
            GlideApp.with(ivSubject).
                    load(mArticle.getArticle_pic())
                    .apply(AppGlideOptions.bigOptions())
                    .into(ivSubject);
        }

        //标题不能为空
        tvTitle.setText(mArticle.getDoc_title());

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
            if (ivTopicPic.getContext() instanceof Activity) {
                if (((Activity) ivTopicPic.getContext()).isDestroyed()) {
                    return;
                }
            }
            GlideApp.with(ivTopicPic).load(mArticle.getSubject_focus_image())
                    .placeholder(PH.zheBig())
                    .error(PH.zheBig())
                    .centerCrop()
                    .apply(AppGlideOptions.bigOptions())
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
            //专题详情页焦点图点击
        } else if (v.getId() == R.id.layout_focus) {
            if (mArticle != null && !TextUtils.isEmpty(mArticle.getSubject_focus_url())) {
                DataAnalyticsUtils.get().SpecialFocusImgClick(mArticle);
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
