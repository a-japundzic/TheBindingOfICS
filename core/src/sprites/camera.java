package sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.japundzic.icsgame.icsGame;

import screens.playScreen;

/**
 * Creates the camera body
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class camera extends Sprite {
    //Holds the world
    public World world;

    //Holds the circle shape
    public CircleShape shape3;
    //Holds the camera body
    public Body b2CameraBody;


    /**
     * constructor for camera
     * @param screen takes in a screen
     */
    public camera(playScreen screen){
        this.world = screen.getWorld();

        //defines the camera
        defineCamera();
    }

    public void update(float dt){

    }

    /**
     * defines the camera
     */
    public void defineCamera(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(screens.playScreen.gameWidth / 2, screens.playScreen.gameHeight / 2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2CameraBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        shape3 = new CircleShape();
        shape3.setRadius(6 / icsGame.PPM);
        fdef.filter.categoryBits = icsGame.CAMERA_BIT;

        fdef.shape = shape3;
        fdef.isSensor = true;
        b2CameraBody.createFixture(fdef);
    }

}
