package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class Player extends Sprite {

    // always need to start with a world in order to create a body...
    private World world;
    private Body body;

    // animation variables ...
    private TextureAtlas playerAtlas;
    private Animation animation;
    private com.badlogic.gdx.graphics.g2d.Animation animation1;
    private float elapsedTime;

    private boolean isWalking, isDead;

    public Player(World world, float x, float y) {
        super(new Texture("Players/bear.png"));
        this.world = world;
        this.isDead = false;
        setPosition(x, y);
        createBody();
        playerAtlas = new TextureAtlas("Player Animation/sprites.txt");

    }

    private void createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // set position
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);

        body = world.createBody(bodyDef);
        // prevent the body from rotating .....
        body.setFixedRotation(true);

        // shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(( getWidth()/ 2f )/ GameInfo.PPM, ( getHeight()/2f )/GameInfo.PPM);

        // fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4f; // this is the player body's MASS
        fixtureDef.friction = 2f; // friction prevents player from sliding
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PLAYER; // Define the category (for collisions)
        // define which categories can this collide with?!?!
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLE; // can collide with collectables and defaults

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");

        shape.dispose();
    }

    // MOVE the player
    public void movePlayer(float x, float y) {
        // Keep player facing the right way...
        if (x < 0 && !this.isFlipX()){
            // moving left.. flip player to face left
            this.flip(true, false);
        } else if (x > 0 && this.isFlipX()){
            // moving right.. flip player to face right
            this.flip(true, false);
        }
        // set player motion...
        body.setLinearVelocity(x, y);
    }

    // TODO TODO TODO TODO !!!!
    // method to UPDATE the player
    public void updatePlayer() {
        setPosition(body.getPosition().x * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM);
    }
    public void updatePlayer2() {
        if (body.getPosition().y > 20) {
            setPosition(body.getPosition().x * GameInfo.PPM,
                    body.getPosition().y * GameInfo.PPM);
        } else {
            setPosition(body.getPosition().x * GameInfo.PPM, 20);
        }
    }

    // method to DRAW the player
    public void drawPlayerIdle(SpriteBatch batch) {
        if (!isWalking) {
            batch.draw(this, getX() - getWidth() / 2f,
                    (getY() - getHeight() / 2f));
        }
    }

    public void drawPlayerAnimation(SpriteBatch batch){
        if(isWalking){

            elapsedTime += Gdx.graphics.getDeltaTime();

            // let the player flip left or right...
            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();
            for (TextureRegion frame : frames){
                if (body.getLinearVelocity().x < 0 && !frame.isFlipX()){
                    // moving left..
                    frame.flip(true, false);// flip x left or right (true) but not up and down (false)
                } else if (body.getLinearVelocity().x > 0 && frame.isFlipX()){
                    // moving right..
                    frame.flip(true, false);
                }
            }

            animation = new Animation();
            animation1 = new com.badlogic.gdx.graphics.g2d.Animation(1/10f, playerAtlas.getRegions());

            batch.draw((TextureRegion)animation1.getKeyFrame(elapsedTime, true),
                    getX() - getWidth() / 2f,
                    getY() - getHeight() / 2f);
        }
    }

    public void setWalking(boolean isWalking){
        this.isWalking = isWalking;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public boolean isDead() {
        return this.isDead;
    }
}