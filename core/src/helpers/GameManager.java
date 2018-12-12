package helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager {

    /*
    *
    THIS IS A SINGLETON PATTERN
    The game mananger will take care of parts of the game that need to be managed by a Singleton
    *
     */
    private static final GameManager ourInstance = new GameManager();

    public GameData gameData;
    // Save game data in a Json object...
    private Json json = new Json();
    // file to save data
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json"); // local files are read and write

    // create some variables to control the game...
    // Game started from main means we set up initial values
    // game started from pause means we preserve lives, coins and score...
    public boolean gameStartedFromMain, isPaused = true; // The gameplay class will set this to false when the game is first initiated
    public int lifeScore, coinScore, score;

    private GameManager() {
    }

    public void initializeGameData() {
        // check if file handle exists to see if we have initialized data yet
        // or if this is the first time
        if (!fileHandle.exists()) {
            // first time ...
            // set data to initial values
            gameData = new GameData();
            gameData.setHighscore(0);
            gameData.setCoinHighscore(0);
            gameData.setEasyDifficulty(false);
            gameData.setMediumDifficulty(true);
            gameData.setHardDifficulty(false);
            gameData.setMusicOn(false);
            // save the data
            saveData();
        } else {
            loadData();
        }
    }

    // methods to save and load data
    public void saveData() {
        if (gameData != null){
            // writes it so it's readable to human..
            fileHandle.writeString(json.prettyPrint(gameData), false);
            // encodes so that users can't manipulate the game data
            //fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
        }
    }
    public void loadData() {
        // reads english text
        gameData = json.fromJson(GameData.class, fileHandle.readString());
        // reads data that was encoded
        //gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
    }

    // Method to check for new high scores...


    public void checkForNewHighScore() {
        int oldHighscore = gameData.getHighscore();
        int oldCoinscore = gameData.getCoinHighscore();

        if (oldHighscore < score) {
            gameData.setHighscore(score);
        }
        if (oldCoinscore < coinScore) {
            gameData.setCoinHighscore(coinScore);
        }
        saveData();
    }

    public static GameManager getInstance() {
        return ourInstance;
    }

}
