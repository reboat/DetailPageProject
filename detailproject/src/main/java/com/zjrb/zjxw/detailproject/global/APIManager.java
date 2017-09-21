package com.zjrb.zjxw.detailproject.global;


/**
 * API管理
 * Created by wanglinjie.
 * create time:2017/7/24  下午4:12
 */
public class APIManager {

    /**
     * 正式包，一定不要忘记改为 false
     */
    public static boolean isDebug = true;

    /**
     * url 端点 路径
     */
    public static final class endpoint {
        /**
         * 获取sessionId
         */
        public static final String GET_SESSIONID = "/api/account/init";

        /**
         * 获取频道(撤稿)列表
         */
        public static final String GET_RANK_LIST = "/api/article/rank_list";

        /**
         * 详情页
         */
        public static final String NEWS_DETAIL = "/api/article/detail";

        /**
         * 评论列表
         */
        public static final String COMMENT_LIST = "/api/comment/list";

        /**
         * 评论点赞
         */
        public static final String COMMENT_PRISE = "/api/comment/like";

        /**
         * 删除评论
         */
        public static final String COMMENT_DELETE = "/api/comment/delete";

        /**
         * 官员详情
         */
        public static final String OFFICAL_DETAIL = "/api/officer/info";

        /**
         * 所有官员列表
         */
        public static final String OFFICAL_LIST = "/api/officer/list";

        /**
         * 获取专题列表
         */
        public static final String SUBJECT_LIST = "/api/article/subject_group_list";

        /**
         * 稿件收藏
         */
        public static final String DRAFT_COLLECT = "/api/article/collect";

        /**
         * 频道订阅
         */
        public static final String COLUMN_SUBSCRIBE = "/api/column/subscribe_action";
        /**
         * 稿件点赞
         */
        public static final String DRAFT_LIKE = " /api/article/like";

        /**
         * 提交评论
         */
        public static final String  COMMENT_SUBMIT = "/api/comment/create";

        /**
         * 分享新闻
         */
        public static final String DRAFT_SHARE = "/api/collection/share_news";

    }

}
