package com.leetzilantonis.puzzle;

import java.util.Comparator;

public class PieceComparer implements Comparator<Piece> {

	@Override
	public int compare(Piece a, Piece b) {
		int start = compare(a.getIndex(), b.getIndex());
		return start != 0 ? start : compare(a.getIndex(), b.getIndex());
	}

	private int compare(int a, int b) {
		return a < b ? -1 : a > b ? 1 : 0;
	}
	
}
