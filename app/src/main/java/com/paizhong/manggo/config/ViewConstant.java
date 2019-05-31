package com.paizhong.manggo.config;


import java.util.ArrayList;
import java.util.List;

/**
 * view 相关 常量
 * Created by zab on 2018/4/25 0025.
 */

public class ViewConstant {
    //通用tab
    public static final int SIMP_TAB_ITEM_ZERO = 0;
    public static final int SIMP_TAB_ITEM_ONE = 1;
    public static final int SIMP_TAB_ITEM_TWO = 2;
    public static final int SIMP_TAB_ITEM_THR = 3;
    public static final int SIMP_TAB_ITEM_FOU = 4;

    //代金券 tab
    public static final String VOUCHER_TAB_KY = "KY";
    public static final String VOUCHER_TAB_GQ = "GQ";

    //---------------K-线页面-tab-Type------------------------
    public static final String KLINE_TAB_0 = "0"; //分时线
    public static final String KLINE_TAB_1 = "1"; //1分时线
    public static final String KLINE_TAB_S = "S"; //闪电图
    public static final String KLINE_TAB_5 = "5"; //5分线
    public static final String KLINE_TAB_15 = "15"; //15分线
    public static final String KLINE_TAB_30 = "30"; //30分线
    public static final String KLINE_TAB_1H = "1H"; //1小时
    public static final String KLINE_TAB_2H = "2H"; //2小时
    public static final String KLINE_TAB_4H = "4H"; //4小时
    public static final String KLINE_TAB_1D = "1D"; //日线
    public static final String KLINE_TAB_WEEK = "WEEK"; //周线


    //------------------爱淘商城-productID------------------------------
    public static final String PRODUCT_ID_AG = "11";//GL银
    public static final String PRODUCT_ID_CU = "10";//GL铜
    public static final String PRODUCT_ID_NL = "9";//NI镍
    public static final String PRODUCT_ID_ZS = "12";//大豆
    public static final String PRODUCT_ID_ZW = "13";//小麦
    public static final String PRODUCT_ID_ZC = "14";//玉米

    //------------------banner-type------------------------------
    public static final String HOME_BANNER = "1";//首页Banner
    public static final String HOME_ACTIVITY = "2";//首页活动推广
    public static final String HOME_BANNER_BOTTOM = "3";//首页banner
    public static final String FOLLOW_BANNER = "9";//跟单Banner


    /**
     * 屏蔽渠道列表
     * @return
     */
    public static List<String> getAuditMarket(){
        List<String> markets = new ArrayList<>();
        markets.add("huawei");
        markets.add("xiaomi");
        markets.add("uc");
        return markets;
    }


    /**
     * 屏蔽时是否屏蔽user页面 所包含的渠道
     * @return
     */
    public static List<String> getAuditUserMarket(){
        List<String> markets = new ArrayList<>();
        markets.add("xiaomi");
        return markets;
    }

    /**
     * 屏蔽版本
     * @return
     */
    public static String getAuditVersion(){
        return "3.2.0";
    }
}
