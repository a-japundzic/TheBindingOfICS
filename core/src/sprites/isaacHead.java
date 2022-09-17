package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;

/**
 * Creates the head texture for the body and renders the bullets on the screen
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class isaacHead extends Sprite {
    //Different states the head texture can be in
    public enum State {EASTWEST, NORTH, SOUTH, DEFAULT, WALKUP, WALKDOWN, WALKRIGHTLEFT}

    //Holds the current and previous states
    public State currentStateShooting;
    public State previousStateShooting;

    //Holds the world
    public World world;

    //Holds the different textures and animations
    private TextureRegion walkUp;
    private TextureRegion walkDown;
    private TextureRegion walkRightLeft;
    private TextureRegion currentDirection;
    private playScreen screen;
    private Animation<TextureRegion> eastWest;
    private Animation<TextureRegion> north;
    private Animation<TextureRegion> south;
    private TextureRegion region;

    //Hold the time
    private float stateTimer;
    private float shootTimer;

    //Variable that decides how long between isaac's shots
    private static final float SHOOT_WAIT_TIME = 0.5f;

    //Hold wether isaac is shooting right or not
    private boolean shootingRight;

    //Holds wether isaac should be destroyed or not
    public static boolean setToDestroy;

    //Holds the bullets
    private Array<bullet> bullets;
    /**
     * constructer for isaacHead
     * @param screen takes in a screen
     */
    public isaacHead(playScreen screen) {
        //Declaring variables
        super(screen.getAtlas().findRegion("isaac_body"));
        this.world = screen.getWorld();
        this.screen = screen;

        currentStateShooting = State.DEFAULT;
        previousStateShooting = State.DEFAULT;
        stateTimer = 0;

        //Declaring all the different texures and animations the head can be in
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 0, 32, 32));
        south = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        for (int i = 2; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 0, 32, 32));
        eastWest = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        for (int i = 4; i < 6; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 0, 32, 32));
        north = new Animation<TextureRegion>(0.25f, frames);
        frames.clear();

        walkDown = new TextureRegion(getTexture(), 0, 0, 32, 32);
        currentDirection = walkDown;
        walkUp = new TextureRegion(getTexture(), 128, 0, 32, 32);
        walkRightLeft = new TextureRegion(getTexture(), 64, 0, 32, 32);

        //Setting the bounds and region of the textures
        setBounds(0, 0, 32 / icsGame.PPM, 32 / icsGame.PPM);
        setRegion(walkDown);

        //Declaring variables and array
        shootTimer  = 0;
        setToDestroy = false;
        bullets = new Array<bullet>();
    }

    //Is used to switch between if statements, so isaac shoots tears in different positions
    private boolean leftRight = false;
    /**
     * Updates variables, and decides where to spawn bullets when firing
     * @param dt takes in time
     */
    public void update(float dt) {
        //Set posistion and region of head texture
        setPosition(isaac.ISAACX - getWidth() / 2, isaac.ISAACY - getHeight() / 2);
        setRegion(getFrame(dt));

        //Holds how far left and right the bullets should be firing from
        float side = 0.05f;

        //Loops through the bullets and passes them time, also removes the bullet texture if the bullet is destroyed.
        for(bullet bullet : bullets){
            bullet.update(dt);
            if(bullet.isDestroyed())
                bullets.removeValue(bullet, true);
        }

        //Adds time to the shoottimer
        shootTimer += dt;

        //Decides where to spawn the bullets when the player is shooting
        if(currentStateShooting == State.EASTWEST && shootTimer >= SHOOT_WAIT_TIME) {
            shootTimer = 0;
            icsGame.manager.get("audio/sounds/tearFire.mp3", Sound.class).play();
            fireRightLeft();
        }
        else if(currentStateShooting == State.NORTH && shootTimer >= SHOOT_WAIT_TIME && leftRight){
            shootTimer = 0;
            icsGame.manager.get("audio/sounds/tearFire.mp3", Sound.class).play();
            leftRight = false;
            fireUpDown(true, isaac.ISAACX + side, isaac.ISAACY + 0.10f);
        }
        else if(currentStateShooting == State.NORTH && shootTimer >= SHOOT_WAIT_TIME && !leftRight){
            shootTimer = 0;
            icsGame.manager.get("audio/sounds/tearFire.mp3", Sound.class).play();
            leftRight = true;
            fireUpDown(true, isaac.ISAACX - side, isaac.ISAACY + 0.10f);
        }
        else if(currentStateShooting == State.SOUTH && shootTimer >= SHOOT_WAIT_TIME && leftRight){
            shootTimer = 0;
            icsGame.manager.get("audio/sounds/tearFire.mp3", Sound.class).play();
            leftRight = false;
            fireUpDown(false, isaac.ISAACX + side, isaac.ISAACY);
        }
        else if(currentStateShooting == State.SOUTH && shootTimer >= SHOOT_WAIT_TIME && !leftRight){
            shootTimer = 0;
            icsGame.manager.get("audio/sounds/tearFire.mp3", Sound.class).play();
            leftRight = true;
            fireUpDown(false, isaac.ISAACX - side, isaac.ISAACY);
        }

    }

    /**
     * Switches between the different states
     * @param dt takes in a time
     * @return the texture for the state
     */
    public TextureRegion getFrame(float dt) {
        currentStateShooting = getStateShooting();

        switch (currentStateShooting) {
            case SOUTH:
                region = south.getKeyFrame(stateTimer, true);
                break;
            case EASTWEST:
                region = eastWest.getKeyFrame(stateTimer, true);
                break;
            case NORTH:
                region = north.getKeyFrame(stateTimer, true);
                break;
            case WALKUP:
                region = walkUp;
                break;
            case WALKRIGHTLEFT:
                region = walkRightLeft;
                break;
            case WALKDOWN:
                region = walkDown;
                currentDirection = walkDown;
                break;
            case DEFAULT:
            default:
                region = currentDirection;
                break;
        }

        //Flips the texture based one what key is pressed
        if (((Gdx.input.isKeyPressed(Input.Keys.LEFT) || !shootingRight) && !region.isFlipX())) {
            region.flip(true, false);
            shootingRight = false;
        } else if (((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || shootingRight) && region.isFlipX())) {
            region.flip(true, false);
            shootingRight = true;
        }

        if(((Gdx.input.isKeyJustPressed(Input.Keys.A) || !shootingRight) && !region.isFlipX())){
            region.flip(true, false);
            shootingRight = false;
        }
        else if(((Gdx.input.isKeyJustPressed(Input.Keys.D) || shootingRight) && region.isFlipX())){
            region.flip(true, false);
            shootingRight = true;
        }

        //Sets the stateTimer to different values depending on the state
        stateTimer = currentStateShooting == previousStateShooting ? stateTimer + dt : 0;
        previousStateShooting = currentStateShooting;

        return region;
    }

    /**
     * Returns what state to be accessed in the switch
     * @return the state
     */
    public State getStateShooting() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            return State.NORTH;
        } else if ((Gdx.input.isKeyPressed(Input.Keys.LEFT)) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
            return State.EASTWEST;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            return State.SOUTH;
        }else if(Gdx.input.isKeyPressed(Input.Keys.W)){
            return State.WALKUP;
        }else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            return State.WALKDOWN;
        }else if((Gdx.input.isKeyPressed(Input.Keys.D))|| (Gdx.input.isKeyPressed(Input.Keys.A))) {
            return State.WALKRIGHTLEFT;
        }
        else {
            return State.DEFAULT;
        }
    }

    /**
     * Creates a bullet that will fire either right or left
     */
    public void fireRightLeft(){
        bullets.add(new bullet(screen, isaac.ISAACX, isaac.ISAACY, shootingRight ? true : false, false, false));
    }

    /**
     * Creates a bullet that will fire eith up or down
     * @param fireUp decides wether to fire up or down
     * @param x decides what x coordinate to spawn the bullet in
     * @param y decides what y coordinate to spawn the bulle in
     */
    public void fireUpDown(boolean fireUp, float x, float y){
        bullets.add(new bullet(screen, x, y, shootingRight ? true : false, fireUp, true));
    }

    /**
     * Draws the bullets
     * @param batch takes in a batch
     */
    public void draw(Batch batch){
        super.draw(batch);
        for(bullet bullet : bullets){
            bullet.draw(batch);
        }
    }

}
