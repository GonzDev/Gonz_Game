package com.gonz.game.Sprites.Entities;

import java.awt.Graphics;

import com.gonz.game.Sprites.Sprite;
import com.gonz.game.Sprites.Teleporter;
import com.gonz.game.World.Tile_World;

public class EntityNPC extends Entity {
	
	protected final int WAITING_TICKS = 425;		
	protected final String DEFAULT_TILESET = "Default_NPC";
	
	protected String text;
	
	
	
	// CONSTRUCTORS
		
	public EntityNPC(String name, Tile_World tW) {	
		
		super(name, tW);
		waitingTicks = 85;	// 85 ticks = 1 second aprox.
		
		text = "[WIP] This is a test dialog. Don't disturb me please.";
		
	}
	
	
	
	// GETTERS AND SETTERS
	
	public String getText() {	return this.text;	}
	
	public void startDialog() {
		
		this.isInDialog = true;
		waitingTicks = 0;
		
	}
	
	public void stopDialog() {
		
		this.isInDialog = false;
		waitingTicks = WAITING_TICKS;
		
	}
	
	public void lookAtPlayer() {
		
		if(world.player.isFacingUp())
			this.setFacingDown();
		
		else if(world.player.isFacingDown())
			this.setFacingUp();
		
		else if(world.player.isFacingRight())
			this.setFacingLeft();
		
		else if(world.player.isFacingLeft())
			this.setFacingRight();
		
	}
	
	
		
	
	
	
	// METHODS
	
	public void update() {

		if(waitingTicks > 0) 	// The entity has to wait			
			waitingTicks--;
		
		else if(movingTicks > 0) {	
			
			if(canMove) {
				movingTicks--;			
				move();
			}
			
			teleport();
			
			if(isInSequence && !canMove)
				movingTicks++;
			
			if(movingTicks == 0) {
				stopMoving();
			}
		}

		if(isInDialog)
			
			lookAtPlayer();
		if(!isInDialog && movingTicks == 0 && waitingTicks == 0) {
			
			if(isInSequence)			
				sequence.executeSeq();		
				
			else
				selectDirection();
		}
		
		update_TilePosition();				
				
	}
	
	public void move() { 
		
		super.move();
		
	}
	
	public boolean canMove(int x, int y) {
		
		canMove = super.canMove(x, y);
			
		if(canMove) {
			Sprite spt = world.getSprite(x, y);
			if(spt != null && spt instanceof Teleporter)
				canMove = false;
		}
		
		return canMove;
		
	}	

	protected void stopMoving() {				
		super.stopMoving();
		aniFrame = 0;
		waitingTicks = WAITING_TICKS;
	}	
	
	protected void selectDirection() {
	
		double randomDir = Math.random();
		if (randomDir < 0.50) {		// The NCP has the fifty percent probability of moving							
			// Wait 5 seconds	
			isMoving = up = down = right = left = false; 	
			waitingTicks = WAITING_TICKS;
						
		}
		
		else if (randomDir < 0.60) {
			
			stopMoving();	
			
			randomDir = Math.random();
			
			if(randomDir < 0.25) 		up = true;
			else if(randomDir < 0.50) 	down = true;
			else if(randomDir < 0.75) 	right = true;
			else left = true;

			waitingTicks = WAITING_TICKS;		
		}
			
		else { 
						
			randomDir = Math.random();
			isMoving = false;
			
			if (randomDir < 0.25) {
				up = true;
				if(canMove(x_Tile, y_Tile - 1)) {
					isMoving = true;
					movingTicks = MOVING_TICKS;	
				}
				else 
					waitingTicks = WAITING_TICKS;				
			}
			
			else if (randomDir < 0.50) {
				down = true;
				if(canMove(x_Tile, y_Tile + 1)) {
					isMoving = true;
					movingTicks = MOVING_TICKS;
				}
				else 
					waitingTicks = WAITING_TICKS;				
			}
			
			else if(randomDir < 0.75) {
				right = true;
				if(canMove(x_Tile + 1, y_Tile)) {
					isMoving = true;
					movingTicks = MOVING_TICKS;
				}
				else 
					waitingTicks = WAITING_TICKS;
			}
			
			else {
				left = true;
				if(canMove(x_Tile - 1, y_Tile)) {
					isMoving = true;
					movingTicks = MOVING_TICKS;
				}
				else 
					waitingTicks = WAITING_TICKS;
			}
		}		
	}

	public void render(Graphics g) {
		
		super.render(g);
		
	}

	

	
}
