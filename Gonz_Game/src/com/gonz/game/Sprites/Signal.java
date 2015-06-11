package com.gonz.game.Sprites;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public class Signal extends Sprite {
	
	protected final String DATA_PATH = super.DATA_PATH + "Signals/";
	
	private String text; 
	
	public Signal(String name, Tile_World world) {
		
		super(name, world);
		
		loadData();
		
		this.x_Pix = x_Tile * Tile.TILE_SIZE;
		this.y_Pix = y_Tile * Tile.TILE_SIZE;
		
		world.addSprite(this);
		
	}	
	
	protected void loadData() {
		
		try {
			
			// Initialize the reader
			InputStream in = getClass().getResourceAsStream(DATA_PATH + this.name + ".dat");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			// Read every file line
			while(true) {
				
				String line = reader.readLine();
				
				if(line == null) {		// Reader is at end
					
					reader.close();
					break;
					
				}
				
				// Compare and save the attribute
				
				if(line.startsWith("x=")) 
					this.x_Tile = Integer.parseInt(line.substring("x=".length()));				
				
				else if(line.startsWith("y=")) 
					this.y_Tile = Integer.parseInt(line.substring("y=".length()));
	
				else if(line.startsWith("text="))
					this.text = "";
				
				else
					text += line + " ";
			}
			
		} catch (IOException e) {
			
			System.err.println("[SIGNAL]\t\tError reading "+ this.name +".dat");
			
		}			
	}
	
	
	public String getText() {
		
		return this.text;
		
	}
	
	public void showText(Graphics g) {
		
		
		
	}
	
}
