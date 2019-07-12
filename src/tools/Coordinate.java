package tools;

public class Coordinate {

	public int x;
	public int y;

	/**
	 * constructor
	 * @param a for the x coordinate 
	 * @param b for the y coordinate 
	 */
	public Coordinate(int a, int b){
		x =a;
		y= b;
	}

	/**
	 * override equals method
	 * @param other
	 * @return
	 */
	public boolean equals(Coordinate other){
		if( other.x == x && other.y == y ){
			return true;
		} else {
			return false;
		}
	}
}
