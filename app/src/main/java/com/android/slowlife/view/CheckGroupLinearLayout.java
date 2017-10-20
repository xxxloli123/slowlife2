package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by sgrape on 2017/6/8.
 * e-mail: sgrape1153@gmail.com
 */

public class CheckGroupLinearLayout extends LinearLayout {
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public CheckGroupLinearLayout(Context context) {
        this(context, null);
    }

    public CheckGroupLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckGroupLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == CheckGroupLinearLayout.this && child instanceof RadioRelativeLayout) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == CheckGroupLinearLayout.this && child instanceof RadioRelativeLayout) {
                ((RadioRelativeLayout) child).setOnCheckedChangeWidgetListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(RadioRelativeLayout view, boolean isChecked);
    }
}
