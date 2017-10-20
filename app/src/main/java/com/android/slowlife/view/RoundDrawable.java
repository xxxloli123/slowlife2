package com.android.slowlife.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by sgrape on 2017/5/5.
 * e-mail: sgrape1153@gmail.com
 */

public class RoundDrawable extends Drawable {

    private Bitmap img;
    private int roundx, roundy;
    private Paint paint;
    private RectF rectF;

    public RoundDrawable(Bitmap img) {
        this(img, -1, -1);
    }


    public RoundDrawable(Bitmap img, int roundx, int roundy) {
        this.img = img;
        this.roundx = roundx == -1 ? Integer.MAX_VALUE : roundx;
        this.roundy = roundy == -1 ? Integer.MAX_VALUE : roundy;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        rectF = new RectF(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(rectF, roundx, roundy, paint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        rectF = new RectF(bounds);
        createScaledBitmap();
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) rectF.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) rectF.height();
    }

    private void createScaledBitmap() {
        this.img = Bitmap.createScaledBitmap(img, getIntrinsicWidth(), getIntrinsicHeight(), true);
        paint.setShader(new BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    }
}
