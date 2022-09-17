package tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.japundzic.icsgame.icsGame;

import sprites.bullet;
import sprites.enemy;
import sprites.enemyBullet;
import sprites.interactiveTileObject;
import sprites.isaac;
/**
 * Contact listener which listens for collisions and calls methods when they happen
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */

public class worldContactListener implements ContactListener {
    @Override
    /**
     * Listens for contact between two fixtures, and then calls a method depending on the contact that happened
     * @param contact takes in a new contact
     */
    public void beginContact(Contact contact) {
        //Hold the first and second fixture involved in the contact
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Holds the two fixtures that collided
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        //Switches between the different possible collisions and calls a certain method depending on the one that happened, usually it is a destroy method to destroy the body after a contact
        switch (cDef) {
           case icsGame.ISAAC_BIT | icsGame.DOOR_BIT:
            case icsGame.ISAAC_HEAD_BIT | icsGame.DOOR_BIT:
               if(fixA.getFilterData().categoryBits == icsGame.ISAAC_BIT) {
                   ((interactiveTileObject) fixB.getUserData()).onBodyHit((isaac) fixA.getUserData());
               }
               else{
                   ((interactiveTileObject) fixA.getUserData()).onBodyHit((isaac) fixB.getUserData());
               }
               break;
            case icsGame.BULLET_BIT | icsGame.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.ENEMY_BIT) {
                    ((enemy) fixA.getUserData()).hit();
                    ((bullet) fixB.getUserData()).setToDestroy();
                } else if (fixB.getFilterData().categoryBits == icsGame.ENEMY_BIT) {
                    ((enemy) fixB.getUserData()).hit();
                    ((bullet) fixA.getUserData()).setToDestroy();
                }
                break;
            case icsGame.BULLET_BIT | icsGame.DOOR_BIT:
            case icsGame.BULLET_BIT | icsGame.DEAFAULT_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.BULLET_BIT) {
                    ((bullet) fixA.getUserData()).setToDestroy();
                } else if (fixB.getFilterData().categoryBits == icsGame.BULLET_BIT) {
                    ((bullet) fixB.getUserData()).setToDestroy();
                }
                break;
            case icsGame.ENEMY_BULLET_BIT | icsGame.DOOR_BIT:
            case icsGame.ENEMY_BULLET_BIT | icsGame.DEAFAULT_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.ENEMY_BULLET_BIT) {
                    ((enemyBullet) fixA.getUserData()).setToDestroy();
                } else if (fixB.getFilterData().categoryBits == icsGame.ENEMY_BULLET_BIT) {
                    ((enemyBullet) fixB.getUserData()).setToDestroy();
                }
                break;
            case icsGame.ENEMY_BULLET_BIT | icsGame.ISAAC_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.ISAAC_BIT) {
                    ((isaac) fixA.getUserData()).hit();
                    ((enemyBullet) fixB.getUserData()).setToDestroy();
                } else if (fixB.getFilterData().categoryBits == icsGame.ISAAC_BIT) {
                    ((isaac) fixB.getUserData()).hit();
                    ((enemyBullet) fixA.getUserData()).setToDestroy();
                }
                break;
            case icsGame.ENEMY_BULLET_BIT | icsGame.ISAAC_HEAD_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.ISAAC_HEAD_BIT) {
                    ((isaac) fixA.getUserData()).hit();
                    ((enemyBullet) fixB.getUserData()).setToDestroy();
                } else if (fixB.getFilterData().categoryBits == icsGame.ISAAC_HEAD_BIT) {
                    ((isaac) fixB.getUserData()).hit();
                    ((enemyBullet) fixA.getUserData()).setToDestroy();
                }
                break;
            case icsGame.ISAAC_HEAD_BIT | icsGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == icsGame.ISAAC_HEAD_BIT) {
                    ((isaac) fixA.getUserData()).hit();
                }
                else{
                    ((isaac) fixB.getUserData()).hit();
                }
                break;
            case icsGame.ISAAC_BIT | icsGame.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == icsGame.ISAAC_BIT) {
                    ((isaac) fixA.getUserData()).hit();
                }
                else{
                    ((isaac) fixB.getUserData()).hit();
                }
                break;
            case icsGame.ENEMY_BIT | icsGame.DEAFAULT_BIT:
                if (fixA.getFilterData().categoryBits == icsGame.ENEMY_BIT) {
                    ((enemy) fixA.getUserData()).reverseVelocity(true, true);
                } else if (fixB.getFilterData().categoryBits == icsGame.ENEMY_BIT) {
                    ((enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case icsGame.ENEMY_BIT | icsGame.ENEMY_BIT:
                    ((enemy) fixA.getUserData()).reverseVelocity(true, false);
                    ((enemy) fixB.getUserData()).reverseVelocity(true, false);
                    break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
