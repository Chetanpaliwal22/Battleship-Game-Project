package test;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import model.AI;
import tools.Coordinate;
import view.MainWindow;

/**
 * The following class performs unit tests on AI class.
 
 */
public class AITest {
	
	private AI ai;
	private int code;
	private ArrayList<Coordinate> coordinateList;
	private int[][] copyCountGrid;
	private int[][] countGrid;
	MainWindow mainWindow;
	
	
	
	
	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		mainWindow = new MainWindow();
		ai = new AI();
		code =0;
		coordinateList = new ArrayList<Coordinate>();
		//countGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];
		//copyCountGrid = new int[Constants.BOARD_SIZE.x][Constants.BOARD_SIZE.y];
	}

	/**
	 * The following method test the next five move from the AI.
	 */
	@Test
	public void getFiveNextMoveTest()
	{
		//coordinateList = ai.getFiveNextMove();
		System.out.println(coordinateList);
		
		//assertEquals(countryName, "countryThree");
		//assertEquals(c.getCountryName(), "countryThree");
	}
	
	
}