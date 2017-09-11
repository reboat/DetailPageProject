package com.zjrb.zjxw.detailproject.holder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseRecyclerViewHolder;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.nomaldetail.adapter.NewsRelatedSubjectAdapter;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 详情页相关专题Holder
 * Created by wanglinjie.
 * create time:2017/7/28  下午12:28
 */

public class NewsDetailRelatedSubjectHolder extends BaseRecyclerViewHolder<DraftDetailBean> implements OnItemClickListener {
    @BindView(R2.id.rv_content)
    RecyclerView mRecyleView;
    @BindView(R2.id.tv_related)
    TextView tvRelated;
    @BindView(R2.id.ly_container)
    LinearLayout lyContainer;

    private NewsRelatedSubjectAdapter adapter;

    public NewsDetailRelatedSubjectHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_subject_news, parent, false));
        ButterKnife.bind(this, itemView);
        initView();
    }

    private void initView() {
        mRecyleView.addItemDecoration(new ListSpaceDivider(20, 0, false));
        mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }


    /** mock数据
     * @return
     */
    private List<RelatedSubjectsBean> mockText() {
        List<RelatedSubjectsBean> list = new ArrayList<>();
        RelatedSubjectsBean b1 = new RelatedSubjectsBean();
        b1.setPic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        RelatedSubjectsBean b2 = new RelatedSubjectsBean();
        b2.setPic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        RelatedSubjectsBean b3 = new RelatedSubjectsBean();
        b3.setPic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        RelatedSubjectsBean b4 = new RelatedSubjectsBean();
        b4.setPic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        RelatedSubjectsBean b5 = new RelatedSubjectsBean();
        b5.setPic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        list.add(b5);

        return list;
    }


    @Override
    public void bindView() {
        itemView.setOnClickListener(null);
        List<RelatedSubjectsBean> list = mockText();
        tvRelated.setText("推荐专题");
        adapter = new NewsRelatedSubjectAdapter(list);
        adapter.setOnItemClickListener(this);
        mRecyleView.setAdapter(adapter);

        //TODO  WLJ
//        if (mData == null || mData.getArticle().getRelated_subjects() == null || mData.getArticle().getRelated_subjects().isEmpty()) {
//            lyContainer.setVisibility(View.GONE);
//        } else {
//            tvRelated.setText("推荐专题");
//            mRecyleView.addItemDecoration(new ListSpaceDivider(32, 0, false));
//            mRecyleView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext(),
//                    LinearLayoutManager.VERTICAL, false));
//            adapter = new NewsRelatedSubjectAdapter(mData.getArticle().getRelated_subjects());
//            adapter.setOnItemClickListener(this);
//            mRecyleView.setAdapter(adapter);
//        }
    }

    /**
     * @param itemView
     * @param position 跳转详情页
     */
    @Override
    public void onItemClick(View itemView, int position) {
        BizUtils.jumpToDetailActivity(mData);
    }
}
