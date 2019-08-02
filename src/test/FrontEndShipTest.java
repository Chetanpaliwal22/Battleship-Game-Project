package test;

import constants.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FrontEndShipTest {

    // The coordinates based on the window
    public double x = 0, y = 0;

    // Direction the ship faces 1: Right 2: Down 3: Left 4: Up
    public int direction = 0;

    public int size;

    // The occupied grids of this ship
    public List<Integer> occupiedGridX = new ArrayList<Integer>(), occupiedGridY = new ArrayList<Integer>();

    // The pivot of the ship, also known as the reference point
    public int pivotGridX = 0, pivotGridY = 0;

    public boolean validity = true;

    public boolean sunk = false;


    // A list contains all the five ships
    public static List<FrontEndShipTest> shipList;

    private int shipSize, shipDirection, shipPivotGridX, shipPivotGridY;


    @Test
    public void validShipsTest() {
        shipList = new ArrayList<FrontEndShipTest>();

        shipSize = 2;
        shipDirection = 2;
        shipPivotGridX = 6;
        shipPivotGridY = 1;

        shipList.add(new FrontEndShipTest());


        shipSize = 3;
        shipDirection = 3;
        shipPivotGridX = 2;
        shipPivotGridY = 3;

        shipList.add(new FrontEndShipTest());

        assertEquals(true, validateLocation());
    }

    @Test
    public void outsideTheBoardTest() {
        shipList = new ArrayList<FrontEndShipTest>();

        shipSize = 2;
        shipDirection = 2;
        shipPivotGridX = 0;
        shipPivotGridY = 0;

        shipList.add(new FrontEndShipTest());

        assertEquals(false, !validateLocation());
    }

    @Test
    public void overlappingShipsTest() {
        shipList = new ArrayList<FrontEndShipTest>();

        shipSize = 2;
        shipDirection = 1;
        shipPivotGridX = 5;
        shipPivotGridY = 5;

        shipList.add(new FrontEndShipTest());


        shipSize = 3;
        shipDirection = 2;
        shipPivotGridX = 5;
        shipPivotGridY = 6;

        shipList.add(new FrontEndShipTest());

        assertEquals(false, !validateLocation());
    }

    @Test
    public void touchingShipsTest() {
        shipList = new ArrayList<FrontEndShipTest>();

        shipSize = 2;
        shipDirection = 1;
        shipPivotGridX = 5;
        shipPivotGridY = 5;

        shipList.add(new FrontEndShipTest());


        shipSize = 3;
        shipDirection = 1;
        shipPivotGridX = 4;
        shipPivotGridY = 5;

        shipList.add(new FrontEndShipTest());

        assertEquals(true, validateLocation());
    }

    // Constructor for initializing the attributes
    public FrontEndShipTest() {

        size = shipSize;

        recalculate(shipDirection, shipPivotGridX, shipPivotGridY);
    }

    // Every time the direction or location of the ship changed, recalculate its occupied grids
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

    // Check if the ship is outside the board
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
        for (int i = 0; i < shipList.size(); i++) {
            if (shipList.get(i) != this) {
                // Compare the coordinates of this ship to other ships' coordinates
                for (int j = 0; j < occupiedGridX.size(); j++) {
                    for (int gridIndex = 0; gridIndex < shipList.get(i).occupiedGridX.size(); gridIndex++) {
                        // Check if they have the same X and Y
                        if (occupiedGridX.get(j) == shipList.get(i).occupiedGridX.get(gridIndex)
                                & occupiedGridY.get(j) == shipList.get(i).occupiedGridY.get(gridIndex)) {
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