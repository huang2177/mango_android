package com.paizhong.manggo.dialog.recharge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.base.BaseDialog;
import com.paizhong.manggo.bean.zj.BankListBean;
import com.paizhong.manggo.bean.zj.ChannelsZJBean;
import com.paizhong.manggo.bean.zj.PictureZJBean;
import com.paizhong.manggo.bean.zj.RechargeMoneyZJBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.events.BankListEvent;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.ui.paycenter.addbank.AddBankActivity;
import com.paizhong.manggo.ui.paycenter.qrcode.QrCodeActivity;
import com.paizhong.manggo.ui.paycenter.recharge.PayModeAdapter;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeAdapter;
import com.paizhong.manggo.ui.paycenter.recharge.RechargeContract;
import com.paizhong.manggo.ui.paycenter.recharge.RechargePresenter;
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.NoScrollGridView;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.net.URLEncoder;
import java.util.List;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Des: 充值dialog
 * Created by hs on 2018/6/20 0020 10:02
 */
public class RechargeDialog extends BaseDialog<RechargePresenter> implements RechargeContract.View
        , View.OnClickListener
        , AdapterView.OnItemClickListener
        , BaseRecyclerAdapter.OnItemClick {

    TextView tvCancel;
    TextView tvDetermine;
    RecyclerView listView;
    NoScrollGridView gridView;


    private AppHintDialog mAppHintDialog;
    private PayModeAdapter mPayModeAdapter;
    private RechargeAdapter mRechargeAdapter;

    private List<PictureZJBean> mPictureBeans;
    private List<ChannelsZJBean> mChannelsBeans;
    private String mBankCardId;

    public RechargeDialog(@NonNull BaseActivity context) {
        super(context, R.style.translucent_theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recharge);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }



    private void init() {
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert  manage !=null;
        manage.getDefaultDisplay().getMetrics(dm);

        params.width = mContext.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE ? dm.heightPixels : dm.widthPixels;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        tvCancel = findViewById(R.id.tv_cancel);
        listView = findViewById(R.id.lv_pay_mode);
        gridView = findViewById(R.id.gv_gridview);
        tvDetermine = findViewById(R.id.tv_determine);
        mRechargeAdapter = new RechargeAdapter(mContext,mContext.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE ? dm.heightPixels : dm.widthPixels);
        gridView.setAdapter(mRechargeAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        listView.setLayoutManager(manager);
        listView.setNestedScrollingEnabled(false);
        mPayModeAdapter = new PayModeAdapter(mContext);
        listView.setAdapter(mPayModeAdapter);
    }

    private void setListener() {
        tvCancel.setOnClickListener(this);
        tvDetermine.setOnClickListener(this);
        mPayModeAdapter.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    /**
     * 获取数据
     */
    public void getData() {
        mPresenter.getPicture();
        mPresenter.getRechargeConfig(false, AppApplication.getConfig().getAt_token());
        mPresenter.getBankcardZjList(AppApplication.getConfig().getAt_token());
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void bindRechargeConfig(List<ChannelsZJBean> list) {
        mPresenter.getCardList();
        mChannelsBeans = list;
        showDialog();
        if(mPictureBeans!=null && mPictureBeans.size()>3){
            mPayModeAdapter.updateData(mPictureBeans.get(3),mChannelsBeans);
        }else if(mPictureBeans!=null && mPictureBeans.size()>0){
            mPayModeAdapter.updateData(mPictureBeans.get(0),mChannelsBeans);
        }
    }

    @Override
    public void bindPicture(List<PictureZJBean> list) {
        mPictureBeans = list;
        showDialog();
    }

    /**
     * 等数据加载完成在显示
     */
    private void showDialog() {
        if (mPictureBeans != null && mChannelsBeans != null) {
            show();
            init();
            setListener();
            mPayModeAdapter.clearPayData();
            mPayModeAdapter.addData(mChannelsBeans);
            mRechargeAdapter.notifyDataChanged(mPictureBeans);
        }
    }



    /**
     * 绑卡列表 （用于验证）
     * @param bankCardId 对应卡id
     */
    @Override
    public void bindBankcardZjList(String bankCardId) {
        this.mBankCardId = bankCardId;
    }

    @Override
    public void bindRechargeMoney(RechargeMoneyZJBean bean) {
        if (bean !=null && bean.isNoVerifyCard()){
            showUnbindVerifyCard(mBankCardId,"您目前还没实名认证，请前往实名认证？");
            return;
        }

        if (bean == null || !bean.isSuccess() || bean.data == null || bean.data.extra == null) {
            String error = bean == null ? "请保持网络畅通" : bean.desc;
            showErrorMsg(error, null);
            return;
        }
        switch (bean.data.channel) {
            //支付宝 充值
            case "alipay_qr":
                //支付宝跳过开启动画打开扫码和付款码的url scheme分别是alipayqr://platformapi/startapp?saId=10000007和
                //alipayqr://platformapi/startapp?saId=20000056
                //alipayqr://platformapi/startapp?saId=10000007&qrcode=
                try {
                    //String qrCode = URLEncoder.encode(bean.data.extra.qr_code, "UTF-8");
                    //Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=" + qrCode);
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(bean.data.extra.qr_code));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg("推荐使用QQ浏览器打开",null);
                }
                break;

            //QQ充值
            case "qq_qr":
                Intent qqIntent = new Intent(mContext, WebViewActivity.class);
                qqIntent.putExtra("builderPar", false);
                qqIntent.putExtra("url", bean.data.extra.qr_code);
                qqIntent.putExtra("title", "充值");
                mContext.startActivity(qqIntent);

                if (mAppHintDialog == null) {
                    mAppHintDialog = new AppHintDialog(mContext);
                }
                mAppHintDialog.showAppDialog(9, -1, "充值" + (bean.data.amount / Constant.ZJ_PRICE_COMPANY) + "元，请等待打开QQ");
                break;

            //微信 充值
            case "wx_h5":
                Intent intent = new Intent(mContext, QrCodeActivity.class);
                intent.putExtra("qrCode", bean.data.extra.pay_url);
                intent.putExtra("amount", bean.data.amount);
                mContext.startActivity(intent);
                break;

            //银联 充值   bean.data.channel  quick_h5
            case "quick_h5":
                Intent qIntent = new Intent(mContext, WebViewActivity.class);
                qIntent.putExtra("builderPar", false);
                qIntent.putExtra("url", bean.data.extra.pay_url);
                qIntent.putExtra("title", "充值");
                mContext.startActivity(qIntent);

                if (mAppHintDialog == null) {
                    mAppHintDialog = new AppHintDialog(mContext);
                }
                mAppHintDialog.showAppDialog(9, -1, "充值" + (bean.data.amount / Constant.ZJ_PRICE_COMPANY) + "元，请等待打开银联");
                break;
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel: //取消
                dismiss();
                break;
            case R.id.tv_determine:  //确定
                if (TextUtils.equals("alipay_qr",mPayModeAdapter.getChannel())){
                    showErrorMsg("推荐使用QQ浏览器打开",null);
                }
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (mRechargeAdapter.getAmount() == -1) {
                    showErrorMsg("请选择金额", null);
                    return;
                }
                if (TextUtils.isEmpty(mPayModeAdapter.getModeId())) {
                    showErrorMsg("请选择充值渠道", null);
                    return;
                }
                //String token, String channel_id, String secret_access_key, String amount, String extra
                mPresenter.getRechargeMoney(AppApplication.getConfig().getAt_token()
                        , mPayModeAdapter.getModeId()
                        , AppApplication.getConfig().getAt_secret_access_key()
                        , String.valueOf(mRechargeAdapter.getAmount())
                        , "{\"front_end_url\":\"" + Constant.H5_RECHARGE_COMPLETED + "\"}"
                        ,mPayModeAdapter.getChannel());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            //选择金额
            case R.id.gv_gridview:
                PictureZJBean pictureZJBean = (PictureZJBean) adapterView.getItemAtPosition(i);
//                if (pictureZJBean.chargeAmount / Constant.ZJ_PRICE_COMPANY > 500){
//                    if (mPayModeAdapter !=null){
//                        mPayModeAdapter.changePay(true);
//                    }
//                }else {
//                    if (mPayModeAdapter !=null){
//                        mPayModeAdapter.changePay(false);
//                    }
//                }
                mPayModeAdapter.updateData(pictureZJBean,mChannelsBeans);
                mRechargeAdapter.changeMoney(i);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mChannelsBeans == null) {
            return;
        }
        ChannelsZJBean channelsBean = mChannelsBeans.get(position);
        if (channelsBean.is_disable != 0) {
            showErrorMsg("通道维护中...", null);
            return;
        }
        mPayModeAdapter.changeID(channelsBean.id,channelsBean.channel);
    }

    /**
     * 充值后是否需要关闭dialog
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseUIRefreshEvent event) {
        if (event.mType == 2) {
            dismiss();
        }
    }

    @Override
    public void bindAddBank(String account) {
        mPayModeAdapter.bindAddBank(account);
    }

    @Override
    public void bindDeleteBank(int position) {
        mPayModeAdapter.bindDeleteBank(position);
    }

    @Override
    public void bindBankList(List<BankListBean> list) {
        mPayModeAdapter.setBankList(list);
        mPayModeAdapter.doExpandState(mPayModeAdapter.isExpand());
    }

    @Override
    public void dismiss() {
        onError();
        super.dismiss();
    }

    @Override
    public void onError() {
        onDestroy();
        //充值完成刷新金额
        EventBus.getDefault().post(new BaseUIRefreshEvent(2));
        EventBus.getDefault().unregister(this);
    }

    /**
     * 添加银行卡 解绑
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BankListEvent event) {
        if (event.code == 1) {
            mPresenter.getSaveCard(event.account);
        } else {
            mPresenter.getDeleteCard(event.account, event.position);
        }
    }




    /**
     * 验证解绑银行卡提示
     * @param bankCardId
     * @param hint
     */
    private void showUnbindVerifyCard(final String bankCardId,String hint){
        final AppHintDialog appHintDialog = new AppHintDialog(mContext);
        appHintDialog.showAppDialog(11, hint, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()){
                    if (!TextUtils.isEmpty(bankCardId)){
                        mPresenter.unbindingCard(AppApplication.getConfig().getAt_token(),bankCardId);
                    }
                    mContext.startActivity(new Intent(mContext, AddBankActivity.class));
                    appHintDialog.dismiss();
                }
            }
        });
    }
}
