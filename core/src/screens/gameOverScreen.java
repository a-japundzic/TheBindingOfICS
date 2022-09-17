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
 * Creates the gameOver screen
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class gameOverScreen implements Screen {
    //Scene2D.ui Stage and its own viewport for controlScreen
    private Viewport viewport;
    private Stage stage;

    //Application lisstener that switches screens
    private Game game;

    Music music;

    /**
     * Creates the table that holds and spaces out all the text for the screen
     * @param game holds game
     *
     */
    public gameOverScreen(Game game){
        //Scene2D.ui Stage and its own viewport for controlScreen
        this.game = game;
        viewport = new FitViewport(icsGame.V_WIDTH, icsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((icsGame) game).batch);

        //Creates the table and fonts
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Press space to Play Again", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        //Adds the table as an actor
        stage.addActor(table);

        music = icsGame.manager.get("audio/music/death.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    /**
     * renders the stuff onto the screen and switches screens on user input
     * @param delta holds time
     *
     */
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            music.stop();
            game.setScreen(new menuScreen((icsGame) game));
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
     * Disposes of stage.
     *
     */
    public void dispose() {
        stage.dispose();
    }
}
