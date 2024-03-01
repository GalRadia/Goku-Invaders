package com.example.gokuinvader.UI_Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.gokuinvader.Interfaces.HighscoreCallbacks;
import com.example.gokuinvader.R;
import com.example.gokuinvader.Views.MapsFragment;
import com.example.gokuinvader.Views.RecyclerViewFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ScoreActivity extends AppCompatActivity {
    public static final String KEY_PLAYER_NAME = "KEY_PLAYER_NAME";
    private ShapeableImageView score_IMG_background;
    private MaterialButton score_BTN_PlayAgain;
    private FrameLayout scoreFrameRcv;
    private FrameLayout scoreFrameMap;
    private MapsFragment mapsFragment;
    private RecyclerViewFragment recyclerViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
    }

    private void initViews() {
        score_BTN_PlayAgain.setOnClickListener(view -> {
            Intent startIntent = new Intent(this,StartActivity.class);
            startActivity(startIntent);
            finish();
        });
       // Glide.with(this).load(R.drawable.score_background).placeholder(R.drawable.ic_launcher_foreground).fitCenter().into(score_IMG_background);
        recyclerViewFragment = new RecyclerViewFragment();
        recyclerViewFragment.setCallbacks((position,playerName) -> mapsFragment.ChangeLocation(position,playerName));
        mapsFragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_rcv,recyclerViewFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map,mapsFragment).commit();
    }

    private void findViews() {
        score_BTN_PlayAgain= findViewById(R.id.score_BTN_playAgain);
        //score_IMG_background = findViewById(R.id.score_IMG_background);
        scoreFrameRcv = findViewById(R.id.score_FRAME_rcv);
        scoreFrameMap = findViewById(R.id.score_FRAME_map);
    }


}