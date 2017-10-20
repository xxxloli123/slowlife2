package com.android.slowlife.database.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.slowlife.CityEntity;
import com.android.slowlife.database.CityDao;
import com.android.slowlife.database.DataRowToBean;
import com.android.slowlife.database.DatabaseOpenHelper;

import java.util.List;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public class CityDaoImpl implements CityDao {
    private SQLiteDatabase database;

    private CityDaoImpl() {
    }

    private CityDaoImpl(Context context) {
        database = getDao(context);
    }

    private SQLiteDatabase getDao(Context context) {
        return new DatabaseOpenHelper(context).getReadableDatabase();
    }

    public static CityDao getInstance(Context context) {
        CityDao dao = new CityDaoImpl(context);
        return dao;
    }

    @Override
    public CityEntity findById(int id) {
        Cursor cursor = database.query(CITY, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        CityEntity ce = DataRowToBean.rowToBean(cursor, CityEntity.class);
        cursor.close();
        database.close();
        return ce;
    }

    @Override
    public List<CityEntity> getAll() {
        Cursor cr = database.query(CITY, null, null, null, null, null, null);
        List<CityEntity> list = (List<CityEntity>) DataRowToBean.rowToBeanList(cr, CityEntity.class);
        cr.close();
        database.close();
        return list;
    }

    @Override
    public boolean deleteById(int id) {
        long num = database.delete(CITY, "id=?", new String[]{String.valueOf(id)});
        database.close();
        return num == 1 ? true : false;
    }

    @Override
    public boolean insert(CityEntity cityEntity) {
        return false;
    }

    @Override
    public List<CityEntity> seachByProvince(int pid) {
        Cursor cr = database.query(CITY, null, "pid=?", new String[]{String.valueOf(pid)}, null, null, null, null);
        List<CityEntity> list = (List<CityEntity>) DataRowToBean.rowToBeanList(cr, CityEntity.class);
        cr.close();
        database.close();
        return list;
    }
}
