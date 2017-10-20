package com.android.slowlife.database.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.slowlife.ProvinceEntity;
import com.android.slowlife.database.DataRowToBean;
import com.android.slowlife.database.DatabaseOpenHelper;
import com.android.slowlife.database.ProvinceDao;

import java.util.List;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public class ProvinceDaoImpl implements ProvinceDao {
    private SQLiteDatabase database;

    private ProvinceDaoImpl() {
    }

    private ProvinceDaoImpl(Context context) {
        database = getDao(context);
    }

    private SQLiteDatabase getDao(Context context) {
        database = new DatabaseOpenHelper(context).getReadableDatabase();
        return database;
    }

    public static ProvinceDao getInstance(Context context) {
        ProvinceDao dao = new ProvinceDaoImpl(context);
        return dao;
    }

    @Override
    public ProvinceEntity findById(int id) {
        Cursor cr = database.query(PROVINCE, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        ProvinceEntity pe = DataRowToBean.rowToBean(cr, ProvinceEntity.class);
        database.close();
        return pe;
    }

    @Override
    public List<ProvinceEntity> getAll() {
        Cursor cr = database.query(PROVINCE, null, null, null, null, null, null);
        List<ProvinceEntity> list = (List<ProvinceEntity>) DataRowToBean.rowToBeanList(cr, ProvinceEntity.class);
        cr.close();
        database.close();
        return list;
    }

    @Override
    public boolean deleteById(int id) {
        long num = database.delete(PROVINCE, "id=?", new String[]{String.valueOf(id)});
        database.close();
        return num == 1 ? true : false;
    }

    @Override
    public boolean insert(ProvinceEntity provinceEntity) {
        return false;
    }

    @Override
    public ProvinceEntity searchByName(String startPro) {
        Cursor cr = database.query(PROVINCE, null, "name like '%" + startPro + "%'", null, null, null, null);
        ProvinceEntity pe = DataRowToBean.rowToBean(cr, ProvinceEntity.class);
        cr.close();
        database.close();
        return pe;
    }
}
