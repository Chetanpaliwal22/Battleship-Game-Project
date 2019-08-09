package view;

import constants.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * a class contains the attributes of visualized ship entities
 */
public class FrontEndShip {

    // The coordinates based on the window
    public double x = 0, y = 0;

    // Direction the ship faces 1: Right 2: Down 3: Left 4: Up
    public int direction = 0;

    public int size;

    // The occupied grids of this ship
    public List<Integer> occupiedGridX = new ArrayList<Integer>(), occupiedGridY = new ArrayList<Integer>();

    // The pivot of the ship, also known as the reference point
    public int pivotGridX = 0, pivotGridY = 0;

    public String name = "";

    public boolean validity = true;

    public boolean sunk = false;


    /**
     * every time the direction or location of the ship changed, recalculate its occupied grids
     * @param shipSize ship size	
     * @param shipDirection ship direction
     * @param shipPivotGridX ship pivot x
     * @paran shipPivotGridY ship pivot y
     */
    public FrontEndShip(int shipSize, int shipDirection, int shipPivotGridX, int shipPivotGridY) {

        size = shipSize;

        if (size == 2) {
            name = "Destroyer";
        } else if (size == 3) {
            if (!MainWindow.submarineNamed) {
                name = "Submarine";
                MainWindow.submarineNamed = true;
            } else {
                name = "Cruiser";
            }
        } else if (size == 4) {
            name = "Battleship";
        } else if (size == 5) {
            name = "Carrier";
        }

        recalculate(shipDirection, shipPivotGridX, shipPivotGridY);
    }


    /**
     * constructor for initializing the attributes
     * @param shipDirection shows the ship direction
     * @param shipPivotGridX x pivot
     * @param shipPivotGridY y pivot
     */
    public void recalculate(int shipDirection, int shipPivotGridX, int shipPivotGridY) {

        direction = shipDirection;

        pivotGridX = shipPivotGridX;
        pivotGridY = shipPivotGridY;

        // Reset the occupied grids
        occupiedGridX = new ArrayList<Integer>();
        occupiedGridY = new ArrayList<Integer>();

        if (size == 2) {
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
        } else if (size == 3) {
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
        } else if (size == 4) {
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
        } else if (size == 5) {
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

    /**
     * check if the ship is outside the board or there is overlapping
     * @return boolean retun true if its a valid locaiton
     */
    public boolean validateLocation() {
        for (int i = 0; i < occupiedGridX.size(); i++) {
            if (occupiedGridX.get(i) < 0 | occupiedGridX.get(i) >= Constants.BOARD_SIZE.y) {
                validity = false;
                return false;
            }
        }

        for (int i = 0; i < occupiedGridY.size(); i++) {
            if (occupiedGridY.get(i) < 0 | occupiedGridY.get(i) >= Constants.BOARD_SIZE.x) {
                validity = false;
                return false;
            }
        }

        // Check the collision
        for (int i = 0; i < MainWindow.shipList.size(); i++) {
            if (MainWindow.shipList.get(i) != this) {
                // Compare the coordinates of this ship to other ships' coordinates
                for (int j = 0; j < occupiedGridX.size(); j++) {
                    for (int gridIndex = 0; gridIndex < MainWindow.shipList.get(i).occupiedGridX.size(); gridIndex++) {
                        // Check if they have the same X and Y
                        if (occupiedGridX.get(j) == MainWindow.shipList.get(i).occupiedGridX.get(gridIndex)
                                & occupiedGridY.get(j) == MainWindow.shipList.get(i).occupiedGridY.get(gridIndex)) {
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
