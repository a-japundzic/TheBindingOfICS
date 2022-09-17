package screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.japundzic.icsgame.icsGame;
/**
 * Creates the contorls screen
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class controlsScreen implements Screen {
    //Scene2D.ui Stage and its own viewport for controlScreen
    private Viewport viewport;
    private Stage stage;

    //Application lisstener that switches screens
    private Game game;

    //Stores the music
    private Music music;

    /**
     * Creates the table that holds and spaces out all the text for the screen
     * @param game holds game
     *
     */
    public controlsScreen(Game game){
        //Sets the viewport and the stage
        this.game = game;
        viewport = new FitViewport(icsGame.V_WIDTH, icsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((icsGame) game).batch);

        //Creates the table and labels
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Label instructionsTitle = new Label("Instructions and Controls", font);
        Label key = new Label("Key", font);
        Label action = new Label("Action", font);
        Label walkUp = new Label("Walk Up:", font);
        Label walkDown = new Label("Walk Down:", font);
        Label walkRight = new Label("Walk Right:", font);
        Label walkLeft = new Label("Walk Left:", font);
        Label shootUp = new Label("Shoot Up:", font);
        Label shootDown = new Label("Shoot Down:", font);
        Label shootLeft = new Label("Shoot Left:", font);
        Label shootRight = new Label("Shoot Right:", font);
        Label A = new Label("A", font);
        Label S = new Label("S", font);
        Label W = new Label("W", font);
        Label D = new Label("D", font);
        Label Left = new Label("Left", font);
        Label Down = new Label("Down", font);
        Label Up = new Label("Up", font);
        Label Right = new Label("Right", font);
        Label instructions = new Label("Kill the boss to win!", font);
        Label play = new Label("Press Start to Begin", font);

        table.add(instructionsTitle).padTop(10);
        table.row();
        table.add(action).expandX();
        table.add(key).expandX();
        table.row();
        table.add(walkUp).expandX();
        table.add(W).expandX();
        table.row();
        table.add(walkDown).expandX();
        table.add(S).expandX();
        table.row();
        table.add(walkLeft).expandX();
        table.add(A).expandX();
        table.row();
        table.add(walkRight).expandX();
        table.add(D).expandX();
        table.row();
        table.add(shootUp).expandX();
        table.add(Up).expandX();
        table.row();
        table.add(shootDown).expandX();
        table.add(Down).expandX();
        table.row();
        table.add(shootLeft).expandX();
        table.add(Left).expandX();
        table.row();
        table.add(shootRight).expandX();
        table.add(Right).expandX();
        table.row();
        table.add(instructions).center().padTop(10);
        table.row();
        table.add(play).center().padTop(10);

        stage.addActor(table);

        //Plays the music
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
     * renders the stuff on the screen and switches the screen on user input
     * @param delta holds the time
     *
     */
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            music.stop();
            music.stop();
            music = icsGame.manager.get("audio/music/titleScreenJingle.ogg", Music.class);
            music.play();
            game.setScreen(new playScreen((icsGame) game));
            dispose();

        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
     * Disposes of the stage
     *
     */
    public void dispose() {
        stage.dispose();
    }
}
