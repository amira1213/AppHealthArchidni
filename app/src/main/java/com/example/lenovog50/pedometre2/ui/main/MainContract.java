package com.example.lenovog50.pedometre2.ui.main;

import android.content.Context;

import com.example.lenovog50.pedometre2.data.model.User;

public interface MainContract {

    public interface View {
        void showTodayInfo (int degrees,String description,int favorable);
        void showWeatherThisWeek (String days);
        void showUserInfo (User user,float imc,String rmq);
    }

    public interface Presenter {
        void onCreate(Context context);
    }

}
