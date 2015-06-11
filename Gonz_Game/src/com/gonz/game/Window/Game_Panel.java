package com.gonz.game.Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import com.gonz.game.States.State_Manager;

public class Game_Panel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	/* Number of frames with a delay of 0ms before the animation thread yields to
	 * the other running threads */
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	/* Number of frames that can be skipped in any one animation loop. The game state
	 * is updated but not rendered */
	private static int MAX_FRAME_SKIPS = 5;
	
	/* Used for gathering statistics	*/
	private static long MAX_STATS_INTERVAL = 1000000000L;		// Record statistics every 1 second (roughly)
	
	private int statsCounter= 0;
	private long statsInterval = 0L;	// In seconds
	
	private int frameCount = 1;	// FPS
	private int actualFPS = 0;
	private int maxFPS = 0;
	private double averageFPS = 0.0;
	
	private int updateCount = 1;	// UPS	
	private int actualUPS = 0;
	private int maxUPS = 0;
	private double averageUPS = 0.0;
	
	private long framesSkipped = 0L;
	private long maxFramesSkipped = 0L;	
	
	/* Panel states */
	private Thread animator;								// For the animation
	private volatile static boolean running = false;			// Stops the animation	
	private volatile boolean isPaused = false;		
	private volatile boolean gameOver = false;				// For game termination
	
	/* Panel size */
	private int pWidth;
	private int pHeight;
	private Game_Window frame; 
	
	/* Global variables for off-screen rendering */
	private Graphics dbg;
	public Image dbImage = null;
	
	private long period;			// Period between drawing in ms	
	public State_Manager statMan;	// Who want to play?
	
	public Game_Debug gDebug;
	public static boolean debugScreen;
	
	public Game_Panel(Game_Window win, long period, int width, int height, Game_Window f) {
		
		this.period = period;
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension scrDim = tk.getScreenSize();
		setPreferredSize(scrDim);
		
		this.frame = f;
		this.pWidth = width;
		this.pHeight = height;
			
		setBackground(Color.white);					// White Background
			
		setFocusable(true);
		requestFocus();								// JPanel now receives key events

		// Create game components
		statMan = new State_Manager(this);
		
		gDebug = new Game_Debug(this);
		debugScreen = false;
	}
	
	public int getWidth() {		return pWidth;	}
	public int getHeight() {	return pHeight;	}
	
	public int getActualFPS() {	return this.actualFPS;	}
	public int getActualUPS() {	return this.actualUPS;	}
	
	public static void enableDebugScreen(){ debugScreen = debugScreen ? false : true; }
	
	/** Wait for the JPanel to be added to the JFrame/JApplet before starting. */
	public void addNotify() {		
		super.addNotify();		// Creates the peer
		startGame();			// Start the thread
	}
	
	private void startGame() {
		// Initialize and start the thread
		if(animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	/** Sets the state to "paused" */
	public void pauseGame() {	isPaused = true;	}
	/** Resume the game execution */
	public void resumeGame() {	isPaused = false;	}
	/** Finishes the game execution */	
	public static void stopGame() {	running = false;	}
	/** isPaused getter*/
	public boolean isPaused() {	return isPaused;	}
		
	/** Repeatedly update, render, sleep so loop takes close to
	 * 	period milliseconds. Overruns in update/renders will cause extra updates
	 * 	to be carried out so UPS ~= requested FPS */
	public void run() {
		
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		
		beforeTime = System.nanoTime();
		
		running = true;		
		while(running) {
			
			while(statMan.isChanging())
				Thread.yield();;
			
			gameUpdate();	// GameState is updated
			gameRender();	// Render to a buffer
			paintScreen();	// Draw buffer to screen				
						
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;		// Time left in this loop			
			
			if (sleepTime > 0) { 	// Some time left in this cycle
						
				try {
					
					Thread.sleep(sleepTime/1000000L); 	// Nanoseconds -> Milliseconds

				}catch(InterruptedException ex){
					
					System.err.println("[GAME_PANEL]\t\tThread sleep interrupted");
					
				}
				
				overSleepTime = System.nanoTime() - afterTime - sleepTime; 
			}
			
			else {				// sleepTime <= 0; frame took longer than the period
				excess -= sleepTime;		// Store excess time value
				overSleepTime = 0L;
				
				if(++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();			// Give another thread a chance to run
					noDelays = 0;
				}
			}			
			beforeTime = System.nanoTime();

			/* If frame animation is taking too long, update the game state	without rendering it, 
			 * to get the updates/second nearer to	the required FPS. */
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				
				excess -= period;
				gameUpdate();			// Update state but don't render
				skips++;
				
			}
			
			framesSkipped += skips;			
			storeStats();
		}
		printStats();
		System.exit(0); 	// So enclosing JFrame/JApplet exits
	}
	
	/** Update every component of the game */
	private void gameUpdate(){
		
		if (!gameOver) {
			
			updateCount++;
			statMan.update();	
			
			if (debugScreen)
				
				gDebug.update();
			
		}
		
	}
	
	/** Draws the game into a buffer for next screen drawing */
	private void gameRender(){
		
		frameCount++;
		// draw the current frame to an image buffer
		if(dbImage == null) {	// Create the buffer
			dbImage = createVolatileImage(pWidth, pHeight);
			if(dbImage == null){
				System.err.println("dbImage is null");
				return ;
			}
			else 
				dbg = dbImage.getGraphics();
		}
			
		// Clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, pWidth, pHeight);		
			
		statMan.render(dbg);
		
		if (debugScreen)
			
			gDebug.render(dbg);

	}
	
	/** Now, print what buffer contains in the screen */
	private void paintScreen() {
		// Actively render the buffer image to the screen
		Graphics g;
		try  {
			
			g = this.getGraphics();					// Get the panel's graphic context
			
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			
			Toolkit.getDefaultToolkit().sync();			// Sync the display on some systems
			
			g.dispose();
			
		} catch (Exception e) {	System.err.println("Graphics context error: " + e);		}
	}
	
	public void pack() {
		frame.pack();
	}
	
	private void storeStats() {
		
		statsInterval += period;
		
		if(statsInterval >= MAX_STATS_INTERVAL) {
			
			statsCounter++;
			
			actualFPS = frameCount;		// Calculate the latest FPS and UPS
			actualUPS = updateCount;
			
			maxFPS += actualFPS;
			maxUPS += actualUPS;
			maxFramesSkipped += framesSkipped;
			
			averageFPS = (double) maxFPS / statsCounter;
			averageUPS = (double) maxUPS / statsCounter;
						
			frameCount = 1;
			updateCount = 1;
			framesSkipped = 0;
			
			frame.setTitle(Game_Window.GAME_NAME + " - FPS: " + actualFPS);
			
			statsInterval = 0;

		}

	}

	
	private void printStats() {
		
		System.out.println();
		System.out.println("Average FPS:\t\t" + averageFPS);
		System.out.println("Average UPS:\t\t" + averageUPS);
		System.out.println("Total Frames Skipped:\t" + maxFramesSkipped);
		
	}
	
}
