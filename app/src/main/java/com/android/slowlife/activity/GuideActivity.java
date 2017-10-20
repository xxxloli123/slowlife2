package com.android.slowlife.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.GuideAdapter;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.GuideUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/20 0020.
 */

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.viewpage)
    ViewPager viewpage;
    @Bind(R.id.guide_layout)
    LinearLayout guideLayout;
    private ArrayList<View> mylist;
    private LayoutInflater myinflater;
    private GuideAdapter adapter;
    private int which;
    private Handler handler = new Handler();
    private static String TAG = "LeftToRight";
    private static String LEFT = "LeftToRight";
    private static String RIGHT = "RightToLeft";
    private GuideUtils guideUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        getview();
    }

    private void getview() {
        // TODO Auto-generated method stub
        mylist = new ArrayList<View>(); // 初始化数据源
        myinflater = getLayoutInflater(); // 布局引用器
        myinflater = LayoutInflater.from(this);
        mylist.add(myinflater.inflate(R.layout.guide1, null)); // 引用布局
        mylist.add(myinflater.inflate(R.layout.guide2, null));
        mylist.add(myinflater.inflate(R.layout.guide3, null));
        adapter = new GuideAdapter(mylist,GuideActivity.this); // 初始化适配器
        viewpage.setAdapter(adapter); // 加载适配器
        guideUtils=new GuideUtils(this, guideLayout, mylist.size());
        guideUtils.drawsIamge();
        viewpage.setOnPageChangeListener(this);
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*
		 * 当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到
		 *
		 * 调用。其中三个参数的含义分别为：
		 *
		 * arg0 :当前页面，及你点击滑动的页面
		 *
		 * arg1:当前页面偏移的百分比
		 *
		 * arg2:当前页面偏移的像素位置
		 */
    }

    @Override
    public void onPageSelected(int position) {
        /*
		 * 此方法是页面跳转完后得到调用，arg0是你当前选中的页面的Position（位置编号）。
		 */
        guideUtils.setItem(position);
        which=position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        /*
		 * 此方法是在状态改变的时候调用，其中arg0这个参数
		 *
		 * 有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
		 *
		 * 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）
		 */

    }
    private Runnable runnable=new Runnable() {

        @Override
        public void run() {
            if(TAG.equals(LEFT)){
                if(which<mylist.size()-1){
                    which++;
                    viewpage.setCurrentItem(which);
                }else{
                    TAG=RIGHT;
                    handler.post(runnable);
                    return;
                }
            }else{
                if(which>0){
                    which--;
                    viewpage.setCurrentItem(which);
                }else{
                    TAG=LEFT;
                    handler.post(runnable);
                    return;
                }
            }
            handler.postDelayed(runnable, 3000);
        }
    };

}
