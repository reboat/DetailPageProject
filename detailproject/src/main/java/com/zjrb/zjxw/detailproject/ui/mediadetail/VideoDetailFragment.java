package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.recycleView.EmptyPageHolder;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedNewsBean;
import com.zjrb.zjxw.detailproject.apibean.bean.RelatedSubjectsBean;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.ui.nomaldetail.adapter.NewsDetailAdapter;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.nav.Nav;
import cn.daily.news.biz.core.utils.RouteManager;

/**
 * 视频详情页详情相关
 * Created by wanglinjie.
 * create time:2019/3/22  下午3:21
 */
public class VideoDetailFragment extends DailyFragment {
    public static final String FRAGMENT_DETAIL_VIDEO = "fragment_detail_video";
    public static final String FRAGMENT_DETAIL_BEAN = "fragment_detail_bean";
    public static final String FRAGMENT_LIST_LISTENER = "fragment_list_listener";
    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    private DraftDetailBean mNewsDetail;
    public NewsDetailAdapter mAdapter;
    private Bundle bundle;
    private float mScale;
    private Analytics.AnalyticsBuilder builder;

    public NewsDetailAdapter getAdapter(){
        return mAdapter;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsDetail = (DraftDetailBean) getArguments().getSerializable(FRAGMENT_DETAIL_BEAN);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_video, container, false);
    }

    private void init() {
        if (mNewsDetail != null) {
            builder = DataAnalyticsUtils.get().pageStayTime(mNewsDetail);
        }
        List datas = new ArrayList<>();
        //添加头布局
        datas.add(mNewsDetail);
        //非直播有详情部分
        if (!mNewsDetail.getArticle().isNative_live()) {
            datas.add(mNewsDetail);
        } else {
            List<RelatedSubjectsBean> subjectList = mNewsDetail.getArticle().getRelated_subjects();
            if (subjectList != null && subjectList.size() > 0) {
                datas.add("相关专题");
                datas.addAll(subjectList);
            }
            List<RelatedNewsBean> articles = mNewsDetail.getArticle().getRelated_news();
            if (articles != null && articles.size() > 0) {
                datas.add("相关新闻");
                datas.addAll(articles);
            }
            datas.add("占位");
        }
        lvNotice.setLayoutManager(new LinearLayoutManager(getActivity()));
        lvNotice.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.color._dddddd_7a7b7d));
        mAdapter = new NewsDetailAdapter(datas, true);
        mAdapter.setEmptyView(
                new EmptyPageHolder(lvNotice,
                        EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")
                ).itemView);
        lvNotice.setAdapter(mAdapter);
    }

    public NewsDetailAdapter getmAdapter() {
        return mAdapter;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null && !mNewsDetail.getArticle().isNative_live()) {
            mAdapter.onWebViewResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null && !mNewsDetail.getArticle().isNative_live()) {
            mAdapter.onWebViewPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (builder != null) {
            //阅读深度
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                builder.setPercentage(mScale + "");
            }
            builder.readPercent(mScale + "");
            Analytics mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
    }

    //频道点击
    public void onOptClickChannel() {
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(IKey.CHANNEL_NAME, mNewsDetail.getArticle().getSource_channel_name());
        bundle.putString(IKey.CHANNEL_ID, mNewsDetail.getArticle().getSource_channel_id());
        Nav.with(UIUtils.getContext()).setExtras(bundle).toPath(RouteManager.SUBSCRIBE_PATH);
    }

    public void onReadingScaleChange(float scale) {
        mScale = scale;
    }

}
