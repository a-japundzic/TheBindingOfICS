package sprites;


import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;
/**
 * Creates the wall bodies and setups the door class
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public abstract class interactiveTileObject {
    //world holds the world
    protected World world;
    //Holds the tiled map
    protected TiledMap map;
    //Holds a rectangle
    protected Rectangle bounds;
    //Holds a body
    protected Body body;

    //Holds a fixture
    protected Fixture fixture;

    /**
     * constructer for interactiveTileObject
     * @param screen takes in a screen
     * @param bounds takes in a rectangle
     */
    public interactiveTileObject(playScreen screen, Rectangle bounds){
        //Declares variables
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        //Deefines the walls and doors
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        fdef.filter.categoryBits = icsGame.DEAFAULT_BIT;

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / icsGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / icsGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / icsGame.PPM, bounds.getHeight() / 2 / icsGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    //Class called when a collison is detected
    public abstract void onBodyHit(isaac isaac);

    /**
     * Sets the filetdata
     * @param filterBit takes in a filterBit
     */
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
