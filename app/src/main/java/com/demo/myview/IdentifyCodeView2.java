package com.demo.myview;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

public class IdentifyCodeView2 extends androidx.appcompat.widget.AppCompatEditText {

    private Paint mPaint;

    private Activity activity;

    private StringBuilder codes = new StringBuilder();

    public IdentifyCodeView2(Context context) {
        this(context,null);
    }

    public IdentifyCodeView2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IdentifyCodeView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.activity = (Activity) context;
        setTextIsSelectable(true);
        //setTransformationMethod();
        this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.copy,menu);
                Log.d("imageView","--------------");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        Log.d("imageView","--------------");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //wrap_content
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = 150 * 6 + 10 * 6 + 10;
            height = 170 + 30;
        }
        setMeasuredDimension(width,height);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onDraw(Canvas canvas) {
        Log.d("MyImageView","onDraw()");
        if (mPaint == null) {
            mPaint = new Paint();

            mPaint.setColor(Color.parseColor("#000000"));   //设置画笔颜色
            mPaint.setStyle(Paint.Style.STROKE);    //设置是否填充颜色
            mPaint.setStrokeWidth(5);   //设置线条宽度
            mPaint.setAntiAlias(true);  //抗锯齿
            //setLayerType(View.LAYER_TYPE_HARDWARE,mPaint);
        }
        int width = 150;
        int height = 170;
        int leftSpace = 10;
        //计算基线
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        for (int i = 0 ; i < 6 ; i++) {
            Log.d("MyImageView","画图" + i);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setTextSize(64);
            if (i == codes.length()) {
                mPaint.setColor(Color.parseColor("#FF0000"));
            }else {
                mPaint.setColor(Color.parseColor("#000000"));
            }
            canvas.drawRoundRect((i*width) + i*leftSpace + leftSpace,
                    30,
                    (i*width) + i*leftSpace +width + leftSpace,
                    height,
                    20,20,mPaint);
            canvas.drawLine(20+i*width + i*leftSpace + leftSpace,
                    width,
                    50+i*width + i*leftSpace + width - 65 + leftSpace,
                    width,
                    mPaint);

            //canvas.drawText("s", 80 + width * i + i * leftSpace, getHeight() / 2 + dy, mPaint);
            if (i < codes.length()) {
                Log.d("MyImageView","画文字" + codes.charAt(i));
                canvas.drawText(String.valueOf(codes.charAt(i)), 70 + width * i + i * leftSpace, getHeight() / 2 + dy, mPaint);
            }
        }

    }

//    @Override
//    public boolean performLongClick() {
//        return super.performLongClick();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestFocus();
        //setEnabled(true);
        long downTime = event.getDownTime();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

//                TimerTask timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (get)
//                    }
//                };
                if (event.getX() > 30 && event.getX() < 200 + 30) {
                    Log.d("MyImageView","第一个");
                    if (isFocused()) {
                        Log.d("MyImageView", "键盘管理");
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                }
                Log.d("MyImageView", "按下");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("MyImageView","抬起");
                startActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.copy,menu);
                    Log.d("imageView","--------------");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

            });
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("MyImageView","移动");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = EditorInfo.TYPE_CLASS_TEXT;
        return null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Log.d("imageView","键值码：" + event.getDisplayLabel());
        switch (keyCode) {
            case KeyEvent.KEYCODE_DEL:
                Log.d("imageView","按下删除键");
                if(codes.length() > 0)
                    codes.deleteCharAt(codes.length() - 1);
                    invalidate();
                    break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
                Log.d("imageView","按下shift键");
                break;
            default:
                if(codes.length() < 6) {
                    codes.append(event.getDisplayLabel());
                    Log.d("imageView","更新view" + codes.charAt(codes.length()-1));
                    invalidate();
                    return true;
                }

        }
        return super.onKeyDown(keyCode, event);
    }
}
