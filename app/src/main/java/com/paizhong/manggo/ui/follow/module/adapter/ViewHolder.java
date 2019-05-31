package com.paizhong.manggo.ui.follow.module.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.events.CancelFollowEvent;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.TradeUtils;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.CircleImageView;
import com.paizhong.manggo.widget.RefreshView;

import org.greenrobot.eventbus.EventBus;

/**
 * Des:
 * Created by huang on 2018/9/4 0004 15:43
 */
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CircleImageView ivHead;
    public RelativeLayout mLayoutHead;
    public RefreshView tvGoodsName, tvType, tvUnitPrice;
    public RefreshView tvName, tvEarnings, tvTime2, tvFollow, tvFollow1;
    public RefreshView tvCostPrice, tvServiceCharge, tvFloatPoint, tvCreatePrice;
    public RefreshView tvTime, tvFollowNum, tvFollowOrder, tvIntegral;
    public RefreshView tvStopUp, tvStopDown, tvMarket;

    private int position;
    private Context mContext;
    private FollowListBean data;

    public ViewHolder(View itemView) {
        super(itemView);
        initView();
        setListener();
    }

    private void initView() {
        mContext = itemView.getContext();

        ivHead = itemView.findViewById(R.id.item_list_iv);

        tvName = itemView.findViewById(R.id.item_list_tv_name);
        tvEarnings = itemView.findViewById(R.id.tv_week_earnings);

        tvType = itemView.findViewById(R.id.tv_buy_type);
        tvFollow = itemView.findViewById(R.id.tv_follow);
        tvFollow1 = itemView.findViewById(R.id.tv_follow1);
        tvFloatPoint = itemView.findViewById(R.id.tv_point);
        tvUnitPrice = itemView.findViewById(R.id.tv_unit_price);
        tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
        tvServiceCharge = itemView.findViewById(R.id.tv_service_charge);

        tvTime = itemView.findViewById(R.id.tv_time);
        tvTime2 = itemView.findViewById(R.id.tv_time2);
        tvMarket = itemView.findViewById(R.id.tv_market);
        tvStopUp = itemView.findViewById(R.id.tv_stop_up);
        tvStopDown = itemView.findViewById(R.id.tv_stop_down);
        tvCostPrice = itemView.findViewById(R.id.tv_cost_price);
        tvCreatePrice = itemView.findViewById(R.id.tv_create_price);

        tvIntegral = itemView.findViewById(R.id.tv_integral);
        tvFollowNum = itemView.findViewById(R.id.tv_follow_num);
        tvFollowOrder = itemView.findViewById(R.id.tv_follow_order);
        mLayoutHead = itemView.findViewById(R.id.rl_head);
    }

    private void setListener() {
        tvFollow.setOnClickListener(this);
        tvMarket.setOnClickListener(this);
        tvFollow1.setOnClickListener(this);
        mLayoutHead.setOnClickListener(this);
        tvFollowOrder.setOnClickListener(this);
    }

    public void setFollowBean(FollowListBean followBean, int position) {
        data = followBean;
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        if (DeviceUtils.isFastDoubleClick() || !((MainActivity) mContext).login()) {
            return;
        }
        if (data == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_follow_order:
                createOrder(3);
                break;
            case R.id.tv_market:
                createOrder(4);
                break;
            case R.id.tv_follow:
            case R.id.tv_follow1:
                follow();
                break;
            case R.id.rl_head:
                startToDetail();
                break;
        }
    }

    /**
     * 下单 或者 查看详情
     *
     * @param type 3跟单下单 4跟单行情
     */
    private void createOrder(int type) {
        if (TextUtils.equals(AppApplication.getConfig().getMobilePhone(), data.phone)) {
            AppToast.show("无法跟买自己的单子！");
            return;
        }
        ((MainActivity) mContext).placeOrder().placeOrderFollow(type
                , String.valueOf(data.typeId)
                , (data.flag == 0)
                , TradeUtils.getNum(data.amount, data.quantity)
                , data.stockIndex
                , data.changeValue
                , data.closingPrice
                , Integer.parseInt(data.profitLimit)
                , Integer.parseInt(data.lossLimit)
                , data.phone
                , data.orderNo);
    }

    /**
     * 跳转到详情页面
     */
    private void startToDetail() {
        Intent intent = new Intent(mContext, PersonInfoActivity.class);
        intent.putExtra("phone", data.phone);
        mContext.startActivity(intent);
    }

    /**
     * 关注
     */
    private void follow() {
        if (TextUtils.equals(data.phone, AppApplication.getConfig().getMobilePhone())) {
            AppToast.show("自己不能关注自己！");
            return;
        }
        EventBus.getDefault().post(new CancelFollowEvent(1, position));
    }
}
