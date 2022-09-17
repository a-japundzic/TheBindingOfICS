package sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;


import screens.playScreen;
/**
 * Creates the enemy deathsHead
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class deathsHead extends enemy{

    //Holds the time
    private float stateTime;

    //Holds the movement animation
    private Animation<TextureRegion> movementAnimation;
    //Holds all the frames in the animation
    private Array<TextureRegion> frames;
    //Holds the texture atlas
    private TextureAtlas atlas;

    //booleans for detecting if the enemy has been destroyed or not
    private boolean setToDestroy;
    private boolean destroyed;
    /**
     * constructer for deathsHead
     * @param x takes in a width
     * @param y takes in a height
     */
    public deathsHead(playScreen screen, float x, float y) {
        super(screen, x, y);
        atlas = new TextureAtlas("deaths_touch.pack");

        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(atlas.findRegion("deaths_head"), i * 32, 0, 32, 32));
        }
        for(int i = 0; i < 1; i++){
            frames.add(new TextureRegion(atlas.findRegion("deaths_head"), i * 32, 32, 32, 32));
        }
        movementAnimation = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();


        stateTime = 0;
        setBounds(getX(), getY(), 32 / icsGame.PPM, 32 / icsGame.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    /**
     * Destroys the sprite or creates it depending on some booleans
     * @param dt takes in a width
     */
    public void update(float dt){
        //Destroys the enemy
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(atlas.findRegion("deaths_head"), 32, 32, 32, 32));
            icsGame.manager.get("audio/sounds/deathBurst.mp3", Sound.class).play();
            stateTime = 0;
        }
        //Gives the enemy a velocity and texture
        else if(!destroyed){
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(movementAnimation.getKeyFrame(stateTime, true));
        } stateTime += dt;

    }

    @Override
    /**
     * Defines the enenmy body
     */
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape3 = new CircleShape();
        shape3.setRadius(12 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ENEMY_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.BULLET_BIT | icsGame.ISAAC_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape3;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Draws the enemy texture if the enemy is not destroyed
     */
    public void draw(Batch batch){
        if(!destroyed || stateTime < 0.4f){
            super.draw(batch);
        }
    }

    @Override
    /**
     * sets destroy to true if the enemy has been hit
     */
    public void hit() {
        setToDestroy = true;
    }
}
