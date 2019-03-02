package com.zjrb.zjxw.detailproject.utils;

import com.zjrb.core.db.SPHelper;

import cn.daily.news.biz.core.constant.C;
import cn.daily.news.biz.core.network.compatible.APICallManager;

/**
 * 设置相关的业务处理
 *
 * @author a_liYa
 * @date 2016/12/8 20:50.
 */
public class SettingBiz {

    private volatile static SettingBiz mInstance;

    public static SettingBiz get() {
        if (mInstance == null) {
            synchronized (SettingBiz.class) {
                if (mInstance == null) {
                    mInstance = new SettingBiz();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public static SettingBiz init() {
        return get();
    }

    /**
     * Html 文字大小比例
     */
    private float htmlFontScale;

    /**
     * 使用跟随系统配置
     */
    private boolean isUseSystemConfig = true;

    /**
     * 构造器
     */
    private SettingBiz() {
        SPHelper spHelper = SPHelper.get();
        htmlFontScale = spHelper.get(SPKey.SETTING_FONTSIZE, C.FONT_SCALE_STANDARD);
        isUseSystemConfig = spHelper.get(SPKey.USE_SYSTEM_CONFIG, true);
    }

    public float getHtmlFontScale() {
        return htmlFontScale;
    }

    public void setHtmlFontScale(float htmlFontScale) {
        if (htmlFontScale != this.htmlFontScale) {
            SPHelper.get().put(SPKey.SETTING_FONTSIZE, htmlFontScale).commit();
            this.htmlFontScale = htmlFontScale;
        }
    }

    public boolean isUseSystemConfig() {
        return isUseSystemConfig;
    }

    public void setUseSystemConfig(boolean useSystemConfig) {
        if (useSystemConfig != isUseSystemConfig) {
            isUseSystemConfig = useSystemConfig;
            SPHelper.get().put(SPKey.USE_SYSTEM_CONFIG, useSystemConfig).commit();
        }
    }

}
