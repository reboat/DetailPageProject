package com.zjrb.detailpageproject;

import android.app.Application;

import com.aliya.uimode.UiModeManager;
import com.zjrb.core.common.base.BaseInit;
import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.UIUtils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UIUtils.init(this);
        ThemeMode.initTheme(R.style.AppTheme, R.style.NightAppTheme);
        UiModeManager.init(this, R.styleable.SupportUiMode);

    }
}
