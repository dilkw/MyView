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

    private float[] values = new float[]{10.02f,2.02134f,4f,1f};

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
        mPaint1.setTextAlign(Paint.Align.CENTER);
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
        //纵坐标箭头
        path.moveTo(110,130);
        path.lineTo(130,110);
        path.lineTo(150,130);
        path.lineTo(130,90);
        //纵坐标箭头
        path.moveTo(780,680);
        path.lineTo(820,700);
        path.lineTo(780,720);
        path.lineTo(800,700);

        canvas.drawLine(130,100,130,700, mPaint1);
        canvas.drawLine(130,700,800,700, mPaint1);
        Log.d("imageView", "onDraw:initValue " + values.length);
        int interval = 60;
        int initValue = 200;
        for (float value : values) {
            Log.d("imageView", "onDraw: initValue : " + initValue);
            canvas.drawRect(initValue,700 - progress * value,initValue+interval,700,mPaint1);
            canvas.drawText(String.valueOf(value * progress / 50), initValue + 30, 700 - progress * value - 20,mPaint1);
            initValue = initValue + interval * 2;
        }
        canvas.drawPath(path, mPaint1);
    }
}
