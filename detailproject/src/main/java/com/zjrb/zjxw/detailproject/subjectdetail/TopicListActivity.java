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
import com.zjrb.core.common.global.C;
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

import java.util.ArrayList;
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


    /**
     * 专题列表适配器
     */
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
        getIntentData(getIntent());
        setContentView(R.layout.module_detail_topic_list);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, list_title != null ? list_title : "").getView();
    }

    /**
     * 专题分组id
     */
    private String group_id;

    /**
     * 专题列表标题
     */
    private String list_title;

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(Key.GROUP_ID) != null) {
                group_id = data.getQueryParameter(Key.GROUP_ID);
            }
            if (data.getQueryParameter(Key.TITLE) != null) {
                list_title = data.getQueryParameter(Key.TITLE);
            }
        }
    }


    /**
     * 初始化
     */
    private void init() {
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
    private List<SubjectItemBean> list;

    //mock测试数据
    private List<SubjectItemBean> mockData() {
        List<SubjectItemBean> list = new ArrayList<>();

        SubjectItemBean b = new SubjectItemBean();
        List<String> l = new ArrayList<>();
        l.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setList_pics(l);
        b.setList_title("文章1");

        SubjectItemBean b1 = new SubjectItemBean();
        List<String> l1 = new ArrayList<>();
        l1.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b1.setList_pics(l1);
        b1.setList_title("文章2");

        SubjectItemBean b2 = new SubjectItemBean();
        List<String> l2 = new ArrayList<>();
        l2.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b2.setList_pics(l2);
        b2.setList_title("文章3");

        SubjectItemBean b3 = new SubjectItemBean();
        List<String> l3 = new ArrayList<>();
        l3.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b3.setList_pics(l3);
        b3.setList_title("文章4");


        SubjectItemBean b4 = new SubjectItemBean();
        List<String> l4 = new ArrayList<>();
        l4.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b4.setList_pics(l4);
        b4.setList_title("文章5");

        SubjectItemBean b5 = new SubjectItemBean();
        List<String> l5 = new ArrayList<>();
        l5.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b5.setList_pics(l5);
        b5.setList_title("文章6");

        SubjectItemBean b6 = new SubjectItemBean();
        List<String> l6 = new ArrayList<>();
        l6.add("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b6.setList_pics(l6);
        b6.setList_title("文章7");

        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        list.add(b6);

        return list;
    }

    /**
     * 加载专题分组列表
     */
    private void loadData() {
        new DraftTopicListTask(new APIExpandCallBack<SubjectListBean>() {
            @Override
            public void onSuccess(SubjectListBean bean) {
                if (bean == null) {
                    return;
                }
                list = mockData();//commentRefreshBean.getComments();
                if (list != null) {
                    if (mAdapter == null) {
                        mAdapter = new TopicListAdapter(list);
                        initAdapter();
                        mRecycler.setAdapter(mAdapter);
                    } else {
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

        }).setTag(this).exe(group_id);
    }


    /**
     * 上次请求的最后一条新闻的ID
     */
    private long lastNewsId;

    /**
     * @param data
     * @param loadMore 加载更多成功
     */
    @Override
    public void onLoadMoreSuccess(SubjectListBean data, LoadMore loadMore) {
        if (data != null && data.getArticle_list() != null) {
            List<SubjectItemBean> list = data.getArticle_list();
            if (list.size() > 0) {
                lastNewsId = getLastNewsId(list);
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
     * @param callback 加载更多
     */
    @Override
    public void onLoadMore(LoadingCallBack<SubjectListBean> callback) {
        new DraftTopicListTask(callback).setTag(this).exe(group_id, lastNewsId);
    }

    /**
     * 下拉刷新
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
     * @return 获取最后一条的时间戳
     */
    private long getLastNewsId(List<SubjectItemBean> list) {
        return list.get(list.size() - 1).getId();
    }

    /**
     * @param itemView
     * @param position 点击专题列表
     *                 跳转到所类型有详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        //TODO WLJ 进入详情页
        BizUtils.jumpToDetailActivity2((SubjectItemBean) mAdapter.getData().get(position));
    }
}
