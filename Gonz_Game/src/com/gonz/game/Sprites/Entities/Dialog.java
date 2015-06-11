package com.gonz.game.Sprites.Entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.InputStream;
import java.util.ArrayList;

import com.gonz.game.Sprites.Signal;
import com.gonz.game.Window.Game_Window;

public class Dialog {

	private final int FAST_SPEED = 5;
	private final int SLOW_SPEED = 10;
	
	private EntityPlayer player;
	private EntityNPC entity;
	
	private String text;
	
	private boolean waiting = false;
		
	private Rectangle dialogArea;
	private Rectangle textArea;
	private Polygon nextArrow;	
	
	private String[] words;
	ArrayList<String> lines = new ArrayList<String>();
	private int wordIndex = 0;
	private int lineIndex = 0;
	
	private int ticks = 1;
	private int maxTicks = SLOW_SPEED;
	private int arrowTicks = 0;
	private int maxArrowTicks = 85;
	private int continueTicks = 25;
	
	private int maxY = 0;
	
	private Font textFont;
	private Color bgColor;
	private Color textColor;
	
	public Dialog(EntityPlayer eP, Signal sign) {
		
		player = eP;
		
		text = sign.getText();
		words = text.split(" ");	
		
		createGeometricForms();		
		
		bgColor = new Color(128, 64, 0, 125);
		textColor = Color.BLACK;
		lines.add("");
		
		if(textFont == null)	loadFont();		
		
	}
	
	public Dialog(EntityPlayer eP, EntityNPC ent) {
		
		player = eP;
		entity = ent;
		
		text = ent.getText();
		words = text.split(" ");	
		
		createGeometricForms();
		
		bgColor = new Color(128, 64, 0, 125);
		textColor = Color.BLACK;
		lines.add("");
		
		if(textFont == null)	loadFont();		
		
	}
	
	private void createGeometricForms() {
		
		dialogArea = new Rectangle(0, Game_Window.HEIGHT / 3 * 2, Game_Window.WIDTH, Game_Window.HEIGHT / 3);
		textArea = new Rectangle(dialogArea.x + 30, dialogArea.y + 30, dialogArea.width - 60, dialogArea.height - 60);
		
		int sideSize = 20;
		int p1, p2, p3;		
		
		p1 = textArea.x + textArea.width;
		p2 = p1 - sideSize;
		p3 = p2 + (p1-p2)/2;
		
		int[] vX = {p1, p2, p3};
		
		p1 = textArea.y + textArea.height - 20;
		p2 = p1;
		p3 = p1 + sideSize;
		
		int[] vY = {p1, p2, p3};
		
		nextArrow = new Polygon(vX, vY, 3);
		
		
	}
	
	private void loadFont() {
		
		try {
			
			// Initialize the reader
			InputStream is = getClass().getResourceAsStream("/Fonts/vcr_osd_mono.ttf");
			textFont = Font.createFont(Font.TRUETYPE_FONT, is);
			textFont = textFont.deriveFont(Font.PLAIN, 25);
			
		} catch (Exception e) {			
			System.err.println("[DIALOG]\t\tError reading font");			
		}	
	}
	
	public void setNormalSpeed() {	maxTicks = SLOW_SPEED;	 }
	public void setDoubleSpeed() {	maxTicks = FAST_SPEED;	 }
	
	public void update() {
		
		if(ticks == 0)					
			ticks = maxTicks;
				
		else ticks--;	
		
		if(waiting) {
			
			if(arrowTicks == maxArrowTicks)
				arrowTicks = 0;
			else
				arrowTicks++;	
			
			if(continueTicks > 0)
				continueTicks--;
			
		}	
			
	}
	
	public void render(Graphics g) {
		
		draw(g);
		write(g);		
		
	}
	
	private void draw(Graphics g) {
		
		g.setColor(bgColor);				
		g.fillRect(dialogArea.x, dialogArea.y, dialogArea.width, dialogArea.height);
		
		// g.setColor(new Color(255,175,175,100));
		// g.fillRect(textArea.x, textArea.y, textArea.width, textArea.height);
		
		g.setColor(Color.BLACK);		
		if(waiting && arrowTicks < maxArrowTicks/2) {			
			g.fillPolygon(nextArrow);	
		}				
		g.drawRect(dialogArea.x, dialogArea.y, dialogArea.width - 1, dialogArea.height - 1);		
			
	}
	
	private void read(FontMetrics fm) {		
		
		if(!waiting && ticks == 0 && wordIndex < words.length) {
			
			if(lines.get(lineIndex ) == null)	 lines.add(lineIndex, "");
				
			if(fm.stringWidth(lines.get(lineIndex) + words[wordIndex]) > textArea.width)  {		// That word will be written in the next line	
				
				if(maxY + fm.getHeight() > + textArea.y + textArea.height)		// That line cannot be in this area					
					waiting = true;	
				
				else {
					lines.add(++lineIndex, "");	
					lines.add(lineIndex, lines.get(lineIndex) + words[wordIndex++] + " " ); 
				}
			}
			
			else
				
				lines.add(lineIndex, lines.get(lineIndex) + words[wordIndex++] + " " ); 		
		}
		
		if(wordIndex == words.length)	waiting = true;
		
	}
	
	private void write(Graphics g) {
		
		Font auxFont = g.getFont();
		
		g.setFont(textFont);
		g.setColor(textColor);		
		
		FontMetrics fm = g.getFontMetrics();
		read(fm);
		
		int h = fm.getHeight();
		int a = fm.getAscent();
		int d = fm.getDescent();
		
		int x = textArea.x;
		int y = textArea.y + a - d;
		
		for(int i = 0; i <= lineIndex; i++) {
			
			g.drawString(lines.get(i), x, y);	
			y += h;
			
			maxY = y > maxY ? y : maxY; 
		}
		
		g.setFont(auxFont);
				
	}
	
	public void next() {
		
		if(waiting) {
			
			if(wordIndex >= words.length - 1) {	
				
				player.stopDialog();			
				if(entity != null)	entity.stopDialog();
				
			}
			
			else {
				
				lines = new ArrayList<String>();
				lines.add("");
				lineIndex = 0;		
				maxY = 0;
				waiting = false;
				arrowTicks = 0;
				continueTicks = 25;
				
			}	
		}
		
	}
	
	public void close() {	
		player.stopDialog();
		if(entity != null)
			entity.stopDialog();
	}	
	

}
