package com.android.slowlife.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.DisplayUtil;

/**
 * Created by sgrape on 2017/5/4.
 * e-mail: sgrape1153@gmail.com
 */

public class BitmapView extends View {
    private final int scaledtouchslop;
    private Bitmap img;
    private Paint rectPaint;
    private Paint imgPaint;
    private int rectSize;
    private PointF point;
    private PointF pressDown;
    private RectF rectF;
    private boolean move;
    private RectF imgRectF;
    private Bitmap resImg;

    public BitmapView(Context context) {
        this(context, null);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rectSize = DisplayUtil.px2dip(context, 200);
        rectSize = 200;
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.RED);
        rectPaint.setStrokeWidth(DisplayUtil.dip2px(context, 0.5f));
        rectPaint.setStyle(Paint.Style.STROKE);
        point = new PointF();
        imgPaint = new Paint();
        rectF = new RectF(0, 0, rectSize, rectSize);
        imgPaint.setAntiAlias(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        scaledtouchslop = viewConfiguration.getScaledTouchSlop();
        pressDown = new PointF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0, height = 0;
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(getBackground().getMinimumWidth(), getMinimumWidth());
                break;
            case MeasureSpec.UNSPECIFIED:
                width = getDefaultSize(0, widthMeasureSpec);
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(getBackground().getMinimumHeight(), getMinimumHeight());
                break;
            case MeasureSpec.UNSPECIFIED:
                height = getDefaultSize(0, heightMeasureSpec);
                break;
        }
        rectF.set((getMeasuredWidth() - rectSize) / 2, (getMeasuredHeight() - rectSize) / 2,
                (getMeasuredWidth() + rectSize) / 2, (getMeasuredHeight() + rectSize) / 2);
        setMeasuredDimension(width, height);
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (img != null) {
            float left = (getWidth() - img.getWidth()) / 2.0f;
            float top = (getHeight() - img.getHeight()) / 2.0f;
            canvas.drawBitmap(img, left, top, imgPaint);
        }

        if (resImg != null)
            canvas.drawBitmap(resImg, null, new Rect(0,0,200,200), null);
        float left = rectF.left + point.x - pressDown.x;
        float top = rectF.top + point.y - pressDown.y;
        if (left < 0) left = 0;
        if (left + rectSize > getMeasuredWidth()) left = getMeasuredWidth() - rectSize;
        if (top < 0) top = 0;
        if (top + rectSize > getMeasuredHeight()) top = getMeasuredHeight() - rectSize;
        canvas.drawRect(left, top, left + rectSize, top + rectSize, rectPaint);
    }


    public void setBitmap(@NonNull Bitmap bitmap) {
        this.img = bitmap;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (img == null) return;
        Bitmap bitmap = this.img;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxWidth = getMeasuredWidth();
        int maxHeight = getMeasuredHeight();
        int newWidth = Math.min(width, maxWidth);
        int newHeight = Math.min(height, maxHeight);
        if (newWidth == width && newHeight == height) {
            img = bitmap;
        } else {
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            img = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
        }
        if (imgRectF == null) imgRectF = new RectF();
        imgRectF.set((getMeasuredWidth() - width) / 2, (getMeasuredHeight() - height) / 2,
                (getMeasuredWidth() + width) / 2, (getMeasuredHeight() + height) / 2);
        rectF = new RectF((maxWidth - rectSize) / 2, (maxHeight - rectSize) / 2,
                (maxWidth - rectSize) / 2 + rectSize, (maxHeight - rectSize) / 2 + rectSize);
    }

    public void setBitmapResource(@DrawableRes int id) {
        setBitmap(BitmapFactory.decodeResource(getResources(), id));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressDown.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                move = false;
                float left = rectF.left + event.getX() - pressDown.x;
                float top = rectF.top + event.getY() - pressDown.y;
                if (left < 0) left = 0;
                if (left + rectSize > getMeasuredWidth()) left = getMeasuredWidth() - rectSize;
                if (top < 0) top = 0;
                if (top + rectSize > getMeasuredHeight()) top = getMeasuredHeight() - rectSize;
                rectF.set(left, top, left + rectSize, top + rectSize);
                pressDown.set(0, 0);
                point.set(0, 0);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (move
                        || Math.abs(event.getX() - pressDown.x) > scaledtouchslop
                        || Math.abs(event.getY() - pressDown.y) > scaledtouchslop) {
                    move = true;
                    point.set(event.getX(), event.getY());
                    invalidate();
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    public Bitmap getBitmap() {
        return img;
    }

    public Bitmap getDisposedBitmap() {
        if (img == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(rectSize, rectSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        float left = (getMeasuredWidth() - img.getWidth()) / 2.0f;
        float top = (getMeasuredHeight() - img.getHeight()) / 2.0f;
        RectF rect = new RectF(rectF.left - left, rectF.top - top, rectF.left - left + rectSize, rectF.top - top + rectSize);


        canvas.drawRect(rect, p);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(img, -rect.left, -rect.top, p);

        /*
        try {
            Bitmap bitmap1 = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(bitmap1);
            Paint p1 = new Paint();
            p1.setAntiAlias(true);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas1.drawBitmap(img, 0, 0, p);
            p1.setStyle(Paint.Style.STROKE);
            p1.setStrokeWidth(1);
            p1.setColor(Color.BLACK);
            canvas1.drawRect(rect, p1);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File("/sdcard/Android/tem1.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
//        resImg = bitmap;
//        invalidate();
        return bitmap;
    }

    public void setRectColor(int color) {
        rectPaint.setColor(color);
        invalidate();
    }

    public int getRectSize() {
        return rectSize;
    }

    public void setRectSize(int rectSize) {
        this.rectSize = rectSize;
        invalidate();
    }

}
