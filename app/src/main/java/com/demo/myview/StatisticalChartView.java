package com.demo.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class StatisticalChartView extends View {

    private Paint mPaint1;

    private Paint mPaint2;

    private Path path;

    private int[] values = new int[]{5,2,4,1};

    private float progress = 0;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatisticalChartView(Context context) {
        this(context,null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatisticalChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatisticalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StatisticalChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMPaint1();
        path = new Path();
    }

    private void initMPaint1() {
        mPaint1 = new Paint();
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setStrokeWidth(5);
        mPaint1.setTextSize(30);
        mPaint1.setColor(Color.parseColor("#000000"));
    }

    private void initMPaint2() {
        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setStrokeWidth(5);
        mPaint2.setColor(Color.parseColor("#CC00F3"));
    }

    //private int columnarWidth =

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(110,130);
        path.lineTo(130,110);
        path.lineTo(150,130);
        path.lineTo(130,90);
        canvas.drawLine(130,100,130,700, mPaint1);
        canvas.drawLine(130,700,800,700, mPaint1);
        Log.d("imageView", "onDraw:initValue " + values.length);
        int interval = 60;
        int initValue = 200;
        for (int value : values) {
            Log.d("imageView", "onDraw: initValue : " + initValue);
            canvas.drawRect(initValue,700 - progress * value,initValue+interval,700,mPaint1);
            canvas.drawText(String.valueOf((int)value), initValue, 700 - progress * value - 20,mPaint1);
            initValue = initValue + interval * 2;
        }

//        path.moveTo(260,700);
//        path.lineTo(260,300);
//        path.lineTo(320,300);
//        path.lineTo(320,700);
//        canvas.drawText("500", 260, 280,mPaint1);
//
//        path.moveTo(380,700);
//        path.lineTo(380,200);
//        path.lineTo(440,200);
//        path.lineTo(440,700);
//        canvas.drawText("300", 380, 180,mPaint1);
//
//        path.moveTo(500,700);
//        path.lineTo(500,400);
//        path.lineTo(560,400);
//        path.lineTo(560,700);
//        canvas.drawText("200", 500, 380,mPaint1);
//
//        canvas.drawRect(620,progress,680,700,mPaint1);
//        canvas.drawText(String.valueOf((int)progress), 620, progress - 20,mPaint1);

        canvas.drawPath(path, mPaint1);
    }
}
