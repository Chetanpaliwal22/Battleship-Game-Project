package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.GameTimer;
import model.Score;

/**
 * The following class performs unit tests on GameTimer class.
 * 
 */
public class GameTimerTest {

	private GameTimer gameTimer;
	private int resultReturnTime;

	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		gameTimer = new GameTimer();
		resultReturnTime =0;
		
	}

	/**
	 * The following method test the return time method.
	 */
	@Test
	public void returnTimeTest() {
		resultReturnTime = gameTimer.returnTime();
		assertEquals(10, resultReturnTime);
	}
}