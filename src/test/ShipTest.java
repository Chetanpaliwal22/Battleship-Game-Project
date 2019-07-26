package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import model.AI;
import model.Board;
import model.Ship;
import tools.Coordinate;
import view.MainWindow;

/**
 * The following class performs unit tests on Ship class.
 * 
 */
public class ShipTest {

	private Ship ship;
	private boolean resultisSunk;
	private boolean resultisHit;
	private Coordinate target;
		

	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		Coordinate[] ship1Coordinate = {new Coordinate(4, 1), new Coordinate(4, 2)};
		target = new Coordinate(4,1);
		ship = new Ship(2,ship1Coordinate);
	}

	/**
	 * The following method test if ship is sunk.
	 */
	@Test
	public void isSunkTest() {
		resultisSunk = ship.isSunk();
		assertEquals(false, resultisSunk);	
	}	
	
	/**
	 * The following method test if ship is hit.
	 */
	@Test
	public void isHitTest() {
		resultisHit = ship.isHit(target);
		assertEquals(true, resultisHit);	
	}	

}