package model;

import tools.Coordinate;
import java.util.ArrayList;

public class Ship {

    private ArrayList<Coordinate> position = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> hitPosition = new ArrayList<Coordinate>();

    private boolean isAlive = true;

    public int size;


    /**
     * argument constructor
     *
     * @param size accepts size as input 
     * @param position accepts position as input
     */
    public Ship(int size, Coordinate[] position) {

        for (int i = 0; i < position.length; i++) {
            this.position.add(position[i]);
        }

        this.size = size;
    }

    /**
     * get list of coordinate of the ship
     *
     * @return arraylist of ship coordinate
     */
    public ArrayList<Coordinate> getPosition() { return position; }


    /**
     * get size of the ship
     *
     * @return int size of the ship
     */
    public int getSize() { return size; }


    /**
     * check if ship has sunk
     *
     * @return boolean if ship is sunk
     */
    public boolean isSunk() {

        if (!isAlive) {

            return true;

        } else { return false; }
    }


    /**
     * check if provided position is a hit
     *
     * @param target of the hit ship
     * @return boolean if it is a hit
     */
    public boolean isHit(Coordinate target) {

        if (isAlive) {

            for (int i = 0; i < this.position.size(); i++) {

                if (this.position.get(i).equals(target)) {

                    hitPosition.add(this.position.get(i));

                    if (hitPosition.size() == position.size()) { isAlive = false; }

                    return true;
                }
            }
        }

        return false;
    }


    /**
     * check if position already received a hit
     *
     * @param target accepts target coordinate as input
     * @return boolean if already hit
     */
    public boolean isAlreadyHit(Coordinate target) {

        if (!isAlive) { return true; }

        for (int i = 0; i < hitPosition.size(); i++) {
            if (hitPosition.get(i).equals(target)) {
                return true;
            }
        }

        return false;
    }
}
