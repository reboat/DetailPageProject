package com.zjrb.zjxw.detailproject.photodetail;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.GridSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.photodetail.adapter.ImageMoreAdapter;

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
    private DraftDetailBean mBean;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static ImageMoreFragment newInstance(DraftDetailBean bean) {
        ImageMoreFragment fragment = new ImageMoreFragment();
        Bundle args = new Bundle();
        args.putSerializable(IKey.FRAGMENT_ARGS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBean = (DraftDetailBean) getArguments().getSerializable(IKey.FRAGMENT_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_atlas_more, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    /**
     * @param v 初始化适配器
     */
    private void initView(View v) {
        lvNotice.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        lvNotice.addItemDecoration(new GridSpaceDivider(6));
        initAdapter();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        if (mBean == null || mBean.getArticle() == null || mBean.getArticle().getRelated_news() == null || mBean.getArticle().getRelated_news().isEmpty())
            return;
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
        if (ClickTracker.isDoubleClick()) return;
        if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            Nav.with(UIUtils.getActivity()).to(((RelatedNewsBean) mAdapter.getData().get(position)).getUri_scheme());
        }
    }
}
