package ezy.boost.update;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DownloadDialogActivity extends Activity {

    private ProgressBar pgBar;
    private TextView tvPg;
    private Context mContext;
    private LocalBroadcastManager broadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        setContentView(R.layout.custom_download_dialog);

        pgBar = (ProgressBar) findViewById(R.id.jjdxm_update_progress_bar);
        tvPg = (TextView) findViewById(R.id.jjdxm_update_progress_text);
        broadcast();
    }

    /**
     * 刷新下载进度
     */
    private void updateProgress(long percent) {
        if (tvPg != null) {
            tvPg.setText(percent + "%");
            pgBar.setProgress((int) percent);
        }
        if (percent >= 100) {
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            initLayout();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /*
                 *  计算高度，做适配
                  */
    private void initLayout() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        //获取头部图片的高度
        int measuredHeight = imageView.getMeasuredHeight();
        //设置布局的marginTop
        LinearLayout dialogLayout = (LinearLayout) findViewById(R.id.popup_anima);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dialogLayout.getLayoutParams();
        //获取view的margin设置参数
        layoutParams.setMargins(layoutParams.leftMargin, (measuredHeight / 2), layoutParams.rightMargin, layoutParams.bottomMargin);
        dialogLayout.setLayoutParams(layoutParams);

        TextView title = (TextView) findViewById(R.id.title);
        LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) title.getLayoutParams();
        //获取view的margin设置参数
        titleParams.setMargins(titleParams.leftMargin, (measuredHeight / 2), titleParams.rightMargin, titleParams.bottomMargin);
        title.setLayoutParams(titleParams);
    }

    BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /** 刷新数据 */
            long type = intent.getLongExtra("type", 0);
            updateProgress(type);
        }
    };
    /**
     * 注册广播
     */
    private void broadcast() {
        broadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter
                .addAction("ezy.boost.update.downloadBroadcast");

        broadcastManager.registerReceiver(mItemViewListClickReceiver,
                intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mItemViewListClickReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (UpdateHelper.getInstance().isForced()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
