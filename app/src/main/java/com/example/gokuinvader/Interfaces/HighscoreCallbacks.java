package com.example.gokuinvader.Interfaces;

import androidx.recyclerview.widget.SortedList;

import com.example.gokuinvader.Models.HighScore;
import com.google.android.gms.maps.model.LatLng;

public interface HighscoreCallbacks {
    void onLocationChange(LatLng position,String name);
}
