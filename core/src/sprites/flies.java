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
 * Creates the enemy: flies
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class flies extends enemy{
    //Holds the time
    private float stateTime;

    //Holds the animation
    private Animation<TextureRegion> movementAnimation;
    //Holds the frames for the animation
    private Array<TextureRegion> frames;
    //Holds the texure atlas
    private TextureAtlas atlas;

    //Detects if the enemy has been destroyed
    private boolean setToDestroy;
    private boolean destroyed;
    /**
     * constructer for flies
     * @param screen takes in a screen
     * @param x takes in a x value
     * @param y takes in a y value
     */
    public flies(playScreen screen, float x, float y) {
        super(screen, x, y);

        //gives atlas a texture atlas
        atlas = new TextureAtlas("fly.pack");

        //adds all the texture frames into frames
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(atlas.findRegion("fly"), i * 32, 0, 32, 32));
        }
        //Puts all the frames in the animation
        movementAnimation = new Animation<TextureRegion>(0.1f, frames);
        //Clears the frame array
        frames.clear();

        //sets the bounds for the texture
        setBounds(getX(), getY(), 32 / icsGame.PPM, 32 / icsGame.PPM);

        //Declares variables
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
    }


    @Override
    /**
     * Updates variables or destroys the enemy.
     * @param dt takes in time
     */
    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed){
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
        CircleShape shape3 = new CircleShape();
        shape3.setRadius(3 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ENEMY_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.BULLET_BIT | icsGame.ISAAC_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape3;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Draws the enemy texture if the enemy hasn't been destroyed
     * @param batch takes in a sprite batch

     */
    public void draw(Batch batch){
        if(!destroyed || stateTime < 0.4f){
            super.draw(batch);
        }
    }

    @Override/**
     * sets setToDestroy to true if the enemy has been hit
     */
    public void hit() {
        setToDestroy = true;
    }
}
