package com.paizhong.manggo.ui.setting.name;

import com.paizhong.manggo.base.BaseView;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface SettingNameContract {

    interface View extends BaseView {
       void bindNickName(String nickName);
    }
    interface Presenter {
       void updateNickName(String userId, String nickName, int genius, String phone);
    }
}
