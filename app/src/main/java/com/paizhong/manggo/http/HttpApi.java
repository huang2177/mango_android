package com.paizhong.manggo.http;

import com.google.gson.JsonNull;
import com.paizhong.manggo.bean.at.KLineBean;
import com.paizhong.manggo.bean.base.BaseResponse;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.bean.follow.FollowRecordBean;
import com.paizhong.manggo.bean.follow.MyFollowerBean;
import com.paizhong.manggo.bean.follow.NoticeListBean;
import com.paizhong.manggo.bean.follow.PersonInfoBean;
import com.paizhong.manggo.bean.follow.PersonOrderBean;
import com.paizhong.manggo.bean.follow.RankListBean;
import com.paizhong.manggo.bean.follow.RecordRankBean;
import com.paizhong.manggo.bean.follow.WeekCountBean;
import com.paizhong.manggo.bean.home.CalendarBean;
import com.paizhong.manggo.bean.home.CalendarDetailBean;
import com.paizhong.manggo.bean.home.EmergencyBean;
import com.paizhong.manggo.bean.home.EmergencyDetailBean;
import com.paizhong.manggo.bean.home.HotProBean;
import com.paizhong.manggo.bean.home.NewBuyInfoBean;
import com.paizhong.manggo.bean.home.ProfitBean;
import com.paizhong.manggo.bean.home.RedPacketBean;
import com.paizhong.manggo.bean.home.ReportBean;
import com.paizhong.manggo.bean.home.TradeChanceBean;
import com.paizhong.manggo.bean.home.TradeDetailBean;
import com.paizhong.manggo.bean.market.MarketHQBean;
import com.paizhong.manggo.bean.other.BannerBean;
import com.paizhong.manggo.bean.other.ChatMsgBean;
import com.paizhong.manggo.bean.other.HeadImageBean;
import com.paizhong.manggo.bean.other.SignBean;
import com.paizhong.manggo.bean.other.SigninListBean;
import com.paizhong.manggo.bean.other.UpdateAppBean;
import com.paizhong.manggo.bean.trade.AtBaseBean;
import com.paizhong.manggo.bean.trade.CashPlaceOrderBean;
import com.paizhong.manggo.bean.trade.HangUpListBean;
import com.paizhong.manggo.bean.trade.PositionListBean;
import com.paizhong.manggo.bean.trade.ProductListBean;
import com.paizhong.manggo.bean.trade.ProductTimeLimitBean;
import com.paizhong.manggo.bean.user.IntegralBean;
import com.paizhong.manggo.bean.user.LoginUserBean;
import com.paizhong.manggo.bean.zj.BankCardZjBean;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.bean.zj.BankUnbCardZJBean;
import com.paizhong.manggo.bean.zj.CapitalBean;
import com.paizhong.manggo.bean.zj.CapitalJsonBean;
import com.paizhong.manggo.bean.zj.PayModeZJBean;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.bean.zj.RechargeConfigZJBean;
import com.paizhong.manggo.bean.zj.RechargeMoneyZJBean;
import com.paizhong.manggo.bean.zj.UserPositionBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.bean.zj.UserZJCheckBean;
import com.paizhong.manggo.bean.zj.UserZJRegisterBean;
import com.paizhong.manggo.bean.zj.UserZJSmsBean;
import com.paizhong.manggo.bean.zj.UserZJSmsCaptchaBean;
import com.paizhong.manggo.bean.zj.VoucherGqZJBean;
import com.paizhong.manggo.bean.zj.VoucherKyZJBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zab on 2018/3/26 0026.
 */

public interface HttpApi {

    /*---------------首页------------*/
    // banner轮播
    @POST("mango-app-api/app/banner/selectBanner")
    Observable<BaseResponse<List<BannerBean>>> selectBanner(@Query("bannerType") String bannerType);

    // 获取红包雨相关信息
    @POST("mango-app-api/redPacket/queryRedPacketActivity")
    Observable<BaseResponse<RedPacketBean>> queryRedPacketInfo();


    // 盈利播报
    @POST("mango-app-api/app/profit/getProfitabilityReporting")
    Observable<BaseResponse<List<ReportBean>>> getReporting();

    // 交易机会
    @POST("mango-app-api/app/banner/selectTradeOpptunityList")
    Observable<BaseResponse<List<TradeChanceBean>>> selectTradeChance(@Query("pageIndex") int pageIndex);

    // 盈利广场
    @POST("mango-app-api/app/ranking/selectRanking")
    Observable<BaseResponse<List<ProfitBean>>> selectRanking(@Query("page") int page);

    // 财经日历
    @GET("https://rili.jin10.com/datas/{yyy}/{md}/economics.json")
    Observable<List<CalendarBean>> getCalendarList(@Path("yyy") String yyy
            , @Path("md") String md
            , @Query("noEncryptDES") boolean noEncryptDES);


    @GET("https://rili.jin10.com/datas/jiedu/{id}.json")
    Observable<CalendarDetailBean> getNewsDetail(@Path("id") String id
            , @Query("noEncryptDES") boolean noEncryptDES);


    // 突发事件
    @GET("mango-app-api/app/shieldAppMarket/{method}")
    Observable<BaseResponse<List<EmergencyBean>>> getEmergency(@Path("method") String method
            , @Query("clientType") String clientType
            , @Query("noEncryptDES") boolean noEncryptDES);

    // 突发事件详情
    @GET()
    Observable<EmergencyDetailBean> getEmergencyDetail(@Url String url
            , @Query("noEncryptDES") boolean noEncryptDES);

    //首页 交易详情
    @POST("mango-app-api/app/banner/selectTradeOpptunityListById")
    Observable<BaseResponse<TradeDetailBean>> getTradeDetail(@Query("tradeOpportunityId") String tradeOpportunityId);

    //首页 最新跟买
    @POST("mango-fellowOrder-api/app/rank/lastestFellow")
    Observable<BaseResponse<List<NewBuyInfoBean>>> getLastFellowInfo();


    //检查是否使用过代金券
    @POST("mango-mall-api/app/sign/getInitTicket")
    Observable<BaseResponse<Boolean>> getInitTicket(@Query("phone") String phone);

    //版本更新
    @POST("mango-app-api/app/version/selectAll")
    Observable<BaseResponse<UpdateAppBean>> updateApp();


    //获取新手券id
    @POST("mango-trader-api/user/getDialog")
    Observable<BaseResponse<UserZJBean>> getNewUserTicket(@Query("token") String token);


    //签到mango-mall-api/app/sign/saveSign
    @POST("mango-mall-api/app/sign/addSign")
    Observable<BaseResponse<SignBean>> saveSign();

    //签到列表 mango-mall-api/app/sign/selectParam
    @POST("mango-mall-api/app/sign/selectSignDate")
    Observable<BaseResponse<SigninListBean>> getSigninList();


    //是否已经签到
    @POST("mango-mall-api/app/sign/getSign")
    Observable<BaseResponse<Integer>> getSign();

    //获取最热产品
    @GET("mango-trader-api/product/getOrderHot")
    Observable<BaseResponse<HotProBean>> getHotProduct();

    //判断是否抽过奖接口(大转盘)
    @POST("mango-activity-api/app/luckActivity/validIsUpPop")
    Observable<BaseResponse<Boolean>> validIsUpPop();

    //幸运抽奖
    @POST("mango-activity-api/app/luckActivity/drawPrize")
    Observable<BaseResponse<Integer>> drawPrize(@Query("userId") String userId);

    //-------------k线相关-----------
    //k 线  (参考数据)
    @GET("mango-kline-api/kline/getOtherKline")
    Observable<BaseResponse<KLineBean>> getCKSJKlineQuotation(@Query("type") int type,
                                                              @Query("code") String code,
                                                              @Query("line") String line,
                                                              @Query("noEncryptDES") boolean noEncryptDES);


    //分时图 (参考数据)
    @GET("mango-timeLine-api/time/getOtherTime")
    Observable<BaseResponse<KLineBean>> getCKSJQuotation(@Query("type") int type,
                                                         @Query("code") String code,
                                                         @Query("noEncryptDES") boolean noEncryptDES);

    //分时图(银)
    @GET("mango-timeLine-api/time/getAGTime")
    Observable<BaseResponse<KLineBean>> getAGTime(@Query("noEncryptDES") boolean noEncryptDES);

    //分时图(铜)
    @GET("mango-timeLine-api/time/getCUTime")
    Observable<BaseResponse<KLineBean>> getCUTime(@Query("noEncryptDES") boolean noEncryptDES);

    //分时图(镍)
    @GET("mango-timeLine-api/time/getNLTime")
    Observable<BaseResponse<KLineBean>> getNLTime(@Query("noEncryptDES") boolean noEncryptDES);

    //分时图(大豆)
    @GET("mango-timeLine-api/time/getZSTime")
    Observable<BaseResponse<KLineBean>> getZSTime(@Query("noEncryptDES") boolean noEncryptDES);

    //分时图(小麦)
    @GET("mango-timeLine-api/time/getZWTime")
    Observable<BaseResponse<KLineBean>> getZWTime(@Query("noEncryptDES") boolean noEncryptDES);

    //分时图(玉米)
    @GET("mango-timeLine-api/time/getZCTime")
    Observable<BaseResponse<KLineBean>> getZCTime(@Query("noEncryptDES") boolean noEncryptDES);


    //k 线(银)
    @GET("mango-kline-api/kline/getAGKline")
    Observable<BaseResponse<KLineBean>> getAGKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);

    //k 线(铜)
    @GET("mango-kline-api/kline/getCUKline")
    Observable<BaseResponse<KLineBean>> getCUKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);

    //k 线(镍)
    @GET("mango-kline-api/kline/getNLKline")
    Observable<BaseResponse<KLineBean>> getNLKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);

    //k 线(大豆)
    @GET("mango-kline-api/kline/getZSKline")
    Observable<BaseResponse<KLineBean>> getZSKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);

    //k 线(小麦)
    @GET("mango-kline-api/kline/getZWKline")
    Observable<BaseResponse<KLineBean>> getZWKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);

    //k 线(玉米)
    @GET("mango-kline-api/kline/getZCKline")
    Observable<BaseResponse<KLineBean>> getZCKline(@Query("type") String type, @Query("noEncryptDES") boolean noEncryptDES);


    //------交易相关------

    //单个产品信息
    @POST("mango-trader-api/product/getSingleProduct")
    Observable<BaseResponse<List<ProductListBean>>> getSingleProduct(@Query("id") String id);


    //全部产品信息
    @GET("mango-trader-api/product/getProductList")
    Observable<BaseResponse<List<ProductListBean>>> getProductList(@Query("noEncryptDES") boolean noEncryptDES);


    //现金建仓
    @POST("mango-trader-api/trans/getCash")
    Observable<BaseResponse<CashPlaceOrderBean>> getCashPlaceOrder(@Query("token") String token,
                                                                   @Query("id") String id,
                                                                   @Query("secret_access_key") String secret_access_key,
                                                                   @Query("productId") String productId,
                                                                   @Query("quantity") int quantity,
                                                                   @Query("flag") int flag,
                                                                   @Query("profitLimit") int profitLimit,
                                                                   @Query("lossLimit") int lossLimit,
                                                                   @Query("amount") int amount,
                                                                   @Query("holdNight") int holdNight,
                                                                   @Query("productName") String productName,
                                                                   @Query("fee") double fee,
                                                                   @Query("code") String code,
                                                                   @Query("followType") int followType,
                                                                   @Query("nickName") String nickName);


    //体验券建仓
    @POST("mango-trader-api/trans/getVoucher")
    Observable<BaseResponse<CashPlaceOrderBean>> getVoucherPlaceOrder(@Query("token") String token,
                                                                      @Query("id") String id,
                                                                      @Query("secret_access_key") String secret_access_key,
                                                                      @Query("productId") String productId,
                                                                      @Query("quantity") int quantity,
                                                                      @Query("flag") int flag,
                                                                      @Query("profitLimit") int profitLimit,
                                                                      @Query("lossLimit") int lossLimit,
                                                                      @Query("couponId") String couponId,
                                                                      @Query("amount") int amount,
                                                                      @Query("productName") String productName,
                                                                      @Query("fee") double fee,
                                                                      @Query("code") String code);


    //跟买现金建仓
    @POST("mango-trader-api/trans/getFollowCash")
    Observable<BaseResponse<CashPlaceOrderBean>> getCashFollow(@Query("token") String token,
                                                               @Query("id") String id,
                                                               @Query("secret_access_key") String secret_access_key,
                                                               @Query("productId") String productId,
                                                               @Query("quantity") int quantity,
                                                               @Query("flag") int flag,
                                                               @Query("profitLimit") int profitLimit,
                                                               @Query("lossLimit") int lossLimit,
                                                               @Query("amount") int amount,
                                                               @Query("holdNight") int holdNight,
                                                               @Query("productName") String productName,
                                                               @Query("fee") double fee,
                                                               @Query("code") String code,
                                                               @Query("orderNo") String orderNo,
                                                               @Query("fellowerPhone") String fellowerPhone,
                                                               @Query("followType") int followType);


    //挂单下单
    @POST("mango-register-api/register/getRegisterOrder")
    Observable<BaseResponse<JsonNull>> getHangUpOrder(@Query("token") String token,
                                                      @Query("id") String id,
                                                      @Query("productId") String productId,
                                                      @Query("quantity") int quantity,
                                                      @Query("flag") int flag,
                                                      @Query("profitLimit") int profitLimit,
                                                      @Query("lossLimit") int lossLimit,
                                                      @Query("amount") int amount,
                                                      @Query("holdNight") int holdNight,
                                                      @Query("productName") String productName,
                                                      @Query("fee") double fee,
                                                      @Query("code") String code,
                                                      @Query("nickName") String nickName,
                                                      @Query("floatNum") String floatNum,
                                                      @Query("registerAmount") String registerAmount);


    //修改挂单
    @POST("mango-register-api/goingRegisterOrder/modifyGoingRegisterOrder")
    Observable<BaseResponse<CashPlaceOrderBean>> getUpdateHangUp(@Query("token") String token,
                                                                 @Query("id") String hangUpID,
                                                                 @Query("registerAmount") String registerAmount,
                                                                 @Query("profitLimit") int profitLimit,
                                                                 @Query("lossLimit") int lossLimit,
                                                                 @Query("floatNum") String floatNum,
                                                                 @Query("quantity") int quantity,
                                                                 @Query("fee") double fee,
                                                                 @Query("amount") int amount,
                                                                 @Query("flag") int flag,
                                                                 @Query("holdNight") int holdNight,
                                                                 @Query("productId") String product_id);


    //持仓列表
    @GET("mango-order-api/order/getOrderList")
    Observable<BaseResponse<List<PositionListBean>>> getPositionList(@Query("token") String token, @Query("noEncryptDES") boolean noEncryptDES);


    //单个产品持仓列表
    @GET("mango-order-api/order/getSignOrderList")
    Observable<BaseResponse<List<PositionListBean>>> getSignOrderList(@Query("token") String token,
                                                                      @Query("code") String code,
                                                                      @Query("noEncryptDES") boolean noEncryptDES);


    //快速平仓
    @POST("mango-trader-api/trans/getTransferList")
    Observable<BaseResponse<ZJBaseBean>> getTransferList(@Query("token") String token,
                                                         @Query("orderids") String orderids,
                                                         @Query("secret_access_key") String secret_access_key);

    //平仓
    @POST("mango-trader-api/trans/getTransfer")
    Observable<BaseResponse<AtBaseBean>> getTransfer(@Query("token") String token,
                                                     @Query("orderId") String orderId,
                                                     @Query("secret_access_key") String secret_access_key);


    //修改持仓过夜状态 true 过夜  false 不过夜
    @POST("mango-trader-api/trans/getOrderHoldNight")
    Observable<BaseResponse<AtBaseBean>> getOrderHoldNight(@Query("token") String token,
                                                           @Query("secret_access_key") String secret_access_key,
                                                           @Query("orderId") String orderId,
                                                           @Query("orderNo") String orderNo,
                                                           @Query("holdNight") boolean holdNight);


    //修改止盈止损
    @POST("mango-trader-api/trans/getProfitLossLimit")
    Observable<BaseResponse<AtBaseBean>> getProfitLossLimit(@Query("token") String token,
                                                            @Query("secret_access_key") String secret_access_key,
                                                            @Query("orderId") String orderId,
                                                            @Query("profitLimit") int profitLimit,
                                                            @Query("lossLimit") int lossLimit,
                                                            @Query("ticketId") String ticketId,
                                                            @Query("orderNo") String orderNo);


    //获取 产品开始时间和做单比列
    @GET("mango-trader-api/product/getProductTime")
    Observable<BaseResponse<List<ProductTimeLimitBean>>> getProductTime(@Query("noEncryptDES") boolean noEncryptDES);


    //获取 单个产品开始时间和做单比列
    @GET("mango-trader-api/product/getSingleTime")
    Observable<BaseResponse<ProductTimeLimitBean>> getSingleTime(@Query("code") String code, @Query("noEncryptDES") boolean noEncryptDES);


    //获取对应时间的交易记录
    @POST("mango-trader-api/trans/getOrderBill")
    Observable<BaseResponse<CapitalBean>> getTodayOrderBill(@Query("token") String token,
                                                            @Query("page") int page,
                                                            @Query("page_size") int page_size,
                                                            @Query("open_time") long open_time,
                                                            @Query("close_time") long close_time);


    //获取提现记录
    @POST("mango-trader-api/charge/getWithdrawalRecord")
    Observable<BaseResponse<CapitalJsonBean>> getWithdrawalList(@Query("token") String token,
                                                                @Query("page") int page,
                                                                @Query("page_size") int page_size);


    //获取充值记录
    @POST("mango-trader-api/charge/getChargeRecord")
    Observable<BaseResponse<CapitalJsonBean>> getRechargeList(@Query("token") String token,
                                                              @Query("page") int page,
                                                              @Query("page_size") int page_size);


    //账户持仓单金额信息
    @GET("mango-order-api/order/getUserMoney")
    Observable<BaseResponse<List<UserPositionBean>>> getUserMoney(@Query("token") String token,
                                                                  @Query("noEncryptDES") boolean noEncryptDES);


    //正在挂单列表
    @POST("mango-register-api/goingRegisterOrder/queryGoingRegisterOrder")
    Observable<BaseResponse<List<HangUpListBean>>> getHangUpOrderList();


    //挂单历史列表
    @POST("mango-register-api/goingRegisterOrder/queryHistoryRegisterOrder")
    Observable<BaseResponse<List<HangUpListBean>>> getHangUpHistoryList(@Query("pageSize") int pageSize, @Query("pageIndex") int pageIndex);


    //撤单
    @POST("mango-register-api/goingRegisterOrder/cancelGoingRegisterOrder")
    Observable<BaseResponse<JsonNull>> getCancelHangUp(@Query("id") String id);

    //----------行情相关-------

    //单个行情
    @GET("mango-realTime-api/real/getReal")
    Observable<BaseResponse<MarketHQBean>> getReal(@Query("type") String type,
                                                   @Query("noEncryptDES") boolean noEncryptDES);


    //单个行情(参考数据)
    @GET("mango-realTime-api/real/getOtherReal")
    Observable<BaseResponse<MarketHQBean>> getOtherReal(@Query("type") int type,
                                                        @Query("code") String code,
                                                        @Query("noEncryptDES") boolean noEncryptDES);

    //多个个行情
    @GET("mango-realTime-api/real/getAll")
    Observable<BaseResponse<List<MarketHQBean>>> getZJMarketList(@Query("noEncryptDES") boolean noEncryptDES);

    //参考行情列表
    @GET("mango-realTime-api/real/getOtherAll")
    Observable<BaseResponse<List<MarketHQBean>>> getZJCKSJMarketList(@Query("noEncryptDES") boolean noEncryptDES,
                                                                     @Query("type") int tabType);


    //用户中金账号信息(只有新手下单用)
    @POST("aj-zjwlUser-api/love/tao/user/getNewUserInfo")
    Observable<BaseResponse<UserZJBean>> getNewUserZjInfo(@Query("token") String token,
                                                          @Query("userId") String userId);

    //----------平台登录相关-------
    //获取登录、注册验证码
    @POST("mango-appUser-api/app/user/sendSms")
    Observable<BaseResponse<JsonNull>> sendSms(@Query("mobile") String phone);

    //登录、注册
    @POST("mango-appUser-api/app/user/login")
    Observable<BaseResponse<LoginUserBean>> loginApp(@Query("mobile") String phone,
                                                     @Query("smsCode") String smsCode,
                                                     @Query("nickName") String nickName,
                                                     @Query("appImei") String appImei);

    //退出登录
    @POST("mango-appUser-api/app/user/logout")
    Observable<BaseResponse<JsonNull>> outLogin(@Query("phone") String phone);


    //头像库
    @POST("mango-appUser-api/app/user/getImageList")
    Observable<BaseResponse<List<HeadImageBean>>> getImageList(@Query("phone") String phone);

    //更改头像
    @POST("mango-appUser-api/app/user/updatePic")
    Observable<BaseResponse<JsonNull>> updateHeadPic(@Query("userId") String userId,
                                                     @Query("userPic") String userPic,
                                                     @Query("genius") int genius,
                                                     @Query("phone") String phone);

    //头像图片上传
    @Multipart
    @POST("mango-appUser-api/app/user/getSaveImage")
    Observable<BaseResponse<String>> uploadHeadImage(@Part MultipartBody.Part file, @Query("requestId") String requestId);

    //更改昵称
    @POST("mango-appUser-api/app/user/updateNickName")
    Observable<BaseResponse<JsonNull>> updateNickName(@Query("userId") String userId,
                                                      @Query("nickName") String nickName,
                                                      @Query("genius") int genius,
                                                      @Query("phone") String phone);

    //获取用户积分
    @POST("mango-mall-api/app/userScore/queryMyScore")
    Observable<BaseResponse<IntegralBean>> getUserIntegral(@Query("token") String token
            , @Query("userId") String userId
            , @Query("phone") String phone);

    /*------------跟单相关-------------*/

    //判断是不是牛人
    @POST("mango-fellowOrder-api/app/rank/getValidRole")
    Observable<BaseResponse<Boolean>> getValidRole();

    //获取盈利播报
    @POST("mango-fellowOrder-api/app/rank/getRecordList")
    Observable<BaseResponse<List<NoticeListBean>>> getRecordList();

    //跟单 （首页列表）
    @POST("mango-fellowOrder-api/app/rank/fellowInfo")
    Observable<BaseResponse<List<FollowListBean>>> getOrderList(@Query("productName") String productName);

    //跟单 （首页列表 获取精华帖）
    @POST("mango-fellowOrder-api/app/rank/bestOrder")
    Observable<BaseResponse<List<FollowListBean>>> getBestOrderList(@Query("productName") String productName);


    //发起跟买 持仓
    @POST("mango-fellowOrder-api/app/rank/getOrderPosition")
    Observable<BaseResponse<List<FollowRecordBean>>> getOrderPosition();

    //发起跟买 平仓
    @POST("mango-fellowOrder-api/app/rank/getOrderEvenUp")
    Observable<BaseResponse<List<FollowRecordBean>>> getOrderEveningUp(@Query("customerPhone") String customerPhone,
                                                                       @Query("pageSize") int pageSize,
                                                                       @Query("pageIndex") int pageIndex);

    //我的跟买 持仓
    @POST("mango-fellowOrder-api/app/rank/getMyOrderPosition")
    Observable<BaseResponse<List<FollowRecordBean>>> getMyOrderPosition();

    //我的跟买 平仓
    @POST("mango-fellowOrder-api/app/rank/getMyOrderEvenUp")
    Observable<BaseResponse<List<FollowRecordBean>>> getMyOrderEvenUp(@Query("pageSize") int pageSize,
                                                                      @Query("pageIndex") int pageIndex);

    //跟买盈利
    @POST("mango-fellowOrder-api/app/rank/getWeek")
    Observable<BaseResponse<RecordRankBean>> getWeekProfit(@Query("customerPhone") String customerPhone);

    //牛人排行榜
    @POST("mango-fellowOrder-api/app/rank/getRank")
    Observable<BaseResponse<List<RankListBean>>> getRankList();

    //查看牛人持仓
    @POST("mango-fellowOrder-api/app/rank/getRankOrder")
    Observable<BaseResponse<List<PersonOrderBean>>> getRankingOrder(@Query("customerPhone") String customerPhone);

    //牛人统计(饼图)
    @POST("mango-fellowOrder-api/app/rank/fellowStatistics")
    Observable<BaseResponse<WeekCountBean>> getWeekCount(@Query("customerPhone") String customerPhone);


    //获取牛人个人信息
    @POST("mango-fellowOrder-api/app/rank/getProfit")
    Observable<BaseResponse<PersonInfoBean>> getPersonInfo(@Query("customerPhone") String customerPhone);

    //关注
    @POST("mango-fellowOrder-api/app/concern/addConcern")
    Observable<BaseResponse<JsonNull>> addConcern(@Query("concernPhone") String concernPhone
            , @Query("isConcern") String isConcern);

    //获取我的关注列表
    @POST("mango-fellowOrder-api/app/concern/selectList")
    Observable<BaseResponse<List<MyFollowerBean>>> getMyFollowerList(@Query("pageIndex") String pageIndex
            , @Query("pageSize") String pageSize);


    //----------交易所相关-------
    //登录中金交易所
    @POST("mango-trader-api/user/getUserLogin")
    Observable<BaseResponse<UserZJRegisterBean>> getUserZjLogin(@Query("mobile") String mobile,
                                                                @Query("password") String password);

    //获取图片验证码
    @POST("mango-trader-api/third/getSmsCaptcha")
    Observable<BaseResponse<UserZJSmsCaptchaBean>> getUserZjSmsCaptcha();

    //修改密码中金
    @POST("mango-trader-api/user/getUserForgot")
    Observable<BaseResponse<UserZJRegisterBean>> getUserZjForgot(@Query("mobile") String mobile,
                                                                 @Query("password") String password,
                                                                 @Query("smsCode") String smsCode,
                                                                 @Query("token") String token);

    //检查中金 交易所是否已经注册
    @POST("mango-trader-api/user/getUserCheck")
    Observable<BaseResponse<UserZJCheckBean>> getUserZjCheck(@Query("mobile") String mobile,
                                                             @Query("codeType") String codeType);

    //注册中金交易所
    @POST("mango-trader-api/user/getRegister")
    Observable<BaseResponse<UserZJRegisterBean>> getZjRegister(@Query("mobile") String mobile,
                                                               @Query("password") String password);

    //中金交易所 获取验证码  短信类型: register注册 forgot忘记密码 withdraw提现允许值: “register”, “forgot”,”withdraw”
    @POST("mango-trader-api/third/getSmsCode")
    Observable<BaseResponse<UserZJSmsBean>> getUserZjSmsCode(@Query("mobile") String mobile,
                                                             @Query("codeType") String codeType);


    @POST("mango-trader-api/third/getForgetSms")
    Observable<BaseResponse<UserZJSmsBean>> getForgetSmsCode(@Query("mobile") String mobile,
                                                             @Query("captcha") String captcha,
                                                             @Query("token") String token);

    //用户中金账号信息
    @POST("mango-trader-api/user/getUserInfo")
    Observable<BaseResponse<UserZJBean>> getUserZjInfo(@Query("token") String token);

    //---------------充值提现相关---------------

    //获取资金配置页面
    @POST("mango-trader-api/charge/getRechargeConfig")
    Observable<BaseResponse<RechargeConfigZJBean>> getRechargeConfig(@Query("token") String token);

    //入金
    @POST("mango-trader-api/charge/getCharge")
    Observable<BaseResponse<RechargeMoneyZJBean>> getRechargeMoney(@Query("token") String token,
                                                                   @Query("channel_id") String channel_id,
                                                                   @Query("secret_access_key") String secret_access_key,
                                                                   @Query("amount") String amount,
                                                                   @Query("extra") String extra,
                                                                   @Query("channel") String channel);

    //获取金额配置
    @POST("mango-app-api/app/scCharge/selectAll")
    Observable<BaseResponse<List<PictureZJBean>>> getPicture();


    //查询绑卡列表
    @POST("mango-trader-api/card/getBankcardList")
    Observable<BaseResponse<BankCardZjListBean>> getBankcardZjList(@Query("token") String token);

    //解绑 银行卡
    @POST("mango-trader-api/card/getUnbindCard")
    Observable<BaseResponse<BankUnbCardZJBean>> unbindingCard(@Query("token") String token,
                                                              @Query("bankCardId") String bankCardId);

    //提现
    @POST("mango-trader-api/withdrawal/getWithdrawMoney")
    Observable<BaseResponse<ZJBaseBean>> getWithdrawMoney(@Query("token") String token,
                                                          @Query("smsCode") String smsCode,
                                                          @Query("password") String password,
                                                          @Query("amount") String amount,
                                                          @Query("account_id") String account_id,
                                                          @Query("card") String card,
                                                          @Query("secret_access_key") String secret_access_key);

    //提现渠道列表
    @POST("mango-trader-api/withdrawal/getWithdrawsList")
    Observable<BaseResponse<PayModeZJBean>> getWithdrawsList(@Query("token") String token);

    //绑卡并实名认证
    @POST("mango-trader-api/card/getVerifyUser")
    Observable<BaseResponse<BankCardZjBean>> getVerifyUser(@Query("token") String token,
                                                        @Query("realName") String realName,
                                                        @Query("bankCard") String bankCard,
                                                        @Query("channel") String channel,
                                                        @Query("idCard") String idCard,
                                                        @Query("bankMobile")String bankMobile) ;

    //校验是否已实名认证
    @POST("mango-trader-api/card/getVerifyCard")
    Observable<BaseResponse<Boolean>> getVerifyCard(@Query("token") String token,
                                                        @Query("realName") String real_name,
                                                        @Query("bankCard") String account,
                                                        @Query("bankMobile")String bankMobile,
                                                        @Query("idCard") String idCard);

    //绑卡 中金
    @POST("mango-trader-api/card/getAddCard")
    Observable<BaseResponse<BankCardZjBean>> bankCardZj(@Query("token") String token,
                                                        @Query("real_name") String real_name,
                                                        @Query("account") String account,
                                                        @Query("channel") String channel,
                                                        @Query("idCard") String idCard);

    //---------------银行卡相关---------------

    //平台绑卡
    @POST("mango-trader-api/card/getSaveCard")
    Observable<BaseResponse<JsonNull>> getSaveCard(@Query("account") String account);

    //平台查询绑卡列表
    @POST("mango-trader-api/card/getCardList")
    Observable<BaseResponse<List<BankListBean>>> getCardList();

    //平台解绑银行卡
    @POST("mango-trader-api/card/getDeleteCard")
    Observable<BaseResponse<JsonNull>> getDeleteCard(@Query("account") String account);

    //-----------代金券------------

    //可用代金券
    @POST("mango-trader-api/voucher/getVoucher")
    Observable<BaseResponse<List<VoucherKyZJBean>>> getUserVoucherZjList(@Query("token") String token,
                                                                         @Query("userId") String userId);

    //历史代金券
    @POST("mango-trader-api/voucher/getHisVoucher")
    Observable<BaseResponse<VoucherGqZJBean>> getHistoryCouponsList(@Query("token") String token,
                                                                    @Query("page") int page,
                                                                    @Query("pageSize") int pageSize);

    //发送消息
    @POST("mango-register-api/alertJpushController/msgProducer")
    Observable<BaseResponse<JsonNull>> msgProducer(@Query("phone") String phone,
                                                   @Query("content") String content,
                                                   @Query("nickName") String name);

    //判断是不是管理员
    @POST("mango-register-api/alertJpushController/queryVipUser")
    Observable<BaseResponse<Integer>> queryVipUser();

    //禁言
    @POST("mango-register-api/alertJpushController/forbiddenUser")
    Observable<BaseResponse<JsonNull>> forbiddenUser(@Query("isPhone") String isPhone);


    //屏蔽接口
    @POST("mango-app-api/app/shieldAppMarket/selectShield")
    Observable<BaseResponse<Boolean>> selectShield();
}
