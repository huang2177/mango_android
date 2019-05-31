/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ezy.boost.update;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import java.io.File;

class UpdateAgent implements ICheckAgent, IUpdateAgent, IDownloadAgent{

    private Context mContext;
    private String mUrl;
    private File mTmpFile;
    private File mApkFile;
    private boolean mIsManual = false;
    private boolean mIsWifiOnly = false;

    private UpdateInfo mInfo;
    private UpdateError mError = null;

    private IUpdateParser mParser = new DefaultUpdateParser();
    private IUpdateChecker mChecker;
    private IUpdateDownloader mDownloader;
    private IUpdatePrompter mPrompter;

    private OnFailureListener mOnFailureListener;

    private  OnDownloadListener mOnDownloadListener;
    public  OnDownloadListener mOnNotificationDownloadListener;
    public static UpdateManager.OnEndListener endListener;

    public UpdateAgent(Context context, String url, boolean isManual, boolean isWifiOnly, int notifyId) {
//        Log.e("tag","UpdateAgent success");
        mContext = context.getApplicationContext();
        mUrl = url;
        mIsManual = isManual;
        mIsWifiOnly = isWifiOnly;
        mDownloader = new DefaultUpdateDownloader(mContext);
        mPrompter = new DefaultUpdatePrompter(context);
        mOnFailureListener = new DefaultFailureListener(context);
        mOnDownloadListener = new DefaultDialogDownloadListener(context);
        if (notifyId > 0) {
            mOnNotificationDownloadListener = new DefaultNotificationDownloadListener(mContext, notifyId);
        } else {
            mOnNotificationDownloadListener = new DefaultDownloadListener();
        }
    }


    public void setParser(IUpdateParser parser) {
        mParser = parser;
    }

    public void setChecker(IUpdateChecker checker) {
        mChecker = checker;
    }

    public void setDownloader(IUpdateDownloader downloader) {
        mDownloader = downloader;
    }

    public void setPrompter(IUpdatePrompter prompter) {
        mPrompter = prompter;
    }

    public void setOnNotificationDownloadListener(OnDownloadListener listener) {
        mOnNotificationDownloadListener = listener;
    }

    public void setOnDownloadListener(OnDownloadListener listener) {
        mOnDownloadListener = listener;
    }

    public void setOnFailureListener(OnFailureListener listener) {
        mOnFailureListener = listener;
    }

    public void setInfo(UpdateInfo info) {
        mInfo = info;
    }

    @Override
    public UpdateInfo getInfo() {
        return mInfo;
    }

    @Override
    public void setInfo(String source) {
        try {
            mInfo = mParser.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
            setError(new UpdateError(UpdateError.CHECK_PARSE));
        }
    }

    @Override
    public void setError(UpdateError error) {
        mError = error;
    }

    @Override
    public void update() {//立即更新
        mApkFile = new File(mContext.getExternalCacheDir(), mInfo.md5 + ".apk");
        if (UpdateUtil.verify(mApkFile, mInfo.md5)) {
            doInstall();
        } else {
            doDownload();
        }
    }

    @Override
    public void ignore() {
        UpdateUtil.setIgnore(mContext, getInfo().md5);
    }

    @Override
    public void onStart() {
        if (mInfo.isSilent) {
            mOnNotificationDownloadListener.onStart();
        } else {

        }
        mOnDownloadListener.onStart();
    }

    @Override
    public void onProgress(int progress) {
        if (mInfo.isSilent) {
            mOnNotificationDownloadListener.onProgress(progress);
        } else {
//            mOnDownloadListener.onProgress(progress);
        }
        mOnDownloadListener.onProgress(progress);
    }

    @Override
    public void onFinish() {
        if (mInfo.isSilent) {
            mOnNotificationDownloadListener.onFinish();
        } else {
//            mOnDownloadListener.onFinish();
        }
        mOnDownloadListener.onFinish();
        if (mError != null) {
            mOnFailureListener.onFailure(mError);
        } else {
            mTmpFile.renameTo(mApkFile);
            if (mInfo.isAutoInstall) {
                doInstall();
            }
        }

    }


    public void check() {
//        UpdateUtil.log("check");
        if (mIsWifiOnly) {
            if (UpdateUtil.checkWifi(mContext)) {
                doCheck();
            } else {
                doFailure(new UpdateError(UpdateError.CHECK_NO_WIFI));
            }
        } else {
            if (UpdateUtil.checkNetwork(mContext)) {
                doCheck();
            } else {
                doFailure(new UpdateError(UpdateError.CHECK_NO_NETWORK));
            }
        }
    }



    void doCheck() {
        if (mUrl == null || mUrl.isEmpty()) {
//            setInfo();
            doCheckFinish();//直接下载，不检查
            return;
        }
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                if (mChecker == null) {
                    mChecker = new UpdateChecker();
                }
                mChecker.check(UpdateAgent.this, mUrl);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                doCheckFinish();
            }
        }.execute();
    }

    void doCheckFinish() {
//        UpdateUtil.log("check finish");
        UpdateError error = mError;
        if (error != null) {
            doFailure(error);
        } else {
            UpdateInfo info = getInfo();
            if (info == null) {
                doFailure(new UpdateError(UpdateError.CHECK_UNKNOWN));
            } else if (!info.hasUpdate) {
                doFailure(new UpdateError(UpdateError.UPDATE_NO_NEWER));
            } else if (UpdateUtil.isIgnore(mContext, info.md5)) {
                doFailure(new UpdateError(UpdateError.UPDATE_IGNORED));
            } else {
//                UpdateUtil.log("update md5" + mInfo.md5);
                UpdateUtil.ensureExternalCacheDir(mContext);
                UpdateUtil.setUpdate(mContext, mInfo.md5);
                mTmpFile = new File(mContext.getExternalCacheDir(), info.md5);
                mApkFile = new File(mContext.getExternalCacheDir(), info.md5 + ".apk");

                if (UpdateUtil.verify(mApkFile, mInfo.md5)) {
                    doInstall();
                }
//                else if (info.isSilent) {//强制更新，不弹框提示
//                    doDownload();
//                }
                else {
                    doPrompt();
                }
            }
        }

    }

    void doPrompt() {
        mPrompter.prompt(this);
    }

    void doDownload() {
        mDownloader.download(this, mInfo.url, mTmpFile);
    }

    void doInstall() {
        UpdateUtil.install(mContext, mApkFile, mInfo.isForce);
    }

    void doFailure(UpdateError error) {
        if (mIsManual || error.isError()) {
            mOnFailureListener.onFailure(error);
        }
    }

    private static class DefaultUpdateDownloader implements IUpdateDownloader {
        final Context mContext;

        public DefaultUpdateDownloader(Context context) {
            mContext = context;
        }

        @Override
        public void download(IDownloadAgent agent, String url, File temp) {//立即下载
            new UpdateDownloader(agent, mContext, url, temp).execute();
        }
    }

    private static class DefaultUpdateParser implements IUpdateParser {
        @Override
        public UpdateInfo parse(String source) throws Exception {
            return UpdateInfo.parse(source);
        }
    }

    private static class DefaultUpdatePrompter implements IUpdatePrompter {

        private Context mContext;

        public DefaultUpdatePrompter(Context context) {
            mContext = context;
        }

        @Override
        public void prompt(IUpdateAgent agent) {
            if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
                return;
            }
            UpdateHelper.getInstance().setAgent(agent);
            final UpdateInfo info = agent.getInfo();
            Intent intent = new Intent(mContext,UpdateDialogActivity.class);
            intent.putExtra("title",info.title);
            intent.putExtra("content",info.updateContent);
            mContext.startActivity(intent);

        }

    }

    private static class DefaultFailureListener implements OnFailureListener {

        private Context mContext;

        public DefaultFailureListener(Context context) {
            mContext = context;
        }

        @Override
        public void onFailure(UpdateError error) {
//            UpdateUtil.log(error.toString());
            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private static class DefaultDialogDownloadListener implements OnDownloadListener {
        private Context mContext;
        public DefaultDialogDownloadListener(Context context) {
            mContext = context;
        }

        @Override
        public void onStart() {
            if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                Intent intent = new Intent(mContext,DownloadDialogActivity.class);
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onProgress(int i) {
            sendBroadcastType(mContext,i);
        }

        @Override
        public void onFinish() {
            if (endListener != null)
                endListener.end();
            sendBroadcastType(mContext,100);
        }
    }
    /**此处为静默下载**/
    private static class DefaultNotificationDownloadListener implements OnDownloadListener {
        private Context mContext;
        private int mNotifyId;
        private NotificationCompat.Builder mBuilder;

        public DefaultNotificationDownloadListener(Context context, int notifyId) {
            mContext = context;
            mNotifyId = notifyId;
        }

        @Override
        public void onStart() {
            if (mBuilder == null) {
                String title = "下载中 - " + mContext.getString(mContext.getApplicationInfo().labelRes);
                mBuilder = new NotificationCompat.Builder(mContext);
                mBuilder.setOngoing(true)
                        .setAutoCancel(false)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(mContext.getApplicationInfo().icon)
                        .setTicker(title)
                        .setContentTitle(title);
            }
            onProgress(0);
        }

        @Override
        public void onProgress(int progress) {
            if (mBuilder != null) {
                if (progress > 0) {
                    mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
                    mBuilder.setDefaults(0);
                }
                mBuilder.setProgress(100, progress, false);

                NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(mNotifyId, mBuilder.build());
            }
        }

        @Override
        public void onFinish() {
            NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(mNotifyId);
        }
    }

    private static void sendBroadcastType(Context mContext,long type) {
        Intent intent = new Intent("ezy.boost.update.downloadBroadcast");
        intent.putExtra("type", type);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

}