package com.example.gokuinvader.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.SortedList;

import com.example.gokuinvader.Models.HighScore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {
    public static final String HIGHSCORE_LIST = "HIGHSCORE_LIST";
    private static volatile SharedPreferencesManager instance = null;
    private static final String DB_FILE = "DB_FILE";
    private SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        synchronized (SharedPreferencesManager.class) {
            if (instance == null)
                instance = new SharedPreferencesManager(context);
        }
    }

    public void putHighscoreList(List<HighScore> score) {
        String highScoreJson = new Gson().toJson(score);
        sharedPreferences.edit().putString(HIGHSCORE_LIST, highScoreJson).apply();
    }

    public List<HighScore> getHighscoreList() {
        String result = sharedPreferences.getString(HIGHSCORE_LIST, "");
        Type listType = new TypeToken<List<HighScore>>() {
        }.getType();
        if (result.equals("")) {
            return new ArrayList<>();
        }
        return new Gson().fromJson( result,listType);
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }
}
