package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import tools.Coordinate;
import tools.GridHelper;

/**
 * The following class performs unit tests on Coordinate class.
 * 
 */
public class CoordinateTest {

	private Coordinate a;
	private Coordinate b;
	private Coordinate c;
	private Coordinate d;
	private Boolean resultEquals;
	private Boolean resultNotEquals;
private Coordinate coordinate;

	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		coordinate = new Coordinate(1,1);
		a = new Coordinate(1,1);
		b = new Coordinate(2,3);	
		c = new Coordinate(1,1);
		d = new Coordinate(2,3);
	}

	/**
	 * The following method test the equals method.
	 */
	@Test
	public void equalsTest() {
		try {
			resultEquals = coordinate.equals(a);
			assertEquals(resultEquals,true);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * The following method test the equals method.
	 */
	@Test
	public void notEqualsTest() {
		try {
			resultNotEquals = coordinate.equals(b);
			assertEquals(resultNotEquals, false);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * The following method test the equals method.
	 */
	@Test
	public void equalsMethodTest() {
		try {
			resultEquals = coordinate.equals(c);
			assertEquals(resultEquals,true);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * The following method test the equals method.
	 */
	@Test
	public void notEqualsMethodTest() {
		try {
			resultNotEquals = coordinate.equals(d);
			assertEquals(resultNotEquals, false);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}