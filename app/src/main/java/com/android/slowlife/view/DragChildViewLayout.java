package com.android.slowlife.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.slowlife.R;

/**
 * Created by sgrape on 2017/5/12.
 * e-mail: sgrape1153@gmail.com
 */

public class DragChildViewLayout extends RelativeLayout {


    private ViewDragHelper dragHelper;
    private int tagView;

    public DragChildViewLayout(Context context) {
        this(context, null);
    }

    public DragChildViewLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragChildViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTagViewId(R.id.appointment);
        dragHelper = ViewDragHelper.create(this, 1.0f, new Callback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }


    public void setTagViewId(@IdRes int tagViewId) {
        this.tagView = tagViewId;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    class Callback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            System.out.println(child.getId() == tagView);
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            System.out.println(left);
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }
    }
}
