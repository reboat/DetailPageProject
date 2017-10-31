package com.zjrb.zjxw.detailproject.persionaldetail.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.OfficerRelatedNewsAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员相关新闻fragment
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class PersionalRelateFragment extends BaseFragment implements HeaderRefresh
        .OnRefreshListener, OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    /**
     * 相关新闻标识
     */
    public static final int TYPE_NEWS = 0;
    /**
     * 相关新闻列表
     */
    private OfficalDetailBean bean;
    /**
     * 官员ID
     */
    private String official_id;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;

    /**
     * 官员详情页相关新闻适配器
     */
    private OfficerRelatedNewsAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (OfficalDetailBean) getArguments().getSerializable(IKey
                    .FRAGMENT_PERSIONAL_RELATER);
            official_id = getArguments().getString(IKey.OFFICIAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_relate_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView() {
        lvNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        lvNotice.addItemDecoration(new ListSpaceDivider(0.5, R.attr.dc_dddddd, true));
        //添加刷新头
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
        mAdapter = new OfficerRelatedNewsAdapter(bean, lvNotice, official_id);
        lvNotice.setAdapter(mAdapter);
        initAdapter();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mAdapter.setHeaderRefresh(refresh.getItemView());
        mAdapter.setOnItemClickListener(this);
        mAdapter = new OfficerRelatedNewsAdapter(bean, lvNotice, official_id);
        mAdapter.setEmptyView(
                new EmptyPageHolder(lvNotice,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
    }

    /**
     * 下拉刷新
     */
    private void initData() {
        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean bean) {
                bindData(bean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getContext(), errMsg);
            }

            @Override
            public void onAfter() {
                refresh.setRefreshing(false);
            }

        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).exe(official_id);
    }


    /**
     * adapte处理
     *
     * @param bean
     */
    private void bindData(OfficalDetailBean bean) {
        mAdapter.setData(bean);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        lvNotice.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                initData();
            }
        });
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick() || mAdapter == null) {
            return;
        }
        NewsUtils.itemClick(this, mAdapter.getData(position));
    }

}
