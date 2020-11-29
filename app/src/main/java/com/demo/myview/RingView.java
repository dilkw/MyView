package com.demo.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;

public class RingView extends View {

    Paint mPaint;

    Paint mPaintMinute;

    Paint mPaintHour;

    int circleX = 0, circleY = 0;

    float progress = 0;

    int seconds = 0;
    int minute = 0;
    int hour = 0;

    public int getNumber() {
        return seconds;
    }

    public void setNumber(int number) {
        this.seconds = number;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public void addSeconds() {
        ++seconds;
        if (seconds == 60) {
            ++minute;
            seconds = 0;
            if (minute == 60) {
                ++hour;
                minute = 0;
                if (hour == 12) {
                    hour = 0;
                }
            }
        }
        Log.d("imageView","ringView ------ addSeconds()");
    }

    public RingView(Context context) {
        this(context,null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(Color.parseColor("#E91E63"));
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(5);
            mPaint.setTextSize(32);
            mPaint.setTextAlign(Paint.Align.CENTER);
        }
        if (mPaintMinute == null) {
            mPaintMinute = new Paint();
            mPaintMinute.setColor(Color.parseColor("#AA05F3"));
            mPaintMinute.setStyle(Paint.Style.STROKE);
            mPaintMinute.setStrokeCap(Paint.Cap.ROUND);
            mPaintMinute.setAntiAlias(true);
            mPaintMinute.setStrokeWidth(10);
        }

        if (mPaintHour == null) {
            mPaintHour = new Paint();
            mPaintHour.setColor(Color.parseColor("#CC00F3"));
            mPaintHour.setStyle(Paint.Style.STROKE);
            mPaintHour.setStrokeCap(Paint.Cap.ROUND);
            mPaintHour.setAntiAlias(true);
            mPaintHour.setStrokeWidth(20);
        }
        Calendar calendar = Calendar.getInstance();
        seconds = calendar.get(Calendar.SECOND);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY) % 12;
        Log.d("imageView"," ringView ------ 构造方法" + hour + ":" + minute + ":"  + seconds);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleX = MeasureSpec.getSize(widthMeasureSpec) /  2;
        circleY = MeasureSpec.getSize(heightMeasureSpec) / 2;
        Log.d("imageView","----------------------" + circleX + circleY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("imageView","ringView ------ onDraw()" + progress + "=============" + getWidth());
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(0,0,getWidth(),getHeight());
        //mPaint.setColor(Color.parseColor("#FF0000"));
        canvas.drawArc(rectF,-90,progress*3.6F,false,mPaint);
        drawNumber(canvas);
        drawScale(canvas);
        drawPointer(canvas);
    }

    //指针刻画
    private void drawPointer(Canvas canvas) {
        canvas.save();
        canvas.rotate(seconds*6,circleX,circleY);
        canvas.drawLine(circleX,circleY + 80,circleX,0,mPaint);
        canvas.restore();
        canvas.save();
        canvas.rotate(minute * 6,circleX,circleY);
        canvas.drawLine(circleX,circleY + 70,circleX,40,mPaintMinute);
        canvas.restore();
        canvas.save();
        canvas.rotate(hour * 30,circleX,circleY);
        canvas.drawLine(circleX,circleY + 40,circleX,80,mPaintHour);
        canvas.restore();
    }

    //刻度画笔
    private void drawScale(Canvas canvas){
        canvas.save();
        int length = 10;
        int isNumber = 0;
        for (int i = 0 ; i <= 59 ; i++ ) {
            isNumber = 0;
            if ((i+1) % 5 == 0) {
                isNumber = 1;
                //canvas.drawText(String.valueOf(i),getWidth()/2,60,mPaint);
            }
            canvas.rotate(6,circleX,circleY);
            canvas.drawLine(circleX,length + isNumber * 20,circleX,0,mPaint);
        }
        canvas.restore();
    }

    private void drawNumber(Canvas canvas) {
        canvas.drawText("12",circleX,64,mPaint);
        canvas.drawText("6",circleX,getHeight() - 64,mPaint);
        canvas.drawText("9",64,circleY + 10,mPaint);
        canvas.drawText("3",getWidth() - 64,circleY + 10,mPaint);
    }

    public void addNumber(){
        seconds++;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("imageView","ringView---按下");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("imageView","移动");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("imageView","抬起");
                break;
        }
        return super.onTouchEvent(event);
        //return false;
    }
}
