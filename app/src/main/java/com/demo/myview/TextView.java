package com.demo.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

public class TextView extends View {

    private String mText;
    private int mTextSize = 15;
    private int mTextColor = Color.BLACK;

    private Paint mPaint;

    //单位转换sp转px
    private int SpToPx(int sp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    //在创建实例的时候调用
    public TextView(Context context) {
        //调用第二个构造方法
        this(context,null);
    }

    //在布局中使用（在....xml布局文件中使用）的时候调用
    public TextView(Context context, @Nullable AttributeSet attrs) {
        //调用第三个构造方法
        this(context, attrs,0);
    }

    //自定义属性（自定义样式），设置样式的时候调用
    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TextView);
        mText = typedArray.getString(R.styleable.TextView_myText);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TextView_myTextSize,mTextSize);
        mTextColor = typedArray.getColor(R.styleable.TextView_myTextColor,mTextColor);
        typedArray.recycle();

        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);

        //设置字体大小颜色
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

    }

    //
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    /**
     * 自定义View的测量方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //布局宽高都是由这个方法测量的
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //布局文件给定大小就直接获取
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //布局文件中大小设定为wrap_content时需要通过计算得出自定义View的大小
        if(widthMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText,0,mText.length(),bounds);
            width = bounds.width();
        }

        //布局文件给定大小就直接获取
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //布局文件中大小设定为wrap_content时需要通过计算得出自定义View的大小
        if(widthMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(mText,0,mText.length(),bounds);
            height = bounds.width();
        }

        //最后设置自定义View的宽高
        setMeasuredDimension(width,height);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * x就是开始的位置
         * y 基线baseline(需要求)
         * */

        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();

        //计算基线
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        canvas.drawText(mText,0,getHeight()/2 + dy,mPaint);
    }


    //用于跟用户交互
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("textView","按下");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("textView","移动");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("textView","抬起");
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}
