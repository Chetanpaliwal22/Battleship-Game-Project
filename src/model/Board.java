package model;
import tools.Coordinate;
import constants.Constants;

import java.util.ArrayList;

public class Board {

    Coordinate boardSize;

    boolean gameHasStarted = false;

    Ship[][] waterGrid;
    int[][] waterGridState;

    ArrayList<Ship> ships = new ArrayList<Ship>();


    /**
     * argument constructor
     */
    public Board(){

        this.boardSize = Constants.BOARD_SIZE;

        this.waterGrid = new Ship[this.boardSize.x][this.boardSize.y];
        this.waterGridState = new int[this.boardSize.x][this.boardSize.y];

        int nbShips = Constants.DESTROYER_NB + Constants.SUBMARINE_NB + Constants.CRUISER_NB + Constants.BATTLESHIP_NB + Constants.CARRIER_NB;

    }

    /**
     * set that the game has started
     */
    public void setGameHasStarted(){
        gameHasStarted = true;
    }

    public void placeShipsRandomly(){

        // hardcoded for now

        Ship ship1 = new Ship(2, 2, 6, 1);
        Ship ship2 = new Ship(3, 3, 2, 3);
        Ship ship3 = new Ship(3, 1, 8, 4);
        Ship ship4 = new Ship(4, 3, 2, 7);
        Ship ship5 = new Ship(5, 3, 4, 5);

        try {
            placeShip(ship1);
            placeShip(ship2);
            placeShip(ship3);
            placeShip(ship4);
            placeShip(ship5);

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Allow human or AI to place a new ship on the board
     * @param shipToPlace
     * @throws Exception
     */
    public void placeShip(Ship shipToPlace) throws Exception {

        if( gameHasStarted ){
            throw new Exception("Can't move the ships after the beginning of the game.");
        }

        Coordinate[] shipCoordinate = (Coordinate[]) shipToPlace.getPosition().toArray( new Coordinate[shipToPlace.getPosition().size()] );

        // add ship reference to the grid
        for(int i=0; i<shipCoordinate.length; i++){
            waterGrid[ shipCoordinate[i].y ][ shipCoordinate[i].x ] = shipToPlace;
        }

        // add ship to ship index
        ships.add( shipToPlace );
    }


    /**
     * Allow human to modify the placement of a ship on the board
     * @param shipIndex
     * @param newShipCoordinate
     * @throws Exception
     */
    /*public void modifyShipPlace(int shipIndex, Coordinate[] newShipCoordinate) throws Exception {

        if( gameHasStarted ){
            throw new Exception("Can't move the ships after the beginning of the game.");
        }

        if( shipIndex < 0 || shipIndex > ships.length ){
            throw new Exception("Invalid ship index.");
        }

        if( doShipCollide(newShipCoordinate) ){
            throw new Exception("New ship coordinate collides with another ship.");
        }

        // get ship position
        Coordinate[] shipCoordinate = (Coordinate[]) ships[shipIndex].getPosition().toArray();

        // loop over previous ship coordinate, and remove
        for(int i=0; i<shipCoordinate.length; i++){
            waterGrid[ shipCoordinate[i].y ][ shipCoordinate[i].x ] = null;
        }

        // loop over new coordinate to add the new ship to the board
        for(int i=0; i<newShipCoordinate.length; i++){
            waterGrid[ newShipCoordinate[i].y ][ newShipCoordinate[i].x ] = ships[shipIndex];
        }

    }*/


    /**
     * check for collision between the provided coordinates and the board
     * @param newShipCoordinate
     * @return
     */
    /*private boolean doShipCollide( Coordinate[] newShipCoordinate ) throws Exception {

        for(int i=0; i<newShipCoordinate.length; i++) {

            if (newShipCoordinate[i].y < 0 || newShipCoordinate[i].y > boardSize.y
                    || newShipCoordinate[i].x < 0 || newShipCoordinate[i].y > boardSize.x) {
                throw new Exception("Ship coordinate outside the grid.");
            }

            if ( waterGrid[newShipCoordinate[i].y][newShipCoordinate[i].x] != null ) {
                return true;
            }

        }

        return false;
    }*/


    /**
     * process a fire on the board and respond accordingly
     * @param target
     * @return
     * @throws Exception
     */
    public int fireAtTarget(Coordinate target) throws Exception{

        if( !gameHasStarted ){ throw new Exception("Ships should be placed before playing."); }

        // That's a miss
        if( waterGrid[target.y][target.x] == null ) {

            waterGridState[ target.y ][ target.x ] = 1;
            return 0;

        // that's a hit !
        } else if( waterGrid[target.y][target.x].isHit(target) ){

            if( waterGrid[target.y][target.x].isSunk() ){ // ship has sunked

                waterGridState[ target.y ][ target.x ] = 2;
                return 2;

            } else {

                waterGridState[ target.y ][ target.x ] = 2;
                return 1;
            }

        // invalid coordinate
        } else { throw new Exception("Invalid coordinate provided as target."); }

    }


    /**
     * return the state of the board as a 2 dimensional array of integer
     * WIP
     * @return
     */
    public int[][] getBoardState() {

        return waterGridState;
    }


    /**
     * print the grid in the console
     */
    public void printGrid(){

        for(int i=this.boardSize.x-1; i>=0; i--){
            for(int j=0; j<this.boardSize.y; j++){
                System.out.print(waterGridState[i][j]);
            }
            System.out.println();
        }
    }

}
