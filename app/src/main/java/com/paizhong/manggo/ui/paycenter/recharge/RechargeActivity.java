package com.paizhong.manggo.ui.paycenter.recharge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
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
import com.paizhong.manggo.ui.web.WebViewActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.widget.NoScrollGridView;
import com.paizhong.manggo.widget.FixRefreshLayout;
import com.paizhong.manggo.widget.recycle.BaseRecyclerAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 充值
 * Created by zab on 2018/4/8 0008.
 */
//ppmgtaojin://mgtaojin/mgtaojin/recharge?appLogin=true&tradeLogin=true
//@Route(path = "/mgtaojin/recharge")
public class RechargeActivity extends BaseActivity<RechargePresenter> implements RechargeContract.View {
    @BindView(R.id.smart_refresh)
    FixRefreshLayout smartRefresh;
    @BindView(R.id.gv_gridview)
    NoScrollGridView gvGridview;
    @BindView(R.id.lv_pay_mode)
    RecyclerView mRecycleView;
    @BindView(R.id.tv_determine)
    TextView tvDetermine;

    private RechargeAdapter mRechargeAdapter;
    private PayModeAdapter mPayModeAdapter;
    private AppHintDialog mAppHintDialog;
    private String mBankCardId;

    private List<ChannelsZJBean> channelsZJList = new ArrayList<>();
    private List<PictureZJBean> pictureZJList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {

        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);

        mTitle.setTitle(true, "充值");
        mRechargeAdapter = new RechargeAdapter(RechargeActivity.this,DeviceUtils.getScreenWidth(RechargeActivity.this));
        gvGridview.setAdapter(mRechargeAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setNestedScrollingEnabled(false);
        mPayModeAdapter = new PayModeAdapter(RechargeActivity.this);
        mRecycleView.setAdapter(mPayModeAdapter);

        setListener();
        mPresenter.getPicture();
        mPresenter.getRechargeConfig(false, AppApplication.getConfig().getAt_token());
        mPresenter.getBankcardZjList(AppApplication.getConfig().getAt_token());
    }


    private void setListener() {
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getPicture();
                mPresenter.getRechargeConfig(true, AppApplication.getConfig().getAt_token());
                smartRefresh.finishRefresh();
            }
        });

        gvGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PictureZJBean pictureZJBean = (PictureZJBean) adapterView.getItemAtPosition(position);
//                if (pictureZJBean.chargeAmount / Constant.ZJ_PRICE_COMPANY > 500){
//                    if (mPayModeAdapter !=null){
//                        mPayModeAdapter.changePay(true);
//                    }
//                }else {
//                    if (mPayModeAdapter !=null){
//                        mPayModeAdapter.changePay(false);
//                    }
//                }
                mPayModeAdapter.updateData(pictureZJBean,channelsZJList);
                mRechargeAdapter.changeMoney(position);
            }
        });

        mPayModeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                ChannelsZJBean channelsBean = mPayModeAdapter.getData().get(position);
                if (channelsBean.is_disable == 0) {
                    mPayModeAdapter.changeID(channelsBean.id,channelsBean.channel);
                } else {
                    showErrorMsg("通道维护中...", null);
                }
            }
        });


        tvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                mPresenter.getRechargeMoney(AppApplication.getConfig().getAt_token(), mPayModeAdapter.getModeId(), AppApplication.getConfig().getAt_secret_access_key()
                        , String.valueOf(mRechargeAdapter.getAmount()), "{\"front_end_url\":\"" + Constant.H5_RECHARGE_COMPLETED + "\"}",mPayModeAdapter.getChannel());
            }
        });
    }

    @Override
    public void bindRechargeConfig(List<ChannelsZJBean> list) {
        mPresenter.getCardList();  //必须在充值渠道获取成功后才能调用
        channelsZJList.clear();
        channelsZJList = list;
        mPayModeAdapter.clearPayData();
        mPayModeAdapter.addData(list);
        if(pictureZJList!=null && pictureZJList.size()>3){
            mPayModeAdapter.updateData(pictureZJList.get(3),channelsZJList);
        }else if(pictureZJList!=null && pictureZJList.size()>0){
            mPayModeAdapter.updateData(pictureZJList.get(0),channelsZJList);
        }
    }


    @Override
    public void bindPicture(List<PictureZJBean> list) {
        //mPayModeAdapter.setChangePay(false);
        pictureZJList.clear();
        pictureZJList = list;
        mRechargeAdapter.notifyDataChanged(list);
    }


    /**
     * 绑卡列表 （用于验证）
     * @param bankCardId 对应卡id
     */
    @Override
    public void bindBankcardZjList(String bankCardId) {
       this.mBankCardId = bankCardId;
    }


    //充值
    @Override
    public void bindRechargeMoney(final RechargeMoneyZJBean bean) {
        if (bean !=null && bean.isNoVerifyCard()){
            showUnbindVerifyCard(mBankCardId,"您目前还没实名认证，请前往实名认证？");
            return;
        }
        if (bean != null && bean.isSuccess() && bean.data != null && bean.data.extra != null) {
            if (TextUtils.equals("alipay_qr", bean.data.channel)) {
                //支付宝 充值
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
                    mActivity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorMsg("推荐使用QQ浏览器打开",null);
                }
                if (mAppHintDialog == null) {
                    mAppHintDialog = new AppHintDialog(RechargeActivity.this);
                }
                mAppHintDialog.showAppDialog(2, "充值" + (bean.data.amount / Constant.ZJ_PRICE_COMPANY) + "元，请等待打开支付宝");

            } else if (TextUtils.equals("qq_qr", bean.data.channel)) {
                //QQ充值
                Intent qqIntent = new Intent(RechargeActivity.this, WebViewActivity.class);
                qqIntent.putExtra("builderPar", false);
                qqIntent.putExtra("url", bean.data.extra.qr_code);
                qqIntent.putExtra("title", "充值");
                startActivity(qqIntent);

                if (mAppHintDialog == null) {
                    mAppHintDialog = new AppHintDialog(RechargeActivity.this);
                }
                mAppHintDialog.showAppDialog(2, "充值" + (bean.data.amount / Constant.ZJ_PRICE_COMPANY) + "元，请等待打开QQ");
            } else if (TextUtils.equals("wx_h5", bean.data.channel)) {
                //微信 充值
                Intent intent = new Intent(RechargeActivity.this, QrCodeActivity.class);
                intent.putExtra("qrCode", bean.data.extra.pay_url);
                intent.putExtra("amount", bean.data.amount);
                startActivity(intent);
            } else {
                //银联 充值   bean.data.channel  quick_h5
                Intent qIntent = new Intent(RechargeActivity.this, WebViewActivity.class);
                qIntent.putExtra("builderPar", false);
                qIntent.putExtra("url", bean.data.extra.pay_url);
                qIntent.putExtra("title", "充值");
                startActivity(qIntent);

                if (mAppHintDialog == null) {
                    mAppHintDialog = new AppHintDialog(RechargeActivity.this);
                }
                mAppHintDialog.showAppDialog(2, "充值" + (bean.data.amount / Constant.ZJ_PRICE_COMPANY) + "元，请等待打开银联");
            }
        } else {
            String error = bean != null ? bean.desc : "请保持网络畅通";
            showErrorMsg(error, null);
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
    public void onError() {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 验证解绑银行卡提示
     * @param bankCardId
     * @param hint
     */
    private void showUnbindVerifyCard(final String bankCardId,String hint){
        final AppHintDialog appHintDialog = new AppHintDialog(RechargeActivity.this);
        appHintDialog.showAppDialog(11, hint, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()){
                    if (!TextUtils.isEmpty(bankCardId)){
                        mPresenter.unbindingCard(AppApplication.getConfig().getAt_token(),bankCardId);
                    }
                    startActivity(new Intent(RechargeActivity.this, AddBankActivity.class));
                    appHintDialog.dismiss();
                }
            }
        });
    }
}
