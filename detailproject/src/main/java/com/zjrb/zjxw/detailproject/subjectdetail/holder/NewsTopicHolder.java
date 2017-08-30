package com.zjrb.zjxw.detailproject.subjectdetail.holder;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjrb.coreprojectlibrary.common.base.BaseRecyclerViewHolder;
import com.zjrb.coreprojectlibrary.utils.UIUtils;
import com.zjrb.zjxw.detailproject.R;
import com.zjrb.zjxw.detailproject.R2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表 - 专题类型 - 上图、中标题、下阅读量
 *
 * @author a_liYa
 * @date 2017/7/7 15:33.
 */
public class NewsTopicHolder extends BaseRecyclerViewHolder {

    @BindView(R2.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R2.id.tv_tag)
    TextView mTvTag;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_other)
    TextView mTvOther;

    private String keyword;

    public NewsTopicHolder(ViewGroup parent) {
        super(UIUtils.inflate(R.layout.module_detail_item_news_topic, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bindView() {
        highLightKeyword();
    }

    private void highLightKeyword() {

        String str = "KTV风暴KTV";
        SpannableString sp = new SpannableString(str);

        Pattern p = Pattern.compile("KTV");
        Matcher m = p.matcher(str);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            sp.setSpan(new ForegroundColorSpan(Color.parseColor("#f44b50")), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTvTitle.setText(sp);
    }

}
