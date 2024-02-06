package com.example.gokuinvader.Logic;


import java.util.Random;


public class GameManager {

    private int life;
    public static final int ROWS = 7;
    public static final int COLS = 3;
    private int currentPlayerIndex = 1;
    private final boolean[][] matrixStatuses;
    private int hits = 0;
    private final Random rnd = new Random();
    private int isBreak = 0;


    public GameManager(int life) {
        this.life = life;
        this.matrixStatuses = new boolean[ROWS][COLS];
        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLS - 1; j++) {
                this.matrixStatuses[i][j] = false;
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

    public void moveObstacles() {
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                if (matrixStatuses[i][j]) {
                    matrixStatuses[i][j] = false;
                    if (i != ROWS - 1) {
                        matrixStatuses[i + 1][j] = true;
                    }
                }

            }
        }
        isBreak++;
        if (isBreak != 3) {
            int newObstacle = rnd.nextInt(COLS);
            matrixStatuses[0][newObstacle] = true;
        } else
            isBreak = 0;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean[][] getMatrixStatuses() {
        return matrixStatuses;
    }

    public boolean isCollision() {
        if (matrixStatuses[ROWS - 1][currentPlayerIndex]) {
            hits++;
            return true;
        }
        return false;
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
