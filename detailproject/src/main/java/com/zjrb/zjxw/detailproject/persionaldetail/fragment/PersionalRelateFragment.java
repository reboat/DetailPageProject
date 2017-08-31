package com.zjrb.zjxw.detailproject.persionaldetail.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.coreprojectlibrary.api.callback.APIExpandCallBack;
import com.zjrb.coreprojectlibrary.api.callback.LoadingCallBack;
import com.zjrb.coreprojectlibrary.common.base.BaseFragment;
import com.zjrb.coreprojectlibrary.common.base.adapter.OnItemClickListener;
import com.zjrb.coreprojectlibrary.common.base.page.LoadMore;
import com.zjrb.coreprojectlibrary.common.listener.LoadMoreListener;
import com.zjrb.coreprojectlibrary.ui.holder.FooterLoadMore;
import com.zjrb.coreprojectlibrary.ui.holder.HeaderRefresh;
import com.zjrb.coreprojectlibrary.ui.widget.divider.ListSpaceDivider;
import com.zjrb.coreprojectlibrary.utils.T;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.eventBus.PersionalInfoTabEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalRelateNewsAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalDetailTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员相关新闻fragment
 */
public class PersionalRelateFragment extends BaseFragment implements HeaderRefresh.OnRefreshListener, LoadMoreListener<OfficalDetailBean>, OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    /**
     * 相关新闻标识
     */
    public static final int TYPE_NEWS = 0;
    /**
     * tab index
     */
    private static int mIndex = -1;
    private ListSpaceDivider diver;
    /**
     * 相关新闻列表
     */
    private List<SubjectItemBean> list;
    /**
     * 官员ID
     */
    private int official_id;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;
    /**
     * 最后刷新时间
     */
    private long lastMinPublishTime = 0;

    /**
     * 官员详情页相关新闻适配器
     */
    private PersionalRelateNewsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(Key.FRAGMENT_ARGS);
            list = ((OfficalDetailBean) getArguments().getSerializable(Key.FRAGMENT_PERSIONAL_RELATER)).getArticle_list();
            official_id = getArguments().getInt(Key.OFFICIAL_ID);
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
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                BizUtils.jumpToDetailActivity2((SubjectItemBean) mAdapter.getData().get(position), position);
            }
        });
    }

    /**
     * @param isVisibleToUser isVisibleToUser:false 不可见
     *                        isVisibleToUser:true  可见
     *                        切换操作,可以进行UI操作
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        T.showShort(UIUtils.getContext(), "当前tab2_index=" + mIndex);
        if (isVisibleToUser) {
            EventBus.getDefault().postSticky(new PersionalInfoTabEvent());
        }
    }

    private void initData() {
        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {

            @Override
            public void onSuccess(OfficalDetailBean data) {
                if (data == null) {
                    return;
                }
                if (data.getResultCode() == 0) {//成功
                    list = data.getArticle_list();
                    if (list != null) {
                        if (mAdapter == null) {
                            mAdapter = new PersionalRelateNewsAdapter(list);
                            initAdapter();
                        }
                        lvNotice.setAdapter(mAdapter);
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(getContext(), data.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }
        }).setTag(this).exe(official_id + "", "", "20");
    }

    /**
     * @param data
     * @param loadMore 上拉加载更多成功
     */
    @Override
    public void onLoadMoreSuccess(OfficalDetailBean data, LoadMore loadMore) {
        if (data != null) {
            List<SubjectItemBean> list = data.getArticle_list();
            if (list != null && list.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime(data.getArticle_list());//获取最后的刷新时间
            }
            mAdapter.addData(data.getArticle_list(), true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * @param callback 上拉加载更多
     */
    @Override
    public void onLoadMore(LoadingCallBack<OfficalDetailBean> callback) {
        new OfficalDetailTask(callback).setTag(this).exe(official_id, lastMinPublishTime, "20");
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        initData();
    }

    /**
     * @param list
     * @return 获取最后一次刷新的时间戳
     */
    private long getLastMinPublishTime(List<SubjectItemBean> list) {
        return list.get(list.size() - 1).getPublished_at();
    }

    /**
     * @param itemView
     * @param position 相关新闻点击事件
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity2((SubjectItemBean) mAdapter.getData().get(position), position);
    }
}
