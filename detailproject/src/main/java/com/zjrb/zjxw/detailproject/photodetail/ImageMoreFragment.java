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
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mIndex = getArguments().getInt(Key.FRAGMENT_ARGS);
//            list = ((OfficalDetailBean) getArguments().getSerializable(Key.FRAGMENT_PERSIONAL_RELATER)).getArticle_list();
//            official_id = getArguments().getInt(Key.OFFICIAL_ID);
//        }
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
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
//        mAdapter = new PersionalRelateNewsAdapter(list);
        mAdapter.setOnItemClickListener(this);
        lvNotice.setAdapter(mAdapter);
    }


//    private void initData() {
//        new OfficalDetailTask(new APIExpandCallBack<OfficalDetailBean>() {
//
//            @Override
//            public void onSuccess(OfficalDetailBean data) {
//                if (data == null) {
//                    return;
//                }
//                if (data.getResultCode() == 0) {//成功
//                    list = data.getArticle_list();
//                    if (list != null) {
//                        if (mAdapter == null) {
//                            mAdapter = new PersionalRelateNewsAdapter(list);
//                            initAdapter();
//                        }
//                        lvNotice.setAdapter(mAdapter);
//                        mAdapter.setData(list);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    T.showShort(getContext(), data.getResultMsg());
//                }
//            }
//
//            @Override
//            public void onError(String errMsg, int errCode) {
//                T.showShort(getContext(), errMsg);
//            }
//
//            @Override
//            public void onAfter() {
//            }
//        }).setTag(this).exe(official_id + "", "", "20");
//    }

    /**
     * @param itemView
     * @param position 更多图集点击
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity2((SubjectItemBean) mAdapter.getData().get(position), position);
    }
}
