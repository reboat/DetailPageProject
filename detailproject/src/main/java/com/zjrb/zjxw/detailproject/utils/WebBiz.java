package com.zjrb.zjxw.detailproject.utils;

import android.app.UiModeManager;
import android.text.TextUtils;

import com.zjrb.coreprojectlibrary.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * WebView相关业务处理
 *
 * @author a_liYa
 * @date 2017/4/5 17:07.
 */
public class WebBiz {

    /**
     * 整数正则表达式
     */
    private static final String REGEX_INTEGER = "^[-\\+]?[\\d]+$";

    /**
     * 解析处理Html内容
     *
     * @param html     HtmlBody字符串
     * @param callBack
     * @return 处理过的Html Body内容
     */
    public static String parseHandleHtml(String html, ImgSrcsCallBack callBack) {

        Document doc = Jsoup.parseBodyFragment(html);

        List<String> imgSrcs = parseImgTags(doc);

        parseVideoTags(doc);

        if (callBack != null) {
            callBack.callBack((imgSrcs.toArray(new String[imgSrcs.size()])));
        }

        return doc.body().html();

    }


    /**
     * 解析视频相关标签 Video、iframe
     *
     * @param doc Document
     */
    private static void parseVideoTags(Document doc) {
        if (doc == null) return;

        Elements iframes = doc.getElementsByTag(html.tag.IFRAME);

        if (iframes != null) {
            for (int i = 0; i < iframes.size(); i++) {
                handleIframeElement(iframes.get(i));
            }
        }

    }

    /**
     * 处理 iframe标签
     *
     * @param node iframe标签
     */
    private static void handleIframeElement(Element node) {
        if (node == null) return;

        if (node.hasAttr(html.attr.SRC)) {

            String widthStr = node.attr(html.attr.WIDTH);
            String heightStr = node.attr(html.attr.HEIGHT);

            int width = -1;
            int height = -1;

            if (Pattern.compile(REGEX_INTEGER).matcher(widthStr).matches()) {
                width = Integer.parseInt(widthStr);
            } else if (widthStr.endsWith("px")) {
                String subPx = widthStr.substring(0, widthStr.indexOf("px"));
                // 判断是否为整数
                if (Pattern.compile(REGEX_INTEGER).matcher(subPx).matches()) {
                    width = Integer.parseInt(subPx);
                }
            }

            if (Pattern.compile(REGEX_INTEGER).matcher(heightStr).matches()) {
                height = Integer.parseInt(heightStr);
            } else if (heightStr.endsWith("px")) {
                String subPx = heightStr.substring(0, heightStr.indexOf("px"));
                // 判断是否为整数
                if (Pattern.compile(REGEX_INTEGER).matcher(subPx).matches()) {
                    height = Integer.parseInt(subPx);
                }
            }

            if (width > 0 && height > 0) {
                float screenWidthDp = UIUtils.px2dip(UIUtils.getScreenW());
                if (width > (screenWidthDp - 8 - 8)) {
                    height = Math.round(height * (screenWidthDp - 8 - 8) / width);
                    node.attr(html.attr.HEIGHT, String.valueOf(height));
                }
            }

        }

    }

    /**
     * 解析处理Img标签
     *
     * @param doc Document
     * @return 返回Img标签的src集合
     */
    private static List<String> parseImgTags(Document doc) {
        List<String> imgSrcs = new ArrayList<>();

        if (doc == null) return imgSrcs;

        Elements imgs = doc.getElementsByTag(html.tag.IMG);
        int index = -1;
        if (imgs != null) {
            for (int i = 0; i < imgs.size(); i++) {
                boolean isNeedOnClick = false;
                Element node = imgs.get(i);
                Element parent = node.parent();

                int widthPx = handleImgElementWidth(node);

                String src = node.attr(html.attr.SRC);
                if (!TextUtils.isEmpty(src)) {
                    if (!(html.tag.A.equalsIgnoreCase(parent.tagName())
                            && !TextUtils.isEmpty(parent.attr(html.attr.HREF)))) {
                        imgSrcs.add(src);
                        ++index;
                        isNeedOnClick = widthPx != 0;

                    }
                }

                // 夜间模式点击应在遮罩层
                if (isNeedOnClick && !UiModeManager.get().isNightMode()) {
                    node.attr("onClick", "imageBrowse(" + index + ")");
                }

                UiModeManager.get().fitUiModeForImgTag(node, isNeedOnClick, index);
            }
        }

        return imgSrcs;
    }

    /**
     * 处理img width 属性
     *
     * @param node img标签Element
     * @return -1 : 没有设置width属性
     */
    private static int handleImgElementWidth(Element node) {
        int widthPx = -1;
        if (node.hasAttr(html.attr.WIDTH)) {
            String width = node.attr(html.attr.WIDTH);
            if (width != null) {
                if (Pattern.compile(REGEX_INTEGER).matcher(width).matches()) {
                    widthPx = Integer.parseInt(width);
                } else if (width.endsWith("px")) {
                    String subPx = width.substring(0, width.indexOf("px"));
                    // 判断是否为整数
                    if (Pattern.compile(REGEX_INTEGER).matcher(subPx).matches()) {
                        widthPx = Integer.parseInt(subPx);
                    }
                }
            }
        }

        if (widthPx > 0) {
            float screenWidthDip = UIUtils.px2dip(UIUtils.getScreenW());
            // 判断像素是否接近屏幕宽度，设置为屏幕宽度
            if (widthPx > screenWidthDip) {
                widthPx = Math.round(screenWidthDip);
                node.attr(html.attr.WIDTH, widthPx + "px");
            }
            // 否则不做处理
        }
        return widthPx;
    }


    public interface ImgSrcsCallBack {

        void callBack(String[] imgSrcs);

    }

    /**
     * Html 相关常量
     */
    private static final class html {

        /* 标签 */
        static final class tag {

            static final String IMG = "img";

            static final String IFRAME = "iframe";

            static final String VIDEO = "video";

            static final String A = "a";

        }

        /* 属性 */
        static final class attr {

            static final String SRC = "src";

            static final String WIDTH = "width";

            static final String HEIGHT = "height";

            static final String HREF = "href";

        }

    }

}
