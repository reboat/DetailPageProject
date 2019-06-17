package com.zjrb.zjxw.detailproject.utils;

import java.io.IOException;

/**
 * Created by wanglinjie on 2017/1/6.
 * 华为
 */

public class EMUIUtils {
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.hw_emui_api_level";

    //是否为华为EMUI系统
    public static boolean isEMUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_EMUI_VERSION_CODE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }
}
