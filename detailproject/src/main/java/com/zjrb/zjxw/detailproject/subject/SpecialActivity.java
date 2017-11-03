package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.holder.TopBarWhiteStyle;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
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
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.subject.holder.HeaderSpecialHolder;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专题详情页
 *
 * @author a_liYa
 * @date 2017/10/12 上午8:51.
 */
public class SpecialActivity extends BaseActivity implements OnItemClickListener,
        HeaderSpecialHolder.OnClickChannelListener, View.OnClickListener {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    @BindView(R2.id.ly_container)
    ViewGroup lyContainer;
    @BindView(R2.id.recycler_copy)
    RecyclerView mRecyclerCopy;
    @BindView(R2.id.group_copy)
    FrameLayout mGroupCopy;

    private SpecialAdapter mAdapter;

    /**
     * 稿件ID
     */
    private String mArticleId = "";
    private DraftDetailBean.ArticleBean mArticle;

    private TopBarWhiteStyle mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_special);
        ButterKnife.bind(this);
        initArgs();
        initView();
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        mTopBar = new TopBarWhiteStyle(this);
        mTopBar.getCollectView().setOnClickListener(this);
        mTopBar.getShareView().setOnClickListener(this);
        return mTopBar.getView();
    }

    private void initArgs() {
        Intent intent = getIntent();
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
        initArgs();
        if (mTopBar != null) {
            mTopBar.setRightVisible(false);
        }
        loadData();
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
            NewsUtils.itemClick(this, data);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (mArticle != null) {
            if (view.getId() == R.id.iv_top_share) {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setImgUri(mArticle.getArticle_pic())
                        .setTextContent(mArticle.getSummary())
                        .setTitle(mArticle.getDoc_title())
                        .setTargetUrl(mArticle.getUrl()));
            } else if (view.getId() == R.id.iv_top_collect) {
                collectTask(); // 收藏
            }
        }
    }

    private void loadData() {
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

        }).setTag(this).bindLoadViewHolder(replaceLoad(mRecycler)).exe(mArticleId);
    }

    private HeaderSpecialHolder headHolder;

    private void fillData(DraftDetailBean data) {
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
        }
        bindCollect();
        mTopBar.setRightVisible(true);
        //添加专题详情页的头部holder
        headHolder = new HeaderSpecialHolder(mRecycler, mRecyclerCopy, this);
        headHolder.setData(data);
        mAdapter = new SpecialAdapter(data);
        mAdapter.addHeaderView(headHolder.getItemView());
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
        new OverlayHelper(mRecycler, mRecyclerCopy, mGroupCopy);
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
                    }
                }
                T.showShort(getActivity(), errCode);
            }

        }).setTag(this).exe(mArticleId, !mArticle.isFollowed());
    }

    private void bindCollect() {
        if (mArticle != null && mTopBar != null) {
            mTopBar.getCollectView().setSelected(mArticle.isFollowed());
        }
    }

    /**
     * 显示撤稿页面
     */
    private void showCancelDraft() {
        lyContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container,
                EmptyStateFragment.newInstance()).commit();
    }

    @Override
    public void onClickChannel(SpecialGroupBean bean) {
        List data = mAdapter.getData();
        if (data != null && bean != null) { // 跳转到指定分组
            int index = data.indexOf(bean);
            LinearLayoutManager lm = (LinearLayoutManager) mRecycler.getLayoutManager();
            lm.scrollToPositionWithOffset(index + mAdapter.getHeaderCount(),
                    mRecyclerCopy.getHeight());
        }
    }

}
