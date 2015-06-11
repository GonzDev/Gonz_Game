package com.gonz.game.States;

import java.awt.Canvas;
import java.awt.Graphics;

import com.gonz.game.Window.Game_Panel;

public abstract class State extends Canvas{
    
 	protected static final long serialVersionUID = 1L;
 	
	protected static boolean isRunning = false;    
	
	protected String name;
    public State_Manager stateManager;
    protected Game_Panel gamePanel;         
    
    public State(String s, Game_Panel gP, State_Manager sm){
        name = s;
        gamePanel = gP;
        stateManager = sm;   

        setFocusable(true);
		requestFocus();								// State now receives key events
    }
    
    public boolean isRunning() {	return isRunning;	}
        
    public abstract void init();
    
    public void close() {
    	gamePanel.remove(this);
    	gamePanel.removeAll();
    }
    
    public void stop() {	isRunning = false;	}
    
    public abstract void update();  
    public abstract void render(Graphics g); 

}