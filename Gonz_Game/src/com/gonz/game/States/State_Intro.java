package com.gonz.game.States;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.gonz.game.Window.Game_Panel;

public class State_Intro extends State{

	private static final long serialVersionUID = 1L;
	
	// Assets path
	private final String ART_PATH = "/Images/State_Intro/";
	
	// Graphics
	private BufferedImage logo;
	
	private final int FADE_IN = 70;
	private final int LENGTH = 100;
	private final int FADE_OUT = 70;
	
	private int alpha;
	private int ticks;
	
	public State_Intro(Game_Panel gP, State_Manager statman){
		
		super("Intro",gP, statman);
		
	}
	
	public void init() {
		ticks = 0;
		isRunning = true;
		
		try {
			
			logo = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "gonz_logo.png")));
			
		}
		catch(Exception e) {
			System.err.println("Error loading intro image.");
			e.printStackTrace();		
		}			
	}
	
	public void close() {	
		super.close();
	}
	
	public void update() {

		ticks++;
		if(ticks < FADE_IN) {
			alpha = (int) (255 - 255 * (1.0 * ticks / FADE_IN));
			if(alpha < 0) alpha = 0;
		}
		if(ticks > FADE_IN + LENGTH) {
			alpha = (int) (255 * (1.0 * ticks - FADE_IN - LENGTH) / FADE_OUT);
			if(alpha > 255) alpha = 255;
		}
		if(ticks > FADE_IN + LENGTH + FADE_OUT) 
			stateManager.setState(State_Manager.MAIN_MENU_STATE);			
	}

	public void render(Graphics g) {	
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
		g.drawImage(logo, 0, 0, gamePanel.getWidth(), gamePanel.getHeight(), null);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

		g.drawImage(gamePanel.dbImage, 0, 0,this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight(), null);	

	}

}
