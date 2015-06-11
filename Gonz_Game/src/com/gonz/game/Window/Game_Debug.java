package com.gonz.game.Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;

import com.gonz.game.States.State_Play;

public class Game_Debug {
	
	// CONSTANTS
	private final int SCREEN_MARGIN = 32;	
	private final int LABEL_MARGIN = 5;
	private final int LABEL_VERTICAL_SEPARATION = 5;
	private final int LABEL_HORIZONTAL_SEPARATION = 10;
	
	// FPS and UPS
	private int fps; 
	private int ups;
	
	// Memory Analysis (we need to convert bytes into megabytes)
	private long usedMemory;
	private long freeMemory;
	private long allocatedMemory;
	private long maxMemory;
	
	// Operating System
	private String osName;
	private String osVersion;
	private String osArch;
	
	// Java 
	private String javaVersion;
	private String javaVMArch;
	private String javaVendor;
	
	// Graphics
	private Font font;
	private Color fontColor;
	private Rectangle label;
	private Color labelColor;
	
	// Other
	private Game_Panel gamePanel;
	
	
	/** Constructor */
	public Game_Debug(Game_Panel gP) {
	
		gamePanel = gP;		
		maxMemory = bytesToMegabytes (Runtime.getRuntime().maxMemory());
		
		systemAnalysis();
				
		try {			
			// Initialize the reader
			InputStream is = getClass().getResourceAsStream("/Fonts/Pixel.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.PLAIN, 12);			
		} catch (Exception e) {			
			System.err.println("[GAME_DEBUG]\t\tError reading font");			
		}
						
		fontColor = Color.BLACK;
		labelColor = new Color (192, 192, 192 , 125);
			
	}
	
	
	/** Get relevant datas about operating system  */
	private void systemAnalysis() {
		
		osName = System.getProperty("os.name");
		osVersion = System.getProperty("os.version");
		osArch = System.getProperty("sun.arch.data.model");
		
		javaVersion = System.getProperty("java.version");
		javaVMArch = System.getProperty("os.arch");
		javaVendor = System.getProperty("java.vendor");
		
	}
	
	
	public void update() {
		
		fps = getFPS();
		ups = getUPS();
		
		runtimeAnalysis();
		
	}
	
	
	private int getFPS() {	return gamePanel.getActualFPS();	}
	private int getUPS() {	return gamePanel.getActualUPS();	}
	
	
	/** Get and set the current state of memory */
 	private void runtimeAnalysis() {
		
		Runtime rt = Runtime.getRuntime();
		
		allocatedMemory = bytesToMegabytes(rt.totalMemory());
		freeMemory = bytesToMegabytes(rt.freeMemory());
		usedMemory = allocatedMemory - freeMemory;		
		
	}
	
 	
	/** Converts Bytes to MegaBytes */
	private long bytesToMegabytes(long bytes) {		
		return bytes / (1024L * 1024L);				
	}
	
	
	public void render(Graphics g) {
		
		String text = "";		
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();		
		int fontHeight = fm.getAscent() - fm.getDescent();
		
		// Fps info
		text = "FPS: "+ fps;		
		label = new Rectangle(SCREEN_MARGIN, SCREEN_MARGIN, fm.stringWidth(text) + LABEL_MARGIN * 2, fontHeight + LABEL_MARGIN * 2);
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// Ups info
		text = "UPS: "+ ups;	
		label.x += label.width + LABEL_HORIZONTAL_SEPARATION;	
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// Operating System architecture
		text = "x" + osArch;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x = Game_Window.WIDTH - SCREEN_MARGIN - label.width;		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// Operating System version
		text = "v" + osVersion;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// Operating System name
		text = osName;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// OPERATING SYSTEM		
		text = "OPERATING SYSTEM:";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);		
		
		label.y += label.height + LABEL_VERTICAL_SEPARATION; 		
		
		if(gamePanel.statMan.currentState() instanceof State_Play) {
			
			State_Play statePlay = (State_Play) gamePanel.statMan.currentState();
			
			
			// PLAYER LOCATION (pixel):
			text = "Player location (pixel):";
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
			label.x = SCREEN_MARGIN;			
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		
			// player.x
			text = "x: "+ (int) statePlay.world.player.getXPix();
			label.x += label.width + LABEL_HORIZONTAL_SEPARATION;
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;		
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		
			// player.y
			text = "y: "+ (int) statePlay.world.player.getYPix();
			label.x += label.width + LABEL_HORIZONTAL_SEPARATION;
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		}
		
		// JAVA architecture
		text = javaVMArch;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x = Game_Window.WIDTH - SCREEN_MARGIN - label.width;		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// JAVA version	
		text = javaVersion;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// JAVA
		text = "JAVA:";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		label.y += label.height + LABEL_VERTICAL_SEPARATION; 
		
		if(gamePanel.statMan.currentState() instanceof State_Play) {
			
			State_Play statePlay = (State_Play) gamePanel.statMan.currentState();
			
			// PLAYER LOCATION (tile):
			text = "Player location (tile):";
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
			label.x = SCREEN_MARGIN;	
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);			
		
		
			// player.x_tile
			text = "x_tile: "+ statePlay.world.player.getXTile();
			label.x += label.width + LABEL_HORIZONTAL_SEPARATION;
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;		
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
				
			// player.y_tile
			text = "y_tile: "+ statePlay.world.player.getYTile();
			label.x += label.width + LABEL_HORIZONTAL_SEPARATION;
			label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
			g.setColor(labelColor);
			g.fillRect(label.x, label.y, label.width, label.height);
			g.setColor(fontColor);
			g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		}		
		
		// JAVA vendor
		text = javaVendor;
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x = Game_Window.WIDTH - SCREEN_MARGIN - label.width;		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		label.y += (label.height + LABEL_VERTICAL_SEPARATION);
		
		// Used Memory
		text = (usedMemory*100/maxMemory) +"% - "+ usedMemory +"/"+ maxMemory +" MB";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x = Game_Window.WIDTH - SCREEN_MARGIN - label.width;		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// USED MEMORY:
		text = "Used memory:";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
		// Allocated Memory
		text = (allocatedMemory*100/maxMemory) +"% - "+ allocatedMemory +"/"+ maxMemory +" MB";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.y += label.height + LABEL_VERTICAL_SEPARATION;
		label.x = Game_Window.WIDTH - SCREEN_MARGIN - label.width;		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
				
		// ALLOCATED MEMORY:
		text = "Allocated memory:";
		label.width = fm.stringWidth(text) + LABEL_MARGIN * 2;
		label.x -=  (LABEL_HORIZONTAL_SEPARATION/2 + label.width);		
		g.setColor(labelColor);
		g.fillRect(label.x, label.y, label.width, label.height);
		g.setColor(fontColor);
		g.drawString(text, label.x + LABEL_MARGIN, label.y + LABEL_MARGIN + fontHeight);
		
	}	

}
