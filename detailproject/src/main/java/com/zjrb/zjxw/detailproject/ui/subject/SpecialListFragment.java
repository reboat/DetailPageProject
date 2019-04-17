package com.zjrb.zjxw.detailproject.ui.subject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.load.LoadingCallBack;
import com.zjrb.core.recycleView.listener.OnItemClickListener;
import com.zjrb.daily.news.R2;
import com.zjrb.daily.news.bean.ArticleItemBean;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.daily.news.ui.widget.NewsSpaceDivider;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SubjectListBean;
import com.zjrb.zjxw.detailproject.apibean.task.DraftTopicListTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.daily.news.biz.core.DailyFragment;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:
 */


public class SpecialListFragment extends DailyFragment {
    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    Unbinder unbinder;
    private String groupId;
    private SpecialListAdapter mAdapter;

    public static Fragment fragment(SpecialGroupBean specialGroupBean) {
        SpecialListFragment fragment = new SpecialListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", specialGroupBean.getGroup_id());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.module_news_special_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initArgs();
        return view;
    }

    private void initArgs() {
        Bundle args = getArguments();
        if (args != null) {
            groupId = args.getString("id");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {

    }

    private void loadData() {
        new DraftTopicListTask(new LoadingCallBack<SubjectListBean>() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {

            }

            @Override
            public void onSuccess(SubjectListBean bean) {
                bindData(bean);
            }

        }).setTag(this)
                .exe(groupId);
    }

    private void bindData(SubjectListBean bean) {
        final List<ArticleItemBean> articleItemBean = bean.getArticle_list();
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addItemDecoration(new NewsSpaceDivider(14,14));
        mAdapter = new SpecialListAdapter(articleItemBean);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                NewsUtils.itemClick(getFragment(),articleItemBean.get(position));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
