package com.gonz.game.Window;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;

import com.gonz.game.Music.SoundPlayer;

public class Game_Window extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_FPS = 80;
	
	public static final String GAME_NAME = "Gonzo's Game";
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 608;	
	
	private Game_Panel gamePan;
	
	/** An almost full-screen (AFS) mode */
	public Game_Window(long period) {
		
		super(GAME_NAME);
		
		SoundPlayer.init();
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		
		gamePan = new Game_Panel(this, period, WIDTH, HEIGHT, this);		
		c.add(gamePan,"Center");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		setLocation(dim.width/2-WIDTH/2, dim.height/2-HEIGHT/2);
		
		setUndecorated(false);		// No borders or titlebar
		setIgnoreRepaint(true);		// Turn off paint events since doing active rendering
		pack();

		setResizable(false);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {			
			public void windowClosing(WindowEvent e) {				 
				Game_Panel.stopGame();				
			}			
		});

	}
	
	public static void main(String[] args) {
		
		int fps = DEFAULT_FPS;
		
		if (args.length != 0)
			fps = Integer.parseInt(args[0]);
		
		long period = (long) 1000.0/fps;
		System.out.println("[GAME_WINDOW]\t\tFPS: " + fps + "; PERIOD: " +period+ " milliseconds");
		
		new Game_Window(period*1000000L); // ms --> nanosecs	
	}


}
