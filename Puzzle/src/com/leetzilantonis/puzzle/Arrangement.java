package com.leetzilantonis.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Arrangement {

	private HashMap<Piece, Coord> locs = new HashMap<Piece, Coord>();
	
	public Arrangement(HashMap<Piece, Coord> locs) {
		this.locs = locs;
	}
	
	public Arrangement(List<Piece> locs) {
		HashMap<Piece, Coord> temp = new HashMap<Piece, Coord>();
		for (Piece p : locs) {
			temp.put(p, p.getOrigin());
		}
		this.locs = temp;
	}
	
	public List<Piece> isPieceMissing() {
		List<Piece> temp = new ArrayList<Piece>();
		for (Piece p : Main.pieces) {
			boolean isIn = true;
			for (Piece test : this.locs.keySet()) {
				if (test.getIndex() != p.getIndex()) {
					isIn = false;
					break;
				}
			}
			if (!isIn) {
				temp.add(p);
			}
		}
		return temp.isEmpty() ? null : temp;
	}
	
	public boolean isPiecePlaced(Piece p) {
		for (Piece test : this.locs.keySet()) {
			if (test.getIndex() == p.getIndex()) {
				return true;
			}
		}
		return false;
	}
	
	public void addPiece(Piece p, Coord c) {
		if (this.isPiecePlaced(p)) {
			return;
		}
		this.locs.put(p, c);
	}
	
	public boolean isTaken(Coord c) {
		if (this.locs.isEmpty())
			return false;
		for (Piece p : this.locs.keySet()) {
			for (Coord offset : p.getOffsets()) {
				Coord off = new Coord(this.locs.get(p).x + offset.x, this.locs.get(p).y + offset.y);
				if (off.equals(c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public HashMap<Piece, Coord> getRawData() {
		return this.locs;
	}
	
	public int freeSpaces(int x, int y, int fx, int fy) {
		int f = 0;
		for (int i = x; i <= fx; i++) {
			for (int ii = y; ii <= fy; ii++) {
				if (!isTaken(new Coord(i, ii))) {
					f += 1;
				}
			}
		}
		return f;
	}
	
	@Override
	public String toString() {
		if (this.locs.isEmpty()) {
			return "";
		}
		String s = "";
		for (Entry<Piece, Coord> e : this.locs.entrySet()) {
			s += "(" + e.getKey().getIndex() + ":" + e.getKey().getRotation() + " " + e.getValue().toString() + "\n";
		}
		return s;
	}
	
}
