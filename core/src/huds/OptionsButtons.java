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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import scenes.MainMenu;

public class OptionsButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    // buttons
    private ImageButton easyBtn;
    private ImageButton mediumBtn;
    private ImageButton hardBtn;
    private ImageButton backBtn;

    // check image
    private Image img;

    // label
    private Label mainLabel;

    public OptionsButtons(GameMain game){
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        // need this to register button clicks..
        Gdx.input.setInputProcessor(stage); // stage will register the clicks with this statement

        createAndPositionButtons();
        addAllListeners();

        // have to add actors to the stage...
        stage.addActor(easyBtn);
        stage.addActor(mediumBtn);
        stage.addActor(hardBtn);
        stage.addActor(backBtn);
        stage.addActor(img);
        stage.addActor(mainLabel);

    }

    private void createAndPositionButtons() {
        // create buttons...
        easyBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Button.png"))));
        mediumBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Button.png"))));
        hardBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Button.png"))));
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Button.png"))));

        // FONT CODE...
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        BitmapFont font = generator.generateFont(parameter);

        // create image...
        //img = new Image(new SpriteDrawable(new Sprite(new Texture(""))));
        img = new Image(new Texture("Buttons/Check1.png"));

        // Create the label...
        mainLabel = new Label("Difficulty: ", new Label.LabelStyle(font, Color.BLACK));

        // position buttons
        easyBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 150, Align.center);
        mediumBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        hardBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f -50, Align.center);
        backBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 150, Align.center);

        // position check image
        img.setPosition(GameInfo.WIDTH / 2f + 70, mediumBtn.getY() + 45, Align.center);

        // position label...
        mainLabel.setPosition(GameInfo.WIDTH / 2f - 230, GameInfo.HEIGHT / 2f + 150, Align.center);
    }

    private void addAllListeners(){

        easyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                img.setY(easyBtn.getY() + 30);
            }
        });

        mediumBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                img.setY(mediumBtn.getY() + 30);
            }
        });

        hardBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                img.setY(hardBtn.getY() + 30);
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

    }

    public Stage getStage() {
        return stage;
    }
}
