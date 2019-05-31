package com.paizhong.manggo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zab on 2018/3/26 0026.
 */

public class Constant {

    //    -------------------服务器url--------------------
    public static final String baseUrl = "http://192.168.1.239/";//测试
//    public static final String baseUrl = "http://muat.313bcmj.com/";//预生产
//    public static final String baseUrl = "https://m.313bcmj.com/";//生产

    //    --------突发行情、大事件、财经日历url集合--------------
    public static final List<String> OTHER_HOSTS = new ArrayList<>();

    public static final String UM_KEY = "5b975584f29d9866f30001c3";
    //app 用户保存数据相关key
    public static final String USER_KEY_TOKEN = "user_key_token";//用户token
    public static final String USER_KEY_PHONE = "user_key_phone";//用户手机号
    public static final String USER_KEY_USERID = "user_key_userid";//用户id
    public static final String USER_KEY_NICKNAME = "user_key_nickname";//用户名称
    public static final String USER_KEY_USERPIC = "user_key_userPic";//用户头像
    public static final String USER_KEY_SPLASH = "user_key_splash";//欢迎页 url
    public static final String USER_KEY_SPLASH_URL = "user_key_splash_url"; //欢迎页 url
    public static final String USER_KEY_SPLASH_HEAD = "user_key_splash_head"; //欢迎页 头部
    public static final String USER_KEY_SPLASH_NAME = "user_key_splash_name"; //欢迎页 名字
    public static final String USER_KEY_SPLASH_SERVERSION = "user_key_splash_serversion"; //欢迎页 服务器版本


    //爱淘相关
    public static final String USER_KEY_AT_REGISTER = "user_key_at_register";//标记该用户是否在爱淘注册过
    public static final String USER_KEY_AT_TOKEN = "user_key_at_token"; //爱淘 token
    public static final String USER_KEY_AT_ACCESS_KEY = "user_key_at_access_key"; //爱淘 access_key
    public static final String USER_KEY_AT_USERID = "user_key_at_userid"; //爱淘 userid
    public static final String USER_KEY_LOGIN_TIME = "user_key_login_time";//记录登录时间


    public static final String CACHE_IS_GUIDED = "cache_is_guided";//标记新手引导
    public static final String CACHE_IS_NEW_USER = "cache_is_new_user";//标记是否是新手
    public static final String CACHE_IS_FIRST_GUIDED = "cache_is_first_guided";//标记新手首次引导
    public static final String CACHE_IS_YE_STYLE = "cache_is_ye_style";//标记夜间模式
    public static final String CACHE_IS_CHAT_MSG = "cache_is_chat_msg"; //弹幕新手引导
    //public static final String CACHE_FOLLOW_POD_CLOSED = "cache_follow_pod_closed";//下单页面是否默认开启跟买提示
    //public static final String CACHE_FOLLOW_CLOSED = "cache_follow_closed";//下单页面是否默认开启跟买

    public static final int ZJ_PRICE_COMPANY = 100;


    //-------------------h5链接--------------------
    public static final String H5_RECHARGE_COMPLETED = baseUrl + "client/rechargeCompleted";//充值完成
    public static final String H5_SHOP_MALL = baseUrl + "integral";//积分商城
    public static final String H5_NEWSCHOOL = baseUrl + "homePage";//新手教程
    public static final String H5_SIGN_GZ = baseUrl + "transRule";//签到规则
    public static final String H5_ABOUTUS = baseUrl + "aboutUsIndex";//关于我们
    public static final String H5_AITAO_RULE = baseUrl + "aitaoRule";//爱淘商城规则
    public static final String H5_USER_PROTOCOL = baseUrl + "userProtocol";//用户协议
    public static final String H5_RISK_WARNING = baseUrl + "riskWarning";//风险告知
    public static final String H5_BUY_RULE = baseUrl + "buyRule";//跟买规则
    public static final String H5_RANK_DESC = baseUrl + "rankDesc";//牛人排行榜说明
    public static final String MAKE_MONEY = baseUrl + "banner/banner03";//新手赚钱攻略

}
