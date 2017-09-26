package com.zjrb.zjxw.detailproject.persionaldetail.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalTrackAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 官员履历fragment
 * Created by wanglinjie.
 * create time:2017/7/17  上午10:14
 */
public class PersionalDetailInfoFragment extends BaseFragment {

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
            bean = (OfficalDetailBean) getArguments().getSerializable(Key.FRAGMENT_PERSIONAL_INFO);
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
        //TODO WLJ 显示空态页面
        if (bean == null || bean.getOfficer() == null || bean.getOfficer().getResumes() == null || bean.getOfficer().getResumes().isEmpty()) {
            return;
        }
        mAdapter = new PersionalTrackAdapter();
        mAdapter.setupData(bean.getOfficer().getResumes());
        lvNotice.setLayoutManager(new LinearLayoutManager(v.getContext()));
        diver = new ListSpaceDivider(0, 0, false);
        lvNotice.addItemDecoration(diver);
        lvNotice.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
