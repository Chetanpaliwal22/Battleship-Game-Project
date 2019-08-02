package model;

import constants.Constants;
import tools.Coordinate;
import tools.GridHelper;

import java.util.ArrayList;

/**
 * This class contains the backend logic of AI
 */

public class AI {

    private Coordinate boardSize;

    boolean targetMode = false;

    int direction = -1;
    int distanceFromHit = 1;
    boolean fixedDirection = false;
    boolean seekAgain = false;
    Coordinate axis;

    private Coordinate previousTarget = new Coordinate(-1, -1);
    private ArrayList<Coordinate> previousTargetSalvation;

    private int[][] countGrid;

    private int[][] destroyerCountGrid;
    private int[][] submarineCountGrid;
    private int[][] cruiserCountGrid;
    private int[][] battleshipCountGrid;
    private int[][] carrierCountGrid;

    private int nbDestroyerDestroyed = 0;
    private int nbSubmarineDestroyed = 0;
    private int nbCruiserDestroyed = 0;
    private int nbBattleshipDestroyed = 0;
    private int nbCarrierDestroyed = 0;

    private ArrayList<Coordinate> missToExclude;
    private ArrayList<Coordinate> toExclude;

    private int sunkNumber = 0;


    /**
     * default constructor
     */
    public AI() {

        this.boardSize = Constants.BOARD_SIZE;

        this.missToExclude = new ArrayList<Coordinate>(boardSize.x * boardSize.y);
        this.toExclude = new ArrayList<Coordinate>(boardSize.x * boardSize.y);

        this.previousTargetSalvation = new ArrayList<Coordinate>(5);

        try {
            updateCountGrid(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        printCountGrid();
    }


    /**
     * update the count grid given the new information (or just initialize)
     *
     * @param code attack result
     * @throws Exception thorws exception if the ship length is bigger then the board size
     */
    private void updateCountGrid(int code) throws Exception {

        // to temporarily hold the new count grid
        int[][] countGridToUpdate = new int[boardSize.x][boardSize.y];

        // update count grid related to each ship length //
        this.destroyerCountGrid = updateSpecificCountGrid(Constants.DESTROYER_SIZE, code);
        this.submarineCountGrid = updateSpecificCountGrid(Constants.SUBMARINE_SIZE, code);
        this.cruiserCountGrid = updateSpecificCountGrid(Constants.CRUISER_SIZE, code);
        this.battleshipCountGrid = updateSpecificCountGrid(Constants.BATTLESHIP_SIZE, code);
        this.carrierCountGrid = updateSpecificCountGrid(Constants.CARRIER_SIZE, code);

        // add them together to get the new final probabilistic grid

        for (int i = 0; i < Constants.DESTROYER_NB - this.nbDestroyerDestroyed; i++) {
            countGridToUpdate = GridHelper.add(countGridToUpdate, this.destroyerCountGrid);
        }

        for (int i = 0; i < Constants.SUBMARINE_NB - this.nbSubmarineDestroyed; i++) {
            countGridToUpdate = GridHelper.add(countGridToUpdate, this.submarineCountGrid);
        }

        for (int i = 0; i < Constants.CRUISER_NB - this.nbCruiserDestroyed; i++) {
            countGridToUpdate = GridHelper.add(countGridToUpdate, this.cruiserCountGrid);
        }

        for (int i = 0; i < Constants.BATTLESHIP_NB - this.nbBattleshipDestroyed; i++) {
            countGridToUpdate = GridHelper.add(countGridToUpdate, this.battleshipCountGrid);
        }

        for (int i = 0; i < Constants.CARRIER_NB - this.nbCarrierDestroyed; i++) {
            countGridToUpdate = GridHelper.add(countGridToUpdate, this.carrierCountGrid);
        }

        for (int i = 0; i < this.toExclude.size(); i++) {
            countGridToUpdate[this.toExclude.get(i).x][this.toExclude.get(i).y] = 0;
        }

        System.out.println("grid created");
        this.countGrid = countGridToUpdate;
    }


    /**
     * update a specific grount grid using the new information (or just initialize)
     *
     * @param shipSize ship size
     * @param code     code attack result
     * @return updated grid
     * @throws Exception thorws exception if the ship length is bigger then the board size
     */
    private int[][] updateSpecificCountGrid(int shipSize, int code) throws Exception {

        int[][] countGridX = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];
        int[][] countGridY = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];

        if (shipSize > boardSize.x || shipSize > boardSize.y) {
            throw new Exception("shipLength cannot be bigger x or y");
        }

        // vertical ship position count //

        // compute weight on first column
        for (int i = 0; i < boardSize.x; i++) { // for every column

            for (int j = 0; j < boardSize.y - shipSize + 1; j++) { // start position

                boolean impossiblePosition = false;

                for (int k = 0; k < shipSize; k++) { // loop over shipLength
                    for (int l = 0; l < missToExclude.size(); l++) { // loop over known coordinates
                        if (missToExclude.get(l).x == i && missToExclude.get(l).y == j + k) {
                            impossiblePosition = true;
                            break;
                        }
                    }
                }

                if (!impossiblePosition) { // add possible ship position to count grid
                    for (int k = 0; k < shipSize; k++) { // loop over shipLength
                        countGridX[i][j + k] += 1;
                    }
                }

            }
        }

        // horizontal ship position count //

        for (int i = 0; i < boardSize.y; i++) { // for every row

            for (int j = 0; j < boardSize.x - shipSize + 1; j++) { // start position

                boolean impossiblePosition = false;

                for (int k = 0; k < shipSize; k++) { // loop over shipLength
                    for (int l = 0; l < missToExclude.size(); l++) { // loop over known coordinates
                        if (missToExclude.get(l).x == j + k && missToExclude.get(l).y == i) {
                            impossiblePosition = true;
                            break;
                        }
                    }
                }

                if (!impossiblePosition) { // add possible ship position to count grid
                    for (int k = 0; k < shipSize; k++) { // loop over shipLength
                        countGridX[j + k][i] += 1;
                    }
                }
            }
        }

        // add vertical and horizontal count grid //

        int[][] specificCountGrid = new int[this.boardSize.x][this.boardSize.y];

        for (int i = 0; i < boardSize.x; i++) {
            for (int j = 0; j < boardSize.y; j++) {
                specificCountGrid[i][j] = countGridX[i][j] + countGridY[i][j];
            }
        }

        return specificCountGrid;
    }


    /**
     * check if x and y coordinate are in bound
     *
     * @param x x Parameter
     * @param y y Parameter
     * @return boolean bound or not
     */
    public boolean inBound(int x, int y) {

        if (x < 0 || x >= boardSize.x || y < 0 || y >= boardSize.y) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * get the next move from the AI player
     *
     * @return the coordinate of next move
     */
    public Coordinate getNextMove() {

        if (targetMode && previousTargetSalvation == null) {

            Coordinate bestNextTarget;

            if (!fixedDirection) { // direction is not known

                int maxCount = 0;
                bestNextTarget = this.previousTarget;

                // find the best target around the last target //

                if (inBound(this.axis.y + 1, this.axis.x)) {
                    maxCount = countGrid[this.axis.y + 1][this.axis.x];
                    bestNextTarget = new Coordinate(this.axis.x, this.axis.y + 1);
                    direction = 0; // north by default
                }

                if (inBound(this.axis.y - 1, this.axis.x)) {
                    if (countGrid[this.axis.y - 1][this.axis.x] > maxCount) {
                        maxCount = countGrid[this.axis.y - 1][this.axis.x];
                        bestNextTarget = new Coordinate(this.axis.x, this.axis.y - 1);
                        direction = 2;
                    }
                }

                if (inBound(this.axis.y, this.axis.x + 1)) { // try east
                    if (countGrid[this.axis.y][this.axis.x + 1] > maxCount) {
                        maxCount = countGrid[this.axis.y][this.axis.x + 1];
                        bestNextTarget = new Coordinate(this.axis.x + 1, this.axis.y);
                        direction = 1;
                    }
                }

                if (inBound(this.axis.y, this.axis.x - 1)) { // try west
                    if (countGrid[this.axis.y][this.axis.x - 1] > maxCount) {
                        maxCount = countGrid[this.axis.y][this.axis.x - 1];
                        bestNextTarget = new Coordinate(this.axis.x - 1, this.axis.y);
                        direction = 3;
                    }
                }

                seekAgain = false;

            } else { // direction is known

                bestNextTarget = new Coordinate(this.axis.x, this.axis.y);

                if (direction == 0) { // north
                    if (inBound(this.axis.y + distanceFromHit, this.axis.x)) {
                        bestNextTarget = new Coordinate(this.axis.x, this.axis.y + distanceFromHit);
                    } else { // else go south
                        distanceFromHit = 1;
                        bestNextTarget = new Coordinate(this.axis.x, this.axis.y - distanceFromHit);
                        direction = 2;
                    }

                } else if (direction == 1) { // east
                    if (inBound(this.axis.y, this.axis.x + distanceFromHit)) {
                        bestNextTarget = new Coordinate(this.axis.x + distanceFromHit, this.axis.y);
                    } else { // else go west
                        distanceFromHit = 1;
                        bestNextTarget = new Coordinate(this.axis.x - distanceFromHit, this.axis.y);
                        direction = 3;
                    }

                } else if (direction == 2) { // south
                    if (inBound(this.axis.y - distanceFromHit, this.axis.x)) {
                        bestNextTarget = new Coordinate(this.axis.x, this.axis.y - distanceFromHit);
                    } else { // else go north
                        distanceFromHit = 1;
                        bestNextTarget = new Coordinate(this.axis.x, this.axis.y + distanceFromHit);
                        direction = 0;
                    }

                } else if (direction == 3) { // west
                    if (inBound(this.axis.y, this.axis.x - distanceFromHit)) {
                        bestNextTarget = new Coordinate(this.axis.x - distanceFromHit, this.axis.y);
                    } else { // else go east
                        distanceFromHit = 1;
                        bestNextTarget = new Coordinate(this.axis.x + distanceFromHit, this.axis.y);
                        direction = 1;
                    }
                }
            }

            this.previousTarget = bestNextTarget;

        } else {
            this.previousTarget = getGlobalHighestCount();
        }

        toExclude.add(new Coordinate(this.previousTarget.y, this.previousTarget.x));

		this.countGrid[this.previousTarget.y][this.previousTarget.x] = 0;

        return this.previousTarget;
    }


    private Coordinate getGlobalHighestCount() {

        Coordinate currentCoordinate = new Coordinate(0, 0);
        float currentHighest = 0;
        for (int i = 0; i < Constants.BOARD_SIZE.x; i++) {

            for (int j = 0; j < Constants.BOARD_SIZE.y; j++) {

                float value = countGrid[i][j];

                if (value > currentHighest) {
                    currentHighest = value;
                    currentCoordinate.x = j;
                    currentCoordinate.y = i;
                }
            }
        }

        return currentCoordinate;
    }


    /**
     * display the content of the AI count grid in the console
     */
    public void printCountGrid() {

        System.out.println("AI count grid :");
        for (int i = this.boardSize.x - 1; i >= 0; i--) {
            for (int j = 0; j < this.boardSize.y; j++) {
                System.out.print(this.countGrid[i][j] + " ");
            }
            System.out.println("");
        }
    }


    /**
     * register the result of the previous move
     *
     * @param code AI accepts code as input
     * @throws Exception throws exception
     */
    public void receiveResult(int code) throws Exception {

        if (code == 0) { // change direction to seek ship position

            if (targetMode && fixedDirection) {

                if (direction == 0) { // if north, then go south

                    direction = 2;
                    distanceFromHit = 1;

                } else if (direction == 1) { // if east, then go west

                    direction = 3;
                    distanceFromHit = 1;

                } else if (direction == 2) { // if south, then go north

                    direction = 1;
                    distanceFromHit = 1;

                } else if (direction == 3) { // if west, then go east

                    direction = 1;
                    distanceFromHit = 1;
                }

            } else {
                missToExclude.add(new Coordinate(this.previousTarget.y, this.previousTarget.x));
            }

        } else if (code == 1) { // hit

            if (targetMode) {

                distanceFromHit += 1;
                fixedDirection = true;

            } else { // enable target mode

                targetMode = true;
                axis = this.previousTarget;
            }

        } else if (code == 2) { // sunk

            targetMode = false;
            direction = -1;
            fixedDirection = false;
            distanceFromHit = 1;
        }

        updateCountGrid(code);
    }


    /**
     * get the next five move from the AI player
     *
     * @param sunkNumber
     * @return
     */
    public ArrayList<Coordinate> getNextMoveSalvation(int sunkNumber) {

        ArrayList<Coordinate> coordinateList = new ArrayList<Coordinate>();

        for (int i = 0; i < 5 - sunkNumber; i++) {
            coordinateList.add(this.getNextMove());
        }

        this.previousTargetSalvation = coordinateList;

        return coordinateList;
    }

    /**
     * receive result from shots in salvation mode
     *
     * @return the coordinate list of next move
     */
    public void receiveResultSalvation(ArrayList<Integer> codeList) throws Exception {

        for (int i = 0; i < codeList.size(); i++) {
            this.previousTarget = this.previousTargetSalvation.get(i);
            this.receiveResult(codeList.get(i));
        }
    }

}
