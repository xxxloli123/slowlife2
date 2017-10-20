package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.android.slowlife.R;

/**
 * Created by liaoyi on 2016/10/10 0010.
 */

public class MScrollView1 extends ScrollView {
    View v1;
    View v2;
    public MScrollView1(Context context) {
        super(context);
        //init();
    }
    public MScrollView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init();
    }
    public MScrollView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init();
    }
    private void init() {
        v2 = findViewById(R.id.tabs);
    }

    public void setV1(View v1) {
        this.v1 = v1;
    }
    public void setV2(View v2) {
        this.v2 = v2;
    }
    @Override
    public void computeScroll() {
        if (getScrollY() >= v2.getBottom()) {
            v1.setVisibility(View.VISIBLE);
        } else {
            v1.setVisibility(View.GONE);
        }
        super.computeScroll();
    }
}