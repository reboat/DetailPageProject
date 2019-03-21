package com.zjrb.zjxw.detailproject.ui.persionaldetail.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.ui.divider.ListSpaceDivider;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.ui.persionaldetail.adapter.PersionalTrackAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.IKey;

/**
 * 官员履历fragment
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalDetailInfoFragment extends DailyFragment {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    private ListSpaceDivider diver;

    /**
     * 任职履历
     */
    public static final int TYPE_INFO = 1;
    private PersionalTrackAdapter mAdapter;

    private OfficalDetailBean bean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (OfficalDetailBean) getArguments().getSerializable(IKey.FRAGMENT_PERSIONAL_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_persional_info, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    /**
     * 初始化适配器
     */
    private void initView(View v) {
        lvNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PersionalTrackAdapter();
        mAdapter.setupData(bean.getOfficer().getResumes());
        mAdapter.setEmptyView(
                new EmptyPageHolder(lvNotice,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("")
                ).itemView);
        diver = new ListSpaceDivider(0, 0, false);
        lvNotice.addItemDecoration(diver);
        lvNotice.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
