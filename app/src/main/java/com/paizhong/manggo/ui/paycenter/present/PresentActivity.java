package com.paizhong.manggo.ui.paycenter.present;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.bean.zj.BankCardZjListBean;
import com.paizhong.manggo.bean.zj.BankUnbCardZJBean;
import com.paizhong.manggo.bean.zj.UserZJBean;
import com.paizhong.manggo.bean.zj.ZJBaseBean;
import com.paizhong.manggo.config.Constant;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.ui.paycenter.addbank.AddBankActivity;
import com.paizhong.manggo.utils.CheckUtils;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.NumberUtil;
import com.paizhong.manggo.widget.CountDownTextView;

import butterknife.BindView;

/**
 * 提现
 * Created by zab on 2018/4/8 0008.
 */

public class PresentActivity extends BaseActivity<PresentPresenter> implements PresentContract.View{
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.tv_bottom_hint)
    TextView tvBottomHint;
    @BindView(R.id.tv_present)
    TextView tvPresent;

    private View footerView;
    private TextView tvMaxMoney;
    private ImageView ivWen;
    private EditText etMoney;
    private TextView tvAllPresent;
    private EditText etSmsedCode;
    private CountDownTextView btnSendCode;
    private EditText etPwd;
    private TextView tvPresentHint;

    private PresentAdapter mPresentAdapter;
    private double mBallance;


    @Override
    public int getLayoutId() {
        return R.layout.activity_present;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }


    private PopupWindow popupWindow;
    private void createrPopupWind(){
        popupWindow = new PopupWindow(PresentActivity.this);
        View rootView = View.inflate(PresentActivity.this, R.layout.popupwind_present_layout, null);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        ColorDrawable drawable = new ColorDrawable(PresentActivity.this.getResources().getColor(android.R.color.transparent));
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.areward_animation);
        popupWindow.setContentView(rootView);
        popupWindow.setOutsideTouchable(true);
    }

    //设置字体样式
    private void setTextFonts(EditText editText) {
        //得到AssetManager
        AssetManager mgr = getAssets();
        if (mgr !=null){
            //根据路径得到Typeface
            Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
            if (tf !=null){
                //设置字体
                editText.setTypeface(tf);
            }
        }
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, "提现");
        footerView = LayoutInflater.from(PresentActivity.this).inflate(R.layout.footer_present_layout, null);
        View headView = LayoutInflater.from(PresentActivity.this).inflate(R.layout.headview_present_layout,null);
        tvMaxMoney = headView.findViewById(R.id.tv_max_money);
        etMoney = headView.findViewById(R.id.et_money);
        etSmsedCode = headView.findViewById(R.id.et_smsed_code);
        tvAllPresent = headView.findViewById(R.id.tv_all_present);
        ivWen = headView.findViewById(R.id.iv_wen);
        btnSendCode = headView.findViewById(R.id.btn_sendCode);
        etPwd = headView.findViewById(R.id.et_pwd);
        tvPresentHint = headView.findViewById(R.id.tv_present_hint);
        tvAllPresent.setEnabled(false);
        setTextFonts(etMoney);
        setTextFonts(etPwd);
        setTextFonts(etSmsedCode);

        setMaxMoney("---");
        createrPopupWind();
        mPresentAdapter = new PresentAdapter(PresentActivity.this);
        mListView.addHeaderView(headView);
        mListView.addFooterView(footerView);
        mListView.setAdapter(mPresentAdapter);
        setListener();

        setPresentFee(etMoney.getText().toString());
    }


    private void setListener() {
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (!DeviceUtils.isFastDoubleClick()){
                     startActivity(new Intent(PresentActivity.this, AddBankActivity.class));
                 }
            }
        });

        //删除事件
        mPresentAdapter.setOnDelBankCardListener(new PresentAdapter.OnDelBankCardListener() {
            @Override
            public void onDelBankCard(final String id) {
                showUnbindCard(id,"您确认需要解绑该银行卡吗？");
            }
        });

        ivWen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAsDropDown(view);
            }
        });


        //发送验证码
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()){
                    btnSendCode.start();
                    mPresenter.getUserZjSmsCode(AppApplication.getConfig().getMobilePhone(),"withdraw");
                }
            }
        });


        //全部提现
        tvAllPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                etMoney.setText(mBallance+"");
            }
        });

        //提交
        tvPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String token, String smsCode, String password, String amount, String account_id
                if (DeviceUtils.isFastDoubleClick()){
                    return;
                }
                if (TextUtils.isEmpty(mPresentAdapter.mBankID)){
                    showErrorMsg("请选择提现账户",null);
                    return;
                }
                double money = Double.parseDouble(etMoney.getText().toString());
                if (money > mBallance){
                    showErrorMsg("提现金额不能大于余额",null);
                    return;
                }
                if (money < 50){
                    showErrorMsg("提现金额必须大于等于50元",null);
                    return;
                }
                mPresenter.getVerifyCard(AppApplication.getConfig().getAt_token(),mPresentAdapter.mRealName,mPresentAdapter.mBankNum,mPresentAdapter.mBankMobile,mPresentAdapter.mIdCard);
            }
        });


        //金额
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                 String money = etMoney.getText().toString();
                 setPresentFee(money);
                 setEnabled();
            }
        });

        etSmsedCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                setEnabled();
            }
        });

        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                setEnabled();
            }
        });
    }



    private void setPresentFee(String money){
        if (CheckUtils.isDouble(money) || CheckUtils.isInt(money)){
            double v = Double.parseDouble(money);
            double fee = v <= 300 ? 3 :(v <=3000 ? NumberUtil.getDouble(NumberUtil.multiply(v, 0.01)) : 30);
            tvPresentHint.setText(String.format("提现手续费:%s元,24小时内到账,节假日顺延", fee));
        }else {
            tvPresentHint.setText("提现手续费:3元,最高30元,24小时内到账,节假日顺延");
        }
    }


    @Override
    protected void onResume() {
         super.onResume();
         mPresenter.getUserZjInfo(AppApplication.getConfig().getAt_token(),AppApplication.getConfig().getAt_userId());
         mPresenter.getBankcardZjList(AppApplication.getConfig().getAt_token());
    }


    //查询余额
    @Override
    public void bindZjUserInfo(UserZJBean userZJBean) {
        if (userZJBean !=null && userZJBean.isSuccess()){
            mBallance = NumberUtil.divide(userZJBean.data.ballance, Constant.ZJ_PRICE_COMPANY);
            tvAllPresent.setEnabled(true);
            setMaxMoney(String.valueOf(mBallance));
        }
    }


    /**
     * 校验是否已实名认证
     * @param verifyCard true 已认证 false 没有
     */
    @Override
    public void bindVerifyCard(boolean verifyCard) {
         if (verifyCard){
             String smsCode = etSmsedCode.getText().toString();
             String pwd = etPwd.getText().toString();
             double money = Double.parseDouble(etMoney.getText().toString());
             String paramMoney = String.valueOf(NumberUtil.multiply(money,Constant.ZJ_PRICE_COMPANY));
             //提现
             mPresenter.getWithdrawMoney(AppApplication.getConfig().getAt_token(),smsCode,pwd,paramMoney,mPresentAdapter.mBankID,mPresentAdapter.mBankNum,AppApplication.getConfig().getAt_secret_access_key());
         }else {
             stopLoading();
             showUnbindVerifyCard(mPresentAdapter.mBankID,"您目前还没实名认证，请前往实名认证？");
         }
    }


    //提现
    @Override
    public void bindZJWithdrawMoney(String amount,ZJBaseBean baseBean) {
         if (baseBean !=null && baseBean.isSuccess()){
             showErrorMsg(baseBean.desc,null);
             mBallance = NumberUtil.divide((NumberUtil.multiply(mBallance,Constant.ZJ_PRICE_COMPANY) - Double.parseDouble(amount)),Constant.ZJ_PRICE_COMPANY);
             setMaxMoney(String.valueOf(mBallance));
             new AppHintDialog(mActivity).showAppDialog(4,null);
         }else {
             showErrorMsg(baseBean !=null ? baseBean.desc : "提现失败",null);
         }
    }


    //绑卡列表
    @Override
    public void bindBankcardZjList(BankCardZjListBean listBean) {
        mPresentAdapter.notifyDataChanged(listBean);
    }

    //解绑 银行卡
    @Override
    public void bindUnbindingCard(boolean showError,String bankCardId,BankUnbCardZJBean unbCardZJBean) {
        if (unbCardZJBean !=null && unbCardZJBean.isSuccess()){
            unbindingCard(bankCardId);
            if (showError){
                showErrorMsg("解绑成功",null);
            }
        }else {
            if (showError){
                String error = unbCardZJBean !=null ? unbCardZJBean.desc : "解绑失败";
                showErrorMsg(error,null);
            }
        }
    }

    /**
     * 解绑银行卡成功后移除对应的银行卡
     * @param bankCardId 银行卡
     */
    private void  unbindingCard(String bankCardId){
        if (mPresentAdapter !=null&& mPresentAdapter.getList() !=null && mPresentAdapter.getList().size() >0){
            for (BankCardZjListBean.DataBean dataBean : mPresentAdapter.getList()){
                if (TextUtils.equals(dataBean.id,bankCardId)){
                    if (TextUtils.equals(dataBean.id,mPresentAdapter.mBankID)){
                        mPresentAdapter.clear();
                    }
                    mPresentAdapter.getList().remove(dataBean);
                    mPresentAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }


    /**
     * 校验提现信息
     */
    private void setEnabled(){
        String money = etMoney.getText().toString();
        String smsCode = etSmsedCode.getText().toString();
        String pwd = etPwd.getText().toString();

        if ((CheckUtils.isDouble(money) || CheckUtils.isInt(money)) &&
                !TextUtils.isEmpty(smsCode) && smsCode.length() == 6
                && !TextUtils.isEmpty(pwd) && pwd.length() == 6
                && !TextUtils.isEmpty(mPresentAdapter.mBankID)
                && Double.parseDouble(money) > 0){
            tvPresent.setEnabled(true);
        }else {
            tvPresent.setEnabled(false);
        }
    }



    /**
     * 解绑银行卡提示
     * @param bankCardId
     * @param hint
     */
    private void showUnbindCard(final String bankCardId,String hint){
        final AppHintDialog appHintDialog = new AppHintDialog(PresentActivity.this);
        appHintDialog.showAppDialog(5, hint, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.unbindingCard(true,AppApplication.getConfig().getAt_token(),bankCardId);
                appHintDialog.dismiss();
            }
        });
    }


    /**
     * 验证解绑银行卡提示
     * @param bankCardId
     * @param hint
     */
    private void showUnbindVerifyCard(final String bankCardId,String hint){
        final AppHintDialog appHintDialog = new AppHintDialog(PresentActivity.this);
        appHintDialog.showAppDialog(11, hint, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DeviceUtils.isFastDoubleClick()){
                    mPresenter.unbindingCard(false,AppApplication.getConfig().getAt_token(),bankCardId);
                    Intent intent = new Intent(PresentActivity.this, AddBankActivity.class);
                    intent.putExtra("mBankNum",mPresentAdapter.mBankNum);
                    intent.putExtra("mRealName",mPresentAdapter.mRealName);
                    intent.putExtra("mBankMobile",mPresentAdapter.mBankMobile);
                    intent.putExtra("mIdCard",mPresentAdapter.mIdCard);
                    startActivity(intent);
                    appHintDialog.dismiss();
                }
            }
        });
    }


    //设置提示文字样式
    private void setMaxMoney(String ballance){
        tvMaxMoney.setText(Html.fromHtml("(可提现金额 <font color = '#008EFF'>" + ballance + "</font> 元,最低提现金额<font color = '#008EFF'> 50 </font>元)"));
    }

    @Override
    protected void onDestroy() {
        popupWindow = null;
        super.onDestroy();
    }
}
