package main;

import constants.Constants;
import model.Board;
import view.MainWindow;

public class Game {

    public static MainWindow mainWindow;

    public static void main(String[] args) {

        // create game GUI //

        mainWindow = new MainWindow();
        mainWindow.setVisible(true);

        // first instantiate the boards //

        Board humanBoard = new Board(Constants.BOARD_SIZE,
                1, 1, 1, 1, 1,
                Constants.DESTROYER_SIZE, Constants.SUBMARINE_SIZE, Constants.CRUISER_SIZE,
                Constants.BATTLESHIP_SIZE, Constants.CARRIER_SIZE);

        Board AIBoard = new Board(Constants.BOARD_SIZE,
                1, 1, 1, 1, 1,
                Constants.DESTROYER_SIZE, Constants.SUBMARINE_SIZE, Constants.CRUISER_SIZE,
                Constants.BATTLESHIP_SIZE, Constants.CARRIER_SIZE);


        // then place the ships //

        //shipList.add(new Ship(2, 2, 6, 1));
        //shipList.add(new Ship(3, 3, 2, 3));
        //shipList.add(new Ship(3, 1, 8, 4));
        //shipList.add(new Ship(4, 3, 2, 7));
        //shipList.add(new Ship(5, 3, 4, 5));

        /*for (int i = 0; i < shipList.get(0).occupiedGridX.size(); i++) {
            System.out.println(
                    i + ": " + shipList.get(0).occupiedGridX.get(i) + ", " + shipList.get(0).occupiedGridY.get(i));
        }*/

        // for (int i = 0; i < shipList.get(1).occupiedGridX.size(); i++) {
        // System.out.println(i + ": " + shipList.get(1).occupiedGridX.get(i) + ", " +
        // shipList.get(1).occupiedGridY.get(i));
        // }

        // MAIN GAME LOOP OF THE GAME


    }
}
