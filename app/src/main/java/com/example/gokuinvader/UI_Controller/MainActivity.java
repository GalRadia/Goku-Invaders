package com.example.gokuinvader.UI_Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gokuinvader.Logic.GameManager;
import com.example.gokuinvader.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ShapeableImageView main_IMG_background;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_PlayerPos;
    private ShapeableImageView[][] main_IMG_obstacles;
    private GameManager gameManager;
    private static final long DELAY = 700;
    private Timer timerObs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();

    }

    @Override
    protected void onStart() {
        initTimers();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerObs.cancel();
    }

    private void initTimers() {
        timerObs = new Timer();
        timerObs.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() ->
                {
                    gameManager.moveObstacles();
                    refresh_UI();
                });
            }
        }, 0, DELAY);
    }

    private void refresh_UI() {
    updateObstacles();
    updatePlayerPos();
    checkCollision();

    }

    private void checkCollision() {
        if (gameManager.isCollision()) {
            vibrate();
            toast("Ough!");
            updateLife();

        }

    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void updatePlayerPos() {
        int playerPos = gameManager.getCurrentPlayerIndex();
        for (int i = 0; i < GameManager.COLS; i++)
            main_IMG_PlayerPos[i].setImageResource(0);
        main_IMG_PlayerPos[playerPos].setImageResource(R.drawable.goku_young);
    }

    private void updateObstacles() {
        boolean[][] obstacles = gameManager.getMatrixStatuses();
        for (int i = 0; i < obstacles.length; i++) {
            for (int j = 0; j < obstacles[0].length; j++) {
                if (obstacles[i][j])
                    main_IMG_obstacles[i][j].setImageResource(R.drawable.fireball_obstacle);
                else
                    main_IMG_obstacles[i][j].setImageResource(0);
            }
        }
    }

    private void updateLife() {
        if (gameManager.getHits() == main_IMG_hearts.length) {
            for (ShapeableImageView mainImgHeart : main_IMG_hearts) {
                mainImgHeart.setVisibility(View.VISIBLE);
            }
            gameManager.setHits(0);
        } else if (gameManager.getHits() != 0) {
            main_IMG_hearts[main_IMG_hearts.length - gameManager.getHits()].setVisibility(View.INVISIBLE);
        }
    }

    private void initViews() {
        gameManager = new GameManager(main_IMG_hearts.length);
        checkCollision();
        Glide
                .with(this)
                .load(R.drawable.clouds)
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(main_IMG_background);
        main_FAB_right.setOnClickListener(view -> {
            gameManager.movePlayerRight();
            updatePlayerPos();
            checkCollision();
        });
        main_FAB_left.setOnClickListener(view -> {
            gameManager.movePlayerLeft();
            updatePlayerPos();
            checkCollision();
        });
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };
        main_IMG_PlayerPos = new ShapeableImageView[]{
                findViewById(R.id.main_SIV_player_60),
                findViewById(R.id.main_SIV_player_61),
                findViewById(R.id.main_SIV_player_62)
        };
        main_IMG_obstacles = new ShapeableImageView[][]{
                {findViewById(R.id.main_SIV_obstacle_00), findViewById(R.id.main_SIV_obstacle_01), findViewById(R.id.main_SIV_obstacle_02)
                },
                {findViewById(R.id.main_SIV_obstacle_10), findViewById(R.id.main_SIV_obstacle_11), findViewById(R.id.main_SIV_obstacle_12)
                },
                {findViewById(R.id.main_SIV_obstacle_20), findViewById(R.id.main_SIV_obstacle_21), findViewById(R.id.main_SIV_obstacle_22)
                },
                {findViewById(R.id.main_SIV_obstacle_30), findViewById(R.id.main_SIV_obstacle_31), findViewById(R.id.main_SIV_obstacle_32)
                },
                {findViewById(R.id.main_SIV_obstacle_40), findViewById(R.id.main_SIV_obstacle_41), findViewById(R.id.main_SIV_obstacle_42)
                },
                {findViewById(R.id.main_SIV_obstacle_50), findViewById(R.id.main_SIV_obstacle_51), findViewById(R.id.main_SIV_obstacle_52)
                },
                {findViewById(R.id.main_SIV_obstacle_60), findViewById(R.id.main_SIV_obstacle_61), findViewById(R.id.main_SIV_obstacle_62)
                }
        };

    }
}