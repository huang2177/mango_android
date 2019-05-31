package com.paizhong.manggo.ui.home.chance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BasePresenter;
import com.paizhong.manggo.base.IBaseView;
import com.paizhong.manggo.bean.home.TradeDetailBean;
import com.paizhong.manggo.http.HttpManager;
import com.paizhong.manggo.http.HttpSubscriber;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Des:
 * Created by huang on 2018/9/7 0007 11:49
 */
public class TradeNoticeModule extends IBaseView<TradeDetailBean> {

    private BaseActivity mActivity;
    private String mTradeOpportunityId;
    public View mView;
    private TextView tvTitle;
    private TextView tvTime;
    private WebView wvWeb;

    private boolean mIsWebLoaded;
    private BasePresenter mTradeChanceNoticePresenter;


    public TradeNoticeModule(BaseActivity mActivity, String mTradeOpportunityId) {
        this.mActivity = mActivity;
        this.mTradeOpportunityId = mTradeOpportunityId;
        this.mView = LayoutInflater.from(mActivity).inflate(R.layout.trade_notice_module, null);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        tvTime = (TextView) mView.findViewById(R.id.tv_time);
        wvWeb = (WebView) mView.findViewById(R.id.wv_web);
        refreshData();
    }

    public void refreshData() {
        if (mTradeChanceNoticePresenter == null) {
            mTradeChanceNoticePresenter = new BasePresenter<IBaseView<TradeDetailBean>>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getTradeDetail(mTradeOpportunityId), new HttpSubscriber<TradeDetailBean>() {

                        @Override
                        protected void _onCompleted() {
                            super._onCompleted();
                            if (mActivity != null) {
                                mActivity.stopLoading();
                            }
                        }

                        @Override
                        protected void _onNext(TradeDetailBean chanceDetailBean) {
                            if (chanceDetailBean != null) {
                                bindData(chanceDetailBean);
                            }
                        }
                    });
                }
            };
        }
        mTradeChanceNoticePresenter.getData();
    }


    @Override
    public void bindData(TradeDetailBean bean) {
        tvTitle.setText(bean.title);
        tvTime.setText(bean.pubTime);

        //设置字体大小
        WebSettings webSettings = wvWeb.getSettings();
        webSettings.setTextZoom(110);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);//支持js
        webSettings.setBuiltInZoomControls(false); // 显示放大缩小
        webSettings.setSupportZoom(false); // 可以缩放
        webSettings.setDisplayZoomControls(false);
        // 设置加载进来的页面自适应手机屏幕
        //settings.setUseWideViewPort(true);
        //mContentTv.getSettings().setLoadWithOverviewMode(true);
        if (!mIsWebLoaded) {
            wvWeb.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
            wvWeb.loadDataWithBaseURL(null, getNewContent(bean.edit), "text/html", "utf-8", null);
            mIsWebLoaded = true;
        }
        wvWeb.setWebViewClient(new MyWebViewClient());
        wvWeb.addJavascriptInterface(new JavaScriptInterface(mActivity), "imagelistner");//这个是给图片设置点击监听的，如果你项目
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        wvWeb.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }


    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        wvWeb.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 解析加载h5代码
     *
     * @param html
     * @return
     */
    public static String getNewContent(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("img");
        for (Element element: elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }


    public class JavaScriptInterface {
        Context context;

        public JavaScriptInterface(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent(context, FullScreenDisplayActivity.class);
            Bundle b = new Bundle();
            ArrayList<String> images = new ArrayList<>();
            images.add(img);
            b.putStringArrayList("image_urls", images);
            b.putInt("position", 0);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }


    /**
     * 销毁
     */
    public void onDestory() {
        if (wvWeb != null) {
            wvWeb.clearHistory();
            wvWeb.clearCache(true);
            wvWeb.destroy();
        }
        mTradeChanceNoticePresenter.onDestroy();
    }
}
