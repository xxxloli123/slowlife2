package com.android.slowlife;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


        List<String> list = new ArrayList<String>();
        Class clz = list.getClass();
        Method m = clz.getMethod("get", int.class);
        System.out.println(m.getReturnType().getName());


        long time = System.currentTimeMillis() /1000;
        System.out.println(time);
        System.out.println(String.valueOf(time).length());

    }
}