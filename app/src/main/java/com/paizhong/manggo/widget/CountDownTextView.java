package com.paizhong.manggo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import com.paizhong.manggo.R;
import com.paizhong.manggo.utils.CheckUtils;


public class CountDownTextView extends android.support.v7.widget.AppCompatTextView {
    public static final int COUNT = 60;
    private static final int DELAY = 1000;
    private int count = COUNT;
    private Handler mHandler;
    private int mWidth;
    private String mFormat = "%ss";
    private String mPhoneText = null;
    private boolean hasStart = false;
    private boolean enable = true;
    private int from = 0;
    private Context mContext;

    public CountDownTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
        mContext = context;
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        final TypedArray attributes = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CountDownTextView,
                        defStyleAttr, 0);
        final int mReferId = attributes.getResourceId(R.styleable.CountDownTextView_refer , -1);
        from = attributes.getInt(R.styleable.CountDownTextView_from , 0);
        init();
    }

    private ViewGroup getRoot(){
        ViewParent viewParent = getParent();
        while(viewParent != null){

            if(((ViewGroup)viewParent).getId() == android.R.id.content){
                return (ViewGroup) viewParent;
            }
            viewParent = viewParent.getParent();
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if(mWidth > 0) {
//            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    private void init(){
        try {
            mHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (from == 1){
                        setText("重新发送("+String.format("%s" , --count)+")");
                        setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_shape_r15_e6e6e6));
                    }else {
                        setText(""+String.format("%ss" , --count)+"");
                    }
                    if(count <= 0 && !isEnabled()){
                        hasStart = false;
                        setEnabled((mPhoneText == null || CheckUtils.checkPhoneNum(mPhoneText)) && enable);
                        if (from == 1){
                            setText("重新发送");
                            setTextColor(getResources().getColor(R.color.color_ffffff));
                            setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_shape_r15_008eff));
                        }else {
                            setText("获取验证码");
                        }
                        mHandler.removeMessages(0);
                        if (listeners !=null){
                            listeners.countDownStop();
                        }
                    }else{
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(0) , 1000);
                    }
                }
            };
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // TODO Auto-generated method stub
                    mWidth = getWidth();
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setFormat(String format){
        mFormat = format;
    }
    public void start(){
        try {
            if(hasStart)return;
            hasStart = true;
            count = COUNT;
            setEnabled(false);
            if (from == 1){
                setText("重新发送("+String.format("%s" , --count)+")");
                setBackground(ContextCompat.getDrawable(mContext,R.drawable.bg_shape_r15_e6e6e6));
                setTextColor(getResources().getColor(R.color.color_50000000));
            }else{
                setText(""+String.format(mFormat , count)+"");
            }
            mHandler.sendMessageDelayed(mHandler.obtainMessage(0) , 1000);
        }catch (Exception e){
             e.printStackTrace();
        }
    }

    public void setEnable(boolean enable){
        try {
            this.enable = enable;
            if(hasStart)return;
            setEnabled(enable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeMessages(){
        try {
            if (mHandler !=null){
                mHandler.removeMessages(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private OnCountDownStopListeners listeners;
    public void setOnCountDownStopListeners(OnCountDownStopListeners l){
       this.listeners = l;
    }
    public interface OnCountDownStopListeners{
        void countDownStop();
    }
}
