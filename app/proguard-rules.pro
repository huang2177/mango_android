# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#本地配置
-dontpreverify
-ignorewarnings
# For native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
#保护xml映射
-keep class org.xmlpull.v1.** {*;}

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

-keepattributes *Annotation*


-keep class android.support.** {*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#自定义view混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#bean 防混淆
-keep class com.paizhong.manggo.ui.kchart.bean.** { *; }
-keep class com.paizhong.manggo.bean.at.** { *; }
-keep class com.paizhong.manggo.bean.base.** { *; }
-keep class com.paizhong.manggo.bean.follow.** { *; }
-keep class com.paizhong.manggo.bean.home.** { *; }
-keep class com.paizhong.manggo.bean.market.** { *; }
-keep class com.paizhong.manggo.bean.other.** { *; }
-keep class com.paizhong.manggo.bean.trade.** { *; }
-keep class com.paizhong.manggo.bean.user.** { *; }
-keep class com.paizhong.manggo.bean.zj.** { *; }


-keep class com.gensee.** { *; }

#网络层混淆
-keep class com.paizhong.manggo.http.** {*;}

# javascript 防混淆
-keep class com.paizhong.manggo.ui.web.WebViewActivity$JavaMethod{*;}

-keep class com.sobot.chat.activity.WebViewActivity$JavaMethod{*;}

-keep class com.paizhong.manggo.ui.home.chance.TradeDetailModule$JavaMethod{*;}

-keep class com.paizhong.manggo.ui.home.emergency.EmergencyDetailActivity$JavaMethod{*;}

-keepattributes *JavascriptInterface*
-keep class android.webkit.JavascriptInterface {*;}


-keepclassmembers class * {
     public <init>(org.json.JSONObject);
    }
 -keep public class com.paizhong.manggo.R$*{
      public static final int *;
 }

 ###排除所有注解类
 -keep class * extends java.lang.annotation.Annotation { *; }
 -keep interface * extends java.lang.annotation.Annotation { *; }

 #保持枚举 enum 类不被混淆
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}


# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** {*; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-renamesourcefileattribute SourceFile
-keep class * implements java.io.Serializable {*;}
-keepclassmembers class * implements java.io.Serializable {*;}

#################################三方混淆配置##################################
# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}


# tencent xg
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
-keep class com.squareup.okhtttp.**{*;}

# Okio
-dontwarn com.squareup.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# fastJson
-keep class com.alibaba.fastjson.** { *; }
-keep class com.alibaba.**{*;}

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep class com.bumptech.glide.integration.okhttp.OkHttpGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

## Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#butterknife混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-dontwarn com.tencent.**
-dontwarn com.alibaba.**
-dontwarn com.umeng.**
-dontwarn com.facebook.**
-dontwarn com.squareup.okhttp.**
-dontwarn java.nio.**
-dontwarn okio.**

#Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keepclassmembers class * extends android.webkit.WebChromeClient {
     public void openFileChooser(...);
}

-keepclassmembers class * {
    public void openFileChooser(android.webkit.ValueCallback,java.lang.String);
    public void openFileChooser(android.webkit.ValueCallback);
    public void openFileChooser(android.webkit.ValueCallback, java.lang.String, java.lang.String);
}
#高德地图
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

-dontwarn com.amap.**
-dontwarn com.amap.api.**

-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

#友盟混淆
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}

-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {*;}

#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

#udesk
-keep class udesk.** {*;}
-keep class cn.udesk.**{*; }
#七牛
-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings
#smack
-keep class org.jxmpp.** {*;}
-keep class de.measite.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.xmlpull.** {*;}
-dontwarn org.xbill.**
-keep class org.xbill.** {*;}

#freso
-keep class com.facebook.** {*; }
-keep class com.facebook.imagepipeline.** {*; }
-keep class com.facebook.animated.gif.** {*; }
-keep class com.facebook.drawee.** {*; }
-keep class com.facebook.drawee.backends.pipeline.** {*; }
-keep class com.facebook.imagepipeline.** {*; }
-keep class bolts.** {*; }
-keep class me.relex.photodraweeview.** {*; }

-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

-dontwarn android.net.compatibility.**
 -dontwarn android.net.http.**
 -dontwarn com.android.internal.http.multipart.**
 -dontwarn org.apache.commons.**
 -dontwarn org.apache.http.**
 -keep class android.net.compatibility.**{*;}
 -keep class android.net.http.**{*;}
 -keep class com.android.internal.http.multipart.**{*;}
 -keep class org.apache.commons.**{*;}
 -keep class org.apache.http.**{*;}


-keep class com.cfmmc.app.sjkh.** {*;}
-dontwarn com.cfmmc.app.sjkh.**

-keep class com.bairuitech.** {*;}
-dontwarn com.bairuitech.**

-keep class com.ca.** {*;}
-dontwarn com.ca.**

-keep class com.google.zxing.client.** {*;}
-dontwarn com.google.zxing.client.**

-dontwarn com.gensee.**

-keep public class android.net.http.SslError

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

