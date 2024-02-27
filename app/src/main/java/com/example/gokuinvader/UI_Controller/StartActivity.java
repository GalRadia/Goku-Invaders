package com.example.gokuinvader.UI_Controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.gokuinvader.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.imageview.ShapeableImageView;

public class StartActivity extends AppCompatActivity {
    ShapeableImageView start_IMG_background;
    MaterialButtonToggleGroup start_MBTG_speed;
    MaterialButtonToggleGroup start_MBTG_buttons;
    MaterialButton start_BTN_highscores;
    MaterialButton start_BTN_start;
    MaterialButton start_BTN_buttons;
    MaterialButton start_BTN_fast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getPermission();
        findViews();
        initViews();
    }
    void getPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                finish();
                                // No location access granted.
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
    private void initViews() {
        Glide.with(this).load(R.drawable.start_background).placeholder(R.drawable.ic_launcher_foreground).centerCrop().into(start_IMG_background);

        start_BTN_start.setOnClickListener(view->{
            changeToMainActivity(start_BTN_fast.isChecked(),start_BTN_buttons.isChecked());
        });
        start_BTN_highscores.setOnClickListener(view -> {
            Intent scoreIntent = new Intent(this,ScoreActivity.class);
            startActivity(scoreIntent);
            finish();
        });
    }

    private void findViews() {
        start_IMG_background=findViewById(R.id.start_IMG_background);
        start_MBTG_buttons = findViewById(R.id.start_MBTG_buttons);
        start_MBTG_speed = findViewById(R.id.start_MBTG_speed);
        start_BTN_start = findViewById(R.id.start_BTN_start);
        start_BTN_highscores = findViewById(R.id.start_BTN_highscores);
        start_BTN_buttons = findViewById(R.id.start_BTN_buttons);
        start_BTN_fast = findViewById(R.id.start_BTN_fast);
    }

    private void changeToMainActivity(boolean isFast, boolean buttons){
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.putExtra(MainActivity.KEY_ISFAST,isFast);
        mainIntent.putExtra(MainActivity.KEY_BUTTONS,buttons);
        startActivity(mainIntent);
        finish();
    }
}