package com.zjrb.zjxw.detailproject.persionaldetail;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.api.callback.LoadingCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.base.adapter.OnItemClickListener;
import com.zjrb.core.common.base.page.LoadMore;
import com.zjrb.core.common.base.toolbar.TopBarFactory;
import com.zjrb.core.common.listener.LoadMoreListener;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.ui.holder.FooterLoadMore;
import com.zjrb.core.ui.holder.HeaderRefresh;
import com.zjrb.core.ui.widget.divider.ListSpaceDivider;
import com.zjrb.core.utils.T;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.bean.OfficalArticlesBean;
import com.zjrb.zjxw.detailproject.bean.OfficalListBean;
import com.zjrb.zjxw.detailproject.global.Key;
import com.zjrb.zjxw.detailproject.persionaldetail.adapter.PersionalListAdapter;
import com.zjrb.zjxw.detailproject.task.OfficalListTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有官员新闻列表
 * Created by wanglinjie.
 * create time:2017/8/21  上午10:24
 */

public class PersionalListActivity extends BaseActivity implements HeaderRefresh.OnRefreshListener, LoadMoreListener<OfficalListBean> {

    @BindView(R2.id.lv_notice)
    RecyclerView mRecycler;
    /**
     * 所有官员列表适配器
     */
    private PersionalListAdapter mAdapter;

    private List<OfficalListBean.OfficerListBean> list;
    /**
     * 刷新头
     */
    private HeaderRefresh refresh;
    /**
     * 加载更多
     */
    private FooterLoadMore more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_topic_list);
        ButterKnife.bind(this);
        init();
        loadData();
    }

    @Override
    protected View onCreateTopBar(ViewGroup view) {
        return TopBarFactory.createDefault(view, this, "").getView();
    }

    /**
     * 测试数据
     *
     * @return
     */
    private List<OfficalListBean.OfficerListBean> mockTest() {
        List<OfficalListBean.OfficerListBean> list = new ArrayList<>();

        //官员1
        OfficalListBean.OfficerListBean b = new OfficalListBean.OfficerListBean();
        b.setTitle("标题1");
        b.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        //官员1文章
        List<OfficalArticlesBean> mlist = new ArrayList<>();
        OfficalArticlesBean bb = new OfficalArticlesBean();
        bb.setTitle("子标题1");
        bb.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bb1 = new OfficalArticlesBean();
        bb1.setTitle("子标题1");
        bb1.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bb2 = new OfficalArticlesBean();
        bb2.setTitle("子标题1");
        bb2.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        mlist.add(bb);
        mlist.add(bb1);
        mlist.add(bb2);
        b.setArticles(mlist);

        //官员2
        OfficalListBean.OfficerListBean b1 = new OfficalListBean.OfficerListBean();
        b1.setTitle("标题1");
        b1.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        //官员2文章
        List<OfficalArticlesBean> mlist1 = new ArrayList<>();
        OfficalArticlesBean bbb = new OfficalArticlesBean();
        bbb.setTitle("子标题1");
        bbb.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbb1 = new OfficalArticlesBean();
        bbb1.setTitle("子标题1");
        bbb1.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbb2 = new OfficalArticlesBean();
        bbb2.setTitle("子标题1");
        bbb2.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        mlist1.add(bbb);
        mlist1.add(bbb1);
        mlist1.add(bbb2);
        b1.setArticles(mlist1);

        //官员3
        OfficalListBean.OfficerListBean b2 = new OfficalListBean.OfficerListBean();
        b2.setTitle("标题1");
        b2.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        //官员3文章
        List<OfficalArticlesBean> mlist2 = new ArrayList<>();
        OfficalArticlesBean bbbb = new OfficalArticlesBean();
        bbbb.setTitle("子标题1");
        bbbb.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbb1 = new OfficalArticlesBean();
        bbbb1.setTitle("子标题1");
        bbbb1.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbb2 = new OfficalArticlesBean();
        bbbb2.setTitle("子标题1");
        bbbb2.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        mlist2.add(bbbb);
        mlist2.add(bbbb1);
        mlist2.add(bbbb2);
        b2.setArticles(mlist2);

        //官员4
        OfficalListBean.OfficerListBean b3 = new OfficalListBean.OfficerListBean();
        b3.setTitle("标题1");
        b3.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        //官员4文章
        List<OfficalArticlesBean> mlist3 = new ArrayList<>();
        OfficalArticlesBean bbbbb = new OfficalArticlesBean();
        bbbbb.setTitle("子标题1");
        bbbbb.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbbb1 = new OfficalArticlesBean();
        bbbbb1.setTitle("子标题1");
        bbbbb1.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbbb2 = new OfficalArticlesBean();
        bbbbb2.setTitle("子标题1");
        bbbbb2.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        mlist3.add(bbbbb);
        mlist3.add(bbbbb1);
        mlist3.add(bbbbb2);
        b3.setArticles(mlist3);

        //官员5
        OfficalListBean.OfficerListBean b4 = new OfficalListBean.OfficerListBean();
        b4.setTitle("标题1");
        b4.setList_pic("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        //官员5文章
        List<OfficalArticlesBean> mlist4 = new ArrayList<>();
        OfficalArticlesBean bbbbbb = new OfficalArticlesBean();
        bbbbbb.setTitle("子标题1");
        bbbbbb.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbbbb1 = new OfficalArticlesBean();
        bbbbbb1.setTitle("子标题1");
        bbbbbb1.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        OfficalArticlesBean bbbbbb2 = new OfficalArticlesBean();
        bbbbbb2.setTitle("子标题1");
        bbbbbb2.setPhoto("http://stc.zjol.com.cn/g1/M00015BCggSBFRpu3iABgN_AADQ1ouTCEs234.png?width=226&height=226");
        mlist4.add(bbbbbb);
        mlist4.add(bbbbbb1);
        mlist4.add(bbbbbb2);
        b4.setArticles(mlist4);

        list.add(b);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        list.add(b4);
        return list;
    }

    private void testInit(List<OfficalListBean.OfficerListBean> list) {
        list = mockTest();//data.getOfficer_list();
        if (list != null) {
            if (mAdapter == null) {
                mAdapter = new PersionalListAdapter();
                mAdapter.setupData(list);
                initAdapter();
            }
            mRecycler.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 下拉加载所有官员列表数据
     */
    private void loadData() {
        new OfficalListTask(new APIExpandCallBack<OfficalListBean>() {

            @Override
            public void onSuccess(OfficalListBean data) {
                if (data == null) {
                    return;
                }
                testInit(mockTest());
//                if (data.getResultCode() == 0) {//成功
//                    list = mockTest();//data.getOfficer_list();
//                    if (list != null) {
//                        if (mAdapter == null) {
//                            mAdapter = new PersionalListAdapter();
//                            initAdapter();
//                        }
//                        mRecycler.setAdapter(mAdapter);
////                        mAdapter.setupData(list);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    T.showShort(PersionalListActivity.this, data.getResultMsg());
//                }
            }

            @Override
            public void onError(String errMsg, int errCode) {
                //WLJ  TEST
                testInit(mockTest());
                T.showShort(getBaseContext(), errMsg);
            }

        }).setTag(this).exe("5", "5");

    }

    /**
     * 初始化分隔符
     */
    private void init() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new ListSpaceDivider(0.5d, R.attr.dc_dddddd, true));
        //添加刷新头
        refresh = new HeaderRefresh(mRecycler);
        refresh.setOnRefreshListener(this);
        more = new FooterLoadMore(mRecycler, this);

    }


    //官员稿件测试地址
    private String uri1 = "10.100.60.98:9000";// 开发环境
    private String uri2 = "10.100.60.93:9000";// 测试环境
    private String uri3 = "zj.zjol.com.cn";// 正式环境
    private String uri4 = "m.8531.cn";// 正式环境2

    /**
     * 初始化适配器
     * 如果禁言，则不允许弹出评论框
     */
    private void initAdapter() {
        mAdapter.setHeaderRefresh(refresh.getItemView());
        mAdapter.setFooterLoadMore(more.getItemView());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (list != null && list.get(position) != null && list.size() > 0) {
                    OfficalArticlesBean b = (OfficalArticlesBean) mAdapter.getData(position);
                    String uri = b.getUrl();
                    int type = b.getType();
                    int officalId = b.getOfficalId();
                    //进入官员详情页
                    if (type == PersionalListAdapter.TYPE_PERSIONAL_DETAIL) {
                        Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
                                .buildUpon()
                                .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(officalId))
                                .build(), 0);
                    } else {
                        if (uri != null && !uri.isEmpty()) {
                            //普通稿件
                            if ((uri.contains(uri1) ||
                                    uri.contains(uri2) ||
                                    uri.contains(uri3) ||
                                    uri.contains(uri4))) {
                                Uri u = Uri.parse(uri);
                                //稿件ID
                                String articleId = u.getQueryParameter(Key.ARTICLE_ID);
                                if (!articleId.isEmpty()) {
                                    Nav.with(UIUtils.getActivity()).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
                                            .buildUpon()
                                            .appendQueryParameter(Key.ARTICLE_ID, String.valueOf(articleId))
                                            .build(), 0);
                                }
                            } else {//链接稿/直播

                            }
                        }

                    }

                }
            }
        });
    }


    /**
     * 最后一次下发的官员ID
     */
    private int lastOfficalId = 0;

    /**加载更多
     * @param data
     * @param loadMore
     */
    @Override
    public void onLoadMoreSuccess(OfficalListBean data, LoadMore loadMore) {
        if (data != null) {
            List<OfficalListBean.OfficerListBean> list = data.getOfficer_list();
            if (list != null && list.size() > 0) {
                lastOfficalId = getLastOfficalId(list);
            }
            mAdapter.addData(list, true);
        } else {
            loadMore.setState(LoadMore.TYPE_NO_MORE);
        }
    }

    /**
     * 加载更多每次加载3条
     * @param callback 官员列表每次下发20条
     */
    @Override
    public void onLoadMore(LoadingCallBack<OfficalListBean> callback) {
        new OfficalListTask(callback).setTag(this).exe(lastOfficalId + "", "3");
    }

    /**
     * 下拉刷新回调
     */
    @Override
    public void onRefresh() {
        loadData();
    }

    /**
     * @param list
     * @return 获取最后一次刷新的官员id
     */
    private int getLastOfficalId(List<OfficalListBean.OfficerListBean> list) {
        return list.get(list.size() - 1).getId();
    }
}
