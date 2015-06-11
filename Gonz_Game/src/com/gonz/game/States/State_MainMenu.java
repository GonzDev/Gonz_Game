package com.gonz.game.States;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.gonz.game.Input.State_MainMenu_Input;
import com.gonz.game.Music.SoundPlayer;
import com.gonz.game.Window.Game_Panel;

public class State_MainMenu extends State {
	
	private static final long serialVersionUID = 1L;
	
	// Assets path
	private final String ART_PATH = "/Images/State_MainMenu/";
	
	private static final int SEPARATION = 30;
	public static final int NORMAL = 0;
	public static final int FOCUSED = 1;
	public static final int PRESSED = 2;
	
	// Background
	private BufferedImage background;	
	
	// Buttons
	private BufferedImage newGame_S, newGame_M, newGame_L;
	private BufferedImage[] newGame;
	public int newGame_Index;
	public Rectangle newGame_Box;
		
	private BufferedImage load_S, load_M, load_L;
	private BufferedImage[] load;
	public int load_Index;
	public Rectangle load_Box;
	
	private BufferedImage options_S, options_M, options_L;
	private BufferedImage[] options;
	public int options_Index;
	public Rectangle options_Box;
	
	private BufferedImage quit_S, quit_M, quit_L;
	private BufferedImage[] quit;
	public int quit_Index;
	public Rectangle quit_Box;
	
	// Focus
	private int numButtons;
	private int keyFocusIndex;
	
	// Input
	private State_MainMenu_Input input;
	
	public State_MainMenu(Game_Panel gP, State_Manager statman) {
		
		super("Play", gP, statman);		
			
	}
	
	public void setNewGameIndex(int index) {	this.newGame_Index = index;		}
	public void setLoadIndex(int index) {		this.load_Index = index;		}
	public void setOptionsIndex(int index) {	this.options_Index = index;		}
	public void setQuitIndex(int index) {		this.quit_Index = index;		}
	
	public void init() {
		
		long before = System.currentTimeMillis();
		
		loadImages();
		loadSounds();
		
		numButtons = 0;
		keyFocusIndex = 0;
		
		newGame = new BufferedImage[3];
		newGame[0] = newGame_S;
		newGame[1] = newGame_M;
		newGame[2] = newGame_L;
		newGame_Index = NORMAL;
		newGame_Box = new Rectangle(gamePanel.getWidth() - newGame[newGame_Index].getWidth() - SEPARATION * 2,  SEPARATION * 2, newGame_S.getWidth(), newGame_S.getHeight());
		numButtons++;
		
		load = new BufferedImage[3];
		load[0] = load_S;
		load[1] = load_M;
		load[2] = load_L;
		load_Index = NORMAL;
		load_Box = new Rectangle(gamePanel.getWidth() - load[load_Index].getWidth() - SEPARATION * 2, newGame_Box.y + newGame_Box.height + SEPARATION, load_S.getWidth(), load_S.getHeight());
		numButtons++;
		
		options = new BufferedImage[3];
		options[0] = options_S;
		options[1] = options_M;
		options[2] = options_L;
		options_Index = NORMAL;
		options_Box = new Rectangle(gamePanel.getWidth() - options[options_Index].getWidth() - SEPARATION * 2, load_Box.y + load_Box.height + SEPARATION, options_S.getWidth(), options_S.getHeight());
		numButtons++;
		
		quit = new BufferedImage[3];
		quit[0] = quit_S;
		quit[1] = quit_M;
		quit[2] = quit_L;
		quit_Index = NORMAL;
		quit_Box = new Rectangle(gamePanel.getWidth() - quit[quit_Index].getWidth() - SEPARATION * 2, options_Box.y + options_Box.height + SEPARATION, quit_S.getWidth(), quit_S.getHeight());
		numButtons++;
		
		input = new State_MainMenu_Input(this);
		
		gamePanel.addMouseListener(input);
		gamePanel.addMouseMotionListener(input);
		gamePanel.addKeyListener(input);
		
		long after = System.currentTimeMillis();		
		
		System.out.println("[STATE_MAIN_MENU]\tGUI created in " + (after - before) + " millis");
		
	}
	
	private void loadImages() {
		
		long before = System.currentTimeMillis();
		
		try{
			
			background = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH +"Background.png")));
			
			newGame_S = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "NewGame_S.png")));
			newGame_M = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "NewGame_M.png")));
			newGame_L = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "NewGame_L.png")));
			
			load_S = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Load_S.png")));
			load_M = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Load_M.png")));
			load_L = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Load_L.png")));
			
			options_S = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Options_S.png")));
			options_M = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Options_M.png")));
			options_L = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Options_L.png")));
			
			quit_S = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Quit_S.png")));
			quit_M = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Quit_M.png")));
			quit_L = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + "Quit_L.png")));
						
		} catch (Exception e) {			
			
		}
		
		long after = System.currentTimeMillis();
		System.out.println("[STATE_MAIN_MENU]\tImages loaded in " + (after - before) + " millis");
		
	}
	
	private void loadSounds() {
		
		long before = System.currentTimeMillis();

		SoundPlayer.loadSound("Select.wav", SoundPlayer.SOUND_EFFECT_PATH);
	
		long after = System.currentTimeMillis();			
		System.out.println("[STATE_MAIN_MENU]\tSounds loaded in " + (after - before) + " millis");
		
	}
	
	public void close() {
		
		gamePanel.removeMouseListener(input);
		gamePanel.removeMouseMotionListener(input);
		gamePanel.removeKeyListener(input);
		
		super.close();
		
	}
	
	/** Update every component of the game */
	public void update(){
		
	}	
	
	
	/** Draws the game into a buffer for next screen drawing */
	public void render(Graphics g){		
		
		g.drawImage(background, 0, -20, null);		
		g.drawImage(newGame[newGame_Index], newGame_Box.x, newGame_Box.y, null);
		g.drawImage(load[load_Index], load_Box.x, load_Box.y, null);
		g.drawImage(options[options_Index], options_Box.x, options_Box.y, null);
		g.drawImage(quit[quit_Index], quit_Box.x, quit_Box.y, null);
				
	}	
	
	public void keyFocusAnt() {
		if(keyFocusIndex <= 0)
			keyFocusIndex = numButtons-1;
		else
			keyFocusIndex--;
		
		focus();
	}	
	
	public void keyFocusNext() {	
		if(keyFocusIndex < 0 || keyFocusIndex == numButtons - 1)
			keyFocusIndex = 0;
		else
			keyFocusIndex++;	
		
		focus();
	}
	
	private void focus() {
		
		switch (keyFocusIndex) {
		
		case 0:
			newGame_Index = FOCUSED;
			load_Index = options_Index = quit_Index = NORMAL;
			break;
		case 1: 
			load_Index = FOCUSED;
			newGame_Index = options_Index = quit_Index = NORMAL;
			break;
		case 2:
			options_Index = FOCUSED;
			newGame_Index = load_Index = quit_Index = NORMAL;
			break;
		case 3:
			quit_Index = FOCUSED;
			newGame_Index = load_Index = options_Index = NORMAL;
			break;
			
		default:
			break;
		}
		
	}
	
	public void keyFocusAction() {
		
		switch (keyFocusIndex) {
		
		case 0:
			newGame();
			break;
		case 1: 
			loadGame();
			break;
		case 2:
			System.out.println("options()");
			break;
		case 3:
			exit();
			break;
			
		default:
			break;
		}
		
	}
	
	
	public void newGame() {		
		
		SoundPlayer.play("Select.wav");
		
		try {	Thread.sleep(500L);		} catch (InterruptedException e) {	e.printStackTrace();	}
		
		stateManager.setState(State_Manager.PLAY_STATE);
		
	}
	
	public void loadGame() {
		
		SoundPlayer.play("Select.wav");
		System.out.println("loadGame()");
		
	}
	
	public void exit() {
		
		Game_Panel.stopGame();
		
	}
	
}

