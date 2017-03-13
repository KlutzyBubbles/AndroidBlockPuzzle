package com.leetzilantonis.puzzle;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Grid {

	private Color color = Color.BLACK;
	private int size = 40;
	
	public Grid(Color color, Graphics g, JComponent panel, int size) {
		this.color = color;
		this.size = size;
		int xMax = panel.getWidth() / this.size;
		int yMax = panel.getHeight() / this.size;
		
		for (int ix=0; ix<=xMax; ix++) {
			for (int iy=0; iy<=yMax; iy++) {
				int x = ix * this.size;
				int y = iy * this.size;
				g.setColor(this.color);
				g.drawRect(x, y, this.size, this.size);
			}
		}
		
	}
	
}
