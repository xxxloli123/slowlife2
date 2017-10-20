package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.android.slowlife.R;

/**
 * Created by liaoyi on 2016/10/10 0010.
 */

public class MScrollView extends ScrollView {

    View v1;
    View v2;

    public MScrollView(Context context) {
        super(context);
        init();
    }

    public MScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    private void init() {
        v2 = findViewById(R.id.layout);
    }

    public void setV1(View v1) {
        this.v1 = v1;
    }

    @Override
    public void computeScroll() {
        if (getScrollY() >= v2.getTop()) {
            v1.setVisibility(View.VISIBLE);
        } else {
            v1.setVisibility(View.GONE);
        }
        super.computeScroll();
    }
}