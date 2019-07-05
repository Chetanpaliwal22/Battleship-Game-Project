import com.tools.Coordinate;

public class Ship {

    private Coordinate[] position;
    private Coordinate[] hitPosition;
    private int size;
    private boolean isAlive = true;

    /**
     * argument constructor
     * @param size
     * @param position
     */
    public void Ship(int size, Coordinate[] position){

        this.position = position;
        this.size = size;

    }

    /**
     * default constructor
     */
    public void Ship(){

        this.position = new Coordinate[]{new Coordinate(0, 0), new Coordinate(0, 1)};
        this.size = 2;

    }


    /**
     * check if ship has sunk
     * @return
     */
    public boolean isSunk(){

        if( !isAlive ){
            return true;
        } else {
            return false;
        }
    }


    /**
     * check if provided position is a hit
     * @param target
     * @return
     */
    public boolean isHit(Coordinate target){

        if( isAlive ){

            for(int i=0; i<position.length; i++){

                if( position[i].equals(target) ){

                    hitPosition[hitPosition.length] = position[i];

                    if( hitPosition.length == position.length ){ isAlive = false; }

                    return true;
                }

            }

            return false;

        } else { return false; }
    }


    /**
     * check if position already received a hit
     * @param target
     * @return
     */
    public boolean isAlreadyHit(Coordinate target){

        if( !isAlive ){ return true; }

        for(int i=0; i<hitPosition.length; i++){
            if( hitPosition[i].equals(target) ){ return true; }
        }

        return false;

    }

}
