package com.gonz.game.World;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile_Set {
	
	// Assets path
	private static final String ART_PATH = "/Images/State_Play/Worlds/";
	
	public static int width_in_tiles;
	public static int height_in_tiles;
	
	public static BufferedImage tileSet;
	
	public static void init() {
		
		try {	
	
			tileSet = ImageIO.read(Tile_Set.class.getResourceAsStream((ART_PATH + "TileSet.png")));
	
			
		} catch (IOException e) {
			
			System.err.println("Error loading Tile Set :(");
			e.printStackTrace();			
		}
		
		width_in_tiles = tileSet.getWidth() / Tile.TILE_SIZE;
		height_in_tiles = tileSet.getHeight() / Tile.TILE_SIZE;
		
		System.out.println("[TILE_SET]\t\tTileSet.png loaded");
		
	}	
	
	
}
