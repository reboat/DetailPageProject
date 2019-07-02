package com.zjrb.zjxw.detailproject.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: lujialei
 * @date: 2019/7/2
 * @describe:
 */


public class AnimationPriseView extends RelativeLayout {
    @BindView(R2.id.iv_not_prise)
    ImageView ivNotPrise;
    @BindView(R2.id.iv_prise)
    ImageView ivPrise;
    public interface OnTouchingListener{
        void onTouching(View view);
        void onNotPriseClick(View view);
        void onPrisedClick(View view);
    }
    private OnTouchingListener mOnTouchingListener;
    public void setOnTouchingListener(OnTouchingListener listener){
        this.mOnTouchingListener = listener;
    }
    Handler handler = new Handler();
    private Runnable sendGiftRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOnTouchingListener!=null){
                mOnTouchingListener.onTouching(AnimationPriseView.this);
                handler.postDelayed(this,100);
            }
        }
    };

    private boolean isPrised;

    public boolean isPrised() {
        return isPrised;
    }

    public void setPrised(boolean prised) {
        isPrised = prised;
        bindView();
    }

    private void bindView() {
        if (isPrised){
            AnimatorSet animatorSetsuofang = new AnimatorSet();//组合动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 1f);
            animatorSetsuofang.setDuration(300);
            animatorSetsuofang.setInterpolator(new OvershootInterpolator());
            animatorSetsuofang.play(scaleX).with(scaleY);//两个动画同时开始
            animatorSetsuofang.start();
            ivPrise.setVisibility(VISIBLE);
            ivNotPrise.setVisibility(GONE);
        }else {
            ivNotPrise.setVisibility(VISIBLE);
            ivPrise.setVisibility(GONE);
        }
    }

    public AnimationPriseView(Context context) {
        super(context);
        initView(context);
    }

    public AnimationPriseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnimationPriseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_detail_animation_prise_layout, this, true);
        ButterKnife.bind(this, view);
        ivNotPrise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnTouchingListener!=null){
                    mOnTouchingListener.onNotPriseClick(view);
                }
//                isPrised = true;
//                bindView();
            }
        });
        ivPrise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnTouchingListener!=null){
                    mOnTouchingListener.onPrisedClick(view);
                }
            }
        });

//        ivPrise.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP){
//                    handler.removeCallbacks(sendGiftRunnable);
//                    return true;
//                }
//                if(event.getAction() == MotionEvent.ACTION_DOWN){
//                    handler.post(sendGiftRunnable);
//                    return true;
//                }
//                return false;
//            }
//        });
    }



}
