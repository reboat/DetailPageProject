package com.zjrb.zjxw.detailproject.global;

/**
 * 页面路由管理
 * Created by wanglinjie.
 * create time:2017/8/22  下午2:00
 */

public final class RouteManager {

    public static final String IMAGE_BROWSE_ACTIVITY = "/detail/ImageBrowseActivity";
    /**
     * 评论列表页
     */
    public static final String COMMENT_ACTIVITY_PATH = "/detail/CommentActivity";

    /**
     * 精选评论列表
     */
    public static final String COMMENT_SELECT_ACTIVITY = "/detail/CommentSelectActivity";

    /**
     * 频道详情页
     */
    public static final String SUBSCRIBE_PATH = "/news/ChannelListActivity";

    /**
     * 栏目列表
     */
    public static final String COLUMN_LIST = "/subscription/detail";

    /**
     * 官员详情页
     */
    public static final String PERSIONAL_DETAIL = "/persionaldetail/PersionalDetailActivity";

    /**
     * 专题列表
     */
    public static final String TOPIC_LIST = "/detail/SpecialListActivity";

    /**
     * 反馈
     */
    public static final String FEED_BACK = "/feedback";

    /*登录*/
    /**
     * 登录页
     */
    public static final String LOGIN_ACTIVITY = "/login/LoginActivity";


    /**
     * 浙报通行证登录
     */
    public static final String ZB_LOGIN = "/login/ZBLoginActivity";

    /**
     * 浙报通行证注册
     */
    public static final String ZB_REGISTER = "/login/ZBRegisterActivity";

    /**
     * 重置密码
     */
    public static final String ZB_RESET_PASSWORD = "/login/ZBResetNewPassWord";

    /**
     * 短信验证登录
     */
    public static final String ZB_SMS_LOGIN = "/login/ZBResetPWSmsLogin";

    /**
     * 验证码确认
     */
    public static final String ZB_VERIFICAITION = "/login/ZBVerificationActivity";

    /**
     * 实名制验证
     */
    public static final String ZB_MOBILE_VERIFICATION = "/login/ZBMobileValidateActivity";
}
