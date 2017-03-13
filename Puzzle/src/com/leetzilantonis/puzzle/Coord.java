package com.leetzilantonis.puzzle;

import java.awt.Point;

public class Coord implements Comparable<Object> {

	public int x;
	public int y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord(Point p) {
		this(p.x, p.y);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public void setOrigin() {
		this.x = this.y = 0;
	}
	
	public boolean equals(Coord c) {
		if (c.x == this.x && c.y == this.y) 
			return true;
		return false;
	}
	
	public boolean equalsNum(Coord c) {
		if ((this.x + this.y) == (c.x + c.y))
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + this.x + "," + this.y + "]";
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Coord)) {
			//System.out.println("NOT COORD");
			return -10;
		}
		if (!this.equals((Coord)o)) {
			//System.out.println("NOT EQUAL COORD");
			return this.equalsNum((Coord) o) ? this.x - ((Coord) o).x : (this.x + this.y) - (((Coord) o).x + ((Coord) o).y);
		}
		//System.out.println("MATCHES");
		return 0;
	}
	
}
