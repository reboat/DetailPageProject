package com.zjrb.zjxw.detailproject.nomaldetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseFragment;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftHotTopNewsBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.EmptyStateListAdapter;
import com.zjrb.zjxw.detailproject.task.DraftRankListTask;

import java.util.List;

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
    private String column_id;

    private EmptyStateListAdapter adapter;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static EmptyStateFragment newInstance(String columnId) {
        EmptyStateFragment fragment = new EmptyStateFragment();
        Bundle args = new Bundle();
        args.putString(Key.COLUMN_ID, columnId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            column_id = getArguments().getString(Key.COLUMN_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_persional_info, container, false);
        ButterKnife.bind(this, v);
        initView();
        loadData();
        return v;
    }

    /**
     * 显示撤稿文案
     */
    private TextView emptyText;
    /**
     * 头部布局
     */
    private View head;

    /**
     * 初始化控件
     */
    private void initView() {
        //添加头布局
        head = UIUtils.inflate(R.layout.moduel_detail_empty_state_head);
        emptyText = (TextView) head.findViewById(R.id.tv_empty_states);
        emptyText.setText(getString(R.string.module_detail_revoke));
        lvNotice.setLayoutManager(new LinearLayoutManager(getContext()));
        lvNotice.addItemDecoration(new ListSpaceDivider(2, UIUtils.getActivity().getResources().getColor(R.color.dc_f5f5f5), true, true));

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        adapter.setOnItemClickListener(this);
        adapter.addHeaderView(head);
    }

    private List<DraftHotTopNewsBean.HotNewsBean> article_list;


    /**
     * 获取频道热门列表
     */
    private void loadData() {
        new DraftRankListTask(new APIExpandCallBack<DraftHotTopNewsBean>() {
            @Override
            public void onSuccess(DraftHotTopNewsBean bean) {
                if (bean == null) {
                    return;
                }
                article_list = bean.getArticle_list();
                if (article_list != null) {
                    if (adapter == null) {
                        adapter = new EmptyStateListAdapter(article_list);
                        initAdapter();
                        lvNotice.setAdapter(adapter);
                    } else {
                        adapter.setData(article_list);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(UIUtils.getContext(), errMsg);
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(lvNotice)).exe(column_id);
    }

    /**
     * @param itemView
     * @param position 进入详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (adapter.getData() != null && !adapter.getData().isEmpty()) {
            Nav.with(UIUtils.getActivity()).to(Uri.parse(((SubjectItemBean) adapter.getData().get(position)).getUrl())
                    .buildUpon()
                    .appendQueryParameter(Key.VIDEO_PATH, ((SubjectItemBean) adapter.getData().get(position)).getVideo_url())//视频地址
                    .appendQueryParameter(Key.ID, String.valueOf(((SubjectItemBean) adapter.getData().get(position)).getId()))
                    .build(), 0);
        }

    }

}
