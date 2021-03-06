package com.zhejiangdaily;

import android.support.multidex.MultiDexApplication;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.common.base.BaseInit;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.AppUtils;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.UIUtils;
import com.zjrb.daily.db.DatabaseLoader;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
        AppUtils.setChannel("hah");
        SettingManager.init(this);
        ThemeMode.initTheme(R.style.AppTheme, R.style.NightAppTheme);
        UiModeManager.init(this, R.styleable.SupportUiMode);
        BaseInit.init(this,"bianfeng");
        setTheme(ThemeMode.isNightMode() ? R.style.NightAppTheme : R.style.AppTheme);

        DatabaseLoader.init(this);
    }
}
