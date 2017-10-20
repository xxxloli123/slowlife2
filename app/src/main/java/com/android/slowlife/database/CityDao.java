package com.android.slowlife.database;


import com.android.slowlife.CityEntity;

import java.util.List;

/**
 * Created by sgrape on 2017/7/2.
 * e-mail: sgrape1153@gmail.com
 */

public interface CityDao extends Dao<CityEntity> {
    List<CityEntity> seachByProvince(int pid);
}
