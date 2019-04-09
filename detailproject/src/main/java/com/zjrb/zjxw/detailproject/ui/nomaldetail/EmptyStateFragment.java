package com.zjrb.zjxw.detailproject.ui.nomaldetail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsLinearLayout;
import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftHotTopNewsBean;
import com.zjrb.zjxw.detailproject.apibean.task.DraftRankListTask;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.EmptyStateListAdapter;
import com.zjrb.zjxw.detailproject.ui.photodetail.AtlasDetailActivity;
import com.zjrb.zjxw.detailproject.ui.topic.ActivityTopicActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.nav.Nav;

/**
 * 撤稿空态页面
 * Created by wanglinjie.
 * create time:2017/9/2  下午9:53
 */

public class EmptyStateFragment extends DailyFragment implements OnItemClickListener {
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;
    @BindView(R2.id.layout_title_bar)
    FitWindowsFrameLayout viewGroup;
    @BindView(R2.id.ly_container)
    FitWindowsLinearLayout mContainer;



    private EmptyStateListAdapter adapter;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static EmptyStateFragment newInstance() {
        EmptyStateFragment fragment = new EmptyStateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_empty, container, false);
        ButterKnife.bind(this, v);
        initView();
        loadData();
        return v;
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //添加头布局
        if (UIUtils.getActivity() instanceof AtlasDetailActivity || UIUtils.getActivity() instanceof ActivityTopicActivity) {
            viewGroup.setVisibility(View.VISIBLE);
        }
        lvNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        lvNotice.addItemDecoration(new ListSpaceDivider(0.5f, R.color._dddddd_343434, true, true));
    }

    private List<DraftHotTopNewsBean.HotNewsBean> article_list;


    @OnClick({R2.id.iv_top_bar_back})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.iv_top_bar_back) {
            getActivity().finish();
        }
    }

    /**
     * 获取频道热门列表
     */
    private void loadData() {
        new DraftRankListTask(new LoadingCallBack<DraftHotTopNewsBean>() {
            @Override
            public void onSuccess(DraftHotTopNewsBean bean) {
                if (bean == null) {
                    return;
                }
                article_list = bean.getArticle_list();
                if (article_list != null) {
                    if (adapter == null) {
                        adapter = new EmptyStateListAdapter(article_list);
                        adapter.setOnItemClickListener(EmptyStateFragment.this);
                        lvNotice.setAdapter(adapter);
                    } else {
                        adapter.setData(article_list);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), errMsg);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(lvNotice)).exe();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (adapter.getData(position) != null && !TextUtils.isEmpty(adapter.getData(position).getUrl())) {
            Nav.with(UIUtils.getActivity()).to(adapter.getData(position).getUrl());
        }
    }
}
