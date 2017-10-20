package com.android.slowlife.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.DisplayUtil;
import com.android.slowlife.R;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by sgrape on 2017/6/16.
 * e-mail: sgrape1153@gmail.com
 */

public class Video extends View {

    private Paint grayPaint;
    private Paint bluePaint;
    private Paint imgPaint;
    private Bitmap bitmap;
    private float radius;
    private Paint arcPaint;
    private float leftRadius, rightRadius;
    private int rectH;
    private Bitmap leftIcon, rightIcon;

    public Video(Context context) {
        this(context, null);
    }

    public Video(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Video(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        grayPaint = new Paint();
        grayPaint.setColor(Color.GRAY);
        grayPaint.setAntiAlias(true);

        bluePaint = new Paint();
        bluePaint.setColor(Color.parseColor("#abcdef"));
        bluePaint.setAntiAlias(true);

        imgPaint = new Paint();
        imgPaint.setAntiAlias(true);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(DisplayUtil.dip2px(context, 1));
        leftIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.video_play_normal);
        rightIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.video_del_normal);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.video);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = Math.max(getMeasuredHeight(), getMeasuredWidth()) * 1.0f / 10;
        rectH = getMeasuredWidth() / 3 * 2;
        leftRadius = rightRadius = rectH / 16;
        int size = rectH / 8;
        leftIcon = Bitmap.createScaledBitmap(leftIcon, size, size, true);
        rightIcon = Bitmap.createScaledBitmap(rightIcon, size, size, true);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) radius, (int) radius, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();
        arcPaint.setColor(Color.GRAY);
        RectF rect = new RectF(0, h / 2 - w / 2, w, h / 2);
        rect.set(0, h / 2 - rectH, w, h / 2);
        canvas.drawCircle(w / 2, h / 2, radius, bluePaint);
        canvas.drawBitmap(bitmap, w / 2 - bitmap.getWidth() / 2, h / 2 - bitmap.getHeight() / 2, imgPaint);

        canvas.drawArc(rect, 45, 90, false, arcPaint);
        canvas.drawRect(rect, arcPaint);

        canvas.drawCircle(w / 7, rect.centerY() + rect.height() / 8 * 2.8f, rightRadius, arcPaint);

        canvas.drawCircle(w / 7 * 6, rect.centerY() + rect.height() / 8 * 2.8f, leftRadius, arcPaint);

        canvas.drawLine(w / 2, 0, w / 2, h, arcPaint);
        arcPaint.setColor(Color.RED);
        canvas.drawLine(0, rect.centerY() + rect.height() / 8 * 2.8f, w,
                rect.centerY() + rect.height() / 8 * 2.8f, arcPaint);
        canvas.drawLine(w / 7, rect.top, w / 7, rect.bottom, arcPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startDownAnim();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        super.onTouchEvent(event);
        return true;
    }


    private void startDownAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(radius, radius / 10.0f * 8, radius);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setVideoBitmap(@NonNull Bitmap img) {
        this.bitmap = img;
        invalidate();
    }

    public Bitmap getVideoBitmap() {
        return bitmap;
    }
}
