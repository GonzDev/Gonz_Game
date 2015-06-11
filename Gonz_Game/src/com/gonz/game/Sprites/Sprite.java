package com.gonz.game.Sprites;

import java.awt.Point;

import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public abstract class Sprite {

	protected Tile_World world;	
	
	// Sprites data path
	protected final String DATA_PATH = "/Sprites/";
	protected final String ART_PATH = "/Images/State_Play/Sprites/";
	
	// Sprite name
	protected String name;
	
	// Sprite's coordinates
	protected double x_Pix, y_Pix;			// Pixels
	protected int x_Tile, y_Tile;			// Tiles -----> 1Tile = 32x32 pix
	
	protected int width, height;
	
	public Sprite(String name, Tile_World tW) {
		
		this.name = name;
		this.world = tW;
		
		this.width = Tile.TILE_SIZE;
		this.height = Tile.TILE_SIZE;
		
	}
	
	protected abstract void loadData();
	
	public void setXPix(double x) {	this.x_Pix = x;	}
	public void setYPix(double y) {	this.y_Pix = y;	}
	
	public void setXTile(int x)	{	this.x_Tile = x; }
	public void setYTile(int y)	{	this.y_Tile = y; }	
	
	public double getXPix(){ return this.x_Pix; }
	public double getYPix(){ return this.y_Pix; }
		
	public int getXTile(){	return x_Tile;	}
	public int getYTile(){	return y_Tile;	}	
	
	public Point getTileLocation() {	return new Point(x_Tile, y_Tile);	}
	public Point getPixlLocation() {	return new Point((int)x_Pix, (int)y_Pix);	}
	
}
