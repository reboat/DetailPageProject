package com.zjrb.zjxw.detailproject.subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliya.view.ratio.RatioRelativeLayout;
import com.google.gson.Gson;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.core.utils.T;
import com.zjrb.daily.news.ui.widget.SlidingTabLayout;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.bean.SpecialGroupBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.daily.news.analytics.Analytics;
import cn.daily.news.biz.core.DailyActivity;
import cn.daily.news.biz.core.constant.IKey;
import cn.daily.news.biz.core.network.compatible.APIExpandCallBack;
import cn.daily.news.biz.core.network.task.DraftCollectTask;
import cn.daily.news.biz.core.share.OutSizeAnalyticsBean;
import cn.daily.news.biz.core.share.UmengShareBean;
import cn.daily.news.biz.core.share.UmengShareUtils;

/**
 * @author: lujialei
 * @date: 2019/3/7
 * @describe:
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
    @BindView(R2.id.app_bar)
    AppBarLayout appBar;
    @BindView(R2.id.tl_top)
    View tlTop;

    /**
     * 专题id
     */
    private DraftDetailBean mDraftDetailBean;

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
        appBar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.e("lujialei","offset==="+Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
                float percent = Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange();
                tlTop.setAlpha(percent);
                tvTitle.setAlpha(1-percent);
            }
        });
    }

    private void bindData(List<SpecialGroupBean> groupBeanList) {
        if (groupBeanList == null) {
            groupBeanList = new ArrayList<>();
        }
        viewPager.setAdapter(new SpecialPagerAdapter(getSupportFragmentManager(), groupBeanList));
        tabLayout.setViewPager(viewPager);
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
        // TODO: 2019/3/14 模拟
        String mock = "{\n" +
                "\t\t\"article\": {\n" +
                "\t\t\t\"id\": 1114462,\n" +
                "\t\t\t\"mlf_id\": 9203388,\n" +
                "\t\t\t\"list_title\": \"人才兴企人才兴市 绍兴发放千张人才“一卡通”\",\n" +
                "\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552088445177_5c82fd7d159bb8144ed310c0.jpeg\"],\n" +
                "\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\"doc_type\": 5,\n" +
                "\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\"read_count\": 451470,\n" +
                "\t\t\t\"read_count_general\": \"45.1万+阅读\",\n" +
                "\t\t\t\"url\": \"https://zj.zjol.com.cn/subject.html?id=1114462\",\n" +
                "\t\t\t\"fixed_number\": 15,\n" +
                "\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\"followed\": false,\n" +
                "\t\t\t\"liked\": false,\n" +
                "\t\t\t\"like_enabled\": false,\n" +
                "\t\t\t\"comment_level\": 0,\n" +
                "\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb50\",\n" +
                "\t\t\t\"channel_name\": \"头条\",\n" +
                "\t\t\t\"channel_code\": \"toutiao\",\n" +
                "\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\"doc_title\": \"用心用情“三服务”\",\n" +
                "\t\t\t\"article_pic\": \"https://stc-new.8531.cn/assets/20190309/1552088445177_5c82fd7d159bb8144ed310c0.jpeg\",\n" +
                "\t\t\t\"source\": \"\",\n" +
                "\t\t\t\"author\": \"\",\n" +
                "\t\t\t\"summary\": \"我省各地正积极开展“服务企业、服务群众、服务基层”活动，为企业、群众、基层排忧解难。\",\n" +
                "\t\t\t\"content\": \"\",\n" +
                "\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\"sort_number\": 1551923605196,\n" +
                "\t\t\t\"published_at\": 1547020320000,\n" +
                "\t\t\t\"subject_display_id\": 0,\n" +
                "\t\t\t\"subject_display_type\": 1,\n" +
                "\t\t\t\"subject_groups\": [{\n" +
                "\t\t\t\t\"group_id\": \"9527\",\n" +
                "\t\t\t\t\"group_name\": \"聚焦“三服务”\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1152965,\n" +
                "\t\t\t\t\t\"mlf_id\": 9624475,\n" +
                "\t\t\t\t\t\"list_title\": \"浙江日报丨人才兴企人才兴市 绍兴发放千张人才“一卡通”\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552087000227_5c82f7d8159bb8144ed31050.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 175,\n" +
                "\t\t\t\t\t\"like_count_general\": \"175赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152965\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"53845b81e4b08e9fb1cdfccf\",\n" +
                "\t\t\t\t\t\"channel_name\": \"绍兴\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shaoxing\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"浙江日报丨人才兴企人才兴市 绍兴发放千张人才“一卡通”\",\n" +
                "\t\t\t\t\t\"summary\": \"就医不用排队，可享专家就诊，子女就学有‘教育绿卡’……这张卡简直太贴心了。在3月8日举行的活力绍兴智引全球2019春季引才新闻发布会上，浙江万丰科技开发股份有限公司研究院院长郑建明接过绍兴市发放的首张高层次人才服务一卡通，高兴地说。记者了解到，一卡通是今年绍兴为高层次人才量身定制的城市服务卡，人才凭卡可享受医疗保健、子女教育、文体休闲、交通出行、出入境签证、创业创新等一揽子优惠政策与便捷服务，首批向1000名高层次人才发放。新落成的海智汇·绍兴国际人才创业创新服务中心，作为绍兴人才之家，这里能让来绍兴创业就业的人才深入了解绍兴、寻觅心仪工作、办理人才业务、获得创业培训、寻找项目合作等。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552086860191,\n" +
                "\t\t\t\t\t\"published_at\": 1552086860000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1149274,\n" +
                "\t\t\t\t\t\"mlf_id\": 9591112,\n" +
                "\t\t\t\t\t\"list_title\": \"\u200B持续阴雨影响蜜蜂养殖 长兴为蜂农请来技术“外援”\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190305/1551742265186_5c7db539159bb8144ed2bfa9.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 123,\n" +
                "\t\t\t\t\t\"like_count_general\": \"123赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1149274\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"584f5311e4aaf2b9de82187e\",\n" +
                "\t\t\t\t\t\"channel_name\": \"政社\",\n" +
                "\t\t\t\t\t\"channel_code\": \"zhengshe\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"\u200B持续阴雨影响蜜蜂养殖 长兴为蜂农请来技术“外援”\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551757530918,\n" +
                "\t\t\t\t\t\"published_at\": 1551742265000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1146458,\n" +
                "\t\t\t\t\t\"mlf_id\": 9560140,\n" +
                "\t\t\t\t\t\"list_title\": \"兰溪部门乡镇一把手公开“晒”承诺、“赛”承诺\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190228/1551344125165_5c77a1fd159bb8144ed27c1f.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 107,\n" +
                "\t\t\t\t\t\"like_count_general\": \"107赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1146458\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"584f5311e4aaf2b9de82187e\",\n" +
                "\t\t\t\t\t\"channel_name\": \"政社\",\n" +
                "\t\t\t\t\t\"channel_code\": \"zhengshe\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"兰溪部门乡镇一把手公开“晒”承诺、“赛”承诺\",\n" +
                "\t\t\t\t\t\"summary\": \"确保全年完成内资65亿元，外资4500万美元，其中50亿元以上项目1个，20亿元以上项目2个，10亿元以上项目5个。做好产业扶持，将全乡的木芙蓉种植面积扩大到1500亩以上……2月28日，兰溪市商务局和兰溪市水亭畲族乡一把手在当地媒体上晒出了工作承诺。近日，兰溪发改、经信、交通等16个部门、16个乡镇的一把手陆续在媒体上晒出抓重点工作、重点项目的承诺，倒逼工作落实，接受群众监督，受到社会各界的广泛关注，当地各微信公众号有关一把手晒承诺内容阅读量大多已达10万+。兰溪市委主要负责人说， 晒承诺更要赛承诺，真正倒逼各部门、乡镇等不起、坐不住、跑起来，通过抓招商抓项目抓服务，助推经济社会高质量发展。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551353365330,\n" +
                "\t\t\t\t\t\"published_at\": 1551344125000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9584\",\n" +
                "\t\t\t\t\"group_name\": \"一线调研\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1119774,\n" +
                "\t\t\t\t\t\"mlf_id\": 9265906,\n" +
                "\t\t\t\t\t\"list_title\": \"深化大调研 推进“三服务”丨以高质量服务推动高质量发展\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190117/1547681295175_5c3fbe0f159bb82a0fcbcce0.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 593,\n" +
                "\t\t\t\t\t\"like_count_general\": \"593赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1119774\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"584e6ac7e200b2098f871d3a\",\n" +
                "\t\t\t\t\t\"channel_name\": \"观点\",\n" +
                "\t\t\t\t\t\"channel_code\": \"guandian\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"深化大调研 推进“三服务”丨以高质量服务推动高质量发展\",\n" +
                "\t\t\t\t\t\"summary\": \"开展服务企业、服务群众、服务基层活动，是我省深入践行习近平总书记对浙江工作新期望，推进八八战略再深化、改革开放再出发的具体行动，是今年推动大学习大调研大抓落实活动往深里做的具体抓手。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1547681295157,\n" +
                "\t\t\t\t\t\"published_at\": 1547681295000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": false\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"related_news\": [],\n" +
                "\t\t\t\"related_subjects\": [],\n" +
                "\t\t\t\"subject_comment_list\": [],\n" +
                "\t\t\t\"status\": 4\n" +
                "\t\t},\n" +
                "\t\t\"adv_places\": {}\n" +
                "\t}";
        mDraftDetailBean = new Gson().fromJson(mock, DraftDetailBean.class);
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
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mDraftDetailBean.getArticle().getChannel_id() + "")
                        .setClassifyName(mDraftDetailBean.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mDraftDetailBean.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfobjectID(mDraftDetailBean.getArticle().getId() + "");

                UmengShareUtils.getInstance().startShare(UmengShareBean.getInstance()
                        .setSingle(false)
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
                    T.showShort(getActivity(), mDraftDetailBean.getArticle().isFollowed() ? "收藏成功" : "取消收藏成功");
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
                        T.showShort(getActivity(), "已收藏成功");
                    }
                } else {
                    T.showShort(getActivity(), errMsg);
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
