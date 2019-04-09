package com.zjrb.zjxw.detailproject.ui.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.apibean.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.ui.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.ui.subject.holder.HeaderSpecialHolder;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.utils.YiDunToken;
import com.zjrb.zjxw.detailproject.utils.global.C;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.network.compatible.APIExpandCallBack;
import cn.daily.news.biz.core.network.task.DraftCollectTask;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.web.JsMultiInterfaceImp;

/**
 * 专题详情页
 * Created by wanglinjie.
 * create time:2017/8/27 上午8:51.
 */
public class SpecialActivity extends DailyActivity implements OnItemClickListener,
        HeaderSpecialHolder.OnClickChannelListener {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    @BindView(R2.id.recycler_copy)
    RecyclerView mRecyclerTabCopy;
    @BindView(R2.id.group_copy)
    FrameLayout mGroupCopy;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.overlay_layout)
    FitWindowsLinearLayout mOverlayLayout;
    @BindView(R2.id.right_layout)
    LinearLayout mLyRight;
    @BindView(R2.id.iv_top_collect)
    ImageView mCollect;
    @BindView(R2.id.fy_container)
    FitWindowsFrameLayout fyContainer;
    @BindView(R2.id.iv_top_bar_back)
    ImageView ivBack;
    @BindView(R2.id.iv_top_share)
    ImageView ivShare;


    private SpecialAdapter mAdapter;

    private Analytics mAnalytics;
    /**
     * 稿件ID
     */
    private String mArticleId = "";
    private String mFromChannel;

    private DraftDetailBean.ArticleBean mArticle;
    private DraftDetailBean bean;

    private OverlayHelper mOverlayHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_special);
        ButterKnife.bind(this);
        initArgs(getIntent());
        initView();
        loadData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initArgs(intent);
        loadData();
    }

    @Override
    public boolean isShowTopBar() {
        return false;
    }

    private void initArgs(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                getIntent().setData(data);
                if (data.getQueryParameter(IKey.ID) != null) {
                    mArticleId = data.getQueryParameter(IKey.ID);
                }
                if (data.getQueryParameter(IKey.FROM_CHANNEL) != null) {
                    mFromChannel = data.getQueryParameter(IKey.FROM_CHANNEL);
                }
            }
        }
    }

    /**
     * 初始化专题详情页头部和列表信息
     */
    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new SpecialSpaceDivider(0.5f, R.color._dddddd_343434));
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;

        Object data = mAdapter.getData(position);
        if (data instanceof ArticleItemBean) {
            DataAnalyticsUtils.get().ClickSpecialItem((ArticleItemBean) data);
            NewsUtils.itemClick(this, data);
        }
    }

    @OnClick({R2.id.iv_top_share, R2.id.iv_top_collect, R2.id.iv_top_bar_back})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (mArticle != null) {
            if (view.getId() == R.id.iv_top_share) {
                if (!TextUtils.isEmpty(mArticle.getUrl())) {
                    //分享专用bean
                    OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                            .setObjectID(mArticle.getMlf_id() + "")
                            .setObjectName(mArticle.getDoc_title())
                            .setObjectType(ObjectType.NewsType).setUrl(mArticle.getUrl())
                            .setClassifyID(mArticle.getChannel_id() + "")
                            .setClassifyName(mArticle.getChannel_name())
                            .setPageType("新闻详情页")
                            .setOtherInfo(Analytics.newOtherInfo()
                                    .put("relatedColumn", mArticle.getColumn_id() + "")
                                    .put("subject", "")
                                    .toString())
                            .setSelfobjectID(mArticle.getId() + "");

                    UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                            .setSingle(false)
                            .setArticleId(mArticle.getId() + "")
                            .setImgUri(mArticle.getArticle_pic())
                            .setTextContent(mArticle.getSummary())
                            .setTitle(mArticle.getDoc_title())
                            .setAnalyticsBean(bean)
                            .setTargetUrl(mArticle.getUrl()).setEventName("NewsShare")
                            .setShareType("文章"));
                }

            } else if (view.getId() == R.id.iv_top_collect) {
                //未被收藏
                DataAnalyticsUtils.get().ClickCollect(mArticle);
                collectTask(); // 收藏
            } else if (view.getId() == R.id.iv_top_bar_back) {//返回
                DataAnalyticsUtils.get().ClickBack(bean);
                finish();
            }
        }
    }

    private void loadData() {
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        mOverlayLayout.setVisibility(View.INVISIBLE);
        new DraftDetailTask(new LoadingCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean data) {
                fillData(data);
                YiDunToken.synYiDunToken(mArticleId);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                mLyRight.setVisibility(View.GONE);
                //专题撤稿
                if (errCode == C.DRAFFT_IS_NOT_EXISE) {
                    showCancelDraft();
                } else {//其余页面
                    mRecycler.setVisibility(View.GONE);
                }
            }

        }).setTag(this).bindLoadViewHolder(replaceLoad(mRecycler)).exe(mArticleId, mFromChannel);
    }

    private HeaderSpecialHolder headHolder;

    private void fillData(DraftDetailBean data) {
        bean = data;
        mLyRight.setVisibility(View.VISIBLE);
        mOverlayLayout.setVisibility(View.VISIBLE);
        mView.setVisibility(View.GONE);
        if (data != null && data.getArticle() != null) {
            mArticle = data.getArticle();
            // 记录阅读信息
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(mArticle.getId())
                            .mlfId(mArticle.getMlf_id())
                            .tag(mArticle.getList_tag())
                            .title(mArticle.getList_title())
                            .url(mArticle.getUrl())
            );
            mAnalytics = DataAnalyticsUtils.get().pageStayTimeSpecial(mArticle);
        }
        bindCollect();

        if (mAdapter == null) {
            mAdapter = new SpecialAdapter(data);
            mAdapter.setOnItemClickListener(this);
            //添加专题详情页的头部holder,这里需要传递一个toolsbar的view
            headHolder = new HeaderSpecialHolder(mRecycler, mRecyclerTabCopy, fyContainer, mGroupCopy, this);
            headHolder.setData(data);
            mAdapter.addHeaderView(headHolder.getItemView());
            mRecycler.setAdapter(mAdapter);
        } else {
            headHolder.setData(data);
            mAdapter.setData(data);
            mAdapter.notifyDataSetChanged();
            mRecycler.scrollToPosition(0);
        }

        if (mOverlayHelper == null) {
            mOverlayHelper = new OverlayHelper(mRecycler, mRecyclerTabCopy, mGroupCopy);
        }
    }

    /**
     * 专题收藏
     */
    private void collectTask() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void data) {
                if (mArticle != null) {
                    mArticle.setFollowed(!mArticle.isFollowed());
                    bindCollect();
                    T.showShort(getActivity(), mArticle.isFollowed() ? "收藏成功" : "取消收藏成功");
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //已收藏成功
                if (errCode == 50013) {
                    if (mArticle != null) {
                        mArticle.setFollowed(true);
                        bindCollect();
                        T.showShort(getActivity(), "已收藏成功");
                    }
                } else {
                    T.showShort(getActivity(), errMsg);
                }
            }

        }).setTag(this).exe(mArticleId, !mArticle.isFollowed(), mArticle.getUrl());
    }

    /**
     * 收藏状态
     */
    private void bindCollect() {
        if (mArticle != null) {
            mCollect.setSelected(mArticle.isFollowed());
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showCancelDraft() {
        mView.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container,
                EmptyStateFragment.newInstance()).commit();
    }

    /**
     * 分组标签点击,背景处理
     *
     * @param bean
     */
    @Override
    public void onClickChannel(SpecialGroupBean bean) {
        List data = mAdapter.getData();
        if (data != null && bean != null) { // 跳转到指定分组
            int index = data.indexOf(bean);
            LinearLayoutManager lm = (LinearLayoutManager) mRecycler.getLayoutManager();
            lm.scrollToPositionWithOffset(index + mAdapter.getHeaderCount(),
                    mRecyclerTabCopy.getHeight());
            DataAnalyticsUtils.get().ClickChannel(mArticle, bean);
            //点击tab标签背景渐变
            headHolder.getItemView().setBackgroundResource(R.color._ffffff);
            fyContainer.setBackgroundResource(R.color._ffffff);
            ivBack.setImageResource(R.drawable.module_detail_topbar_back);
            mCollect.setImageResource(R.drawable.module_biz_ic_special_collect_anim);
            ivShare.setImageResource(R.drawable.module_detail_topbar_share);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.get().remove(JsMultiInterfaceImp.ZJXW_JS_SHARE_BEAN);
        if (mAnalytics != null) {
            mAnalytics.sendWithDuration();
        }
    }
}
