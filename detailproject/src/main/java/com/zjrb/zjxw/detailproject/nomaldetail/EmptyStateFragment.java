package com.zjrb.zjxw.detailproject.nomaldetail;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.EmptyStateListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 撤稿空态页面
 * Created by wanglinjie.
 * create time:2017/9/2  下午9:53
 */

public class EmptyStateFragment extends BaseFragment implements OnItemClickListener {
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    /**
     * 栏目id
     */
    private int column_id;

    private EmptyStateListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_persional_info, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private TextView emptyText;
    private View head;

    private void initView() {
        head = UIUtils.inflate(R.layout.moduel_detail_empty_state_head);
        emptyText = (TextView) head.findViewById(R.id.tv_empty_states);

    }

    private void initAdapter() {
//        adapter = new PersionalTrackAdapter();
        adapter.addHeaderView(head);
        lvNotice.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }
}
