package com.gonz.game.World;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.gonz.game.Music.SoundPlayer;
import com.gonz.game.Sprites.Signal;
import com.gonz.game.Sprites.Sprite;
import com.gonz.game.Sprites.Teleporter;
import com.gonz.game.Sprites.Entities.Entity;
import com.gonz.game.Sprites.Entities.EntityNPC;
import com.gonz.game.Sprites.Entities.EntityNPC_Looped;
import com.gonz.game.Sprites.Entities.EntityNPC_Static;
import com.gonz.game.Sprites.Entities.EntityPlayer;

public class Tile_World {
	
	/* Resources PATH */
	private final String PATH = "/Worlds/";
	
	/* World identification */
	private String name;
	
	/* World dimension */	
	private int width, height;
	private int width_in_tiles, height_in_tiles;
	private int upBound, downBound, leftBound, rightBound;
	
	/* World layers */
	private final int NUM_LAYERS = 4;
	private Tile[][] background;
	private Tile[][] foreground;
	private Tile[][] top;
	private boolean[][] collision;
	private Sprite[][] sprites;
		
	/* Sprites */
	private ArrayList<Entity> entities;
	private ArrayList<Teleporter> teleporters;
	
	public EntityPlayer player;

	private int initX_Tile, initY_Tile;	
	
	
	
	
	// CONSTRUCTORS
	
	public Tile_World(String name) {
		
		this.name = name;
		
		Tile_Set.init();
		
		loadLevel();
		loadSounds();
		
		setBounds();
		
		SoundPlayer.loop("Walkin' on Sanzshine.wav");
		
	}
	
	
	
	
	// GETTERS AND SETTERS
	
	public void addEntity(Entity ent) {	
		entities.add(ent);	
		if(sprites[ent.getXTile()][ent.getYTile()] == null)
			sprites[ent.getXTile()][ent.getYTile()] = ent;
		else
			System.err.println("[TILE_WORLD]\t\tFound some conflict adding entity "+ ent.getName() +
					" entity in position" + ent.getXTile() + " x ," + ent.getYTile() + " y");
	}
	
	public void addSprite(Sprite sprite) {	
		if(sprites[sprite.getXTile()][sprite.getYTile()] == null)
			sprites[sprite.getXTile()][sprite.getYTile()] = sprite;	
	}
	
	public void removeSprite(Sprite sprite) {
		if(sprites[sprite.getXTile()][sprite.getYTile()] != null 
				&& sprites[sprite.getXTile()][sprite.getYTile()].equals(sprite))
			sprites[sprite.getXTile()][sprite.getYTile()] = null;
	}
	
	public void occupyTile(Point p) {		
		collision[p.x][p.y] = true;			
	}
	
	public void occupyTile(int x, int y) {
		collision[x][y] = true;	
	}
	
	public void liberateTile(Point p) {		
		collision[p.x][p.y] = false;	
	}
	
	public void liberateTile(int x, int y) {
		collision[x][y] = false;	
	}
	
	private void setBounds() {
		downBound 	=	height;
		upBound  	= 	0;		
		rightBound 	= 	width;
		leftBound 	=	0;		
	}
	
	public int getWidthInTiles() {		return width_in_tiles;	}	
	
	public int getHeightInTiles() {		return height_in_tiles;	}
	
	public int getUpBound(){	return upBound;		}
	public int getDownBound(){	return downBound;	}
	public int getLeftBound(){	return leftBound;	}
	public int getRightBound(){	return rightBound;	}
	
	public int getInitX() {	return this.initX_Tile;	}
	public int getInitY() {	return this.initY_Tile;	}	
	
	public Sprite getSprite(int x, int y) {
		
		if( x >= 0 && x < width_in_tiles && y >= 0 && y < height_in_tiles )
			
			return sprites[x][y];
		
		return null;
	}
	
	public ArrayList<Entity> getEntitiesList() { return entities;	}
	
	public ArrayList<Teleporter> getTeleportersList() {	return this.teleporters;	}
	
	public boolean[][] getCollisionsArray() {
		return this.collision;
	}
	
	
	// LOADERS
	
	private void loadLevel() {
		
		long initTime = System.currentTimeMillis();
		
		int list = 0;
		
		ArrayList<String> worldData = new ArrayList<String>();
		
		ArrayList<String> background = new ArrayList<String>();
		ArrayList<String> foreground = new ArrayList<String>();
		ArrayList<String> top = new ArrayList<String>();
		ArrayList<String> collisions = new ArrayList<String>();
		
		ArrayList<String> sprites = new ArrayList<String>();
		ArrayList<String> entities = new ArrayList<String>();
		
		@SuppressWarnings("unchecked")
		ArrayList<String>[] lists = new ArrayList[NUM_LAYERS];
		lists[0] = background;
		lists[1] = foreground;
		lists[2] = top;
		lists[3] = collisions;
		
		try {
			
			// Read every line in the text and save it into the list
			InputStream in = getClass().getResourceAsStream(PATH + name + ".dat");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			while(true) {
				String line = reader.readLine();
								
				// The reader has arrived to the end
				if(line == null) {
					reader.close();
					break;
				}
				
				// Add every line the adequate list
				if(line.startsWith("["))
					;
				else if(line.startsWith("$")) 
					worldData.add(line);
				
				else if(line.startsWith("NPC"))
					entities.add(line);
				else if(line.startsWith("Teleporters") || line.startsWith("Signs"))
					sprites.add(line);	
				
				else if(line.startsWith("layer=background"))
					list = 0;
				else if(line.startsWith("layer=foreground"))
					list = 1;
				else if(line.startsWith("layer=top"))
					list = 2;
				else if(line.startsWith("layer=collisions"))
					list = 3;
				else
					lists[list].add(line);									
			}
			
		} catch (IOException e) {
			
			System.err.println("[WORLD_TILED]\t\tError loading " + name);
			e.printStackTrace();
			
		}
		
		loadWorldData(worldData); 
		
		loadBackground(background);
		loadForeground(foreground);
		loadTop(top);
		loadCollisions(collisions);
		
		loadSprites(sprites);
		loadEntities(entities);
		
		long finishTime = System.currentTimeMillis();
		long loadingTime = finishTime - initTime;
		
		System.out.println("[WORLD_TILED]\t\t"+name+" loaded in "+loadingTime+" milliseconds.");		
	}
	
	private void loadWorldData(ArrayList<String> list) {

		for(int i = 0; i < list.size(); i++) {
			
			String line = list.get(i);
			if(line.startsWith("$name=")){
				this.name = line.substring("$name=".length(), line.length());
				//System.out.println(this.name);
			}
			if(line.startsWith("$width_in_tiles=")){
				this.width_in_tiles = Integer.parseInt(line.substring("$width_in_tiles=".length(), line.length()));
				//System.out.println(this.width_in_tiles);
				this.width = width_in_tiles * Tile.TILE_SIZE;
				//System.out.println(this.width);
			}
			if(line.startsWith("$height_in_tiles=")){
				this.height_in_tiles = Integer.parseInt(line.substring("$height_in_tiles=".length(), line.length()));
				//System.out.println(this.height_in_tiles);
				this.height = height_in_tiles * Tile.TILE_SIZE;
				//System.out.println(this.height);
			}
			if(line.startsWith("$initX=")){
				this.initX_Tile = Integer.parseInt(line.substring("$initX=".length(), line.length()));
				//System.out.println(this.initX_Tile);				
			}
			if(line.startsWith("$initY=")){
				this.initY_Tile = Integer.parseInt(line.substring("$initY=".length(), line.length()));
				//System.out.println(this.initY_Tile);				
			}			
		}		
	}
	
	private void loadBackground(ArrayList<String> list) {
		this.background = new Tile[width_in_tiles][height_in_tiles];
		// System.out.println(list.size());
		for(int y = 0; y < height_in_tiles; y++) {
			
			String line = list.get(y);
			String [] keys = line.split(",");
			
			for(int x = 0; x < width_in_tiles; x++)  {
				
				int tilePos = Integer.parseInt(keys[x]);
				int j = tilePos/12;
				int i = tilePos - j*12 - 1;
				int[] v = {i,j};
				
				background[x][y] = new Tile(new Rectangle(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE), v, false);			
			}
		}
	}
	
	private void loadForeground(ArrayList<String> list) {
		this.foreground = new Tile[width_in_tiles][height_in_tiles];
		// System.out.println(list.size());
		for(int y = 0; y < height_in_tiles; y++) {
			
			String line = list.get(y);
			String [] keys = line.split(",");
			
			for(int x = 0; x < width_in_tiles; x++)  {
				
				int tilePos = Integer.parseInt(keys[x]);
				int j = tilePos/12;
				int i = tilePos - j*12 - 1;
				int[] v = {i,j};					
				
				foreground[x][y] = new Tile(new Rectangle(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE), v, false);			
			}
		}
	}	
	
	private void loadTop(ArrayList<String> list) {
		this.top = new Tile[width_in_tiles][height_in_tiles];
		// System.out.println(list.size());
		for(int y = 0; y < height_in_tiles; y++) {
			
			String line = list.get(y);
			String [] keys = line.split(",");
			
			for(int x = 0; x < width_in_tiles; x++)  {
				
				int tilePos = Integer.parseInt(keys[x]);
				int j = tilePos / Tile_Set.width_in_tiles;
				int i = tilePos - j*Tile_Set.width_in_tiles - 1;
				int[] v = {i,j};
					
				top[x][y] = new Tile(new Rectangle(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE), v, false);			
			}
		}
	}	

	private void loadCollisions(ArrayList<String> list) {
		this.collision = new boolean[width_in_tiles][height_in_tiles];
		// System.out.println(list.size());
		for(int y = 0; y < height_in_tiles; y++) {
			
			String line = list.get(y);
			String [] keys = line.split(",");
			
			for(int x = 0; x < width_in_tiles; x++)  {
				
				int tilePos = Integer.parseInt(keys[x]);
			
				collision[x][y] = tilePos == 0 ? false : true;					
			}
		}
	}
	
	private void loadSprites(ArrayList<String> list) {
		
		this.sprites = new Sprite[width_in_tiles][height_in_tiles];
		this.teleporters = new ArrayList<Teleporter>();
		
		for(int i = 0; i < list.size(); i++ ) {
			
			String line = list.get(i);
			String[] spr;
								
			if(line.startsWith("Teleporters=")) { 		
		
				line = line.substring("Teleporters=".length());				
				spr = line.split(",");				
				for(int j = 0; j < spr.length; j++) {
					Teleporter tp = new Teleporter(spr[j], this);
					teleporters.add(tp);
					this.addSprite(tp);
					// System.out.println("Teleport " + spr[j] + " added");
				}
			}
			
			else if(line.startsWith("Signs=")) { 		
				
				line = line.substring("Signs=".length());				
				spr = line.split(",");				
				for(int j = 0; j < spr.length; j++) {
					Signal sign = new Signal(spr[j], this);
					this.addSprite(sign);
					//System.out.println("Sign " + spr[j] + " added");
				}
			}
		}		
		
	}
	
	private void loadEntities(ArrayList<String> list) {
		this.entities = new ArrayList<Entity>();
		
		player = new EntityPlayer("Player", this);
		this.entities.add(player);
		
		for(int i = 0; i < list.size(); i++ ) {
			
			String line = list.get(i);
			String[] ents;
								
			if(line.startsWith("NPCStatic=")) { 				
				line = line.substring("NPCStatic=".length());				
				ents = line.split(",");				
				for(int j = 0; j < ents.length; j++) 
					entities.add(new EntityNPC_Static(ents[j], this));
			}		
			else if(line.startsWith("NPCLooped=")) { 				
				line = line.substring("NPCLooped=".length());			
				ents = line.split(",");				
				for(int j = 0; j < ents.length; j++) 
					entities.add(new EntityNPC_Looped(ents[j], this));
			}			
			else {				
				line = line.substring("NPCBasic=".length());
				ents = line.split(",");				
				for(int j = 0; j < ents.length; j++) 
					entities.add(new EntityNPC(ents[j], this));
			}			
		}
	}	
	
	private void loadSounds() {
		
		SoundPlayer.loadSound("Walkin' on Sanzshine.wav", SoundPlayer.MUSIC_PATH);	
		SoundPlayer.loadSound("Teleport.wav", SoundPlayer.SOUND_EFFECT_PATH);	
		
	}
	
	
	
	
	// METHODS

	public void update() {
		
		for(int i = 0; i < entities.size(); i++)
			
			entities.get(i).update();
		
	}
	
	public void render(Graphics g, int camX, int camY, int renX, int renY) {
		
		// First render the background, solids and objects layers
		for(int i = camX; i < camX + renX; i++)
				
			for(int j = camY; j < camY + renY; j++)
					
				if(i >= 0 && j >= 0 && i < this.width_in_tiles && j < this.height_in_tiles ){
										
					background[i][j].render(g);
					foreground[i][j].render(g);
		
				}

		// Then render the sprites
		for(int i = 0; i < entities.size(); i++)
			
			entities.get(i).render(g);
		
		// And finally render the top layer
		for(int i = camX; i < camX + renX; i++)
			
			for(int j = camY; j < camY + renY; j++)
					
				if(i >= 0 && j >= 0 && i < this.width_in_tiles && j < this.height_in_tiles ) 
										
					top[i][j].render(g);
		
		// Render dialog
		if(player.isInDialog()) 	player.renderDialog(g);
				
	}
}
