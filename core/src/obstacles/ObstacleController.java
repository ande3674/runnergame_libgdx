package obstacles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import collectables.Collectables;
import helpers.GameInfo;
import helpers.GameManager;
import player.Player;

public class ObstacleController {

    // Need a physics world in order to create bodies...
    private World world;

    // array of obstacles to add to our game...
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Array<Platform> platforms = new Array<Platform>();
    private Array<Spike> spikes = new Array<Spike>();
    // array of collectable items...
    private Array<Collectables> collectables = new Array<Collectables>();

    // Variables to control the platform placement...
    private Random r = new Random();
    private float minPlatformY, maxPlatformY;
    private float minSpikeY, maxSpikeY;

    // keep track of the camera's X position to control our game obstacles
    private float cameraX;
    // keep track of the obstacles' last positions so we can add more as we go
    private float lastObstaclePositionX, lastPlatformPositionX, lastSpikePositionX;

    // CONSTANTS
    private final float DISTANCE_BETWEEN_OBSTACLES = 400f;
    private final float DISTANCE_BETWEEN_PLATFORMS = 500f;
    private final float DISTANCE_BETWEEN_SPIKES = 500f;


    // Constructor
    public ObstacleController(World world) {
        this.world = world;
        minPlatformY = GameInfo.HEIGHT * .40f;
        maxPlatformY = GameInfo.HEIGHT * .70f;
        minSpikeY = GameInfo.HEIGHT * .30f;
        maxSpikeY = GameInfo.HEIGHT * .80f;
        createObstacles();
        positionObstacles(true);
    }

    void createObstacles(){
        for (int i = 0 ; i < 5 ; i++){
            obstacles.add(new Obstacle(world, "kirby"));
        }
        for (int i = 0 ; i < 4 ; i++){
            platforms.add(new Platform(world, "button"));
        }
        for (int i = 0 ; i < 4 ; i++){
            spikes.add(new Spike(world, "sol"));
        }
        //obstacles.shuffle();
    }

    public void positionObstacles(boolean firstTimePositioning) {
        // SET THE POSITIONS / COORDINATES...
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
        // Spikes... (which are actually SUNS now...)
        float spikePositionX = 0;
        if (firstTimePositioning){
            spikePositionX = 300f;
        } else {
            spikePositionX = lastSpikePositionX;
        }

        // PLACE THEM ON THE SCREEN
        // Place obstacles -- these are the Kirby guys and are just lined up along the bottom of the screen for now
        for (Obstacle o : obstacles) {
            // make sure we only reposition NEW obstacles and not the ones that
            // are still on / were already on the screen...
            if (o.getX() == 0 && o.getY() == 0){ // (newly made obstacles will have default values of zero)
                o.setSpritePosition(positionX, y);
                positionX += DISTANCE_BETWEEN_OBSTACLES;
                lastObstaclePositionX = positionX;
            }
        }

        // Pick a random Y-coordinate for the platforms...
        float tempY = r.nextFloat() * (maxPlatformY - minPlatformY)  + minPlatformY;
        // Place platforms...
        for (Platform p : platforms){
            if (p.getX() == 0 && p.getY() == 0){
                p.setSpritePosition(platformPositionX, tempY);
                platformPositionX += DISTANCE_BETWEEN_PLATFORMS;
                tempY = r.nextFloat() * (maxPlatformY - minPlatformY) + minPlatformY;
                lastPlatformPositionX = platformPositionX;
                // Add the collectables on top of platforms.....
                if (!firstTimePositioning){
                    int rand = r.nextInt(10); // get a random int 0-9
                    // place an obstacle if rand is 0-7...
                    if (rand < 8){
                        int randC = r.nextInt(5);
                        if (randC < 2){
                            // spawn a life, if life count is lower than 3
                            if (GameManager.getInstance().lifeScore < 3) {
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
                        else {
                            // spawn a coin
                            Collectables collectable = new Collectables(world, GameInfo.COIN);
                            collectable.setCollectablesPosition(p.getX(), p.getY() + 40);
                            collectables.add(collectable);
                        }
                    }
                }
                else {
                    // Place a few INITIAL collectables since this is the first time positioning...
                    // Place on the Kirby's...
//                    Collectables c1 = new Collectables(world, GameInfo.LIFE);
//                    c1.setCollectablesPosition(obstacles.get(1).getX(), obstacles.get(1).getY() + 60);
//                    collectables.add(c1);
//                    Collectables c2 = new Collectables(world, GameInfo.LIFE);
//                    c2.setCollectablesPosition(obstacles.get(5).getX(), obstacles.get(5).getY() + 60);
//                    collectables.add(c2);
                    Collectables c3 = new Collectables(world, GameInfo.COIN);
                    c3.setCollectablesPosition(obstacles.get(3).getX(), obstacles.get(3).getY() + 60);
                    collectables.add(c3);
//                    // Place on the platforms...
//                    Collectables c4 = new Collectables(world, GameInfo.COIN);
//                    c4.setCollectablesPosition(platforms.get(1).getX(), platforms.get(1).getY() + 40);
//                    collectables.add(c4);
//                    Collectables c5 = new Collectables(world, GameInfo.COIN);
//                    c5.setCollectablesPosition(platforms.get(3).getX(), platforms.get(3).getY() + 40);
//                    collectables.add(c5);
                }
            }
        }

        // Get a random Y coordinate for the level of the SUN obstacles
        float tempSpikeY = r.nextFloat() * (maxSpikeY - minSpikeY) + minSpikeY;
        // Place Spikes....
        for (Spike s : spikes){
            if(s.getX() == 0 && s.getY() == 0) {
                s.setSpritePosition(spikePositionX, tempSpikeY);
                spikePositionX += DISTANCE_BETWEEN_SPIKES;
                tempSpikeY = r.nextFloat() * (maxSpikeY - minSpikeY) + minSpikeY;
                lastSpikePositionX = spikePositionX;
            }
        }
        Collectables c1 = new Collectables(world, GameInfo.COIN);
        c1.setCollectablesPosition(platforms.get(1).getX(), platforms.get(1).getY() + 50);
        collectables.add(c1);
        //delete later - testing adding collectables
        //Collectables c2 = new Collectables(world, "life");
        //c2.setCollectablesPosition(platforms.get(1).getX(), platforms.get(1).getY() + 120);
        //collectables.add(c2);
    }

    // draw the platforms and obstacles and spikes...
    public void drawObstacles(SpriteBatch batch){
        for (Obstacle o : obstacles){
            batch.draw(o, o.getX() - o.getWidth()/2f, o.getY() - o.getHeight()/2f);
        }
        for (Platform p : platforms){
            batch.draw(p, p.getX() - p.getWidth()/2f, p.getY() - p.getHeight()/2f);
        }
        for (Spike s : spikes){
            batch.draw(s, s.getX() - s.getWidth()/2f, s.getY() - s.getHeight()/2f);
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
        for (int i = 0 ; i < spikes.size ; i++){
            if ((spikes.get(i).getX() + GameInfo.WIDTH / 2f + GameInfo.WIDTH + 15) < cameraX){
                // spike is out of bounds - REMOVE IT!
                spikes.get(i).getTexture().dispose();
                spikes.removeIndex(i);
            }
        }
        if (obstacles.size + platforms.size == 9){ // TODO this number might need to change
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

































