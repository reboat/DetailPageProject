package com.zhejiangdaily;

import android.support.multidex.MultiDexApplication;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.common.base.BaseInit;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.SettingManager;
import com.zjrb.core.utils.UIUtils;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
        SettingManager.init(this);
        ThemeMode.initTheme(R.style.AppTheme, R.style.NightAppTheme);
        UiModeManager.init(this, R.styleable.SupportUiMode);
        BaseInit.getInstance().init(this,"bianfeng");
    }
}
