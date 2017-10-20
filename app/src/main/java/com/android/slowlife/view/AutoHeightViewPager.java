package com.android.slowlife.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 自适应子View高度的viewPager
 *
 * @author hellsam
 */
public class AutoHeightViewPager extends ViewPager {

    AutoHeightViewPager vp;

    public AutoHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        // 下面遍历所有child的高度
        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec,
//                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (i == page) {
//                height = h;
//            }
//        }
        if (getChildCount() > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(getChildAt(getCurrentItem()).getMeasuredHeight(),
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
