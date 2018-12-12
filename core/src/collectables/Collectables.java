package collectables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Collectables extends Sprite {

    private World world;
    private Body body;
    private Fixture fixture;
    private String name;

    public Collectables(World world, String name) {
        super(new Texture("Collectables/" + name + ".png"));
        this.world = world;
        this.name = name;
    }

    void createCollectablesBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set((getX()) / GameInfo.PPM, (getY()) / GameInfo.PPM);

        body = world.createBody(bodyDef);

        // shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(( getWidth()/ 2f )/ GameInfo.PPM, ( getHeight()/2f )/GameInfo.PPM);

        // fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        //Define the collectable's Body category for collisions...
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE;
        // Make it a sensor so that we can pass through it / be collidable
        fixtureDef.isSensor = true;

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(name); // will be the coin or the life

        shape.dispose();

    }

    public void setCollectablesPosition(float x, float y){
        setPosition(x, y);
        createCollectablesBody();
    }

    public void updateCollectable() {
        setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
    }

    public void changeFilter() {
        // to change the collectable's filter we need to create a new one first...
        Filter filter = new Filter();
        // make it DESTROYED so that our player can no longer collide with it...
        filter.categoryBits = GameInfo.DESTROYED;
        // set the new data via the fixture...
        fixture.setFilterData(filter);
    }

    public Fixture getFixture() {
        return fixture;
    }
}
