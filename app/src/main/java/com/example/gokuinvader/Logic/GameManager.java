package com.example.gokuinvader.Logic;


import com.example.gokuinvader.Models.Obstacle_status;
import com.example.gokuinvader.Utils.SignalManager;

import java.util.Random;


public class GameManager {

    private int life;
    private static final int ROWS = 9;
    private static final int COLS = 5;
    private int currentPlayerIndex = 1;
    private final Obstacle_status[][] matrixStatuses;
    private int hits = 0;
    private final Random rnd = new Random();
//    private MockNeat mock1 = MockNeat.threadLocal();
//    private final Probabilities<Obstacle_status> probs = new Probabilities<>(mock1,Obstacle_status.class)
//            .add(0.4,Obstacle_status.OBSTACLE)
//            .add(0.25,Obstacle_status.NOTHING)
//            .add(0.1,Obstacle_status.HEART)
//            .add(0.25,Obstacle_status.RAMEN);

    int score = 0;

    public GameManager(int life) {
        this.life = life;
        this.matrixStatuses = new Obstacle_status[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                this.matrixStatuses[i][j] = Obstacle_status.NOTHING;
            }
        }
    }

    public void movePlayerRight() {
        if (currentPlayerIndex != COLS - 1) {
            currentPlayerIndex++;
        }
    }

    public void movePlayerLeft() {
        if (currentPlayerIndex != 0) {
            currentPlayerIndex--;
        }
    }

    public int getScore() {
        return score;
    }

    public void moveObstacles() {
        score += 10;
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                if (matrixStatuses[i][j] != Obstacle_status.NOTHING) {
                    if (i != ROWS - 1) {
                        matrixStatuses[i + 1][j] = matrixStatuses[i][j];
                    }
                }
                matrixStatuses[i][j] = Obstacle_status.NOTHING;

            }
        }

        int newObstacle = rnd.nextInt(COLS);
        int kindOfObstacle;
        if (hits == 0)
            kindOfObstacle = rnd.nextInt(Obstacle_status.values().length - 1);
        else
            kindOfObstacle = rnd.nextInt(Obstacle_status.values().length);
        matrixStatuses[0][newObstacle] = Obstacle_status.values()[kindOfObstacle];

    }

    public Obstacle_status getRandomObstacle() {
               /*
        Probability:
        Obstacle -> 40%
        Nothing -> 20%
        Ramen -> 30%
        Heart -> 10%
         */
        int num = hits == 0 ? 90 : 100;
        num = rnd.nextInt(num);
        if (num > 0 && num <= 40)
            return Obstacle_status.OBSTACLE;
        else if (num > 40 && num <= 60)
            return Obstacle_status.NOTHING;
        else if (num > 60 && num <= 90)
            return Obstacle_status.RAMEN;
        else
            return Obstacle_status.HEART;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Obstacle_status[][] getMatrixStatuses() {
        return matrixStatuses;
    }

    public Obstacle_status checkCollision() {
        Obstacle_status kindOfCollision = matrixStatuses[ROWS - 1][currentPlayerIndex];
        switch (kindOfCollision) {
            case OBSTACLE:
                SignalManager.getInstance().vibrate(500);
                SignalManager.getInstance().toast("Ouch, I need to be faster");
                hits++;
                return Obstacle_status.OBSTACLE;
            case HEART:
                SignalManager.getInstance().vibrate(500);
                SignalManager.getInstance().toast("I feel good again");
                score += 50;
                if(hits!=0)
                    hits--;
                return Obstacle_status.HEART;
            case RAMEN:
                SignalManager.getInstance().vibrate(500);
                SignalManager.getInstance().toast("Yum");
                score += 100;
                return Obstacle_status.RAMEN;
            default:
                return Obstacle_status.NOTHING;

        }
    }

    public int getLife() {
        return life;
    }

    public GameManager setLife(int life) {
        this.life = life;
        return this;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

}
