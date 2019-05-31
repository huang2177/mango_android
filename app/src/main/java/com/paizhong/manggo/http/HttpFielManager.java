package com.paizhong.manggo.http;

import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.utils.Logs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Retrofit请求管理类<p>
 */
public class HttpFielManager {

    private HttpApi mHttpApi;

    private static HttpFielManager instance = null;

    /**
     * 获取单例
     *
     * @return 实例
     */
    public static HttpFielManager getInstance() {

        if (instance == null) {
            instance = new HttpFielManager();
            return instance;
        }
        return instance;
    }



    public static HttpApi getApi() {
        return getInstance().mHttpApi;
    }

    private HttpFielManager() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .client(createOkHttpClient())
                //.addConverterFactory(ScalarsConverterFactory.create()) 返回类型转成String
                //.addConverterFactory(MyGsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mHttpApi = retrofit.create(HttpApi.class);
    }

    public OkHttpClient createOkHttpClient() {
        Cache cache = new Cache(new File(AppApplication.getInstance().getCacheDir(), "pengpeiCache"),
                1024 * 1024 * 100);
        //日志拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Logs.d("http", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //CookieManager管理器
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        //InputStream inputStream = AppApplication.getInstance().getResources().openRawResource(R.raw.calffutuer);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .cache(cache)   //缓存
                //.sslSocketFactory(HttpsUtils.getCertificates(inputStream))
                //.addInterceptor(headerInterceptor)
                //.addInterceptor(paramsInterceptor)
                //.addInterceptor(showDialogInterceptor)
                .proxy(Proxy.NO_PROXY)  //防止抓包工具抓包
                .cookieJar(new JavaNetCookieJar(cookieManager))//设置持续化cookie
                .addInterceptor(logging)    //打印日志
                .retryOnConnectionFailure(true)//失败重连
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .hostnameVerifier(HttpsUtils.getHostnameVerifier())
                .build();
        return mOkHttpClient;
    }


    /**
     * 处理线程调度
     *
     * @param <T>
     * @return
     */
    public <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }
}
