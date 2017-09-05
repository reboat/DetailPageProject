package com.zjrb.detailpageproject;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.common.biz.UserBiz;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.T;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv_text);
        tv.setOnClickListener(this);
        initTest();
    }

    /**
     * 获取sessionId
     */
    private void initTest() {
        new InitTask(new APIExpandCallBack<SessionIdBean>() {
            @Override
            public void onError(String errMsg, int errCode) {
                T.showShortNow(MainActivity.this, "获取sessionId失败");
            }

            @Override
            public void onSuccess(@NonNull SessionIdBean result) {
                if (result.getResultCode() == 0) {
                    UserBiz.get().setSessionId(result.getSession().getId());
                } else {
                    T.showShortNow(MainActivity.this, "未知错误");
                }

            }
        }).setTag(this).exe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
                        .buildUpon()
//                        .appendQueryParameter(Key.LOGIN_TYPE, Key.Value.LOGIN_RESET_TYPE)
                        .build(), 0);

                break;
        }
    }
}
