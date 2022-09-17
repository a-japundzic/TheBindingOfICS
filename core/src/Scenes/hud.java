package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.japundzic.icsgame.icsGame;

import sprites.isaac;

/**
 * Creates a hud for the screen
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class hud implements Disposable {
    //Scene2D.ui Stage and its own viewport for HUD
    public Stage stage;
    private Viewport viewPort;

    //Timer and health tracking variables
    private Integer worldTimer;
    private float timeCount;
    public static int health;

    //Scene2d Widgets
    Label countUpLabel;
    Label healthLabel;
    Label timeLabel;
    Label levelLabel;
    Label levelNameLabel;
    Label isaacLabel;

    /**
     * Hud Constructer
     * @param sb holds the SpriteBatch
     *
     */
    public hud(SpriteBatch sb){

        //define tracking variables
        worldTimer = 999;
        timeCount = 0;
        health = 3;

        //setup the hud viewport using a new camera seperate from gamecam
        //define the stage using that viewport and the games spritebatch
        viewPort = new FitViewport(icsGame.V_WIDTH, icsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        //define a table used to organize the hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define the labels for the table using the string and label style = font and font colour
        countUpLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label(String.format("%01d", health), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelNameLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        isaacLabel = new Label("HP", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Add the labels to te table, padding the top, and giving them all equal width with expandX
        table.add(isaacLabel).expandX().padTop(10);
        table.add(levelNameLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //Add a secon row
        table.row();
        table.add(healthLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countUpLabel).expandX();

        //add our table to the stage
        stage.addActor(table);
    }

    /**
     * Updates our health and time variables
     * @param dt holds the time
     *
     *
     */
    public void update(float dt){
        //Counts time down
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer --;
            countUpLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }

        //Counts HP
        if(isaac.isaacHit){
            health --;
            healthLabel.setText(String.format("%01d", health));
            isaac.isaacHit = false;
        }

    }

    @Override
    /**
     * disposes of stage.
     */
    public void dispose() {
        stage.dispose();
    }
}
