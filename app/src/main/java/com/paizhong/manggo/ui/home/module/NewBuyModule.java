package com.paizhong.manggo.ui.home.module;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.home.NewBuyInfoBean;
import com.paizhong.manggo.dialog.placeorder.PlaceOrderDialog;
import com.paizhong.manggo.ui.home.contract.BaseHomeModule;
import com.paizhong.manggo.ui.home.contract.HandlerHelper;
import com.paizhong.manggo.ui.home.contract.ItemHomePresenter;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.RefreshView;

import butterknife.BindView;

/**
 * Des: 首页最新跟买 Adapter
 * Created by huang on 2018/8/17 0017 11:26
 */
public class NewBuyModule extends BaseHomeModule<ItemHomePresenter> implements HandlerHelper.OnExecuteListener
        , View.OnClickListener {

    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.tv_name)
    RefreshView tvName;
    @BindView(R.id.tv_time)
    RefreshView tvTime;
    @BindView(R.id.iv_head)
    CircleImageView mImageHead;
    @BindView(R.id.tv_buy_type)
    RefreshView tvBuyType;
    @BindView(R.id.tv_follow_buy)
    RefreshView tvFollowBuy;
    @BindView(R.id.tv_goods_name)
    RefreshView tvProName;
    @BindView(R.id.tv_unit_price)
    RefreshView tvUnitPrice;
    @BindView(R.id.tv_float_point)
    RefreshView tvFloatPoint;
    @BindView(R.id.tv_create_point)
    RefreshView tvCreatePoint;
    @BindView(R.id.tv_week_earnings)
    RefreshView tvProfit;
    @BindView(R.id.tv_follow_num)
    RefreshView tvFollowNum;
    @BindView(R.id.tv_follow_score)
    RefreshView tvFollowScore;

    private NewBuyInfoBean mBean;
    private HandlerHelper mHelper;
    private int mRedColor, mGreenColor, mGrayColor;

    protected int audited; // -1 审核中  1 已审核

    public NewBuyModule(Context context, HandlerHelper helper) {
        super(context);
        mHelper = helper;
        initView();
    }

    private void initView() {
        setVisibility(GONE);
        this.mRedColor = ContextCompat.getColor(mContext, R.color.color_F74F54);
        this.mGrayColor = ContextCompat.getColor(mContext, R.color.color_878787);
        this.mGreenColor = ContextCompat.getColor(mContext, R.color.color_1AC47A);

        tvMore.setOnClickListener(this);
        tvFollowBuy.setOnClickListener(this);
    }

    @Override
    public void setAuditing(boolean auditing) {
        super.setAuditing(auditing);
        audited = auditing ? -1 : 1;
        onRefresh(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_home_new_buy;
    }


    @Override
    protected boolean isEmpty() {
        return (mBean == null || getVisibility() == GONE);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        execute(true);
    }

    @Override
    public void execute(boolean args) {
        if (audited == 1) mPresenter.getLastFellowInfo();
    }

    @Override
    public void bindLastFellowInfo(NewBuyInfoBean bean) {
        this.mBean = bean;
        mHelper.execute(this);

        handleBaseInfo(bean);
        setVisibility(VISIBLE);
    }

    @Override
    public void bindFellowInfoEmpty() {
        setVisibility(GONE);
        mHelper.cancelExecute(this);
    }

    @Override
    public void refreshDialog(String maxId, String stockIndex, String changeValue, String time) {
        if (!mActivity.isPlaceOrderEmpty()) {
            PlaceOrderDialog dialog = mActivity.placeOrder();
            if (dialog.isShowing() && TextUtils.equals(dialog.getProductID(), maxId)) {
                dialog.refreshPlaceOrderDialog(stockIndex, changeValue, time);
            }
        }
    }

    private void handleBaseInfo(NewBuyInfoBean data) {
        tvName.addText(data.nickName);         //昵称
        tvTime.addText(data.mCurrentTime);     //时间
        tvProName.addText(data.mProductName);  //产品名称
        tvUnitPrice.addText(data.mProductMsg); //手数
        tvBuyType.setSelected(data.flag == 0); //买涨买跌（0 涨  1跌）
        tvBuyType.addText(data.flag == 0 ? R.string.text9 : R.string.text10);
        tvProfit.addText(Html.fromHtml(data.mProfitRate + "%")); //盈利率
        tvCreatePoint.addText(Html.fromHtml("<font color='#B2B2B2'>建仓点位 </font>" + data.openPrice)); //建仓点位
        tvFloatPoint.addText(Html.fromHtml("<font color='#B2B2B2'>浮动点位 </font>" + data.floatPrice)); //浮动点位
        tvFollowNum.addText(Html.fromHtml("跟买人数 <font color='#008EFF'>" + data.fellowCount + "</font>")); //跟买人数
        tvFollowScore.addText(Html.fromHtml("返还积分 <font color='#008EFF'>" + data.mScore + "</font>")); //积分

        ImageUtils.display(data.userPic, mImageHead, R.mipmap.ic_user_head); //头像
        if (data.mFloatPrice > 0) {
            tvFloatPoint.addTextColor(mRedColor);
        } else if (data.mFloatPrice < 0) {
            tvFloatPoint.addTextColor(mGreenColor);
        } else {
            tvFloatPoint.addTextColor(mGrayColor);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_more:
                MainActivity activity = (MainActivity) mContext;
                activity.selectPage(MainActivity.FOLLOW_PAGE, 0);
                break;
            case R.id.tv_follow_buy:
                createOrder();
                break;
        }
    }

    /**
     * 跟买下单
     */
    private void createOrder() {
        if (TextUtils.equals(AppApplication.getConfig().getMobilePhone(), mBean.phone)) {
            AppToast.show("无法跟买自己的单子！");
            return;
        }
        if (mActivity.login()) {
            mActivity.placeOrder().placeOrderFollow(3
                    , String.valueOf(mBean.typeId)
                    , mBean.flag == 0
                    , TradeUtils.getNum(mBean.amount, mBean.quantity)
                    , mBean.stockIndex
                    , mBean.changeValue
                    , mBean.closingPrice
                    , Integer.parseInt(mBean.profitLimit)
                    , Integer.parseInt(mBean.lossLimit)
                    , mBean.phone
                    , mBean.orderNo);
        }
    }

    @Override
    public void onDestroy() {
        if (!mActivity.isPlaceOrderEmpty()) {
            PlaceOrderDialog dialog = mActivity.placeOrder();
            dialog.stopRun();
        }
        super.onDestroy();
    }
}
