package sprites;

import com.badlogic.gdx.audio.Music;
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

import screens.menuScreen;
import screens.playScreen;
/**
 * Creates the boss monstro
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class monstro extends enemy{
    //Holds the time
    public static float stateTime;
    //Holds the animation and the frames
    private Animation<TextureRegion> movementAnimation;
    private Array<TextureRegion> frames;

    //Holds the texture atlas
    private TextureAtlas atlas;

    //booleans to detect wether the boss is destroyed or not
    private boolean setToDestroy;
    private boolean destroyed;

    //Sets the health of the boss
    public static int health = 20;

    //Holds the enemy bullets
    private Array<enemyBullet> enemyBullets;

    //timer for the bullets and the delay between the bullets
    private float bulletTimer;
    private float BULLET_DELAY_TIMER = 1;

    //Holds the music
    private Music music;
    //Boolean that tells the playScreen to stop playing music
    public static boolean musicStop;
    /**
     * constructer for monsto
     * @param screen takes in a screen
     * @param x takes in a x value
     * @param y takes in a y value
     */
    public monstro(playScreen screen, float x, float y) {
        super(screen, x, y);

        //Declares the atlas
        atlas = new TextureAtlas("monstro.pack");

        //Declares the animation after adding all the texture frames to frames
        frames = new Array<TextureRegion>();
        for(int i = 2; i < 4; i++){
            frames.add(new TextureRegion(atlas.findRegion("monstro"), i * 80, 32, 80, 80));
        }
        movementAnimation = new Animation<TextureRegion>(0.5f, frames);
        frames.clear();

        //Declares time variables
        stateTime = 0;
        bulletTimer = 0;

        //Sets the bounds of the texture
        setBounds(getX(), getY(), 80 / icsGame.PPM, 80 / icsGame.PPM);

        //Declares the booleans
        setToDestroy = false;
        destroyed = false;

        //Declares the bullets array
        enemyBullets = new Array<enemyBullet>();

        //Declares the musicStop boolean
        musicStop = false;

        //Declares music
        music =  icsGame.manager.get("audio/music/bossFight.ogg", Music.class);
        music.setLooping(true);
    }

    @Override
    /**
     * Updates variables, checks if the boss is dead, and creates the enemy bullets
     * @param screen takes in a screen
     * @param x takes in a x value
     * @param y takes in a y value
     */
    public void update(float dt) {
        //passes time into the bullet, and destroys the texture if the body is destroyed
        for(enemyBullet enemyBullet : enemyBullets){
            enemyBullet.update(dt);
            if(enemyBullet.isDestroyed())
                enemyBullets.removeValue(enemyBullet, true);
        }

        //Destroys the boss if the health is less than or = to 0
        if(health <= 0){
            setToDestroy = true;
        }
        //Destroys the boss
        stateTime += dt;
        if(setToDestroy && !destroyed){
            music.stop();
            icsGame.manager.get("audio/sounds/bossDeathBurst.mp3", Sound.class).play();
            icsGame.manager.get("audio/sounds/bossGrunt.mp3", Sound.class).play();
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        //set the position and region or the boss texture, okays music, and creates the bullets
        else if(!destroyed){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 0.01f);
            setRegion(movementAnimation.getKeyFrame(stateTime, true));

            System.out.println(bulletTimer);

            if(b2body.getPosition().x < playScreen.whichDoorX + 0.5f) {
                musicStop = true;
                music.play();
                bulletTimer += dt;
            }
            if(bulletTimer >= BULLET_DELAY_TIMER){
                bulletTimer = 0;
                icsGame.manager.get("audio/sounds/bossFire.mp3", Sound.class).play();
                fireRightLeft(true);
                fireRightLeft(false);
                fireUpDown(true, b2body.getPosition().x, b2body.getPosition().y);
                fireUpDown(false, b2body.getPosition().x, b2body.getPosition().y);
            }
        }
    }

    /**
     * Creates a new bullet that shoots left or right
     */
    public void fireRightLeft(boolean shootingRight){
        enemyBullets.add(new enemyBullet(screen, b2body.getPosition().x, b2body.getPosition().y, shootingRight ? true : false, false, false));
    }

    /**
     * Create a new bullet either up or down
     * @param fireUp takes in wether the bullet should be fired up or not
     * @param x takes in where to spawn that bullet in the x coordinate
     * @param y takes in where to spawn that bullet in the y coordinate
     */
    public void fireUpDown(boolean fireUp, float x, float y){
        enemyBullets.add(new enemyBullet(screen, x, y, false, fireUp, true));
    }

    @Override
    /**
     * Defines the bosses body
     */
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape3 = new CircleShape();
        shape3.setRadius(20 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.ENEMY_BIT;
        fdef.filter.maskBits = icsGame.DEAFAULT_BIT | icsGame.BULLET_BIT | icsGame.ISAAC_BIT | icsGame.ENEMY_BIT | icsGame.DOOR_BIT;

        fdef.shape = shape3;
        b2body.createFixture(fdef).setUserData(this);
    }

    /**
     * Draws the bullet texture to the screen
     * @param batch takes in a batch
     */
    public void draw(Batch batch){
        if(!destroyed || stateTime < 0.4f){
            super.draw(batch);
            for(enemyBullet enemyBullet : enemyBullets){
                enemyBullet.draw(batch);
            }
        }
    }

    @Override
    /**
     * if hit health -= 1;
     */
    public void hit() {
        health --;
    }
}
