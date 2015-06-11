package com.gonz.game.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.gonz.game.Sprites.Entities.EntityPlayer;
import com.gonz.game.States.State_Play;
import com.gonz.game.Window.Game_Panel;
import com.gonz.game.World.Tile_World;

public class State_Play_Input implements KeyListener {
	
	private State_Play play;
	private Tile_World world;
	private EntityPlayer player;
	
	public State_Play_Input(State_Play statply) {
		
		this.play = statply;
		this.world = play.world;
		this.player = world.player;	
		
	}
	
	public void keyPressed(KeyEvent e) {
		
		if(player.isInSequence()) return ;
			
		int key = e.getKeyCode();
		
		switch(key) {
			
		case KeyEvent.VK_W:		
			if(!player.isInDialog())
				player.reqUp = true;
			break;
			
		case KeyEvent.VK_S:
			if(!player.isInDialog())
			player.reqDown = true;
			break;
			
		case KeyEvent.VK_D:
			if(!player.isInDialog())
				player.reqRight = true;
			break;
			
		case KeyEvent.VK_A:
			if(!player.isInDialog())
				player.reqLeft = true;
			break;	
				
		case KeyEvent.VK_SPACE:
			player.interact();
			if(player.isInDialog())
				player.readFaster();
			break;
				
		case KeyEvent.VK_F1:
			Game_Panel.enableDebugScreen();
			break;
				
		case KeyEvent.VK_ESCAPE:
			Game_Panel.stopGame();
			break;
		}
				
	}

	public void keyReleased(KeyEvent e) {
		
		if(player.isInSequence()) return ;
		
		int key = e.getKeyCode();
			
			switch(key){
			
			case KeyEvent.VK_W:				
				player.reqUp = false;
				break;
				
			case KeyEvent.VK_S:
				player.reqDown = false;
				break;
				
			case KeyEvent.VK_D:
				player.reqRight = false;
				break;
				
			case KeyEvent.VK_A:
				player.reqLeft = false;
				break;
				
			case KeyEvent.VK_SPACE:				
				if(player.isInDialog())
					player.readLower();
				
				break;
		
			}
		
	}

	public void keyTyped(KeyEvent e) {
		
	
	}

}
