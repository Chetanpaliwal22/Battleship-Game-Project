package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import constants.Constants;
import model.AI;
import model.Board;
import tools.Coordinate;
import view.MainWindow;

/**
 * The following class performs unit tests on Board class.
 * 
 */
public class BoardTest {

	private Board board;
	private Boolean resultCheckPlayerSunk;
	private int[][] waterGridState;
	private Coordinate boardSize;
	private int resultWaterGrid[][] = {{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0,0}};
	

	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		board = new Board();
		boardSize = Constants.BOARD_SIZE;
		waterGridState = new int[boardSize.x][boardSize.y];
		this.boardSize = Constants.BOARD_SIZE;
	}

	/**
	 * The following method check number of sunk ship.
	 */
	@Test
	public void checkPlayerSunkShipsTest() {
		resultCheckPlayerSunk = board.checkPlayerSunkShips();
		assertEquals(false, resultCheckPlayerSunk);
	}

	@Test
	public void getBoardStateTest() {
		waterGridState = board.getBoardState();
		assertEquals(resultWaterGrid,waterGridState);		
	}

	

}