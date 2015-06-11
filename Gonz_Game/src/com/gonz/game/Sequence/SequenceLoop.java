package com.gonz.game.Sequence;

import java.util.Stack;

import com.gonz.game.Sprites.Entities.Entity;

public class SequenceLoop extends Sequence {

	// Data structure
	private Stack<Instruction> instructionsStack_1;
	private Stack<Instruction> instructionsStack_2;	
	
	// Stuff
	private final int STACK_1 = 0;
	private final int STACK_2 = 1;
	private final int QUEUE = 2;	
	private int flag;	

	public SequenceLoop() {
		
		super();
		
		instructionsStack_1 = new Stack<Instruction>();
		instructionsStack_2 = new Stack<Instruction>();
		flag = QUEUE;
		
	}
	
	public SequenceLoop(Entity entity) {
		
		super(entity);	
		
		instructionsStack_1 = new Stack<Instruction>();
		instructionsStack_2 = new Stack<Instruction>();
		flag = QUEUE;
		
	}

	public void addInstruction(String instruction) {
		
		super.addInstruction(instruction);
		
	}


	public void addInstruction(String instruction, int n) {
		
		super.addInstruction(instruction, n);
		
	}
	
	public void executeSeq() {
		
		super.executeSeq();		
		
	}
	
	

	protected void nextInstruction() {
		
		Instruction inst;
		
		if(flag == STACK_1) {
			
			inst = instructionsStack_1.pop();
			instructionsStack_2.add(inst.getOposite());		
			
			if(instructionsStack_1.isEmpty())
				flag = STACK_2;
			// System.out.println("0");			
		}	
		
		else if(flag == STACK_2){
			
			inst = instructionsStack_2.pop();
			instructionsStack_1.add(inst.getOposite());	
			
			if(instructionsStack_2.isEmpty())
				flag = STACK_1;
			// System.out.println("1");
			
		}
		
		else {			// flag == QUEUE
			
			inst = instructionsQueue.poll();			
			instructionsStack_1.add(inst.getOposite());
			
			if(instructionsQueue.isEmpty())
				flag = STACK_1;
			
			// System.out.println("2");
			
		}
		
		inst.exec(ent);
		
	}

}
