package player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Player extends Sprite {

    // always need to start with a world in order to create a body...
    private World world;
    private Body body;

    public Player(World world, float x, float y) {
        super(new Texture("Players/female_runner.png"));
        this.world = world;
        setPosition(x, y);
        createBody();
    }

    private void createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // set position
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);

        body = world.createBody(bodyDef);

        // shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(( getWidth()/ 2f - 60 )/ GameInfo.PPM, ( getHeight()/2f - 50)/GameInfo.PPM);

        // fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4f; // this is the player body's MASS
        fixtureDef.friction = 2f; // friction prevents player from sliding
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    // method to UPDATE the player
    public void updatePlayer() {
        setPosition(body.getPosition().x * GameInfo.PPM,
                body.getPosition().y * GameInfo.PPM);

    }

    // method to DRAW the player
    public void drawPlayer(SpriteBatch batch) {
        batch.draw(this, getX() - getWidth() / 2f,
                (getY() - getHeight() / 2f) + 45);
    }

}