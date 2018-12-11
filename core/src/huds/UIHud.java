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

public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, lifeImg, scoreImg;

    private Label coinLbl, lifeLbl, scoreLbl, scoreLbl2;

    private ImageButton pauseBtn;

    public UIHud(GameMain game) {

        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());

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
        coinImg = new Image(new Texture("Collectables/Coin1.png"));
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
            }
        });

    }

    public Stage getStage() {
        return stage;
    }
}
