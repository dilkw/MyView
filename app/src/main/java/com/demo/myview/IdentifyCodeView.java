package com.demo.myview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.RequiresApi;

public class IdentifyCodeView extends View{

    private Paint mPaint;

    float width, height, top, leftSpace;

    private boolean isLongClick = false;

    private ActionMode mActionMode;

    private StringBuilder codes;

    public String getCodes() {
        return codes.toString();
    }

    public void setCodes(String codes) {
        Log.d("imageView","setCodes()");
        this.codes.delete(0,this.codes.length());
        this.codes.append(codes);
        invalidate();
    }

    public IdentifyCodeView(Context context) {
        this(context,null);
    }

    public IdentifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IdentifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        codes = new StringBuilder();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;

        //wrap_content
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            Log.d("imageView","MeasureSpec.AT_MOST");
            width = 150 * 6 + 10 * 6 + 10;
            height = 170 + 30;
        }

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            Log.d("imageView","MeasureSpec.EXACTLY");
            height = (int) ((float)width * (117F / 97F) *  (20F / 117F));
        }
        setMeasuredDimension(width,height);
        initVar((float)width, (float)height);
    }

    //初始化各变量
    private void initVar(float w, float h) {
        width = w * (15F / 97F) ;
        height = h * (17F / 20F);
        top = h - height;
        leftSpace = w * (7F / 97F) / 7F;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);        //设置是否填充颜色
            mPaint.setStrokeWidth(5);                   //设置线条宽度
            mPaint.setAntiAlias(true);                  //抗锯齿
            mPaint.setTextSize(6.4F*leftSpace);         //设置字体颜色
            //setLayerType(LAYER_TYPE_SOFTWARE, null);    //关闭硬件加速
            setSelected(true);
        }
        //计算基线
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        for (int i = 0 ; i < 6 ; i++) {
            Log.d("MyImageView","画图" + i);
            mPaint.setColor(Color.parseColor("#000000"));   //设置画笔颜色
            if (i == codes.length() && isFocused()) {
                mPaint.setColor(Color.parseColor("#FF0000"));
            }else {
                mPaint.setColor(Color.parseColor("#000000"));
            }

            canvas.drawRoundRect((i*width) + i*leftSpace + leftSpace,
                                top,
                                (i*width) + i*leftSpace +width + leftSpace,
                                    height,
                                leftSpace*2F,leftSpace*2F,mPaint);
            canvas.drawLine(leftSpace*2F+i*width + i*leftSpace + leftSpace, width,
                            leftSpace*5F+i*width + i*leftSpace + width - leftSpace*6.5F + leftSpace, width, mPaint);
            if (i < codes.length()) {
                canvas.drawText(String.valueOf(codes.charAt(i)), leftSpace*7 + width * i + i * leftSpace, getHeight() / 2 + dy, mPaint);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mActionMode != null)
                    mActionMode.finish();
                break;
            case MotionEvent.ACTION_UP:
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (isFocused() && !isLongClick) {
                    clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    if (isLongClick) {
                        isLongClick = false;
                        return true;
                    }
                    requestFocus();
                    inputMethodManager.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    inputMethodManager.restartInput(this);
                    this.setOnKeyListener(new OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            return false;
                        }
                    });
                }
                //InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputMethodManager.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                isLongClick = false;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean onCheckIsTextEditor() {
//        return true;
//    }
//
//    @Override
//    public InputConnection onCreateInputConnection(final EditorInfo outAttrs) {
//        outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
//        //outAttrs.inputType = InputType.TYPE_NULL;
//        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI;
//        outAttrs.hintText = "hello";
//        return new BaseInputConnection(this,false) {
//            @Override
//            public boolean commitText(CharSequence text, int newCursorPosition) {
//                Log.d("keyEvent", "commitText: " + text);
//                if (codes.length() < 6) {
//                    codes.append(text);
//                    invalidate();
//                }
//                return true;
////                outAttrs.hintText = "text";
////                return super.commitText(text, newCursorPosition);
//            }
//
//            @Override
//            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
//                Log.d("keyEvent", "deleteSurroundingText: beforeLength:" + beforeLength + "afterLength:" + afterLength);
//                if (beforeLength==1 && afterLength==0) {
//                    return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
//                            && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
//                }
//                //super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_DEL));
//                return super.deleteSurroundingText(beforeLength, afterLength);
//            }
//
//            @Override
//            public boolean setComposingText(CharSequence text, int newCursorPosition) {
//                Log.d("keyEvent", "setComposingText: " + text);
//                //sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//                //return true;
//                //sendKeyEvent(new KeyEvent())
//                getExtractedText(new ExtractedTextRequest(),GET_EXTRACTED_TEXT_MONITOR);
//                return super.setComposingText(text, newCursorPosition);
//            }
//
//            @Override
//            public Editable getEditable() {
//                Editable editable = super.getEditable();
//                if (editable.length() == 6) {
//                    Log.d("keyEvent", "getEditable: null");
//                    return null;
//                } else if(editable.length() > 0) {
//                    commitText(String.valueOf(editable.charAt(editable.length()-1)),editable.length());
//                }
//                Log.d("keyEvent", "getEditable: " + editable.toString());
//
//                //return super.getEditable();
//                return editable;
//            }
//
//            @Override
//            public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
//                //request = new ExtractedTextRequest();
//                @SuppressLint("ServiceCast") InputMethodService inputMethodService = (InputMethodService) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                ExtractedText extractedText = inputMethodService.getCurrentInputConnection().getExtractedText(request,flags);
//
//                Editable editable = getEditable();
//                ExtractedText extractedText = new ExtractedText();
//                Log.d("keyEvent", "getExtractedText: ");
//                //extractedText.text = editable.toString();
//                extractedText.text = "hello";
////                Log.d("keyEvent", "getExtractedText: " + extractedText.text);
////                editable.replace(0,0,"H");
////
//                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                //inputMethodService.onUpdateExtractedText(request.token,extractedText);
//                inputMethodManager.updateExtractedText(IdentifyCodeView.this,request.token,extractedText);
//                //return super.getExtractedText(request, flags);
//                //return extractedText;
//                return null;
//            }
//        };
//
//    }

//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        super.onWindowFocusChanged(hasWindowFocus);
//        if (!hasWindowFocus) {
//            Log.d("keyEvent", "onWindowFocusChanged: ");
//            hideSoftInputOutOfVonClick();
//        }
//    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("imageView","键值码：" + (char)event.getUnicodeChar() + (char)keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DEL:
                Log.d("keyEvent","按下删除键");
                if(codes.length() > 0)
                    codes.deleteCharAt(codes.length() - 1);
                    invalidate();
                    break;
            case KeyEvent.KEYCODE_SHIFT_LEFT:
                Log.d("keyEvent","按下shift键");
                break;
            default:
                if(codes.length() < 6) {
                    codes.append((char)event.getUnicodeChar());
                    Log.d("keyEvent","更新view" + codes.charAt(codes.length()-1));
                    invalidate();
                    return true;
                }

        }
        return super.onKeyDown(keyCode, event);
    }


    //重写此方法主要是监听【键盘在显示时】按下返回键事件
    //因为在【键盘在显示时】onKeyDown()方法中无法监听到
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("imageView","-----------键值：" + keyCode);
            this.clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener(){
        @RequiresApi(api = Build.VERSION_CODES.M)
        public boolean onLongClick(View v) {
            Log.d("MyImageView","长按1");
            //identifyCodeView.getX()
            isLongClick = true;
            requestFocus();
            mActionMode = startActionMode(new ActionMode.Callback2() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.copy,menu);
                    Log.d("MyImageView","--------------");
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    ClipboardManager clipboardManager = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    switch (item.getItemId()) {
                        case R.id.item1:
                            Log.d("MyImageView", "粘贴");
                            ClipData clipData = clipboardManager.getPrimaryClip();
                            if (clipData != null) {
                                String codes = clipData.getItemAt(0).getText().toString().trim();
                                Log.d("MyImageView", "粘贴板内容：" + clipData.getItemAt(0).toString());
                                if (codes.length() == 6) {
                                    setCodes(codes);
                                }
                            }
                            mode.finish();
                            break;
                        case R.id.item2:
                            Log.d("MyImageView", "复制");
                            ClipData mClipData = ClipData.newPlainText("Label", getCodes());
                            clipboardManager.setPrimaryClip(mClipData);
                            mode.finish();
                            break;
                        case R.id.item3:
                            Log.d("MyImageView", "清空");
                            setCodes("");
                            mode.finish();
                            break;
                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                    Log.d("MyImageView", "弹框位置" + getLeft() + "," + getRight() + "," + getTop() + ",");
                    outRect.set(new Rect(0,0,0,0));
                }
            },ActionMode.TYPE_FLOATING);
            return true;
        }
    };

    public void setOnLongClickListener() {
        this.setOnLongClickListener(this.onLongClickListener);
    }

    //当键盘弹出时，点击控件以外收起键盘
    public void hideSoftInputOutOfVonClick() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("imageView", "隐藏键盘2");
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }
}
