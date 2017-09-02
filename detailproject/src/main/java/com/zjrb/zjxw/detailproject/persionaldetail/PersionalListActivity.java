package com.zjrb.zjxw.detailproject.persionaldetail;

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
import com.zjrb.zjxw.detailproject.global.Key;
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
        setContentView(R.layout.module_detail_topic_list);
        ButterKnife.bind(this);
        init();
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
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
                if (data.getResultCode() == 0) {//成功
                    list = data.getOfficer_list();
                    if (list != null) {
                        if (mAdapter == null) {
                            mAdapter = new PersionalListAdapter(list);
                            initAdapter();
                        }
                        mRecycler.setAdapter(mAdapter);
                        mAdapter.setupData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(PersionalListActivity.this, data.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }

        }).setTag(this).exe();

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


    //官员稿件测试地址
    private String uri1 = "10.100.60.98:9000";// 开发环境
    private String uri2 = "10.100.60.93:9000";// 测试环境
    private String uri3 = "zj.zjol.com.cn";// 正式环境
    private String uri4 = "m.8531.cn";// 正式环境2

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
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
                        Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
                                .buildUpon()
                                .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(officalId))
                                .build(), 0);
                    } else {
                        if (uri != null && !uri.isEmpty()) {
                            //普通稿件
                            if ((uri.contains(uri1) ||
                                    uri.contains(uri2) ||
                                    uri.contains(uri3) ||
                                    uri.contains(uri4))) {
                                Uri u = Uri.parse(uri);
                                //稿件ID
                                String articleId = u.getQueryParameter(Key.ARTICLE_ID);
                                if (!articleId.isEmpty()) {
                                    Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
                                            .buildUpon()
                                            .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(articleId))
                                            .build(), 0);
                                }
                            } else {//链接稿/直播

                            }
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

    @Override
    public void onLoadMoreSuccess(OfficalListBean data, LoadMore loadMore) {
        if (data != null) {
            List<OfficalListBean.OfficerListBean> list = data.getOfficer_list();
            if (list != null && list.size() > 0) {
                lastOfficalId = getLastOfficalId(list);
            }
            mAdapter.addData(list, true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * @param callback 官员列表每次下发20条
     */
    @Override
    public void onLoadMore(LoadingCallBack<OfficalListBean> callback) {
        new OfficalListTask(callback).setTag(this).exe(lastOfficalId + "", "20");
    }

    /**
     * 下拉刷新回调
     */
    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * @param list
     * @return 获取最后一次刷新的官员id
     */
    private int getLastOfficalId(List<OfficalListBean.OfficerListBean> list) {
        return list.get(list.size() - 1).getId();
    }
}
