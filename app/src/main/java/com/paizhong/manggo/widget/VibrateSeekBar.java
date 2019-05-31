package com.paizhong.manggo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.paizhong.manggo.utils.Logs;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VibrateSeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private Vibrator vibrator;
    private LinkedList<Integer> extremeList = new LinkedList<>();

    //记录用户最后滑动的两个process值
    private int[] processArray = new int[]{0 , 0};

    //来回拖动四次则显示提示用户可输入
    private static final int MAX_DRAG = 4;
    private static final int PROCESS_GAP = 15;
    private boolean hasShowGuide = false;
    public VibrateSeekBar(Context context) {
        super(context);
        init();
    }

    public VibrateSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VibrateSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(mListener != null){
                    mListener.onProgress(progress);
                }
                //判断是否需要有提示用户输入入口
                //isInflectionPoint(progress);
                isChanging = true;
                //touchDown();
                handler.removeMessages(2);
                handler.sendEmptyMessageDelayed(2 , 120);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(mListener != null){
                    mListener.onStartTrackingTouch();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                extremeList.clear();
                if(mListener != null){
                    mListener.onStopTrackingTouch();
                }
                hasShowGuide = false;
            }
        });
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
    }

    private void isInflectionPoint(int currentProcess){

        if(hasShowGuide)return;
        if(processArray[0] == 0) {
            processArray[0] = currentProcess;
            return;
        }

        if(processArray[1] == 0){
            processArray[1] = currentProcess;
            return;
        }
        int delta1 = processArray[1] - processArray[0];
        int delta2 = currentProcess - processArray[1];
        int temp = processArray[1];
        processArray[0] = temp;
        processArray[1] = currentProcess;
        if((delta1 > 0 && delta2 < 0) || (delta1 < 0 && delta2 > 0)){
            print();
            extremeList.add(currentProcess);
            if (extremeList.size() >= MAX_DRAG){
                //判断是否需要显示可输入提示

                if(needShowGuide()){
                    if(mListener != null){
                        mListener.guessNumber(0);
                    }
                    hasShowGuide = true;
                }else{
                    extremeList.removeFirst();
                }
            }
        }
    }

    private boolean needShowGuide(){
        for(int i=0 ; i<extremeList.size() - 1 ; i++){
            if(Math.abs(extremeList.get(i) - extremeList.get(i + 1)) > PROCESS_GAP)
                return false;
        }

        return true;
    }

    private void print(){
        for(Integer process : extremeList){
            Logs.d("VibrateSeekBar" , "process ---> " + process);
        }
        Logs.d("VibrateSeekBar" , "  ---------------------====------------------ ");
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            isChanging = false;
        }
    };
    public void setProgressListener(OnProgressListener listener){
        mListener = listener;
    }

    private OnProgressListener mListener;

    public interface OnProgressListener{
        void onProgress(int progress);

        void guessNumber(int number);

        void onStartTrackingTouch();

        void onStopTrackingTouch();
    }
    private void touchDown(){
        throttleWithTimeoutObserver().subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                if(!isChanging || !vibrator.hasVibrator()){
                    return;
                }
                vibrator.vibrate(new long[]{25L , 25L} , -1);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        });
    }

    private boolean isChanging = false;
    private Observable<Integer> createObserver() {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if(subscriber.isUnsubscribed())return;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation());
    }

    private Observable<Integer> throttleWithTimeoutObserver() {
        return createObserver().throttleWithTimeout(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());

    }
}
