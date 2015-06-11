package com.gonz.game.Sprites.Entities;

import java.awt.Graphics;

// import com.gonz.game.Music.SoundPlayer;
import com.gonz.game.Sprites.Signal;
import com.gonz.game.States.State_Play;
import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public class EntityPlayer extends Entity {
	
	// Requested movements
	public boolean reqUp, reqDown, reqRight, reqLeft;
	
	private int collisionTicks; 
	private Dialog dialog;
	
	public EntityPlayer(String name, Tile_World tW) {	
		
		super(name, tW);			
	
		State_Play.setCameraX(	(x_Tile - (State_Play.getCameraW() - Tile.TILE_SIZE) /2) * Tile.TILE_SIZE	);
		State_Play.setCameraY(	(y_Tile - (State_Play.getCameraH() - Tile.TILE_SIZE) /2) * Tile.TILE_SIZE	);
		
		collisionTicks = 0;
		dialog = null;
		isInDialog = false;
		
		// SoundPlayer.loadSound("Collision.wav", SoundPlayer.SOUND_EFFECT_PATH);
		
	}
	
	public void startDialog() {	isInDialog = true;	}

	public void stopDialog() {	
		
		dialog = null;
		
		isInDialog = false;		
		interactTicks = INTERACTION_TICKS;
		
	}

	
	public void update() {
		
		if(waitingTicks > 0)	// The entity has to wait			
			waitingTicks--;
		
		else if(movingTicks > 0) {	
			
			movingTicks--;
			move();
			
			if(interactTicks == 0 && movingTicks == 0)	
				teleport();
			
			if(movingTicks == 0)   
				super.stopMoving();			
			
		}
		
		if(movingTicks == 0) {		
			if(isInSequence )
				sequence.executeSeq();					
			else
				selectDirection();
		}
				
		if(interactTicks > 0 )	interactTicks--;
		if(collisionTicks > 0 )	collisionTicks--;
		
		if(isInDialog && dialog != null)	
			dialog.update();
		
		update_TilePosition();	
		update_Camera();
		
	}
	
	public void render(Graphics g) {
		
		super.render(g);

	}	
	
	public void renderDialog(Graphics g) {		
		if(dialog != null)
			dialog.render(g);		
	}
	
	private void update_Camera() {
		
		State_Play.setCameraX(x_Pix - width/2 - (State_Play.getCameraW() - Tile.TILE_SIZE) /2);
		State_Play.setCameraY(y_Pix - height/2 - (State_Play.getCameraH() - Tile.TILE_SIZE) /2);
		
	}
	
	public void move() {
		
		super.move();
				
	}	
	
	public boolean canMove(int x, int y) {
		
		canMove = super.canMove(x, y);
		
		if(canMove == false && collisionTicks == 0 && !isInDialog) {
			// SoundPlayer.play("Collision.wav");
			collisionTicks = MOVING_TICKS * 3;
		}
		
		return canMove;
		
	}
	
	protected void stopMoving() {
		
		this.reqUp = this.reqDown = this.reqRight = this.reqLeft = false;
		
		super.stopMoving();
	}
	
	protected void selectDirection() {
				
		if(reqUp == true) {
			
			lastImage = pImgUp;
			
			if(canMove(x_Tile, y_Tile - 1)) {
				
				up = true;
				isMoving = true;
				movingTicks = MOVING_TICKS;
													
			}
		}
		
		else if(reqDown == true) {
			
			lastImage = pImgDown;
			
			if(canMove(x_Tile, y_Tile + 1)) {
				
				down = true;
				isMoving = true;
				movingTicks = MOVING_TICKS;
				
			}
		}
			
		else if(reqRight == true) {
			
			lastImage = pImgRight;
			
			if(canMove(x_Tile + 1, y_Tile)) {
				
				right = true;
				isMoving = true;
				movingTicks = MOVING_TICKS;
								
			}
		}
		
		else if(reqLeft == true) {
			
			lastImage = pImgLeft;
			
			if(canMove(x_Tile - 1, y_Tile)) {
				
				left = true;
				isMoving = true;
				movingTicks = MOVING_TICKS;

			}
		}					
	}
	
	public void interact() {
		
		
		if(isInDialog)
			dialog.next();
		
		else if(!isMoving) {
			
			if(lastImage == pImgUp) {
				
				if( world.getSprite(x_Tile, y_Tile - 1) instanceof EntityNPC ) {
					
					EntityNPC ent = (EntityNPC) world.getSprite(x_Tile, y_Tile - 1);
					ent.lastImage = pImgDown;
					this.talkWith(ent);	
					
				}
				
				else if( world.getSprite(x_Tile, y_Tile - 1) instanceof Signal ) {
					
					this.readSignal( (Signal) world.getSprite(x_Tile, y_Tile - 1));
				}
			}
			
			else if(lastImage == pImgDown) {
				
				if( world.getSprite(x_Tile, y_Tile + 1) instanceof EntityNPC ) {
				
					EntityNPC ent = (EntityNPC) world.getSprite(x_Tile, y_Tile + 1);
					ent.lastImage = pImgUp;
					this.talkWith(ent);	
					
				}
				
				else if( world.getSprite(x_Tile, y_Tile + 1) instanceof Signal )
					
					this.readSignal( (Signal) world.getSprite(x_Tile, y_Tile + 1));
			}
			
			else if(lastImage == pImgRight) {
				
				if( world.getSprite(x_Tile + 1, y_Tile) instanceof EntityNPC ) {
				
					EntityNPC ent = (EntityNPC) world.getSprite(x_Tile + 1, y_Tile);
					ent.lastImage = pImgLeft;
					this.talkWith(ent);	
					
				}
				
				else if( world.getSprite(x_Tile + 1, y_Tile) instanceof Signal )
					
					this.readSignal( (Signal) world.getSprite(x_Tile + 1, y_Tile));
			}
			
			else if(lastImage == pImgLeft) {
				
				if( world.getSprite(x_Tile - 1, y_Tile) instanceof EntityNPC ) {
					
					EntityNPC ent = (EntityNPC) world.getSprite(x_Tile - 1, y_Tile);
					ent.lastImage = pImgRight;
					this.talkWith(ent);	
					
				}
				
				else if( world.getSprite(x_Tile - 1, y_Tile) instanceof Signal )
					
					this.readSignal( (Signal) world.getSprite(x_Tile - 1, y_Tile));			
			}			
			
		}		
	}
	
	
	private void readSignal(Signal sign) {
		
		if(interactTicks == 0) {
			isInDialog = true;
			dialog = new Dialog(this, sign);
		}
		
	}
	
	public void readLower() {
		
		if(dialog != null)
			dialog.setNormalSpeed();
		
	}
	
	public void readFaster() {
		
		if(dialog != null)
			dialog.setDoubleSpeed();
		
	}
	
	private void talkWith(EntityNPC ent) {
		
		if(interactTicks == 0) {
			
			this.startDialog();
			ent.startDialog();
		
			dialog = new Dialog(this, ent);
		}
		
	}		
	


}
