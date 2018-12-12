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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import scenes.MainMenu;

public class HighscoreButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label scoreLabel, coinLabel;
    private Label score, coin;

    private ImageButton backBtn;

    public HighscoreButtons(GameMain game){
        this.game = game;
        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(gameViewport, game.getBatch());

        // need this to register button clicks..
        Gdx.input.setInputProcessor(stage); // stage will register the clicks

        createAndPositionUIElements();
        addAllListeners();

        // have to add actors to the stage...
        stage.addActor(backBtn);
        stage.addActor(scoreLabel);
        stage.addActor(coinLabel);
        stage.addActor(score);
        stage.addActor(coin);


    }

    private void addAllListeners() {
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });
    }

    void createAndPositionUIElements() {

        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/arrow_left.png"))));

        // FONT CODE...
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;

        BitmapFont font = generator.generateFont(parameter);

        scoreLabel = new Label("100", new Label.LabelStyle(font, Color.WHITE));
        coinLabel = new Label("100", new Label.LabelStyle(font, Color.WHITE));

        score = new Label("High score: ", new Label.LabelStyle(font, Color.WHITE));
        coin = new Label("High coins: ", new Label.LabelStyle(font, Color.WHITE));

        backBtn.setPosition(120, 70, Align.center);

        score.setPosition(GameInfo.WIDTH / 2f - 180, GameInfo.HEIGHT / 2f + 20);
        coin.setPosition(GameInfo.WIDTH / 2f - 180, GameInfo.HEIGHT / 2f - 20);

        scoreLabel.setPosition(GameInfo.WIDTH / 2f + 40, GameInfo.HEIGHT / 2f + 20);
        coinLabel.setPosition(GameInfo.WIDTH / 2f + 40, GameInfo.HEIGHT / 2f - 20);

    }

    public Stage getStage(){
        return this.stage;
    }
}
