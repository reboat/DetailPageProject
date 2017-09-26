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
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.domain.base.BaseInnerData;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.core.utils.click.ClickTracker;
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
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;
import com.zjrb.zjxw.detailproject.utils.BizUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新闻专题 详情页面
 * Created by wanglinjie.
 * create time:2017/7/25  上午11:24
 */

public class NewsTopicActivity extends BaseActivity implements OnItemClickListener {
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
    private String mArticleId = "";

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

    /**
     * topbar
     */
    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        topHolder.setViewVisible(topHolder.getCollectView(), View.VISIBLE);
        return topHolder.getView();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(Key.ID) != null) {
                mArticleId = data.getQueryParameter(Key.ID);
            }
        }
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
            if (mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
                Nav.with(UIUtils.getActivity()).to(Uri.parse(b.getUrl())
                        .buildUpon()
                        .appendQueryParameter(Key.VIDEO_PATH, b.getVideo_url())//视频地址
                        .appendQueryParameter(Key.ID, String.valueOf(b.getId()))
                        .build(), 0);
            }
        } else if (mAdapter.getData().get(position) instanceof SubjectNewsBean.GroupArticlesBean) {
            //进入专题更多列表
            if (((SubjectNewsBean.GroupArticlesBean) mAdapter.getData().get(position)).getArticleList().size() >= 3) {
                SubjectNewsBean.GroupArticlesBean b = (SubjectNewsBean.GroupArticlesBean) mAdapter.getData().get(position);
                Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/TopicListActivity")
                        .buildUpon()
                        .appendQueryParameter(Key.ID, String.valueOf(b.getGroupId()))
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
        if (mAdapter.datas != null && !mAdapter.datas.isEmpty()) {
            for (SubjectItemBean bean : (List<SubjectItemBean>) mAdapter.datas) {
                if (bean.getId() == event.getType()) {
                    if (mAdapter.datas.size() >= (bean.getPosition() + 1)) {
                        ((LinearLayoutManager) mRvContent.getLayoutManager()).scrollToPositionWithOffset(bean.getPosition() + 1, 0);
                        ((LinearLayoutManager) mRvContent.getLayoutManager()).setStackFromEnd(true);
                    }
                    break;
                }
            }
        }

    }


    @OnClick({R2.id.iv_top_share, R2.id.iv_top_collect})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.iv_share) {
            //TODO WLJ 分享
            T.showShort(NewsTopicActivity.this, "分享");
        } else {
            //收藏
            newsTopicCollect();
        }
    }

    /**
     * 加载专题数据
     */
    private void loadData() {
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                //TODO  WLJ 显示空态页面
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
        //添加专题详情页的头部holder
        headHolder = new HeaderTopicHolder(mRvContent);
        headHolder.initData(draftTopicBean);
        mAdapter.addHeaderView(headHolder.getItemView());

        //专题详情页列表
        if (mAdapter == null) {
            mAdapter = new NewsTopicAdapter();
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.setupData(bean.getArticle().getSubject_groups());
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 专题收藏
     */
    private void newsTopicCollect() {
        new DraftCollectTask(new APIExpandCallBack<BaseInnerData>() {

            @Override
            public void onSuccess(BaseInnerData baseInnerData) {
                //TODO  WLJ  少图片
                topHolder.getCollectView().setImageResource(R.mipmap.module_detail_collect_night);
                T.showShort(getBaseContext(), "收藏成功");
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), "收藏失败");
            }

        }).setTag(this).exe(mArticleId);
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
