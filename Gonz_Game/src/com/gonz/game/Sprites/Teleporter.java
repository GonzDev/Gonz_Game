package com.gonz.game.Sprites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.gonz.game.Music.SoundPlayer;
import com.gonz.game.Sequence.Instruction;
import com.gonz.game.Sequence.SequenceLinear;
import com.gonz.game.Sprites.Entities.Entity;
import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public class Teleporter extends Sprite {
	
	protected final String DATA_PATH = super.DATA_PATH + "Teleporters/";
	
	private int ID, destID;
	private Teleporter destination;		// Teleporter linked with
	private SequenceLinear seq;			
		
	public Teleporter(String name, Tile_World tW) {
		
		super(name, tW);	
		
		this.loadData();
		
		this.x_Pix = x_Tile * Tile.TILE_SIZE;
		this.y_Pix = y_Tile * Tile.TILE_SIZE;

		this.destination = null;		
				
	}

	protected void loadData() {
		
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
				
				if(line.startsWith("ID="))
					this.ID = Integer.parseInt(line.substring("ID=".length()));
				
				else if(line.startsWith("destID="))
					this.destID = Integer.parseInt(line.substring("destID=".length()));					
				
				else if(line.startsWith("x=")) 
					this.x_Tile = Integer.parseInt(line.substring("x=".length()));				
				
				else if(line.startsWith("y=")) 
					this.y_Tile = Integer.parseInt(line.substring("y=".length()));			
				
			}
			
			
		} catch (IOException e) {
			
			System.err.println("[TELEPORTER]\t\tError reading "+ this.name +".dat");
			
		}			
		
	}
	
	public int getID() {	return this.ID;	}
	
	/** Sets the destination the new destination */
	public void setDestination(Teleporter tel) {	this.destination = tel;	}
	
	/** Returns the teleporter which is linked with */
	public Teleporter getDestination() {	return destination;	}
	
	/** Return true if the entity is inside the teleport area */
	public boolean inTeleportArea(Entity ent) {

		return ent.getTileLocation().equals(this.getTileLocation());			
		
	}
	
	private void createSequence(Entity ent) {
	
		seq = new SequenceLinear(ent);	
		
		if(ent.canMove(ent.getXTile(), ent.getYTile() + 1)) {
			seq.addInstruction(Instruction.LOOK_DOWN);
			seq.addInstruction(Instruction.WAIT);
			seq.addInstruction(Instruction.DOWN);
		}

		else if(ent.canMove(ent.getXTile() + 1, ent.getYTile())) {
			seq.addInstruction(Instruction.LOOK_RIGHT);
			seq.addInstruction(Instruction.WAIT);
			seq.addInstruction(Instruction.RIGHT);
		}

		else if(ent.canMove(ent.getXTile() - 1, ent.getYTile())) {
			seq.addInstruction(Instruction.LOOK_LEFT);
			seq.addInstruction(Instruction.WAIT);
			seq.addInstruction(Instruction.LEFT);
		}

		else if(ent.canMove(ent.getXTile(), ent.getYTile() - 1)) {
			seq.addInstruction(Instruction.LOOK_UP);
			seq.addInstruction(Instruction.WAIT);
			seq.addInstruction(Instruction.UP);
		}

	}
	
	/** Teleports the entity to the linked Teleport */
	public void teleport(Entity ent) {
		
		if(destination == null) {		// Destination does not exists, let's find it
			boolean found = false;
			for(int i = 0; i < world.getTeleportersList().size() && !found; i++)
				
				if(world.getTeleportersList().get(i).getID() == destID) {
					
					destination = world.getTeleportersList().get(i);
					found = true;
					
				}
			
		}
		
		if(destination != null) {
			
			SoundPlayer.play("Teleport.wav");
			
			ent.setXPix(destination.getXPix() + ent.getWidth()/2);
			ent.setYPix(destination.getYPix() + ent.getHeight()/2);			
			
			ent.update_TilePosition();
			
			createSequence(ent);
			ent.setSequence(seq);

		}
		
	}


}
