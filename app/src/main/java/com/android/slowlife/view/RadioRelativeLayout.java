package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

/**
 * Created by sgrape on 2017/6/8.
 * e-mail: sgrape1153@gmail.com
 */

public class RadioRelativeLayout extends RelativeLayout implements Checkable, View.OnClickListener {

    private OnClickListener onClickListener;
    private CheckBox checkBox;
    private CheckGroupLinearLayout.OnCheckedChangeListener listener;
    private CheckGroupLinearLayout.OnCheckedChangeListener onCheckedChangeListener;

    public RadioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checkBox != null) checkBox.setChecked(checked);
        if (listener != null) listener.onCheckedChanged(this, checked);
        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedChanged(this, checked);
    }

    @Override
    public boolean isChecked() {
        if (checkBox != null)
            return checkBox.isChecked();
        else return false;
    }

    @Override
    public void toggle() {
        if (checkBox != null) checkBox.toggle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View child = getChildAt(0);
        if (child != null && child instanceof CheckBox)
            checkBox = (CheckBox) child;
    }

    void setOnCheckedChangeWidgetListener(CheckGroupLinearLayout.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setOnCheckedChangeListener(CheckGroupLinearLayout.OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toggle();
        if (onClickListener != null) onClickListener.onClick(v);
    }

}
