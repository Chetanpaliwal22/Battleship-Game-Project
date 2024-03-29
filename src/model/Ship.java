package model;
import tools.Coordinate;

import java.util.ArrayList;


/**
 * a class to handle the logic of a ship
 */
public class Ship {

    private ArrayList<Coordinate> position = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> hitPosition = new ArrayList<Coordinate>();
    public int size;
    private boolean isAlive = true;

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

    public ArrayList<Coordinate> getHitPosition() {
        return hitPosition;
    }

    public void setHitPosition(ArrayList<Coordinate> targetHitPosition) {
        hitPosition = targetHitPosition;
    }

    public void setIsLive(boolean liveState) {
        isAlive = liveState;
    }

    /**
     * get size of the ship
     *
     * @return size of the ship
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


    public boolean getIsAlive() {
        return isAlive;
    }


    /**
     * check if provided position is a hit
     *
     * @param target cooridnate of the position
     * @return boolean 
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
    * @param target of the hit ship
     * @return boolean if it is a hit
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
