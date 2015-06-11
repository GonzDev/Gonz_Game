package com.gonz.game.Sequence;

import com.gonz.game.Sprites.Entities.Entity;

public class Instruction {
	
	private String instruction;
	
	// Instructions
	public static final String UP = "up";
	public static final String DOWN = "down";
	public static final String LEFT = "left";
	public static final String RIGHT = "right";	
	
	public static final String WAIT = "wait";
	
	public static final String LOOK_UP = "look_up";
	public static final String LOOK_DOWN = "look_down";
	public static final String LOOK_LEFT = "look_left";
	public static final String LOOK_RIGHT = "look_right";	

	
	
	public Instruction(String ins) {
		
		this.instruction = ins;
		
	}
	
	public Instruction getOposite() {
		
		Instruction res;
		
		switch (this.instruction) {
		
		case UP:
			res = new Instruction(DOWN);	
			break;			
		case DOWN:
			res = new Instruction(UP);			
			break;			
		case RIGHT:
			res = new Instruction(LEFT);			
			break;			
		case LEFT:
			res = new Instruction(RIGHT);	
			break;

		default:
			res = null;
			break;			
		}
		
		return res;		
		
	}
	
	public void exec(Entity ent) {
		
		switch (this.instruction) {
		
		case UP:							
			ent.enableUp();
			ent.disableDown();
			ent.disableRight();
			ent.disableLeft();
			ent.restartMovingTicks();
			break;
			
		case DOWN:
			ent.disableUp();
			ent.enableDown();
			ent.disableRight();
			ent.disableLeft();
			ent.restartMovingTicks();
			break;
			
		case RIGHT:			
			ent.disableUp();
			ent.disableDown();		
			ent.enableRight();
			ent.disableLeft();	
			ent.restartMovingTicks();
			break;
			
		case LEFT:			
			ent.disableUp();
			ent.disableDown();			
			ent.disableRight();
			ent.enableLeft();
			ent.restartMovingTicks();
			break;		
			
		case WAIT:
			ent.disableAllDirections();
			ent.restartWaitingTicks();
		
		case LOOK_UP:							
			ent.enableUp();
			ent.disableDown();
			ent.disableRight();
			ent.disableLeft();
			break;
			
		case LOOK_DOWN:
			ent.disableUp();
			ent.enableDown();
			ent.disableRight();
			ent.disableLeft();
			break;
			
		case LOOK_RIGHT:			
			ent.disableUp();
			ent.disableDown();		
			ent.enableRight();
			ent.disableLeft();	
			break;
			
		case LOOK_LEFT:			
			ent.disableUp();
			ent.disableDown();			
			ent.disableRight();
			ent.enableLeft();
			break;		
			
		default:			
			break;			
		}		
		
	}
	
	
	
}
