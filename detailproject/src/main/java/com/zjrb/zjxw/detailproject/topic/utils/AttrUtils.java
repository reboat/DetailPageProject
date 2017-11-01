package com.zjrb.zjxw.detailproject.topic.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

/**
 * attr 解析 工具类
 *
 * @author a_liYa
 * @date 2017/11/1 13:07.
 */
public class AttrUtils {

    private static TypedValue sOutValue = new TypedValue();

    public static int getColor(Resources.Theme theme, @AttrRes int attrId) {
        theme.resolveAttribute(attrId, sOutValue, true);
        switch (sOutValue.type) {
            case TypedValue.TYPE_INT_COLOR_ARGB4:
            case TypedValue.TYPE_INT_COLOR_ARGB8:
            case TypedValue.TYPE_INT_COLOR_RGB4:
            case TypedValue.TYPE_INT_COLOR_RGB8:
                return sOutValue.data;
            default:
                return Color.TRANSPARENT;
        }
    }

}
