package com.leetzilantonis.puzzle;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Piece {

	private List<Coord> points = new ArrayList<Coord>();
	private int size = 60;
	private Color color = Color.GREEN;
	private Point origin = new Point(60, 60);
	private int index = 0;
	private int orientation = 0;
	private String letter;
	public boolean selected = false;
	public boolean drawn = false;

	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public int width;
	public int height;

	public Piece(List<Coord> points, Coord origin, Color color, int index, String letter) {
		this.points = points;
		this.origin = new Point(origin.x * this.size, origin.y * this.size);
		this.color = color;
		this.index = index;
		this.letter = letter;
		reShape();
	}

	public Piece(Piece p) {
		this(p.points, new Coord(p.origin.x / p.size, p.origin.y / p.size), p.color, p.index, p.letter);
	}

	private void reShape() {
		int minX = 0;
		int minY = 0;
		int maxX = 0;
		int maxY = 0;
		for (Coord p : this.points) {
			minX = p.x < minX ? p.x : minX;
			minY = p.y < minY ? p.y : minY;
			maxX = p.x > maxX ? p.x : maxX;
			maxY = p.y > maxY ? p.y : maxY;
		}
		this.width = maxX - minX;
		this.height = maxY - minY;
	}

	public void draw(Graphics g) {
		if (!this.drawn)
			return;
		if (this.selected) {
			for (Coord p : this.points) {
				Coord o = new Coord(origin.x + (p.x * this.size), origin.y + (p.y * this.size));
				Coord n = this.isNear(new Coord(o.x + this.size, o.y));
				Coord e = this.isNear(new Coord(o.x + this.size + 10, o.y + 10));
				int width = this.size - 10;
				g.setColor(Color.WHITE);
				if (n != null) {
					int x = o.x + (width / 2) - 2;
					int y = o.y - 10;
					g.fillRect(x, y, 14, 20);
				}
				if (e != null) {
					int x = o.x + width;
					int y = o.y + (width / 2) - 2;
					g.fillRect(x, y, 20, 14);
				}
				g.setColor(Color.WHITE);
				g.fillOval(origin.x + (p.x * this.size) + 2, origin.y + (p.y * this.size) + 2, this.size - 4, this.size - 4);
			}
		}
		for (Coord p : this.points) {
			Coord o = new Coord(origin.x + (p.x * this.size), origin.y + (p.y * this.size));
			Coord n = this.isNear(new Coord(o.x + this.size, o.y));
			Coord e = this.isNear(new Coord(o.x + this.size + 10, o.y + 10));
			int width = this.size - 10;
			g.setColor(this.color);
			if (n != null) {
				int x = o.x + (width / 2);
				int y = o.y - 10;
				g.fillRect(x, y, 10, 20);
			}
			if (e != null) {
				int x = o.x + width;
				int y = o.y + (width / 2);
				g.fillRect(x, y, 20, 10);
			}
			g.setColor(this.color);
			g.fillOval(origin.x + (p.x * this.size) + 5, origin.y + (p.y * this.size) + 5, this.size - 10, this.size - 10);
			g.setColor(Color.BLACK);
			g.setFont(g.getFont().deriveFont(20F));
			g.drawString(this.letter, origin.x + (p.x * this.size) + 24, origin.y + (p.y * this.size) + 38);
		}
	}

	public Coord isNear(Coord test) {
		for (Coord point : this.points) {
			if (test.x > this.origin.x + (point.x * this.size)
					&& test.x <= this.origin.x + (point.x * this.size) + this.size) {
				if (this.origin.x + (point.x * this.size) < 0) {
					return null;
				}
				if (test.y > this.origin.y + (point.y * this.size)
						&& test.y <= this.origin.y + (point.y * this.size) + this.size) {
					if (this.origin.y + (point.y * this.size) < 0) {
						return null;
					}
					return point;
				}
			}
		}
		return null;
	}

	public boolean rotate(Coord point) {
		if (this.points.contains(point)) {
			int xa = 0 - point.x;
			int ya = 0 - point.y;
			List<Coord> temp = new ArrayList<Coord>();
			for (Coord p : this.points) {
				Coord t = new Coord((p.y + ya) * -1, p.x + xa);
				temp.add(t);
			}
			this.points = temp;
			this.origin = new Point(this.origin.x + point.x * this.size, this.origin.y + point.y * this.size);
			this.orientation = this.orientation == 3 ? 0 : this.orientation + 1;
		}
		return false;
	}

	public Piece rotate() {
		List<Coord> temp = new ArrayList<Coord>();
		for (Coord p : this.points) {
			Coord t = new Coord(p.y * -1, p.x);
			temp.add(t);
		}
		this.points = temp;
		this.orientation = this.orientation == 3 ? 0 : this.orientation + 1;
		return this;
	}

	public Piece flip(int direction) {
		if (direction == 2) {
			List<Coord> temp = new ArrayList<Coord>();
			for (Coord p : this.points) {
				Coord t = new Coord(p.x, p.y * -1);
				temp.add(t);
			}
			this.points = temp;
		} else {
			List<Coord> temp = new ArrayList<Coord>();
			for (Coord p : this.points) {
				Coord t = new Coord(p.x * -1, p.y);
				temp.add(t);
			}
			this.points = temp;
		}
		return this;
	}

	public boolean adjust(Coord point) {
		if (this.points.contains(point)) {
			int xa = 0 - point.x;
			int ya = 0 - point.y;
			List<Coord> temp = new ArrayList<Coord>();
			for (Coord p : this.points) {
				Coord t = new Coord(p.x + xa, p.y + ya);
				temp.add(t);
			}
			this.points = temp;
			this.origin = new Point(point.x * this.size, point.y * this.size);
		}
		return false;
	}

	public void setOrigin(Point point) {
		this.origin = point;
	}

	public void setOrigin(Coord coord) {
		this.origin = new Point(coord.x * this.size, coord.y * this.size);
	}

	public Point calcGrid(Point point) {
		int x = (this.floor((double)(point.x) / this.size)) * this.size;
		int y = (this.floor((double)(point.y) / this.size)) * this.size;
		return new Point(x, y);
	}

	public int floor(Double num) {
		BigDecimal temp = new BigDecimal(num);
		temp.setScale(0, RoundingMode.FLOOR);
		return temp.intValue();
	}

	public int getIndex() {
		return this.index;
	}

	public void move(int x, int y) {
		this.origin = new Point(this.origin.x + (x * this.size), this.origin.y + (y * this.size));
	}

	public void setSelected(boolean b) {
		this.selected = b;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public Point getOriginRaw() {
		return this.origin;
	}

	public Coord getOrigin() {
		return new Coord(this.origin.x / this.size, this.origin.y / this.size);
	}

	public List<Coord> getOffsets() {
		return this.points;
	}

	public boolean matchPiece(Piece p) {
		if (p.index == this.index)
			return true;
		return false;
	}

	// Assumes same origin
	public boolean sameOrient(Piece p) {
		//System.out.println("\nNEW");
		if (p.origin == this.origin)
			return false;
		List<Coord> s1 = new ArrayList<Coord>(p.points);
		//System.out.println(s1.toString() + " - 1");
		List<Coord> s2 = new ArrayList<Coord>(this.points);
		//System.out.println(s2.toString() + " - 2");
		
//		Coord o = new Coord(0, 0);
//		int index = -1;
//		int counter = 0;
//		Iterator<Coord> it = s1.iterator();
//		while (it.hasNext()) {
//			Coord next = it.next();
//			if (next.equals(o)) {
//				System.out.println(next.toString() + " : " + o.toString());
//				index = counter;
//				break;
//			}
//			counter++;
//		}
////		for (Coord ct : s1.iterator()) {
////			if (s1[i].equals(o)) {
////				System.out.println(p.points.get(i).toString() + " : " + o.toString());
////				index = i;
////				break;
////			}
////			counter++;
////		}
//		System.out.println(index + " <- INDEX");
//		o = this.points.get(index);
//		System.out.println(o.toString() + " <- ADJUST");
//		Set<Coord> temp = new HashSet<Coord>();
//		for (Coord c : s1) {
//			System.out.println(c.x + ":" + c.y + " - " + (c.x + o.x) + ":" + (c.y + o.y) + " <- NEW");
//			temp.add(new Coord((c.x + o.x), (c.y + o.y)));
//		}
//		System.out.println(temp.toString());
//		System.out.println("EQUALS: " + temp.equals(s2));
		
		
		// Top Left and Bottom right Adjustments
		Coord tl1 = new Coord(0, 0);
		Coord tl2 = new Coord(0, 0);
		
		// Reconstructed shapes to align
		List<Coord> rc1 = new ArrayList<Coord>();
		List<Coord> rc2 = new ArrayList<Coord>();
		
		for (Coord c : s1) {
			//System.out.println("X: " + c.x + ":" + tl1.x + " - Y: " + c.y + ":" + tl1.y);
			int l = c.x < tl1.x ? c.x : tl1.x;
			int t = c.y < tl1.y ? c.y : tl1.y;
			tl1 = new Coord(l, t);
		}
		tl1 = new Coord(0 - tl1.x, 0 - tl1.y);
		
		for (Coord c : s2) {
			//System.out.println("X: " + c.x + ":" + tl2.x + " - Y: " + c.y + ":" + tl2.y);
			int l = c.x < tl2.x ? c.x : tl2.x;
			int t = c.y < tl2.y ? c.y : tl2.y;
			tl2 = new Coord(l, t);
		}
		tl2 = new Coord(0 - tl2.x, 0 - tl2.y);
		
		//System.out.println(tl1.toString() + " - 1");
		//System.out.println(tl2.toString() + " - 2");
		
		for (Coord c : s1) {
			rc1.add(new Coord(c.x + tl1.x, c.y + tl1.y));
		}
		
		for (Coord c : s2) {
			rc2.add(new Coord(c.x + tl2.x, c.y + tl2.y));
		}
		
		Coord[] t1 = new Coord[rc1.size()];
		for (int i = 0; i < rc1.size(); i++) {
			t1[i] = rc1.get(i);
		}
		Arrays.sort(t1);
		Coord[] t2 = new Coord[rc2.size()];
		for (int i = 0; i < rc2.size(); i++) {
			t2[i] = rc2.get(i);
		}
		Arrays.sort(t2);
		//System.out.print("[");
		for (int i = 0; i < t1.length; i++) {
			//System.out.print("[" + t1[i].x + "," + t1[i].y + "]");
		}
		//System.out.println("]");
		//System.out.print("[");
		for (int i = 0; i < t2.length; i++) {
			//System.out.print("[" + t2[i].x + "," + t2[i].y + "]");
		}
		//System.out.println("]");
		//System.out.println(rc1.toString() + " - 1");
		//System.out.println(rc2.toString() + " - 2");
		//System.out.println(Arrays.toString(t1).equals(Arrays.toString(t2)));
		return Arrays.toString(t1).equals(Arrays.toString(t2));
	}

	public int getRotation() {
		return this.orientation;
	}

	@Override
	public String toString() {
		String s = "( " + this.origin.x + "," + this.origin.y + " ;";
		for (Coord c : this.points) {
			s += c.x + "," + c.y + ";";
		}
		s += " " + this.orientation + ")";
		return s;
	}

	public boolean shapeMatches(Piece p) {
		if (this.points == p.points) {
			return true;
		}
		return false;
	}

}
