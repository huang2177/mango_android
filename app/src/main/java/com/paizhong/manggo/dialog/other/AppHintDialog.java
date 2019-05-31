package com.paizhong.manggo.dialog.other;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paizhong.manggo.R;
import com.paizhong.manggo.base.BaseActivity;
import com.paizhong.manggo.dialog.recharge.RechargeDialog;
import com.paizhong.manggo.events.BaseUIRefreshEvent;
import com.paizhong.manggo.events.CancelFollowEvent;
import com.paizhong.manggo.ui.main.MainActivity;
import com.paizhong.manggo.utils.DeviceUtils;

import org.greenrobot.eventbus.EventBus;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * Created by zab on 2018/8/30 0030.
 */
public class AppHintDialog extends Dialog {
    private BaseActivity mContext;
    private ImageView ivImg;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvBtnLeft;
    private TextView tvBtnRight;
    private View llContentSco;
    private TextView tvContentSco1;
    private TextView tvContentSco2;

    public AppHintDialog(@NonNull BaseActivity context) {
        super(context, R.style.center_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_app_hint_layout);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert  manage !=null;
        manage.getDefaultDisplay().getMetrics(dm);

        params.width = mContext.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE ? dm.heightPixels : dm.widthPixels;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindow.setAttributes(params);

        ivImg = findViewById(R.id.iv_img);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        llContentSco = findViewById(R.id.ll_content_sco);
        tvContentSco1 = findViewById(R.id.tv_content_sco1);
        tvContentSco2 = findViewById(R.id.tv_content_sco2);
        tvBtnLeft = findViewById(R.id.tv_btn_left);
        tvBtnRight = findViewById(R.id.tv_btn_right);
    }


    public void showAppDialog(int mType) {
        showAppDialog(mType, 0, null, null, null, null);
    }

    public void showAppDialog(int mType, int integral) {
        showAppDialog(mType, integral, null, null, null, null);
    }

    public void showAppDialog(int mType, String content) {
        showAppDialog(mType, -1, content);
    }

    public void showAppDialog(int mType, int integral, String content) {
        showAppDialog(mType, integral, null, content, null, null);
    }

    public void showAppDialog(int mType, String content, View.OnClickListener btnRightListener) {
        showAppDialog(mType, -1, null, content, null, btnRightListener);
    }

    /**
     * mType  0 余额不足    2支付提示  3 取消关注  4 提现提示 5银行卡解绑提示  6挂单成功提示 7修改挂单提示  8挂单撤单
     * *      9 dialog页面充值完成  10禁言  11银行卡解绑提示（与校验）
     *
     * @param mType
     */
    public void showAppDialog(int mType, final int integral, String couIntegral, final String content, View.OnClickListener btnLeftListener, View.OnClickListener btnRightListener) {
        show();
        if (mType == 0) {
            //余额不足
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_pay_error);
            tvTitle.setText("余额不足");
            tvContent.setText("您的余额不足，请尽快充值");
            tvBtnLeft.setText("关闭");
            tvBtnRight.setText("去充值");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                }
            });
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RechargeDialog(mContext).getData();
                    dismiss();
                }
            });
        } else if (mType == 2) {
            //支付完成操作（充值页面跳转）
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_position_succ);
            tvTitle.setText("温馨提示");
            tvContent.setText(content);
            tvBtnLeft.setText("支付失败");
            tvBtnRight.setText("支付完成");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("page", MainActivity.TRADE_PAGE);
                    //intent.putExtra("smPage",1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    dismiss();
                }
            });
        } else if (mType == 3) {
            //取消关注提示（跟买）
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_pay_error);
            tvTitle.setText("确定取消关注吗？");
            tvContent.setText("取消关注后您将无法及时获取该牛人发布的跟买");
            tvContent.setPadding(DeviceUtils.dip2px(mContext, 10), 0, DeviceUtils.dip2px(mContext, 10), 0);
            tvBtnLeft.setText("关闭");
            tvBtnRight.setText("确定");
            tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_878787));
            tvBtnLeft.setTextColor(ContextCompat.getColor(mContext, R.color.color_ffffff));
            tvBtnRight.setTextColor(ContextCompat.getColor(mContext, R.color.color_b2b2b2));
            tvBtnLeft.setBackgroundResource(R.drawable.bg_shape_pingcang);
            tvBtnRight.setBackgroundResource(R.drawable.bg_capital_withdrawals);
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new CancelFollowEvent(0, integral));
                    dismiss();
                }
            });
        } else if (mType == 4) {
            //提现成功
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_position_succ);
            tvTitle.setText("提现申请成功");
            tvContent.setText("本次提现申请已成功提交，请等待审核");
            tvBtnLeft.setText("关闭");
            tvBtnLeft.setVisibility(View.GONE);
            tvBtnRight.setText("知道了");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("page", MainActivity.TRADE_PAGE);
                    intent.putExtra("smPage", 3);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    dismiss();
                }
            });
        } else if (mType == 5) {
            //银行卡解绑提示
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_pay_error);
            tvTitle.setText("温馨提示");
            tvContent.setText(content);
            tvBtnLeft.setText("取消");
            tvBtnRight.setText("确认");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            if (btnRightListener != null) {
                tvBtnRight.setOnClickListener(btnRightListener);
            }
        } else if (mType == 6 || mType == 7) {
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_position_succ);
            tvTitle.setText(mType == 6 ? "挂单已生成" : "修改挂单成功");
            tvContent.setText(content);
            tvBtnLeft.setText("关闭");
            tvBtnRight.setText("查看挂单");

            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (mType == 6) {
                tvBtnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("page", MainActivity.TRADE_PAGE);
                        intent.putExtra("smPage", 2);
                        mContext.startActivity(intent);
                        dismiss();
                    }
                });
            } else {
                tvBtnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }

        } else if (mType == 8) {
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_position_succ);
            tvTitle.setText("您确认要撤销该订单？");
            tvBtnLeft.setText("取消");
            tvBtnRight.setText("确认");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            if (btnRightListener != null) {
                tvBtnRight.setOnClickListener(btnRightListener);
            }
        } else if (mType == 9) {
            //支付完成操作（充值页面跳转）
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_position_succ);
            tvTitle.setText("温馨提示");
            tvContent.setText(content);
            tvBtnLeft.setText("支付失败");
            tvBtnRight.setText("支付完成");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            tvBtnRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new BaseUIRefreshEvent(2));
                    dismiss();
                }
            });
        }else if (mType == 10) {
            //银行卡解绑提示
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_pay_error);
            tvTitle.setText("禁言提示");
            tvContent.setText(content);
            tvBtnLeft.setText("取消");
            tvBtnRight.setText("确认");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            if (btnRightListener != null) {
                tvBtnRight.setOnClickListener(btnRightListener);
            }
        }else if (mType == 11) {
            //银行卡解绑提示
            tvContent.setVisibility(View.VISIBLE);
            ivImg.setImageResource(R.mipmap.ic_app_pay_error);
            tvTitle.setText("温馨提示");
            tvContent.setText(content);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvContent.getLayoutParams();
            layoutParams.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10dp),
                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_6dp),
                    mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10dp),
                    0);
            tvBtnLeft.setText("取消");
            tvBtnRight.setText("前往");
            tvBtnLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            if (btnRightListener != null) {
                tvBtnRight.setOnClickListener(btnRightListener);
            }
        }
    }
}
