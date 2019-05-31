package com.paizhong.manggo.ui.kchart.utils;

import android.text.TextUtils;

import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.config.ViewConstant;
import com.paizhong.manggo.ui.kchart.bean.KLineEntity;
import com.paizhong.manggo.ui.kchart.bean.KlineCycle;
import com.paizhong.manggo.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zab on 2018/8/17 0017.
 */
public class KLineDataUtils {

    public static List<KlineCycle> getKlineCycleList(boolean atMarket,String code){
        List<KlineCycle> klineCycleList = new ArrayList<KlineCycle>();
        if (!atMarket){
            klineCycleList.add(new KlineCycle("分时", "0",ViewConstant.KLINE_TAB_0));
            klineCycleList.add(new KlineCycle("1分钟", "1",ViewConstant.KLINE_TAB_1));
            klineCycleList.add(new KlineCycle("5分钟", "2",ViewConstant.KLINE_TAB_5));
            klineCycleList.add(new KlineCycle("15分钟", "3",ViewConstant.KLINE_TAB_15));
            klineCycleList.add(new KlineCycle("30分钟", "4",ViewConstant.KLINE_TAB_30));
            klineCycleList.add(new KlineCycle("1小时", "5",ViewConstant.KLINE_TAB_1H));
            klineCycleList.add(new KlineCycle("4小时", "6",ViewConstant.KLINE_TAB_4H));
            klineCycleList.add(new KlineCycle("日线", "7",ViewConstant.KLINE_TAB_1D));
            //klineCycleList.add(new KlineCycle("周线", "8",ViewConstant.KLINE_TAB_WEEK));
        }else {
            klineCycleList.add(new KlineCycle("分时", "0",ViewConstant.KLINE_TAB_0));
            klineCycleList.add(new KlineCycle("5分钟", "5",ViewConstant.KLINE_TAB_5));
            klineCycleList.add(new KlineCycle("15分钟", "15",ViewConstant.KLINE_TAB_15));
            klineCycleList.add(new KlineCycle("30分钟", "30",ViewConstant.KLINE_TAB_30));
            klineCycleList.add(new KlineCycle("1小时", "60",ViewConstant.KLINE_TAB_1H));
            klineCycleList.add(new KlineCycle("2小时", "120",ViewConstant.KLINE_TAB_2H));
            klineCycleList.add(new KlineCycle("日线", "DAY",ViewConstant.KLINE_TAB_1D));
        }
        return klineCycleList;
    }



    public static List<KLineEntity> parseKLine(boolean mIsCKZS, KLineBean kLine) {
        List<KLineEntity> kLineEntities = new ArrayList<>();
        if (kLine.dataList !=null && kLine.dataList.size() > 0) {
            int count = kLine.dataList.size();

            for (int i = 0; i < count; i++) {
                KLineEntity kLineData = new KLineEntity();
                kLineData.dateStr = kLine.dataList.get(i).get(0);
                kLineData.open = Float.parseFloat(kLine.dataList.get(i).get(1));
                kLineData.close = Float.parseFloat(kLine.dataList.get(i).get(2));
                kLineData.high = Float.parseFloat(kLine.dataList.get(i).get(3));
                kLineData.low = Float.parseFloat(kLine.dataList.get(i).get(4));
                if (mIsCKZS){
                    kLineData.priceNum = NumberUtil.getFloatStr4(Double.parseDouble(kLine.dataList.get(i).get(5)));
                }else {
                    kLineData.priceNum = kLine.dataList.get(i).get(5);
                }
                kLineData.percentage = kLine.dataList.get(i).get(6);
                kLineEntities.add(kLineData);
            }
        }
        DataHelper.calculate(kLineEntities);
        return kLineEntities;
    }

    public static List<KLineEntity> parseKLine(String mTimeCode,boolean mIsCKZS, KLineBean kLine ,float stockIndex) {
        List<KLineEntity> kLineEntities = new ArrayList<>();
        if (kLine.dataList !=null && kLine.dataList.size() > 0) {
            int count = kLine.dataList.size();
            for (int i = 0; i < count; i++) {
                KLineEntity kLineData = new KLineEntity();
                kLineData.dateStr = kLine.dataList.get(i).get(0);
                kLineData.open = Float.parseFloat(kLine.dataList.get(i).get(1));
                kLineData.close = Float.parseFloat(kLine.dataList.get(i).get(2));
                kLineData.high = Float.parseFloat(kLine.dataList.get(i).get(3));
                kLineData.low = Float.parseFloat(kLine.dataList.get(i).get(4));
                if (mIsCKZS){
                    kLineData.priceNum = NumberUtil.getFloatStr4(Double.parseDouble(kLine.dataList.get(i).get(5)));
                }else {
                    kLineData.priceNum = kLine.dataList.get(i).get(5);
                }
                kLineData.percentage = kLine.dataList.get(i).get(6);
                kLineEntities.add(kLineData);
            }
            if (!mIsCKZS
                    && stockIndex > 0
                    && kLineEntities.size() > 0
                    && !TextUtils.equals(ViewConstant.KLINE_TAB_1D, mTimeCode)
                    && !TextUtils.equals(ViewConstant.KLINE_TAB_WEEK, mTimeCode)){
                KLineEntity kLineEntityLas = kLineEntities.get(kLineEntities.size() - 1);
                kLineEntityLas.close = stockIndex;
            }
        }
        DataHelper.calculate(kLineEntities);
        return kLineEntities;
    }



    public static String  getProductCode(String productID){
        return getProductCode(0,productID);
    }

    /**
     *
     * @param type 0 爱淘行情   1-黄金 2-农产品 3-贵金属 4-外汇
     * @param productID
     * @return
     */
    public static String  getProductCode(int type,String productID){
        if (type == 0){
            if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,productID)){//银
                return "AG";
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,productID)){//铜
                return "CU";
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,productID)){//镍
                return "NL";
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,productID)){//大豆
                return "SOYBEAN";
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,productID)){//小麦
                return "WHEAT";
            }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,productID)){//玉米
                return "MAIZE";
            }
        }else if (type == 1){ //黄金
            if (TextUtils.equals("SGPMAG",productID)){//现货白银
                return "AG";
            }else if (TextUtils.equals("SGPMAP",productID)){//现货白金
                return "AP";
            }else if (TextUtils.equals("SGPMAU",productID)){//现货黄金
                return "AU";
            }else if (TextUtils.equals("SGPMGT",productID)){//香港黄金
                return "GT";
            }else if (TextUtils.equals("SGPMPD",productID)){//现货钯金
                return "PD";
            }else if (TextUtils.equals("SGPMUDI",productID)){//美元指数
                return "DI";
            }else if (TextUtils.equals("SGPMUSDCNY",productID)){//美元人民币
                return "NY";
            }else if (TextUtils.equals("SGPMTWGD",productID)){//台两黄金
                return "GD";
            }
        }else if (type == 2){//农产品
            if (TextUtils.equals("COZCZ0",productID)){//美玉米12
                return "ZCZ0";
            }else if (TextUtils.equals("COZLZ0",productID)){//美豆油12
                return "ZLZ0";
            }else if (TextUtils.equals("COZMZ0",productID)){//美豆粕12
                return "ZMZ0";
            }else if (TextUtils.equals("COZRX0",productID)){//美稻米11
                return "ZRX0";
            }else if (TextUtils.equals("COZSX0",productID)){//美大豆11
                return "ZSX0";
            }else if (TextUtils.equals("COZWZ0",productID)){//美小麦12
                return "ZWZ0";
            }
        }else if (type == 3){//贵金属
            if (TextUtils.equals("LEAHD3M",productID)){//LME铝03(电子)
                return "AHD3M";
            }else if (TextUtils.equals("LECAD3M",productID)){//LME铜03(电子)
                return "CAD3M";
            }else if (TextUtils.equals("LECOD3M",productID)){//LME钴03(电子)
                return "COD3M";
            }else if (TextUtils.equals("LENID3M",productID)){//LME镍03(电子)
                return "NID3M";
            }else if (TextUtils.equals("LEPBD3M",productID)){//LME铅03(电子)
                return "PBD3M";
            }else if (TextUtils.equals("LESND3M",productID)){//LME锡03(电子)
                return "SND3M";
            }else if (TextUtils.equals("LEZSD3M",productID)){//LME锌03(电子)
                return "ZSD3M";
            }
        }else if (type == 4){//外汇
            if (TextUtils.equals("FXAUDUSD",productID)){//澳元美元
                return "AUDUSD";
            }else if (TextUtils.equals("FXEURAUD",productID)){//欧元澳元
                return "EURAUD";
            }else if (TextUtils.equals("FXEURCAD",productID)){//欧元加元
                return "EURCAD";
            }else if (TextUtils.equals("FXEURGBP",productID)){//欧元英镑
                return "EURGBP";
            }else if (TextUtils.equals("FXEURJPY",productID)){//欧元日元
                return "EURJPY";
            }else if (TextUtils.equals("FXEURUSD",productID)){//欧元美元
                return "EURUSD";
            }else if (TextUtils.equals("FXGBPJPY",productID)){//英镑日元
                return "GBPJPY";
            }else if (TextUtils.equals("FXGBPUSD",productID)){//英镑美元
                return "GBPUSD";
            }else if (TextUtils.equals("FXUSDCAD",productID)){//美元加元
                return "USDCAD";
            }else if (TextUtils.equals("FXUSDCNH",productID)){//离岸人民币
                return "USDCNH";
            }else if (TextUtils.equals("FXUSDHKD",productID)){//美元港币
                return "USDHKD";
            }else if (TextUtils.equals("FXUSDJPY",productID)){//美元日元
                return "USDJPY";
            }else if (TextUtils.equals("FXXAGUSD",productID)){//白银美元
                return "XAGUSD";
            }else if (TextUtils.equals("FXXAUUSD",productID)){//黄金美元
                return "XAUUSD";
            }
        }
        return "";
    }




    public static String getProductName(String productID){
        if (TextUtils.isEmpty(productID)){
            return "";
        }
        if (TextUtils.equals(ViewConstant.PRODUCT_ID_AG,productID)){//GL银
            return  "GL银";
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_CU,productID)){//GL铜
            return "GL铜";
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_NL,productID)) {//镍
            return "GL镍";
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZS,productID)){
            return "GL大豆";
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZW,productID)){
            return "GL小麦";
        }else if (TextUtils.equals(ViewConstant.PRODUCT_ID_ZC,productID)){
            return "GL玉米";
        }
        return "";
    }
}
