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

    boolean shipsArePlaced = false;

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
    public void Board(Coordinate boardSize,
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
     * Allow the AI or human player to place the ships on the board
     * WIP
     */
    public void placeShips() throws Exception {

        if( shipsArePlaced ){ throw new Exception("Ships are already placed on the board."); }

//        for(int i=0; i<destroyerNb; i++){
//            ships[ships.length] = new Ship();
//        }
//
//        for(int i=0; i<submarineNb; i++){
//            ships[ships.length] = new Ship();
//        }
//
//        for(int i=0; i<cruiserNb; i++){
//            ships[ships.length] = new Ship();
//        }
//
//        for(int i=0; i<battleshipNb; i++){
//            ships[ships.length] = new Ship();
//        }
//
//        for(int i=0; i<carrierNb; i++){
//            ships[ships.length] = new Ship();
//        }

        shipsArePlaced = true;
    }


    /**
     * process a fire on the board and respond accordingly
     * @param target
     * @return
     * @throws Exception
     */
    public int fireAtTarget(Coordinate target) throws Exception{

        if( !shipsArePlaced ){ throw new Exception("Ships should be placed before playing."); }

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
