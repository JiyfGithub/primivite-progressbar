package com.sw.primitiveprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by acer on 2017/6/12.
 * 自定义进度条控件
 */
public class CustomView extends View{
    private int outterCircleWidth;//外圆宽度
    private int outterCircleRadius;//外圆半径
    private int outterCircleColor;//外圆颜色
    private int innerCircleColor;//内圆颜色
    private int innerCircleRadius;//内圆半径
    private Paint outterCirclePaint;//外圆画笔
    private Paint innerCirclePaint;//内圆画笔
    private TextPaint textPaint;//文字进度画笔
    private int centerX;//控件的中心的x坐标
    private int centerY;//控件的中心的y坐标
    private int textSize = 50;//文字大小

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
       init(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //获取自定义属性的值
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        outterCircleWidth = ta.getDimensionPixelSize(R.styleable.CustomView_outterCircleWidth, 100);
        outterCircleColor = ta.getColor(R.styleable.CustomView_outterCircleColor, Color.BLUE);
        innerCircleColor = ta.getColor(R.styleable.CustomView_innerCircleColor, Color.BLUE);
        outterCircleRadius = outterCircleWidth/2;
        innerCircleRadius = 5;
        ta.recycle();
        //初始化外圆画笔
        outterCirclePaint = new Paint();
        outterCirclePaint.setColor(outterCircleColor);
        outterCirclePaint.setStyle(Paint.Style.FILL);
        outterCirclePaint.setAntiAlias(true);
        //初始化内圆画笔
        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(innerCircleColor);
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setAntiAlias(true);
        //初始化文字进度画笔
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textSize = outterCircleWidth/5;
        textPaint.setTextSize(textSize);
        textWidth = (int)textPaint.measureText("0.0%");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置自定义控件的大小
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(outterCircleWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        centerX = width/2;
        centerY = height/2;
    }

    private float textWidth;
    private int width;
    private int height;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画出外圆
        canvas.drawCircle(centerX, centerY, outterCircleRadius, outterCirclePaint);
        //画出内圆
        canvas.drawCircle(centerX, centerY, innerCircleRadius, innerCirclePaint);
        //画出当前进度
        canvas.drawText(progress+"%", (width - textWidth)/2, (height+textSize)/2, textPaint);
    }

    private int gap = 50;
    private int totalTime = 5000;//总用时为5秒
    private int now = 0;
    private float progress;
    private boolean isDownloading;
    public void startDownload(){
        if(isDownloading){
            Toast.makeText(getContext(), "正在下载...", Toast.LENGTH_SHORT).show();
            return;
        }
        //开启线程
        new Thread(){
            @Override
            public void run() {
                isDownloading = true;
                while(now < totalTime){
                    SystemClock.sleep(gap);
                    now += gap;

                    //计算当前进度，并更新UI
                    progress = now*100f/totalTime;
                    innerCircleRadius = (int)(progress*outterCircleRadius/100);
                    //计算进度文字的宽度
                    textWidth = (int)textPaint.measureText(progress+"%");

                    postInvalidate();
                }
                postInvalidate();
                now = 0;
                isDownloading = false;
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isDownloading){
            //根据手指触摸位置，移动自定义view的位置
            move((int)event.getRawX(), (int)event.getRawY());
            return true;
        }

        return super.onTouchEvent(event);
    }

    //根据手指触摸位置，移动自定义view的位置
    public void move(int x, int y){
//        layout(x - width/2, y - height/2, x + width/2, y + height/2);
        layout(x - width/2, y - height, x + width/2, y);//还需要把状态栏等的高度计算在内？
    }
}
