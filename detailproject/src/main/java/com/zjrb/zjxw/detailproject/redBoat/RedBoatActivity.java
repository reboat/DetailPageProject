package com.zjrb.zjxw.detailproject.redBoat;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.aliya.view.fitsys.FitWindowsFrameLayout;
import com.aliya.view.fitsys.FitWindowsRecyclerView;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;
import com.zjrb.zjxw.detailproject.topic.widget.ColorImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：红船号详情页
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/12 2007
 */

public class RedBoatActivity extends BaseActivity {

    @BindView(R2.id.recycler)
    FitWindowsRecyclerView recycler;
    @BindView(R2.id.iv_top_back)
    ColorImageView ivTopBack;
    @BindView(R2.id.iv_top_share)
    ColorImageView ivTopShare;
    @BindView(R2.id.top_line)
    View topLine;
    @BindView(R2.id.top_bar)
    FitWindowsFrameLayout topBar;
    @BindView(R2.id.v_container)
    FrameLayout vContainer;
    @BindView(R2.id.ry_container)
    FitWindowsFrameLayout ryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
    }
}
