package com.example.gokuinvader.UI_Controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.gokuinvader.Interfaces.TiltCallbacks;
import com.example.gokuinvader.Logic.GameManager;
import com.example.gokuinvader.Models.Obstacle_status;
import com.example.gokuinvader.Models.HighScore;
import com.example.gokuinvader.R;
import com.example.gokuinvader.Utils.SoundManager;
import com.example.gokuinvader.Utils.SharedPreferencesManager;
import com.example.gokuinvader.Utils.TiltDetector;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_ISFAST = "KEY_ISFAST";
    public static final String KEY_BUTTONS = "KEY_BUTTONS";
    private static final int SMALLEST = 500;
    private static final int SLOW = 900;
    private static final int FAST = 700;

    private static final int LARGEST = 1200;

    private ShapeableImageView main_IMG_background;
    private ExtendedFloatingActionButton main_FAB_left;
    private ExtendedFloatingActionButton main_FAB_right;
    private ShapeableImageView[] main_IMG_hearts;
    private ShapeableImageView[] main_IMG_PlayerPos;
    private ShapeableImageView[][] main_IMG_obstacles;
    private GameManager gameManager;
    private static long delay = SLOW;
    private Timer timerObs = new Timer();
    private String playerName = "";
    private MaterialTextView main_LBL_score;
    private FusedLocationProviderClient fusedLocationClient;
    private TiltDetector tiltDetector;
    private SoundManager obstacleSound;
    private SoundManager ramenSound;
    private SoundManager healthSound;
    private boolean isFast;
    private boolean buttons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent previousScreen = getIntent();
        isFast = previousScreen.getBooleanExtra(KEY_ISFAST, false);
        buttons = previousScreen.getBooleanExtra(KEY_BUTTONS, true);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        findViews();
        initViews(isFast, buttons);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initTimers();
        if (!buttons)
            tiltDetector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!buttons)
            tiltDetector.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerObs != null) {
            timerObs.cancel();
        }
    }

    private void initTimers() {
        Log.d("#######", "initTimers: "+delay);
        if (timerObs == null)
            return;
        timerObs = new Timer();

        timerObs.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    gameManager.moveObstacles();
                    refresh_UI();
                });
            }
        }, 0, delay);

    }

    private void refresh_UI() {
        updateObstacles();
        updatePlayerPos();
        switch (gameManager.checkCollision()) {
            case RAMEN:
                ramenSound.playSound(false);
                break;
            case OBSTACLE:
                obstacleSound.playSound(false);
                decreaseHeart();
                break;
            case HEART:
                healthSound.playSound(false);
                addHeart();
                break;
            default:
                break;
        }
        //updateLife();
        updateScore();

    }

    private void updateScore() {
        main_LBL_score.setText(String.valueOf(gameManager.getScore()));
    }


    private void updatePlayerPos() {
        int playerPos = gameManager.getCurrentPlayerIndex();
        for (int i = 0; i < main_IMG_obstacles[0].length; i++)
            main_IMG_PlayerPos[i].setImageResource(0);
        main_IMG_PlayerPos[playerPos].setImageResource(R.drawable.goku_young);
    }


    private void updateObstacles() {
        Obstacle_status[][] obstacles = gameManager.getMatrixStatuses();
        for (int i = 0; i < obstacles.length; i++) {
            for (int j = 0; j < obstacles[0].length; j++) {
                switch (obstacles[i][j]) {
                    case RAMEN:
                        main_IMG_obstacles[i][j].setImageResource(R.drawable.ramen);
                        break;
                    case HEART:
                        main_IMG_obstacles[i][j].setImageResource(R.drawable.heart);
                        break;
                    case OBSTACLE:
                        main_IMG_obstacles[i][j].setImageResource(R.drawable.fireball_obstacle);
                        break;
                    case NOTHING:
                        main_IMG_obstacles[i][j].setImageResource(0);
                }
            }
        }
    }

    private void decreaseHeart() {
        int hits = gameManager.getHits();
        main_IMG_hearts[gameManager.getLife() - hits].setVisibility(View.INVISIBLE);
        if (hits == main_IMG_hearts.length)
            alertDialog();

    }

    private void addHeart() {
        int hits = gameManager.getHits();
        main_IMG_hearts[gameManager.getLife() - hits - 1].setVisibility(View.VISIBLE);
    }

    private void updateLife() {
        int hits = gameManager.getHits();
        if (hits == main_IMG_hearts.length) {
            alertDialog();
        } else {
            for (int i = 1; i <= hits; i++)
                main_IMG_hearts[main_IMG_hearts.length - i].setVisibility(View.INVISIBLE);
            for (int i = 0; i < main_IMG_hearts.length - hits; i++)
                main_IMG_hearts[i].setVisibility(View.VISIBLE);
        }
    }

    private void navigateHighscoreActivity() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putExtra(ScoreActivity.KEY_PLAYER_NAME, playerName);
        startActivity(scoreIntent);
        finish();
    }

    public void alertDialog() {
        if (timerObs != null) {
            timerObs.cancel();
        }
        timerObs = null;
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_message)
                .setView(input)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_continue, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String temp = input.getText().toString().trim();
                        if (TextUtils.isEmpty(temp)) {
                            input.setError(getString(R.string.dialog_error));
                        } else {
                            playerName = temp;
                            dialog.dismiss();
                            insertHighScore();
                        }
                    }
                });
            }
        });
        dialog.show();


    }

    private void initViews(boolean isFast, boolean buttons) {
        initSounds();
        Glide.with(this).load(R.drawable.clouds).placeholder(R.drawable.ic_launcher_foreground).centerCrop().into(main_IMG_background);
        if (buttons) {
            main_FAB_right.setOnClickListener(view -> {
                gameManager.movePlayerRight();
                updatePlayerPos();
            });
            main_FAB_left.setOnClickListener(view -> {
                gameManager.movePlayerLeft();
                updatePlayerPos();
            });
        } else {
            main_FAB_right.setVisibility(View.INVISIBLE);
            main_FAB_left.setVisibility(View.INVISIBLE);
            initTiltDetector();
        }
        if (isFast) delay = FAST;
        else delay = SLOW;
    }

    private void initSounds() {
        obstacleSound = new SoundManager(this, R.raw.kiblast);
        healthSound = new SoundManager(this, R.raw.health_pickup);
        ramenSound = new SoundManager(this, R.raw.ramen_slurp);
        gameManager = new GameManager(main_IMG_hearts.length);
    }

    private void initTiltDetector() {
        tiltDetector = new TiltDetector(this, new TiltCallbacks() {
            @Override
            public void tiltLeft() {
                gameManager.movePlayerLeft();
            }

            @Override
            public void tiltRight() {
                gameManager.movePlayerRight();
            }

            @Override
            public void tiltInward() {
                makeGameSlower();
            }

            @Override
            public void tiltOutward() {
                makeGameFaster();
            }

        });
    }

    private void makeGameSlower() {
        if (delay < LARGEST) {
            //delay += 50;
            delay = LARGEST;
            restartTimer();
        }
        //delay -= 50;
        /*
            delay = SLOWEST
         */

    }

    private void makeGameFaster() {
        if (delay > SMALLEST) {
            delay=SMALLEST;
            //delay -=50 ;
            restartTimer();
        }
        //delay += 50;
        /*
            delay = FASTEST
         */
    }

    private void restartTimer() {
        if (timerObs != null)
            timerObs.cancel();
        initTimers();
    }

    public void insertHighScore() {
        HighScore score = new HighScore(Integer.parseInt(main_LBL_score.getText().toString()), playerName);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(this, location -> {
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                score.setPostion(new LatLng(location.getLatitude(), location.getLongitude()));
                SharedPreferencesManager manager = SharedPreferencesManager.getInstance();
                List<HighScore> list = manager.getHighscoreList();
                list.add(score);
                manager.putHighscoreList(list);
                navigateHighscoreActivity();
            }
        });


    }

    private void findViews() {
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_FAB_left = findViewById(R.id.main_FAB_left);
        main_FAB_right = findViewById(R.id.main_FAB_right);
        main_IMG_hearts = new ShapeableImageView[]{findViewById(R.id.main_IMG_heart1), findViewById(R.id.main_IMG_heart2), findViewById(R.id.main_IMG_heart3)};
        main_IMG_PlayerPos = new ShapeableImageView[]{findViewById(R.id.main_SIV_player_80), findViewById(R.id.main_SIV_player_81), findViewById(R.id.main_SIV_player_82), findViewById(R.id.main_SIV_player_83), findViewById(R.id.main_SIV_player_84)};
        main_IMG_obstacles = new ShapeableImageView[][]{{findViewById(R.id.main_SIV_obstacle_00), findViewById(R.id.main_SIV_obstacle_01), findViewById(R.id.main_SIV_obstacle_02), findViewById(R.id.main_SIV_obstacle_03), findViewById(R.id.main_SIV_obstacle_04)}, {findViewById(R.id.main_SIV_obstacle_10), findViewById(R.id.main_SIV_obstacle_11), findViewById(R.id.main_SIV_obstacle_12), findViewById(R.id.main_SIV_obstacle_13), findViewById(R.id.main_SIV_obstacle_14)}, {findViewById(R.id.main_SIV_obstacle_20), findViewById(R.id.main_SIV_obstacle_21), findViewById(R.id.main_SIV_obstacle_22), findViewById(R.id.main_SIV_obstacle_23), findViewById(R.id.main_SIV_obstacle_24)}, {findViewById(R.id.main_SIV_obstacle_30), findViewById(R.id.main_SIV_obstacle_31), findViewById(R.id.main_SIV_obstacle_32), findViewById(R.id.main_SIV_obstacle_33), findViewById(R.id.main_SIV_obstacle_34)}, {findViewById(R.id.main_SIV_obstacle_40), findViewById(R.id.main_SIV_obstacle_41), findViewById(R.id.main_SIV_obstacle_42), findViewById(R.id.main_SIV_obstacle_43), findViewById(R.id.main_SIV_obstacle_44)}, {findViewById(R.id.main_SIV_obstacle_50), findViewById(R.id.main_SIV_obstacle_51), findViewById(R.id.main_SIV_obstacle_52), findViewById(R.id.main_SIV_obstacle_53), findViewById(R.id.main_SIV_obstacle_54)}, {findViewById(R.id.main_SIV_obstacle_60), findViewById(R.id.main_SIV_obstacle_61), findViewById(R.id.main_SIV_obstacle_62), findViewById(R.id.main_SIV_obstacle_63), findViewById(R.id.main_SIV_obstacle_64)}, {findViewById(R.id.main_SIV_obstacle_70), findViewById(R.id.main_SIV_obstacle_71), findViewById(R.id.main_SIV_obstacle_72), findViewById(R.id.main_SIV_obstacle_73), findViewById(R.id.main_SIV_obstacle_74)}, {findViewById(R.id.main_SIV_obstacle_80), findViewById(R.id.main_SIV_obstacle_81), findViewById(R.id.main_SIV_obstacle_82), findViewById(R.id.main_SIV_obstacle_83), findViewById(R.id.main_SIV_obstacle_84)}

        };

    }
}