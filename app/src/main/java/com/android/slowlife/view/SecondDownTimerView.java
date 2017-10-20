package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;

public class SecondDownTimerView extends BaseCountDownTimerView{

	public SecondDownTimerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SecondDownTimerView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SecondDownTimerView(Context context) {
		this(context,null);
	}

	@Override
	protected String getStrokeColor() {
		return "ff0000";
	}

	@Override
	protected String getTextColor() {
		return "ffffff";
	}

	@Override
	protected int getCornerRadius() {
		return 1;
	}

	@Override
	protected int getTextSize() {
		return 16;
	}

	@Override
	protected String getBackgroundColor() {
		return "ff0000";
	}
	
}
