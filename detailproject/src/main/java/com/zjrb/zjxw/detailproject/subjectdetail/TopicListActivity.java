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
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.TopicListAdapter;
import com.zjrb.zjxw.detailproject.task.DraftTopicListTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

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
        setContentView(R.layout.module_detail_topic_list);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    private int group_id = 0;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
                group_id = Integer.parseInt(data.getQueryParameter(Key.GROUP_ID));
        }
    }


    /**
     * 初始化
     */
    private void init() {
        getIntentData(getIntent());
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
    private List<SubjectListBean.ArticleListBean> list;

    private void loadData() {
        new DraftTopicListTask(new APIExpandCallBack<SubjectListBean>() {
            @Override
            public void onSuccess(SubjectListBean bean) {
                if (bean == null) {
                    return;
                }
                if (bean.getResultCode() == 0) {
                    list = bean.getArticle_list();
                    if (list != null) {
                        if (mAdapter == null) {
                            mAdapter = new TopicListAdapter(list);
                            initAdapter();
                            mRecycler.setAdapter(mAdapter);
                        }
                        mRecycler.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(getBaseContext(), bean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }
        }).setTag(this).exe(group_id+"", "", "20");
    }


    private long lastMinPublishTime = 0;

    @Override
    public void onLoadMoreSuccess(SubjectListBean data, LoadMore loadMore) {
        if (data != null) {
            List<SubjectListBean.ArticleListBean> list = data.getArticle_list();
            if (list != null && list.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime(list);
            }
            mAdapter.addData(list, true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    @Override
    public void onLoadMore(LoadingCallBack<SubjectListBean> callback) {
        new DraftTopicListTask(callback).setTag(this).exe(group_id, lastMinPublishTime, "20");
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * @param list
     * @return 获取最后一条的时间戳
     */
    private long getLastMinPublishTime(List<SubjectListBean.ArticleListBean> list) {
        return list.get(list.size() - 1).getId();
    }

    /**
     * @param itemView
     * @param position 点击专题列表
     *                 跳转到所类型有详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity2((SubjectItemBean) mAdapter.getData().get(position));
    }
}
