package com.zjrb.zjxw.detailproject.persionaldetail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.global.C;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalListAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalListTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有官员新闻列表
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:24
 */

public class PersionalListActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView mRecycler;
    /**
     * 所有官员列表适配器
     */
    private PersionalListAdapter mAdapter;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_special_list);
        ButterKnife.bind(this);
        init();
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * 下拉加载所有官员列表数据
     */
    private void loadData() {
        new OfficalListTask(new APIExpandCallBack<OfficalListBean>() {

            @Override
            public void onSuccess(OfficalListBean data) {
                bindData(data);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(mRecycler)).exe();

    }

    /**
     * 初始化分隔符
     */
    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
        //添加刷新头
        refresh = new HeaderRefresh(mRecycler);
        refresh.setOnRefreshListener(this);

    }

    /**
     * adapte处理
     *
     * @param bean
     */
    private void bindData(OfficalListBean bean) {
        //初始化适配器
        if (mAdapter == null) {
            mAdapter = new PersionalListAdapter(bean, mRecycler);
            mAdapter.setHeaderRefresh(refresh.getItemView());
            mAdapter.setEmptyView(
                    new EmptyPageHolder(mRecycler,
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据").attrId(R.attr.ic_comment_empty)
                    ).itemView);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setData(bean);
        }
    }

    /**
     * 下拉刷新回调
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

    @Override
    public void onItemClick(View itemView, int position) {
        if (mAdapter.getData(position) instanceof OfficalListBean.OfficerListBean) {
            if(mAdapter.getData(position) != null && !TextUtils.isEmpty(((OfficalListBean.OfficerListBean) mAdapter.getData(position)).getDetail_url())){
                Nav.with(PersionalListActivity.this).to(((OfficalListBean.OfficerListBean) mAdapter.getData(position)).getDetail_url());
            }
        } else if (mAdapter.getData(position) instanceof OfficalArticlesBean) {
            if(mAdapter.getData(position) != null && !TextUtils.isEmpty(((OfficalArticlesBean) mAdapter.getData(position)).getUrl())){
                Nav.with(PersionalListActivity.this).to(((OfficalArticlesBean) mAdapter.getData(position)).getUrl());
            }
        }
    }
}
