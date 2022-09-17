package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;
/**
 * Does something when there is a collision with the door
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class door extends interactiveTileObject{
    //Booleans that detect tell the playscreen which transistion to do
    public static boolean transistionLeft = false;
    public static boolean transistionRight = false;
    public static boolean transistionUp = false;
    public static boolean transistionDown = false;

    /**
     * constructer for the door
     * @param screen takes in a screen
     * @param bounds takes in a rectangle
     */
    public door(playScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(icsGame.DOOR_BIT);
    }

    @Override
    /**
     * Detects which door was collided with depending on the player posistion
     * @param isaac takes in the player so that its posistion can be used
     */
    public void onBodyHit(isaac isaac) {
        Gdx.app.log("Door", "Collision");
        if(isaac.ISAACX > playScreen.whichDoorX + 1 && (isaac.ISAACY < playScreen.whichDoorY + 0.9f || isaac.ISAACY > playScreen.whichDoorY - 0.9f)){
            transistionRight = true;
        }
        else if(isaac.ISAACX < playScreen.whichDoorX - 1 && (isaac.ISAACY < playScreen.whichDoorY + 0.9f || isaac.ISAACY > playScreen.whichDoorY - 0.9f)) {
            transistionLeft = true;
        }

        if (isaac.ISAACY > playScreen.whichDoorY + 0.9f && (isaac.ISAACX < playScreen.whichDoorX + 1 || isaac.ISAACX > playScreen.whichDoorX - 1)) {
            transistionUp = true;
        }
        else if(isaac.ISAACY < playScreen.whichDoorY - 0.9f && (isaac.ISAACX < playScreen.whichDoorX + 1 || isaac.ISAACX > playScreen.whichDoorX - 1)) {
            transistionDown = true;
        }
    }
}
