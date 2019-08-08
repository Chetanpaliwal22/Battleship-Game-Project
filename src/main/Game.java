package main;

import java.io.File;

import constants.Constants;
import exception.CustomException;
import jaco.mp3.player.MP3Player;
import view.MainWindow;

/**
 * This class is the starting point of the Game.
 */

public class Game {

	public static MainWindow mainWindow;

	public static void main(String[] args) {

		// Start the game //

		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
		try {
//			new MP3Player(new File(Constants.MUSIC_FILE_NAME)).play();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
