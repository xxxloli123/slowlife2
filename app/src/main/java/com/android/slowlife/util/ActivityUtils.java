package com.android.slowlife.util;

import android.app.Activity;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7 .
 */

public class ActivityUtils {
    /**
     * 页面跳转及动画
     *
     * @param activity
     * @param activityClazz
     * @param param
     */
    public static void startActivityAnimGeneral(Activity activity,
                                                Class activityClazz, Map<String, Object> param) {
        Intent intent = new Intent(activity, activityClazz);
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object obj = entry.getValue();
                if (obj != null) {
                    if (obj instanceof String) {
                        intent.putExtra(entry.getKey(),
                                (String) entry.getValue());
                    } else if (obj instanceof Integer) {
                        intent.putExtra(entry.getKey(),
                                (Integer) entry.getValue());
                    } else {
                        try {
                            intent.putExtra(entry.getKey(),
                                    (Serializable) entry.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        activity.startActivity(intent);
        /**
         * 跳转效果
         */
        if (activity.getParent() != null) {
//            activity.getParent().overridePendingTransition(
//                    R.anim.in_from_right, R.anim.out_to_left);
        } else {
//            activity.overridePendingTransition(R.anim.in_from_right,
//                    R.anim.out_to_left);
        }
    }

}
