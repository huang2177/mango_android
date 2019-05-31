package ezy.boost.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by LiuZhen on 2017/8/18.
 */

public class UpdateHelper {

    private static class SingletonHolder {
        private static final UpdateHelper INSTANCE = new UpdateHelper();
    }
    private UpdateHelper(){}
    public static UpdateHelper getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private IUpdateAgent agent;
    private boolean isForced;

    /**
     * @param isForced 是否后台下载
     * @param desc 更新内容
     * @param downloadUrl 下载地址
     */
    public void update(Context context,boolean isForced,String title,String desc,int code,
                        String versionName, String downloadUrl, String md5) {
        this.isForced = isForced;
        UpdateInfo info = new UpdateInfo();
        // 是否有新版本
        info.hasUpdate = true;
        info.title = title;
        //更新内容
        info.updateContent = desc;
        info.versionCode = code;
        info.versionName = versionName;
        info.url = downloadUrl;
        info.md5 = md5;//md5效验
        if (TextUtils.isEmpty(info.md5)){
            info.md5 = "test";//md5字段不能为空，必须要有初始值
        }

        info.size = 0;
        info.isIgnorable = false;
        if (isForced) {
            info.isSilent = false;// 是否静默下载：有新版本时不提示直接下载
            info.isForce = true;
        }else {
            info.isSilent = true;
            info.isForce = false;
        }
        UpdateManager.create(context)
                .setManual(true)// 在设置界面点击检查更新
                .setNotifyId(998)//notify唯一标识
                .setUserInfo(info)
                .check();
    }

    public IUpdateAgent getAgent() {
        return agent;
    }

    public void setAgent(IUpdateAgent agent) {
        this.agent = agent;
    }

    public boolean isForced(){
        return isForced;
    }
}
