package com.japundzic.icsgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.japundzic.icsgame.icsGame;
/**
 * Creates the desktop launcher and sets the application size, which is twice the camera size.
 *
 * Authors: Andreja Japundzic
 * Prompt: project
 * Date Created: Porject Start Date
 * Last Modified: Porject End Date
 * Assumptions: none
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 836;
		config.height = 572;
		new LwjglApplication(new icsGame(), config);
	}
}
