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
 * Creates the bullets for the player
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class bullet extends Sprite {
    //Holds the world and the playscreen
    private playScreen screen;
    private World world;

    //Holds the bullet animation frames
    private Array<TextureRegion> frames;
    //Holds the bullet animation
    private Animation<TextureRegion> bulletDestroyedAnimation;
    //Hods the textureAtlas for the tears
    private TextureAtlas tearPopAtlas;

    //Holds the time
    private float stateTime;

    //Booleans for detecting if the bullet has been destroyed, and what direction to shoot the bullet in
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    private boolean fireY;
    private boolean fireUp;

    //New body to hold the bullet body
    private Body b2Body;

    /**
     * constructer for bullet
     * @param screen takes in a screen
     * @param x takes in a width
     * @param y takes in a height
     * @param fireRight takes in a boolean on if the player is shooting right or not
     * @param fireUp takes in a boolean on if the player is shooting up or not
     * @param fireY takes in a boolean on if the player is shooting in the y direction or not
     */
    public bullet(playScreen screen, float x, float y, boolean fireRight, boolean fireUp, boolean fireY){
        //Declares all the variables
        this.fireY = fireY;
        this.fireUp = fireUp;
        this.fireRight = fireRight;
        this.screen = screen;
        this.world = screen.getWorld();

        //adds the texture atlas
        tearPopAtlas = new TextureAtlas("tear_pop.pack");

        //Gets all the frames into the array
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 5; i++){
            frames.add(new TextureRegion(tearPopAtlas.findRegion("tear_pop"), i * 32, 0, 64, 64));
        }
        for(int i = 0; i < 5; i++){
            frames.add(new TextureRegion(tearPopAtlas.findRegion("tear_pop"), i * 32, 32, 64, 64));
        }
        for(int i = 0; i < 5; i++){
            frames.add(new TextureRegion(tearPopAtlas.findRegion("tear_pop"), i * 32, 64, 64, 64));
        }
        //Puts all the frames into the animation
        bulletDestroyedAnimation = new Animation<TextureRegion>(0.1f, frames);

        //sets the textureRegion to the animation
        setRegion(bulletDestroyedAnimation.getKeyFrame(0));
        //sets the bounds of the texture
        setBounds(x, y, 40 / icsGame.PPM, 40 / icsGame.PPM);
        //defines the bullet body
        defineBullet();
    }

    /**
     * defines the bullet body
     */
    public void defineBullet(){
        FixtureDef fdef;
        CircleShape shape;
        BodyDef bdef = new BodyDef();
        //Creates the body in different positions depending on what direction the player was firing
        if(!fireY) {
            bdef.position.set(fireRight ? getX() + 0.05f / icsGame.PPM : getX() - 0.05f, getY());
        }
        else if(fireY){
            bdef.position.set(getX(), getY());
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        //makes sure to only create the body if it doesn't produce an error
        if(!world.isLocked())
            b2Body = world.createBody(bdef);

        fdef = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(3 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.BULLET_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape;
        fdef.friction = 0;
        b2Body.createFixture(fdef).setUserData(this);
        //sets the bullets velocity depending on what direction it was shot
        if(!fireY) {
            b2Body.setLinearVelocity(new Vector2(fireRight ? 2f : -2f, 0));
        }
        else if(fireY){
            b2Body.setLinearVelocity(new Vector2(0, fireUp ? 2f : -2f));
        }
    }

    /**
     * Updates the texture position and destroys the bullet
     * @param dt takes in time
     */
    public void update(float dt){
        stateTime += dt;
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        if((stateTime > 1 || setToDestroy) && !destroyed){
            icsGame.manager.get("audio/sounds/tearImpact.mp3", Sound.class).play();
            world.destroyBody(b2Body);
            destroyed = true;
        }

        if((fireRight && b2Body.getLinearVelocity().x < 0) || (!fireRight && b2Body.getLinearVelocity().x > 0)){
            setToDestroy();
        }

    }

    /**
     * @return setToDestroy
     */
    public void setToDestroy(){
        setToDestroy = true;
    }

    /**
     * @return destroyed
     */
    public boolean isDestroyed(){
        return destroyed;
    }
}
