package com.zjrb.zjxw.detailproject.redBoat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.google.gson.Gson;
import com.trs.tasdk.entity.ObjectType;
import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.base.toolbar.holder.DefaultTopBarHolder1;
import com.zjrb.core.common.global.IKey;
import com.zjrb.core.ui.holder.EmptyPageHolder;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.click.ClickTracker;
import com.zjrb.daily.db.bean.ReadNewsBean;
import com.zjrb.daily.db.dao.ReadNewsDaoHelper;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.DraftDetailBean;
import com.zjrb.zjxw.detailproject.global.ErrorCode;
import com.zjrb.zjxw.detailproject.nomaldetail.EmptyStateFragment;
import com.zjrb.zjxw.detailproject.nomaldetail.NewsDetailSpaceDivider;
import com.zjrb.zjxw.detailproject.redBoat.adapter.RedBoatAdapter;
import com.zjrb.zjxw.detailproject.task.DraftDetailTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.daily.news.analytics.Analytics;

import static com.zjrb.core.utils.UIUtils.getContext;

/**
 * 类描述：红船号详情页
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/12 2007
 */

public class RedBoatActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.rv_content)
    FitWindowsRecyclerView mRvContent;
    @BindView(R2.id.v_container)
    FrameLayout mView;
    @BindView(R2.id.ry_container)
    RelativeLayout mContainer;

    //稿件ID
    public String mArticleId;
    private String mFromChannel;

    private DefaultTopBarHolder1 topHolder;
    private Analytics.AnalyticsBuilder builder;
    private DraftDetailBean mNewsDetail;
    private RedBoatAdapter mAdapter;
    private Analytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucenceStatusBarBg();
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
        getIntentData(getIntent());
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        topHolder = TopBarFactory.createDefault1(view, this);
        topHolder.setViewVisible(topHolder.getShareView(),View.INVISIBLE);
        return topHolder.getView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        loadData();
    }

    /**
     * @param intent 获取传递数据
     */
    private void getIntentData(Intent intent) {
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                if (data.getQueryParameter(IKey.ID) != null) {
                    mArticleId = data.getQueryParameter(IKey.ID);
                }
                if (data.getQueryParameter(IKey.FROM_CHANNEL) != null) {
                    mFromChannel = data.getQueryParameter(IKey.FROM_CHANNEL);
                }
            }


        }

//        loadData();
        simulateData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.onWebViewResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.onWebViewPause();
        }
    }

    /**
     * 请求详情页数据
     */
    private void loadData() {
        if (mArticleId == null || mArticleId.isEmpty()) return;
        new DraftDetailTask(new APIExpandCallBack<DraftDetailBean>() {
            @Override
            public void onSuccess(DraftDetailBean draftDetailBean) {
                if (draftDetailBean == null || draftDetailBean.getArticle() == null) return;
                Map map = new HashMap();
                map.put("relatedColumn", draftDetailBean.getArticle().getColumn_id());
                map.put("subject", "");
                builder = new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                        .setEvenName("页面停留时长/阅读深度")
                        .setObjectID(draftDetailBean.getArticle().getMlf_id() + "")
                        .setObjectName(draftDetailBean.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(draftDetailBean.getArticle().getChannel_id())
                        .setClassifyName(draftDetailBean.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setSelfObjectID(draftDetailBean.getArticle().getId() + "");

                if (mView.getVisibility() == View.VISIBLE) {
                    mView.setVisibility(View.GONE);
                }
                mNewsDetail = draftDetailBean;
                fillData(mNewsDetail);
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //撤稿
                if (errCode == ErrorCode.DRAFFT_IS_NOT_EXISE) {
                    topHolder.getShareView().setVisibility(View.GONE);
                    showEmptyNewsDetail();
                } else {
                    T.showShortNow(RedBoatActivity.this, errMsg);
                }
            }
        }).setTag(this).bindLoadViewHolder(replaceLoad(mContainer)).exe(mArticleId, mFromChannel);
    }

    /**
     * 模拟数据
     */
    private void simulateData() {
        DraftDetailBean draftDetailBean = new Gson().fromJson(response, DraftDetailBean.class);
        if (draftDetailBean == null) return;
        Map map = new HashMap();
        map.put("relatedColumn", draftDetailBean.getArticle().getColumn_id());
        map.put("subject", "");
        builder = new Analytics.AnalyticsBuilder(getContext(), "A0010", "800021")
                .setEvenName("页面停留时长/阅读深度")
                .setObjectID(draftDetailBean.getArticle().getMlf_id() + "")
                .setObjectName(draftDetailBean.getArticle().getDoc_title())
                .setObjectType(ObjectType.NewsType)
                .setClassifyID(draftDetailBean.getArticle().getChannel_id())
                .setClassifyName(draftDetailBean.getArticle().getChannel_name())
                .setPageType("新闻详情页")
                .setSelfObjectID(draftDetailBean.getArticle().getId() + "");

        if (mView.getVisibility() == View.VISIBLE) {
            mView.setVisibility(View.GONE);
        }
        mNewsDetail = draftDetailBean;
        fillData(mNewsDetail);
    }

    /**
     * @param data 填充详情页数据
     */
    private void fillData(DraftDetailBean data) {
        DraftDetailBean.ArticleBean article = data.getArticle();
        if (article != null) {
            ReadNewsDaoHelper.get().asyncRecord(
                    ReadNewsBean.newBuilder().id(article.getId())
                            .mlfId(article.getMlf_id())
                            .tag(article.getList_tag())
                            .title(article.getList_title())
                            .url(article.getUrl()));
        }

        mNewsDetail = data;
        List datas = new ArrayList<>();
        //头
        datas.add(data);
        //web
        datas.add(data);
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mRvContent.addItemDecoration(new NewsDetailSpaceDivider(0.5f, R.attr.dc_dddddd));
        mAdapter = new RedBoatAdapter(datas,
                !TextUtils.isEmpty(mNewsDetail.getArticle().getVideo_url()) ? true : false);
        mAdapter.setEmptyView(new EmptyPageHolder(mRvContent,
                EmptyPageHolder.ArgsBuilder.newBuilder().content("暂无数据")).itemView);
        mRvContent.setAdapter(mAdapter);
    }

    /**
     * 显示撤稿页面
     */
    private void showEmptyNewsDetail() {
        mView.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.v_container, EmptyStateFragment.newInstance()).commit();
    }

    @Override
    @OnClick({R2.id.iv_top_bar_back})
    public void onClick(View v) {
        if (ClickTracker.isDoubleClick()) return;
        if (v.getId() == R.id.iv_top_bar_back) {
            if (mNewsDetail != null && mNewsDetail.getArticle() != null) {
                new Analytics.AnalyticsBuilder(getContext(), "800001", "800001")
                        .setEvenName("点击返回")
                        .setObjectID(mNewsDetail.getArticle().getMlf_id() + "")
                        .setObjectName(mNewsDetail.getArticle().getDoc_title())
                        .setObjectType(ObjectType.NewsType)
                        .setClassifyID(mNewsDetail.getArticle().getChannel_id())
                        .setClassifyName(mNewsDetail.getArticle().getChannel_name())
                        .setPageType("新闻详情页")
                        .setOtherInfo(Analytics.newOtherInfo()
                                .put("relatedColumn", mNewsDetail.getArticle().getColumn_id() + "")
                                .put("subject", "")
                                .toString())
                        .setSelfObjectID(mNewsDetail.getArticle().getId() + "")
                        .build()
                        .send();
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (builder != null) {
            mAnalytics = builder.build();
            if (mAnalytics != null) {
                mAnalytics.sendWithDuration();
            }
        }
    }

    private String response = "{\"article\":{\"id\":892350.0,\"mlf_id\":6789197.0,\"list_title\":\"浙江代表团审议监察法草案 车俊袁家军参加\",\"list_style\":2.0,\"list_pics\":[\"https://stc-new.8531.cn/assets/20180313/1520933744431_5aa79b70159bb85d9608a913.jpeg\"],\"list_tag\":\"\",\"list_type\":2.0,\"doc_type\":2.0,\"read_count\":162685.0,\"read_count_general\":\"16.2万+阅读\",\"url\":\"https://zj.zjol.com.cn/news.html?id\\u003d892350\",\"fixed_number\":0.0,\"original_id\":0.0,\"src_metadata_id\":0.0,\"followed\":false,\"liked\":false,\"like_enabled\":true,\"comment_level\":0.0,\"channel_id\":\"52e5f902cf81d754a434fb50\",\"channel_name\":\"头条\",\"channel_code\":\"toutiao\",\"source_channel_id\":\"\",\"column_id\":0.0,\"column_subscribed\":false,\"doc_title\":\"浙江代表团审议监察法草案 车俊袁家军参加\",\"article_pic\":\"\",\"source\":\"浙江新闻客户端\",\"author\":\"记者 王国锋 丁谨之\",\"summary\":\"\",\"content\":\"\\u003cdiv contenteditabled\\u003d\\\"false\\\" style\\u003d\\\"display:inline-block\\\" class\\u003d\\\"picdiv\\\"\\u003e\\n \\u003cfigure class\\u003d\\\"wg wgImage\\\"\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-source\\\" contenteditable\\u003d\\\"false\\\"\\u003e\\u003cimg src\\u003d\\\"https://stc-new.8531.cn/assets/20180313/1520933742921_5aa79b6e159bb85d9608a910.jpeg\\\" class\\u003d\\\"limitWidth\\\" oldsrc\\u003d\\\"W020180313627523818590.jpg\\\"\\u003e\\u003c/p\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-content\\\"\\u003e13日下午，浙江代表团举行第八次全体会议，车俊代表发言。浙江新闻客户端记者 梁臻 摄\\u003c/p\\u003e\\n \\u003c/figure\\u003e\\n\\u003c/div\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e13日下午，浙江代表团举行第八次全体会议，审议监察法草案。\\u003c/p\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e车俊、袁家军、刘建超、梁黎明参加审议，姒健敏主持。大会秘书处法案组有关同志到会听取意见。\\u003c/p\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e车俊代表说，制定监察法是以习近平同志为核心的党中央高瞻远瞩、审时度势作出的重大决策，是中国特色社会主义监察制度的重大创新，是推进国家治理体系和治理能力现代化的关键举措。它充分彰显了党中央开创全面从严治党新局面的坚定意志和决心，吹响了夺取反腐败斗争压倒性胜利的新号角；充分体现了全面深化改革、全面依法治国和全面从严治党的有机统一，必将有力推动国家监察体制改革向纵深发展。特别是监察法进一步明确了党对反腐败斗争的集中统一领导，有利于构建集中统一、权威高效的国家监察体系；进一步明确了6类监察对象，有利于加强对权力的监督制约；进一步明确了监察机关的监察职责、监察范围、监察权限、监察程序和对监察机关监察人员的监督，有利于提升反腐败工作的法治化水平。监察法通过后，浙江将继续发挥在国家监察体制改革中先行先试的作用，把监察法所体现的法治精神和制度安排，落实到全面从严治党实践中去，特别要落实到清廉浙江建设的谋划部署中去，加快形成机构整合、人员融合和工作流程磨合的整体合力，积极推进监督体制改革和制度建设，更好地把制度优势转化为治理效能。\\u003c/p\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e袁家军代表说，监察法草案通篇贯穿了习近平新时代中国特色社会主义思想，充分体现了党性与人民性、党内监督与国家监督、充分授权与自我约束、改革试点与顶层设计的有机统一。制定监察法，是新时代构建集中统一、权威高效的中国特色国家监察体系的创制之举，是全面从严治党和依法治权、依法治吏的重大制度供给，具有划时代意义。监察法铸就了全面从严治党新利器，形成了党领导下合力推进反腐败工作新优势，开创了对所有行使公权力的公职人员监察全覆盖新局面，迈上了法治化推进反腐败工作新征程，必将有力推动形成不敢腐不能腐不想腐的监察制度体系，加快推动反腐败斗争取得压倒性胜利。我们将坚持讲政治为先、重法治为要、守纪律为基，全面贯彻落实监察法，加快打造廉洁政府和清廉浙江，营造政府系统风清气正的政治生态。\\u003c/p\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e刘建超代表说，监察法的制定，在国家法治建设史和纪检监察史上具有里程碑意义。2016年底，党中央确定浙江为国家监察体制改革先行试点地区，浙江省委及省纪委坚决扛起这一重大政治责任，全面准确落实党中央决策部署，改革进展顺利并向纵深推进，制度优势正逐步向治理效能转化。我们要按照监察法新精神新要求，以赋予监察委员会宪法地位为契机，继续当好改革探路者，认真组织学习宣传监察法，继续强化党对反腐败工作的集中统一领导，深化监察组织机构建设，规范监察工作，完善纪法和法法衔接机制，着力加强队伍建设，在构建党统一指挥、全面覆盖、权威高效的监督体系上走在前列、创造更多浙江经验。\\u003c/p\\u003e\\n\\u003cp style\\u003d\\\"text-align: left;\\\"\\u003e梁黎明、贾宇、葛益平、郑亚莉、张咏梅、柯建华、张世方、胡成中等代表先后发言。\\u003c/p\\u003e\\n\\u003cdiv contenteditabled\\u003d\\\"false\\\" style\\u003d\\\"display:inline-block\\\" class\\u003d\\\"picdiv\\\"\\u003e\\n \\u003cfigure class\\u003d\\\"wg wgImage\\\"\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-source\\\" contenteditable\\u003d\\\"false\\\"\\u003e\\u003cimg src\\u003d\\\"https://stc-new.8531.cn/assets/20180313/1520933743682_5aa79b6f159bb85d9608a911.jpeg\\\" class\\u003d\\\"limitWidth\\\" oldsrc\\u003d\\\"W020180313627525742872.jpg\\\"\\u003e\\u003c/p\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-content\\\"\\u003e13日下午，浙江代表团举行第八次全体会议，代表先后发言。浙江新闻客户端记者 梁臻 摄\\u003c/p\\u003e\\n \\u003c/figure\\u003e\\n\\u003c/div\\u003e\\n\\u003cdiv contenteditabled\\u003d\\\"false\\\" style\\u003d\\\"display:inline-block\\\" class\\u003d\\\"picdiv\\\"\\u003e\\n \\u003cfigure class\\u003d\\\"wg wgImage\\\"\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-source\\\" contenteditable\\u003d\\\"false\\\"\\u003e\\u003cimg src\\u003d\\\"https://stc-new.8531.cn/assets/20180313/1520933744370_5aa79b70159bb85d9608a912.jpeg\\\" class\\u003d\\\"limitWidth\\\" oldsrc\\u003d\\\"W020180313627526934158.jpg\\\"\\u003e\\u003c/p\\u003e\\n  \\u003cp class\\u003d\\\"wgImage-content\\\"\\u003e13日下午，浙江代表团举行第八次全体会议，会议现场。浙江新闻客户端记者 梁臻 摄\\u003c/p\\u003e\\n \\u003c/figure\\u003e\\n\\u003c/div\\u003e\\n\\u003cp\\u003e\\u003cbr\\u003e\\u003c/p\\u003e\",\"web_link\":\"\",\"sort_number\":1.520983625226E12,\"published_at\":1.520933744E12,\"related_news\":[{\"id\":889962.0,\"mlf_id\":6766566.0,\"title\":\"浙江代表团审议“两高”报告 车俊袁家军参加\",\"pic\":\"https://stc-new.8531.cn/assets/20180310/1520693507441_5aa3f103159bb85d96086f09.jpeg\",\"url\":\"https://zj.zjol.com.cn/news.html?id\\u003d889962\"},{\"id\":890357.0,\"mlf_id\":6770252.0,\"title\":\"浙江代表团举行第六次全体会议\",\"pic\":\"https://stc-new.8531.cn/assets/20180311/1520772186526_5aa5245a159bb85d96087c4c.jpeg\",\"url\":\"https://zj.zjol.com.cn/news.html?id\\u003d890357\"},{\"id\":891418.0,\"mlf_id\":6780131.0,\"title\":\"浙江代表团审议全国人大常委会工作报告 车俊袁家军参加\",\"pic\":\"https://stc-new.8531.cn/assets/20180312/1520846457235_5aa64679159bb85d96089099.jpeg\",\"url\":\"https://zj.zjol.com.cn/news.html?id\\u003d891418\"}],\"related_subjects\":[]}}";
}
