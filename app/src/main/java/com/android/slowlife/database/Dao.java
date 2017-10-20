package com.android.slowlife.database;

import java.util.List;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public interface Dao<T> extends DatabaseContance {
    T findById(int id);

    List<T> getAll();

    boolean deleteById(int id);

    boolean insert(T t);
}
