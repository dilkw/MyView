package com.demo.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class MyImageView extends View {

    private int radio;
    private Paint paint;
    private int imgSrc;
    private Bitmap bitmap;

    //初始化MyImageView默认显示图像
    private int resId = 0;

    //设置显示的图像的resourceId
    public void setImageResId(@DrawableRes int resId) {
        Log.d("MyImageView","setBitmap2");
        this.resId = resId;
        this.bitmap = BitmapFactory.decodeResource(getResources(),resId);
        invalidate();
    }

    //获取显示的图像的resourceId
    public @DrawableRes int getResId() {
        return this.resId;
    }

    //获取显示的图像的bitmap格式
    public Bitmap getBitmap() {
        return this.bitmap;
    }

    //设置显示的图像的bitmap格式
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public MyImageView(Context context) {
        this(context,null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        Log.d("MyImageView","构造方法");
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);

        //创建TypeArray接收xml中传递过来的参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.MyImageView);
        resId = typedArray.getResourceId(R.styleable.MyImageView_mySrc,0);
        if(resId != 0) {
            bitmap = BitmapFactory.decodeResource(getResources(),resId);
        }
        //清空回收TypeArray
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("MyImageView","onMeasure方法");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 200;
        int height = 200;
        if(widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        //最后设置自定义View的宽高
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("MyImageView","onDraw方法");
//        canvas.save();
        if(this.bitmap == null) {
            Log.d("imageView","图片为空");
            setImageResId(R.drawable.img);
        }
        Shader shader = new BitmapShader(this.bitmap,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT);
        paint.setShader(shader);
        int cx = 0,cy = 0;
        cx = getWidth()/2;
        cy = getHeight()/2;
        Log.d("MyImageView","cx" + getWidth() + "   cy:" + getHeight());
        canvas.drawCircle(cx,cy,getWidth()/2,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("imageView","按下");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("imageView","移动");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("imageView","抬起");
                break;
        }
//        return super.onTouchEvent(event);
        return false;
    }
}
