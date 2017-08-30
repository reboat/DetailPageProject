package com.zjrb.detailpageproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseActivity;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv_text);
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
//                ARouter.getInstance().build("/module/detail/NewsDetailActivity")
//                        .navigation();

                break;
        }
    }
}
