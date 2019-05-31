package com.paizhong.manggo.ui.home.emergency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.home.EmergencyDetailBean;
import com.paizhong.manggo.ui.home.chance.FullScreenDisplayActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Des: 突发行情 详情
 * Created by hs on 2018/6/28 0028 15:19
 */
public class EmergencyDetailActivity extends BaseActivity<EmergencyDetailPresenter> implements EmergencyContract.View {

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    WebView mWebView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_emergency_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initWebView();
        getEmergencyDetail();
    }

    private void initWebView() {
        mTitle.setTitle(true, getIntent().getStringExtra("title"));

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setTextZoom(110);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
        webSettings.setJavaScriptEnabled(true);    //支持js
        webSettings.setBuiltInZoomControls(false); // 显示放大缩小
        webSettings.setSupportZoom(false);         // 可以缩放
        webSettings.setDisplayZoomControls(false);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小

        mWebView.setWebViewClient(new MyWebViewClient());
        //这个是给图片设置点击监听的，如果你项目
        mWebView.addJavascriptInterface(new JavaMethod(this), "imageListener");
    }

    private void getEmergencyDetail() {
        String url = getIntent().getStringExtra("url");
        mPresenter.getEmergencyDetail(url);
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            resetImageSize();
            addImageClickListener();
        }

        private void resetImageSize() {
            mWebView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName('img'); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "var img = objs[i];   " +
                    "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                    "}" +
                    "})()");
        }

        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数
        private void addImageClickListener() {
            mWebView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imageListener.openImage(this.src);  " +
                    "    }  " +
                    "}" +
                    "})()");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    @Override
    public void bindEmergencyDetail(EmergencyDetailBean data) {
        tvTitle.setText(data.title);
        mWebView.loadDataWithBaseURL(null, getNewContent(data), "text/html", "utf-8", null);
    }

    /**
     * 解析加载h5代码
     */
    public static String getNewContent(EmergencyDetailBean data) {
        Document doc = Jsoup.parse(data.text);
        Elements imgElements = doc.getElementsByTag("img");
        Elements pElements = doc.getElementsByTag("p");
        for (Element pElement : pElements) {
            pElement.attr("style", "line-height: 30px");
        }

        for (int i = 0; i < imgElements.size(); i++) {
            imgElements.get(i)
                    .attr("width", "100%")
                    .attr("height", "auto");

            if (data.text.trim().endsWith("</a></p>") && i == imgElements.size() - 1) {
                imgElements.get(i)
                        .attr("width", "0%")
                        .attr("height", "0%")
                        .attr("src", "");
            }
        }
        return doc.toString()
                .replace("<a", "<em")
                .replace("/a>", "/em>")
                .replace("金十", "芒果");
    }


    private class JavaMethod {
        Context context;

        public JavaMethod(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent(EmergencyDetailActivity.this, FullScreenDisplayActivity.class);
            Bundle b = new Bundle();
            ArrayList<String> images = new ArrayList<>();
            images.add(img);
            b.putStringArrayList("image_urls", images);
            b.putInt("position", 0);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}
