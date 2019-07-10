package model;
import tools.Coordinate;
import view.BattleShip;

import java.util.ArrayList;

public class Board {

    Coordinate boardSize;

    int destroyerNb;
    int submarineNb;
    int cruiserNb;
    int battleshipNb;
    int carrierNb;

    int destroyerSize;
    int submarineSize;
    int cruiserSize;
    int battleshipSize;
    int carrierSize;

    boolean gameHasStarted = false;

    Ship[][] waterGrid;

    Ship[] ships;


    /**
     * argument constructor
     * @param boardSize
     * @param destroyerNb
     * @param submarineNb
     * @param cruiserNb
     * @param battleshipNb
     * @param carrierNb
     * @param destroyerSize
     * @param submarineSize
     * @param cruiserSize
     * @param battleshipSize
     * @param carrierSize
     */
    public Board(Coordinate boardSize,
        int destroyerNb, int submarineNb, int cruiserNb, int battleshipNb, int carrierNb,
        int destroyerSize, int submarineSize, int cruiserSize, int battleshipSize, int carrierSize){

        this.boardSize = boardSize;

        this.destroyerNb = destroyerNb;
        this.submarineNb = submarineNb;
        this.cruiserNb = cruiserNb;
        this.battleshipNb = battleshipNb;
        this.carrierNb = carrierNb;

        this.destroyerSize = destroyerSize;
        this.submarineSize = submarineSize;
        this.cruiserSize = cruiserSize;
        this.battleshipSize = battleshipSize;
        this.carrierSize = carrierSize;

        this.waterGrid = new Ship[this.boardSize.x][this.boardSize.y];

        this.ships = new Ship[ destroyerNb + submarineNb + cruiserNb + battleshipNb + carrierNb ];

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

        //ships = { new Ship(), new Ship(), new Ship(), new Ship(), new Ship() }
    }


    /**
     * Allow the AI or human player to place the ships on the board
     * WIP
     */
    public void placeShip(Coordinate[] shipCoordinate) throws Exception {

        if( gameHasStarted ){ throw new Exception("Can't move the ships after the beginning of the game."); }



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

            return 2;

        // Coordinate has already been fired at
        } else if( waterGrid[target.x][target.y].isAlreadyHit(target) ){

            return 0;

        // that's a hit !
        } else if( waterGrid[target.x][target.y].isHit(target) ){

            if( waterGrid[target.x][target.y].isSunk() ){ // ship has sunked

                return 3;

            } else {

                return 1;
            }

        // invalid coordinate
        } else { throw new Exception("Invalid coordinate provided as target."); }

    }




    // The following methods needed to correct
    // The following methods needed to correct






















}
