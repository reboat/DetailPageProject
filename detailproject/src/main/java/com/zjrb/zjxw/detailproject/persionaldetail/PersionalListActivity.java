package com.zjrb.zjxw.detailproject.persionaldetail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.base.toolbar.TopBarFactory;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.recycleView.HeaderRefresh;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalListAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalListTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.C;

/**
 * 所有官员新闻列表
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:24
 */

public class PersionalListActivity extends DailyActivity implements HeaderRefresh.OnRefreshListener {

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
        loadData(true);
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
    private void loadData(boolean isFirst) {
        new OfficalListTask(new LoadingCallBack<OfficalListBean>() {

            @Override
            public void onSuccess(OfficalListBean data) {
                bindData(data);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(isFirst ? replaceLoad(mRecycler) : null).exe();

    }

    /**
     * 初始化分隔符
     */
    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.color._dddddd_343434, true));
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
                            EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据").resId(R.mipmap.ic_comment_empty)
                    ).itemView);
            mRecycler.setAdapter(mAdapter);
        } else {
            mAdapter.setData(bean);
            mAdapter.notifyDataSetChanged();
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
                loadData(false);
            }
        });

    }

}
