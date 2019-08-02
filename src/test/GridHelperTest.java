package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import model.Score;
import tools.GridHelper;

/**
 * The following class performs unit tests on Grid class.
 * 
 */
public class GridHelperTest {

	private GridHelper gridHelper;
	private float[][] a = {{2}};
	private float[][] b = {{2}};
	private float[][] resultAdd;
	
	private int[][] c = {{1}};
	private int[][] d = {{2}};
	private int[][] resultAddInt;
	
	private float[][] resultMean;


	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		gridHelper = new GridHelper();
	
	}

	/**
	 * The following method test the add method.
	 */
	@Test
	public void addTest() {
		try {
			resultAdd = gridHelper.add(a, b);
			assertEquals(resultAdd[0][0], 4.0, 0.0);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * The following method test the add method.
	 */
	@Test
	public void addIntTest() {
		try {
			resultAddInt = gridHelper.add(c, d);
			assertEquals(resultAddInt[0][0], 3);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * The following method test the mean method.
	 */
	@Test
	public void meanTest() {
		try {
			resultMean = gridHelper.mean(a, b);
			assertEquals(resultMean[0][0], 2.0, 0.0);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}