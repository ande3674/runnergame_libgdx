package obstacles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class ObstacleController {

    // Need a physics world in order to create bodies...
    private World world;

    // array of obstacles to add to our game...
    private Array<Obstacle> obstacles = new Array<Obstacle>();

    private final float DISTANCE_BETWEEN_OBSTACLES = 300f;

    public ObstacleController(World world) {
        this.world = world;
        createObstacles();
        positionObstacles();
    }

    void createObstacles(){

        for (int i = 0 ; i < 6 ; i++){
            obstacles.add(new Obstacle(world, "kirby"));
        }
        for (int i = 0 ; i < 2 ; i++){
            // TODO add something for the player to jump on...
        }
        obstacles.shuffle();
    }

    public void positionObstacles() {
        float positionX = 10;
        float tempY = 50;

        for (Obstacle o : obstacles) {
            o.setSpritePosition(positionX, tempY);

            positionX += DISTANCE_BETWEEN_OBSTACLES;

        }
    }

    public void drawObstacles(SpriteBatch batch){
        for (Obstacle o : obstacles){
            batch.draw(o, o.getX() - o.getWidth()/2f, o.getY() - o.getHeight()/2f);
        }
    }
}

































