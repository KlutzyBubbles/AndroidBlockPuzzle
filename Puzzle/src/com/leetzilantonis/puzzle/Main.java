package com.leetzilantonis.puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {

	public static List<Piece> pieces = new ArrayList<Piece>();
	public static Piece last = null;
	public static boolean dragged = false;
	public static Board mainBoard = null;
	public static int currentArr = -1;
	public static int pieceCount = 0;
	public static List<Arrangement> list = new ArrayList<Arrangement>();

	public static void main(String[] args) {
		pieces.add(new Piece(generate("0,0;0,1;1,1;2,1"), new Coord(0, 1), c("#ff8000"), 1, "A"));
		pieces.add(new Piece(generate("0,0;1,0;0,1;1,1;2,1"), new Coord(0, 3), c("#e60000"), 2, "B"));
		pieces.add(new Piece(generate("0,0;0,1;0,2;0,3;-1,3"), new Coord(5, 1), c("#0000cc"), 3, "C"));
		pieces.add(new Piece(generate("0,0;1,0;2,0;3,0;2,1"), new Coord(6, 1), Color.PINK, 4, "D"));
		pieces.add(new Piece(generate("0,0;1,0;1,1;2,1;3,1"), new Coord(0, 0), c("#008000"), 5, "E"));
		pieces.add(new Piece(generate("0,0;0,1;1,1"), new Coord(6, 3), Color.WHITE, 6, "F"));
		pieces.add(new Piece(generate("0,0;1,0;2,0;2,1;2,2"), new Coord(2, 0), c("#b3daff"), 7, "G"));
		pieces.add(new Piece(generate("0,0;1,0;1,1;2,1;2,2"), new Coord(6, 2), c("#ff0080"), 8, "H"));
		pieces.add(new Piece(generate("0,0;1,0;1,1;1,2;0,2"), new Coord(9, 0), c("#ffd11a"), 9, "I"));
		pieces.add(new Piece(generate("0,0;1,0;2,0;3,0"), new Coord(5, 0), c("#6600cc"), 10, "J"));
		pieces.add(new Piece(generate("0,0;0,1;1,0;1,1"), new Coord(9, 3), c("#66ff66"), 11, "K"));
		pieces.add(new Piece(generate("0,0;0,1;-1,1;1,1;0,2"), new Coord(3, 2), Color.LIGHT_GRAY, 12, "L"));
		for (Piece p : pieces) {
			p.setDrawn(false);
		}
		pieceCount = pieces.size();
		mainBoard = new Board(pieces, new Coord(1, 1), 11, 5, 60);
		Collections.reverse(pieces);
		for (Piece p : pieces) {
			p.move(1, 1);
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				start(); 
			}
		});
	}
	
	private static Color c(String s) {
		return new Color(
	            Integer.valueOf(s.substring( 1, 3 ), 16),
	            Integer.valueOf(s.substring( 3, 5 ), 16),
	            Integer.valueOf(s.substring( 5, 7 ), 16));
	}

	private static void start() {
		JFrame f = new JFrame("Puzzle Game");
		JPanel p = new TestPanel();
		p.setPreferredSize(new Dimension(780, 780));
		p.setBackground(Color.BLACK);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		f.getContentPane().add(p);
		f.pack();
		f.setVisible(true);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				list = mainBoard.genNewSolutions();
//			}
//		}).start();
		System.out.println(list.toArray());
	}
	
	public static List<Coord> generate(String string) {
		List<Coord> list = new ArrayList<Coord>();
		for (String piece : string.split(";")) {
			list.add(new Coord(Integer.parseInt(piece.split(",")[0]), Integer.parseInt(piece.split(",")[1])));
		}
		return list;
	}
	
	public static Piece getPiece(int index) {
		for (Piece p : pieces) {
			if (p.getIndex() == index) {
				return p;
			}
		}
		return null;
	}
	
	public static void deSelectAll() {
		for (Piece p : pieces) {
			p.setSelected(false);
		}
	}

}

class TestPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4905155374118042164L;

	public TestPanel() {

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!Main.dragged) {
					if (e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2) {
						List<Arrangement> arr = Main.mainBoard.getSolutions(true);
						if (arr == null)
							return;
						if (arr.isEmpty())
							return;
						if (e.getButton() == MouseEvent.BUTTON2) {
							if (Main.currentArr == -1) {
								Main.currentArr = 0;
								setArrangement(arr.get(Main.currentArr));
							} else if (Main.currentArr == 0) {
								Main.currentArr = arr.size() - 1;
								setArrangement(arr.get(Main.currentArr));
							} else {
								Main.currentArr -= 1;
								setArrangement(arr.get(Main.currentArr));
							}
						} else {if (Main.currentArr == 0) {
								Main.currentArr += 1;
								setArrangement(arr.get(Main.currentArr));
							} else {
								Main.currentArr = 0;
								setArrangement(arr.get(Main.currentArr));
							}
						}
//						boolean nothing = true;
//						for (Piece p : Main.pieces) {
//							if (p.isNear(new Coord(e.getPoint())) != null) {
//								nothing = false;
//								if (p.isSelected()) {
//									p.flip(e.getButton());
//									break;
//								} else {
//									Main.deSelectAll();
//									p.setSelected(true);
//								}
//							}
//						}
//						if (nothing) {
//							Main.deSelectAll();
//						}
					} else {
						boolean nothing = true;
						for (Piece p : Main.pieces) {
							if (p.isNear(new Coord(e.getPoint())) != null) {
								nothing = false;
								if (p.isSelected()) {
									p.rotate(p.isNear(new Coord(e.getPoint())));
									break;
								} else {
									Main.deSelectAll();
									p.setSelected(true);
								}
							}
						}
						if (nothing) {
							Main.deSelectAll();
						}
					}
				} else {
					System.out.println("----- RELEASED -----");
					for (Coord p : Main.mainBoard.getFreeSlots()) {
						System.out.println("X: " + p.x + " Y: " + p.y);
					}
				}
				refresh();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				Main.dragged = false;
				for (Piece p : Main.pieces) {
					if (p.isNear(new Coord(e.getPoint())) != null) {
						p.adjust(p.isNear(new Coord(e.getPoint())));
						p.setOrigin(p.calcGrid(e.getPoint()));
						Main.last = p;
						break;
					}
				}
				refresh();
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Main.dragged = true;
				if (Main.last != null) {
					Main.deSelectAll();
					Main.last.setSelected(true);
					Main.last.setOrigin(Main.last.calcGrid(e.getPoint()));
				}
				refresh();
			}
		});

	}

	private void refresh() {
		repaint();
	}
	
	public void setArrangement(Arrangement a) {
		List<Piece> temp = new ArrayList<Piece>();
		for (Entry<Piece, Coord> e : a.getRawData().entrySet()) {
			e.getKey().setOrigin(e.getValue());
			e.getKey().setDrawn(true);
			temp.add(e.getKey());
		}
		Main.pieces = temp;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		new Grid(Color.DARK_GRAY, g, this, 60);
		List<Piece> temp = new ArrayList<Piece>(Main.pieces);
		Main.mainBoard.setBorder(Color.WHITE);
		Main.mainBoard.draw(g);
		Collections.sort(temp, new PieceComparer());
		for (Piece p : temp) {
			p.setDrawn(true);
			p.draw(g);
		}
	}

}
