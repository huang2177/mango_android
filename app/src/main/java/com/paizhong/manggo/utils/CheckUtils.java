package com.paizhong.manggo.utils;

import android.text.TextUtils;

import com.paizhong.manggo.config.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本校验工具类
 */

public class CheckUtils {

    /**
     * 校验手机号码是否匹配
     *
     * @param num 手机号码
     * @return true  成功
     */
    public static boolean checkPhoneNum(String num) {
        String pattern = "^1(3|4|5|6|7|8|9)\\d{9}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(num);

        return m.matches();
    }


//    public static boolean checkName(String name){
//       String pattern = "[\\u4e00-\\u9fa5_·]";
//        Pattern r = Pattern.compile(pattern);
//        Matcher m = r.matcher(name);
//        return m.matches();
//    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (TextUtils.isEmpty(cardId)) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
              //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean isBackPicError(String text) {
        return (TextUtils.isEmpty(text) || text.contains("?") || text.contains("？"));
    }


    /**
     * 判断是不是 double
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        return Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$").matcher(str).matches();
    }

    /**
     * find 配备 某个    matches匹配整个
     * 判断是不是 int
     * @param str
     * @return
     */
    public static boolean isInt(String str){
        return  Pattern.compile("^-?[1-9]\\d*$").matcher(str).matches();
    }


    public static boolean matcherImage(String path){
        if (TextUtils.isEmpty(path)){
            return false;
        }
        String fileType = "";
        int typeIndex = path.lastIndexOf(".");
        if (typeIndex != -1) {
            fileType  = path.substring(typeIndex + 1)
                    .toLowerCase();
        }
        //图片格式
        String regStr = "[Gg][Ii][Ff]|[Jj][Pp][Gg]|[Bb][Mm][Pp]|[Jj][Pp][Ee][Gg]|[Pp][Nn][Gg]|[Ww][Ee][Pp]";
        //把正则 匹配的格式放入 类中
        Pattern pattern = Pattern.compile(regStr);
        //把需要匹配的，放入
        Matcher matcher = pattern.matcher(fileType);
        //匹配结果，符合为true 反之false
        return matcher.find();
    }

    public static boolean atCheckTime(){

        long loginTime = SpUtil.getLong(Constant.USER_KEY_LOGIN_TIME);

        if(loginTime > 0 && 60*1000*60*3 < System.currentTimeMillis() - loginTime){
            return true;
        }else {
            return false;
        }
    }
}
