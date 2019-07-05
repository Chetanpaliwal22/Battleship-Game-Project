import com.tools.Coordinate;

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

    int[][] waterGrid;

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

        this.waterGrid = new int[this.boardSize.x][this.boardSize.y];

    }


    /**
     * Allow the AI or human player to place the ships on the board
     */
    public void placeShips(){

        for(int i=0; i<destroyerNb; i++){
            // place
        }

        for(int i=0; i<submarineNb; i++){
            // place
        }

        for(int i=0; i<cruiserNb; i++){
            // place
        }

        for(int i=0; i<battleshipNb; i++){
            // place
        }

        for(int i=0; i<carrierNb; i++){
            // place
        }

        shipsArePlaced = true;
    }


    /**
     * process a fire on the board and respond accordingly
     * @param target
     * @return
     * @throws Exception
     */
    public int fireAtTarget(Coordinate target) throws Exception{

        // Coordinate has already been fired at
        if( waterGrid[target.x][target.y] == 0 ){

            return 0;

        // that's a hit !
        } else if( waterGrid[target.x][target.y] == 1 ){

            if( this.shipIsSunked() ){ // ship is sunked

                return 3;

            } else {

                return 1;
            }

        // that's a miss ...
        } else if( waterGrid[target.x][target.y] == 2 ){

            return 2;

        // invalid coordinate
        } else {
            throw new Exception("Invalid coordinate provided as target.");
        }

    }

    /**
     * check if one the ship on the board has sunked
     * @return
     */
    private boolean shipIsSunked(){

        // loop over all ship coordinate, return 1 if all coordinate of a given ship
        // were hit

        for(int i=0; i<this.ships.length; i++){
            // check
        }

        return false;

    }

}
