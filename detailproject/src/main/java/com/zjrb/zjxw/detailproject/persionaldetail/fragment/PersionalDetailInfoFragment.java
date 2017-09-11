package com.zjrb.zjxw.detailproject.persionaldetail.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalDetailBean;
import com.zjrb.zjxw.detailproject.eventBus.PersionalDetailTabEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalTrackAdapter;

import org.greenrobot.eventbus.EventBus;

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

    /**
     * 任职履历
     */
    public static final int TYPE_INFO = 1;
    private static int mIndex = -1;
    private PersionalTrackAdapter mAdapter;

    private OfficalDetailBean bean;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(Key.FRAGMENT_ARGS);
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
        mAdapter = new PersionalTrackAdapter(bean.getOfficer().getResumes());
//        mAdapter.setupData(bean.getOfficer().getResumes());
        ListSpaceDivider diver;
        lvNotice.setLayoutManager(new LinearLayoutManager(v.getContext()));
        diver = new ListSpaceDivider(0, 0, false);
        lvNotice.addItemDecoration(diver);
        lvNotice.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param isVisibleToUser isVisibleToUser:false 不可见
     *                        isVisibleToUser:true  可见
     *                        切换操作,可以进行UI操作
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        T.showShort(UIUtils.getContext(), "当前tab1_index=" + mIndex);
//        if (isVisibleToUser) {
//            EventBus.getDefault().postSticky(new PersionalDetailTabEvent());
//        }

    }

}
