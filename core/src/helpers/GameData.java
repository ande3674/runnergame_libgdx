package helpers;

public class GameData {

    //variables that will keep track of our game data
    private int highscore;
    private int coinHighscore;
    private boolean easyDifficulty, mediumDifficulty, hardDifficulty;
    private boolean musicOn;


    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public int getCoinHighscore() {
        return coinHighscore;
    }

    public void setCoinHighscore(int coinHighscore) {
        this.coinHighscore = coinHighscore;
    }

    public boolean isEasyDifficulty() {
        return easyDifficulty;
    }

    public void setEasyDifficulty(boolean easyDifficulty) {
        this.easyDifficulty = easyDifficulty;
    }

    public boolean isMediumDifficulty() {
        return mediumDifficulty;
    }

    public void setMediumDifficulty(boolean mediumDifficulty) {
        this.mediumDifficulty = mediumDifficulty;
    }

    public boolean isHardDifficulty() {
        return hardDifficulty;
    }

    public void setHardDifficulty(boolean hardDifficulty) {
        this.hardDifficulty = hardDifficulty;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }
}
