package com.android.slowlife;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.SaveUtils;
import com.android.slowlife.view.SystemBarTintManager;

/**
 * Created by Administrator on 2017/1/19 .
 * 项目中所有的activity继承基类BaseActivity做统一处理
 */
public class BaseActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.background1);//状态栏设置为透明色
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(Color.TRANSPARENT);//导航栏设置为透明色
        }
        //统一使用框架
        /**
         * view绑定框架
         *ButterKnife.bind(this);
         */
        /**
         * 图片处理框架可查看api了解属性
         *  Picasso.with(this).load(url).into(view);
         */


    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /**
     * 返回及返回动画
     */
    public void onBack() {
        finish();
//        activity.getParent().overridePendingTransition(
//                    R.anim.in_from_right, R.anim.out_to_left);
    }

    /**
     * 安全退出账号清除保存的数据
     */
    public void exitLogin() {
        SaveUtils.clear(this);
        /**
         * 根据退出时的情况是否有必要清除除登录以外的activity页面
         * 属于优化内存
         */
        CacheActivity.finishActivity();
    }

}
