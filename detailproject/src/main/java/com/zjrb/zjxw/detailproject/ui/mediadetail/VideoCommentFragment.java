package com.zjrb.zjxw.detailproject.ui.mediadetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.apibean.bean.DraftDetailBean;

import butterknife.ButterKnife;
import cn.daily.news.biz.core.DailyFragment;

/**
 * Created by wanglinjie.
 * create time:2019/3/22  下午4:24
 */
public class VideoCommentFragment extends DailyFragment {
    public static final String FRAGMENT_DETAIL_COMMENT = "fragment_detail_comment";

    private DraftDetailBean mNewsDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNewsDetail = (DraftDetailBean) getArguments().getSerializable(FRAGMENT_DETAIL_COMMENT);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.module_detail_fragment_relate_news, container, false);
    }
}
