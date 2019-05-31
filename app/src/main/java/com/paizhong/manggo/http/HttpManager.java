package com.paizhong.manggo.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.utils.DESEncrypt;
import com.paizhong.manggo.utils.Logs;
import com.paizhong.manggo.utils.ZipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.Proxy;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit请求管理类<p>
 */
public class HttpManager {

    private HttpApi mHttpApi;
    private static HttpManager instance = null;

    private ByteArrayInputStream zipIs = null;
    private Source zipSource = null;
    private GzipSource zipGSource = null;
    private BufferedSource zipBuffer = null;

    /**
     * 获取单例
     *
     * @return 实例
     */
    public static HttpManager getInstance() {

        if (instance == null) {
            instance = new HttpManager();
            return instance;
        }
        return instance;
    }

    public static HttpApi getApi() {
        return getInstance().mHttpApi;
    }

    private HttpManager() {
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


    public void zipClose() {
        try {
            if (zipIs != null) {
                zipIs.close();
            }
            if (zipSource != null) {
                zipSource.close();
            }
            if (zipGSource != null) {
                zipGSource.close();
            }
            if (zipBuffer != null) {
                zipBuffer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OkHttpClient createOkHttpClient() {

        Cache cache = new Cache(new File(AppApplication.getInstance().getCacheDir(), "pengpeiCache"),
                1024 * 1024 * 100);

        Interceptor zipInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);

                if (response.body() != null) {
                    try {
                        String body = response.body().string();
                        if (Constant.OTHER_HOSTS.contains(request.url().host())) {
                            //Logs.d("http", body);
                            return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), body)).build();
                        } else {
                            JSONObject jsonResponse = new JSONObject(body);
                            String errorCode = jsonResponse.getString("errorCode");
                            if (!TextUtils.isEmpty(errorCode)) {
                                //1 数组 2 对象
                                if (TextUtils.equals("1", errorCode)) {
                                    zipIs = new ByteArrayInputStream(ZipUtils.toByteArray(jsonResponse.getString("data")));
                                    zipSource = Okio.source(zipIs);
                                    zipGSource = new GzipSource(zipSource);
                                    zipBuffer = Okio.buffer(zipGSource);
                                    jsonResponse.put("data", new JSONArray(zipBuffer.readUtf8()));

                                    //Logs.d("http", jsonResponse.toString());
                                    return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), jsonResponse.toString())).build();
                                } else if (TextUtils.equals("2", errorCode)) {
                                    zipIs = new ByteArrayInputStream(ZipUtils.toByteArray(jsonResponse.getString("data")));
                                    zipSource = Okio.source(zipIs);
                                    zipGSource = new GzipSource(zipSource);
                                    zipBuffer = Okio.buffer(zipGSource);
                                    jsonResponse.put("data", new JSONObject(zipBuffer.readUtf8()));

                                    //Logs.d("http", jsonResponse.toString());
                                    return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), jsonResponse.toString())).build();
                                } else {

                                    //Logs.d("http", body);
                                    return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), body)).build();
                                }
                            } else {
                                //Logs.d("http", body);
                                return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), body)).build();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //Logs.d("http", response.body().string());

                return response.newBuilder().body(ResponseBody.create(MediaType.parse("UTF-8"), response.body().string())).build();
            }
        };

        //日志拦截器
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Logs.d("http", message);
//            }
//        });


        //添加全局统一请求参数
        Interceptor paramsInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                JSONObject jsonObject = new JSONObject();
                boolean noEncryptDES = false;
                try {
                    HttpUrl httpUrl = request.url();
                    HttpUrl.Builder builder = httpUrl.newBuilder();
                    Set<String> queryParameterNames = httpUrl.queryParameterNames();
                    noEncryptDES = queryParameterNames.contains("noEncryptDES");
                    if (!noEncryptDES) {
                        for (String parameterName : queryParameterNames) {
                            String value = httpUrl.queryParameter(parameterName);
                            if (!TextUtils.isEmpty(value)) {
                                jsonObject.put(parameterName, httpUrl.queryParameter(parameterName));
                            }
                            builder.removeAllQueryParameters(parameterName);
                        }

                        jsonObject.put("clientType", "android");
                        jsonObject.put("appMarket", AppApplication.getConfig().getAppMarket());
                        jsonObject.put("appVersion", AppApplication.getConfig().getAppVersion());
                        jsonObject.put("deviceName", AppApplication.getConfig().getDeviceName());
                        jsonObject.put("osVersion", AppApplication.getConfig().getOsVersion());
                        jsonObject.put("deviceId", AppApplication.getConfig().getDeviceId());
                        jsonObject.put("phone", AppApplication.getConfig().getMobilePhone());
                        jsonObject.put("appToken", AppApplication.getConfig().getAppToken());

                        builder.addQueryParameter("type", "0");
                        String AJ_CIPHER = DESEncrypt.encryptDES(jsonObject.toString(), DESEncrypt.KEY);
                        builder.addQueryParameter("AJ_CIPHER", AJ_CIPHER);
                        String AJ_TOKEN = DESEncrypt.encode(AJ_CIPHER.getBytes("utf-8"));
                        builder.addQueryParameter("AJ_TOKEN", AJ_TOKEN);
                        Request.Builder requestBuilder = request.newBuilder();

                        requestBuilder.url(builder.build());
                        request = requestBuilder.build();

                        //Logs.d("xzy","=====打印加密前数据========="+jsonObject.toString());
                    } else {
                        for (String parameterName : queryParameterNames) {
                            if (TextUtils.equals("noEncryptDES", parameterName)) {
                                builder.removeAllQueryParameters(parameterName);
                                break;
                            }
                        }
                        builder.addQueryParameter("clientType", "android");
                        //Logs.d("xzy","=====打印加密前数据========="+httpUrl.encodedQuery());
                        request = request.newBuilder().url(builder.build()).build();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return chain.proceed(request);
            }
        };

        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //CookieManager管理器
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        //InputStream inputStream = AppApplication.getInstance().getResources().openRawResource(R.raw.calffutuer);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .cache(cache)   //缓存
                //.sslSocketFactory(HttpsUtils.getCertificates(inputStream))
                //.addInterceptor(headerInterceptor)
                .proxy(Proxy.NO_PROXY)  //防止抓包工具抓包
                .addInterceptor(paramsInterceptor)
                .addInterceptor(zipInterceptor)
                .cookieJar(new JavaNetCookieJar(cookieManager))//设置持续化cookie
                //.addInterceptor(logging)    //打印日志
                .retryOnConnectionFailure(true)//失败重连
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .hostnameVerifier(HttpsUtils.getHostnameVerifier())
                .build();
        return mOkHttpClient;
    }
}
