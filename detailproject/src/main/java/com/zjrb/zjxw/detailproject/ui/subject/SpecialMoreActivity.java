package com.zjrb.zjxw.detailproject.ui.subject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.common.glide.GlideApp;
import com.zjrb.core.ui.widget.CompatViewPager;
import com.zjrb.core.utils.T;
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
        // TODO: 2019/3/14 模拟 正式上线前去掉
        String mock = "{\n" +
                "\t\t\"article\": {\n" +
                "\t\t\t\"id\": 1146710,\n" +
                "\t\t\t\"mlf_id\": 9563078,\n" +
                "\t\t\t\"list_title\": \"代表委员热议推动全方位对外开放 坚定不移向世界敞开大门\",\n" +
                "\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552106777025_5c834519159bb8144ed3147d.jpeg\"],\n" +
                "\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\"doc_type\": 5,\n" +
                "\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\"read_count\": 349397,\n" +
                "\t\t\t\"read_count_general\": \"34.9万+阅读\",\n" +
                "\t\t\t\"url\": \"https://zj.zjol.com.cn/subject.html?id=1146710\",\n" +
                "\t\t\t\"fixed_number\": 5,\n" +
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
                "\t\t\t\"doc_title\": \"聚焦2019全国两会\",\n" +
                "\t\t\t\"article_pic\": \"https://stc-new.8531.cn/assets/20190309/1552106777025_5c834519159bb8144ed3147d.jpeg\",\n" +
                "\t\t\t\"source\": \"\",\n" +
                "\t\t\t\"author\": \"\",\n" +
                "\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\"content\": \"\",\n" +
                "\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\"sort_number\": 1552085315658,\n" +
                "\t\t\t\"published_at\": 1551396141000,\n" +
                "\t\t\t\"subject_display_id\": 0,\n" +
                "\t\t\t\"subject_display_type\": 1,\n" +
                "\t\t\t\"subject_groups\": [{\n" +
                "\t\t\t\t\"group_id\": \"9989\",\n" +
                "\t\t\t\t\"group_name\": \"代表委员\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1152994,\n" +
                "\t\t\t\t\t\"mlf_id\": 9624496,\n" +
                "\t\t\t\t\t\"list_title\": \"代表委员热议推动全方位对外开放 向世界敞开大门\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552088335167_5c82fd0f159bb8144ed310b7.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152994\",\n" +
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
                "\t\t\t\t\t\"doc_title\": \"代表委员热议推动全方位对外开放 坚定不移向世界敞开大门\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552088335150,\n" +
                "\t\t\t\t\t\"published_at\": 1552088335000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1153148,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625565,\n" +
                "\t\t\t\t\t\"list_title\": \"“部长通道”第三次开启：解读国策 直通民情\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552104335542_5c833b8f159bb8144ed31424.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153148\",\n" +
                "\t\t\t\t\t\"fixed_number\": 8,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"“部长通道”第三次开启：解读国策 直通民情\",\n" +
                "\t\t\t\t\t\"summary\": \"央视网消息3月8日，2019年全国两会第三场部长通道在人民大会堂举行。中国气象局局长刘雅鸣、文化和旅游部部长雒树刚、国家卫生健康委员会主任马晓伟、国务院发展研究中心党组书记马建堂、国家体育总局局长苟仲文、科学技术部部长王志刚、国务院港澳事务办公室主任张晓明等七位部长在这里直面热点，回应关切。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552104335505,\n" +
                "\t\t\t\t\t\"published_at\": 1552104336000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1153146,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625545,\n" +
                "\t\t\t\t\t\"list_title\": \"聚焦·代表通道丨话履职感悟 谋务实之策\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552104330398_5c833b8a159bb8144ed31419.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 60,\n" +
                "\t\t\t\t\t\"like_count_general\": \"60赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153146\",\n" +
                "\t\t\t\t\t\"fixed_number\": 9,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"聚焦·代表通道丨话履职感悟 谋务实之策\",\n" +
                "\t\t\t\t\t\"summary\": \"原标题：话履职感悟谋务实之策。云南省怒江傈僳族自治州贡山独龙族怒族自治县人大常委会主任、党组书记马正山代表：去年年底，独龙族实现了整族脱贫，贫困发生率下降到了2.63%，独龙江乡1086户群众全部住进了新房，所有自然村都通了硬化路，所有住户通了4G网络，广播信号覆盖全乡。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552104330387,\n" +
                "\t\t\t\t\t\"published_at\": 1552104330000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9990\",\n" +
                "\t\t\t\t\"group_name\": \"要闻发布\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1153024,\n" +
                "\t\t\t\t\t\"mlf_id\": 9624666,\n" +
                "\t\t\t\t\t\"list_title\": \"外商投资法草案提请全国人代会审议\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552099085174_5c83270d159bb8144ed31356.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153024\",\n" +
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
                "\t\t\t\t\t\"doc_title\": \"外商投资法草案提请全国人代会审议\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552091970151,\n" +
                "\t\t\t\t\t\"published_at\": 1552091970000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1153141,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625552,\n" +
                "\t\t\t\t\t\"list_title\": \"从全国人大常委会工作报告看新时代人大工作的责任担当\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552103625170_5c8338c9159bb8144ed31402.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153141\",\n" +
                "\t\t\t\t\t\"fixed_number\": 12,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"从全国人大常委会工作报告看新时代人大工作的责任担当\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552103625153,\n" +
                "\t\t\t\t\t\"published_at\": 1552103625000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1152782,\n" +
                "\t\t\t\t\t\"mlf_id\": 9622903,\n" +
                "\t\t\t\t\t\"list_title\": \"两会日程预告丨人代会9日审议全国人大常委会工作报告\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552090825172_5c8306c9159bb8144ed31136.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 151,\n" +
                "\t\t\t\t\t\"like_count_general\": \"151赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152782\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"两会日程预告丨人代会9日审议全国人大常委会工作报告\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552051655709,\n" +
                "\t\t\t\t\t\"published_at\": 1552041601000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9980\",\n" +
                "\t\t\t\t\"group_name\": \"深度报道\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1153145,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625537,\n" +
                "\t\t\t\t\t\"list_title\": \"我从基层来丨聚焦民生关切 回应人民期盼\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552104315244_5c833b7b159bb8144ed3140e.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153145\",\n" +
                "\t\t\t\t\t\"fixed_number\": 11,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"我从基层来丨聚焦民生关切 回应人民期盼\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552104315224,\n" +
                "\t\t\t\t\t\"published_at\": 1552104315000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1152132,\n" +
                "\t\t\t\t\t\"mlf_id\": 9617538,\n" +
                "\t\t\t\t\t\"list_title\": \"如何推进长三角更高质量发展 代表委员讲述蝶变故事\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190308/1552010315689_5c81cc4b159bb8144ed2ffb7.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 121,\n" +
                "\t\t\t\t\t\"like_count_general\": \"121赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152132\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"53845f54e4b08e9fb1cdfdbc\",\n" +
                "\t\t\t\t\t\"channel_name\": \"经济\",\n" +
                "\t\t\t\t\t\"channel_code\": \"jingji\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"如何推进长三角更高质量一体化发展 代表委员讲述蝶变故事\",\n" +
                "\t\t\t\t\t\"summary\": \"政府工作报告提出，将长三角区域一体化发展上升为国家战略，编制实施发展规划纲要。连日来，长三角一体化成为全国两会上的高热度词。代表委员们表示，长三角一体化的深度合作，不仅要有宏大叙事，还要找出共振点，这正是快速提升区域合作质量的突破口。在代表委员们的讲述中，长三角一体化的共振点是一条廊、一张网、一座岛、一条路……它们精彩蝶变的故事，正是长三角更高质量一体化的缩影。这是2月28日，在江苏省苏州市相城区召开的G 60科创走廊智能驾驶、新能源、人工智能产业联盟筹备会议上的场景。去年6月，来自苏浙皖的8个城市齐聚上海松江区，签署了共建共享G 60科创走廊战略合作协议，3.0版的G 60科创走廊令9城市间合作更加紧密。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552002780433,\n" +
                "\t\t\t\t\t\"published_at\": 1552002780000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1151097,\n" +
                "\t\t\t\t\t\"mlf_id\": 9608494,\n" +
                "\t\t\t\t\t\"list_title\": \"关于人社工作 今年政府工作报告都说了啥？\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190306/1551887155191_5c7feb33159bb8144ed2e860.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 128,\n" +
                "\t\t\t\t\t\"like_count_general\": \"128赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1151097\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"关于人社工作 今年政府工作报告都说了啥？\",\n" +
                "\t\t\t\t\t\"summary\": \"其中提到哪些人社事?针对外部环境变化给就业带来的影响,及时出台稳就业举措。今年城镇新增就业要在实现预期目标的基础上,力争达到近几年的实际规模,既保障城镇劳动力就业,也为农业富余劳动力转移就业留出空间。落实退役军人待遇保障,完善退役士兵基本养老、基本医疗保险接续政策。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551885902602,\n" +
                "\t\t\t\t\t\"published_at\": 1551887155000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9977\",\n" +
                "\t\t\t\t\"group_name\": \"两会时刻\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1152896,\n" +
                "\t\t\t\t\t\"mlf_id\": 9623785,\n" +
                "\t\t\t\t\t\"list_title\": \"多位全国政协委员建言 打好“组合拳”提升科技支撑能力\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190308/1552051647403_5c826dbf159bb8144ed30ec9.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152896\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"多位全国政协委员建言 打好“组合拳”提升科技支撑能力\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551948225149,\n" +
                "\t\t\t\t\t\"published_at\": 1552051647000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1152775,\n" +
                "\t\t\t\t\t\"mlf_id\": 9622838,\n" +
                "\t\t\t\t\t\"list_title\": \"共享成长“天空” 一块屏幕牵动三位代表委员的心\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190308/1552040996373_5c824424159bb8144ed30c09.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 89,\n" +
                "\t\t\t\t\t\"like_count_general\": \"89赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152775\",\n" +
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
                "\t\t\t\t\t\"doc_title\": \"共享成长“天空” 一块屏幕牵动三位代表委员的心\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552040996355,\n" +
                "\t\t\t\t\t\"published_at\": 1552040996000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1151183,\n" +
                "\t\t\t\t\t\"mlf_id\": 9609273,\n" +
                "\t\t\t\t\t\"list_title\": \"“最多跑一次”再次写入政府工作报告 代表委员建言献策\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190307/1551914985184_5c8057e9159bb8144ed2ea09.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 97,\n" +
                "\t\t\t\t\t\"like_count_general\": \"97赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1151183\",\n" +
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
                "\t\t\t\t\t\"doc_title\": \"“最多跑一次”再次写入政府工作报告 代表委员建言献策\",\n" +
                "\t\t\t\t\t\"summary\": \"最多跑一次又一次写入政府工作报告！……代表委员们围绕政府工作报告，结合浙江改革实际，纷纷出谋划策，建议要聚焦深化市场化改革，以领跑者姿态，将最多跑一次改革进行到底。针对如何深化最多跑一次改革撰写了调研报告，诊断出改革中尚存的数个问题。黄廉熙委员建议，要把最多跑一次改革与高水平推进平安浙江、法治浙江建设有机统一起来，充分发挥司法职能作用，加快推进公共法律服务体系建设，营造良好的法治化营商环境， 我们律师要做好法律医生，有针对性地对企业开展法律体检。要用专业的眼光和思路，为企业提供有针对性的服务，共同为打造浙江更好的法治营商环境努力。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551920660147,\n" +
                "\t\t\t\t\t\"published_at\": 1551914985000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9978\",\n" +
                "\t\t\t\t\"group_name\": \"会场内外\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1153098,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625227,\n" +
                "\t\t\t\t\t\"list_title\": \"两会走笔︱中国制造要有使命感\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552099140416_5c832744159bb8144ed3135f.png\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 147,\n" +
                "\t\t\t\t\t\"like_count_general\": \"147赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153098\",\n" +
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
                "\t\t\t\t\t\"doc_title\": \"两会走笔︱中国制造要有使命感\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552098135337,\n" +
                "\t\t\t\t\t\"published_at\": 1552098135000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1153143,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625560,\n" +
                "\t\t\t\t\t\"list_title\": \"长三角圆桌︱加快构建“通勤化一体化立体化”交通格局\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552105410228_5c833fc2159bb8144ed3146d.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 57,\n" +
                "\t\t\t\t\t\"like_count_general\": \"57赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153143\",\n" +
                "\t\t\t\t\t\"fixed_number\": 1,\n" +
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
                "\t\t\t\t\t\"doc_title\": \"长三角圆桌︱加快构建“通勤化一体化立体化”交通格局\",\n" +
                "\t\t\t\t\t\"summary\": \"政府工作报告提出，将长三角区域一体化发展上升为国家战略，编制实施发展规划纲要。基础设施互联互通，是区域一体化发展的应有之义，位于长三角几何中心的嘉兴是如何进行布局和思考的？记者专访了全国人大代表、嘉兴市委书记张兵。张兵介绍，在对接杭州方面，嘉兴积极参与杭州都市圈建设，重点推进杭海城际铁路建设，争取杭绍台高铁二期东延到嘉兴南站。同时，嘉兴正在谋划建设嘉兴至湖州城际铁路，这是嘉兴衔接皖东南、苏南地区的快速通道，建成后可进一步加强与南京、合肥等长三角重要城市的联系。他还透露，嘉兴正在抓紧推进的杭平申、湖嘉申二期、乍嘉苏、京杭运河等一批航道项目，将构筑起嘉兴通往周边重要城市的水上通道。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552104085237,\n" +
                "\t\t\t\t\t\"published_at\": 1552104085000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1152005,\n" +
                "\t\t\t\t\t\"mlf_id\": 9616561,\n" +
                "\t\t\t\t\t\"list_title\": \"她们眼中的“她” 全国政协妇联界别委员谈女性发展\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190307/1551967825996_5c812651159bb8144ed2fa37.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152005\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"她们眼中的“她” 全国政协妇联界别委员谈女性发展\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551881471398,\n" +
                "\t\t\t\t\t\"published_at\": 1551967825000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"9988\",\n" +
                "\t\t\t\t\"group_name\": \"融媒产品\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1151973,\n" +
                "\t\t\t\t\t\"mlf_id\": 9616329,\n" +
                "\t\t\t\t\t\"list_title\": \"画中话丨这份政府工作报告将惠及全民\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190307/1551963431697_5c811527159bb8144ed2f958.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1151973\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"画中话丨这份政府工作报告将惠及全民\",\n" +
                "\t\t\t\t\t\"summary\": \"减税降费：将制造业等行业现行16%的增值税税率降至13%。全年减轻企业税收和社保缴费负担近2万亿元。铁路和公路水运投资，能够改善基础设施，为民生改善创造条件，为国民经济长远发展奠定交通基础。长三角地铁全国率先实现互联互通，也将释放更多民生红利。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551881470148,\n" +
                "\t\t\t\t\t\"published_at\": 1551963432000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1151585,\n" +
                "\t\t\t\t\t\"mlf_id\": 9611560,\n" +
                "\t\t\t\t\t\"list_title\": \"微访谈丨王亚民委员：故宫文创要立足中华文化根基 做群众最需求的产品\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190307/1551934276553_5c80a344159bb8144ed2f101.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 9,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 116,\n" +
                "\t\t\t\t\t\"like_count_general\": \"116赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/video.html?id=1151585\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"57d690e7e200b20fbb4af09f\",\n" +
                "\t\t\t\t\t\"channel_name\": \"视频\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shipin\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"微访谈丨王亚民委员：故宫文创要立足中华文化根基 做群众最需要的产品\",\n" +
                "\t\t\t\t\t\"summary\": \"尽可能做到无一物没有来历，体现故宫文创的元素性、创意性和故事性。\",\n" +
                "\t\t\t\t\t\"web_link\": \"https://v-cdn.zjol.com.cn/236350.mp4?isVertical=0&ref_aid=1151585\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551932811439,\n" +
                "\t\t\t\t\t\"published_at\": 1551934276000,\n" +
                "\t\t\t\t\t\"video_type\": 1,\n" +
                "\t\t\t\t\t\"video_url\": \"https://v-cdn.zjol.com.cn/236350.mp4?isVertical=0\",\n" +
                "\t\t\t\t\t\"video_duration\": 94,\n" +
                "\t\t\t\t\t\"video_size\": 36360485,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1151310,\n" +
                "\t\t\t\t\t\"mlf_id\": 9610145,\n" +
                "\t\t\t\t\t\"list_title\": \"现场速递丨如何打造互联网时代的新文创新消费？全国政协委员这样说\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190307/1551922090813_5c8073aa159bb8144ed2ec18.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 9,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/video.html?id=1151310\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"57d690e7e200b20fbb4af09f\",\n" +
                "\t\t\t\t\t\"channel_name\": \"视频\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shipin\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"现场速递丨如何打造互联网时代的新文创新消费？全国政协委员这样说\",\n" +
                "\t\t\t\t\t\"summary\": \"全国政协委员、国家博物馆文物艺术品鉴定中心副主任耿东升，全国政协委员、故宫博物院总策展人王亚民，全国政协委员、国际博物馆协会副主席安来顺，全国政协委员、中国文化产业协会会长张斌，与阿里巴巴文创行业负责人王飞等嘉宾。\",\n" +
                "\t\t\t\t\t\"web_link\": \"https://v-cdn.zjol.com.cn/236303.mp4?isVertical=0&ref_aid=1151310\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551879505414,\n" +
                "\t\t\t\t\t\"published_at\": 1551922091000,\n" +
                "\t\t\t\t\t\"video_type\": 1,\n" +
                "\t\t\t\t\t\"video_url\": \"https://v-cdn.zjol.com.cn/236303.mp4?isVertical=0\",\n" +
                "\t\t\t\t\t\"video_duration\": 139,\n" +
                "\t\t\t\t\t\"video_size\": 49532212,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"10002\",\n" +
                "\t\t\t\t\"group_name\": \"部长发声\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1149705,\n" +
                "\t\t\t\t\t\"mlf_id\": 9594643,\n" +
                "\t\t\t\t\t\"list_title\": \"郭树清：在金融开放方面 中美可以达成一致\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190305/1551765810229_5c7e1132159bb8144ed2c761.png\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 36,\n" +
                "\t\t\t\t\t\"like_count_general\": \"36赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1149705\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"郭树清：在金融开放方面 中美可以达成一致\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551972325178,\n" +
                "\t\t\t\t\t\"published_at\": 1551765810000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1149662,\n" +
                "\t\t\t\t\t\"mlf_id\": 9594212,\n" +
                "\t\t\t\t\t\"list_title\": \"李小鹏：今年在京津冀等地取消高速省界收费站\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190305/1551762020234_5c7e0264159bb8144ed2c655.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 93,\n" +
                "\t\t\t\t\t\"like_count_general\": \"93赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1149662\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"李小鹏：今年在京津冀等地取消高速省界收费站\",\n" +
                "\t\t\t\t\t\"summary\": \"3月5日上午，交通运输部部长李小鹏在部长通道上就高速公路取消省界收费站问题回答记者提问。2019年将抓重点区域和省份，取消京津冀，长三角地区以及东北、西南地区重点省份的高速公路省界收费站， 2020年基本实现取消省界收费站的目标。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551948221163,\n" +
                "\t\t\t\t\t\"published_at\": 1551762020000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1149663,\n" +
                "\t\t\t\t\t\"mlf_id\": 9594210,\n" +
                "\t\t\t\t\t\"list_title\": \"商务部长钟山谈中美贸易磋商：达成协议需双方相向而行\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190305/1551762025224_5c7e0269159bb8144ed2c657.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 112,\n" +
                "\t\t\t\t\t\"like_count_general\": \"112赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1149663\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"商务部长钟山谈中美贸易磋商：达成协议需双方相向而行\",\n" +
                "\t\t\t\t\t\"summary\": \"3月5日上午，十三届全国人大二次会议开幕会结束后，第二场部长通道在人民大会堂举行。商务部部长钟山在部长通道上表示，中美磋商成果来之不易，目前双方工作团队正在继续磋商，更加需要相向而行、共同努力。假如能够达成协议，将有利于两国经济的发展，有利于全球经济的发展。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551967826334,\n" +
                "\t\t\t\t\t\"published_at\": 1551762025000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"group_id\": \"10008\",\n" +
                "\t\t\t\t\"group_name\": \"重要评论\",\n" +
                "\t\t\t\t\"group_articles\": [{\n" +
                "\t\t\t\t\t\"id\": 1153138,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625531,\n" +
                "\t\t\t\t\t\"list_title\": \"专家解读：新时代外商投资法律制度的“四梁八柱”\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552103450189_5c83381a159bb8144ed313fd.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"like_count\": 52,\n" +
                "\t\t\t\t\t\"like_count_general\": \"52赞\",\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153138\",\n" +
                "\t\t\t\t\t\"fixed_number\": 14,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"专家解读：新时代外商投资法律制度的“四梁八柱”\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552103450176,\n" +
                "\t\t\t\t\t\"published_at\": 1552103450000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1153136,\n" +
                "\t\t\t\t\t\"mlf_id\": 9625542,\n" +
                "\t\t\t\t\t\"list_title\": \"新华社评论员：走好新时代乡村振兴路\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190309/1552103330165_5c8337a2159bb8144ed313f9.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1153136\",\n" +
                "\t\t\t\t\t\"fixed_number\": 16,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"新华社评论员：走好新时代乡村振兴路\",\n" +
                "\t\t\t\t\t\"summary\": \"新华社北京3月8日电题：走好新时代乡村振兴路新时代的中国农村充满希望，广阔天地大有可为。3月8日，习近平总书记在参加十三届全国人大二次会议河南代表团审议时，与各位代表深入交流，就推进乡村全面振兴作出重要论述，为进一步做好“三农”工作鼓舞干劲。\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1552103330151,\n" +
                "\t\t\t\t\t\"published_at\": 1552103330000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"id\": 1152905,\n" +
                "\t\t\t\t\t\"mlf_id\": 9623753,\n" +
                "\t\t\t\t\t\"list_title\": \"沿着高质量发展的轨道稳步前行\",\n" +
                "\t\t\t\t\t\"list_style\": 2,\n" +
                "\t\t\t\t\t\"list_pics\": [\"https://stc-new.8531.cn/assets/20190308/1552051652782_5c826dc4159bb8144ed30ed8.jpeg\"],\n" +
                "\t\t\t\t\t\"list_tag\": \"\",\n" +
                "\t\t\t\t\t\"list_type\": 2,\n" +
                "\t\t\t\t\t\"doc_type\": 2,\n" +
                "\t\t\t\t\t\"doc_category\": 1,\n" +
                "\t\t\t\t\t\"url\": \"https://zj.zjol.com.cn/news.html?id=1152905\",\n" +
                "\t\t\t\t\t\"fixed_number\": 0,\n" +
                "\t\t\t\t\t\"original_id\": 0,\n" +
                "\t\t\t\t\t\"src_metadata_id\": 0,\n" +
                "\t\t\t\t\t\"like_enabled\": true,\n" +
                "\t\t\t\t\t\"comment_level\": 1,\n" +
                "\t\t\t\t\t\"channel_id\": \"52e5f902cf81d754a434fb51\",\n" +
                "\t\t\t\t\t\"channel_name\": \"时局\",\n" +
                "\t\t\t\t\t\"channel_code\": \"shiju\",\n" +
                "\t\t\t\t\t\"source_channel_id\": \"\",\n" +
                "\t\t\t\t\t\"column_id\": 0,\n" +
                "\t\t\t\t\t\"column_subscribed\": false,\n" +
                "\t\t\t\t\t\"doc_title\": \"沿着高质量发展的轨道稳步前行\",\n" +
                "\t\t\t\t\t\"summary\": \"\",\n" +
                "\t\t\t\t\t\"web_link\": \"\",\n" +
                "\t\t\t\t\t\"sort_number\": 1551963431679,\n" +
                "\t\t\t\t\t\"published_at\": 1552051653000,\n" +
                "\t\t\t\t\t\"status\": 4\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"group_has_more\": true\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"related_news\": [],\n" +
                "\t\t\t\"related_subjects\": [],\n" +
                "\t\t\t\"subject_comment_list\": [],\n" +
                "\t\t\t\"status\": 4\n" +
                "\t\t},\n" +
                "\t\t\"adv_places\": {}\n" +
                "\t}";
//        mDraftDetailBean = new Gson().fromJson(mock, DraftDetailBean.class);
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
