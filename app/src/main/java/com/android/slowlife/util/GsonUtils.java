package com.android.slowlife.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Administrator on 2017/2/7 .
 */

public class GsonUtils {
    /**
     * Gson解析之自定义序列化和反序列化
     * 解决解析对象null指针
     *
     * @param backStr
     * @return
     */
    public static Object getGsonData(Class object, String backStr) {
        LogUtils.i(object.getSimpleName() + "_json=", backStr);
        try {
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(String.class, new StringConverter());//对String类型对象自定义序列化和反序列化
            gb.registerTypeAdapter(Integer.class, new IntegerConverter());//对Integer类型对象自定义序列化和反序列化
            Gson gson = gb.create();
            return gson.fromJson(backStr, object);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
