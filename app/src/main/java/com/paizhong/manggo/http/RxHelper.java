package com.paizhong.manggo.http;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.paizhong.manggo.bean.base.BaseResponse;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;



public class RxHelper {

    /**
     * 利用Observable.takeUntil()停止网络请求
     *
     * @param event
     * @param lifecycleSubject
     * @param <T>
     * @return
     */
    @NonNull
    public <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull final ActivityLifeCycleEvent event , final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event);
                            }
                        });
                return sourceObservable.takeUntil(compareLifecycleObservable);
            }
        };
    }

    /**
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseResponse<T>, T>    handleResult(final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<BaseResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseResponse<T>> tObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event);
                            }
                        });
                return tObservable.flatMap(new Func1<BaseResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResponse<T> result) {
                        if (result.success) {
                            return createData(result.data);
                        }else {
                            if (TextUtils.isEmpty(result.errorCode)){
                                return Observable.error(new ApiException(result.errorInfo));
                            }else{
                                return Observable.error(new ApiException(result.errorInfo,Integer.parseInt(result.errorCode),result.time));
                            }
                        }
                    }
                }) .takeUntil(compareLifecycleObservable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    //传入什么解析成什么
    public static <T> Observable.Transformer<T,T>    handleSimplyResult(final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event);
                            }
                        });
                return tObservable.flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T result) {
                        return createData(result);
                    }
                }) .takeUntil(compareLifecycleObservable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    /**
     * @param <T> 简单的接口调用
     * @return
     */
    public static <T> Observable.Transformer<BaseResponse<T>, T>   handleSimplyResult_2() {
        return new Observable.Transformer<BaseResponse<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseResponse<T>> tObservable) {
                return tObservable.flatMap(new Func1<BaseResponse<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseResponse<T> result) {
                        if (result.success) {
                            return createData(result.data);
                        }

                        else {
                            if (TextUtils.isEmpty(result.errorCode)){
                                return Observable.error(new ApiException(result.errorInfo));
                            }else{
                                return Observable.error(new ApiException(result.errorInfo,Integer.parseInt(result.errorCode),result.time));
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }

}
