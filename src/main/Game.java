package main;

import view.MainWindow;

public class Game {

    public static MainWindow mainWindow;

    public static void main(String[] args) {

        // Start the game //

        mainWindow = new MainWindow();
        mainWindow.setVisible(true);

    }
}
