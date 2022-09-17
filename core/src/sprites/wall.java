package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import screens.playScreen;
/**
 * Does something when there is a collision with the wall
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class wall extends interactiveTileObject{
    public static boolean destroyed = false;
    public wall(playScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
    }

    @Override
    public void onBodyHit(isaac isaac) {
    }

}
