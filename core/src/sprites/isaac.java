package sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;

import Scenes.hud;
import screens.playScreen;
/**
 * Creates the body for the player, creates the body texture, moves the player between rooms, and kills the player if they run out of hp
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class isaac extends Sprite {
    //The different states the body texture can be in
    public enum  State { WALKINGY, WALKINGX, STANDING, DEAD }

    //Holds the body textures previous and current state
    public State currentState;
    public State previousState;

    //Holds the world
    public World world;

    //Holds the circleShape for the body, and the circleShape for the head
    private CircleShape shape2;
    private CircleShape shape;

    //Holds the players body
    public Body b2Body;

    //texture for when isaac is standing, texure for when isaac is dead, and texture for isaac default head posistion
    private TextureRegion isaacStand;
    private TextureRegion isaacDead;
    private TextureRegion isaacDefault;

    //animations for when isaac is walking
    private Animation<TextureRegion> isaacWalkX;
    private Animation<TextureRegion> isaacWalkY;

    //Holds the time
    private float stateTimer;
    private float timer;

    //Different booleans detecting what direction the player is walking, if they are dead or have been hit, and if the texture should be blinking
    private boolean walkingRight;
    private boolean walkingDown;
    public static boolean isaacHit;
    private boolean isaacIsDead;
    public static boolean blink = false;

    //Holds the variable deciding how long the texture will blink for
    private float blinkLength = 2f;

    //Holds the players current coordinates
    public static float ISAACX;
    public static float ISAACY;

    /**
     * constructer for isaac
     * @param screen takes in a screen
     */
    public isaac(playScreen screen){
        //Takes in the textureatlas from screen
        super(screen.getAtlas().findRegion("isaac_body"));
        //Declares world
        this.world = screen.getWorld();

        //Declares variables
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        walkingRight = true;
        walkingDown = true;
        timer = 0;

        //Declaring all the different texures and animations for the sprite
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 6; i < 8; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 0, 32, 32));
        for(int i = 0; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), i*32, 32, 32, 32));
        isaacWalkY = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();

        for(int i = 0; i < 8; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 64, 32, 32));
        for(int i = 0; i < 2; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 96, 32, 32));
        isaacWalkX = new Animation<TextureRegion>(0.1f, frames);

        isaacStand = new TextureRegion(getTexture(), 0,32, 32, 32);

        isaacDefault = new TextureRegion(getTexture(), 0, 0, 32, 32);

        isaacDead = new TextureRegion(getTexture(), 192, 160, 64, 32);

        //defines the players body, bounds, and region
        defineIsaac();
        setBounds(0,0,32 / icsGame.PPM, 32 / icsGame.PPM);
        setRegion(isaacStand);
    }

    /**
     * Updates variables, moves isaac between different rooms, and sets the texture to blinking.
     * @param dt takes in time
     */
    public void update(float dt) {
        setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        //Constantly updating these variables with the bodies current x, and y + the shape2 radius, this is for the head.
        ISAACX = b2Body.getPosition().x;
        ISAACY = b2Body.getPosition().y + shape2.getRadius();

        //Moves the player in a different direction depending on the door touched
        if(door.transistionRight){
            b2Body.setTransform(b2Body.getPosition().x+1, b2Body.getPosition().y, 0);
        }
        if(door.transistionLeft){
            b2Body.setTransform(b2Body.getPosition().x-1f, b2Body.getPosition().y, 0);
        }
        if(door.transistionUp){
            b2Body.setTransform(b2Body.getPosition().x, b2Body.getPosition().y +1, 0);
        }
        if(door.transistionDown){
            b2Body.setTransform(b2Body.getPosition().x, b2Body.getPosition().y -1, 0);
        }

        //sets the texture to blinking.
        if(blink == true){
            timer += dt;
            if(timer >= blinkLength){
                timer = 0;
                blink = false;
            }
        }



    }

    /**
     * Switch for the different states the texture can be in
     * @param dt takes in time
     * @return the current texture to be displayed on the screen
     */
    public TextureRegion getFrame(float dt){
        //Holds what case to go into
        currentState = getState();

        //Different textures depending on the state
        TextureRegion region;
        switch(currentState){
            case DEAD:
                region = isaacDead;
                setBounds(getX(), getY(), 64 / icsGame.PPM, 32 /icsGame.PPM);
                break;
            case WALKINGY:
                region = isaacWalkY.getKeyFrame(stateTimer, true);
                setBounds(getX(), getY(), 32 / icsGame.PPM, 32 /icsGame.PPM);
                break;
            case WALKINGX:
                region = isaacWalkX.getKeyFrame(stateTimer, true);
                setBounds(getX(), getY(), 32 / icsGame.PPM, 32 /icsGame.PPM);
                break;
            case STANDING:
            default:
                region = isaacStand;
                setBounds(getX(), getY(), 32 / icsGame.PPM, 32 /icsGame.PPM);
                break;
        }

        //Flips the texture in the x direction depending on the bodies velocity
        if((b2Body.getLinearVelocity().x < 0 || !walkingRight) && !region.isFlipX()){
           region.flip(true, false);
           walkingRight = false;
        }
        else if((b2Body.getLinearVelocity().x > 0 || walkingRight) && region.isFlipX()){
           region.flip(true, false);
           walkingRight = true;
        }

        //Flips the dead texture to always be facing right
        if(currentState == State.DEAD){
            if(!walkingRight) {
                region.flip(true, false);
            }
            //Sets the bodies velocity to 0
            if(b2Body.getLinearVelocity().x > 0 || b2Body.getLinearVelocity().y > 0){
                b2Body.setLinearVelocity(0,0);
            }
        }

        //Flips the texture in the y direction depending on the bodies velocity
        if((b2Body.getLinearVelocity().y > 0) && !region.isFlipX()){
            region.flip(true, false);
            walkingDown = false;
        }
        else if((b2Body.getLinearVelocity().y < 0) && region.isFlipX()){
            region.flip(true, false);
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            walkingDown = true;
        }

        //sets the stateTimer to the value that gets the currentTexture shown on the screen or to 0
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    /**
     * Returns the current state depending on what conditions are met
     * @return current state
     */
    public State getState(){
        if(isaacIsDead){
            return State.DEAD;
        }
        else if(b2Body.getLinearVelocity().y > 0 || b2Body.getLinearVelocity().y < 0){
            return State.WALKINGY;
        }
        else if((b2Body.getLinearVelocity().x > 0 || b2Body.getLinearVelocity().x < 0)){
            return State.WALKINGX;
        }
        else{
            return State.STANDING;
        }
    }

    /**
     * @return if isaacIsDead or not
     */
    public boolean isDead(){
        return isaacIsDead;
    }

    /**
     * @return the stateTimer
     */
    public float getStateTimer(){
        return stateTimer;
    }

    /**
     * Decides what to do if isaac is hit, either take away a health point or kill him
     */
    public void hit(){
        if(hud.health > 0) {
            icsGame.manager.get("audio/sounds/Isaac_Hurt_Grunt0.mp3", Sound.class).play();
            blink = true;
            isaacHit = true;
        }
        else if(hud.health <= 0){
            icsGame.manager.get("audio/sounds/isaacdies.mp3", Sound.class).play();
            isaacIsDead = true;
        }
    }

    /**
     * Defines the isaac body
     */
    public void defineIsaac(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / icsGame.PPM, 64 / icsGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        shape = new CircleShape();
        shape.setRadius(6 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ISAAC_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        FixtureDef fdef2 = new FixtureDef();
        shape2 = new CircleShape();
        shape2.setRadius(10 / icsGame.PPM);
        fdef2.filter.categoryBits = icsGame.ISAAC_HEAD_BIT;
        fdef2.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT | icsGame.ENEMY_BULLET_BIT;
        Vector2 vec = new Vector2(shape.getPosition().x / 2, shape2.getRadius());

        shape2.setPosition(vec);

        fdef2.shape = shape2;
        b2Body.createFixture(fdef2).setUserData(this);
    }


}
