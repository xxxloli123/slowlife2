package com.android.slowlife.util;

import android.util.Log;

import com.android.slowlife.BuildConfig;

/**
 * Created by Administrator on 2017/2/7 .
 */

public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String Prefix = "jww";

    /**
     * 错误 Write By LILIN 2014-5-8
     *
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(Prefix + tag, msg + "");
        }
    }

    public static void epn(Exception e) {
        epn("", e);
    }

    public static void epn(String tag, Exception e) {
        if (isDebug) {
            String msg = (e != null && e.getMessage() != null) ? e.getMessage()
                    : " e is null";
            Log.e(Prefix + tag, msg + "");
        }
    }

    /**
     * 信息 Write
     *
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isDebug && msg != null) {
            while (msg.length() > 0) {
                if (msg.length() > 1024) {
                    Log.i(Prefix + tag, msg.substring(0, 1024) + "");
                    msg = msg.substring(1024);
                } else {
                    Log.i(Prefix + tag, msg + "");
                    msg = "";
                }
            }

        }
    }

    /**
     * 警告 Write
     *
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(Prefix + tag, msg + "");
        }
    }
}