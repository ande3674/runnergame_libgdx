package obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Obstacle extends Sprite {

    // physics world
    private World world;
    private Body body;
    private String obsName;

    public Obstacle(World world, String obsName){
        super(new Texture("Obstacles/" + obsName + ".png"));
        this.world = world;
        this.obsName = obsName;
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);

        // create body with world and bodyDef...
        body = world.createBody(bodyDef);

        // create the shape ...
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2) / GameInfo.PPM, (getHeight() / 2) / GameInfo.PPM);

        // to use the shape we need to assign it to a fixture...
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // create fixture...
        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();

    }

    public void setSpritePosition(float x, float y){
        setPosition(x, y);
        createBody();
    }

    public String getObsName() {
        return this.obsName;
    }


}
