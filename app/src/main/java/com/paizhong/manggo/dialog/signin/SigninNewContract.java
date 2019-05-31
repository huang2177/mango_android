package com.paizhong.manggo.dialog.signin;
import com.paizhong.manggo.base.BaseView;
import com.paizhong.manggo.bean.other.SignBean;
import com.paizhong.manggo.bean.other.SigninListBean;

/**
 * Created by zab on 2018/8/13 0013.
 */
public interface SigninNewContract {
    interface View extends BaseView {
        void bindSign(SignBean signBean);
        void bindDrawPrize(Integer score);
    }

    interface Presenter {
        void saveSign();
        void saveSign2();
        void drawPrize(String userId);
    }
}
