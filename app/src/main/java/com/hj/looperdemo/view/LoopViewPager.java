package com.hj.looperdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


public class LoopViewPager extends ViewPager implements View.OnTouchListener {
    private String TAG = "LoopViewPager";

    //这是默认view的切换时间间隔
    public static final long DEFAULT_DELAY_TIME = 2000;
    private long delayTime = DEFAULT_DELAY_TIME;

    private boolean isClick = false;
    private float downX, downY;
    private long downTime;

    public LoopViewPager(@NonNull Context context) {
        this(context, null);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = getCurrentItem();
            currentItem++;
            setCurrentItem(currentItem);
            postDelayed(this, delayTime);
            Log.e("currentItem", "currentItem" + currentItem);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow...to");
        startLooper();

    }

    private void startLooper() {
        removeCallbacks(runnable);
        postDelayed(runnable, delayTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, "onDetachedFromWindow...from");
        stopLooper();
    }

    private void stopLooper() {
        removeCallbacks(runnable);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = System.currentTimeMillis();
                stopLooper();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                float dX = Math.abs(event.getX() - downX);
                float dY = Math.abs(event.getY() - downY);
                long dTime = System.currentTimeMillis() - downTime;
                isClick = (dX < 5 && dY < 5 && dTime < 1000);
                if(isClick && pageItemClickListener != null){//点击
                    //调用接口方法
                    pageItemClickListener.PagerItemClickListener(getCurrentItem());
                }
                startLooper();
                break;

        }
        return false;
    }

    private OnPagerItemClickListener pageItemClickListener = null;

    public void setPagerItemClickListener(OnPagerItemClickListener listener){
        this.pageItemClickListener = listener;
    }
    public interface OnPagerItemClickListener{
        void PagerItemClickListener(int position);
    }
}
