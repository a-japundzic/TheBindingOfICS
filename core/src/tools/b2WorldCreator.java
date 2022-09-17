package tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;
import sprites.door;
import sprites.enemy;
import sprites.flies;
import sprites.monstro;
import sprites.wall;
import sprites.deathsHead;
import sprites.nerveEnding;

/**
 * Takes in the tiledMap object settings and spawns the doors, walls, and enemies based on where those objects are on the tiled map
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class b2WorldCreator {
    //Holds all the deathsHead enemies
    private Array<deathsHead> deathsHead;
    //Holds all the nerveEndings enemies
    private Array<nerveEnding> nerveEndings;
    //Holds all the flies enemies
    private Array<flies> flies;
    //Holds all the monstro enemies;
    private Array<monstro> monstro;

    /**
     * Adds all the enemy arrays into one single enemy array to be rendered
     */
    public Array<enemy> getEnemies(){
        Array<enemy> enemies = new Array<enemy>();
        enemies.addAll(deathsHead);
        enemies.addAll(nerveEndings);
        enemies.addAll(flies);
        enemies.addAll(monstro);
        return enemies;
    }

    /**
     * Sets the posistion of the doors, walls, and enemies based on where the objects are in the tile map
     * @param screen takes in a screen
     */
    public b2WorldCreator(playScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //walls
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new wall(screen, rect);

        }

        //Doors
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new door(screen, rect);
        }

        //deathsHead
        deathsHead = new Array<deathsHead>();
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            deathsHead.add(new deathsHead(screen, rect.getX() / icsGame.PPM, rect.getY() / icsGame.PPM));
        }

        //nerveEndings
        nerveEndings = new Array<nerveEnding>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            nerveEndings.add(new nerveEnding(screen, rect.getX() / icsGame.PPM, rect.getY() / icsGame.PPM));
        }

        //flies
        flies = new Array<flies>();
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flies.add(new flies(screen, rect.getX() / icsGame.PPM, rect.getY() / icsGame.PPM));
        }

        //monstro
        monstro = new Array<monstro>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            monstro.add(new monstro(screen, rect.getX() / icsGame.PPM, rect.getY() / icsGame.PPM));
        }


    }
}
