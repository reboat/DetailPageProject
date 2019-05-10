package com.zjrb.zjxw.detailproject.ui.subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.apibean.bean.SpecialGroupBean;

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
    }

    private void bindData(List<SpecialGroupBean> groupBeanList) {
        if (groupBeanList == null) {
            groupBeanList = new ArrayList<>();
        }
        viewPager.setAdapter(new SpecialPagerAdapter(getSupportFragmentManager(), groupBeanList));
        tabLayout.setViewPager(viewPager);
        if (mCurrentBean!=null){
            int position = findCurrentPosition(groupBeanList);
            viewPager.setCurrentItem(position);
        }
    }

    private int findCurrentPosition(List<SpecialGroupBean> groupBeanList){
        int currentPosition = 0;
        for (int i = 0; i <groupBeanList.size(); i++) {
            if (mCurrentBean.getGroup_id().equals(groupBeanList.get(i).getGroup_id())){
                currentPosition = i;
            }
        }
        return currentPosition;
    }

    private void initView() {
        if (mDraftDetailBean != null) {
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

    @Override
    public void onClick(View view) {
        if (view.getId() == ivTopBarBack.getId()) {
            super.onBackPressed();
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
                        .setColumn_id(mDraftDetailBean.getArticle().getChannel_id())
                        .setColumn_name(mDraftDetailBean.getArticle().getColumn_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDraftDetailBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mDraftDetailBean.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
                        .setNewsCard(true)
                        .setCardUrl(mDraftDetailBean.getArticle().getCard_url())
                        .setArticleId(mDraftDetailBean.getArticle().getId() + "")
                        .setImgUri(mDraftDetailBean.getArticle().getArticle_pic())
                        .setTextContent(mDraftDetailBean.getArticle().getSummary())
                        .setTitle(mDraftDetailBean.getArticle().getDoc_title())
                        .setAnalyticsBean(bean)
                        .setTargetUrl(mDraftDetailBean.getArticle().getUrl()).setEventName("NewsShare")
                        .setShareType("文章"));
            }
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
