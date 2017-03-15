package com.leetzilantonis.puzzle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Board {

	private int size, width, height;
	private List<Piece> pieces = new ArrayList<Piece>();
	private List<Piece> piecesLeft = new ArrayList<Piece>();
	private Color background, border;
	private Coord origin;
	private List<Arrangement> arr = new ArrayList<Arrangement>();
	private long time = 0;

	public Board(List<Piece> pieces, Coord origin, int width, int height, int size, Color background, Color border) {
		this.background = background;
		this.border = border;
		this.origin = origin;
		this.size = size;
		this.width = width;
		this.height = height;
		this.pieces = pieces;
		this.piecesLeft = new ArrayList<Piece>(pieces);
	}

	public Board(List<Piece> pieces, Coord origin, int width, int height, int size) {
		this(pieces, origin, width, height, size, Color.BLACK, Color.WHITE);
	}

	public void draw(Graphics g) {
		for (int ix=0; ix<width; ix++) {
			for (int iy=0; iy<height; iy++) {
				int x = (ix + origin.x) * this.size;
				int y = (iy + origin.y) * this.size;
				g.setColor(this.background);
				g.fillRect(x, y, this.size, this.size);
				g.setColor(this.border);
				g.drawRect(x, y, this.size, this.size);
			}
		}
	}

	public void setBorder(Color c) {
		this.border = c;
	}

	public List<Coord> getFreeSlots() {
		List<Coord> list = new ArrayList<Coord>();
		for (int ix=0; ix<width;ix++) {
			for (int iy=0; iy<height; iy++) {
				Coord p = new Coord(ix + origin.x, iy + origin.y);
				if (list.contains(p)) {
					continue;
				}
				if (isTaken(p) == null) {
					list.add(p);
				}
			}
		}
		return list;
	}

	public Piece isTaken(Coord point) {
		if (point.x < origin.x + width && point.x >= origin.x) {
			if (point.y < origin.y + height && point.y >= origin.y) {
				for (Piece p : this.pieces) {
					if (!p.drawn) {
						continue;
					}
					Coord o = p.getOrigin();
					List<Coord> offsets = p.getOffsets();
					for (Coord offset : offsets) {
						Coord test = new Coord(o.x + offset.x, o.y + offset.y);
						if (test.equals(point)) {
							//Main.debugLoop("SOMETHING: " + test.x + " : " + test.y);
							return p;
						}
					}
				}
			} else {
				//Main.debug("Y OUT");
				return new Piece(Main.generate("0,0"), new Coord(-1, -1), Color.BLACK, 100, "Z");
			}
		} else {
			//Main.debug("X OUT");
			return new Piece(Main.generate("0,0"), new Coord(-1, -1), Color.BLACK, 100, "Z");
		}
		return null;
	}

	public List<Piece> allTranslations(Piece p) {
		List<Piece> trans = new ArrayList<Piece>();
		Piece p1 = new Piece(p);
		Piece p2 = new Piece(p).rotate();
		//p2.rotate();
		Piece p3 = new Piece(p).rotate().rotate();
		//p3.rotate().rotate();
		Piece p4 = new Piece(p).rotate().rotate().rotate();
		//p4.rotate().rotate().rotate();
		Piece p5 = new Piece(p).flip(1);
		//p5.flip(1);
		Piece p6 = new Piece(p).flip(1).rotate();
		//p6.flip(1).rotate();
		Piece p7 = new Piece(p).flip(1).rotate().rotate();
		//p7.flip(1).rotate().rotate();
		Piece p8 = new Piece(p).flip(1).rotate().rotate().rotate();
		//p8.flip(1).rotate().rotate().rotate();
		trans.add(p1);
		trans.add(p2);
		trans.add(p3);
		trans.add(p4);
		trans.add(p5);
		trans.add(p6);
		trans.add(p7);
		trans.add(p8);
		List<Piece> temp = new ArrayList<Piece>();
		for (Piece pi : trans) {
			//Main.debug("LOOP");
			if (temp.isEmpty()) {
				temp.add(pi);
				continue;
			}
			boolean c = false;
			for (Piece pie : temp) {
				pie.resetOrigin();
				if (pie.sameOrient(pi)) {
					Main.debug("MATCH");
					c = true;
					break;
				}
			}
			if (!c)
				temp.add(p);
		}
		return temp;
	}

	public boolean positionValid(Piece piece, Coord pos) {
		for (Coord p : piece.getOffsets()) {
			Coord test = new Coord(pos.x + p.x, pos.y + p.y);
			if (isTaken(test) != null) {
				return false;
			}
		}
		return true;
	}

	public boolean positionValid(Arrangement a, Piece piece, Coord pos) {
		for (Coord p : piece.getOffsets()) {
			Coord test = new Coord(pos.x + p.x, pos.y + p.y);
			//			Main.debug(test.toString());
			//			Main.debug(origin.x + " " + origin.y);
			//			Main.debug(width + " " + height);
			if (test.x < origin.x + width && test.x >= origin.x) {
				if (test.y < origin.y + height && test.y >= origin.y) {
					if (a.isTaken(test)) {
						//Main.debug("TAKEN FALSE");
						return false;
					}
				} else {
					//Main.debug("Y OUT");
					return false;
				}
			} else {
				//Main.debug("X OUT");
				return false;
			}
		}
		return true;
	}

	// NEED TO USE IT
	public HashMap<Piece, List<Coord>> getAllPositions(Piece piece) {
		HashMap<Piece, List<Coord>> pos = new HashMap<Piece, List<Coord>>();
		for (Piece p : this.allTranslations(piece)) {
			List<Coord> tempL = new ArrayList<Coord>();
			for (int ix=0; ix < width;ix++) {
				for (int iy=0; iy < height; iy++) {
					Coord temp = new Coord(ix + origin.x, iy + origin.y);
					if (this.positionValid(p, temp)) {
						//Main.debug("POSITION VALID");
						if (!tempL.contains(temp)) {
							//Main.debug("Exists NOT");
							tempL.add(temp);
						}
					}
				}
			}
			if (!pos.containsKey(p)) {
				pos.put(p, tempL);
			}
		}
		return pos;
	}

	public List<Arrangement> getAllPositions(Piece piece, List<Arrangement> arr) {
		Main.debug("BEFORE: " + arr.size());
		if (arr.isEmpty()) {
			Main.debug("EMPTY");
			Main.debug(this.allTranslations(piece).size() + " PS");
			for (Piece p : this.allTranslations(piece)) {
				Main.debug(p.toString());
				for (int ix=0; ix < width;ix++) {
					for (int iy=0; iy < height; iy++) {
						Coord c = new Coord(ix + origin.x, iy + origin.y);
						Arrangement a = new Arrangement(new HashMap<Piece, Coord>());
						//Main.debug(ix + " : " + iy);
						if (this.positionValid(p, c)) {
							//Main.debug("VALID");
							a.addPiece(p, c);
							synchronized (a) {
								if (this.time + 1000 < System.currentTimeMillis()) {
									this.time = System.currentTimeMillis();
									this.setArrangement(a);
								}
							}
							arr.add(a);
						}
					}
				}
			}
			Main.debug("AFTER1: " + arr.size());
			return arr;
		}
		List<Arrangement> f = new ArrayList<Arrangement>();
		for (Arrangement a : arr) {
			//Main.debug(p.toString());
			for (int ix=0; ix<width;ix++) {
				for (int iy=0; iy<height; iy++) {
					for (Piece p : this.allTranslations(piece)) {
						Coord c = new Coord(ix + origin.x, iy + origin.y);
						if (this.positionValid(a, p, c)) {
							//Main.debug("VALID");
							a.addPiece(p, c);
//							synchronized (a) {
//								if (this.time + 1000 < System.currentTimeMillis()) {
//									Main.debug("TRUE");
//									this.time = System.currentTimeMillis();
//									this.setArrangement(a);
//								}
//							}
							f.add(a);
							//Main.debug(f.size() + " ID");
						}
					}
				}
			}
		}
		Main.debug("AFTER2: " + f.size());
		return f;
	}

	private void setArrangement(Arrangement a) {
		List<Piece> temp = new ArrayList<Piece>();
		for (Entry<Piece, Coord> e : a.getRawData().entrySet()) {
			e.getKey().setOrigin(e.getValue());
			e.getKey().setDrawn(true);
			temp.add(e.getKey());
		}
		this.pieces = temp;
		Main.pieces = temp;
	}

	public List<Arrangement> genNewSolutions() {
		for (Piece p : this.piecesLeft) {
			//Main.debug(p.toString());
		}
		this.arr = new ArrayList<Arrangement>();
		return this.genSolutions();
	}

	public List<Arrangement> genSolutions() {
		if (arr == null) {
			arr = new ArrayList<Arrangement>();
		}
		if (this.piecesLeft.isEmpty()) {
			List<Arrangement> finalList = new ArrayList<Arrangement>();
			for (Arrangement a : arr) {
				if (a.getRawData().size() == Main.pieceCount) {
					Main.debug("CHECK 1");
					finalList.add(a);
				}
			}
			return finalList;
		} else {
			Piece p = this.piecesLeft.get(0);
			List<Arrangement> pos = this.getAllPositions(p, arr);
			if (pos.isEmpty()) {
				Main.debug("NULL");
				return null; // Not enough grid space to fit the piece
			}
			this.arr = pos;
			this.piecesLeft.remove(p);
			if (this.piecesLeft.isEmpty()) {
				List<Arrangement> finalList = new ArrayList<Arrangement>();
				for (Arrangement a : arr) {
					Main.debug("" + a.toString());
					if (a.getRawData().size() == Main.pieceCount) {
						finalList.add(a);
					}
				}
				return finalList;
			}
			Main.debug("NEW");
			return genSolutions();
		}
	}

	public List<Arrangement> getSolutions(boolean ignoreSpares) {
		if (arr == null) {
			arr = new ArrayList<Arrangement>();
		}
		if (this.piecesLeft.isEmpty()) {
			List<Arrangement> finalList = new ArrayList<Arrangement>();
			for (Arrangement a : arr) {
				Main.debug("" + a.getRawData().size());
				if (a.getRawData().size() == Main.pieceCount) {
					Main.debug("CHECK 3");
					finalList.add(a);
				}
			}
			return finalList;
		} else {
			if (ignoreSpares) {
				List<Arrangement> finalList = new ArrayList<Arrangement>();
				for (Arrangement a : arr) {
					Main.debug("" + a.getRawData().size());
					if (a.getRawData().size() == Main.pieceCount) {
						Main.debug("CHECK 4");
						finalList.add(a);
					}
				}
				return finalList;
			}
			return null;
		}
	}

}
