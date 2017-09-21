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

import java.util.ArrayList;
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
            BizUtils.jumpToDetailActivity2(b);
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
                bean = draftDetailBean;
                bean = mockTest2();
                bean.getArticle().setSubject_groups(mockTest());
                fillData(bean);

//                if (draftDetailBean == null) {
//                    return;
//                } else {
//                    bean = draftDetailBean;
//                    fillData(draftDetailBean);
//                }
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
        if (draftTopicBean == null) return;
        //TODO WLJ 空态页面
        headHolder = new HeaderTopicHolder(mRvContent);
        headHolder.initData(draftTopicBean);

        //专题详情页列表

        if (mAdapter == null) {
            mAdapter = new NewsTopicAdapter();
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.setupData(mockTest());

//        if (mAdapter != null) {
//            mAdapter = new NewsTopicAdapter(bean.getArticle().getSubject_groups());
//            mAdapter.setOnItemClickListener(this);
//        }
//        mAdapter.setupData(draftTopicBean.getArticle().getSubject_groups());
        mAdapter.addHeaderView(headHolder.getItemView());
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

    private DraftDetailBean mockTest2() {
        DraftDetailBean b = new DraftDetailBean();
        DraftDetailBean.ArticleBean a = new DraftDetailBean.ArticleBean();
        a.setOpen(false);
        a.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        a.setList_title("说的科技时代会计核算地方就看水电费是肯德基发货速度就复健科水电费收款方");
        a.setSummary("框架按时间旷达科技阿斯达你阿卡家到那家卡萨的尼桑卡机发布啦发链接开始打放假啊啥大部分爱神的箭发神经的发布好的部分啊数据恢复就好sad吧房间号是房间号是发啥大部分啊是否被加深对房间号卡上饭卡圣诞节发布圣诞节开发别介啊卡水电费阿贾克斯饭卡加深对饭卡刷简单和放假阿斯达发斯蒂芬阿斯蒂芬建行卡都是放假卡上放假卡上大饭卡刷就饭卡刷就大富科技按时父控件是饭卡圣诞节和饭卡刷的回复把框架阿斯达饭卡圣诞节发");
        a.setSubject_focus_image("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setArticle(a);
        return b;
    }


    private List<SubjectNewsBean.GroupArticlesBean> mockTest() {
        List<SubjectNewsBean.GroupArticlesBean> subject_groups = new ArrayList<>();
        SubjectNewsBean.GroupArticlesBean group_articles = new SubjectNewsBean.GroupArticlesBean();
        group_articles.setGroupId(1);
        group_articles.setGroupName("标题1");

        List<SubjectItemBean> list = new ArrayList<>();
        SubjectItemBean b = new SubjectItemBean();
        b.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b.setList_title("子标题1");
        SubjectItemBean b1 = new SubjectItemBean();
        b1.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b1.setList_title("子标题1");
        SubjectItemBean b2 = new SubjectItemBean();
        b2.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        b2.setList_title("子标题1");
        list.add(b);
        list.add(b1);
        list.add(b2);
        group_articles.setArticleList(list);

        SubjectNewsBean.GroupArticlesBean group_articles1 = new SubjectNewsBean.GroupArticlesBean();
        group_articles1.setGroupId(2);
        group_articles1.setGroupName("标题2");

        List<SubjectItemBean> list1 = new ArrayList<>();
        SubjectItemBean bb = new SubjectItemBean();
        bb.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bb.setList_title("子标题2");
        SubjectItemBean bb1 = new SubjectItemBean();
        bb1.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bb1.setList_title("子标题2");
        SubjectItemBean bb2 = new SubjectItemBean();
        bb2.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bb2.setList_title("子标题2");
        list1.add(bb);
        list1.add(bb1);
        list1.add(bb2);
        group_articles1.setArticleList(list1);


        SubjectNewsBean.GroupArticlesBean group_articles2 = new SubjectNewsBean.GroupArticlesBean();
        group_articles2.setGroupId(3);
        group_articles2.setGroupName("标题3");

        List<SubjectItemBean> list2 = new ArrayList<>();
        SubjectItemBean bbb = new SubjectItemBean();
        bbb.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbb.setList_title("子标题3");
        SubjectItemBean bbb1 = new SubjectItemBean();
        bbb1.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbb1.setList_title("子标题3");
        SubjectItemBean bbb2 = new SubjectItemBean();
        bbb2.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbb2.setList_title("子标题3");
        list2.add(bbb);
        list2.add(bbb1);
        list2.add(bbb2);
        group_articles2.setArticleList(list2);


        SubjectNewsBean.GroupArticlesBean group_articles3 = new SubjectNewsBean.GroupArticlesBean();
        group_articles3.setGroupId(4);
        group_articles3.setGroupName("标题4");

        List<SubjectItemBean> list3 = new ArrayList<>();
        SubjectItemBean bbbb = new SubjectItemBean();
        bbbb.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbbb.setList_title("子标题3");
        SubjectItemBean bbbb1 = new SubjectItemBean();
        bbbb1.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbbb1.setList_title("子标题3");
        SubjectItemBean bbbb2 = new SubjectItemBean();
        bbbb2.setArticle_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        bbbb2.setList_title("子标题3");
        list3.add(bbbb);
        list3.add(bbbb1);
        list3.add(bbbb2);
        group_articles3.setArticleList(list3);

        subject_groups.add(group_articles);
        subject_groups.add(group_articles1);
        subject_groups.add(group_articles2);
        subject_groups.add(group_articles3);

        return subject_groups;

    }
}
