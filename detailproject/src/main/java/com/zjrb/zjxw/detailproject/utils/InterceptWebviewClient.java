package com.zjrb.zjxw.detailproject.utils;

import android.webkit.WebViewClient;


/**
 * 详情页链接点击拦截
 * Created by wanglinjie.
 * create time:2018/6/12  上午8:57
 */

public class InterceptWebviewClient extends WebViewClient {

//    private boolean isRedirect; // true : 重定向
//
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        if (!TextUtils.isEmpty(url)) {
//            Uri uri = Uri.parse(url);
//            if (uri != null && !TextUtils.equals(uri.getScheme(), "http") && !TextUtils.equals(uri.getScheme(), "https")) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            if (isRedirect) { // 重定向
//                view.loadUrl(url);
//            } else { // 点击跳转
//                if (ClickTracker.isDoubleClick()) return true;
//                if (Nav.with(getContext()).to(url)) {
//                    //点击话题链接
//                    if (url.contains("topic.html?id=")) {
//                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800016", "800016",null,false)
//                                .setEvenName("点击话题标签")
//                                .setPageType("新闻详情页")
//                                .build()
//                                .send();
//
//                        //官员名称
//                    } else if (url.contains("gy.html?id=")) {
//                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800017", "800017",null,false)
//                                .setEvenName("点击官员名称")
//                                .setPageType("新闻详情页")
//                                .setOtherInfo(Analytics.newOtherInfo()
//                                        .put("customObjectType", "OfficerType")
//                                        .toString())
//                                .build()
//                                .send();
//                    } else {
//                        new Analytics.AnalyticsBuilder(UIUtils.getContext(), "800015", "800015","RelatedContentClick",false)
//                                .setEvenName("链接点击")
//                                .setPageType("新闻详情页")
//                                .setOtherInfo(Analytics.newOtherInfo()
//                                        .put("mediaURL", url)
//                                        .toString())
//                                .pageType("新闻详情页")
//                                .relatedContentClick("链接")
//                                .build()
//                                .send();
//                    }
//                    return true;
//                }
//            }
//
//        }
//        return super.shouldOverrideUrlLoading(view, url);
//    }
//
//    @Override
//    public void onPageFinished(WebView view, String url) {
//        super.onPageFinished(view, url);
//        isRedirect = false;
//    }
//
//    @Override
//    public void onPageStarted(WebView view, String url, Bitmap favicon) {
//        super.onPageStarted(view, url, favicon);
//        isRedirect = true;
//    }

}

