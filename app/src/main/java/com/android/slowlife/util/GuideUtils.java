package com.android.slowlife.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.android.slowlife.R;

import java.util.ArrayList;

public class GuideUtils {
	
	private Context context;
	private ArrayList<ImageView> imagePoints=new ArrayList<ImageView>();
	private int imageSize;
	private LinearLayout guideLayout;
	public GuideUtils(Context context,LinearLayout guideLayout,int imageSize){
		this.context=context;
		this.guideLayout=guideLayout;
		this.imageSize=imageSize;
	}
	
	public void drawsIamge(){
		guideLayout.removeAllViews();
		LayoutParams mParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mParams.setMargins(10, 0, 10, 0);
		for(int i=0;i<imageSize;i++){
			ImageView imagePoint=new ImageView(context);
			if(i==0){
				imagePoint.setBackgroundResource(R.drawable.dot_selected1);
			}else{
				imagePoint.setBackgroundResource(R.drawable.dot_unselected1);
			}
			guideLayout.addView(imagePoint, mParams);
			imagePoints.add(imagePoint);
		}
	}
	
	public void setItem(int position){
		for(int i=0;i<imageSize;i++){
			if(position==i){
				imagePoints.get(i).setBackgroundResource(R.drawable.dot_selected1);
			}else{
				imagePoints.get(i).setBackgroundResource(R.drawable.dot_unselected1);
			}
		}
	}
}
