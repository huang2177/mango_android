package com.paizhong.manggo.bean.other;

/**
 * Created by zab on 2018/8/15 0015.
 */
public class BannerBean {

    /**
     * activityId : 32
     * activityName : 2
     * activityImageUrl : http://mangoo.oss-cn-shanghai.aliyuncs.com/banner/1536055505706-20180904180505.jpg
     * activityHtml5Url : 2
     * sort : 0
     * status : 0
     * startTime : 1536076800000
     * endTime : 1536163200000
     * type : 1
     * detail : 233
     * createTime : 1536055513000
     * modifyTime : 1536055513000
     * createMan : null
     * modifyMan : null
     * isNeedHead : 0
     */

    public String activityName;
    public int isLogin;  // 0 不需要登录  1需要登录
    public String activityImageUrl;
    public String activityHtml5Url;
    public int isNeedHead; //0 不需要   1 需要
    public String detail;//版本

    public BannerBean(String activityImageUrl, String activityHtml5Url) {
        this.activityImageUrl = activityImageUrl;
        this.activityHtml5Url = activityHtml5Url;
    }
}
