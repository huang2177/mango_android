package com.paizhong.manggo.ui.setting.head;

import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.other.HeadImageBean;

import java.util.List;

/**
 * Created by zab on 2018/4/4 0004.
 */

public interface SettingHeadContract {

    interface View extends BaseView {
       void bindImageList(List<HeadImageBean> images);
       void bindUpdateHeadPic(String userPic);
    }
    interface Presenter {
      void getImageList(boolean show, String phone);

      void updateHeadPic(String userId, String userPic, int genius, String phone);
    }
}
