package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;

public class SecondDownTimerView1 extends BaseCountDownTimerView{

	public SecondDownTimerView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SecondDownTimerView1(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SecondDownTimerView1(Context context) {
		this(context,null);
	}

	@Override
	protected String getStrokeColor() {
		return "e9e9e9";
	}

	@Override
	protected String getTextColor() {
		return "3d3c3a";
	}

	@Override
	protected int getCornerRadius() {
		return 1;
	}

	@Override
	protected int getTextSize() {
		return 25;
	}

	@Override
	protected String getBackgroundColor() {
		return "ffffff";
	}
	
}
