package com.gonz.game.World;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.gonz.game.States.State_Play;

public class Tile extends Rectangle {
	
	private static final long serialVersionUID = 1L;
	public static final int[] TILE_DEFAULT_ID = {-1, -1};
	public static final int TILE_SIZE = 32;	
	
	private int[] id;
	private boolean solid;
	
	/** Constructor for a void tile */
	public Tile(Rectangle rect) {
		setBounds(rect);
		this.id = TILE_DEFAULT_ID;
		this.solid = false;
	}
	
	/** Contructor for a non-void tile */
	public Tile(Rectangle rect, int[]id, boolean solid) {
		setBounds(rect);
		this.id = id;	
		this.solid = solid;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void render(Graphics g) {
		if(id[0]>-1 && id[1] >-1)
			g.drawImage(Tile_Set.tileSet,
						x - (int) State_Play.getCameraX(), 
						y - (int) State_Play.getCameraY(), 
						x + TILE_SIZE - (int) State_Play.getCameraX(), 
						y + TILE_SIZE - (int) State_Play.getCameraY(), 
						id[0]*TILE_SIZE, 
						id[1]*TILE_SIZE, 
						id[0]*TILE_SIZE + TILE_SIZE,
						id[1]*TILE_SIZE + TILE_SIZE, 
						null);		
	}
	
}
