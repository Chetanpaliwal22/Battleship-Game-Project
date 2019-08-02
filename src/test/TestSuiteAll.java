package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tools.GridHelper;

/**
 * Test Suite class to run all the test from model package
 */
@RunWith(Suite.class)
@SuiteClasses({AITest.class, BoardTest.class, ShipTest.class, GameTimerTest.class, ScoreTest.class, MouseTest.class,
        FrontEndShipTest.class, GridHelperTest.class, CoordinateTest.class, RendererTest.class})

public class TestSuiteAll {

}