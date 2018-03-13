package com.zjrb.zjxw.detailproject.redBoat;

import android.os.Bundle;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

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

    @BindView(R2.id.test_tv)
    TextView testTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_detail_redboat_activity);
        ButterKnife.bind(this);
    }
}
