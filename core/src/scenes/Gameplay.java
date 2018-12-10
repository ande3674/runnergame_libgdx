package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import obstacles.Obstacle;
import obstacles.ObstacleController;
import player.Player;

public class Gameplay implements Screen {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Sprite[] bgs;
    private float lastXPosition;

    private ObstacleController obstacleController;

    private Player player;

    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        box2DCamera = new OrthographicCamera();

        // divide by GameInfo.PPM so that 100 px = 1 m
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        // in our world we want the force of gravity
        world = new World(new Vector2(0, -9.8f), true);

        // create obstacle controller
        obstacleController = new ObstacleController(world);

        player = obstacleController.positionThePlayer(player);

        createBackgrounds();
    }

    /*

    GAME DESIGNER DEFINED METHODS ...

    */
    void update(float dt){
        //moveCamera();
        checkBackgroundsOutOfBounds();
        obstacleController.setCameraX(mainCamera.position.x);
        obstacleController.createAndArrangeNewObstacles();
    }
    // moves the camera down along the repeated background images
    void moveCamera(){
        mainCamera.position.x += 2f;
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

        obstacleController.drawObstacles(game.getBatch());

        player.drawPlayer(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        //player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

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
