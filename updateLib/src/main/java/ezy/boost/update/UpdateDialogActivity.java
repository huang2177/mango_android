package ezy.boost.update;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * ========================================

 * ========================================
 */
public class UpdateDialogActivity extends Activity implements View.OnClickListener{

    private TextView jjdxm_update_title;
    private TextView jjdxm_update_content;

    private Button jjdxm_update_id_ok;
    private Button jjdxm_update_id_cancel;

    private DefaultPromptClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_update_dialog);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        listener = new DefaultPromptClickListener(true, new DefaultPromptClickListener.CallBack() {
            @Override
            public void dismiss() {
                finish();
            }
        });

        jjdxm_update_title = (TextView) findViewById(R.id.jjdxm_update_title);
        jjdxm_update_content = (TextView) findViewById(R.id.jjdxm_update_content);
        if (title != null && !title.isEmpty()){
            jjdxm_update_title.setText(title);
        }
        if (content != null && !content.isEmpty()){
            jjdxm_update_content.setText(content);
        }

        jjdxm_update_id_ok = (Button) findViewById(R.id.jjdxm_update_id_ok);
        jjdxm_update_id_cancel = (Button) findViewById(R.id.jjdxm_update_id_cancel);
        View alphaView = findViewById(R.id.view_alpha);
        alphaView.setAlpha(0.9F);

        jjdxm_update_id_ok.setOnClickListener(this);
        jjdxm_update_id_cancel.setOnClickListener(this);
        if (UpdateHelper.getInstance().isForced()){//强制更新禁止取消跳过
            jjdxm_update_id_cancel.setEnabled(false);
        }
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 341);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.jjdxm_update_id_ok) {
            listener.onClick(DialogInterface.BUTTON_POSITIVE);
        } else if (id == R.id.jjdxm_update_id_cancel) {
            listener.onClick(DialogInterface.BUTTON_NEGATIVE);
        }
    }
    /**
     * 申请动态权限的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==341){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

//                finish();
            }

        }
    }

    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName(Context context) {
        String versionName = "0.0.0";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            //Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
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
