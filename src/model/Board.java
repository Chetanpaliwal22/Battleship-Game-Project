package model;

import tools.Coordinate;
import constants.Constants;
import view.MainWindow;

import java.util.ArrayList;

/**
 * Class to represent a player board
 */
public class Board {

    Coordinate boardSize;

    boolean gameHasStarted = false;

    Ship[][] waterGrid;
    int[][] waterGridState;

    private ArrayList<Ship> ships;

    public int sunkNumber = 0;


    /**
     * argument constructor
     */
    public Board() {

        this.boardSize = Constants.BOARD_SIZE;

        this.waterGrid = new Ship[this.boardSize.x][this.boardSize.y];
        this.waterGridState = new int[this.boardSize.x][this.boardSize.y];

        int nbShips = Constants.DESTROYER_NB + Constants.SUBMARINE_NB + Constants.CRUISER_NB + Constants.BATTLESHIP_NB + Constants.CARRIER_NB;
        this.ships = new ArrayList<Ship>();
    }

    /**
     * set that the game has started
     */
    public void setGameHasStarted() {
        gameHasStarted = true;
    }

    public void placeShipsRandomly() {

        // hardcoded for now

        Coordinate[] ship1Coordinate = {new Coordinate(4, 1), new Coordinate(4, 2)};
        Coordinate[] ship2Coordinate = {new Coordinate(3, 7), new Coordinate(4, 7), new Coordinate(5, 7)};
        Coordinate[] ship3Coordinate = {new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)};
        Coordinate[] ship4Coordinate = {new Coordinate(5, 3), new Coordinate(6, 3), new Coordinate(7, 3), new Coordinate(8, 3)};
        Coordinate[] ship5Coordinate = {new Coordinate(2, 0), new Coordinate(2, 1), new Coordinate(2, 2), new Coordinate(2, 3), new Coordinate(2, 4)};


        try {
            placeShip(ship1Coordinate);
            placeShip(ship2Coordinate);
            placeShip(ship3Coordinate);
            placeShip(ship4Coordinate);
            placeShip(ship5Coordinate);

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /**
     * Allow human or AI to place a new ship on the board
     *
     * @param newShipCoordinate - New Ship Coordinate
     * @throws Exception - Throws Error if the ships are moved after the game has started.
     */
    public void placeShip(Coordinate[] newShipCoordinate) throws Exception {

        if (gameHasStarted) {
            throw new Exception("Can't move the ships after the beginning of the game.");
        }

        // create a new ship
        Ship newShip = new Ship(newShipCoordinate.length, newShipCoordinate);

        // add ship reference to the grid
        for (int i = 0; i < newShipCoordinate.length; i++) {
            waterGrid[newShipCoordinate[i].y][newShipCoordinate[i].x] = newShip;
        }

        // add ship to ship index
        ships.add(newShip);
    }


    public void updateShip(int shipIndex, ArrayList<Coordinate> targetHitPosition, boolean liveState) {
        ships.get(shipIndex).setHitPosition(targetHitPosition);
        ships.get(shipIndex).setIsLive(liveState);
    }


    /**
     * process a fire on the board and respond accordingly
     *
     * @param target accept target as prameter
     * @return the  code for result
     * @throws Exception throws exception
     */
    public int fireAtTarget(Coordinate target) throws Exception {

        if (!gameHasStarted) {
            throw new Exception("Ships should be placed before playing.");
        }

        if (waterGrid[target.y][target.x] == null) { // That's a miss

            waterGridState[target.y][target.x] = 1;

            return 0;

        } else if (waterGrid[target.y][target.x].isHit(target)) { // that's a hit !

            if (waterGrid[target.y][target.x].isSunk()) { // ship has sunk

                waterGridState[target.y][target.x] = 2;
                sunkNumber++;

                return 2;

            } else {

                waterGridState[target.y][target.x] = 2;

                return 1;
            }

            // invalid coordinate
        } else {
            throw new Exception("Invalid coordinate provided as target.");
        }

    }


    public ArrayList<Ship> getShips() {
        return ships;
    }


    /**
     * return the state of the board as a 2 dimensional array of integer
     * WIP
     *
     * @return the board state
     */
    public int[][] getBoardState() {
        return waterGridState;
    }


    public void setBoardState(int[][] boardState) {
        waterGridState = boardState;
    }


    public void setBoardState(int x, int y) {
        waterGridState[y][x] = 1;
    }


    /**
     * print the state grid in the console
     */
    public void printStateGrid() {

        System.out.println("State grid :");

        for (int i = this.boardSize.x - 1; i >= 0; i--) {
            for (int j = 0; j < this.boardSize.y; j++) {
                System.out.print(waterGridState[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * print the ship grid in the console
     */
    public void printShipGrid() {

        System.out.println("Ship grid :");

        for (int i = this.boardSize.x - 1; i >= 0; i--) {
            for (int j = 0; j < this.boardSize.y; j++) {

                if (waterGrid[i][j] != null) {

                    System.out.print(1);

                } else {
                    System.out.print(0);
                }

            }
            System.out.println();
        }

    }

    /**
     * check if all ship are sunk
     * @return return true if sunk
     */
    public boolean checkSunk() {
        boolean shipResult = false;

        for (int i = 0; i < MainWindow.shipList.size(); i++) {

            if (!MainWindow.shipList.get(i).sunk) {
                int hitCount = 0;

                for (int j = 0; j < MainWindow.shipList.get(i).occupiedGridX.size(); j++) {

                    if (MainWindow.humanBoard.getBoardState()[MainWindow.shipList.get(i).occupiedGridY
                            .get(j)][MainWindow.shipList.get(i).occupiedGridX.get(j)] == 1
                            | MainWindow.humanBoard.getBoardState()[MainWindow.shipList.get(i).occupiedGridY
                            .get(j)][MainWindow.shipList.get(i).occupiedGridX.get(j)] == 2) {

                        hitCount += 1;
                    }
                }

                if (!MainWindow.shipList.get(i).sunk) {
                    if (hitCount == MainWindow.shipList.get(i).size) {
                        MainWindow.shipList.get(i).sunk = true;

                        // Reduce the number of shots by 1 when the player lost ship
                        MainWindow.numberOfPlayerMaxShots -= 1;
                        MainWindow.timerLabel.setText("<html>Number of shots: " + MainWindow.numberOfPlayerShots + "/" + MainWindow.numberOfPlayerMaxShots + "</html>");

                        return true;
                    }
                }
            }
        }

        return shipResult;
    }


    /**
     * check number of sunk ship
     *
     * @return the board state
     */
    public boolean checkPlayerSunkShips() {

        int numberOfSunkShips = 0;

        for (int i = 0; i < MainWindow.shipList.size(); i++) {

            if (MainWindow.shipList.get(i).sunk) {
                numberOfSunkShips += 1;
            }
        }

        if (numberOfSunkShips == 5) {

            return true;

        } else {
            return false;
        }
    }
}
