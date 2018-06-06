package com.zjrb.zjxw.detailproject.utils;

import android.text.TextUtils;

import com.zjrb.core.db.ThemeMode;
import com.zjrb.core.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * WebView相关业务处理
 * Created by wanglinjie.
 * create time:2017/7/28  上午11:18
 */
public class WebBiz {

    /**
     * 整数正则表达式
     */
    private static final String REGEX_INTEGER = "^[-\\+]?[\\d]+$";

    /**
     * 解析处理Html内容
     *
     * @param html          HtmlBody字符串
     * @param callBack      图片
     * @param callBack1     超链接图片
     * @param textBack      标题
     * @param audioCallBack 音频
     * @return 处理过的Html Body内容
     */
    public static String parseHandleHtml(String html, ImgSrcsCallBack callBack, ImgASrcsCallBack callBack1, TextCallBack textBack, AudioCallBack audioCallBack) {

        Document doc = Jsoup.parseBodyFragment(html);
        List<String> imgSrcs = parseImgTags(doc);
        List<Map<String, String>> imgSrcs1 = parseAImgTags(doc);
        parseVideoTags(doc);

        if (callBack != null) {
            callBack.callBack((imgSrcs.toArray(new String[imgSrcs.size()])));
        }

        //超链接
        if (callBack1 != null) {
            callBack1.callBack(imgSrcs1);
        }

        if (textBack != null) {
            textBack.callBack(doc.text());
        }

        if (audioCallBack != null) {
            audioCallBack.callBack(parseAudioTags(doc));
        }

        return doc.body().html();

    }


    /**
     * 解析音频标签
     *
     * @param doc
     * @return
     */
    private static int parseAudioTags(Document doc) {
        if (doc == null) return 0;
        Elements audio = doc.getElementsByTag(html.tag.AUDIO);
        if (audio != null && audio.size() > 0) {
            return audio.size();
        } else {
            return 0;
        }
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
                if (isNeedOnClick && !ThemeMode.isNightMode()) {
                    node.attr("onClick", "imageBrowse(" + index + ")");
                }
                //夜间模式应添加长按模式
                fitUiModeForImgTag(node, isNeedOnClick, index);
            }
        }

        return imgSrcs;
    }


    /**
     * 解析处理带超链接的Img标签
     *
     * @param doc Document
     * @return 返回Img标签的src集合
     */
    private static List<Map<String, String>> parseAImgTags(Document doc) {
        List<Map<String, String>> imgSrcs = new ArrayList<>();

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
                    if ((html.tag.A.equalsIgnoreCase(parent.tagName())
                            && !TextUtils.isEmpty(parent.attr(html.attr.HREF)))) {
                        Map<String, String> map = new HashMap<>();
                        map.put(node.attr(html.attr.SRC), node.parent().attr(html.attr.HREF));
                        imgSrcs.add(map);
                        ++index;
                        isNeedOnClick = widthPx != 0;

                    }
                }

                // 夜间模式点击应在遮罩层
                if (isNeedOnClick && !ThemeMode.isNightMode()) {
                    node.attr("onClick", "imageABrowse(" + index + ")");
                }
                fitUiModeForImgATag(node, isNeedOnClick, index);
            }
        }

        return imgSrcs;
    }

    private static void replaceWebViewPic(int position, String url) {

    }


    /**
     * 适配Img标签
     *
     * @param node         Img节点
     * @param isAddOnClick
     * @param index
     */
    private static void fitUiModeForImgTag(Element node, boolean isAddOnClick, int index) {
        if (ThemeMode.isNightMode()) { // 是否适配夜间模式

            Element div = new Element("div");
            div.attr("class", "zjxw_imgBox");
            node.replaceWith(div);

            div.appendChild(node);

            Element imgBgDiv = new Element("div");

            if (isAddOnClick) {
                imgBgDiv.attr("onClick", "imageBrowse(" + index + ")");
            }

            imgBgDiv.attr("class", "zjxw_imgMask");


            div.appendChild(imgBgDiv);
        }
    }

    /**
     * 适配Img标签
     *
     * @param node         Img节点
     * @param isAddOnClick
     * @param index
     */
    private static void fitUiModeForImgATag(Element node, boolean isAddOnClick, int index) {
        if (ThemeMode.isNightMode()) { // 是否适配夜间模式

            Element div = new Element("div");
            div.attr("class", "zjxw_imgBox");
            node.replaceWith(div);

            div.appendChild(node);

            Element imgBgDiv = new Element("div");

            if (isAddOnClick) {
                imgBgDiv.attr("onClick", "imageABrowse(" + index + ")");
            }

            imgBgDiv.attr("class", "zjxw_imgMask");


            div.appendChild(imgBgDiv);
        }
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


    /**
     * 带超链接父节点的图片集合
     */
    public interface ImgASrcsCallBack {
        void callBack(List<Map<String, String>> imgSrcs);
    }

    /**
     * 获取网页图集
     */
    public interface ImgSrcsCallBack {
        void callBack(String[] imgSrcs);
    }

    /**
     * 获取网页中的文本
     */
    public interface TextCallBack {
        void callBack(String text);
    }

    /**
     * 获取网页中的音频
     */
    public interface AudioCallBack {
        void callBack(int count);
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

            static final String AUDIO = "audio";

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
