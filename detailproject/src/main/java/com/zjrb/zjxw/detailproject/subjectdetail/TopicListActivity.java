package com.zjrb.zjxw.detailproject.subjectdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.TopicListAdapter;
import com.zjrb.zjxw.detailproject.task.DraftTopicListTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 专题列表(支持所有列表类型)
 * Created by wanglinjie.
 * create time:2017/7/25  上午11:24
 */

public class TopicListActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, LoadMoreListener<SubjectListBean>, OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView mRecycler;


    /**
     * 专题列表适配器
     */
    private TopicListAdapter mAdapter;

    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData(getIntent());
        setContentView(R.layout.module_detail_topic_list);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, list_title != null ? list_title : "").getView();
    }

    /**
     * 专题分组id
     */
    private String group_id;

    /**
     * 专题列表标题
     */
    private String list_title;

    /**
     * @param intent 获取传递数据
     */
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


    /**
     * 初始化
     */
    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
        refresh = new HeaderRefresh(mRecycler);
        refresh.setOnRefreshListener(this);
        more = new FooterLoadMore(mRecycler, this);
        loadData();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mAdapter.setHeaderRefresh(refresh.getItemView());
        mAdapter.setFooterLoadMore(more.getItemView());
        mAdapter.setOnItemClickListener(this);
    }

    /**
     * 专题列表数据
     */
    private List<SubjectItemBean> list;

    /**
     * 加载专题分组列表
     */
    private void loadData() {
        new DraftTopicListTask(new APIExpandCallBack<SubjectListBean>() {
            @Override
            public void onSuccess(SubjectListBean bean) {
                if (bean == null) {
                    return;
                }
                more.setState(LoadMore.TYPE_IDLE);
                list = bean.getArticle_list();
                if (list != null) {
                    if (mAdapter == null) {
                        mAdapter = new TopicListAdapter(list);
                        initAdapter();
                        mRecycler.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(mRecycler)).exe(group_id);
    }


    /**
     * 上次请求的最后一条新闻的ID
     */
    private long lastNewsId;

    /**
     * @param data
     * @param loadMore 加载更多成功
     */
    @Override
    public void onLoadMoreSuccess(SubjectListBean data, LoadMore loadMore) {
        if (data != null && data.getArticle_list() != null) {
            List<SubjectItemBean> list = data.getArticle_list();
            if (list.size() > 0) {
                lastNewsId = getLastNewsId(list);
            }
            mAdapter.addData(list, true);
            if (list.size() < C.PAGE_SIZE) {
                loadMore.setState(LoadMore.TYPE_NO_MORE);
            }

        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * @param callback 加载更多
     */
    @Override
    public void onLoadMore(LoadingCallBack<SubjectListBean> callback) {
        new DraftTopicListTask(callback).setTag(this).exe(group_id, lastNewsId == 0 ? null : lastNewsId + "");
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        mRecycler.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                loadData();
            }
        });
    }

    /**
     * @param list
     * @return 获取最后一条新闻的ID
     */
    private long getLastNewsId(List<SubjectItemBean> list) {
        return list.get(list.size() - 1).getId();
    }

    /**
     * @param itemView
     * @param position 点击专题列表
     *                 跳转到所类型有详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
                Nav.with(UIUtils.getActivity()).to(Uri.parse(((SubjectItemBean) mAdapter.getData().get(position)).getUrl())
                        .buildUpon()
                        .build(), 0);

            }
        }
    }
}
