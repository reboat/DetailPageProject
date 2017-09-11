package com.zjrb.zjxw.detailproject.subjectdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.ExpandableTextView;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SubjectItemBean;
import com.zjrb.zjxw.detailproject.bean.SubjectNewsBean;
import com.zjrb.zjxw.detailproject.eventBus.ChannelItemClickEvent;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.subjectdetail.adapter.NewsTopicAdapter;
import com.zjrb.zjxw.detailproject.subjectdetail.holder.HeaderTopicHolder;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻专题 详情页面
 * Created by wanglinjie.
 * create time:2017/7/25  上午11:24
 */

public class NewsTopicActivity extends BaseActivity implements
        ExpandableTextView.OnLineCountListener, OnItemClickListener {
    @BindView(R2.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R2.id.ly_container)
    LinearLayout lyContainer;


    /**
     * 专题详情页适配器
     */
    private NewsTopicAdapter mAdapter;

    /**
     * 稿件ID
     */
    public int mArticleId = -1;
    /**
     * 媒立方ID
     */
    public int mlfId = -1;

    /**
     * 专题详情页数据
     */
    private DraftDetailBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_topic);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            mArticleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
            mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
        }
    }

    /**
     * 加载专题数据
     */
    private void loadData() {
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null) {
                    return;
                } else {
                    bean = draftDetailBean;
                    fillData(draftDetailBean);
                }

            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), errMsg);
            }

            @Override
            public void onAfter() {
            }

        }).setTag(this).exe(mArticleId);
    }

    /**
     * 头holder
     */
    private HeaderTopicHolder headHolder;

    /**
     * @param draftTopicBean 填充数据
     */
    private void fillData(DraftDetailBean draftTopicBean) {
        headHolder = new HeaderTopicHolder(mRvContent);
        headHolder.initData(draftTopicBean);

        //专题详情页列表
        if (mAdapter != null) {
            mAdapter = new NewsTopicAdapter(bean.getArticle().getSubject_groups());
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.setupData(draftTopicBean.getArticle().getSubject_groups());
        mAdapter.addHeaderView(headHolder.getItemView());
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 初始化专题详情页头部和列表信息
     */
    private void initView() {
        //专题列表
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new ListSpaceDivider(0.5f, UIUtils.getColor(R.color.dc_f5f5f5), true, true));
    }

    /**
     * @param itemView
     * @param *专题详情页item点击事件
     */
    @Override
    public void onItemClick(View itemView, int position) {
        //点击跳转详情页(所有类型)
        if (mAdapter.getData().get(position) instanceof SubjectItemBean) {
            SubjectItemBean b = (SubjectItemBean) mAdapter.getData().get(position);
            BizUtils.jumpToDetailActivity2(b);
        } else if (mAdapter.getData().get(position) instanceof SubjectNewsBean.GroupArticlesBean) {
            //进入专题更多列表
            if (((SubjectNewsBean.GroupArticlesBean) mAdapter.getData().get(position)).getArticleList().size() >= 3) {
                SubjectNewsBean.GroupArticlesBean b = (SubjectNewsBean.GroupArticlesBean) mAdapter.getData().get(position);
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/TopicListActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(b.getGroupId()))
                        .build(), 0);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param event 点击channel 滚动到相关位置
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(ChannelItemClickEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        SubjectNewsBean.GroupArticlesBean b = event.getData();
        if (b != null && mRvContent != null && mAdapter != null && mAdapter.getData().size() > 0) {
            for (SubjectItemBean bean : b.getArticleList()) {
                if (bean.getDoc_type() == -1 && bean.getId() == b.getGroupId()) {
                    mRvContent.scrollToPosition(bean.getPosition());
                }
            }


        }
    }

    @Override
    public void onLineCount(int lineCount, int maxLines) {

    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        lyContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(bean.getArticle().getColumn_id()))).commit();
    }
}
