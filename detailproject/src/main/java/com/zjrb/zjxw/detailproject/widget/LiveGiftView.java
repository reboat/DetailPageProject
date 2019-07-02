package com.zjrb.zjxw.detailproject.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zjrb.zjxw.detailproject.R;

/**
 * Date: 2018/8/27 下午3:49
 * Email: sisq@8531.cn
 * Author: sishuqun
 * Description: 直播刷礼物
 */
public class LiveGiftView extends RelativeLayout{


    private int mWidth, mHeight;
    private Drawable[] drawables = new Drawable[5];
    LayoutParams layoutParams;
    public static final int GIFT_SIZE = 60;

    public LiveGiftView(Context context) {
        super(context);
        init();
    }

    public LiveGiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        drawables[0] = ContextCompat.getDrawable(getContext(), R.mipmap.module_biz_news_prise_click);
        drawables[1] = ContextCompat.getDrawable(getContext(), R.mipmap.module_biz_news_prise_click);
        drawables[2] = ContextCompat.getDrawable(getContext(), R.mipmap.module_biz_news_prise_click);
        drawables[3] = ContextCompat.getDrawable(getContext(), R.mipmap.module_biz_news_prise_click);
        drawables[4] = ContextCompat.getDrawable(getContext(), R.mipmap.module_biz_news_prise_click);
        layoutParams = new LayoutParams(GIFT_SIZE, GIFT_SIZE);
        layoutParams.addRule(CENTER_HORIZONTAL);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 点击点赞时,添加点赞图片
     */
    public void addGiftView() {
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(drawables[(int) (Math.random() * drawables.length)]);
            imageView.setLayoutParams(layoutParams);
            addView(imageView);

            // 放大动画
            startScaleAnim(imageView);
            // 贝塞尔曲线伴随透明度动画
            startBezierAnim(imageView);
        }
    }

    /**
     * 放大动画
     * @param imageView
     */
    private void startScaleAnim(ImageView imageView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0.2f, 1.0f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setInterpolator(new LinearInterpolator());
        set.playTogether(scaleX, scaleY);
        set.setTarget(imageView);
        set.start();
    }

    private void startBezierAnim(final ImageView imageView) {
        PointF startPoint = new PointF(mWidth / 2-GIFT_SIZE/2, mHeight-GIFT_SIZE/2);
        PointF endPoint = new PointF((float) (Math.random() * mWidth * 2 / 3), (float) (Math.random() * 50));

        ValueAnimator animator = ValueAnimator.ofObject(new GiftEvaluator(getPointF(), getPointF()), startPoint, endPoint);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                imageView.setX(pointF.x);
                imageView.setY(pointF.y);
                imageView.setAlpha(1 - valueAnimator.getAnimatedFraction());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LiveGiftView.this.removeView(imageView);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }



    /**
     * 生成随机控制点
     * @return
     */
    private PointF getPointF() {
        PointF pointF = new PointF();
        pointF.x = (float) (Math.random() * mWidth);
        pointF.y = (float) (Math.random() * mHeight * 3 / 4);
        return pointF;
    }


    public class GiftEvaluator implements TypeEvaluator<PointF> {
        PointF control1, control2;

        public GiftEvaluator(PointF control1, PointF control2) {
            this.control1 = control1;
            this.control2 = control2;
        }

        @Override
        public PointF evaluate(float t, PointF startValue, PointF  endValue) {
            int x = (int) (startValue.x * Math.pow((1 - t), 3) + 3 * control1.x * t * Math.pow((1 - t), 2) + 3 *
                    control2.x * Math.pow(t, 2) * (1 - t) + endValue.x * Math.pow(t, 3));
            int y = (int) (startValue.y * Math.pow((1 - t), 3) + 3 * control1.y * t * Math.pow((1 - t), 2) + 3 *
                    control2.y * Math.pow(t, 2) * (1 - t) + endValue.y * Math.pow(t, 3));
            return new PointF(x, y);
        }
    }

}
