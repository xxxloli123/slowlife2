package com.android.slowlife.view;

import android.view.animation.Interpolator;


public class SpringInterpolator implements Interpolator {

    @Override
    public float getInterpolation(float input) {
        /**
         * factor = 0.4
         pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1
         */
        double factor = 0.8d;
        double springValue = Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2.0d * Math.PI) / factor) + 1;
        return (float) springValue;
    }
}

