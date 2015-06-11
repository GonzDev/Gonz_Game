package com.gonz.game.Sprites.Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import com.gonz.game.Sequence.SequenceLinear;
import com.gonz.game.Sprites.Sprite;
import com.gonz.game.Sprites.Teleporter;
import com.gonz.game.States.State_Play;
import com.gonz.game.World.Tile;
import com.gonz.game.World.Tile_World;

public abstract class Entity extends Sprite{

	// Entities data path
	protected final String DATA_PATH = super.DATA_PATH + "Entities/";
	protected final String ART_PATH = super.ART_PATH + "Entities/";
	
	// Ticks
	public static final int INTERACTION_TICKS = 50;	// 1 secs aprox for 85 ups
	protected final int MOVING_TICKS = 25;			// 20 * 1.6 (DEFAULT SPEED) = 32 pixels = 1 tile

	protected int interactTicks = INTERACTION_TICKS;
	protected int movingTicks = 0;
	protected int waitingTicks = 0;
	
	// Entity's direction
	protected boolean up, down, left, right;
	protected boolean isTeleporting;
	protected boolean isMoving, canMove;
	protected boolean isInSequence;
	
	// Sequences
	protected SequenceLinear sequence = null;
	
	// Animation movement
	protected int aniFrame = 0;
	protected int aniTime = 10;
	protected int aniDelta = 0;
		
	// Images and frames for rendering	
	protected int[][] pImgDown = 	{{0,0},{1,0},{2,0},{3,0}};
	protected int[][] pImgLeft = 	{{0,1},{1,1},{2,1},{3,1}};
	protected int[][] pImgRight = 	{{0,2},{1,2},{2,2},{3,2}};
	protected int[][] pImgUp = 		{{0,3},{1,3},{2,3},{3,3}};
	
	protected BufferedImage entityTileSet;
	protected int[][] lastImage = pImgDown;
	
	// Others
	public static final double WALKING_SPEED = 1.28;
	protected double moveSpeed;
	protected String tileSetName;
	protected boolean isInDialog;	
	
	
	
	
	// CONSTRUCTORS
	
	public Entity(String name, Tile_World tW){
		
		super(name, tW);
		
		this.loadData();			
		
		this.x_Pix = this.x_Tile * Tile.TILE_SIZE + width/2;
		this.y_Pix = this.y_Tile * Tile.TILE_SIZE + height/2;
				
		this.moveSpeed = WALKING_SPEED;
		
		this.isMoving = false;	
		this.isInSequence = false;
		this.canMove = true;		
		this.isInDialog = false;
		
		this.update_TilePosition();
				
	}
	
	
	
	
	// GETTERS AND SETTERS
	
	public String getName() { return this.name; }
	
	public int getWidth() {		return width;	}
	public int getHeight() {	return height;	}	
			
	public boolean isUp() {		return up;		}
	public boolean isDown() {	return down;	}
	public boolean isLeft() {	return left;	}
	public boolean isRight(){	return right;	}
	
	public boolean isMoving() { 	
		this.isMoving = up || down || right || left ? true : false; 
		return isMoving;
	}
	
	protected void stopMoving() {	
		this.isMoving = this.up = this.down = this.right = this.left = false;
	}
	
	public void startSequence() {	this.isInSequence = true; disableAllDirections();	}
	public void stopSequence() {	this.isInSequence = false;	}
	public boolean isInSequence() {	return this.isInSequence;	}

	public void enableUp()	{		this.up = true;	}
	public void enableDown()	{	this.down = true;	}
	public void enableRight()	{	this.right = true;	}
	public void enableLeft()	{	this.left = true;	}
	
	public void disableUp()	{		this.up = false;	}
	public void disableDown()	{	this.down = false;	}
	public void disableRight()	{	this.right = false;	}
	public void disableLeft()	{	this.left = false;	}
	
	public void disableAllDirections() {	stopMoving();	}	
	
	public void setFacingUp() {
		stopMoving();
		lastImage = pImgUp;
	}
	
	public void setFacingDown() {
		stopMoving();
		lastImage = pImgDown;
	}

	public void setFacingRight() {
		stopMoving();
		lastImage = pImgRight;
	}

	public void setFacingLeft() {
		stopMoving();
		lastImage = pImgLeft;
	}
	
	public boolean isFacingUp() {	return lastImage == pImgUp;	}
	public boolean isFacingDown() {	return lastImage == pImgDown; }
	public boolean isFacingRight() {	return lastImage == pImgRight; }
	public boolean isFacingLeft() {	return lastImage == pImgLeft; }
	
	public void restartMovingTicks() {	this.movingTicks = MOVING_TICKS;	}	
	public void restartWaitingTicks() {	this.waitingTicks = INTERACTION_TICKS;	}
	
	public void setMovementSpeed(double speed) { this.moveSpeed = speed;	}

	public void setSequence(SequenceLinear seq) {
	
		disableAllDirections();
		
		this.sequence = seq;	
		this.sequence.executeSeq();	
		isInSequence = true;
		
		movingTicks = MOVING_TICKS;
		
	}
	
	public abstract void startDialog();
	public abstract void stopDialog();
	public boolean isInDialog() {	return this.isInDialog;	}
	
	
	public boolean equals(Object o) {		
		if(o instanceof Entity) {			
			Entity other = (Entity) o;
			if(this.name.equals(other.name))
				return true;			
		}
		
		return false;		
	}
	
	public String toString() {
		String res = "";
		res += this.name;
		res += " (" + this.x_Tile + ", " + this.y_Tile + ")";		
		return res;
		/* For example: Cristina (17, 12) */		
	}
 	
	
	
	
	//	LOADERS
	
	protected void loadData() {
		
		try {
			
			// Initialize the reader
			InputStream in = getClass().getResourceAsStream(DATA_PATH + this.name + ".dat");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			// Read every file line
			while(true) {
				
				String line = reader.readLine();
				
				if(line == null) {		// Reader is at end
					
					reader.close();
					break;
					
				}
				
				// Compare and save the attribute
				
				if(line.startsWith("name=")) {
					this.name = line.substring("name=".length());	
					//System.out.println(this.name);
				}
				
				else if(line.startsWith("width=")) {
					this.width = Integer.parseInt(line.substring("width=".length()));
					//System.out.println(this.width);
				}
					
				else if(line.startsWith("height=")){
					this.height = Integer.parseInt(line.substring("height=".length()));
					//System.out.println(this.height);
				}
					
				else if(line.startsWith("x_Tile=")) {
					this.x_Tile = Integer.parseInt(line.substring("x_Tile=".length()));
					if(this.x_Tile == 0)
						this.x_Tile = world.getInitX();
					//System.out.println(this.x_Tile);
				}
				
				else if(line.startsWith("y_Tile=")) {
					this.y_Tile = Integer.parseInt(line.substring("y_Tile=".length()));
					if(this.y_Tile == 0)
						this.y_Tile = world.getInitY();
					//System.out.println(this.y_Tile);
				}
					
				else if(line.startsWith("tileset=")) {
					this.tileSetName = line.substring("tileset=".length());			
					//System.out.println(this.tileSetName);
				}
				
			}
			
			
		} catch (IOException e) {
			
			System.err.println("[ENTITY]\t\tError reading "+ name +".dat");
			
		}	
		
		this.loadImage();
		
	}
	
	protected void loadImage(){
		
		try{
			
			entityTileSet = ImageIO.read(this.getClass().getResourceAsStream((ART_PATH + this.tileSetName)));
			
		}catch(IOException e){			
			System.err.println("[ENTITY]\t\tError loading "+ this.tileSetName +" image");
			// e.printStackTrace();
		}
	}
	
	protected void loadImage(String otherName){
		
		try{
			
			entityTileSet = ImageIO.read(new File(ART_PATH + otherName +".png"));			
			
		}catch(IOException e){			
			System.err.println("[ENTITY]\t\tError loading "+ otherName +".png image");
			// e.printStackTrace();
		}
		
	}
	
	
	
	
	// METHODS
	
	public abstract void update();

	protected abstract void selectDirection();
	
	public void render(Graphics g) {
		
		int renderX = (int) (x_Pix - width/2 - State_Play.getCameraX());
		int renderY = (int) (y_Pix - height/2 - State_Play.getCameraY());
		int renderW = (int) (renderX + width);
		int renderH = (int) (renderY + height);
				
		if(up){			
			g.drawImage(entityTileSet, renderX, renderY, renderW, renderH,
					pImgUp[aniFrame][0]*width, pImgUp[aniFrame][1]*height, pImgUp[aniFrame][0]*width+width, pImgUp[aniFrame][1]*height + height, null);
			lastImage = pImgUp;
		}
		
		else if(down){
			g.drawImage(entityTileSet, renderX, renderY, renderW, renderH, 
					pImgDown[aniFrame][0]*width, pImgDown[aniFrame][1]*height, pImgDown[aniFrame][0]*width+width, pImgDown[aniFrame][1]*height + height, null);
			lastImage = pImgDown;
		}
			
		else if(left){
			g.drawImage(entityTileSet, renderX, renderY, renderW, renderH,
					pImgLeft[aniFrame][0]*width, pImgLeft[aniFrame][1]*height, pImgLeft[aniFrame][0]*width+width, pImgLeft[aniFrame][1]*height + height, null);
			lastImage = pImgLeft;
		}
		
		else if(right){
			g.drawImage(entityTileSet, renderX, renderY, renderW, renderH,
					pImgRight[aniFrame][0]*width, pImgRight[aniFrame][1]*height, pImgRight[aniFrame][0]*width+width, pImgRight[aniFrame][1]*height + height, null);
			lastImage = pImgRight;
		}
		
		else
			g.drawImage(entityTileSet, renderX, renderY, renderW, renderH, 
					lastImage[0][0]*width, lastImage[0][1]*height, lastImage[0][0]*width+width, lastImage[0][1]*height + height,null);		
		
	}
	
	public void move() {
		
		aniDelta++;
		
		if(aniDelta >= aniTime){
			aniFrame++;
			aniDelta = 0;
			if(aniFrame > 3)
				aniFrame = 0;
		}
		
		if(up && canMove) {
			
			this.y_Pix -= moveSpeed;
			
		}		
		else if(down && canMove) {
			
			this.y_Pix += moveSpeed;

		}					
		else if(left && canMove) {
			
			this.x_Pix -= moveSpeed;
			
		}			
		else if(right && canMove) {	
			
			this.x_Pix += moveSpeed;
		}	
		
	}
	
	public boolean canMove(int x, int y) {
		
		canMove = true;
		
		if(x < 0 || y < 0 || x > world.getWidthInTiles() - 1 || y > world.getHeightInTiles() - 1)
			
			canMove = false;
		
		else if(world.getCollisionsArray()[x][y] == true)		// true == collision
			
			canMove = false;
		
		else if(world.getSprite(x, y) != null && world.getSprite(x, y) instanceof Entity)
			
			canMove = false;
		
		// REPORT THE COLLISION
		if(!canMove) System.out.println(this.toString() + " has found one collision");
		
		if(!canMove)	handleCollision();
		
		return canMove;
		
	}	
	
	protected void handleCollision() {
		
		this.x_Pix = this.x_Tile * Tile.TILE_SIZE + width/2;
		this.y_Pix = this.y_Tile * Tile.TILE_SIZE + height/2;
		
	}
	
	public void update_TilePosition(){
		
		// Updates his position	
		world.removeSprite(this);	
		this.x_Tile = (int) (this.x_Pix / Tile.TILE_SIZE);
		this.y_Tile = (int) (this.y_Pix / Tile.TILE_SIZE);
		world.addSprite(this);
		
	}		

	public void teleport() {
		
		Sprite spt = world.getSprite(x_Tile, y_Tile);
		Teleporter tp;
		
		if(interactTicks == 0 && spt != null && spt instanceof Teleporter) {			
		
			tp = (Teleporter) spt;
				
			if (tp.inTeleportArea(this)) {
				
				stopMoving();
				interactTicks = INTERACTION_TICKS;			
				tp.teleport(this);
				
			}
		}			
		
	}

}