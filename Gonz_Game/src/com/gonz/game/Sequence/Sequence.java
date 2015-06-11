package com.gonz.game.Sequence;

import java.util.LinkedList;
import java.util.Queue;

import com.gonz.game.Sprites.Entities.Entity;

public abstract class Sequence {
	
	// Target entity
	protected final double SPEED = Entity.WALKING_SPEED;
	protected Entity ent;
	
	// Data structure
	protected Queue<Instruction> instructionsQueue;
	
	// Others
	protected boolean started;	
		
	public Sequence() {
		
		this.ent = null;		
		this.started = false;
		this.instructionsQueue  = new LinkedList<Instruction>();		
		
	}	
	
	public Sequence(Entity entity) {
		
		this.ent = entity;		
		this.started = false;
		this.instructionsQueue  = new LinkedList<Instruction>();	
		
	}
	
	public void setEntity(Entity ent) {
		this.ent = ent;
	}
	
	public void addInstruction(String instruction) {
		
		switch (instruction) {
		
		case Instruction.UP:
			instructionsQueue.add(new Instruction(Instruction.UP));
			break;
			
		case Instruction.DOWN:
			instructionsQueue.add(new Instruction(Instruction.DOWN));
			break;
			
		case Instruction.RIGHT:
			instructionsQueue.add(new Instruction(Instruction.RIGHT));
			break;
			
		case Instruction.LEFT:
			instructionsQueue.add(new Instruction(Instruction.LEFT));
			break;		
			
		case Instruction.WAIT:
			instructionsQueue.add(new Instruction(Instruction.WAIT));
			break;

		default:
			
			break;			
		}	
		
	}
	
	public void addInstruction(String instruction, int n) {
		
		for(int i = 0; i < n; i++) 			
			addInstruction(instruction);
		
	}
			
	public void executeSeq() {
		
		if(!started) {
			ent.startSequence();
			ent.setMovementSpeed(SPEED);
			started = true;
		}

		else 			
			nextInstruction();
		
	}
	
	protected abstract void nextInstruction();
		
	
	
}
