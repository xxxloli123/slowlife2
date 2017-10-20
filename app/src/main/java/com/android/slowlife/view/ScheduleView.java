package com.android.slowlife.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;

import com.DisplayUtil;

/**
 * Created by sgrape on 2017/5/22.
 * e-mail: sgrape1153@gmail.com
 */

public class ScheduleView extends TableLayout {

    private Paint linePaint;
    private Paint roundPaint;
    private Paint circlePaint;
    private int roundSize;
    private int strokeWidth;
    private int schedule = 1;

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        roundSize = DisplayUtil.dip2px(context, 5);
        strokeWidth = DisplayUtil.dip2px(context, 2);
        linePaint = new Paint();
        roundPaint = new Paint();
        circlePaint = new Paint();
        linePaint.setColor(Color.GRAY);
        linePaint.setAntiAlias(true);
        linePaint.setAlpha(175);
        roundPaint.setColor(Color.RED);
        roundPaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        circlePaint.setAlpha(100);
        setOrientation(VERTICAL);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int childCount = getChildCount();
        if (childCount == 0) return;
        final int left = getPaddingLeft() + roundSize + strokeWidth;
        final int top = getPaddingTop();
        linePaint.setAlpha(175);
        canvas.drawRect(
                left - 2, top + getChildAt(0).getHeight() / 2, left + 4,
                getChildAt(childCount - 1).getTop() + getChildAt(childCount - 1).getHeight() / 2,
                linePaint);

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final int t = child.getTop() + child.getHeight() / 2;
            if (i < schedule) {
                canvas.drawCircle(left + 2, t, roundSize + strokeWidth, circlePaint);
                canvas.drawCircle(left + 2, t, roundSize, roundPaint);
            } else {
                linePaint.setAlpha(255);
                canvas.drawCircle(left + 2, t, roundSize, linePaint);
            }
        }
    }

    public void setSchedule(int schedule) {
        this.schedule = schedule;
    }

    public int getSchedule() {
        return schedule;
    }
}
