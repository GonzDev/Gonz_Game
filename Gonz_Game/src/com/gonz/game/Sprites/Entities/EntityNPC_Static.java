package com.gonz.game.Sprites.Entities;

import java.awt.Graphics;

import com.gonz.game.World.Tile_World;

public class EntityNPC_Static extends EntityNPC {
	
	protected final int WAITING_TICKS = super.WAITING_TICKS * 2;
	
	public EntityNPC_Static(String name, Tile_World tW) {	
		
		super(name, tW);
		
		this.moveSpeed = 0;
		
		this.isMoving = false;	
		this.isInSequence = false;
		this.canMove = false;	
		
		isMoving = up = down = right = left = false; 	
				
		world.occupyTile(this.x_Tile, this.y_Tile);	
		world.addSprite(this);
	}
	
	public void update() {
		
		if(isInDialog) 			
			lookAtPlayer();
		
		else if(waitingTicks > 0)	// The entity has to wait				
			waitingTicks--;
		
		else
			selectDirection();
	}
	
	public void move() {		
		// Do nothing, you do not have legs		
	}
	
	protected void selectDirection() {
		
		double randomDir = Math.random();
		if (randomDir < 0.90) {		// The NCP has the fifty percent probability of moving							
			// Wait 5 seconds	
			waitingTicks = WAITING_TICKS;

		}
		
		else if (randomDir < 0.10) {
			
			randomDir = Math.random();
			
			if(randomDir < 0.25) lastImage = pImgUp;
			else if(randomDir < 0.50) 	lastImage = pImgDown;
			else if(randomDir < 0.75) 	lastImage = pImgRight;
			else lastImage = pImgLeft;
			
			waitingTicks = WAITING_TICKS;
		
		}
		
	}
	
	public void render(Graphics g) {
		
		super.render(g);
		
	}
}
