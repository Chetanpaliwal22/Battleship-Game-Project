package com.tools;

public class Coordinate {

	public int x;
	public int y;

	/**
	 * constructor
	 * @param a
	 * @param b
	 */
	public Coordinate(int a, int b){
		x =a;
		y= b;
	}

	public boolean equals(Coordinate other){
		if( other.x == x && other.y == y ){
			return true;
		} else {
			return false;
		}
	}
}
