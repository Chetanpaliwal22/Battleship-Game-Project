package model;
import tools.Coordinate;
import constants.Constants;

import java.util.ArrayList;

public class Board {

    Coordinate boardSize;

    boolean gameHasStarted = false;

    Ship[][] waterGrid;
    int[][] waterGridState;

    private ArrayList<Ship> ships;


    /**
     * argument constructor
     */
    public Board(){

        this.boardSize = Constants.BOARD_SIZE;

        this.waterGrid = new Ship[this.boardSize.x][this.boardSize.y];
        this.waterGridState = new int[this.boardSize.x][this.boardSize.y];

        int nbShips = Constants.DESTROYER_NB + Constants.SUBMARINE_NB + Constants.CRUISER_NB + Constants.BATTLESHIP_NB + Constants.CARRIER_NB;
        this.ships = new ArrayList<Ship>();
    }

    /**
     * set that the game has started
     */
    public void setGameHasStarted(){
        gameHasStarted = true;
    }

    public void placeShipsRandomly(){

        // hardcoded for now

        Coordinate[] ship1Coordinate = { new Coordinate(4, 1), new Coordinate(4, 2) };
        Coordinate[] ship2Coordinate = { new Coordinate(3, 7), new Coordinate(4, 7), new Coordinate(5, 7) };
        Coordinate[] ship3Coordinate = { new Coordinate(0 ,1), new Coordinate(0, 2), new Coordinate(0, 3) };
        Coordinate[] ship4Coordinate = { new Coordinate(5, 3), new Coordinate(6, 3), new Coordinate(7, 3), new Coordinate(8, 3) };
        Coordinate[] ship5Coordinate = { new Coordinate(2, 0), new Coordinate(2, 1), new Coordinate(2, 2), new Coordinate(2, 3), new Coordinate(2, 4) };


        try {
            placeShip(ship1Coordinate);
            placeShip(ship2Coordinate);
            placeShip(ship3Coordinate);
            placeShip(ship4Coordinate);
            placeShip(ship5Coordinate);

        } catch(Exception e){ System.out.println(e); }
    }


    /**
     * Allow human or AI to place a new ship on the board
     * @param newShipCoordinate - New Ship Coordinate
     * @throws Exception - Throws Error if the ships are moved after the game has started.
     */
    public void placeShip(Coordinate[] newShipCoordinate) throws Exception {

        if( gameHasStarted ){
            throw new Exception("Can't move the ships after the beginning of the game.");
        }

        /*if( doShipCollide(newShipCoordinate) ){
            throw new Exception("New ship coordinate collides with another ship.");
        }*/

        // create a new ship
        Ship newShip = new Ship(newShipCoordinate.length, newShipCoordinate);

        // add ship reference to the grid
        for(int i=0; i<newShipCoordinate.length; i++){
            waterGrid[ newShipCoordinate[i].y ][ newShipCoordinate[i].x ] = newShip;
        }

        // add ship to ship index
        ships.add( newShip ) ;
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

        if( shipIndex < 0 || shipIndex > ships.size() ){
            throw new Exception("Invalid ship index.");
        }

        if( doShipCollide(newShipCoordinate) ){
            throw new Exception("New ship coordinate collides with another ship.");
        }

        // get ship position
        Coordinate[] shipCoordinate = (Coordinate[]) ships.get(shipIndex).getPosition().toArray();

        // loop over previous ship coordinate, and remove
        for(int i=0; i<shipCoordinate.length; i++){
            waterGrid[ shipCoordinate[i].y ][ shipCoordinate[i].x ] = null;
        }

        // loop over new coordinate to add the new ship to the board
        for(int i=0; i<newShipCoordinate.length; i++){
            waterGrid[ newShipCoordinate[i].y ][ newShipCoordinate[i].x ] = ships.get(shipIndex);
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

            if ( waterGrid[newShipCoordinate[i].x][newShipCoordinate[i].y] != null ) {
                return true;
            }

        }

        return false;
    }*/


    /**
     * process a fire on the board and respond accordingly
     * @param target accpt target as prameter
     * @return the int code for result
     * @throws Exception throws exception
     */
    public int fireAtTarget(Coordinate target) throws Exception{

        if( !gameHasStarted ){ throw new Exception("Ships should be placed before playing."); }

        // That's a miss
        if( waterGrid[target.y][target.x] == null ) {

            waterGridState[ target.y ][ target.x ] = 1;
            return 0;

        // that's a hit !
        } else if( waterGrid[target.y][target.x].isHit(target) ){

            if( waterGrid[target.y][target.x].isSunk() ){ // ship has sunk

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
     * @return the board state
     */
    public int[][] getBoardState() {

        return waterGridState;
    }


    /**
     * print the state grid in the console
     */
    public void printStateGrid(){

        System.out.println("State grid :");
        for(int i=this.boardSize.x-1; i>=0; i--){
            for(int j=0; j<this.boardSize.y; j++){
                System.out.print(waterGridState[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * print the ship grid in the console
     */
    public void printShipGrid(){

        System.out.println("Ship grid :");
        for(int i=this.boardSize.x-1; i>=0; i--){
            for(int j=0; j<this.boardSize.y; j++){
                if( waterGrid[i][j] != null){
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }

            }
            System.out.println();
        }

    }
}
