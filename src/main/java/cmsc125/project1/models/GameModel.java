package cmsc125.project1.models;

public class GameModel {
    // Will add logic later
    private static final int MAX_LIVES = 7;
    private int lives = MAX_LIVES;

    public void resetLives() {lives = MAX_LIVES;}
    public void reduceLives() {lives--;}
    public int getLives() {return lives;}
}
