package tools;

/**
 * This util class contains the description of the cooridate x and y used across the project
 */
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
	 * @param other for the coordinate
	 * @return boolean
	 */
	public boolean equals(Coordinate other){

		if( other.x == x && other.y == y ){

			return true;

		} else { return false; }
	}
}
