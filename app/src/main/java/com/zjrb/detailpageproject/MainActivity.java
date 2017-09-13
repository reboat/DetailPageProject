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
import com.zjrb.zjxw.detailproject.global.Key;


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


//    mArticleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
//    mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
//    mVideoPath = data.getQueryParameter(Key.VIDEO_PATH);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/NewsDetailActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ARTICLE_ID, "65427")
//                        .appendQueryParameter(Key.MLF_ID,"12653")
//                        .appendQueryParameter(Key.VIDEO_PATH,"http//www.baidu.com")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/CommentActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ARTICLE_ID, "739652")
//                        .appendQueryParameter(Key.MLF_ID,"12653")
//                        .appendQueryParameter(Key.TITLE,"骄傲啥啥啥是骄傲啥事安静地王大厦的描述发")
//                        .appendQueryParameter(Key.COMMENT_SET,"1")
//                        .appendQueryParameter(Key.PARENT_ID,"2324")
//                        .appendQueryParameter(Key.PARENT_ID,"true")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/AtlasDetailActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ARTICLE_ID, "739652")
//                        .appendQueryParameter(Key.MLF_ID,"12653")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/PersionalDetailActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.OFFICIAL_ID, "5")
//                        .build(), 0);
//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/PersionalListActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.OFFICIAL_ID, "5")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/NewsTopicActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ARTICLE_ID, "65427")
//                        .build(), 0);
                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/ActivityTopicActivity")
                        .buildUpon()
                        .build(), 0);

                break;
        }
    }
}
