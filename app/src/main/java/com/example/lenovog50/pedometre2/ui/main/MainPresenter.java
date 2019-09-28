package com.example.lenovog50.pedometre2.ui.main;

import android.content.Context;

import com.example.lenovog50.pedometre2.IMCUtils;
import com.example.lenovog50.pedometre2.data.UserRepository;
import com.example.lenovog50.pedometre2.data.model.User;

public class MainPresenter implements MainContract.Presenter {

    private User user;
    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void onCreate(Context context) {
        user = new UserRepository(context).getConnectedUser();
        view.showUserInfo(user,getImc(user), getRmq(user));
        view.showWeatherThisWeek(getFavorableDays());
        view.showTodayInfo(getDegrees(),getDescription(),getFavorable());
    }


    private float getImc (User user)
    {
        return IMCUtils.getIMC(user.getWeight(),user.getHeight());
    }

    private String getRmq(User user)
    {
        return IMCUtils.getRmq(IMCUtils.getIMC(user.getWeight(),user.getHeight()));
    }

    private String getFavorableDays()
    {
        return "Mardi, Samedi";
    }

    private int getDegrees()
    {
        return 30;
    }

    private String getDescription ()
    {
        return "ciel dégagé";
    }

    private int getFavorable()
    {
        return 0;
    }

}
