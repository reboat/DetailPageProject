package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.base.toolbar.TopBarFactory;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialListAdapter;
import com.zjrb.zjxw.detailproject.task.DraftTopicListTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.constant.IKey;

/**
 * 专题列表页面
 * Created by wanglinjie.
 * create time:2017/8/27 上午9:39.
 */
public class SpecialListActivity extends DailyActivity implements HeaderRefresh.OnRefreshListener,
        OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView mRecycler;

    /**
     * 专题分组id
     */
    private String group_id;
    /**
     * 专题列表标题
     */
    private String list_title;

    private HeaderRefresh mRefresh;
    private SpecialListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_special_list);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, list_title != null ? list_title : "")
                .getView();
    }

    private void getIntentData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IKey.GROUP_ID)) {
                group_id = intent.getStringExtra(IKey.GROUP_ID);
            }
            if (intent.hasExtra(IKey.TITLE)) {
                list_title = intent.getStringExtra(IKey.TITLE);
            }
        }
    }

    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.color._dddddd_343434, true));
        mRefresh = new HeaderRefresh(mRecycler);
        mRefresh.setOnRefreshListener(this);
        loadData(true);
    }

    private SubjectListBean mBean;

    private void loadData(boolean isFirst) {
        new DraftTopicListTask(new LoadingCallBack<SubjectListBean>() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {

            }

            @Override
            public void onSuccess(SubjectListBean bean) {
                bindData(bean);
                if (mRefresh != null) {
                    mRefresh.setRefreshing(false);
                }
            }

        }).setTag(this)
                .setShortestTime(isFirst ? 0 : C.REFRESH_SHORTEST_TIME)
                .bindLoadViewHolder(isFirst ? replaceLoad(mRecycler) : null)
                .exe(group_id);
    }

    private void bindData(SubjectListBean bean) {
        mBean = bean;
        if (mAdapter == null) {
            mAdapter = new SpecialListAdapter(bean, mRecycler, group_id);
            mAdapter.setHeaderRefresh(mRefresh.getItemView());
            mAdapter.setEmptyView(
                    new EmptyPageHolder(mRecycler,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                    ).itemView);
            mAdapter.setOnItemClickListener(this);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setData(bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cancelLoadMore();
        }
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;
        if (mAdapter != null) {
            if (mBean != null && mBean.getArticle_list() != null && mBean.getArticle_list().size() >= position) {
                ArticleItemBean bean = mBean.getArticle_list().get(position);
                new Analytics.AnalyticsBuilder(this, "200007", "200007", "AppContentClick", false)
                        .setEvenName("点击更多进入专题列表页面后，新闻列表点击")
                        .setObjectID(bean.getMlf_id() + "")
                        .setObjectName(bean.getDoc_title())
                        .setClassifyID(bean.getChannel_id())
                        .setClassifyName(bean.getChannel_name())
                        .setPageType("专题详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("customObjectType", "SubjectType")
                                .toString())
                        .setSelfObjectID(bean.getId() + "")
                        .pageType("专题详情页")
                        .objectType("专题新闻列表")
                        .pubUrl(bean.getUrl())
                        .newsID(bean.getMlf_id() + "")
                        .selfNewsID(bean.getId() + "")
                        .newsTitle(bean.getDoc_title())
                        .selfChannelID(bean.getChannel_id())
                        .channelName(bean.getChannel_name())
                        .build()
                        .send();
            }

            NewsUtils.itemClick(this, mAdapter.getData(position));
        }
    }
}
