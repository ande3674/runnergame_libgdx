package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import scenes.MainMenu;

public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg, pausePanel;

    private Label coinLbl, lifeLbl, scoreLbl, scoreLbl2;

    private ImageButton pauseBtn, resumeBtn, quitBtn;

    public UIHud(GameMain game) {

        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());

        // important for registering clicks!!!
        Gdx.input.setInputProcessor(stage);

        // call the create methods...
        createLabels();
        createImages();
        createButtonAndAddListener();

        // create a table to hold the coin and life information...
        Table lifeAndCoinTable = new Table();
        lifeAndCoinTable.top().left();
        lifeAndCoinTable.setFillParent(true);

        lifeAndCoinTable.add(lifeImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(lifeLbl).padLeft(10).padTop(10);
        lifeAndCoinTable.row();
        lifeAndCoinTable.add(coinImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(coinLbl).padLeft(10).padTop(10);

        // create table for the score information...
        Table scoreTable = new Table();
        scoreTable.top().right();
        scoreTable.setFillParent(true);
        scoreTable.add(scoreLbl2).padRight(20).padTop(15);
        scoreTable.row();
        scoreTable.add(scoreLbl).padRight(20).padTop(20);

        // add actors to the stage...
        stage.addActor(lifeAndCoinTable);
        stage.addActor(scoreTable);
        stage.addActor(pauseBtn);


    }

    void createLabels() {
        // Create fonts...
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40; // can change font size here
        BitmapFont font = generator.generateFont(parameter); // can make as many font styles as needed

        // create labels...
        coinLbl = new Label("x0", new Label.LabelStyle(font, Color.WHITE));
        lifeLbl = new Label("x1", new Label.LabelStyle(font, Color.WHITE));
        scoreLbl = new Label("100", new Label.LabelStyle(font, Color.WHITE));
        scoreLbl2 = new Label("Score:", new Label.LabelStyle(font, Color.WHITE));

    }

    void createImages() {
        coinImg = new Image(new Texture("Collectables/Coin.png"));
        lifeImg = new Image(new Texture("Collectables/portrait.png"));
        // TODO find a score image scoreImg = new Image(new Texture("Collectables/..."));
    }

    void createButtonAndAddListener() {
        pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/pause.png"))));

        pauseBtn.setPosition(640, 360, Align.center);

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // code for pausing the game
                createPausePanel();
            }
        });

    }

    void createPausePanel() {
        // create
        pausePanel = new Image(new Texture("Buttons/Pause/Pause Panel.png"));
        resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Pause/Resume.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Pause/Quit 2.png"))));

        // position
        pausePanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 20, Align.center);
        resumeBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 30, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 100, Align.center);

        // add listeners
        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // quitting --> set screen to the main menu...
                game.setScreen(new MainMenu(game));
            }
        });

        // add actors to the stage...
        stage.addActor(pausePanel);
        stage.addActor(resumeBtn);
        stage.addActor(quitBtn);
    }

    void removePausePanel() {
        pausePanel.remove();
        resumeBtn.remove();
        quitBtn.remove();
    }

    public Stage getStage() {
        return stage;
    }
}
