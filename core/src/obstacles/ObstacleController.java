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

    private final float DISTANCE_BETWEEN_OBSTACLES = 400f;
    private final float DISTANCE_BETWEEN_PLATFORMS = 500f;

    public ObstacleController(World world) {
        this.world = world;
        minPlatformY = GameInfo.HEIGHT * .40f;
        maxPlatformY = GameInfo.HEIGHT * .60f;
        createObstacles();
        positionObstacles();
    }

    void createObstacles(){
        for (int i = 0 ; i < 6 ; i++){
            obstacles.add(new Obstacle(world, "kirby"));
        }
        for (int i = 0 ; i < 2 ; i++){
            platforms.add(new Platform(world, "button"));
        }
        obstacles.shuffle();
    }

    public void positionObstacles() {
        // Obstacles.....
        float positionX = 10;
        float y = 50;
        // Place obstacles
        for (Obstacle o : obstacles) {
            o.setSpritePosition(positionX, y);
            positionX += DISTANCE_BETWEEN_OBSTACLES;
        }
        // Platforms...
        float firstXPosition = 200f;
        float tempY = r.nextFloat() * (maxPlatformY - minPlatformY)  + minPlatformY;
        // Place platforms...
        for (Platform p : platforms){
            p.setSpritePosition(firstXPosition, tempY);
            firstXPosition += DISTANCE_BETWEEN_PLATFORMS;
            tempY = r.nextFloat() * (maxPlatformY - minPlatformY)  + minPlatformY;
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
}

































