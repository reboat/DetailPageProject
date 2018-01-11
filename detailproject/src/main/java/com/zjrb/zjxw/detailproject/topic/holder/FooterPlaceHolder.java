package com.zjrb.zjxw.detailproject.topic.holder;

import android.view.ViewGroup;

import com.zjrb.core.common.base.page.PageItem;
import com.zjrb.zjxw.detailproject.R;

/**
 * 底部占位 - footer
 *
 * @author a_liYa
 * @date 2018/1/11 14:22.
 */
public class FooterPlaceHolder extends PageItem {

    public FooterPlaceHolder(ViewGroup parent) {
        super(parent, R.layout.module_detail_topic_footer_place_holder);
        itemView.setTag(R.id.tag_fit_child, Boolean.TRUE);
    }
}
