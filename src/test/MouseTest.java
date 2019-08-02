package test;

import static org.junit.Assert.assertEquals;

import java.awt.event.MouseEvent;

import org.junit.Before;
import org.junit.Test;

import controller.Mouse;
import model.Score;

/**
 * The following class performs unit tests on Mouse class.
 * 
 */
public class MouseTest {

	private Mouse mouse;
	
	/**
	 * The following method sets up the testing context for the unit tests.
	 */
	@Before
	public void setUp() {
		mouse = new Mouse();
	}

	/**
	 * The following method test the mouse pressed.
	 */
	@Test
	public void mousePressedTest() {
		assertEquals(false, Mouse.leftClicked);
	}
	
	/**
	 * The following method test the mouse clicked.
	 */
	@Test
	public void mouseClickedTest() {
		assertEquals(false, Mouse.leftClicked);
	}
	
	/**
	 * The following method test the mouse released.
	 */
	@Test
	public void mouseReleasedTest() {
		assertEquals(false, Mouse.leftClicked);
	}
	
}