package com.zhejiangdaily;

import android.support.multidex.MultiDexApplication;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.db.DatabaseLoader;

import cn.daily.news.biz.core.db.SettingManager;
import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.network.DailyNetworkManager;
import cn.daily.news.biz.core.utils.BaseInit;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        DailyNetworkManager.init(this);
        AppUtils.setChannel("bianfeng");
        UIUtils.init(this);
        AppUtils.setChannel("hah");
        SettingManager.init(this);
        ThemeMode.init(this);
        UiModeManager.init(this, R.styleable.SupportUiMode);
        BaseInit.init(this,"bianfeng");
        setTheme(ThemeMode.isNightMode() ? R.style.NightAppTheme : R.style.AppTheme);

        DatabaseLoader.init(this);
    }
}
