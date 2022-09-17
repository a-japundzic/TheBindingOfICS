package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.japundzic.icsgame.icsGame;

import java.text.DecimalFormat;

import Scenes.hud;
import sprites.camera;
import sprites.door;
import sprites.isaac;
import sprites.isaacHead;
import sprites.monstro;
import tools.b2WorldCreator;
import tools.worldContactListener;
/**
 * This is the main class for the game, this creates the play screen which displayes all the sprite as well as the tiled map
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class playScreen implements Screen {
    //reference to the game, used to set Screens
    private icsGame game;
    private TextureAtlas atlas;

    //basic playScreen variables
    private OrthographicCamera gameCam;
    public Viewport gamePort;
    private Scenes.hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private b2WorldCreator creator;

    //Sprites
    private isaac player;
    private isaacHead head;
    private sprites.camera camera;

    //Holds the world width and world height
    public static float gameWidth;
    public static float gameHeight;

    //Holds the position of the camera sprite
    public static float whichDoorX;
    public static float whichDoorY;

    //Holds the music
    private Music music;

    /**
     * Playscreen constructor
     *
     */
    public playScreen(icsGame game) {
        //Hold the texture file
        atlas = new TextureAtlas("isaac.pack");

        this.game = game;
        //Create the cam used to show the world
        gameCam = new OrthographicCamera();

        //Create a fitvViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(icsGame.V_WIDTH / icsGame.PPM,icsGame.V_HEIGHT / icsGame.PPM,gameCam);
        //Create game HUD for scores/timer/level info
        hud = new hud(game.batch);

        //Load the map and setup the map renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / icsGame.PPM);

        //Defines gameWidth and gameHeigt
        gameWidth = gamePort.getWorldWidth();
        gameHeight = gamePort.getWorldHeight();

        //initially set the gamecam to be centered correctly at the start of the map
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create the Box2D world, setting no gravity and allowing bodies to sleep
        world = new World(new Vector2(0,0), true);
        //allows for debug lines to show in the box2d world
        b2dr = new Box2DDebugRenderer();

        creator = new b2WorldCreator(this);

        //create the sprites in the game world
        player = new isaac(this);
        head = new isaacHead(this);
        camera = new camera(this);

        world.setContactListener(new worldContactListener());

        //plays the music
        music = icsGame.manager.get("audio/music/gameMusic.mp3", Music.class);
        music.setLooping(true);
        music.play();



    }

    /**
     * returns textture atlas
     * @return atlas
     */
    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    /**
     * Takes in the user input for WASD, giving a certain velocity to the player body depending on the key pressed
     * @param dt takes in delta time
     */
    public void handleInput(float dt){
        if(player.currentState != isaac.State.DEAD) {
            if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2Body.getLinearVelocity().x >= -2 && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(-1.5f, 0f);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2Body.getLinearVelocity().x <= 2 && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(1.5f, 0f);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A) == Gdx.input.isKeyPressed(Input.Keys.D) && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) && player.b2Body.getLinearVelocity().y <= 2 && camera.b2CameraBody.getLinearVelocity().y == 0)
                player.b2Body.setLinearVelocity(0, 1.5f);
            if (Gdx.input.isKeyPressed(Input.Keys.S) && player.b2Body.getLinearVelocity().y >= -2 && camera.b2CameraBody.getLinearVelocity().y == 0)
                player.b2Body.setLinearVelocity(0, -1.5f);
            if ((Gdx.input.isKeyPressed(Input.Keys.A)) && (Gdx.input.isKeyPressed(Input.Keys.S)) && (player.b2Body.getLinearVelocity().x <= 2) && (player.b2Body.getLinearVelocity().y <= 2) && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(-1.5f, -1.5f);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.D)) && (Gdx.input.isKeyPressed(Input.Keys.S)) && (player.b2Body.getLinearVelocity().x <= 2) && (player.b2Body.getLinearVelocity().y <= 2) && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(1.5f, -1.5f);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.A)) && (Gdx.input.isKeyPressed(Input.Keys.W)) && (player.b2Body.getLinearVelocity().x <= 2) && (player.b2Body.getLinearVelocity().y <= 2) && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(-1.5f, 1.5f);
            }
            if ((Gdx.input.isKeyPressed(Input.Keys.D)) && (Gdx.input.isKeyPressed(Input.Keys.W)) && (player.b2Body.getLinearVelocity().x <= 2) && (player.b2Body.getLinearVelocity().y <= 2) && camera.b2CameraBody.getLinearVelocity().x == 0) {
                player.b2Body.setLinearVelocity(1.5f, 1.5f);
            }
        }
    }

    //Holds the starting position for the camera body
    public float startingPosition = 0;

    /**
     * Constantly updates variables and controls the camera.
     * @param dt takes in delta time
     */
    public void update(float dt){
        //handle user input first
        handleInput(dt);

        //Formats numbers to three decimal places.
        DecimalFormat df2 = new DecimalFormat("0.000");

        //takes 1 step in the physics simulation(60 timer per second)
        world.step(1/60f,6,2);

        //Passes time to player
        player.update(dt);

        //pass time to the enemies
        for(sprites.enemy enemy : creator.getEnemies()){
            enemy.update(dt);

        }
        //passes time to the head
        head.update(dt);
        //passes time to the hud
        hud.update(dt);

        //sets the camera position the camera body which is in the middle of the screen
        gameCam.position.x = camera.b2CameraBody.getPosition().x;
        gameCam.position.y = camera.b2CameraBody.getPosition().y;

        //Gets the camera bodies posistion
        whichDoorX = camera.b2CameraBody.getPosition().x;
        whichDoorY = camera.b2CameraBody.getPosition().y;

        //Gets the camera's bodies's position and formats it to 3 decimal places
        float tempX = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().x));
        float tempY = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().y));

        //Prevents the player from moving if the camera is moving
        if(camera.b2CameraBody.getLinearVelocity().x > 0 || camera.b2CameraBody.getLinearVelocity().x < 0){
            player.b2Body.setLinearVelocity(0,0);
        }

        //Stops the camera body after a certain distance in the positive x direction
        if((camera.b2CameraBody.getLinearVelocity().x > 0) && (tempX > Float.parseFloat(df2.format((startingPosition + (2 * 2.09f)))))) {
            camera.b2CameraBody.setLinearVelocity(0, 0);
            camera.b2CameraBody.setTransform(Float.parseFloat(df2.format((startingPosition + (2 * 2.09f)))), tempY, 0);
        }

        //Stops the camera body after a certain distance in the negative x direction
        if(camera.b2CameraBody.getLinearVelocity().x < 0 && (tempX) < Float.parseFloat(df2.format((startingPosition - (2 * 2.09f))))){
            camera.b2CameraBody.setLinearVelocity(0, 0);
            camera.b2CameraBody.setTransform(Float.parseFloat(df2.format((startingPosition - (2 * 2.09f)))), tempY, 0);
        }

        //Stops the camera after a certain distance in the positive y direction
        if(camera.b2CameraBody.getLinearVelocity().y > 0 && tempY > Float.parseFloat(df2.format(startingPosition + (2 * 1.43f)))){
            camera.b2CameraBody.setLinearVelocity(0, 0);
            camera.b2CameraBody.setTransform(tempX ,Float.parseFloat(df2.format((startingPosition + (2 * 1.43f)))), 0);
        }

        //Stops the camera after a certain distance in the negative y direction
        if(camera.b2CameraBody.getLinearVelocity().y < 0 && tempY < Float.parseFloat(df2.format(startingPosition - (2 * 1.43f)))){
            camera.b2CameraBody.setLinearVelocity(0, 0);
            camera.b2CameraBody.setTransform(tempX ,Float.parseFloat(df2.format((startingPosition - (2 * 1.43f)))), 0);
        }

        //If the door is transitioning right set the body velocity to 2 in the x direction.
        if(door.transistionRight){
            startingPosition = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().x));
            camera.b2CameraBody.setLinearVelocity(2f, 0);
            door.transistionRight = false;
        }
        //If the door is transitioning left set the body velocity to 2 in the -x direction
        if(door.transistionLeft){
            startingPosition = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().x));
            camera.b2CameraBody.setLinearVelocity(-2f, 0);
            door.transistionLeft = false;
        }
        //If the door is transitioning up set the body velocity to 2 in the y direction
        if(door.transistionUp){
            startingPosition = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().y));
            System.out.println(startingPosition);
            camera.b2CameraBody.setLinearVelocity(0, 2f);
            door.transistionUp = false;
        }
        //If the door is transitioning down set the body velocity to -2 in the y direction
        if(door.transistionDown){
            startingPosition = Float.parseFloat(df2.format(camera.b2CameraBody.getPosition().y));
            camera.b2CameraBody.setLinearVelocity(0, -2f);
            door.transistionDown = false;
        }


        //If musicStop == true stop the music
        if(monstro.musicStop){
            music.stop();
        }

        //Update the gamecam with correct coordinates after changes
        gameCam.update();
        //Tell the renderer to draw only what our camera can see
        renderer.setView(gameCam);
    }

    //Holds the value used for the sprite flashing after its been hit
    private float alpha = .0f;

    @Override
    /**
     * Renders stuff to the screen
     * @param delta takes in delta time
     */
    public void render(float delta) {
        //separate the update logic from render
        update(delta);

        //Clear the game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the game map
        renderer.render();

        //Renders box2D debug lines
        //b2dr.render(world, gameCam.combined);

        //Draws what the camera sees
        game.batch.setProjectionMatrix(gameCam.combined);
        alpha += delta;
        game.batch.begin();
        //Draws the player body
        if(!isaac.blink){
            player.draw(game.batch);
        }
        //Draws the player body blinking after a hit
        else if(isaac.blink) {
            player.draw(game.batch, +5f * (float) Math.sin(alpha) + .5f);
        }
        //Draws the player head
        if(player.currentState != isaac.State.DEAD && !isaac.blink) {
            head.draw(game.batch);
        }
        //Draws the player head blinking after a hit
        else if(player.currentState != isaac.State.DEAD && isaac.blink){
            head.draw(game.batch, +5f * (float) Math.sin(alpha) + .5f);
        }
        //Draws the enemies
        for(sprites.enemy enemy : creator.getEnemies()){
            enemy.draw(game.batch);
        }
        game.batch.end();

        //Set the batch to now draw what the HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        //If the gameOver is true set the screen to gameOverScreen
        if(gameOver()){
            music.stop();
            game.setScreen(new gameOverScreen(game));
        }

        //If gameWin is true set the screen to winScreen
        if(gameWin()){
            music.stop();
            game.setScreen(new winScreen(game));
        }
    }

    /**
     * returns if the game is over or not
     */
    public boolean gameOver(){
        if(player.currentState == isaac.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * returns if the game is won or not
     * @return true or false
     */
    public boolean gameWin(){
        if(monstro.health <= 0 && monstro.stateTime > 6){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    /**
     * Updates the game viewport
     * @param width takes in a width
     * @param hieght takes in a height
     */
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    /**
     * Returns map
     * @return map
     */
    public TiledMap getMap(){
        return map;
    }

    /**
     * Returns world
     * @return world;
     */
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    /**
     * Disposes of stuff
     */
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
