package com.zhejiangdaily;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.zjrb.core.api.callback.APIExpandCallBack;
import com.zjrb.core.common.base.BaseActivity;
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
//                UserBiz.get().setSession(result.getSession());

            }
        }).setTag(this).exe();
    }


//    mArticleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
//    mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
//    mVideoPath = data.getQueryParameter(Key.VIDEO_PATH);

    private Bundle bundle;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
//                if (bundle == null) {
//                    bundle = new Bundle();
//                }
//                bundle.putInt(Key.ID, 739652);
//                Nav.with(UIUtils.getContext()).setExtras(bundle).to("https://zj.zjol.com.cn/news.html");

                Nav.with(this).to("http://10.200.70.86:8085/topic.html?id=740398",0);
//、
//                Nav.with(this).to("http://10.200.70.86:8000/link.html?id=740302",0);
//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/TopicListActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.GROUP_ID, "5")
//                        .appendQueryParameter(Key.TITLE, "各地实践")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("https://www.8531.cn/detail/CommentActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ID, "739652")
//                        .appendQueryParameter(Key.MLF_ID, "12653")
//                        .appendQueryParameter(Key.TITLE, "骄傲啥啥啥是骄傲啥事安静地王大厦的描述发")
//                        .appendQueryParameter(Key.COMMENT_SET, "1")
//                        .appendQueryParameter(Key.PARENT_ID, "2324")
//                        .appendQueryParameter(Key.PARENT_ID, "true")
//                        .build(), 0);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/AtlasDetailActivity")
//                        .buildUpon()
//                        .appendQueryParameter(Key.ID, "739652")
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

//                Nav.with(this).to("https://zj.zjol.com.cn/subject.html?id=740345");

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/ActivityTopicActivity")
//                        .buildUpon()
//                        .build(), 0);
//
//                Nav.with(this).to(Uri.parse("https://zj.zjol.com.cn/topic.html?id=65427")
//                        .buildUpon()
//                        .build(), 0);


                break;
        }
    }
}
