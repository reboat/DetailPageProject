package com.zjrb.zjxw.detailproject.persionaldetail.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalSuperRelateAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员相关新闻fragment
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class PersionalRelateFragment extends BaseFragment implements HeaderRefresh.OnRefreshListener, OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    @BindView(R2.id.view_exise)
    LinearLayout mViewExise;

    /**
     * 相关新闻标识
     */
    public static final int TYPE_NEWS = 0;
    private ListSpaceDivider diver;
    /**
     * 相关新闻列表
     */
    private OfficalDetailBean bean;
    private List<ArticleItemBean> list;
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
    private PersionalSuperRelateAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (OfficalDetailBean) getArguments().getSerializable(IKey.FRAGMENT_PERSIONAL_RELATER);
            official_id = getArguments().getString(IKey.OFFICIAL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_relate_news, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    /**
     * @param v 初始化适配器
     */
    private void initView(View v) {
        if (bean != null || bean.getArticle_list().isEmpty()) {
            mViewExise.setVisibility(View.VISIBLE);
            mViewExise.setVisibility(View.GONE);
            return;
        }
        mAdapter = new PersionalSuperRelateAdapter(bean, lvNotice, official_id);
        lvNotice.setAdapter(mAdapter);
        lvNotice.setLayoutManager(new LinearLayoutManager(v.getContext()));
        diver = new ListSpaceDivider(32, 0, false);
        lvNotice.addItemDecoration(diver);
        //添加刷新头
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
        initAdapter();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mAdapter.setHeaderRefresh(refresh.getItemView());
        mAdapter.setOnItemClickListener(this);
        mAdapter = new PersionalSuperRelateAdapter(bean, lvNotice, official_id);
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

        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(lvNotice)).exe(official_id);
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

    /**
     * @param itemView
     * @param position 相关新闻点击事件
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            Nav.with(UIUtils.getActivity()).to(((ArticleItemBean) mAdapter.getData().get(position)).getUrl());
//            Nav.with(UIUtils.getActivity()).to(Uri.parse(((ArticleItemBean) mAdapter.getData().get(position)).getUrl())
//                    .buildUpon()
//                    .build(), 0);

        }
    }
}
