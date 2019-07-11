package model;

import tools.Coordinate;

public class Player {

String type;

public Player(String type){
this.type = type;
}

public Coordinate getNextMove(int row, int col,float[][] doubles){
	Coordinate currentCoordinate = new Coordinate(0, 0);
	if(this.type == "human") {
		
		//Get the move from Board.
		
	}else if(this.type == "AI") {
		
		
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
	   
	}
	 return currentCoordinate; 
}

public void playNextMove(){

}



}