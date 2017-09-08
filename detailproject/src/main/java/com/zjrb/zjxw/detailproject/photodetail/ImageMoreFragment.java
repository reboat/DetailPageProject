package com.zjrb.zjxw.detailproject.photodetail;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImageMoreAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更多图集
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class ImageMoreFragment extends BaseFragment implements OnItemClickListener {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    private ImageMoreAdapter mAdapter;
    private ListSpaceDivider diver;
    private static DraftDetailBean mBean;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static ImageMoreFragment newInstance(DraftDetailBean bean) {
        ImageMoreFragment fragment = new ImageMoreFragment();
        mBean = bean;
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
        View v = inflater.inflate(R.layout.module_detail_fragment_relate_news, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    /**
     * @param v 初始化适配器
     */
    private void initView(View v) {
        lvNotice.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        diver = new ListSpaceDivider(32, 0, false);
        lvNotice.addItemDecoration(diver);
        initAdapter();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mAdapter = new ImageMoreAdapter(mBean.getArticle().getRelated_news());
        mAdapter.setOnItemClickListener(this);
        lvNotice.setAdapter(mAdapter);
    }


    /**
     * @param itemView
     * @param position 更多图集点击
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity((DraftDetailBean) mAdapter.getData().get(position), position);
    }
}
