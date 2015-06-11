package com.gonz.game.States;

import java.awt.Graphics;
import java.awt.event.KeyListener;

import com.gonz.game.Input.State_Play_Input;
import com.gonz.game.States.State_Manager;
import com.gonz.game.Window.Game_Panel;
import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public class State_Play extends State{
	
	private static final long serialVersionUID = 1L;
	
	// Game status
	public static boolean moving = false;

	// World
	public Tile_World world;
	
	// Render Camera
	private static double cameraX;
	private static double cameraY;
	private static double cameraW;
	private static double cameraH;
	
	private KeyListener playInput;
	
	public State_Play(Game_Panel gP, State_Manager statman){	
		super("Play", gP, statman);	

	}	
	
	public static double getCameraX() {	return cameraX;	}
	public static double getCameraY() {	return cameraY;	}
	public static double getCameraW() {	return cameraW;	}
	public static double getCameraH() {	return cameraH;	}
	
	public static void setCameraX(double x) {	
		if(x < 0)			
			cameraX = 0;
		else if (x + cameraW > 1600)
			cameraX = 1600 - cameraW;
		else
			cameraX = x;
	}
	
	public static void setCameraY(double y) {	
		if(y < 0)			
			cameraY = 0;
		else if(y + cameraH > 1600)
			cameraY = 1600 - cameraH;		
		else				
			cameraY = y;
	}	
	
	public void init(){
		
		world = new Tile_World("World_0");	
			
		playInput = new State_Play_Input(this);
		gamePanel.addKeyListener(playInput);
		
		cameraW = gamePanel.getWidth();
		cameraH = gamePanel.getHeight();

		isRunning = true;		
	}		
	
	
	public void close() {			
		super.close();
		gamePanel.removeKeyListener(playInput);		
	}
		
	
	public static void pause(){	isRunning = isRunning ? false : true;	}	
		
	
	public void update(){
		if(world == null) return ;
		
		world.update();

	}
	
	
	public void render(Graphics g) {	
		if(world == null) return ;
		
		world.render(g, 
					(int)(cameraX/Tile.TILE_SIZE), 
					(int)(cameraY/Tile.TILE_SIZE), 
					(int)(cameraW/Tile.TILE_SIZE) + 1 , 
					(int)(cameraH/Tile.TILE_SIZE) + 1);

	}

}