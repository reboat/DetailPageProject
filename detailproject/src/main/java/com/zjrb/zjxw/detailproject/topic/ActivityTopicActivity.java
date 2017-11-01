package com.zjrb.zjxw.detailproject.topic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
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
import com.zjrb.zjxw.detailproject.topic.adapter.TopicAdapter;
import com.zjrb.zjxw.detailproject.topic.holder.FloorBarHolder;
import com.zjrb.zjxw.detailproject.topic.holder.HeaderTopicTop;
import com.zjrb.zjxw.detailproject.topic.holder.OverlyHolder;
import com.zjrb.zjxw.detailproject.topic.holder.TopBarHolder;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 话题详情页
 * Created by wanglinjie.
 * create time:2017/9/13  上午9:08
 */
public class ActivityTopicActivity extends BaseActivity implements
        TopicAdapter.CommonOptCallBack, LoadMoreListener<CommentRefreshBean>,
        DetailCommentHolder.deleteCommentListener {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;

    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ry_container)
    FrameLayout mContainer;
    @BindView(R2.id.ly_bottom_comment)
    RelativeLayout mFloorBar;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;

    @BindView(R2.id.top_bar)
    FrameLayout mTopBar;

    private TopicAdapter mAdapter;

    // 加载更多
    private FooterLoadMore mLoadMore;
    private HeaderTopicTop mTopicTop;

    // top bar
    private TopBarHolder mTopBarHolder;
    // overlay
    private OverlyHolder mOverlyHolder;
    private FloorBarHolder mFloorBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (mFloorBarHolder != null) mFloorBarHolder.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 文章ID
     */
    private String mArticleId;
    private DraftDetailBean mDetailData;

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

    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        mTopBarHolder = new TopBarHolder(mTopBar);
        mOverlyHolder = new OverlyHolder(findViewById(R.id.layout_fixed));
        mFloorBarHolder = new FloorBarHolder(mFloorBar);

        mLoadMore = new FooterLoadMore(mRecycler, this);
        mTopicTop = new HeaderTopicTop(mRecycler);

        mTopicTop.setTopBar(mTopBarHolder);
        mTopicTop.setOverlayHolder(mOverlyHolder);
        mTopicTop.setFloorBarHolder(mFloorBarHolder);
    }

    @Override
    public boolean isShowTopBar() {
        return false;
    }

    /**
     * 初始化/拉取数据
     */
    private void loadData() {
        mTopBarHolder.setShareVisible(false);
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean data) {
                if (data == null) return;
                mTopBarHolder.setShareVisible(true);
                fillData(data);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //话题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId);
    }

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

        mDetailData = data;

        if (mAdapter == null) {
            mAdapter = new TopicAdapter(data);
            mAdapter.addHeaderView(mTopicTop.itemView);
            mAdapter.setEmptyView(
                    new EmptyPageHolder(mRecycler,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                    ).itemView);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
        }

        mTopicTop.setData(data);
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
     * 订阅操作
     */
    @Override
    public void onOptSubscribe() {
        new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
                mDetailData.getArticle().setColumn_subscribed(true);
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShortNow(ActivityTopicActivity.this, errMsg);
            }

        }).setTag(this).exe(mDetailData.getArticle().getColumn_id(), true);

    }

    @Override
    public void onOptPageFinished() {
        mAdapter.setFooterLoadMore(mLoadMore.getItemView());
        mAdapter.showAll();
    }

    /**
     * 进入栏目
     */
    @Override
    public void onOptClickColumn() {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon()
                .appendQueryParameter("id", String.valueOf(mDetailData.getArticle().getColumn_id
                        ())).build().toString());
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mDetailData == null) return;
        // 点赞
        if (mDetailData.getArticle().isLiked()) {
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
                mDetailData.getArticle().setLiked(true);
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
            MoreDialog.newInstance(mDetailData).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mDetailData != null) {
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(String
                        .valueOf(mDetailData.getArticle().getId())))).show
                        (getSupportFragmentManager(), "CommentWindowDialog");
            }
        } else if (view.getId() == R.id.menu_comment) {
            if (mDetailData != null) {
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mDetailData);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager
                        .COMMENT_ACTIVITY_PATH);
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
            mAdapter.addData(commentList, true);
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
        int size = mAdapter.getDataSize();
        if (size > 0) {
            int count = 1;
            while (size - count >= 0) {
                Object data = mAdapter.getData(size - count++);
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
    public void onDeleteComment(String comment_id, int position) {
        List list = mAdapter.getData();
        for (Object obj : list) {
            int count = 0;
            if (obj instanceof HotCommentsBean && ((HotCommentsBean) obj).getId().equals
                    (comment_id)) {
                mAdapter.getData().remove(obj);
                mAdapter.notifyItemMoved(count, count);
//                mAdapter.notifyItemRangeChanged(count, mAdapter.getDataSize());
                break;
            }
            count++;
        }
    }

}
