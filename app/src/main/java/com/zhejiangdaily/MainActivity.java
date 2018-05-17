package com.zhejiangdaily;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zjrb.core.common.base.BaseActivity;
import com.zjrb.core.nav.Nav;
import com.zjrb.core.utils.SettingManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv_text);
        tv.setOnClickListener(this);
        findViewById(R.id.tv_ui_mode).setOnClickListener(this);
    }


//    mArticleId = Integer.parseInt(data.getQueryParameter(Key.ARTICLE_ID));
//    mlfId = Integer.parseInt(data.getQueryParameter(Key.MLF_ID));
//    mVideoPath = data.getQueryParameter(Key.VIDEO_PATH);

    private Bundle bundle;

    @Override
    public void onClick(View v) {
//        Nav.with(this).to("https://zjbeta.8531.cn/album.html?id=741050");
//        Nav.with(this).to("https://zjbeta.8531.cn/topic.html?id=741648"); // 741334
//        Nav.with(this).to("https://zj.zjol.com.cn/subject.html?id=256620"); // 741791
//        Nav.with(this).to("https://zj.zjol.com.cn/subject.html?id=785088"); // 741791
//        Nav.with(this).to("https://zjbeta.8531.cn/subject.html?id=741790");

//        switch (v.getId()) {
//            case R.id.tv_text:
//                if (bundle == null) {
//                    bundle = new Bundle();
//                }
//                bundle.putInt(Key.ID, 739652);
//                Nav.with(UIUtils.getContext()).setExtras(bundle).to("https://zj.zjol.com
// .cn/news.html");

//                Nav.with(this).to("http://10.200.70.86:8000/link.html?id=740555");
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


        // 专题详情页
//                Nav.with(this).to("https://www.8531.cn/subject.html?id=740345");
//                Nav.with(this).to("https://www.8531.cn/subject.html?id=740823");

        // 图集详情页
//                Nav.with(this).to("http://zjbeta.8531.cn/album.html?id=741157");

        // 话题详情页
//                Nav.with(this).to("http://zjbeta.8531.cn/topic.html?id=802710"); // 802710,802979

        //普通详情页
                Nav.with(this).to("https://zjprev.8531.cn/red_boat.html?id=100003112");
//        Nav.with(this).to("https://www.baidu.com/");
//                Nav.with(this).to("https://zj.zjol.com.cn/video.html?id=925559");
        //红船号详情页
//                SettingManager.getInstance().setOpenHttps(true);
//                SettingManager.getInstance().setHost("api-new.8531.cn");
//                Nav.with(this).to("https://zj.zjol.com.cn/video.html?id=925559");


//                Nav.with(this).to("https://zj.zjol.com.cn/news.html?id=920034");

//                //进入专题更多列表
//                Bundle bundle = new Bundle();
//                bundle.putString(IKey.GROUP_ID, String.valueOf(1313));
//                bundle.putString(IKey.TITLE, "时局");
//                Nav.with(v.getContext()).setExtras(bundle).toPath(RouteManager.TOPIC_LIST);

//                Nav.with(this).to(Uri.parse("http://www.8531.cn/detail/ActivityTopicActivity")
//                        .buildUpon()
//                        .build(), 0);
//
//                Nav.with(this).to(Uri.parse("https://zj.zjol.com.cn/topic.html?id=65427")
//                        .buildUpon()
//                        .build(), 0);


//                break;
//            case R.id.tv_ui_mode:
//                ThemeMode.setUiMode(!ThemeMode.isNightMode());
//                break;
//        }
    }
}
