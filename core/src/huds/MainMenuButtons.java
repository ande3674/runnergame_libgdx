package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import scenes.Gameplay;
import scenes.Highscore;
import scenes.MainMenu;
import scenes.Options;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    // buttons
    private ImageButton playBtn;
    private ImageButton highscoreBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;

    public MainMenuButtons(GameMain game){
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        // need this to register button clicks..
        Gdx.input.setInputProcessor(stage); // stage will register the clicks

        createAndPositionButtons();
        addAllListeners();

        // have to add actors to the stage...
        stage.addActor(playBtn);
        stage.addActor(highscoreBtn);
        stage.addActor(optionsBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);

        checkMusic();
    }

    private void createAndPositionButtons() {
        // create buttons
        playBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/play button.png"))));
        highscoreBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Score button.png"))));
        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Option button.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Quit button 2.png"))));
        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Music/Music On.png"))));

        // position buttons
        playBtn.setPosition(GameInfo.WIDTH / 2f - 100, GameInfo.HEIGHT / 2f + 105, Align.center);
        highscoreBtn.setPosition(GameInfo.WIDTH / 2f - 100, GameInfo.HEIGHT / 2f - 20, Align.center);
        optionsBtn.setPosition(GameInfo.WIDTH / 2f + 100, GameInfo.HEIGHT / 2f + 105, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f + 100, GameInfo.HEIGHT / 2f - 20, Align.center);
        musicBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 105, Align.center);
    }

    void addAllListeners() {

        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.getInstance().gameStartedFromMain = true;
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                });

                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f));// fade out
                sa.addAction(run);

                stage.addAction(sa);

                // debug..
                //System.out.println("Button clicked.");
            }
        });

        highscoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Highscore(game));
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new Options(game));
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameManager.getInstance().gameData.isMusicOn()){
                    GameManager.getInstance().gameData.setMusicOn(false);
                    GameManager.getInstance().stopMusic();
                } else {
                    GameManager.getInstance().gameData.setMusicOn(true);
                    GameManager.getInstance().playMusic();
                }

                GameManager.getInstance().saveData();
            }
        });
    }

    void checkMusic(){
        if (GameManager.getInstance().gameData.isMusicOn()) {
            GameManager.getInstance().playMusic();
        }
    }

    public Stage getStage() {
        return stage;
    }
}
