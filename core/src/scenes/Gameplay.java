package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;

public class Gameplay implements Screen {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private Sprite[] bgs;
    private float lastXPosition;

    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        createBackgrounds();
    }

    /*

    GAME DESIGNER DEFINED METHODS ...

    */
    void update(float dt){

        moveCamera();
        checkBackgroundsOutOfBounds();
    }
    // moves the camera down along the repeated background images
    void moveCamera(){
        mainCamera.position.x += 3;
    }
    // this method repeats our background image 3 times so the game can scroll
    void createBackgrounds() {
        bgs = new Sprite[3];

        for (int i = 0 ; i < bgs.length ; i++){
            bgs[i] = new Sprite(new Texture("Backgrounds/desert_BG.png"));
            // we want to stack 3 of the background images so the player can travel down
            // the scene
            bgs[i].setPosition(i*bgs[i].getWidth(), 0);
            lastXPosition = bgs[i].getX();
        }
    }

    void drawBackgrounds(){
        for (int i = 0 ; i < bgs.length ; i++) {
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }
    // defined by game maker to check if we have passed the end of our background images
    // called in the update() method so it continuously checks that we're not at the end of the background images
    void checkBackgroundsOutOfBounds() {
        for (int i = 0 ; i < bgs.length ; i++) {
            if ((bgs[i].getX() + bgs[i].getWidth()/2f + bgs[i].getWidth()) < mainCamera.position.x) { // -5 px because we don't want to see the tiny red line when the backgrounds repeat
                float newPosition = bgs[i].getWidth() + lastXPosition;

                bgs[i].setPosition(newPosition, 0);
                lastXPosition = newPosition;

            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBackgrounds();

        game.getBatch().end();

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
