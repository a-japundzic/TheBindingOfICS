package sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;

/**
 * Exact same as the bullet class just with different textures and filter bits
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class enemyBullet extends Sprite {

    private playScreen screen;
    private World world;
    private TextureRegion tear;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    private TextureAtlas tearAtlas;
    private boolean fireY;
    private boolean fireUp;

    private Body b2Body;

    public enemyBullet(playScreen screen, float x, float y, boolean fireRight, boolean fireUp, boolean fireY){
        this.fireY = fireY;
        this.fireUp = fireUp;
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        tearAtlas = new TextureAtlas("tear.pack");

        tear = new TextureRegion(tearAtlas.findRegion("tears"), 0, 96, 32,32);

        setRegion(tear);
        setBounds(x, y, 32 / icsGame.PPM, 32 / icsGame.PPM);
        defineBullet();
    }

    public void defineBullet(){
        FixtureDef fdef;
        CircleShape shape;
        BodyDef bdef = new BodyDef();
        if(!fireY) {
            bdef.position.set(fireRight ? getX() + 0.05f / icsGame.PPM : getX() - 0.05f, getY());
        }
        else if(fireY){
            bdef.position.set(getX(), getY());
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!world.isLocked())
            b2Body = world.createBody(bdef);

        fdef = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(6 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ENEMY_BULLET_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.DOOR_BIT | icsGame.ISAAC_BIT | icsGame.ISAAC_HEAD_BIT;

        fdef.shape = shape;
        fdef.friction = 0;
        b2Body.createFixture(fdef).setUserData(this);
        if(!fireY) {
            b2Body.setLinearVelocity(new Vector2(fireRight ? 2f : -2f, 0));
        }
        else if(fireY){
            b2Body.setLinearVelocity(new Vector2(0, fireUp ? 2f : -2f));
        }
    }

    public void update(float dt){
        stateTime += dt;
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        if((setToDestroy) && !destroyed){
            icsGame.manager.get("audio/sounds/tearImpact.mp3", Sound.class).play();
            world.destroyBody(b2Body);
            destroyed = true;
        }

        if((fireRight && b2Body.getLinearVelocity().x < 0) || (!fireRight && b2Body.getLinearVelocity().x > 0)){
            setToDestroy();
        }

    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}

