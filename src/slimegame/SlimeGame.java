package slimegame;

import processing.core.PApplet;
import processing.core.PImage;

public class SlimeGame extends PApplet {

    PImage slime;
    PImage bg;
    PImage slimeSurprised;
    PImage barrel;

    // Constants
    final int SLIME_START_X = 70;
    final int SLIME_START_Y = 300;
    final int BARREL_START_Y = 300;
    final float INITIAL_SPEED = 8;
    final float SPEED_INCREASE = 0.8f;
    final float RATE_OF_DIFFICULTY = 5;

    // Slime position
    int slimeX = SLIME_START_X;
    int slimeY = SLIME_START_Y;

    // Background position
    int bgX = 0;
    int bgY = 0;

    // Barrel position
    int barrelX;
    int barrelY = BARREL_START_Y;

    // Game state variables
    float speed = INITIAL_SPEED;
    int jumpHeight;
    int startTime;
    int timer;
    int difficultyTime = 0;
    int score = 0;
    int highScore = 0;

    enum GameState {
        GAMEOVER, RUNNING, MENUSCREEN
    }

    static GameState currentState;

    public static void main(String[] args) {
        PApplet.main("slimegame.SlimeGame");
    }

    public void settings() {
        size(700, 400);
    }

    public void setup() {
        loadImageAssets();
        barrel.resize(200, 0);
        startTime = millis();
        currentState = GameState.MENUSCREEN;
    }

    public void draw() {
        switch (currentState) {
            case MENUSCREEN:
                drawMenuScreen();
                break;
            case RUNNING:
                drawRunningScreen();
                break;
            case GAMEOVER:
                drawGameOverScreen();
                break;
        }
    }

    private void loadImageAssets() {
        slime = loadImage("Images/try.png");
        bg = loadImage("Images/bg.png");
        barrel = loadImage("Images/barrel.png");
        slimeSurprised = loadImage("Images/slime surprised.png");
    }

    private void drawMenuScreen() {
        background(0);
        fill(255);
        textSize(32);
        textAlign(CENTER);
        text("Slime Game", width / 2, height / 2 - 40);
        textSize(20);
        text("Click to Start", width / 2, height / 2);
    }

    private void drawRunningScreen() {
        drawBackground();
        drawSlime();
        createObstacles();
        timer = (millis() - startTime) / 1000;
        drawScore();
        increaseDifficulty();
    }

    private void drawSlime() {
        imageMode(CENTER);
        image(slime, slimeX, slimeY);
        if (slimeY <= SLIME_START_Y) {
            image(slimeSurprised, slimeX, slimeY);
            jumpHeight += 1;
            slimeY += jumpHeight;
        }
    }

    private void drawBackground() {
        imageMode(CORNER);
        image(bg, bgX, bgY);
        image(bg, bgX + bg.width, 0);
        bgX -= speed;
        if (bgX <= (bg.width * -1)) {
            bgX = 0;
        }
    }

    private void createObstacles() {
        imageMode(CENTER);
        image(barrel, barrelX, barrelY);
        barrelX -= speed;
        if (barrelX < 0) {
            barrelX = width;
        }
        if (collidesWithSlime()) {
            endGame();
        }
    }

    private boolean collidesWithSlime() {
        return (abs(slimeX - barrelX) < 40) && (abs(slimeY - barrelY) < 80);
    }

    private void endGame() {
        score = timer;
        if (score > highScore) {
            highScore = score;
        }
        currentState = GameState.GAMEOVER;
    }

    private void drawScore() {
        fill(255, 255, 255);
        textAlign(CENTER);
        text("Score: " + timer, width - 70, 30);
    }

    private void increaseDifficulty() {
        if (timer % RATE_OF_DIFFICULTY == 0 && difficultyTime != timer) {
            speed += SPEED_INCREASE;
            difficultyTime = timer;
        }
    }

    private void drawGameOverScreen() {
        fill(255, 190, 190);
        noStroke();
        rect(width / 2 - 125, height / 2 - 80, 250, 160);
        fill(255, 100, 100);
        textAlign(CENTER);
        text("Game Over! ", width / 2, height / 2 - 50);
        text("Your Score: " + score, width / 2, height / 2 - 30);
        text("High Score: " + highScore, width / 2, height / 2 - 10);
    }

    public void mousePressed() {
        switch (currentState) {
            case MENUSCREEN:
                startGame();
                break;
            case RUNNING:
                makeSlimeJump();
                break;
            case GAMEOVER:
                resetGame();
                break;
        }
    }

    private void startGame() {
        currentState = GameState.RUNNING;
        startTime = millis();
    }

    private void makeSlimeJump() {
        if (slimeY >= SLIME_START_Y) {
            jumpHeight = -15;
            slimeY += jumpHeight;
        }
    }

    private void resetGame() {
        barrelX = 0;
        bgX = 0;
        startTime = millis();
        speed = INITIAL_SPEED;
        currentState = GameState.RUNNING;
    }
}
