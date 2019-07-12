package model;
import tools.Coordinate;
import constants.Constants;

public class Board {

    Coordinate boardSize;

    boolean gameHasStarted = false;

    Ship[][] waterGrid;
    int[][] waterGridState;

    Ship[] ships;


    /**
     * argument constructor
     */
    public Board(){

        this.boardSize = Constants.BOARD_SIZE;

        this.waterGrid = new Ship[this.boardSize.x][this.boardSize.y];
        this.waterGridState = new int[this.boardSize.x][this.boardSize.y];

        int nbShips = Constants.DESTROYER_NB + Constants.SUBMARINE_NB + Constants.CRUISER_NB + Constants.BATTLESHIP_NB + Constants.CARRIER_NB;
        this.ships = new Ship[ nbShips ];

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
     * @param newShipCoordinate
     * @throws Exception
     */
    public void placeShip(Coordinate[] newShipCoordinate) throws Exception {

        if( gameHasStarted ){
            throw new Exception("Can't move the ships after the beginning of the game.");
        }

        if( doShipCollide(newShipCoordinate) ){
            throw new Exception("New ship coordinate collides with another ship.");
        }

        // create a new ship
        Ship newShip = new Ship(newShipCoordinate.length, newShipCoordinate);

        // add ship reference to the grid
        for(int i=0; i<newShipCoordinate.length; i++){
            waterGrid[ newShipCoordinate[i].x ][ newShipCoordinate[i].y ] = newShip;
        }

        // add ship to ship index
        ships[ships.length-1] = newShip;
    }


    /**
     * Allow human to modify the placement of a ship on the board
     * @param shipIndex
     * @param newShipCoordinate
     * @throws Exception
     */
    public void modifyShipPlace(int shipIndex, Coordinate[] newShipCoordinate) throws Exception {

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
            waterGrid[ shipCoordinate[i].x ][ shipCoordinate[i].y ] = null;
        }

        // loop over new coordinate to add the new ship to the board
        for(int i=0; i<newShipCoordinate.length; i++){
            waterGrid[ newShipCoordinate[i].x ][ newShipCoordinate[i].y ] = ships[shipIndex];
        }

    }


    /**
     * check for collision between the provided coordinates and the board
     * @param newShipCoordinate
     * @return
     */
    private boolean doShipCollide( Coordinate[] newShipCoordinate ) throws Exception {

        for(int i=0; i<newShipCoordinate.length; i++) {

            if (newShipCoordinate[i].x < 0 || newShipCoordinate[i].x > boardSize.x
                    || newShipCoordinate[i].y < 0 || newShipCoordinate[i].y > boardSize.y) {
                throw new Exception("Ship coordinate outside the grid.");
            }

            if ( waterGrid[newShipCoordinate[i].x][newShipCoordinate[i].y] != null ) {
                return true;
            }

        }

        return false;
    }


    /**
     * process a fire on the board and respond accordingly
     * @param target
     * @return
     * @throws Exception
     */
    public int fireAtTarget(Coordinate target) throws Exception{

        if( !gameHasStarted ){ throw new Exception("Ships should be placed before playing."); }

        // That's a miss
        if( waterGrid[target.x][target.y] == null ) {

            waterGridState[ target.x ][ target.y ] = 1;
            return 0;

        // that's a hit !
        } else if( waterGrid[target.x][target.y].isHit(target) ){

            if( waterGrid[target.x][target.y].isSunk() ){ // ship has sunked

                waterGridState[ target.x ][ target.y ] = 2;
                return 2;

            } else {

                waterGridState[ target.x ][ target.y ] = 2;
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

        for(int i=0; i<this.boardSize.x; i++){
            for(int j=0; j<this.boardSize.y; j++){
                System.out.print(waterGridState[i][j]);
            }
            System.out.println();
        }
    }

}
