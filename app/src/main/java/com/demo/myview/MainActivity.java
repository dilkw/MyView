package com.demo.myview;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity {

    private IdentifyCodeView identifyCodeView;

    private RingView ringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyImageView myImageView = findViewById(R.id.my_imageView);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img2);
        myImageView.setImageResId(R.drawable.img2);
        Log.d("MyImageView","setBitmap1");
//        ImageView imageView;

        ringView = findViewById(R.id.ringView);
        identifyCodeView = findViewById(R.id.identifycode);
        identifyCodeView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    identifyCodeView.hideSoftInputOutOfVonClick();
                }
            }
        });

//        identifyCodeView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    Log.d("imageView","返回键");
//                    identifyCodeView.clearFocus();
//                }
//                return false;
//            }
//        });

        ObjectAnimator animator = ObjectAnimator.ofFloat(ringView,"progress", 0,100);
        //动画每次执行的时间（毫秒数）
        animator.setDuration(1000);
        //设置动画执行次数（ValueAnimator.INFINITE为无限）
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //当动画执行次数大于零或是无限（ValueAnimator.INFINITE）时setRepeatMode才有效
        animator.setRepeatMode(ValueAnimator.RESTART);
        //设置插值（匀速，创建插值对象）
        animator.setInterpolator(new LinearInterpolator());
//        ringView.setRo
        //启动动画
        animator.start();
        //添加动画监听
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ringView.addSeconds();
            }
        });

        identifyCodeView.setOnLongClickListener();
        IdentifyCodeView2 identifyCodeView2 = findViewById(R.id.identifycode2);
        identifyCodeView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("MyImageView","长按2");
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("imageView","失去焦点");
        identifyCodeView.clearFocus();
//        identifyCodeView.hideSoftInputOutOfVonClick();
        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.d("imageView","控件以外" + ev.getRawX() + "=====" + identifyCodeView.getY() + " ===== " + ev.getRawY() + "====" + identifyCodeView.getTop() + "====" + identifyCodeView.getBottom());
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if ((ev.getRawX() < identifyCodeView.getLeft() || ev.getRawX() >identifyCodeView.getRight())
//                || (ev.getRawY() < identifyCodeView.getTop() + 50 || ev.getRawY() > identifyCodeView.getBottom())) {
//                Log.d("imageView", "隐藏键盘1");
//                if (identifyCodeView.isFocused()) {
//                    identifyCodeView.clearFocus();
//                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(identifyCodeView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    Log.d("imageView", "隐藏键盘2");
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
