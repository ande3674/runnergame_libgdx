package obstacles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import collectables.Collectables;
import helpers.GameInfo;
import player.Player;

public class ObstacleController {

    // Need a physics world in order to create bodies...
    private World world;

    // array of obstacles to add to our game...
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Platform> platforms = new Array<Platform>();
    // array of collectable items...
    private Array<Collectables> collectables = new Array<Collectables>();

    // Variables to control the platform placement...
    private Random r = new Random();
    private float minPlatformY, maxPlatformY;

    // keep track of the camera's X position to control our game obstacles
    private float cameraX;
    // keep track of the obstacles' last positions so we can add more as we go
    private float lastObstaclePositionX, lastPlatformPositionX;

    // CONSTANTS
    private final float DISTANCE_BETWEEN_OBSTACLES = 400f;
    private final float DISTANCE_BETWEEN_PLATFORMS = 500f;


    // Constructor
    public ObstacleController(World world) {
        this.world = world;
        minPlatformY = GameInfo.HEIGHT * .40f;
        maxPlatformY = GameInfo.HEIGHT * .60f;
        createObstacles();
        positionObstacles(true);
    }

    void createObstacles(){
        for (int i = 0 ; i < 6 ; i++){
            obstacles.add(new Obstacle(world, "kirby"));
        }
        for (int i = 0 ; i < 5 ; i++){
            platforms.add(new Platform(world, "button"));
        }
        obstacles.shuffle();
    }

    public void positionObstacles(boolean firstTimePositioning) {
        // Obstacles.....
        float y = 50;
        float positionX = 0;
        if (firstTimePositioning) {
            positionX = 10;
        }
        else {
            positionX = lastObstaclePositionX;
        }
        // Platforms...
        float platformPositionX = 0;
        if (firstTimePositioning){
            platformPositionX = 200f;
        } else {
            platformPositionX = lastPlatformPositionX;
        }


        // Place obstacles
        for (Obstacle o : obstacles) {
            // make sure we only reposition NEW obstacles and not the ones that
            // are still on / were already on the screen...
            if (o.getX() == 0 && o.getY() == 0){
                o.setSpritePosition(positionX, y);
                positionX += DISTANCE_BETWEEN_OBSTACLES;
                lastObstaclePositionX = positionX;
            }
        }

        float tempY = r.nextFloat() * (maxPlatformY - minPlatformY)  + minPlatformY;
        // Place platforms...
        for (Platform p : platforms){
            if (p.getX() == 0 && p.getY() == 0){
                p.setSpritePosition(platformPositionX, tempY);
                platformPositionX += DISTANCE_BETWEEN_PLATFORMS;
                tempY = r.nextFloat() * (maxPlatformY - minPlatformY) + minPlatformY;
                lastPlatformPositionX = platformPositionX;

                if (!firstTimePositioning){
                    int rand = r.nextInt(10); // get a random int 0-9
                    if (rand < 8){
                        int randC = r.nextInt(5);
                        if (randC < 1){
                            // TODO spawn a life, if life count is lower than 2
                            Collectables collectable = new Collectables(world, GameInfo.LIFE);
                            collectable.setCollectablesPosition(p.getX(), p.getY() + 40);
                            collectables.add(collectable);
                        } else {
                            // spawn a coin
                            Collectables collectable = new Collectables(world, GameInfo.COIN);
                            collectable.setCollectablesPosition(p.getX(), p.getY() + 40);
                            collectables.add(collectable);
                        }
                    }
                }
            }
        }

        // Add the collectables .....


        //delete later - testing adding collectables
        Collectables c1 = new Collectables(world, "Coin");
        Collectables c2 = new Collectables(world, "life");
        c1.setCollectablesPosition(platforms.get(1).getX(), platforms.get(1).getY() + 80);
        c2.setCollectablesPosition(platforms.get(1).getX(), platforms.get(1).getY() + 120);
        collectables.add(c1);
        collectables.add(c2);
    }

    // draw the platforms and obstacles...
    public void drawObstacles(SpriteBatch batch){
        for (Obstacle o : obstacles){
            batch.draw(o, o.getX() - o.getWidth()/2f, o.getY() - o.getHeight()/2f);
        }
        for (Platform p : platforms){
            batch.draw(p, p.getX() - p.getWidth()/2f, p.getY() - p.getHeight()/2f);
        }
    }

    // draw collectable items...
    public void drawCollectables(SpriteBatch batch){
        for (Collectables c : collectables){
            c.updateCollectable();
            batch.draw(c, c.getX() - c.getWidth()/2f, c.getY() - c.getHeight()/2f);
        }
    }

    public void removeCollectables() {
        for (int i = 0 ; i < collectables.size ; i++) {
            if (collectables.get(i).getFixture().getUserData() == "Remove"){
                // we need to change the filter so we inform the physics world that
                // we aren't going to collide with this collectable anymore
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }


    // dispose of obstacles that have gone out of bounds
    // create new ones to replace the old ones
    public void createAndArrangeNewObstacles() {
        for (int i = 0 ; i < obstacles.size; i++){
            if ((obstacles.get(i).getX() + GameInfo.WIDTH / 2f + GameInfo.WIDTH + 15) < cameraX){
                // obstacle is out of bounds - REMOVE IT!!!
                obstacles.get(i).getTexture().dispose();
                obstacles.removeIndex(i);
            }
        }
        for (int i = 0 ; i < platforms.size ; i++){
            if ((platforms.get(i).getX() + GameInfo.WIDTH / 2f + GameInfo.WIDTH + 15) < cameraX){
                // platform is out of bounds - REMOVE IT!
                platforms.get(i).getTexture().dispose();
                platforms.removeIndex(i);
            }
        }
        if (obstacles.size + platforms.size == 7){
            createObstacles();
            positionObstacles(false);
        }
    }

    public void removeOffScreenCollectables() {
        for (int i = 0 ; i < collectables.size ; i++){
            if ((collectables.get(i).getX() - GameInfo.WIDTH / 2f + GameInfo.WIDTH - 15) < cameraX) {
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);

                // debug stuff...
                //System.out.println("Collectible has been removed!");
            }
        }
    }
    // set the x position of the camera
    public void setCameraX(float cameraX){
        this.cameraX = cameraX;
    }

    public Player positionThePlayer(Player player){
        player = new Player(world, platforms.get(0).getX(), platforms.get(0).getY() + 50);
        return player;
    }
}

































