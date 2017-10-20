package com.android.slowlife.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sgrape on 2017/6/30.
 * e-mail: sgrape1153@gmail.com
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper implements DatabaseContance {
    private Context context;

    public DatabaseOpenHelper(Context context) {
        super(context, "area.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream in = null, in1 = null;
        ByteArrayOutputStream baos1 = null, baos2 = null;
        String province = "CREATE TABLE  IF NOT EXISTS province('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' VARCHAR(20) NOT NULL,  'status' TINYINT(1)  DEFAULT 0);";

        String city = "CREATE TABLE IF NOT EXISTS 'city'('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'pid' INTEGER NOT NULL,'name' VARCHAR(20) NOT NULL, 'status' TINYINT(1)  DEFAULT 0);";

        String district = "CREATE TABLE IF NOT EXISTS 'district'('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'cid' INTEGER NOT NULL,'name' VARCHAR(20) NOT NULL, 'cityCode' INTEGER(6)  NOT NULL,'adCode' TINYINT  NOT NULL, 'status' TINYINT(1)  DEFAULT 0);";
        db.execSQL(province);
        db.execSQL(city);
//        db.execSQL(district);
        try {
            in = context.getAssets().open("province");
            int len;
            byte[] buff = new byte[1024];
            baos1 = new ByteArrayOutputStream();
            while ((len = in.read(buff)) != -1) {
                baos1.write(buff, 0, len);
                baos1.flush();
            }
            String sql = baos1.toString("UTF-8");
            db.execSQL(sql);

            in1 = context.getAssets().open("citys");
            baos2 = new ByteArrayOutputStream();
            while (((len = in1.read(buff)) != -1)) {
                baos2.write(buff, 0, len);
                baos2.flush();
            }
            sql = baos2.toString("UTF-8");
//            db.execSQL(sql);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (baos1 != null) baos1.close();
                if (in1 != null) in1.close();
                if (baos2 != null) baos2.close();
            } catch (IOException e) {

            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
