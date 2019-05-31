package com.paizhong.manggo.http;
import com.paizhong.manggo.events.AppAccountEvent;
import com.paizhong.manggo.utils.NetUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


public abstract class HttpSubscriber<T> extends Subscriber<T> {
    public final int ERROR_DEFAULT = -1;    //默认异常
    public final int ERROR_NETWORK = -4;     //无网络
    public final int ERROR_TIME_OUT = -5;     //请求超时
    public final int ERROR_TOKET_ERROR_301 = 301;//toket失效
    public final int ERROR_TOKET_ERROR_308 = 308;//toket 错误
    public final int ERROR_TOKET_ERROR_309 = 309;//toket 错误

    public HttpSubscriber() {
    }

    @Override
    public void onStart() {
        super.onStart();
        _onStart();
    }

    @Override
    public void onCompleted() {
        _onCompleted();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (!NetUtil.isConnected()) {
            _onError("网络不可用", ERROR_NETWORK);

        }else if (e instanceof ApiException) {
             ApiException apiException = (ApiException)e;
             if (apiException.getCode() == ERROR_TOKET_ERROR_301
                     || apiException.getCode() == ERROR_TOKET_ERROR_308
                     || apiException.getCode() == ERROR_TOKET_ERROR_309){
                 EventBus.getDefault().post(new AppAccountEvent(0,null));
                 _onError("登录失效请重新登录App",apiException.getCode());
             }else {
                 _onError(apiException.getMessage(), ERROR_DEFAULT);
             }
        }else if (e instanceof HttpException){
            HttpException exception = (HttpException) e;
            if (exception.code() == 500) {
                _onError("服务器异常，请稍后重试", ERROR_DEFAULT);
            } else {
                _onError("连接服务器失败，请稍后再试", ERROR_DEFAULT);
            }
        }else if (e instanceof SocketTimeoutException){
            _onError("连接服务器超时，请稍后再试", ERROR_TIME_OUT);

        }else {
            _onError("连接服务器失败，请稍后再试", ERROR_DEFAULT);

        }
        _onCompleted();
    }

    protected void _onStart() {
    }

    protected void _onNext(T t) {
    }

    protected void _onError(String message) {
    }

    protected void _onCompleted() {
    }

    protected void _onError(String message, int code) {
        _onError(message);
    }
}
