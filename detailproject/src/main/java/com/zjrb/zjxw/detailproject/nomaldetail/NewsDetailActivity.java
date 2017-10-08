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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliya.player.PlayerManager;
import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.biz.TouchSlopHelper;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.domain.CommentDialogBean;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.dialog.CommentWindowDialog;
import com.zjrb.core.ui.widget.load.LoadViewHolder;
import com.zjrb.core.utils.NetUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.CommentResultEvent;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.task.ColumnSubscribeTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.task.DraftPraiseTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;
import com.zjrb.zjxw.detailproject.utils.MoreDialog;
import com.zjrb.zjxw.detailproject.webjs.WebJsInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        NewsDetailAdapter.CommonOptCallBack, View.OnClickListener {

    /**
     * 稿件ID
     */
    public String mArticleId;
    //视频地址
    public String mVideoPath = "";
    @BindView(R2.id.iv_image)
    ImageView mIvImage;
    @BindView(R2.id.iv_type_video)
    ImageView mIvTypeVideo;
    @BindView(R2.id.video_container)
    FrameLayout mVideoContainer;
    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.tv_comment)
    EditText mTvComment;
    @BindView(R2.id.fl_comment)
    FrameLayout mFlComment;
    @BindView(R2.id.menu_comment)
    ImageView mMenuComment;
    @BindView(R2.id.tv_comments_num)
    TextView mTvCommentsNum;
    @BindView(R2.id.menu_prised)
    ImageView mMenuPrised;
    @BindView(R2.id.menu_setting)
    ImageView mMenuSetting;
    @BindView(R2.id.floor_bar)
    FitWindowsFrameLayout mFloorBar;
    @BindView(R2.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R2.id.ly_container)
    LinearLayout mContainer;
    @BindView(R2.id.view_exise)
    LinearLayout mViewExise;
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


    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
            if (data.getQueryParameter(IKey.VIDEO_PATH) != null) {
                mVideoPath = data.getQueryParameter(IKey.VIDEO_PATH);
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
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        mTouchSlopHelper = new TouchSlopHelper();
        mTouchSlopHelper.setOnTouchSlopListener(this);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        if (!TextUtils.isEmpty(mVideoPath)) {
            initVideo();
        }
        loadData();
    }

    /**
     * 初始化视频
     */
    private void initVideo() {
        if (!TextUtils.isEmpty(mVideoPath)) {
            mVideoContainer.setVisibility(View.VISIBLE);
            PlayerManager.get().play(mVideoContainer, mVideoPath);
        } else {
            mVideoContainer.setVisibility(View.GONE);
        }

        if (!NetUtils.isAvailable()) {
            T.showShort(getContext(), "网络不可用");
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
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {
                    //别的错误
                    mFlContent.setVisibility(View.GONE);
                    mViewExise.setVisibility(View.VISIBLE);
                }
            }
        }).setTag(this).exe(mArticleId);
    }

    @Override
    public LoadViewHolder replaceLoad() {
        if (TextUtils.isEmpty(mVideoPath)) {
            return super.replaceLoad();
        } else { // 视频详情页
            return replaceLoad(mFlContent);
        }
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {

        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
        //middle
        datas.add(data);
        //相关专题
        if (data.getArticle().getRelated_subjects() != null && data.getArticle().getRelated_subjects().size() > 0) {
            datas.add(data);
        }
        //相关新闻
        if (data.getArticle().getRelated_news() != null && data.getArticle().getRelated_news().size() > 0) {
            datas.add(data);

        }
        //热门评论
        if (data.getArticle().getHot_comments() != null && data.getArticle().getHot_comments().size() > 0) {
            datas.add(data);
        }
        mAdapter = new NewsDetailAdapter(datas);
        mRvContent.setAdapter(mAdapter);

        //点赞数量
        mMenuPrised.setSelected(data.getArticle().isLiked());
        if (data.getArticle().getComment_count() <= 0) {
            mTvCommentsNum.setVisibility(View.GONE);
        } else {
            if (data.getArticle().getComment_count() < 9999) {
                mTvCommentsNum.setText(data.getArticle().getComment_count() + "");
            } else if (data.getArticle().getComment_count() > 9999) {
                mTvCommentsNum.setText(BizUtils.numFormat(data.getArticle().getComment_count(), 10000, 1) + "");
            }
        }
        //评论分级
        BizUtils.setCommentSet(mTvComment, mNewsDetail.getArticle().getComment_level());
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

        }).setTag(this).exe(mNewsDetail.getArticle().getColumn_id(), true);

    }

    @Override
    public void onOptPageFinished() { // WebView页面加载完毕
        mAdapter.showAll();
    }

    private Bundle bundle;

    /**
     * 进入栏目列表页
     */
    @Override
    public void onOptClickColumn() {
        //TODO  WLJ 进入栏目列表
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt(IKey.ID, mNewsDetail.getArticle().getColumn_id());
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/subscription/detail");
    }

    /**
     * 进入频道详情页
     */
    @Override
    public void onOptClickChannel() {
        //TODO WLJ 频道详情页
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(IKey.CHANNEL_NAME, mNewsDetail.getArticle().getChannel_name());
        bundle.putString(IKey.CHANNEL_ID, mNewsDetail.getArticle().getChannel_id());
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/subscription/subscribe");
    }

    /**
     * 点赞操作
     */
    public void onOptFabulous() {
        if (mNewsDetail == null) return;
        // 点赞
        if (mNewsDetail.getArticle().isLiked()) {
            T.showNow(this, "您已点赞", Toast.LENGTH_SHORT);
            return;
        }
        new DraftPraiseTask(new APIExpandCallBack<Void>() {

            @Override
            public void onError(String errMsg, int errCode) {
                //用户未登录
                T.showShort(getBaseContext(), "点赞失败");
            }

            @Override
            public void onSuccess(Void baseInnerData) {
                T.showShort(getBaseContext(), "点赞成功");
                mNewsDetail.getArticle().setLiked(true);
                mMenuPrised.setSelected(true);
            }
        }).setTag(this).exe(mArticleId, true);
    }


    @OnClick({R2.id.menu_comment, R2.id.menu_prised, R2.id.menu_setting,
            R2.id.tv_comment, R2.id.view_exise, R2.id.iv_top_share})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        //评论框
        if (view.getId() == R.id.menu_comment) {
            if (mNewsDetail != null) {
                //进入评论列表页面
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putSerializable(IKey.NEWS_DETAIL, mNewsDetail);
                Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/detail/CommentActivity");
            }
            //点赞
        } else if (view.getId() == R.id.menu_prised) {
            onOptFabulous();
            //更多
        } else if (view.getId() == R.id.menu_setting) {
            MoreDialog.newInstance(mNewsDetail).show(getSupportFragmentManager(), "MoreDialog");
            //评论列表
        } else if (view.getId() == R.id.tv_comment) {
            if (mNewsDetail != null &&
                    BizUtils.isCanComment(this, mNewsDetail.getArticle().getComment_level())) {
                //进入评论编辑页面(不针对某条评论)
                CommentWindowDialog.newInstance(new CommentDialogBean(String.valueOf(mNewsDetail.getArticle().getId()))).show(getSupportFragmentManager(), "CommentWindowDialog");
                return;
            }
            //分享
        } else if (view.getId() == R.id.iv_top_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(TextUtils.isEmpty(WebJsInterface.getInstance(this).getmImgSrcs().toString()) ?
                            mNewsDetail.getArticle().getArticle_pic() : WebJsInterface.getInstance(this).getmImgSrcs()[0])
                    .setTextContent(TextUtils.isEmpty(WebJsInterface.getInstance(this).getHtmlText()) ? "" :
                            WebJsInterface.getInstance(this).getHtmlText())
                    .setTitle(mNewsDetail.getArticle().getList_title())
                    .setTargetUrl(mNewsDetail.getArticle().getUrl()));
            //重新加载
        } else if (view.getId() == R.id.view_exise) {
            loadData();
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(CommentResultEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        if (mNewsDetail != null && event.getData() > 0) {
            mNewsDetail.getArticle().setComment_count(mNewsDetail.getArticle().getComment_count() + event.getData());
            mTvCommentsNum.setText(BizUtils.formatComments(mNewsDetail.getArticle().getComment_count()));
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(mNewsDetail.getArticle().getColumn_id()))).commit();
    }
}


