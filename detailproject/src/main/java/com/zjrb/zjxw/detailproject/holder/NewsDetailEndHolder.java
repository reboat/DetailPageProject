package com.zjrb.zjxw.detailproject.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻详情页 - 底部 - ViewHolder
 *
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */
public class NewsDetailEndHolder extends BaseRecyclerViewHolder<Object> implements View
        .OnAttachStateChangeListener, View.OnTouchListener {

    @BindView(R2.id.tv_end)
    TextView mTvEnd;

    private final int MAX_HEIGHT = UIUtils.dip2px(150);

    public NewsDetailEndHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_end, parent, false));
        ButterKnife.bind(this, itemView);
        itemView.addOnAttachStateChangeListener(this);
    }

    @Override
    public void bindView() {
        itemView.setClickable(false);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        if (v == itemView) {
            if (v.getParent() instanceof RecyclerView) {
                ((RecyclerView) v.getParent()).setOnTouchListener(this);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (v == itemView) {
            if (v.getParent() instanceof RecyclerView) {
                ((RecyclerView) v.getParent()).setOnTouchListener(null);
            }
        }
    }

    private boolean isOnTouching, isTrigger;

    private int lastX, lastY;

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (itemView.getParent() == v) {
            int x = (int) e.getX();
            int y = (int) e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isOnTouching = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isOnTouching) {
                        // 已经触发 ／ 滑倒最底部，设置isTrigger = true
                        if (isTrigger || (isTrigger = !ViewCompat.canScrollHorizontally(v, 1))) {
                            if (Math.abs(lastY - y) > Math.abs(lastX - x)) { // 水平滑动大于横向
                                layoutItemView(lastY - y);
                            }
                        }
                    } else {
                        isOnTouching = true; // 此时没有经过Down事件，后期拦截过来的
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isOnTouching = isTrigger = false;
                    autoRebound();
                    break;
            }
            lastX = x;
            lastY = y;

        }

        return false;
    }

    /**
     * 布局itemView
     *
     * @param dy y轴更改的数值
     */
    private void layoutItemView(int dy) {
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        int height = dy + lp.height;
        if (height < 1) {
            height = 1;
        } else if (height > MAX_HEIGHT) {
            height = MAX_HEIGHT;
        }
        if (lp.height != height) {
            lp.height = height;
            itemView.requestLayout();
        }
    }

    /**
     * 自动回弹
     */
    private void autoRebound() {
        ValueAnimator objectAnimator = ObjectAnimator.ofInt(itemView.getHeight(), 1);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                itemView.getLayoutParams().height = (int) animation.getAnimatedValue();
                itemView.requestLayout();
            }
        });
        objectAnimator.start();
    }

}
