package com.gonz.game.States;

import java.awt.Graphics;

// import com.gonz.game.Sound.JukeBox;
import com.gonz.game.Window.Game_Panel;

public class State_Manager {
	
	public static final int INTRO_STATE = 0;
	public static final int MAIN_MENU_STATE = 1;
	public static final int PLAY_STATE = 2;
	
	private Game_Panel gamePanel;
	private State[] states;
	private int previousState;
	private int currentState;
	
	private boolean isChanging;
	
	// States
	protected State_Intro mainMenu;	
	
	public State_Manager(Game_Panel gP){		
		states = new State[3];
		this.gamePanel = gP;
		isChanging = false;
		initStates();		
	}
	
	public void initStates(){
		// new Thread(new JukeBox()).start();
		
		currentState = INTRO_STATE;
		states[currentState] = new State_Intro(gamePanel, this);
		startCurrentState();
	}
	
	public void changeStateTo(int newState){
		currentState = newState;		
	}
	
	public void startCurrentState(){
		gamePanel.add(states[currentState]);
		states[currentState].init();
	}
	
	public void stopCurrentState(){
		gamePanel.remove(states[currentState]);
		states[currentState].stop();
	}
	
	public void setState(int newState) {
		
		isChanging = true;
		
		previousState = currentState;
		unloadState(previousState);
		currentState = newState;		
		switch (currentState) {
			case INTRO_STATE:
				states[currentState] = new State_Intro(gamePanel, this);	
				startCurrentState();
				break;
			case MAIN_MENU_STATE:
				states[currentState] = new State_MainMenu(gamePanel, this);	
				startCurrentState();
				break;
			case PLAY_STATE:
				states[currentState] = new State_Play(gamePanel, this);	
				startCurrentState();
				break;
			default:
				System.err.println("[WIP]\tState not available :(");
				break;
		}		
		
		isChanging = false;
	}
	
	public void unloadState(int state) {
		states[state].close();
		states[state] = null;		
	}
	
	public void update(){		
		
		if(states[currentState] != null)
			states[currentState].update();	
		
	}
	
	public void render(Graphics g) {
		
		if(states[currentState] != null)
			states[currentState].render(g);	
		
	}
	
	public int currentStateIndex() {	return currentState;	}
	public State currentState() {	return states[currentState];	}
	public boolean isChanging() {	return isChanging;	}
	
}
