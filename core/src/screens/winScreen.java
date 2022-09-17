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
 * Exact same as the gameOverScreen it just prints something slightly different
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class winScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    private Music music;
    public winScreen(Game game){
        this.game = game;
        viewport = new FitViewport(icsGame.V_WIDTH, icsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((icsGame) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameWinLabel = new Label("You Win!", font);
        Label playAgainLabel = new Label("Thanks For Playing.", font);

        table.add(gameWinLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);

        music = icsGame.manager.get("audio/music/credits.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
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
    public void dispose() {
        stage.dispose();
    }
}
