package model;

import constants.Constants;
import tools.Coordinate;
import view.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private ArrayList<Coordinate> position = new ArrayList<Coordinate>();
    private ArrayList<Coordinate> hitPosition = new ArrayList<Coordinate>();
    public int size;
    private boolean isAlive = true;

    /**
     * argument constructor
     *
     * @param size
     * @param position
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
     * @return
     */
    public ArrayList<Coordinate> getPosition() {
        return position;
    }

    /**
     * get size of the ship
     *
     * @return
     */
    public int getSize() {
        return size;
    }


    /**
     * check if ship has sunk
     *
     * @return
     */
    public boolean isSunk() {

        if (!isAlive) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * check if provided position is a hit
     *
     * @param target
     * @return
     */
    public boolean isHit(Coordinate target) {

        if (isAlive) {

            for (int i = 0; i < this.position.size(); i++) {

                if (this.position.get(i).equals(target)) {

                    hitPosition.add(this.position.get(i));

                    if (hitPosition.size() == position.size()) {
                        isAlive = false;
                    }

                    return true;
                }
            }
        }

        return false;
    }


    /**
     * check if position already received a hit
     *
     * @param target
     * @return
     */
    public boolean isAlreadyHit(Coordinate target) {

        if (!isAlive) {
            return true;
        }

        for (int i = 0; i < hitPosition.size(); i++) {
            if (hitPosition.get(i).equals(target)) {
                return true;
            }
        }

        return false;

    }
}
