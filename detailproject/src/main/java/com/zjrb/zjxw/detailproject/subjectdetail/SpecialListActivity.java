package com.zjrb.zjxw.detailproject.subjectdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.SpecialListAdapter;
import com.zjrb.zjxw.detailproject.task.DraftTopicListTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专题列表(支持所有列表类型)
 * Created by wanglinjie.
 * create time:2017/7/25  上午11:24
 */
public class SpecialListActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener,
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
        setContentView(R.layout.module_detail_topic_list);
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
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
        mRefresh = new HeaderRefresh(mRecycler);
        mRefresh.setOnRefreshListener(this);
        loadData(true);
    }

    private void loadData(boolean isFirst) {
        new DraftTopicListTask(new APIExpandCallBack<SubjectListBean>() {
            @Override
            public void onSuccess(SubjectListBean bean) {
                bindData(bean);
            }

            @Override
            public void onAfter() {
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
            NewsUtils.itemClick(this, mAdapter.getData(position));
        }
    }
}
