package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.japundzic.icsgame.icsGame;
/**
 * Prints the main menu
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class menuScreen implements Screen {
    //Scene2D.ui Stage and its own viewport for controlScreen
    private Viewport viewport;
    private Stage stage;

    //Camera to view the sprite, and sprite to hold the sprite;
    private OrthographicCamera gameCam;
    private SpriteBatch sb;

    //Application listener that switches screens
    private Game game;

    //Holds the texture
    private Texture backGround;

    //Holds the music
    private Music music;

    /**
     * Creates the table and defines the variables;
     * @param game holds game
     *
     */
    public menuScreen(Game game){
        //Degine the variables
        this.game = game;
        gameCam = new OrthographicCamera();
        viewport = new FitViewport(icsGame.V_WIDTH, icsGame.V_HEIGHT, gameCam);
        stage = new Stage(viewport, ((icsGame) game).batch);

        //Give the texture and image
        backGround = new Texture("mainbackground2.png");

        //Create the table and add the image to it
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.background(new TextureRegionDrawable(new TextureRegion(backGround)));

        //Add the table as an actor
        stage.addActor(table);

        //play music
        music = icsGame.manager.get("audio/music/titleScreenIntro.ogg", Music.class);
        music.play();
        music = icsGame.manager.get("audio/music/titleScreenLoop.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {
    }

    @Override
    /**
     * renders the stuff to the screen and switches the screen depending on the input
     * @param delta holds time
     *
     */
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            game.setScreen(new controlsScreen((icsGame) game));
            dispose();
        }
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
     * Disposes of stage
     *
     */
    public void dispose() {
        stage.dispose();
    }
}
