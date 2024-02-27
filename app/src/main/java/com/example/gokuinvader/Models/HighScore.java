package com.example.gokuinvader.Models;

import com.google.android.gms.maps.model.LatLng;

public class HighScore implements Comparable<HighScore> {
    private int score;
    private String name;
    private LatLng position;

    public HighScore(int score, String name, LatLng position) {
        this.score = score;
        this.name = name;
        this.position = position;
    }
    public HighScore(int score, String name) {
        this.score = score;
        this.name = name;
        this.position = null;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public LatLng getPostion() {
        return position;
    }

    public HighScore setScore(int score) {
        this.score = score;
        return this;
    }

    public HighScore setName(String name) {
        this.name = name;
        return this;
    }

    public HighScore setPostion(LatLng position) {
        this.position = position;
        return this;
    }

    @Override
    public int compareTo(HighScore highScore) {
        return Integer.compare(this.getScore(), highScore.getScore());
    }
}
