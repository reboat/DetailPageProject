package com.zjrb.zjxw.detailproject.topic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.CommentRefreshBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.CommentListTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.topic.adapter.ActivityTopicAdapter;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题详情页
 * Created by wanglinjie.
 * create time:2017/9/13  上午9:08
 */

public class ActivityTopicActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        ActivityTopicAdapter.CommonOptCallBack, LoadMoreListener<CommentRefreshBean>, DetailCommentHolder.deleteCommentListener {
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ry_container)
    FrameLayout mContainer;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;

    private ActivityTopicAdapter adapter;
    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;

    /**
     * 加载更多
     */
    private FooterLoadMore more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOverly(true);
        setContentView(R.layout.module_detail_topic_activity);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        loadData();
    }


    /**
     * 处理上下移动监听
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchSlopHelper != null)
            mTouchSlopHelper.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 文章ID
     */
    private String mArticleId;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();

    }

    /**
     * head监听滚动 渐变
     */
    private void initView() {
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
    }

    /**
     * topbar
     */
    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        return topHolder.getView();
    }

    /**
     * 初始化/拉取数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null) return;

                mNewsDetail = draftDetailBean;
                fillData(draftDetailBean);
                initRectleView(!TextUtils.isEmpty(draftDetailBean.getArticle().getArticle_pic()) ? true : false);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //话题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    topHolder.getShareView().setVisibility(View.GONE);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId);
    }

    /**
     * @param isShowArticlePic 初始化recyleView滚动事件
     */
    private void initRectleView(boolean isShowArticlePic) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
        if (isShowArticlePic) {
            mRecyclerView.setLayoutParams(layoutParams);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });
        }
    }

    /**
     * 填充数据
     *
     * @param data
     */

    private void fillData(DraftDetailBean data) {

        // 记录阅读记录
        if (data != null && data.getArticle() != null) {
            DraftDetailBean.ArticleBean article = data.getArticle();
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }

        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //webview
        datas.add(data);

        adapter = new ActivityTopicAdapter(datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        more = new FooterLoadMore(mRecyclerView, this);
        adapter.setEmptyView(
                new EmptyPageHolder(mRecyclerView,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
        mRecyclerView.setAdapter(adapter);


        //是否可以点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //禁止评论，隐藏评论框及评论按钮
        if (data.getArticle().getComment_level() == 0) {
            mFyContainer.setVisibility(View.GONE);
            mMenuComment.setVisibility(View.GONE);
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            mFyContainer.setVisibility(View.VISIBLE);
            mMenuComment.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 底部评论框显示动画
     */
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * @param isUp 控制底部floorBar
     */
    @Override
    public void onTouchSlop(boolean isUp) {
        if (!isUp && mFloorBar.getVisibility() == View.GONE) {
            mFloorBar.setVisibility(View.VISIBLE);
        }
        int translationY = !isUp ? 0 : mFloorBar.getHeight() + getFloorBarMarginBottom();
        mFloorBar.animate().setInterpolator(mInterpolator)
                .setDuration(200)
                .translationY(translationY);
    }

    /**
     * @return 获取底部栏间距
     */
    private int getFloorBarMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = mFloorBar.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    /**
     * 订阅操作
     */
    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
                mNewsDetail.getArticle().setColumn_subscribed(true);
            }

            @Override
            public void onAfter() {
                adapter.updateSubscribeInfo();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShortNow(ActivityTopicActivity.this, errMsg);
            }

        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);

    }

    @Override
    public void onOptPageFinished() {
        adapter.setFooterLoadMore(more.getItemView());
        adapter.showAll();
    }

    /**
     * 进入栏目
     */
    @Override
    public void onOptClickColumn() {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("id", String.valueOf(mNewsDetail.getArticle().getColumn_id())).build().toString());
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
            T.showNow(this, getString(R.string.module_detail_you_have_liked), Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_failed));
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true);
    }

    private Bundle bundle;

    @OnClick({R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.menu_comment, R2.id.iv_top_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        //点赞
        if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null) {
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String.valueOf(mNewsDetail.getArticle().getId())))).show(getSupportFragmentManager(), "CommentWindowDialog");
            }
            //分享
        } else if (view.getId() == R.id.iv_top_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(mNewsDetail.getArticle().getFirstPic())
                    .setTextContent(mNewsDetail.getArticle().getSummary())
                    .setTitle(mNewsDetail.getArticle().getDoc_title())
                    .setTargetUrl(mNewsDetail.getArticle().getUrl()));
        } else if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ry_container, EmptyStateFragment.newInstance()).commit();
    }

    private long lastMinPublishTime;

    /**
     * 互动评论加载更多成功
     *
     * @param data
     * @param loadMore
     */
    @Override
    public void onLoadMoreSuccess(CommentRefreshBean data, LoadMore loadMore) {
        if (data != null && data.getComments() != null) {
            List<HotCommentsBean> commentList = data.getComments();
            if (commentList.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime();
            }
            adapter.addData(commentList, true);
            if (commentList.size() < C.PAGE_SIZE) {
                loadMore.setState(LoadMore.TYPE_NO_MORE);
            }

        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * 互动评论加载更多
     *
     * @param callback
     */
    @Override
    public void onLoadMore(LoadingCallBack<CommentRefreshBean> callback) {
        new CommentListTask(callback, true).setTag(this).exe(mArticleId, lastMinPublishTime);
    }

    /**
     * @return 获取最后一次刷新的时间戳
     */
    private Long getLastMinPublishTime() {
        int size = adapter.getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = adapter.getData(size - count++);
                if (data instanceof HotCommentsBean) {
                    return ((HotCommentsBean) data).getSort_number();
                }
            }
        }
        return null;
    }

    /**
     * 删除评论，局部刷新
     *
     * @param comment_id
     */
    @Override
    public void onDeleteComment(String comment_id,int position) {
        List list = adapter.getData();
        for (Object obj : list) {
            int count = 0;
            if (obj instanceof HotCommentsBean && ((HotCommentsBean) obj).getId().equals(comment_id)) {
                adapter.getData().remove(obj);
                adapter.notifyItemMoved(count, count);
//                adapter.notifyItemRangeChanged(count, adapter.getDataSize());
                break;
            }
            count++;
        }
    }
}
