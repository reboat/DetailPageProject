package com.zjrb.zjxw.detailproject.ui.photodetail;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.ui.photodetail.adapter.ImageMoreAdapter;
import com.zjrb.zjxw.detailproject.utils.DataAnalyticsUtils;
import com.zjrb.zjxw.detailproject.widget.VideoGridSpaceDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.DailyFragment;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;

/**
 * 更多图集
 * Created by wanglinjie.
 * create time:2017/8/27  上午10:14
 */
public class ImageMoreFragment extends DailyFragment {

    @BindView(R2.id.lv_notice)
    RecyclerView lvNotice;

    private ImageMoreAdapter mAdapter;
    private DraftDetailBean mBean;

    /**
     * 创建实例
     *
     * @return 实例对象
     */
    public static ImageMoreFragment newInstance(DraftDetailBean bean) {
        ImageMoreFragment fragment = new ImageMoreFragment();
        Bundle args = new Bundle();
        args.putSerializable(IKey.FRAGMENT_ARGS, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBean = (DraftDetailBean) getArguments().getSerializable(IKey.FRAGMENT_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.module_detail_fragment_atlas_more, container, false);
        ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    /**
     * @param v 初始化适配器
     */
    private void initView(View v) {
        lvNotice.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        lvNotice.addItemDecoration(new VideoGridSpaceDivider(6));
        initAdapter();
    }

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        if (mBean == null || mBean.getArticle() == null || mBean.getArticle().getRelated_news() == null || mBean.getArticle().getRelated_news().isEmpty())
            return;
        mAdapter = new ImageMoreAdapter(mBean.getArticle().getRelated_news());
        mAdapter.setDetailBean(mBean);
        lvNotice.setAdapter(mAdapter);
    }

    @OnClick(R2.id.iv_back)
    public void onBack() {
        if (getActivity() != null) {
            if (mBean != null && mBean.getArticle() != null) {
                DataAnalyticsUtils.get().ClickBack(mBean);
            }
            getActivity().finish();
        }
    }

    @OnClick(R2.id.iv_share)
    public void onShare() {
        if (null != mBean && null != mBean.getArticle() && !TextUtils.isEmpty(mBean.getArticle().getUrl())) {
            //分享专用bean
            OutSizeAnalyticsBean bean = OutSizeAnalyticsBean.getInstance()
                    .setObjectID(mBean.getArticle().getMlf_id() + "")
                    .setObjectName(mBean.getArticle().getDoc_title())
                    .setObjectType(ObjectType.C01).setUrl(mBean.getArticle().getUrl())
                    .setClassifyID(mBean.getArticle().getChannel_id() + "")
                    .setClassifyName(mBean.getArticle().getChannel_name())
                    .setColumn_id(String.valueOf(mBean.getArticle().getColumn_id()))
                    .setColumn_name(mBean.getArticle().getColumn_name())
                    .setPageType("新闻详情页")
                    .setOtherInfo(Analytics.newOtherInfo()
                            .put("relatedColumn", mBean.getArticle().getColumn_id() + "")
                            .put("subject", "")
                            .toString())
                    .setSelfobjectID(mBean.getArticle().getId() + "");

            UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                    .setSingle(false)
                    .setNewsCard(true)
                    .setCardUrl(mBean.getArticle().getCard_url())
                    .setArticleId(mBean.getArticle().getId() + "")
                    .setImgUri(mBean.getArticle().getAlbum_image_list().get(0).getImage_url())
                    .setTextContent(mBean.getArticle().getAlbum_image_list().get(0)
                            .getDescription())
                    .setTitle(mBean.getArticle().getDoc_title())
                    .setTargetUrl(mBean.getArticle().getUrl())
                    .setAnalyticsBean(bean).setEventName("NewsShare")
                    .setShareType("文章")
            );
        }
    }

    private Analytics mAnalytics;

    @Override
    public void onResume() {
        super.onResume();
        DraftDetailBean.ArticleBean articleBean = mBean.getArticle();
        mAnalytics = new Analytics.AnalyticsBuilder(getContext(), "A0010", "PicturePageStay", true)
                .name("打开“更多图集”页面")
                .selfObjectID(String.valueOf(articleBean.getId()))
                .columnID(String.valueOf(articleBean.getColumn_id()))
                .classShortName(articleBean.getChannel_name())
                .objectShortName(articleBean.getDoc_title())
                .classID(articleBean.getChannel_id())
                .pageType("更多图集页")
                .ilurl(articleBean.getUrl())
                .seObjectType(ObjectType.C01)
                .objectID(String.valueOf(articleBean.getMlf_id()))
                .columnName(articleBean.getColumn_name())
                .selfNewsID(String.valueOf(articleBean.getId()))
                .pubUrl(articleBean.getUrl())
                .selfChannelID(articleBean.getChannel_id())
                .newsID(String.valueOf(articleBean.getMlf_id()))
                .newsTitle(articleBean.getDoc_title())
                .channelName(articleBean.getChannel_name())
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAnalytics.sendWithDuration();
    }
}
