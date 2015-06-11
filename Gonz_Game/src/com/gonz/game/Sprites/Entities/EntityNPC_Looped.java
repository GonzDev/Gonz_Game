package com.gonz.game.Sprites.Entities;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.gonz.game.Sequence.Instruction;
import com.gonz.game.Sequence.Sequence;
import com.gonz.game.Sequence.SequenceLoop;
import com.gonz.game.World.Tile_World;

public class EntityNPC_Looped extends EntityNPC {
	
	private Sequence sequence;
	
	public EntityNPC_Looped(String name, Tile_World tW) {	
		
		super(name, tW);			
		this.waitingTicks = 0;
		this.loadSequence();
		
	}	
	
	// LOADERS	

	public void loadSequence() {
		
		sequence = new SequenceLoop(this);		
			
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
				
				if(line.startsWith("sequence=")) {	
					
					line = line.substring("name=".length());	
					String[] instructions = line.split("-");
					
					for(int i = 0; i < instructions.length; i++) 
						
						switch (instructions[i]) {
						case "up":
							sequence.addInstruction(Instruction.UP);
							break;
						case "down":
							sequence.addInstruction(Instruction.DOWN);
							break;
						case "right":
							sequence.addInstruction(Instruction.RIGHT);
							break;
						case "left":
							sequence.addInstruction(Instruction.LEFT);
							break;

						default:
							break;
						}					
				}				
			}			
			
		} catch (IOException e) {			
			System.err.println("[ENTITY]\t\tError reading "+ name +".dat");			
		}			
	}
	
	
	
	
	// METHODS
	
	public void update() {
				
		if(waitingTicks > 0) 
			waitingTicks--;
				
		else if(movingTicks > 0) {
			
			movingTicks--;
			
			if(canMove) {				
				move();				
				update_TilePosition();
			}

		}
		
		else if(isInDialog)	
			
			lookAtPlayer();	
		
		else {
			
			if(canMove)
				selectDirection();	
			
			else {
				waitingTicks = 5;
				movingTicks = MOVING_TICKS;
			}
			
			canMove();
		}
		
	}	
	
	public void move() {		
	
		super.move();
	
		
	}
	
	private boolean canMove() {
		
		if(up)
			return super.canMove(x_Tile, y_Tile - 1); 
		else if(down)
			return super.canMove(x_Tile, y_Tile + 1); 
		else if(right)
			return super.canMove(x_Tile + 1, y_Tile);
		else if(left)
			return super.canMove(x_Tile - 1, y_Tile); 
		
		return true;
		
	}
	
	protected void selectDirection() {
		
		this.sequence.executeSeq();
				
	}
	
	public boolean canMove(int x, int y) {
		
		canMove = true;
		
		if(x < 0 || y < 0 || x > world.getWidthInTiles() - 1 || y > world.getHeightInTiles() - 1)
			
			canMove = false;
		
		else if(world.getCollisionsArray()[x][y] == true)		// true == collision
			
			canMove = false;
		
		else if(world.getSprite(x, y) != null && world.getSprite(x, y) instanceof Entity)
			
			canMove = false;
		
		// REPORT THE COLLISION
		if(!canMove) System.out.println(this.toString() + " has found one collision");
		
		return canMove;
		
	}
	
	public void render(Graphics g) {
		
		super.render(g);
		
	}
	
	public void stopDialog() {	this.isInDialog = false;	}
}
