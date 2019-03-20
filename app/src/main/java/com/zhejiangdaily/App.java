package com.zhejiangdaily;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.aliya.uimode.UiModeManager;
import com.netease.mobsec.rjsb.watchman;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.db.DatabaseLoader;

import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.network.DailyNetworkManager;
import cn.daily.news.biz.core.utils.BaseInit;
import cn.daily.news.update.UpdateManager;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        DailyNetworkManager.init(this);
//        AppUtils.setChannel("bianfeng");
//        UIUtils.init(this);
//        AppUtils.setChannel("hah");
//        SettingManager.init(this);
//        ThemeMode.init(this);
//        UiModeManager.init(this, R.styleable.SupportUiMode);
//        BaseInit.init(this,"bianfeng");
//        setTheme(ThemeMode.isNightMode() ? R.style.NightAppTheme : R.style.AppTheme);


        UIUtils.init(this);
        DailyNetworkManager.init(this);
        AppUtils.setChannel("bianfeng");
        watchman.init(this, "YD00158346389550");
        UpdateManager.init(this);
        DatabaseLoader.init(this);

    }
}
