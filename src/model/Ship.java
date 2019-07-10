package model;
import tools.Coordinate;
import view.BattleShip;

import java.util.ArrayList;
import java.util.List;

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




    // The coordinates based on the window
    public double x = 0, y = 0;

    public int length = 0;

    /*
     * Direction the ship faces 1: Right 2: Down 3: Left 4: Up
     */
    public int direction = 0;

    // The occupied grids of this ship
    public List<Integer> occupiedGridX = new ArrayList<Integer>(), occupiedGridY = new ArrayList<Integer>();

    // The pivot of the ship, also known as the reference point
    public int pivotGridX = 0, pivotGridY = 0;

    public boolean validity = true;

    // Constructor for initializing the attributes
    public Ship(int shipLength, int shipDirection, int shipPivotGridX, int shipPivotGridY) {
        length = shipLength;

        recalculate(shipDirection, shipPivotGridX, shipPivotGridY);
    }

    // Every time the direction or location of the ship changed, recalculate its
    // occupied grids
    public void recalculate(int shipDirection, int shipPivotGridX, int shipPivotGridY) {

        direction = shipDirection;

        pivotGridX = shipPivotGridX;
        pivotGridY = shipPivotGridY;

        // Reset the occupied grids
        occupiedGridX = new ArrayList<Integer>();
        occupiedGridY = new ArrayList<Integer>();

        if (length == 2) {
            // Check the ship direction
            if (direction == 1) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 1);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 2) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 1);
            } else if (direction == 3) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 1);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 4) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 1);
            }
        } else if (length == 3) {
            // Check the ship direction
            if (direction == 1 | direction == 3) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 1);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 2 | direction == 4) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 1);
            }
        } else if (length == 4) {
            // Check the ship direction
            if (direction == 1) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 2);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 2) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 2);
            } else if (direction == 3) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 2);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 4) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 2);
            }
        } else if (length == 5) {
            // Check the ship direction
            if (direction == 1 | direction == 3) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX + 2);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 1);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX - 2);
                occupiedGridY.add(shipPivotGridY);
            } else if (direction == 2 | direction == 4) {
                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY + 2);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 1);

                occupiedGridX.add(shipPivotGridX);
                occupiedGridY.add(shipPivotGridY - 2);
            }
        }
    }

    // Check if the ship is outside the board
    public boolean validateLocation() {
        for (int i = 0; i < occupiedGridX.size(); i++) {
            if (occupiedGridX.get(i) < 0 | occupiedGridX.get(i) > 10) {
                validity = false;
                return false;

            }
        }

        for (int i = 0; i < occupiedGridY.size(); i++) {
            if (occupiedGridY.get(i) < 0 | occupiedGridY.get(i) > 8) {
                validity = false;
                return false;

            }
        }

        // Check the collision
        for (int i = 0; i < BattleShip.shipList.size(); i++) {
            if (BattleShip.shipList.get(i) != this) {
                // Compare the coordinates of this ship to other ships' coordinates
                for (int j = 0; j < occupiedGridX.size(); j++) {
                    for (int gridIndex = 0; gridIndex < BattleShip.shipList.get(i).occupiedGridX.size(); gridIndex++) {
                        // Check if they have the same X and Y
                        if (occupiedGridX.get(j) == BattleShip.shipList.get(i).occupiedGridX.get(gridIndex)
                                & occupiedGridY.get(j) == BattleShip.shipList.get(i).occupiedGridY.get(gridIndex)) {
                            validity = false;
                            return false;
                        }
                    }

                }
            }
        }

        validity = true;

        return true;
    }
}
