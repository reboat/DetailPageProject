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
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.subject.holder.HeaderTopicHolder;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专题详情页
 *
 * @author a_liYa
 * @date 2017/10/12 上午8:51.
 */
public class SpecialActivity extends BaseActivity implements OnItemClickListener,
        HeaderTopicHolder.OnClickChannelListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_special);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        loadData();
    }

    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        topHolder.setViewVisible(topHolder.getCollectView(), View.VISIBLE);
        return topHolder.getView();
    }

    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
    }

    /**
     * 初始化专题详情页头部和列表信息
     */
    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new SpecialSpaceDivider(0.5f, R.attr.bc_dddddd));
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;

        Object data = mAdapter.getData(position);
        if (data instanceof ArticleItemBean) {
            NewsUtils.itemClick(this, data);
        }
    }

    @OnClick({R2.id.iv_top_share, R2.id.iv_top_collect})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;

        if (mArticle != null) {
            if (view.getId() == R.id.iv_share) {
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setImgUri(mArticle.getArticle_pic())
                        .setTextContent(mArticle.getSummary())
                        .setTitle(mArticle.getList_title())
                        .setTargetUrl(mArticle.getUrl()));
            } else if (view.getId() == R.id.iv_top_collect) {
                newsTopicCollect(); // 收藏
            }
        }
    }

    /**
     * 加载专题数据
     */
    private void loadData() {
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                fillData(draftDetailBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //专题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {//其余页面
                    mRecycler.setVisibility(View.GONE);
                }
            }

        }).setTag(this).exe(mArticleId);
    }

    private HeaderTopicHolder headHolder;

    private void fillData(DraftDetailBean data) {

        if (data != null) {
            mArticle = data.getArticle();
        }

        //添加专题详情页的头部holder
        headHolder = new HeaderTopicHolder(mRecycler, mRecyclerCopy, this);
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
    private void newsTopicCollect() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                topHolder.getCollectView().setImageResource(R.mipmap.module_detail_collect_night);
                T.showShort(getBaseContext(), getString(R.string.module_detail_collect_success));
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_collect_failed));
            }

        }).setTag(this).exe(mArticleId, !mArticle.isFollowed());
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        lyContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container,
                EmptyStateFragment.newInstance(String.valueOf(mArticle.getColumn_id()))).commit();
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