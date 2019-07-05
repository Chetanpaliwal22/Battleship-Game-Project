import com.tools.Coordinate;

public class Ship {

    public Coordinate[] position;
    public int size;
    boolean isAlive = true;

    /**
     * constructor
     * @param size
     * @param position
     */
    public void Ship(int size, Coordinate[] position){

        this.position = position;
        this.size = size;

    }

}
