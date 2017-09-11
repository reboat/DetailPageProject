package com.zjrb.zjxw.detailproject.nomaldetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ChannelBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.EmptyStateListAdapter;
import com.zjrb.zjxw.detailproject.task.GetChannelListTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 撤稿空态页面
 * Created by wanglinjie.
 * create time:2017/9/2  下午9:53
 */

public class EmptyStateFragment extends BaseFragment implements OnItemClickListener, HeaderRefresh.OnRefreshListener, LoadMoreListener<ChannelBean> {
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    /**
     * 栏目id
     */
    private String column_id;

    private EmptyStateListAdapter adapter;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static EmptyStateFragment newInstance(String columnId) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(Key.COLUMN_ID, columnId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            column_id = getArguments().getString(Key.COLUMN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_persional_info, container, false);
        ButterKnife.bind(this, v);
        getIntentData(getActivity().getIntent());
        initView();
        loadData();
        return v;
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            column_id = data.getQueryParameter(Key.COLUMN_ID);
        }
    }

    /**
     * 显示撤稿文案
     */
    private TextView emptyText;
    /**
     * 头布局
     */
    private View head;

    /**
     * 初始化控件
     */
    private void initView() {
        //添加头布局
        head = UIUtils.inflate(R.layout.moduel_detail_empty_state_head);
        emptyText = (TextView) head.findViewById(R.id.tv_empty_states);
        emptyText.setText(getString(R.string.module_detail_revoke));
        lvNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        lvNotice.addItemDecoration(new ListSpaceDivider(0.5f, UIUtils.getColor(R.color.dc_f5f5f5), true, true));
        //添加刷新头
        refresh = new HeaderRefresh(lvNotice);
        refresh.setOnRefreshListener(this);
        more = new FooterLoadMore(lvNotice, this);

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        adapter.setHeaderRefresh(refresh.getItemView());
        adapter.setFooterLoadMore(more.getItemView());
        adapter.setOnItemClickListener(this);
        adapter.addHeaderView(head);
        lvNotice.setAdapter(adapter);
    }

    //最后刷新时间
    private long lastMinPublishTime = 0;
    private List<SubjectItemBean> article_list;

    /**
     * 获取频道热门列表
     */
    private void loadData() {
        new GetChannelListTask(new APIExpandCallBack<ChannelBean>() {
            @Override
            public void onSuccess(ChannelBean bean) {
                if (bean == null) {
                    return;
                }
                if (bean.getResultCode() == 0) {//成功
                    article_list = bean.getArticle_list();
                    if (article_list != null) {
                        if (adapter == null) {
                            adapter = new EmptyStateListAdapter(article_list);
                            initAdapter();
                        }
                        lvNotice.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(UIUtils.getContext(), bean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }
        }).setTag(this).exe(column_id, lastMinPublishTime + "", "20");
    }

    /**
     * @param itemView
     * @param position 进入详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity2((SubjectItemBean) adapter.getData().get(position));
    }

    @Override
    public void onLoadMoreSuccess(ChannelBean data, LoadMore loadMore) {
        if (data != null) {
            List<SubjectItemBean> list = data.getArticle_list();
            if (list != null && list.size() > 0) {
                lastMinPublishTime = getLastMinPublishTime(list);//获取最后的刷新时间
            }
            adapter.addData(list, true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * @param callback 加载更多
     */
    @Override
    public void onLoadMore(LoadingCallBack<ChannelBean> callback) {
        new GetChannelListTask(callback).setTag(this).exe(column_id, lastMinPublishTime, "20");
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * @param list
     * @return 获取最后一次刷新的时间戳
     */
    private long getLastMinPublishTime(List<SubjectItemBean> list) {
        return list.get(list.size() - 1).getSort_number();
    }

}
