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
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.global.C;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalRelateNewsAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员相关新闻fragment
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class PersionalRelateFragment extends BaseFragment implements HeaderRefresh.OnRefreshListener, LoadMoreListener<OfficalDetailBean>, OnItemClickListener {

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
    private List<SubjectItemBean> list;
    /**
     * 官员ID
     */
    private String official_id;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;
    /**
     * 最后刷新的id
     */
    private int lastID;

    /**
     * 官员详情页相关新闻适配器
     */
    private PersionalRelateNewsAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = ((OfficalDetailBean) getArguments().getSerializable(Key.FRAGMENT_PERSIONAL_RELATER)).getArticle_list();
            official_id = getArguments().getString(Key.OFFICIAL_ID);
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
        if (list != null || list.isEmpty()) {
            mViewExise.setVisibility(View.VISIBLE);
            mViewExise.setVisibility(View.GONE);
            return;
        }
        mAdapter = new PersionalRelateNewsAdapter(list);
        lvNotice.setAdapter(mAdapter);
        lvNotice.setLayoutManager(new LinearLayoutManager(v.getContext()));
        diver = new ListSpaceDivider(32, 0, false);
        lvNotice.addItemDecoration(diver);
        //添加刷新头
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
        //添加上拉加载更多
        more = new FooterLoadMore(lvNotice, this);
        initAdapter();
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
     * 下拉刷新
     */
    private void initData() {
        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean bean) {
                if (bean == null) {
                    return;
                }
                more.setState(LoadMore.TYPE_IDLE);
                list = bean.getArticle_list();
                if (list != null) {
                    if (mAdapter == null) {
                        mAdapter = new PersionalRelateNewsAdapter(list);
                        initAdapter();
                        lvNotice.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getContext(), errMsg);
            }
        }).setTag(this).setShortestTime(C.REFRESH_SHORTEST_TIME).bindLoadViewHolder(replaceLoad(lvNotice)).exe(official_id);
    }

    /**
     * @param data
     * @param loadMore 上拉加载更多成功
     */
    @Override
    public void onLoadMoreSuccess(OfficalDetailBean data, LoadMore loadMore) {
        if (data != null && data.getArticle_list() != null) {
            List<SubjectItemBean> list = data.getArticle_list();
            if (list.size() > 0) {
                lastID = getLastID(list);//获取最后的刷新时间
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
     * @param callback 上拉加载更多
     */
    @Override
    public void onLoadMore(LoadingCallBack<OfficalDetailBean> callback) {
        new OfficalDetailTask(callback).setTag(this).exe(official_id, lastID == 0 ? null : lastID);
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
     * @param list
     * @return 获取最后一次刷新的时间戳
     */
    private int getLastID(List<SubjectItemBean> list) {
        return list.get(list.size() - 1).getId();
    }

    /**
     * @param itemView
     * @param position 相关新闻点击事件
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse(((SubjectItemBean) mAdapter.getData().get(position)).getUrl())
                    .buildUpon()
                    .appendQueryParameter(Key.VIDEO_PATH, ((SubjectItemBean) mAdapter.getData().get(position)).getVideo_url())//视频地址
                    .build(), 0);

        }
    }
}
