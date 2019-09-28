package com.example.lenovog50.pedometre2.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.lenovog50.pedometre2.data.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class UserRepository {


    private Context context;

    public UserRepository(Context context) {
        this.context = context;
    }

    public void addUser (User user)
    {
        String usersJson = SharedPrefsUtils.loadString(context,SharedPrefsUtils.USERS);
        ArrayList<User> users;
        if (usersJson == null)
        {
            users = new ArrayList<>();
        }
        else
        {
            users = new Gson().fromJson(usersJson,new TypeToken<ArrayList<User>>(){}.getType());
        }
        users.add(user);
        SharedPrefsUtils.saveString(context,SharedPrefsUtils.USERS,new Gson().toJson(users));
    }

    public ArrayList<User> getUsers ()
    {
        String usersJson = SharedPrefsUtils.loadString(context,SharedPrefsUtils.USERS);
        ArrayList<User> users;
        if (usersJson == null)
        {
            users = new ArrayList<>();
        }
        else
        {
            users = new Gson().fromJson(usersJson,new TypeToken<ArrayList<User>>(){}.getType());
        }
        return users;
    }

    public void getUser (String email,String password,OnUserSearchComplete onUserSearchComplete)
    {
        ArrayList<User> users = getUsers();
        for (User user : users)
        {
            if (user.getEmail().equals(email)&&user.getPassword().equals(password))
            {
                onUserSearchComplete.onUserFound(user);
                return;
            }
        }
        onUserSearchComplete.onUserNotFound();
        return;
    }

    public void connectUser (User user)
    {
        SharedPrefsUtils.saveString(context,SharedPrefsUtils.USER_KEY,new Gson().toJson(user));
    }

    public void updateUser (User user)
    {
        SharedPrefsUtils.saveString(context,SharedPrefsUtils.USER_KEY,new Gson().toJson(user));
        ArrayList<User> users = getUsers();
        for (User user1 : users)
        {
            user1.setWeight(user.getWeight());
            user1.setHeight(user.getHeight());
        }
        SharedPrefsUtils.saveString(context,SharedPrefsUtils.USERS,
                new Gson().toJson(users));
    }

    public User getConnectedUser ()
    {
        String userJson = SharedPrefsUtils.loadString(context,SharedPrefsUtils.USER_KEY);
        if (userJson==null)
            return null;
        User user = new Gson().fromJson(userJson,User.class);
        return user;
    }

    public void disconnectUser ()
    {
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(SharedPrefsUtils.USER_KEY);
        editor.apply();
    }

    public boolean emailExists (String email)
    {
        ArrayList<User> users = getUsers();
        for (User user:users)
        {
            if (user.getEmail().equals(email))
            {
                return true;
            }
        }
        return false;
    }

    public interface OnUserSearchComplete {
        void onUserFound (User user);
        void onUserNotFound ();
    }
}
