package com.gonz.game.Sequence;

import com.gonz.game.Sprites.Entities.Entity;

public class SequenceLinear extends Sequence{
	
	public SequenceLinear(Entity entity) {
		
		super(entity);	
		
	}
	
	public void addInstruction(String ins) {			
		
		super.addInstruction(ins);
		
	}
	
	public void addInstruction(String instruction, int n) {
		
		super.addInstruction(instruction, n);
		
	}
			
	public void executeSeq() {
		
		super.executeSeq();
			
	}
	
	protected void nextInstruction() {
		
		Instruction inst = instructionsQueue.poll();
		
		if(inst == null) {		
			ent.stopSequence();
			ent.disableAllDirections();
		}
		
		else {			
			inst.exec(ent);
		}		
	
	}
	
}
