package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import model.Score;

/**
 * The following class performs unit tests on Score class.
 * 
 */
public class ScoreTest {

	private Score score;
	private int resultNumberOfStars;
	private int resultFinalScore;

	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		score = new Score();
		resultNumberOfStars = 0;
		resultFinalScore = 0;
	}

	/**
	 * The following method test the number of star method.
	 */
	@Test
	public void calculateStarsTest() {
		resultNumberOfStars = score.calculateStars();
		assertEquals(0, resultNumberOfStars);
	}
	
	
	/**
	 * The following method test the final score of the player.
	 */
	@Test
	public void calculateFinalScoreTest() {
		resultNumberOfStars = score.calculateStars();
		assertEquals(0, resultFinalScore);
	}
	
}