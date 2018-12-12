package helpers;

public class GameManager {

    /*

    THIS IS A SINGLETON PATTERN
    The game mananger will take care of parts of the game that need to be managed by a Singleton

     */
    private static final GameManager ourInstance = new GameManager();

    // create some variables to control the game...
    // Game started from main means we set up initial values
    // game started from pause means we preserve lives, coins and score...
    public boolean gameStartedFromMain, isPaused = true; // The gameplay class will set this to false when the game is first initiated
    public int lifeScore, coinScore, score;

    private GameManager() {
    }

    public static GameManager getInstance() {
        return ourInstance;
    }

}
