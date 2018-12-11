package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
        scoreLbl = new Label("x2", new Label.LabelStyle(font, Color.WHITE));
        scoreLbl = new Label("Score:", new Label.LabelStyle(font, Color.WHITE));

    }

    void createImages() {
        coinImg = new Image(new Texture("Collectables/Coin1.png"));
        lifeImg = new Image(new Texture("Collectables/portrait.png"));
        // TODO find a score image scoreImg = new Image(new Texture("Collectables/..."));
    }
}
