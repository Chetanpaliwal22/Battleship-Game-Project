package model;

import tools.Coordinate;

public class Brain {
	
	public Coordinate getNextMove(float[][] doubles, int row, int col) {
		Coordinate currentCoordinate = new Coordinate(0, 0);
		float currentHighest = 0;
	    
	    for (int i = 0; i < row; row++) {
	        for (int j = 0; j < col; col++) {
	        	float value = doubles[i][j];
	            if (value > currentHighest) {
	            	currentHighest = value;
	            	currentCoordinate.x = i;
	            	currentCoordinate.y = j;
	            }
	        }
	    }
	    return currentCoordinate; 
	}
}
