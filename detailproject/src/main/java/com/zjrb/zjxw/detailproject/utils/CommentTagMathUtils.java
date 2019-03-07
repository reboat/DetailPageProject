package com.zjrb.zjxw.detailproject.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.zjrb.core.db.SPHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.daily.news.biz.core.db.ThemeMode;
import cn.daily.news.biz.core.model.ResourceBiz;

/**
 * 评论匹正则匹配
 * Created by wanglinjie.
 * create time:2019/3/7  上午9:01
 */
public class CommentTagMathUtils {
    private static CommentTagMathUtils mInstance;

    private CommentTagMathUtils() {
    }

    public static CommentTagMathUtils newInstance() {
        if (mInstance == null) {
            synchronized (CommentTagMathUtils.class) {
                if (mInstance == null) {
                    mInstance = new CommentTagMathUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 评论标签算法
     *
     * @param s
     * @return
     */
    public SpannableString doCommentTag(String s) {
        SpannableString spannableString = new SpannableString(s);
        ResourceBiz sp = SPHelper.get().getObject("initialization_resources");
        //如果正则为空，则清除标签
        if (sp == null || TextUtils.isEmpty(sp.comment_pattern)) {
            SPHelper.get().put("comment_tag", "").commit();
            return spannableString;
        }
        Pattern datePattern = Pattern.compile(sp.comment_pattern);
        if (datePattern == null || TextUtils.isEmpty(s)) return null;
        Matcher dateMatcher = datePattern.matcher(s);
        while (dateMatcher.find()) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(getcolor())), dateMatcher.start(), dateMatcher.end(), 0);
            break;
        }
        return spannableString;
    }

    private String getcolor() {
        if (ThemeMode.isNightMode()) {
            return "#4b7aae";
        } else {
            return "#036ce2";
        }
    }
}
