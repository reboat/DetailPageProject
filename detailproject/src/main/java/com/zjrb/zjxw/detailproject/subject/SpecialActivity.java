package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.holder.TopBarWhiteStyle;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.db.SPHelper;
import com.zjrb.core.ui.UmengUtils.OutSizeAnalyticsBean;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.web.ZBJsInterface;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.interFace.DetailWMHelperInterFace;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.subject.holder.HeaderSpecialHolder;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 专题详情页
 *
 * @author a_liYa
 * @date 2017/10/12 上午8:51.
 */
public class SpecialActivity extends BaseActivity implements OnItemClickListener,
        HeaderSpecialHolder.OnClickChannelListener, View.OnClickListener, DetailWMHelperInterFace.SpercialDetailWM {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    @BindView(R2.id.ly_container)
    ViewGroup lyContainer;
    @BindView(R2.id.recycler_copy)
    RecyclerView mRecyclerTabCopy;
    @BindView(R2.id.group_copy)
    FrameLayout mGroupCopy;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.overlay_layout)
    LinearLayout mOverlayLayout;

    private SpecialAdapter mAdapter;

    private Analytics mAnalytics;

    /**
     * 稿件ID
     */
    private String mArticleId = "";
    private String mFromChannel;

    private DraftDetailBean.ArticleBean mArticle;

    private TopBarWhiteStyle mTopBar;
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
        if (mTopBar != null) {
            mTopBar.setRightVisible(false);
        }
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        mTopBar = new TopBarWhiteStyle(this);
        mTopBar.getCollectView().setOnClickListener(this);
        mTopBar.getShareView().setOnClickListener(this);
        return mTopBar.getView();
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
        mRecycler.addItemDecoration(new SpecialSpaceDivider(0.5f, R.attr.dc_dddddd));
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;

        Object data = mAdapter.getData(position);
        if (data instanceof ArticleItemBean) {
            ClickSpecialItem((ArticleItemBean) data);
            NewsUtils.itemClick(this, data);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (mArticle != null) {
            if (view.getId() == R.id.iv_top_share) {
                if (!TextUtils.isEmpty(mArticle.getUrl())) {
                    //分享专用bean
                    OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                            .setObjectID(mArticle.getMlf_id() + "")
                            .setObjectName(mArticle.getDoc_title())
                            .setObjectType(ObjectType.NewsType)
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
                            .setTargetUrl(mArticle.getUrl()));
                }

            } else if (view.getId() == R.id.iv_top_collect) {
                //未被收藏
                ClickCollect(!mArticle.isFollowed());
                collectTask(); // 收藏
            } else if (view.getId() == R.id.iv_top_bar_back) {//返回
                ClickBack();
                finish();
            }
        }
    }

    private void loadData() {
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
        mOverlayLayout.setVisibility(View.INVISIBLE);
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean data) {
                fillData(data);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //专题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showCancelDraft();
                } else {//其余页面
                    mRecycler.setVisibility(View.GONE);
                }
            }

        }).setTag(this).bindLoadViewHolder(replaceLoad(mRecycler)).exe(mArticleId, mFromChannel);
    }

    private HeaderSpecialHolder headHolder;

    private void fillData(DraftDetailBean data) {
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
            mAnalytics = pageStayTime(data);
        }
        bindCollect();
        mTopBar.setRightVisible(true);

        if (mAdapter == null) {
            mAdapter = new SpecialAdapter(data);
            mAdapter.setOnItemClickListener(this);
            //添加专题详情页的头部holder
            headHolder = new HeaderSpecialHolder(mRecycler, mRecyclerTabCopy, this);
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

                    T.showShort(getActivity(), mArticle.isFollowed() ? "收藏成功" : "取消收藏成功");
                    bindCollect();
                }
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
        if (mArticle != null && mTopBar != null) {
            mTopBar.getCollectView().setSelected(mArticle.isFollowed());
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

    @Override
    public void onClickChannel(SpecialGroupBean bean) {
        List data = mAdapter.getData();
        if (data != null && bean != null) { // 跳转到指定分组
            int index = data.indexOf(bean);
            LinearLayoutManager lm = (LinearLayoutManager) mRecycler.getLayoutManager();
            lm.scrollToPositionWithOffset(index + mAdapter.getHeaderCount(),
                    mRecyclerTabCopy.getHeight());
            ClickChannel(bean);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.get().remove(ZBJsInterface.ZJXW_JS_SHARE_BEAN);
        if (mAnalytics != null) {
            mAnalytics.sendWithDuration();
        }
    }

    @Override
    public Analytics pageStayTime(DraftDetailBean bean) {
        return new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                .setEvenName("页面停留时长")
                .setObjectID(bean.getArticle().getMlf_id() + "")
                .setObjectName(bean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(bean.getArticle().getChannel_id())
                .setClassifyName(bean.getArticle().getChannel_name())
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", bean.getArticle().getColumn_id() + "")
                        .put("subject", bean.getArticle().getId() + "")
                        .toString())
                .setSelfObjectID(bean.getArticle().getId() + "")
                .build();
    }

    @Override
    public void ClickChannel(SpecialGroupBean bean) {
        if (mArticle != null) {
            new Analytics.AnalyticsBuilder(this, "900001", "900001")
                    .setEvenName("专题详情页，分类标签点击")
                    .setObjectType(ObjectType.NewsType)
                    .setObjectID(mArticle.getMlf_id() + "")
                    .setObjectName(mArticle.getDoc_title())
                    .setPageType("专题详情页")
                    .setSearch(bean.getGroup_name())
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mArticle.getColumn_id() + "")
                            .put("subject", mArticle.getId() + "")
                            .toString())
                    .setSelfObjectID(mArticle.getId() + "")
                    .build()
                    .send();
        }
    }

    @Override
    public void ClickSpecialItem(ArticleItemBean bean) {
        new Analytics.AnalyticsBuilder(this, "200007", "200007")
                .setEvenName("专题详情页，新闻列表点击")
                .setObjectID(bean.getMlf_id() + "")
                .setObjectName(bean.getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setPageType("专题详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", "SubjectType")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(bean.getId() + "")
                .build()
                .send();
    }

    @Override
    public void ClickCollect(boolean isCollect) {
        if (!isCollect) {
            new Analytics.AnalyticsBuilder(this, "A0024", "A0024")
                    .setEvenName("点击收藏")
                    .setObjectID(mArticle.getMlf_id() + "")
                    .setObjectName(mArticle.getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mArticle.getChannel_id())
                    .setClassifyName(mArticle.getChannel_name())
                    .setPageType("专题详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", "SubjectType")
                            .put("subject", mArticle.getId() + "")
                            .toString())
                    .setSelfObjectID(mArticle.getId() + "")
                    .build()
                    .send();
        } else {
            new Analytics.AnalyticsBuilder(this, "A0124", "A0124")
                    .setEvenName("取消收藏")
                    .setObjectID(mArticle.getMlf_id() + "")
                    .setObjectName(mArticle.getDoc_title())
                    .setObjectType(ObjectType.NewsType)
                    .setClassifyID(mArticle.getChannel_id())
                    .setClassifyName(mArticle.getChannel_name())
                    .setPageType("专题详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", "SubjectType")
                            .put("subject", mArticle.getId() + "")
                            .toString())
                    .setSelfObjectID(mArticle.getId() + "")
                    .build()
                    .send();
        }

    }

    @Override
    public void ClickBack() {
        new Analytics.AnalyticsBuilder(getContext(), "800001", "800001")
                .setEvenName("点击返回")
                .setObjectID(mArticle.getMlf_id() + "")
                .setObjectName(mArticle.getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(mArticle.getChannel_id())
                .setClassifyName(mArticle.getChannel_name())
                .setPageType("新闻详情页")
                .setOtherInfo(Analytics.newOtherInfo()
                        .put("relatedColumn", mArticle.getColumn_id() + "")
                        .put("subject", "")
                        .toString())
                .setSelfObjectID(mArticle.getId() + "")
                .build()
                .send();
    }
}
