package com.zjrb.zjxw.detailproject.nomaldetail;

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
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.ArrayList;
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
//        getIntentData(getActivity().getIntent());
        initView();
        loadData();
        return v;
    }

//    /**
//     * @param intent 获取传递数据
//     */
//    private void getIntentData(Intent intent) {
//        if (intent != null && intent.getData() != null) {
//            Uri data = intent.getData();
//            column_id = data.getQueryParameter(Key.COLUMN_ID);
//        }
//    }

    /**
     * 显示撤稿文案
     */
    private TextView emptyText;
    /**
     * 头布局
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
        lvNotice.addItemDecoration(new ListSpaceDivider(2, UIUtils.getColor(R.color.dc_f5f5f5), true, true));

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        adapter.setOnItemClickListener(this);
        adapter.addHeaderView(head);
    }

    private List<DraftHotTopNewsBean.HotNewsBean> article_list;


    private DraftHotTopNewsBean mockTest() {
        DraftHotTopNewsBean bean = new DraftHotTopNewsBean();
        List<DraftHotTopNewsBean.HotNewsBean> list = new ArrayList<>();

        DraftHotTopNewsBean.HotNewsBean b = new DraftHotTopNewsBean.HotNewsBean();
        b.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b1 = new DraftHotTopNewsBean.HotNewsBean();
        b1.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b2 = new DraftHotTopNewsBean.HotNewsBean();
        b2.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b3 = new DraftHotTopNewsBean.HotNewsBean();
        b3.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b4 = new DraftHotTopNewsBean.HotNewsBean();
        b4.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b5 = new DraftHotTopNewsBean.HotNewsBean();
        b5.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b6 = new DraftHotTopNewsBean.HotNewsBean();
        b6.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b7 = new DraftHotTopNewsBean.HotNewsBean();
        b7.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b8 = new DraftHotTopNewsBean.HotNewsBean();
        b8.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b9 = new DraftHotTopNewsBean.HotNewsBean();
        b9.setList_title("热门1");

        DraftHotTopNewsBean.HotNewsBean b10 = new DraftHotTopNewsBean.HotNewsBean();
        b10.setList_title("热门1");
//        b10.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");

        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);
        list.add(b6);
        list.add(b7);
        list.add(b8);
        list.add(b9);
        list.add(b10);
        bean.setArticle_list(list);
        return bean;
    }

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
                if (bean.getResultCode() == 0) {//成功
                    article_list = mockTest().getArticle_list();//bean.getArticle_list();
                    if (article_list != null) {
                        if (adapter == null) {
                            adapter = new EmptyStateListAdapter(article_list);
                            initAdapter();
                        }
                        lvNotice.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    T.showShort(UIUtils.getContext(), bean.getResultMsg());
                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //TODO  WLJ  TEST
                article_list = mockTest().getArticle_list();//bean.getArticle_list();
                if (article_list != null) {
                    if (adapter == null) {
                        adapter = new EmptyStateListAdapter(article_list);
                        initAdapter();
                    }
                    lvNotice.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                T.showShort(UIUtils.getContext(), errMsg);
            }
        }).setTag(this).exe(column_id);
    }

    /**
     * @param itemView
     * @param position 进入详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
//        BizUtils.jumpToDetailActivity2((SubjectItemBean) adapter.getData().get(position));
    }

}
