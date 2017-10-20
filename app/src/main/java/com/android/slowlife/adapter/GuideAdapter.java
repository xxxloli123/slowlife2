package com.android.slowlife.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.slowlife.MainActivity;
import com.android.slowlife.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class GuideAdapter extends PagerAdapter{
    private ArrayList<View> mPagerView;
    private Activity activity;
    private static final String LAUNCH_NAME="launch";
    private static final String GUIDE_ACTIVITY="guide";
    public GuideAdapter(ArrayList<View> mPagerView,Activity activity){
        this.mPagerView=mPagerView;
        this.activity=activity;
    }
    @Override
    public int getCount() {
        return mPagerView!=null?mPagerView.size():0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==(View)arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(View view, int position) {
        ((ViewPager) view).addView(mPagerView.get(position), 0);
        if (position == mPagerView.size() - 1) {
            TextView run = (TextView) view.findViewById(R.id.run);
            run.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 设置已经引导
                    setGuided();
                    Intent intent=new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }
        return mPagerView.get(position);
    }
    private void setGuided(){
        SharedPreferences pref = activity.getSharedPreferences("myActivityName", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();
    }

}
