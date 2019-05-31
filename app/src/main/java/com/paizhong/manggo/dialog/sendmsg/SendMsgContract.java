package com.paizhong.manggo.dialog.sendmsg;

import com.paizhong.manggo.base.BaseView;

public interface SendMsgContract {

    interface View extends BaseView{
        void bindMsgProducer();
    }

    interface Presenter {
        void getMsgProducer(String phone,String content,String name);
    }
}
