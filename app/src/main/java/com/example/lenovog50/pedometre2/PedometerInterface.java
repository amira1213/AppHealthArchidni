package com.example.lenovog50.pedometre2;

import android.content.Context;

/**
 * Created by lenovo g50 on 26/11/2018.
 */

public interface PedometerInterface {
    public void startPedometer(Context context);
    public void setNewStepListener(OnNewStepListener onNewStepListener);
    public void stopPedometer(Context context);

    public interface OnNewStepListener {
        public void onNewStep(int stepCount,float distanceTravelled,float caloriesBurnt);
    }
}
