package com.zjrb.zjxw.detailproject.ui.subject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;
import com.zjrb.zjxw.detailproject.apibean.task.SpecialDoFollowTask;
import com.zjrb.zjxw.detailproject.apibean.task.SpecialUnDoFollowTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.network.compatible.APIExpandCallBack;
import cn.daily.news.biz.core.network.task.DraftCollectTask;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;
import cn.daily.news.biz.core.ui.toast.ZBToast;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:专题更多页面
 */


public class SpecialMoreActivity extends DailyActivity implements View.OnClickListener {
    @BindView(R2.id.iv_share)
    ImageView ivShare;
    @BindView(R2.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R2.id.view_pager)
    CompatViewPager viewPager;
    @BindView(R2.id.iv_top_bg)
    ImageView ivTopBg;
    @BindView(R2.id.iv_top_bar_back)
    ImageView ivTopBarBack;
    @BindView(R2.id.iv_top_collect)
    ImageView ivTopCollect;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.special_tab_container)
    ViewGroup mTabContainer;
    @BindView(R2.id.tv_follow)
    TextView tvFollow;

    /**
     * 专题id
     */
    private DraftDetailBean mDraftDetailBean;
    private SpecialGroupBean mCurrentBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntentData(getIntent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_news_activity_activity_special_more);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        ivShare.setOnClickListener(this);
        ivTopBarBack.setOnClickListener(this);
        ivTopCollect.setOnClickListener(this);
        tvFollow.setOnClickListener(this);
    }

    private void bindData(List<SpecialGroupBean> groupBeanList) {
        if (groupBeanList == null) {
            groupBeanList = new ArrayList<>();
        }
        viewPager.setAdapter(new SpecialPagerAdapter(getSupportFragmentManager(), groupBeanList));
        if (groupBeanList.size() > 1) {
            tabLayout.setViewPager(viewPager);
            mTabContainer.setVisibility(View.VISIBLE);
        } else {
            mTabContainer.setVisibility(View.GONE);
        }
        if (mCurrentBean != null) {
            int position = findCurrentPosition(groupBeanList);
            viewPager.setCurrentItem(position);
        }
    }

    private int findCurrentPosition(List<SpecialGroupBean> groupBeanList) {
        int currentPosition = 0;
        for (int i = 0; i < groupBeanList.size(); i++) {
            if (mCurrentBean.getGroup_id().equals(groupBeanList.get(i).getGroup_id())) {
                currentPosition = i;
            }
        }
        return currentPosition;
    }

    private void initView() {
        if (mDraftDetailBean != null) {
            tvFollow.setVisibility(View.VISIBLE);
            tvFollow.setText(mDraftDetailBean.getArticle().traced?"已追踪":"追踪");
            bindData(mDraftDetailBean.getArticle().getSubject_groups());
            GlideApp.with(this).load(mDraftDetailBean.getArticle().getArticle_pic()).into(ivTopBg);
            tvTitle.setText(mDraftDetailBean.getArticle().getDoc_title());
            bindCollect();
        }
    }

    private void getIntentData(Intent intent) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().getSerializable(IKey.NEWS_DETAIL) != null) {
            mDraftDetailBean = (DraftDetailBean) intent.getExtras().getSerializable(IKey.NEWS_DETAIL);
        }
        if (intent != null && intent.getExtras() != null && intent.getExtras().getSerializable(IKey.DATA) != null) {
            mCurrentBean = (SpecialGroupBean) intent.getExtras().getSerializable(IKey.DATA);
        }
    }

    private void followTask() {
        if (mDraftDetailBean==null||mDraftDetailBean.getArticle()==null||TextUtils.isEmpty(mDraftDetailBean.getArticle().getUrl())){
            return;
        }
        String s = tvFollow.getText().toString();
        if ("已追踪".equals(s)){
            new SpecialUnDoFollowTask(new APIExpandCallBack<Void>() {
                @Override
                public void onSuccess(Void data) {
                    mDraftDetailBean.getArticle().traced = false;
                    tvFollow.setText(mDraftDetailBean.getArticle().traced?"已追踪":"追踪");
                    ZBToast.showShort(tvFollow.getContext(), "取消追踪成功");
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errMsg, int errCode) {
                    ZBToast.showShort(getActivity(), errMsg);
                }

            }).setTag(this).exe(mDraftDetailBean.getArticle().getUrl());
            if (mDraftDetailBean.getArticle()!=null){
                new Analytics.AnalyticsBuilder(getBaseContext(), "A0163", "", false)
                        .name("点击取消追踪")
                        .selfObjectID(mDraftDetailBean.getArticle().getId()+"")
                        .classShortName(mDraftDetailBean.getArticle().getChannel_name())
                        .objectShortName(mDraftDetailBean.getArticle().getDoc_title())
                        .objectType("C01")
                        .classID(mDraftDetailBean.getArticle().getChannel_id())
                        .pageType("专题详情页")
                        .ilurl(mDraftDetailBean.getArticle().getUrl())
                        .objectID(mDraftDetailBean.getArticle().getMlf_id()+"")
                        .build()
                        .send();
            }

        }else if ("追踪".equals(s)) {
            new SpecialDoFollowTask(new APIExpandCallBack<Void>() {
                @Override
                public void onSuccess(Void data) {
                    mDraftDetailBean.getArticle().traced = true;
                    tvFollow.setText(mDraftDetailBean.getArticle().traced?"已追踪":"追踪");
                    ZBToast.showShort(tvFollow.getContext(), "追踪成功！可在“我的追踪”中查看动态更新哦");
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String errMsg, int errCode) {
                    ZBToast.showShort(getActivity(), errMsg);
                }

            }).setTag(this).exe(mDraftDetailBean.getArticle().getUrl());
            if (mDraftDetailBean.getArticle()!=null){
                new Analytics.AnalyticsBuilder(getBaseContext(), "A0063", "", false)
                        .name("点击追踪")
                        .selfObjectID(mDraftDetailBean.getArticle().getId()+"")
                        .classShortName(mDraftDetailBean.getArticle().getChannel_name())
                        .objectShortName(mDraftDetailBean.getArticle().getDoc_title())
                        .objectType("C01")
                        .classID(mDraftDetailBean.getArticle().getChannel_id())
                        .pageType("专题详情页")
                        .ilurl(mDraftDetailBean.getArticle().getUrl())
                        .objectID(mDraftDetailBean.getArticle().getMlf_id()+"")
                        .build()
                        .send();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra(SpecialActivity.KEY_COLLECT,mDraftDetailBean.getArticle().isFollowed());
        intent.putExtra(SpecialActivity.KEY_FOLLOW,mDraftDetailBean.getArticle().traced);
        setResult(Activity.RESULT_OK,intent);
        super.onBackPressed();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == ivTopBarBack.getId()) {
            onBackPressed();
        } else if (view.getId() == ivTopCollect.getId()) {
            //未被收藏
            collectTask(); // 收藏
        } else if (view.getId() == ivShare.getId()) {
            if (!TextUtils.isEmpty(mDraftDetailBean.getArticle().getUrl())) {
                //分享专用bean
                OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                        .setObjectID(mDraftDetailBean.getArticle().getMlf_id() + "")
                        .setObjectName(mDraftDetailBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.C01).setUrl(mDraftDetailBean.getArticle().getUrl())
                        .setClassifyID(mDraftDetailBean.getArticle().getChannel_id() + "")
                        .setClassifyName(mDraftDetailBean.getArticle().getChannel_name())
                        .setColumn_id(String.valueOf(mDraftDetailBean.getArticle().getColumn_id()))
                        .setColumn_name(mDraftDetailBean.getArticle().getColumn_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDraftDetailBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mDraftDetailBean.getArticle().getId() + "");

                DraftDetailBean.ArticleBean mArticle = mDraftDetailBean.getArticle();
                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setCardUrl(mArticle.getCard_url())
                        .setArticleId(mArticle.getId() + "")
                        .setImgUri(mArticle.getFirstSubjectPic())
                        .setTextContent(mArticle.getSummary())
                        .setTitle(mArticle.getList_title())
                        .setAnalyticsBean(bean)
                        .setTargetUrl(mArticle.getUrl()).setEventName("NewsShare")
                        .setShareType("文章"));
            }
        }else if (view.getId() == tvFollow.getId()){
            followTask();
        }
    }

    /**
     * 专题收藏
     */
    private void collectTask() {
        new DraftCollectTask(new APIExpandCallBack<Void>() {

            @Override
            public void onSuccess(Void data) {
                if (mDraftDetailBean != null && mDraftDetailBean.getArticle() != null) {
                    mDraftDetailBean.getArticle().setFollowed(!mDraftDetailBean.getArticle().isFollowed());
                    bindCollect();
                    ZBToast.showShort(getActivity(), mDraftDetailBean.getArticle().isFollowed() ? "收藏成功" : "取消收藏成功");
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(String errMsg, int errCode) {
                //已收藏成功
                if (errCode == 50013) {
                    if (mDraftDetailBean != null && mDraftDetailBean.getArticle() != null) {
                        mDraftDetailBean.getArticle().setFollowed(true);
                        bindCollect();
                        ZBToast.showShort(getActivity(), "已收藏成功");
                    }
                } else {
                    ZBToast.showShort(getActivity(), errMsg);
                }
            }

        }).setTag(this).exe(mDraftDetailBean.getArticle().getId(), !mDraftDetailBean.getArticle().isFollowed(), mDraftDetailBean.getArticle().getUrl());
    }

    /**
     * 收藏状态
     */
    private void bindCollect() {
        if (mDraftDetailBean != null && mDraftDetailBean.getArticle() != null) {
            ivTopCollect.setSelected(mDraftDetailBean.getArticle().isFollowed());
        }
    }



}
