package sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;
/**
 * Creates the enemy nerveEnding
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class nerveEnding extends enemy{
    //Holds time
    private float stateTime;

    //Holds the animations and frames
    private Animation<TextureRegion> movementAnimation;
    private Array<TextureRegion> frames;

    //Holds the texture atlas
    private TextureAtlas atlas;

    //Holds whether the enemy should be destroyed or not and whether they have been destroyed.
    private boolean setToDestroy;
    private boolean destroyed;

    /**
     * constructer for nerveEnding
     * @param screen takes in a screen
     * @param x takes in a x value
     * @param y takes in a y value
     */
    public nerveEnding(playScreen screen, float x, float y) {
        super(screen, x, y);
        //Declares the atlas for nerveEnding
        atlas = new TextureAtlas("nerve_ending.pack");

        //Declares the animation using the textures passed into frames array
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(atlas.findRegion("nerve_ending"), i * 32, 0, 32, 64));
        }
        movementAnimation = new Animation<TextureRegion>(0.15f, frames);

        //Declares stateTime
        stateTime = 0;
        //sets bounds of texture
        setBounds(getX(), getY(), 32 / icsGame.PPM, 64 / icsGame.PPM);

        //Declares the booleans
        setToDestroy = false;
        destroyed = false;
    }

    /**
     * Updates the variables or destroys the enemy
     * @param dt takes in time
     */
    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            icsGame.manager.get("audio/sounds/deathBurst.mp3", Sound.class).play();
            stateTime = 0;
        }
        else if(!destroyed){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(movementAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    /**
     * defines the enemy body
     */
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape3 = new PolygonShape();
        shape3.setAsBox(6 / icsGame.PPM, 24 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ENEMY_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.BULLET_BIT | icsGame.ISAAC_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape3;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Draws the enemy
     * @param batch takes in a batch
     */
    public void draw(Batch batch){
        if(!destroyed || stateTime < 0.4f){
            super.draw(batch);
        }
    }

    @Override
    /**
     * If the enemy is hit setToDestroy = true;
     */
    public void hit() {
        setToDestroy = true;
    }
}

