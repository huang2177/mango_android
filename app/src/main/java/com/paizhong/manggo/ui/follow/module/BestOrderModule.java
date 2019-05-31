package com.paizhong.manggo.ui.follow.module;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.app.AppApplication;
import com.paizhong.manggo.bean.follow.FollowListBean;
import com.paizhong.manggo.dialog.other.AppHintDialog;
import com.paizhong.manggo.events.CancelFollowEvent;
import com.paizhong.manggo.ui.follow.module.adapter.NewBuyAdapter;
import com.paizhong.manggo.ui.follow.contract.BaseFollowModule;
import com.paizhong.manggo.ui.follow.contract.ItemFollowPresenter;
import com.paizhong.manggo.ui.follow.detail.PersonInfoActivity;
import com.paizhong.manggo.utils.DeviceUtils;
import com.paizhong.manggo.utils.ImageUtils;
import com.paizhong.manggo.utils.toast.AppToast;
import com.paizhong.manggo.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Des: 精华贴  Module
 * Created by huang on 2018/9/13 0013 13:11
 */
public class BestOrderModule extends BaseFollowModule<ItemFollowPresenter> {

    private static final int DEF_VALUE = 100;
    private static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int width;

    private String mProName = "";
    private NewBuyAdapter mAdapter;
    private RecyclerView recycler;
    private List<ViewHolder> mHolders;

    public BestOrderModule(Context context, NewBuyAdapter adapter, RecyclerView recycler) {
        super(context);
        mAdapter = adapter;
        mHolders = new ArrayList<>();
        this.recycler = recycler;
        width = DeviceUtils.getScreenWidth(context);
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        mPresenter.getBestOrderList(mProName);
    }

    @Override
    protected boolean isEmpty() {
        return mAdapter.getFootersCount() == 0;
    }


    @Override
    public void bindBestOrderList(String proName, final List<FollowListBean> list) {
        if (mAdapter == null) {
            return;
        }

        if (!mHolders.isEmpty()) {
            for (int i = 0; i < mHolders.size(); i++) {
                if (list.size() <= i) {
                    mHolders.get(i).view.setVisibility(GONE);
                } else {
                    mHolders.get(i).data = list.get(i);
                    mHolders.get(i).handleBaseInfo();
                    mHolders.get(i).view.setVisibility(VISIBLE);
                }
            }
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            View view = mInflater.inflate(R.layout.item_follow_best_order, recycler, false);
            mHolders.add(new ViewHolder(view, list.get(i), i));
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(MATCH, WRAP);
            params.leftMargin = DeviceUtils.dip2px(mContext, 10);
            params.rightMargin = DeviceUtils.dip2px(mContext, 10);
            view.setLayoutParams(params);
            mAdapter.addFooterView(view);
        }

    }

//    private void addEmptyView() {
//        postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                View view = mInflater.inflate(R.layout.view_empty_layout, null, false);
//                mAdapter.addHeaderView(view);
//            }
//        }, 400);
//    }

    public void setProName(String proName) {
        mProName = proName;
        onRefresh(false);
    }


    class ViewHolder implements View.OnClickListener {
        View view;
        CircleImageView ivHead;
        RelativeLayout mLayoutHead;
        TextView tvTime, tvFollowNum, tvIntegral;
        TextView tvGoodsName, tvType, tvUnitPrice, tvName;
        TextView tvCostPrice, tvServiceCharge, tvFloatPoint, tvCreatePrice;
        TextView tvEarnings, tvFollow, tvFollow1, tvClosePoint, tvCloseTime, tvGetPoint;

        int position;
        FollowListBean data;

        public ViewHolder(View itemView, FollowListBean data, int position) {
            this.data = data;
            this.view = itemView;
            this.position = position;

            initView(itemView);
            handleBaseInfo();
        }

        private void initView(View itemView) {
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
            tvCostPrice = itemView.findViewById(R.id.tv_cost_price);
            tvCreatePrice = itemView.findViewById(R.id.tv_create_price);

            mLayoutHead = itemView.findViewById(R.id.rl_head);
            tvIntegral = itemView.findViewById(R.id.tv_integral);
            tvGetPoint = itemView.findViewById(R.id.tv_get_point);
            tvFollowNum = itemView.findViewById(R.id.tv_follow_num);
            tvCloseTime = itemView.findViewById(R.id.tv_close_time);
            tvClosePoint = itemView.findViewById(R.id.tv_close_point);

            ViewGroup.LayoutParams params = tvCostPrice.getLayoutParams();
            params.width = (int) (width * 0.14);
            tvCostPrice.setLayoutParams(params);
            tvCreatePrice.setLayoutParams(params);
            tvClosePoint.setLayoutParams(params);

            tvFollow.setOnClickListener(this);
            tvFollow1.setOnClickListener(this);
            mLayoutHead.setOnClickListener(this);
        }


        private void handleBaseInfo() {
            tvName.setText(data.nickName);  //昵称
            ImageUtils.display(data.userPic, ivHead, R.mipmap.ic_user_head); //头像
            tvEarnings.setText(Html.fromHtml("周盈利率 <font color = '#008EFF'>" + data.mProfitRate + "%</font>")); //盈利率

            tvTime.setText(data.openTime); //建仓时间
            tvServiceCharge.setText(data.mFee); //手续费
            tvCostPrice.setText(data.mCostPrice); //成本
            tvUnitPrice.setText(data.mProductMsg);//手数
            tvCreatePrice.setText(data.openPrice); //建仓点位
            tvGoodsName.setText(data.mProductName);  //产品名称
            tvClosePoint.setText(data.closePrice);  //平仓点位
            tvCloseTime.setText(data.closeTime);  //平仓时间

            tvIntegral.setText(data.mScore); // 积分
            tvFollowNum.setText(data.mFellowCount); //几人跟买
            tvFollow1.setText(String.valueOf(data.concernCount)); //关注数
            tvFollow1.setCompoundDrawablesWithIntrinsicBounds(data.isConcern == 0
                            ? R.mipmap.ic_followed1 : R.mipmap.ic_follow1
                    , 0
                    , 0
                    , 0); //是否关注

            //买涨买跌（0 涨  1跌）
            tvType.setSelected(data.flag == 0);
            tvType.setText(data.flag == 0 ? R.string.text9 : R.string.text10);

            int getPoint = Integer.parseInt(data.closePrice) - Integer.parseInt(data.openPrice);
            tvGetPoint.setText(String.valueOf(Math.abs(getPoint)));  //捕获点位
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(data.phone) || data.isRanking == 0 || !mActivity.login()) {
                return;
            }
            switch (v.getId()) {
                case R.id.tv_follow:
                case R.id.tv_follow1:
                    addConcern();
                    break;
                case R.id.rl_head:
                    startToDetail();
                    break;
            }
        }

        private void startToDetail() {
            Intent intent = new Intent(mContext, PersonInfoActivity.class);
            intent.putExtra("phone", data.phone);
            mContext.startActivity(intent);
        }

        private void addConcern() {
            String phone = AppApplication.getConfig().getMobilePhone();
            if (TextUtils.equals(data.phone, phone)) {
                AppToast.show("自己不能关注自己！");
                return;
            }
            if (data.isConcern == 0) {
                new AppHintDialog(mActivity).showAppDialog(3, position + DEF_VALUE);
            } else {
                mPresenter.addConcern(data.phone, data.isConcern, position);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CancelFollowEvent event) {
        int position = event.position - DEF_VALUE;
        if (position < 0) {
            return;
        }
        FollowListBean data = mHolders.get(position).data;
        mPresenter.addConcern(data.phone, data.isConcern, position);
    }

    @Override
    public void bindConcern(int position, int isConcern) {
        TextView textView = mHolders.get(position).tvFollow1;
        int count = Integer.parseInt(textView.getText().toString());
        if (count > 0 || count == 0 && isConcern == 0) {
            textView.setText(String.valueOf(isConcern == 0 ? (count + 1) : (count - 1)));
            textView.setCompoundDrawablesWithIntrinsicBounds(isConcern == 0
                            ? R.mipmap.ic_followed1 : R.mipmap.ic_follow1
                    , 0
                    , 0
                    , 0); //是否关注

            mHolders.get(position).data.isConcern = isConcern;
            showErrorMsg(isConcern == 0 ? "关注成功" : "取消关注成功", null);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }
}