package model;

public class Player {

String type;

Player(String type){
this.type = type;
}

public Coordinate getNextMove(){
	
	if(this.type == "human") {
		
		//Get the move from Board.
		
	}else if(this.type == "AI") {
		
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

public void playNextMove(){

}



}