package com.paizhong.manggo.ui.home.chance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.FlickerProgressBar;
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
public class TradeDetailModule extends IBaseView<TradeDetailBean> {

    private BasePresenter mTradeChanceDetailPresenter;
    private BaseActivity activity;
    public View mView;

    private CircleImageView civHead;
    private TextView tvName;
    private TextView tvTime;
    private TextView tvProductName;
    private TextView tvProductState;
    private TextView tvTopTitle;
    private TextView tvProposalName;
    private TextView tvProposalContent;
    private FlickerProgressBar pbProgress;
    private TextView tvBuyUp;
    private TextView tvBuyDown;
    private View rlBuyState;
    private View tvBuyStateHint;
    private WebView wvWeb;

    private boolean mIsWebLoaded;
    private String mTradeOpportunityId;

    public TradeDetailModule(BaseActivity activity, String mTradeOpportunityId) {
        this.activity = activity;
        this.mTradeOpportunityId = mTradeOpportunityId;
        this.mView = LayoutInflater.from(activity).inflate(R.layout.trade_detail_module, null);
        initView();
        refreshData();
    }

    private void initView() {
        civHead = (CircleImageView) mView.findViewById(R.id.civ_head);
        tvName = (TextView) mView.findViewById(R.id.tv_name);
        tvTime = (TextView) mView.findViewById(R.id.tv_time);
        tvProductName = (TextView) mView.findViewById(R.id.tv_product_name);
        tvProductState = (TextView) mView.findViewById(R.id.tv_product_state);
        tvTopTitle = (TextView) mView.findViewById(R.id.tv_top_title);
        tvProposalName = (TextView) mView.findViewById(R.id.tv_proposal_name);
        tvProposalContent = (TextView) mView.findViewById(R.id.tv_proposal_content);
        pbProgress = (FlickerProgressBar) mView.findViewById(R.id.pb_progress);
        tvBuyUp = (TextView) mView.findViewById(R.id.tv_buy_up);
        tvBuyDown = (TextView) mView.findViewById(R.id.tv_buy_down);
        rlBuyState = mView.findViewById(R.id.rl_buy_state);
        tvBuyStateHint = mView.findViewById(R.id.tv_buy_state_hint);
        wvWeb = (WebView) mView.findViewById(R.id.wv_web);

        pbProgress.setVisibility(View.GONE);
        rlBuyState.setVisibility(View.GONE);
        tvBuyStateHint.setVisibility(View.GONE);
    }

    public void refreshData() {
        if (mTradeChanceDetailPresenter == null) {
            mTradeChanceDetailPresenter = new BasePresenter<IBaseView<TradeDetailBean>>(this) {
                @Override
                public void getData() {
                    super.getData();
                    toSubscribe(HttpManager.getApi().getTradeDetail(mTradeOpportunityId), new HttpSubscriber<TradeDetailBean>() {

                        @Override
                        protected void _onCompleted() {
                            if (activity != null) {
                                activity.stopLoading();
                            }
                        }

                        @Override
                        protected void _onNext(TradeDetailBean detailBean) {
                            if (detailBean != null) {
                                bindData(detailBean);
                            }
                        }
                    });
                }
            };
        }
        mTradeChanceDetailPresenter.getData();
    }


    @Override
    public void bindData(TradeDetailBean bean) {
//        if (!TextUtils.isEmpty(bean.createMan)) {
//            tvName.setText(bean.createMan);
//        }
//        ImageUtils.display(bean.createManPic, civHead, R.mipmap.ic_user_head);

        tvTime.setText(bean.pubTime);

        if (!TextUtils.isEmpty(bean.productName)) {
            tvProductName.setText(bean.productName);
        }
        //direction 1是看空 2 是看多
        if (TextUtils.equals("1", bean.direction)) {
            tvProductState.setTextColor(activity.getResources().getColor(R.color.color_1AC47A));
            tvProductState.setText("看空");
        } else {
            tvProductState.setTextColor(activity.getResources().getColor(R.color.color_F74F54));
            tvProductState.setText("看多");
        }

        if (!TextUtils.isEmpty(bean.title)) {
            tvTopTitle.setText(bean.title);
        }

        tvProposalName.setText("分析师建议：");

        if (!TextUtils.isEmpty(bean.tips)) {
            tvProposalContent.setText(bean.tips);
        }

        if (bean.morePercent == 0 && bean.sellEmptyPercent == 0) {
            pbProgress.setVisibility(View.GONE);
            rlBuyState.setVisibility(View.GONE);
            tvBuyStateHint.setVisibility(View.GONE);
        } else {
            pbProgress.setVisibility(View.VISIBLE);
            rlBuyState.setVisibility(View.VISIBLE);
            tvBuyStateHint.setVisibility(View.VISIBLE);
        }
        pbProgress.setProgress(bean.morePercent);
        tvBuyUp.setText("买涨" + bean.morePercent + "%");
        tvBuyDown.setText("买跌" + bean.sellEmptyPercent + "%");

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
        wvWeb.addJavascriptInterface(new JavaMethod(activity), "imagelistner");//这个是给图片设置点击监听的，如果你项目
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


    public class JavaMethod {
        Context context;

        public JavaMethod(Context context) {
            this.context = context;
        }

        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface
        public void openImage(String img) {
            Intent intent = new Intent(activity, FullScreenDisplayActivity.class);
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
        if (mTradeChanceDetailPresenter != null) {
            mTradeChanceDetailPresenter.onDestroy();
        }
    }
}