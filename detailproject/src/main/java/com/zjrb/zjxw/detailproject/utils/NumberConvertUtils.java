package com.zjrb.zjxw.detailproject.utils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 订阅数、稿件数转换类
 *
 * @author zhangshugang
 */
public class NumberConvertUtils {
    private static final int TEN_THOUSANDS = 10_000;// 一万
    private static final int HUNDRED_THOUSANDS = 100_000;//十万
    private static final int HUNDRED_MILLIONS = 100_000_000;// 一亿
	private static DecimalFormat formatter = new DecimalFormat();

    /**
     * 把精确的数字转换成大概的数字，用于App端显示
     *
     * @param number
     * @return
     */
    public static String convert(Integer number) {
        if (number <= 0) {
            return "";
        }

        if (number < TEN_THOUSANDS) {
            return String.valueOf(number);
        }

        Integer unit = number < HUNDRED_MILLIONS ? TEN_THOUSANDS : HUNDRED_MILLIONS;
        String unitSymbol = number < HUNDRED_MILLIONS ? "万" : "亿";

        int beforePoint = number / unit;
        int afterPoint = (number % unit) / (unit / 10);

        String plusSymbol = "";// 是否显示加号
        if (number - (beforePoint * unit + afterPoint * (unit / 10)) > 0) {
            plusSymbol = "+";
        }

        if (afterPoint==0) {
            return beforePoint + unitSymbol + plusSymbol;
        } else {
            return beforePoint + "." + afterPoint + unitSymbol + plusSymbol;
        }
    }



    /**
     * 处理点赞数
     *
     * @param number 需要转化的数字
     * @return 转化字符串
     */
    public static String convertLikeCount(Integer number) {
        if (number <= 0) {
            return "";
        }

        if (number < HUNDRED_THOUSANDS) {
            return String.valueOf(number);
        }

        return formatter.format(1.0 * number / TEN_THOUSANDS) + "万";
    }



}
