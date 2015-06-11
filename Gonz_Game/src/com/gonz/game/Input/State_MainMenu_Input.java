package com.gonz.game.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import com.gonz.game.States.State_MainMenu;
import com.gonz.game.Window.Game_Panel;

public class State_MainMenu_Input implements MouseInputListener, MouseMotionListener, KeyListener {
	
	private State_MainMenu menu;
	
	public State_MainMenu_Input (State_MainMenu stmm) {
		
		menu = stmm;
	}
	
	
	// --------------------------------------- MOUSE INPUT LISTENER -----------------------------------
	
	public void mousePressed(MouseEvent e) {
		
		if(menu.newGame_Box.contains(e.getPoint()))	{		
			menu.setNewGameIndex(State_MainMenu.PRESSED);
			menu.newGame();
		}
		
		else if(menu.load_Box.contains(e.getPoint())) {			
			menu.setLoadIndex(State_MainMenu.PRESSED);
			menu.loadGame();
		}
		
		else if(menu.options_Box.contains(e.getPoint())) {			
			menu.setOptionsIndex(State_MainMenu.PRESSED);
			System.out.println("options()");
		}
		
		else if(menu.quit_Box.contains(e.getPoint())) {			
			menu.setQuitIndex(State_MainMenu.PRESSED);
			menu.exit();
		}
		
		else {
			menu.setNewGameIndex(State_MainMenu.NORMAL);			
			menu.setLoadIndex(State_MainMenu.NORMAL);			
			menu.setOptionsIndex(State_MainMenu.NORMAL);			
			menu.setQuitIndex(State_MainMenu.NORMAL);
		}
		
		e.consume();		
	}

	public void mouseReleased(MouseEvent e) {

		if(menu.newGame_Box.contains(e.getPoint()))			
			menu.setNewGameIndex(State_MainMenu.FOCUSED);
		
		else if(menu.load_Box.contains(e.getPoint()))			
			menu.setLoadIndex(State_MainMenu.FOCUSED);
		
		else if(menu.options_Box.contains(e.getPoint()))			
			menu.setOptionsIndex(State_MainMenu.FOCUSED);
		
		else if(menu.quit_Box.contains(e.getPoint()))			
			menu.setQuitIndex(State_MainMenu.FOCUSED);
		
		else {
			menu.setNewGameIndex(State_MainMenu.NORMAL);			
			menu.setLoadIndex(State_MainMenu.NORMAL);			
			menu.setOptionsIndex(State_MainMenu.NORMAL);			
			menu.setQuitIndex(State_MainMenu.NORMAL);
		}
		
		e.consume();	
	}	
		
	public void mouseClicked(MouseEvent e) {	/* System.out.println("Mouse Clicked!!!");  */	}
	public void mouseEntered(MouseEvent e) {	/* System.out.println("Mouse Entered!!!");  */	}
	public void mouseExited(MouseEvent e) {		/* System.out.println("Mouse Exited!!!");	*/	}
	
	
	
	// --------------------------------------- MOUSE MOTION LISTENER -----------------------------------
	
	public void mouseMoved(MouseEvent e) {
		
		if(menu.newGame_Box.contains(e.getPoint()))			
			menu.setNewGameIndex(State_MainMenu.FOCUSED);
		
		else if(menu.load_Box.contains(e.getPoint()))			
			menu.setLoadIndex(State_MainMenu.FOCUSED);
		
		else if(menu.options_Box.contains(e.getPoint()))			
			menu.setOptionsIndex(State_MainMenu.FOCUSED);
		
		else if(menu.quit_Box.contains(e.getPoint()))			
			menu.setQuitIndex(State_MainMenu.FOCUSED);
		
		else {
			menu.setNewGameIndex(State_MainMenu.NORMAL);			
			menu.setLoadIndex(State_MainMenu.NORMAL);			
			menu.setOptionsIndex(State_MainMenu.NORMAL);			
			menu.setQuitIndex(State_MainMenu.NORMAL);
		}
		
		e.consume();
	}
	public void mouseDragged(MouseEvent e) {	/* System.out.println("Mouse Dragged!!!");	*/}

	// --------------------------------------- KEY LISTENER -----------------------------------

	public void keyPressed(KeyEvent e) {
		
		int keyCode = e.getKeyCode();
		
		switch (keyCode) {
		
		case KeyEvent.VK_S :
		case KeyEvent.VK_DOWN :			
			menu.keyFocusNext();
			break;
		
		case KeyEvent.VK_W :
		case KeyEvent.VK_UP :			
			menu.keyFocusAnt();
			break;
			
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_SPACE:
			menu.keyFocusAction();
			break;	
			
		case KeyEvent.VK_F1:
			Game_Panel.enableDebugScreen();
			break;

		default:
			break;
		}
		
		e.consume();
		
	}

	public void keyReleased(KeyEvent e) {

		
	}

	public void keyTyped(KeyEvent e) {

		
	}
}
