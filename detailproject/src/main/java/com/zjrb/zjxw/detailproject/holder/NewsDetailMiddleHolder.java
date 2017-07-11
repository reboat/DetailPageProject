package com.zjrb.zjxw.detailproject.holder;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zjrb.coreprojectlibrary.api.base.APIError;
import com.zjrb.coreprojectlibrary.common.biz.UserBiz;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.coreprojectlibrary.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻详情页中间内容
 *
 * @author a_liYa
 * @date 2017/5/15 21:07.
 */
public class NewsDetailMiddleHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements
        IOnItemClickListener<AuthorEntity>, View.OnAttachStateChangeListener,
        NewsDetailAdapter.IBindSubscribe, NewsDetailAdapter.IBindFabulous {

    @BindView(R2.id.iv_integral)
    ImageView mIvIntegral;
    @BindView(R2.id.tv_integral)
    TextView mTvIntegral;
    @BindView(R2.id.iv_fabulous)
    ImageView mIvFabulous;
    @BindView(R2.id.tv_column_name)
    TextView mTvColumnName;
    @BindView(R2.id.tv_subscribe)
    TextView mTvSubscribe;
    @BindView(R2.id.rv_author)
    RecyclerView mRvAuthor;
    @BindView(R2.id.ll_author)
    LinearLayout mLlAuthor;
    @BindView(R2.id.tv_related)
    TextView mTvRelated;
    @BindView(R2.id.ll_login)
    LinearLayout mLlLogin;

    private AuthorAdapter mAuthorAdapter;

    public NewsDetailMiddleHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_layout_middle, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        if (UserBiz.get().isLogin() && mData.isFinished()) { // 已经读完文章
            BizUtils.setDepthIntegralText(mTvIntegral, mData.getReadTotalPoints());
        } else {
            mTvIntegral.setVisibility(View.GONE);
            mLlLogin.setVisibility(View.VISIBLE);
            itemView.removeOnAttachStateChangeListener(this);
            itemView.addOnAttachStateChangeListener(this);
            BizUtils.setDepthIntegralText(mTvIntegral,
                    mData.getPoints() + mData.getFinishedPoints());
        }
        itemView.setClickable(false);
        mTvColumnName.setText(mData.getColumnName());

        bindSubscribe();
        bindFabulous();

        if (mData.getAuthorList() == null || mData.getAuthorList().isEmpty()) { // 没有作者
            if (mLlAuthor.getVisibility() != View.GONE) {
                mLlAuthor.setVisibility(View.GONE);
            }
        } else {
            if (mLlAuthor.getVisibility() != View.VISIBLE) {
                mLlAuthor.setVisibility(View.VISIBLE);
            }
            if (mAuthorAdapter == null) {
                mAuthorAdapter = new AuthorAdapter(mData.getAuthorList());
                mAuthorAdapter.setOnItemClickListener(this);
            }
            mRvAuthor.setLayoutManager(new LinearLayoutManager(
                    itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mRvAuthor.setAdapter(mAuthorAdapter);
        }

        if (mData.getRecommendedReading() == null || mData.getRecommendedReading().isEmpty()) {
            mTvRelated.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(View view, int position, AuthorEntity data) {
        if (ClickTracker.isDoubleClick()) return;
        // 作者条目点击
        MobclickAgent.onEvent(view.getContext(), "ARTICAL_DETAIL_AUTHOR");
        view.getContext().startActivity(
                PersonalActivity.getIntent(data.getUserId()));
    }

    @OnClick({R2.id.iv_fabulous, R2.id.tv_subscribe, R2.id.tv_column_name,
            R2.id.btn_login})
    public void onViewClicked(View view) {
        NewsDetailAdapter.CommonOptCallBack callback;
        if (itemView.getContext() instanceof NewsDetailAdapter.CommonOptCallBack) {
            callback = (NewsDetailAdapter.CommonOptCallBack) itemView.getContext();
            switch (view.getId()) {
                case R2.id.iv_fabulous:
                    if (mIvFabulous.isSelected()) {
                        mIvFabulous.setSelected(true);
                        T.showNow(itemView.getContext(), "您已点赞", Toast.LENGTH_SHORT);
                        return;
                    } else {
                        callback.onOptFabulous();
                    }
                    break;
                case R2.id.tv_subscribe:
                    if (mData.isSubscribed()) {
                        callback.onOptCancelSubscribe();
                    } else {
                        callback.onOptSubscribe();
                    }
                    break;
                case R2.id.tv_column_name:
                    callback.onOptClickColumn();
                    break;
                case R2.id.btn_login:
                    deepRead();
                    break;
            }
        }
    }

    /**
     * 深读
     */
    private void deepRead() {
        final long timeMillis = System.currentTimeMillis();
        new DraftReadedTask(new APIExpandCallBack<PointsEntity>() {
            @Override
            public void onSuccess(final PointsEntity points) {
                if (points.getPoints() > 0) {
                    long time = System.currentTimeMillis() - timeMillis;
                    if (time >= 600) {
                        showShortToast("恭喜获取" + points.getPoints() + "个积分");
                    } else {
                        UIUtils.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showShortToast("恭喜获取" + points.getPoints() + "个积分");
                            }
                        }, 600 - time);
                    }
                }
                if (mData.getPoints() == 0) {
                    BizUtils.setDepthIntegralText(
                            mTvIntegral, mData.getReadTotalPoints() + points.getPoints());
                } else {
                    BizUtils.setDepthIntegralText(
                            mTvIntegral, mData.getPoints() + points.getPoints());
                }
                mTvIntegral.setVisibility(View.VISIBLE);
                mLlLogin.setVisibility(View.GONE);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                if (APIError.code.UNLOGIN == errCode) {
                    mLlLogin.setVisibility(View.VISIBLE);
                    mTvIntegral.setVisibility(View.GONE);
                }
            }
        }).setTag(this).exe(mData.getId());
    }

    /**
     * 跳出深度积分动画
     */
    private void jumpDeepAnim() {
        ObjectAnimator jumpUp = ObjectAnimator.ofFloat(mIvIntegral, View.TRANSLATION_Y, 0,
                -1.8f * mIvIntegral.getHeight());
        ObjectAnimator jumpDown = ObjectAnimator.ofFloat(mIvIntegral, View.TRANSLATION_Y,
                -1.8f * mIvIntegral.getHeight(), 0);
        jumpUp.setDuration(500);
        jumpUp.setInterpolator(new DecelerateInterpolator());
        jumpDown.setDuration(800);
        jumpDown.setInterpolator(new BounceInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(jumpUp).before(jumpDown);
        set.start();
    }

    public void showShortToast(CharSequence message) {
        T.showShort(itemView.getContext(), message);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        if (v == itemView) {
            if (v.getParent() instanceof RecyclerView) {
                ((RecyclerView) v.getParent()).addOnScrollListener(mScrollListener);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        if (v == itemView) {
            if (v.getParent() instanceof RecyclerView) {
                ((RecyclerView) v.getParent()).removeOnScrollListener(mScrollListener);
            }
        }
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) { // 处理深读任务
                if (itemView.getTop() + mIvIntegral.getTop() < recyclerView.getBottom()) {
                    itemView.removeOnAttachStateChangeListener(NewsDetailMiddleHolder.this);
                    recyclerView.removeOnScrollListener(mScrollListener);
                    if (UserBiz.get().isLogin()) {
                        jumpDeepAnim();
                        mData.setFinished(1);
                        deepRead();
                    }
                }
            }
        }
    };

    @Override
    public void bindSubscribe() {
        mTvSubscribe.setText(mData.isSubscribed() ? "已订阅" : "订阅");
    }

    @Override
    public void bindFabulous() {
        mIvFabulous.setSelected(mData.isPraised());
    }

    /**
     * 作者Adapter
     *
     * @author a_liYa
     * @date 2017/5/16 下午2:35.
     */
    class AuthorAdapter extends BaseRecyclerAdapter<AuthorEntity, AuthorViewHolder> {

        public AuthorAdapter(List<AuthorEntity> datas) {
            super(datas);
        }

        @Override
        public AuthorViewHolder onAbsCreateViewHolder(ViewGroup parent, int viewType) {
            return new AuthorViewHolder(parent);
        }
    }

}