package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.UmengUtils.UmengShareBean;
import com.zjrb.core.ui.UmengUtils.UmengShareUtils;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.news.bean.type.DocType;
import com.zjrb.daily.news.other.NewsUtils;
import com.zjrb.daily.news.test.CreateData;
import com.zjrb.daily.news.test.CreateDataArticleList;
import com.zjrb.daily.news.test.TagTest;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.ArticleItemBean;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.global.RouteManager;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.subject.adapter.SpecialAdapter;
import com.zjrb.zjxw.detailproject.subject.holder.HeaderTopicHolder;
import com.zjrb.zjxw.detailproject.task.DraftCollectTask;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专题详情页
 *
 * @author a_liYa
 * @date 2017/10/12 上午8:51.
 */
public class SpecialActivity extends BaseActivity implements OnItemClickListener,
        HeaderTopicHolder.OnClickChannelListener {

    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    @BindView(R2.id.ly_container)
    ViewGroup lyContainer;
    @BindView(R2.id.recycler_copy)
    RecyclerView mRecyclerCopy;

    private SpecialAdapter mAdapter;

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
        setContentView(R.layout.module_detail_special);
        ButterKnife.bind(this);
        getIntentData(getIntent());
        initView();
        loadData();
    }

    private DefaultTopBarHolder1 topHolder;

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        topHolder.setViewVisible(topHolder.getShareView(), View.VISIBLE);
        topHolder.setViewVisible(topHolder.getCollectView(), View.VISIBLE);
        return topHolder.getView();
    }

    private void getIntentData(Intent intent) {
        if (intent != null && intent.getData() != null) {
            Uri data = intent.getData();
            if (data.getQueryParameter(IKey.ID) != null) {
                mArticleId = data.getQueryParameter(IKey.ID);
            }
        }
    }

    /**
     * 初始化专题详情页头部和列表信息
     */
    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5f, R.attr.bc_f5f5f5, true));
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (ClickTracker.isDoubleClick()) return;

        Object data = mAdapter.getData(position);
        if (data instanceof ArticleItemBean) {
            NewsUtils.itemClick(this, data);
        } else if (data instanceof SpecialGroupBean) {
            //进入专题更多列表
            Bundle bundle = new Bundle();
            bundle.putInt(IKey.GROUP_ID, ((SpecialGroupBean) data).getGroup_id());
            bundle.putString(IKey.TITLE, ((SpecialGroupBean) data).getGroup_name());
            Nav.with(this).setExtras(bundle).toPath(RouteManager.TOPIC_LIST);
        }
    }

    @OnClick({R2.id.iv_top_share, R2.id.iv_top_collect})
    public void onClick(View view) {
        if (ClickTracker.isDoubleClick()) return;
        if (view.getId() == R.id.iv_share) {
            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setImgUri(bean.getArticle().getArticle_pic())
                    .setTextContent(bean.getArticle().getSummary())
                    .setTitle(bean.getArticle().getList_title())
                    .setTargetUrl(bean.getArticle().getUrl()));
        } else {
            newsTopicCollect(); // 收藏
        }
    }

    /**
     * 加载专题数据
     */
    private void loadData() {
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                fillData(draftDetailBean);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //专题撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    showEmptyNewsDetail();
                } else {//其余页面
                    mRecycler.setVisibility(View.GONE);
                }
            }

        }).setTag(this).exe(mArticleId);
    }

    /**
     * 头holder
     */
    private HeaderTopicHolder headHolder;

    private void fillData(DraftDetailBean data) {

        // TODO: 2017/10/11 a_liYa mock
        DraftDetailBean.ArticleBean article = data.getArticle();
        article.setSummary
                ("习近平总书记党建新理念新思想新战略，是党中央治国理政新理念新思想新战略的核心组成部分，具有重要地位。学习贯彻党中央治国理政新理念新思想新战略，首先要把习近平总书记党建新理念新思想新战略学习好、领会好、贯彻好。");
        article.setSubject_focus_decription("夏宝龙：奋力打好小城镇环境综合整治具体的啦了工作在开化东阳调研时强调化东阳调研时的啦强化东");
        article.setSubject_focus_image(CreateData.getUrl());
        article.setSubject_pic(CreateData.getUrl());
        article.setSubject_focus_url("https://www.baidu.com");
        List<SpecialGroupBean> subject_groups = article.getSubject_groups();
        Random random = new Random();
        for (SpecialGroupBean group : subject_groups) {
            List<ArticleItemBean> articleList = new ArrayList();
            group.setGroup_articles(articleList);
            for (int i = 0; i < 5; i++) {
                ArticleItemBean bean = new ArticleItemBean();
                bean.setDoc_type(i % 10);
                bean.setList_style(i % 3 + 1);
                switch (i % 3) {
                    case 0:
                        bean.setColumn_name("新闻眼");
                        bean.setList_title("各方救援力量争分夺秒 救援一刻不停");
                        break;
                    case 1:
                        bean.setColumn_name("文化");
                        bean.setList_title("杭州丝绸现身一带一路杭州丝绸现高峰论谈会杭州丝绸峰论谈会峰");
                        break;
                    case 2:
                        bean.setColumn_name("科技组");
                        bean.setList_title("私享会投票：2015年电影大片哪部好看？网友都在期待中");
                        break;
                }
                bean.setList_tag(TagTest.tag(i));
                if (bean.getDoc_type() == DocType.LIVE) {
                    bean.setList_tag("");
                }

                bean.setLike_count(random.nextInt(1000000));
                bean.setRead_count(random.nextInt(1000000));

                bean.setList_pics(CreateData.getList());

                articleList.add(bean);
            }
        }

        subject_groups.addAll(subject_groups);
        subject_groups.addAll(subject_groups);

        //添加专题详情页的头部holder
        headHolder = new HeaderTopicHolder(mRecycler, mRecyclerCopy, this);
        headHolder.setData(data);
        mAdapter = new SpecialAdapter(data);
        mAdapter.addHeaderView(headHolder.getItemView());
        mAdapter.setOnItemClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * 专题收藏
     */
    private void newsTopicCollect() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void baseInnerData) {
                topHolder.getCollectView().setImageResource(R.mipmap.module_detail_collect_night);
                T.showShort(getBaseContext(), getString(R.string.module_detail_collect_success));
            }

            @Override
            public void onError(String errMsg, int errCode) {
                T.showShort(getBaseContext(), getString(R.string.module_detail_collect_failed));
            }

        }).setTag(this).exe(mArticleId, !bean.getArticle().isFollowed());
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        lyContainer.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.ly_container, EmptyStateFragment.newInstance(String.valueOf(bean.getArticle()
                .getColumn_id()))).commit();
    }

    @Override
    public void onClickChannel(SpecialGroupBean bean) {
        List data = mAdapter.getData();
        if (data != null && bean != null) {
            int index = data.indexOf(bean);
            LinearLayoutManager lm = (LinearLayoutManager) mRecycler.getLayoutManager();
            lm.scrollToPositionWithOffset(index + mAdapter.getHeaderCount(),
                    mRecyclerCopy.getHeight());
//            mRecycler.scrollToPosition(index + mAdapter.getHeaderCount());
            Log.e("TAG", "index " + index);
        }

    }

}
