package com.android.slowlife.database;

import android.database.Cursor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public class DataRowToBean {
    public static <T> T rowToBean(Cursor cursor, Class<T> clz) {
        if (cursor.moveToNext()) {
            try {
                T t = clz.newInstance();
                String[] columNames = cursor.getColumnNames();
                for (String columName : columNames) {
                    try {
                        Field field = clz.getDeclaredField(columName);
                        field.setAccessible(true);
                        setFieldValueByCursor(cursor, field, t);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                return t;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<?> rowToBeanList(Cursor cursor, Class clz) {
        List<Object> list = new ArrayList();
        for (int i = 0; i < cursor.getCount(); i++)
            list.add(rowToBean(cursor, clz));
        System.out.println(cursor.getColumnNames());
        return list;
    }

    private static void setFieldValueByCursor(Cursor cursor, Field field, Object obj) throws IllegalAccessException {
        if (field==null)return;
        Class<?> clz = field.getType();
        if (clz == int.class || clz == Integer.class) {
            field.set(obj, cursor.getInt(cursor.getColumnIndex(field.getName())));
        } else if (clz == String.class) {
            field.set(obj, cursor.getString(cursor.getColumnIndex(field.getName())));
        } else if (clz == double.class || clz == Double.class) {
            field.set(obj, cursor.getDouble(cursor.getColumnIndex(field.getName())));
        } else if (clz == float.class || clz == Float.class) {
            field.set(obj, cursor.getFloat(cursor.getColumnIndex(field.getName())));
        } else if (clz == long.class || clz == Long.class) {
            field.set(obj, cursor.getLong(cursor.getColumnIndex(field.getName())));
        }
    }
}
