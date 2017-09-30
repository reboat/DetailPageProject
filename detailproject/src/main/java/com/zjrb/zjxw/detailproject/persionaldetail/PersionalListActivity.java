package com.zjrb.zjxw.detailproject.persionaldetail;

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
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalListAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalListTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有官员新闻列表
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:24
 */

public class PersionalListActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, LoadMoreListener<OfficalListBean> {

    @BindView(R2.id.lv_notice)
    RecyclerView mRecycler;
    /**
     * 所有官员列表适配器
     */
    private PersionalListAdapter mAdapter;

    private List<OfficalListBean.OfficerListBean> list;
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
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, offical_title).getView();
    }

    /**
     * 官员列表标题
     */
    private String offical_title;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(IKey.OFFICAL_TITLE)) {
                offical_title = intent.getStringExtra(IKey.OFFICAL_TITLE);
            }
        }
    }


    /**
     * 下拉加载所有官员列表数据
     */
    private void loadData() {
        new OfficalListTask(new APIExpandCallBack<OfficalListBean>() {

            @Override
            public void onSuccess(OfficalListBean data) {
                if (data == null) {
                    return;
                }
                more.setState(LoadMore.TYPE_IDLE);
                list = data.getOfficer_list();
                if (list != null) {
                    if (mAdapter == null) {
                        mAdapter = new PersionalListAdapter();
                        initAdapter();
                        mAdapter.setupData(list);
                        mRecycler.setAdapter(mAdapter);
                    } else {
                        mAdapter.setupData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
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
        more = new FooterLoadMore(mRecycler, this);

    }

    private Bundle bundle;

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter.setHeaderRefresh(refresh.getItemView());
        mAdapter.setFooterLoadMore(more.getItemView());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (list != null && list.get(position) != null && list.size() > 0) {
                    OfficalArticlesBean b = (OfficalArticlesBean) mAdapter.getData(position);
                    String uri = b.getUrl();
                    int type = b.getType();
                    int officalId = b.getOfficalId();
                    //进入官员详情页
                    if (type == PersionalListAdapter.TYPE_PERSIONAL_DETAIL) {
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putInt(IKey.OFFICIAL_ID, officalId);
                        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath("/detail/PersionalDetailActivity");
                    } else {
                        //支持跳转到所有的新闻详情页
                        if (uri != null && !uri.isEmpty()) {
                            Uri u = Uri.parse(uri);
                            Nav.with(UIUtils.getActivity()).to(u
                                    .buildUpon()
                                    .build(), 0);

                        }

                    }

                }
            }
        });
    }


    /**
     * 最后一次下发的官员ID
     */
    private int lastOfficalId = 0;

    /**
     * 加载更多
     *
     * @param data
     * @param loadMore
     */
    @Override
    public void onLoadMoreSuccess(OfficalListBean data, LoadMore loadMore) {
        if (data != null && data.getOfficer_list() != null) {
            List<OfficalListBean.OfficerListBean> list = data.getOfficer_list();
            if (list.size() > 0) {
                //获取最后的刷新ID
                lastOfficalId = getLastOfficalId(list);
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
     * 加载更多每次加载3条
     *
     * @param callback 官员列表每次下发20条
     */
    @Override
    public void onLoadMore(LoadingCallBack<OfficalListBean> callback) {
        new OfficalListTask(callback).setTag(this).exe(lastOfficalId == 0 ? null : lastOfficalId + "");
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

    /**
     * @param list
     * @return 获取最后一次刷新的官员id
     */
    private int getLastOfficalId(List<OfficalListBean.OfficerListBean> list) {
        if (list != null && list.size() > 0) {
            return list.get(list.size() - 1).getId();
        }
        return 0;
    }
}
