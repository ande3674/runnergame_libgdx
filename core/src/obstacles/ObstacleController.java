package obstacles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import helpers.GameInfo;

public class ObstacleController {

    // Need a physics world in order to create bodies...
    private World world;

    // array of obstacles to add to our game...
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Platform> platforms = new Array<Platform>();

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
            }
        }
    }

    public void drawObstacles(SpriteBatch batch){
        for (Obstacle o : obstacles){
            batch.draw(o, o.getX() - o.getWidth()/2f, o.getY() - o.getHeight()/2f);
        }
        for (Platform p : platforms){
            batch.draw(p, p.getX() - p.getWidth()/2f, p.getY() - p.getHeight()/2f);
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
    // set the x position of the camera
    public void setCameraX(float cameraX){
        this.cameraX = cameraX;
    }
}

































