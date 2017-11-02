package com.zjrb.zjxw.detailproject.nomaldetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.player.PlayerManager;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.aliya.view.fitsys.FitWindowsRelativeLayout;
import com.aliya.view.ratio.RatioFrameLayout;
import com.umeng.socialize.UMShareAPI;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.global.PH;
import com.zjrb.core.common.global.RouteManager;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.utils.NetUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.HotCommentsBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.holder.DetailCommentHolder;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zjrb.core.utils.UIUtils.getContext;


/**
 * 普通详情页
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class NewsDetailActivity extends BaseActivity implements TouchSlopHelper.OnTouchSlopListener,
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener, DetailCommentHolder.deleteCommentListener {

    /**
     * 稿件ID
     */
    public String mArticleId;

    @BindView(R2.id.video_container)
    RatioFrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    TextView mTvComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.ly_bottom_comment)
    FitWindowsRelativeLayout mFloorBar;
    @BindView(R2.id.ry_container)
    RelativeLayout mContainer;
    @BindView(R2.id.iv_image)
    ImageView mivVideoBG;
    @BindView(R2.id.fl_comment)
    FrameLayout mFyContainer;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;

    /**
     * 上下滑动超出范围处理
     */
    private TouchSlopHelper mTouchSlopHelper;
    /**
     * 详情页数据
     */
    private DraftDetailBean mNewsDetail;
    /**
     * 详情页适配器
     */
    private NewsDetailAdapter mAdapter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_activity_detail);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();

    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null && data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
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
    private void init() {
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        loadData();
    }

    /**
     * 初始化视频
     */
    private void initVideo(String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            mVideoContainer.setVisibility(View.VISIBLE);
            GlideApp.with(mivVideoBG).load(mNewsDetail.getArticle().getList_pics().get(0)).placeholder(PH.zheBig()).centerCrop()
                    .into(mivVideoBG);
            PlayerManager.get().play(mVideoContainer, videoPath);
        } else {
            mVideoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable()) {
            T.showShort(getContext(), getString(R.string.module_detail_no_network));
        }
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null) return;
                mNewsDetail = draftDetailBean;
                if (!TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url())) {
                    initVideo(mNewsDetail.getArticle().getVideo_url());
                }
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    topHolder.setViewVisible(topHolder.getShareView(), View.GONE);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId);
    }


    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {

        DraftDetailBean.ArticleBean article = data.getArticle();
        if (article != null) {
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }

        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        mNewsDetail = data;
        initViewState(mNewsDetail);
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
//        if (mAdapter == null) {
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.attr.dc_dddddd));
        mAdapter = new NewsDetailAdapter(datas);
        mAdapter.setEmptyView(
                new EmptyPageHolder(mRvContent,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
        mRvContent.setAdapter(mAdapter);
//        } else {
//            mAdapter.setData(datas);
//        }


    }

    /**
     * 刷新底部栏状态
     *
     * @param data
     */
    private void initViewState(DraftDetailBean data) {
        //是否已点赞
        if (data.getArticle().isLike_enabled()) {
            mMenuPrised.setVisibility(View.VISIBLE);
            mMenuPrised.setSelected(data.getArticle().isLiked());
        } else {
            mMenuPrised.setVisibility(View.GONE);
        }

        //大致评论数量
        if (!TextUtils.isEmpty(data.getArticle().getComment_count_general())) {
            mTvCommentsNum.setVisibility(View.VISIBLE);
            mTvCommentsNum.setText(data.getArticle().getComment_count_general());
        } else {
            mTvCommentsNum.setVisibility(View.GONE);
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


    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * @param isUp 控制底部floorBar
     */
    @Override
    public void onTouchSlop(boolean isUp) {
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
     * 订阅
     */
    @Override
    public void onOptSubscribe() {
        //如果频道未订阅
        new ColumnSubscribeTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_subscribe_success));
            }

            @Override
            public void onAfter() {
                mAdapter.updateSubscribeInfo();
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShortNow(NewsDetailActivity.this, errMsg);
            }

        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);

    }

    /**
     * WebView加载完毕
     */
    @Override
    public void onOptPageFinished() {
        mAdapter.showAll();
    }

    private Bundle bundle;

    /**
     * 进入栏目列表页
     */
    @Override
    public void onOptClickColumn() {
        Nav.with(this).to(Uri.parse("http://www.8531.cn/subscription/detail").buildUpon().appendQueryParameter("id", String.valueOf(mNewsDetail.getArticle().getColumn_id())).build().toString());
    }

    /**
     * 进入频道详情页
     */
    @Override
    public void onOptClickChannel() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(IKey.CHANNEL_NAME, mNewsDetail.getArticle().getSource_channel_name());
        bundle.putString(IKey.CHANNEL_ID, mNewsDetail.getArticle().getSource_channel_id());
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.SUBSCRIBE_PATH);
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
                //用户未登录
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_prise_success));
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true);
    }


    @OnClick({R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.iv_top_share, R2.id.iv_type_video})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //评论列表
        if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.COMMENT_ACTIVITY_PATH);
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            MoreDialog.newInstance(mNewsDetail).setWebViewCallBack(mAdapter.getWebViewHolder(),mAdapter.getWebViewHolder()).show(getSupportFragmentManager(), "MoreDialog");
            //评论框
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null) {
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).show(getSupportFragmentManager(), "CommentWindowDialog");
            }
            //分享
        } else if (view.getId() == R.id.iv_top_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(mNewsDetail.getArticle().getFirstPic())
                    .setTextContent(mNewsDetail.getArticle().getSummary())
                    .setTitle(mNewsDetail.getArticle().getDoc_title())
                    .setTargetUrl(mNewsDetail.getArticle().getUrl()));
            //重新加载
        } else if (view.getId() == R.id.iv_type_video) {
            PlayerManager.get().play(mVideoContainer, mNewsDetail.getArticle().getVideo_url());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.onWebViewResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onWebViewPause();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(UIUtils.getApp()).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        topHolder.getShareView().setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ry_container, EmptyStateFragment.newInstance()).commit();
    }

    /**
     * 删除评论，局部刷新
     */
    @Override
    public void onDeleteComment(String comment_id, int position) {
        List list = mAdapter.getData();
        for (Object obj : list) {
            int count = 0;
            if (obj instanceof HotCommentsBean && ((HotCommentsBean) obj).getId().equals(comment_id)) {
                mAdapter.getData().remove(obj);
                mAdapter.notifyItemMoved(count, count);
//                mAdapter.notifyItemRangeChanged(count, mAdapter.getDataSize());
                break;
            }
            count++;
        }
    }
}


