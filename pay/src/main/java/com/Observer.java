package com;

/**
 * Created by sgrape on 2017/6/21.
 * e-mail: sgrape1153@gmail.com
 */

public interface Observer<T> {
    public void update(Observable observable, T t);
}
