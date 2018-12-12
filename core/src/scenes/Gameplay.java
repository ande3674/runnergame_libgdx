package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.runnergame.GameMain;

import helpers.GameInfo;
import helpers.GameManager;
import huds.UIHud;
import obstacles.Obstacle;
import obstacles.ObstacleController;
import player.Player;

// ContactListener picks up contact between the game elements

public class Gameplay implements Screen, ContactListener {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Sprite[] bgs;
    private float lastXPosition;

    private boolean touchedForTheVeryFirstTime = false; // madonna

    private ObstacleController obstacleController;

    private Player player;
    private float lastPlayerPos;

    private UIHud hud;

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

        // create UI
        hud = new UIHud(game);

        // in our world we want the force of gravity
        world = new World(new Vector2(0, -9.8f), true);
        // inform the world that the contact listener is the gameplay class...
        world.setContactListener(this);

        // create obstacle controller
        obstacleController = new ObstacleController(world);

        player = obstacleController.positionThePlayer(player);
        lastPlayerPos = player.getX(); // TODO OOOOOOOOOOOOOOOOOOOO

        createBackgrounds();
    }

    /*
    *
    GAME DESIGNER DEFINED METHODS ...
    *
    */
    void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.setWalking(true);
            player.movePlayer(2f, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.setWalking(true);
            player.movePlayer(0, 2f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setWalking(true);
            player.movePlayer(-2f, 0);
        }else {
            player.setWalking(false);
        }
    }

    void checkForFirstTouch() {

        if(!touchedForTheVeryFirstTime){
            if (Gdx.input.justTouched()){
                touchedForTheVeryFirstTime = true;
                GameManager.getInstance().isPaused = false;
            }
        }
    }


    void update(float dt){

        checkForFirstTouch();

        if (!GameManager.getInstance().isPaused) {
            // Start a new game with initial values...
            handleInput(dt);
            moveCamera();
            checkBackgroundsOutOfBounds();
            obstacleController.setCameraX(mainCamera.position.x);
            obstacleController.createAndArrangeNewObstacles();
            obstacleController.removeOffScreenCollectables();
            checkPlayerBounds();
            countScore();
        }
    }
    // moves the camera down along the repeated background images
    void moveCamera(){
        mainCamera.position.x += 1.5f;
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

    void checkPlayerBounds() {
        // Check player out of bounds to the left...
        if (player.getX() + player.getWidth()/2f - GameInfo.WIDTH / 2f + GameInfo.WIDTH < mainCamera.position.x) {
            // debug...
//            System.out.println("Player out of bounds to the left");
//            GameManager.getInstance().isPaused = true;
            if (!player.isDead()){
                playerDied();
            }
        }

        // Check player out of bounds to right...
        if (player.getX() + player.getWidth()/2f - GameInfo.WIDTH / 2f > mainCamera.position.x) {
            // debug...
//            System.out.println("Player out of bounds to the right");
//            GameManager.getInstance().isPaused = true;
            if (!player.isDead()){
                playerDied();
            }
        }

        if(player.getY() > GameInfo.HEIGHT) { // actually I don't think we will kill him when he goes too high sooo leave this...
            // player out of bounds on the top...
            //System.out.println("Player out of bounds on top");
            //GameManager.getInstance().isPaused = true;
        } else if (player.getY() + player.getHeight() < 0){ // TODO figure out the bottom situation
            // player out of bounds on the bottom
//            System.out.println("Player out of bounds on bottom");
//            GameManager.getInstance().isPaused = true;
            if (!player.isDead()){
                playerDied();
            }
        }
        // TODO Keep player in bounds on the bottom..
//        if (player.getY() <= 50) {
//            player.updatePlayer();
//            // debug...
//            System.out.println("Player is on the bottom of the screen");
//            GameManager.getInstance().isPaused = true;
//        }
    }

    void countScore() {
        if (lastPlayerPos < player.getX()){
            hud.incrementScore(1);
            lastPlayerPos = player.getX();
        }
    }

    // player died method
    void playerDied() {

        // player died code
        // stop game..
        GameManager.getInstance().isPaused = true;

        // decrement life
        // do this in UI hud
        hud.decrementLife();

        player.setDead(true);

        player.setPosition(1200, 1200); // simulate the player being removed from the scene

        if (GameManager.getInstance().lifeScore < 0){
            // player has no more lives left

            // new high score?????

            // show end score

            // load main menu
            game.setScreen(new MainMenu(game));
        } else {
            // reload the game so player can keep playing
            game.setScreen(new Gameplay(game));
        }

    }


    // SCREEN INTERFACE METHODS...

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
        obstacleController.drawCollectables(game.getBatch());

        player.drawPlayerIdle(game.getBatch());
        player.drawPlayerAnimation(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        // it is important that the following two statements come after the two before...
        // projection matrix needs to be set to the main camera so we follow its
        // movement on the screen
        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        player.updatePlayer();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        world.dispose();
        bgs[0].getTexture().dispose();
        bgs[1].getTexture().dispose();
        bgs[2].getTexture().dispose();
        player.getTexture().dispose();
        debugRenderer.dispose();
    }


    // CONTACTLISTENER METHODS.........
    // Dealing with contact in the game...
    @Override
    public void beginContact(Contact contact) {

        Fixture body1, body2;

        // Make sure body1 is always the player...
        if(contact.getFixtureA().getUserData() == "Player"){
            // Player is FixtureA ...
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else { // player is FixtureB ...
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }
        if (body1.getUserData() == "Player" && body2.getUserData() == GameInfo.COIN){
            // Player collided with a coin...
            // we want to remove coins when they collide with the player...
            // we also want to increment coins
            hud.incrementCoins();
            body2.setUserData("Remove");
            obstacleController.removeCollectables();
            // debug...
            //System.out.println("COIN!");
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == GameInfo.LIFE) {
            // increment life count and remove the life from the screen
            hud.incrementLives();
            body2.setUserData("Remove");
            obstacleController.removeCollectables();
            // debug...
            //System.out.println("LIFE.");
        }

        if (body1.getUserData() == "Player" && body2.getUserData() == GameInfo.SUN) {
            // hitting a sun means we are dead...
            if (!player.isDead()){
                playerDied();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
