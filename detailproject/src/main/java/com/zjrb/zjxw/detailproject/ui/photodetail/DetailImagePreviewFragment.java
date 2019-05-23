package com.zjrb.zjxw.detailproject.ui.photodetail;

import android.os.Bundle;

import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import cn.daily.news.analytics.Analytics;
import cn.daily.news.analytics.ObjectType;
import cn.daily.news.biz.core.web.ImagePreviewFragment;

public class DetailImagePreviewFragment extends ImagePreviewFragment {

    private static final String ARGS = "args";
    private static final String FLAG = "flag";
    private static final String ID = "mlf_id";

    private DraftDetailBean.ArticleBean mBean;
    private Analytics mAnalytics;


    public static DetailImagePreviewFragment newInstance(DraftDetailBean.ArticleBean bean,String url, boolean isFromAtlas, int mlfid) {
        DetailImagePreviewFragment fragment = new DetailImagePreviewFragment();

        Bundle args = new Bundle();
        args.putString(ARGS, url);
        args.putBoolean(FLAG, isFromAtlas);
        args.putInt(ID, mlfid);
        args.putSerializable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mBean = (DraftDetailBean.ArticleBean) args.getSerializable("bean");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBean != null) {
            mAnalytics = new Analytics.AnalyticsBuilder(getContext(), "A0010", "PicturePageStay", true)
                    .name("图片浏览（左右滑动）")
                    .selfObjectID(String.valueOf(mBean.getMlf_id()))
                    .columnID(String.valueOf(mBean.getColumn_id()))
                    .classShortName(mBean.getChannel_name())
                    .objectShortName(mBean.getDoc_title())
                    .classID(mBean.getChannel_id())
                    .pageType("图集详情页")
                    .ilurl(mBean.getUrl())
                    .seObjectType(ObjectType.C01)
                    .objectID(String.valueOf(mBean.getMlf_id()))
                    .columnName(mBean.getColumn_name())
                    .selfNewsID(String.valueOf(mBean.getId()))
                    .pubUrl(mBean.getUrl())
                    .selfChannelID(mBean.getChannel_id())
                    .newsID(String.valueOf(mBean.getMlf_id()))
                    .newsTitle(mBean.getDoc_title())
                    .channelName(mBean.getChannel_name())
                    .build();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAnalytics!=null){
            mAnalytics.sendWithDuration();
        }
    }
}
