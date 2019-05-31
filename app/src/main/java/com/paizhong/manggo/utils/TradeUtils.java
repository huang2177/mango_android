package com.paizhong.manggo.utils;

import android.text.TextUtils;

import com.paizhong.manggo.config.ViewConstant;

/**
 * 交易相关
 * Created by zab on 2018/5/4 0004.
 */

public class TradeUtils {


    /**
     * 获取每手对应波动一个点位的价格
     * @param productID
     * @param selectPrice
     * @return
     */
    public static double getProductPrice(String productID,int selectPrice){
        double dPrice =0;
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,productID)){//GL银
            if (selectPrice == 8){
                dPrice = 0.1;
            }else if (selectPrice == 100){
                dPrice = 1;
            }else {
                dPrice = 5;
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,productID)){//GL铜
            if (selectPrice == 8){
                dPrice = 0.02;
            }else if (selectPrice == 100){
                dPrice = 0.2;
            }else if (selectPrice == 120){
                dPrice = 0.48;
            }else if (selectPrice == 400){
                dPrice = 1.6;
            }else {
                dPrice = 1;
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,productID)){ //GL镍
            if (selectPrice == 8){
                dPrice = 0.01;
            }else if (selectPrice == 100){
                dPrice = 0.08;
            }else if (selectPrice == 120){
                dPrice = 0.15;
            }else if (selectPrice == 400){
                dPrice = 0.5;
            }else {
                dPrice = 0.4;
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,productID)) { //GL大豆
             if (selectPrice == 8){
                 dPrice = 0.2;
             }else if (selectPrice == 200){
                 dPrice = 5;
             }else {
                 dPrice = 10;
             }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,productID)){ // GL小麦
             if (selectPrice == 8){
                 dPrice = 0.2;
             }else if (selectPrice == 200){
                 dPrice = 5;
             }else {
                 dPrice = 10;
             }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,productID)){ //GL玉米
            if (selectPrice == 8){
                dPrice = 0.2;
            }else if (selectPrice == 200){
                dPrice = 5;
            }else {
                dPrice = 10;
            }
        }
        return dPrice;
    }



    public static String getProductMsg(String productID,int selectPrice){
        String dPrice = "";
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,productID)){//GL银
            if (selectPrice == 8){
                dPrice = "0.1千克";
            }else if (selectPrice == 100){
                dPrice = "1千克";
            }else {
                dPrice = "5千克";
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,productID)){//GL铜
            if (selectPrice == 8){
                dPrice = "0.02吨";
            }else if (selectPrice == 100){
                dPrice = "0.2吨";
            }else if (selectPrice == 120){
                dPrice = "0.48吨";
            }else if (selectPrice == 400){
                dPrice = "1.6吨";
            }else {
                dPrice = "1吨";
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,productID)){ //GL镍
            if (selectPrice == 8){
                dPrice = "0.01吨";
            }else if (selectPrice == 100){
                dPrice = "0.08吨";
            }else if (selectPrice == 120){
                dPrice = "0.15吨";
            }else if (selectPrice == 400){
                dPrice = "0.5吨";
            }else {
                dPrice = "0.4吨";
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,productID)) { //GL大豆
            if (selectPrice == 8){
                dPrice = "0.2吨";
            }else if (selectPrice == 200){
                dPrice = "5吨";
            }else {
                dPrice = "10吨";
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,productID)){ // GL小麦
            if (selectPrice == 8){
                dPrice = "0.2吨";
            }else if (selectPrice == 200){
                dPrice = "5吨";
            }else {
                dPrice = "10吨";
            }
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,productID)){ //GL玉米
            if (selectPrice == 8){
                dPrice = "0.2吨";
            }else if (selectPrice == 200){
                dPrice = "5吨";
            }else {
                dPrice = "10吨";
            }
        }
        return dPrice;
    }


    /**
     * 获取跟单金额序号
     *
     * @return 目前 1代表8元  2 代表 100元 3 代表500  2 代表200 3代表400
     */
    public static int getNum(int amount ,int quantity) {
        int productAmount = amount /quantity;
        if (productAmount == 100) {
            return 2;
        } else if (productAmount == 120){
            return 2;
        }else if (productAmount == 200) {
            return 2;
        }else if (productAmount == 500) {
            return 3;
        }else if (productAmount == 400) {
            return 3;
        } else {
            return 1;
        }
    }
}
