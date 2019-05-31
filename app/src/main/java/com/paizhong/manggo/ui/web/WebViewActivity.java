package com.paizhong.manggo.ui.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import com.google.gson.Gson;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.other.WebViewParamsBean;
import com.paizhong.manggo.events.JysAccountEvent;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeActivity;
import com.paizhong.manggo.utils.DESEncrypt;
import com.paizhong.manggo.utils.NumberUtil;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;

/**
 * Created by zab on 2018/3/28 0028.
 */

//ppmgtaojin://mgtaojin/mgtaojin/webView?url=https://www.baidu.com/&title=h5
public class WebViewActivity extends BaseActivity<WebViewPresenter> implements WebViewContract.View {
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.webview)
    WebView mWebView;

    private String mWebUrl;
    public String title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    @Override
    public void loadData() {
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

        //是否需要head
        if (getIntent().getBooleanExtra("noTitle",false)) {
            mTitle.setTitleVisibility(View.GONE);
        }else {
            mTitle.setTitle(true, title);
        }

         String  url = getIntent().getStringExtra("url");
         title = getIntent().getStringExtra("title");
         //builderPar 是否需要参数
         boolean mBuilderUrl = getIntent().getBooleanExtra("builderPar", true) && AppApplication.getConfig().isLoginStatus();

        //获取web url
        if (mBuilderUrl && !TextUtils.isEmpty(url)){
            mWebUrl = url +"?params="+ Uri.encode(DESEncrypt.encodeToString(DESEncrypt.aesEncryptToBytes(getParamsGson(),DESEncrypt.AES_KEY),Base64.DEFAULT));
        }else {
            mWebUrl = url;
        }
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //WebView属性设置！！！
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不适用缓存
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);

        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            settings.setMixedContentMode(WebSettings.);
        }
        mWebView.addJavascriptInterface(new JavaMethod(), "nativeMethod");
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());

        //http://h5.kzwawa.com/h5/ev_activity/rankingLists
        if (!TextUtils.isEmpty(mWebUrl)) {
            mWebView.loadUrl(mWebUrl);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.destroy();
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mProgressBar != null) {
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String webtitle) {
            super.onReceivedTitle(view, webtitle);
            if (!TextUtils.isEmpty(webtitle)) {
                mTitle.setTitle(true, title);
            } else {
                title = webtitle;
                mTitle.setTitle(true, title);
            }
        }
    }


    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, com.tencent.smtt.export.external.interfaces.WebResourceRequest webResourceRequest) {
            Uri url = webResourceRequest.getUrl();
            try {
                if (url.getScheme().contains("mqqapi")) {//qq支付
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);
                    return true;
                } else if (url.getScheme().contains("ppmgtaojin")) {
                    boolean login = url.getBooleanQueryParameter("login", false);
                    schemejump(login, url);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url) && url.startsWith("ppmgtaojin")) {
                Uri sssuri = Uri.parse(url);
                boolean login = sssuri.getBooleanQueryParameter("login", false);
                schemejump(login, sssuri);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            sslErrorHandler.proceed();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mWebUrl = url;
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public class JavaMethod {

        @JavascriptInterface
        public void backWeb() {
            finish();
        }


        //充值完成
        @JavascriptInterface
        public void rechargeCompleted() {
            finish();
        }


        //登录
        @JavascriptInterface
        public void loginAppTrade() {
            //屏蔽审核
            mIsSchemeLogin = false;
            mIsWebParms = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (login()) {
                        webGetTradeParams();
                    }
                }
            });
        }
    }


    //交易所登录成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(JysAccountEvent event) {
        if (event.code == 1) {
            if (mIsSchemeLogin) {
                if (!TextUtils.isEmpty(mSchemeUrl)) {
                    jump(Uri.parse(mSchemeUrl));
                }
            } else if (mIsWebParms) {
                webGetTradeParams();
            }
        }
    }



    //web获取交易所登录数据
    private void webGetTradeParams() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null) {
                    mWebView.loadUrl("javascript:getParams('" + Uri.encode(DESEncrypt.encodeToString(DESEncrypt.aesEncryptToBytes(getParamsGson(),DESEncrypt.AES_KEY),Base64.DEFAULT)) + "')");
                }
            }
        });
    }


    //web获取数据
    private boolean mIsWebParms = false;
    //协议跳转
    private String mSchemeUrl;
    private boolean mIsSchemeLogin = false;

    private void schemejump(boolean login, Uri url) {
        this.mIsWebParms = false;
        if (login) {
            this.mIsSchemeLogin = true;
            this.mSchemeUrl = url != null ? url.toString() : "";
            if (login()) {
                jump(url);
            }
        } else {
            this.mIsSchemeLogin = false;
            this.mSchemeUrl = "";
            jump(url);
        }
    }


    //协议跳转页面
    private void jump(final Uri url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlPath = url.getLastPathSegment();
                    if (TextUtils.equals("main",urlPath)){
                        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                        intent.putExtra("page", NumberUtil.getInt(url.getQueryParameter("page"),-1));
                        intent.putExtra("smPage", NumberUtil.getInt(url.getQueryParameter("smPage"),-1));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else if (TextUtils.equals("recharge",urlPath)){
                        if (!AppApplication.getConfig().mIsAuditing){
                            Intent intent = new Intent(WebViewActivity.this, RechargeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private String getParamsGson() {
        WebViewParamsBean paramsBean = new WebViewParamsBean();
        paramsBean.appToken = AppApplication.getConfig().getAppToken();
        paramsBean.appUserId = AppApplication.getConfig().getUserId();
        paramsBean.phone = AppApplication.getConfig().getMobilePhone();
        if (!TextUtils.isEmpty(AppApplication.getConfig().getAt_token())){
            paramsBean.exchangeToken = AppApplication.getConfig().getAt_token();
            paramsBean.exchangeuserId = AppApplication.getConfig().getAt_userId();
            paramsBean.secretAccessKey = AppApplication.getConfig().getAt_secret_access_key();
        }
        return new Gson().toJson(paramsBean);
    }
}
