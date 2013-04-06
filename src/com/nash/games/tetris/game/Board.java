package com.nash.games.tetris.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;

public class Board {

	protected static final int NULL = -1, DOWN = 0, LEFT = 1, RIGHT = 2, UP = 3, FAR_LEFT = 4, FAR_RIGHT = 5;
	public int x;
	public int y;
	public int w;
	public int h;
	public int u;

	protected int[][] placement;
	protected Piece currentPiece, ghost;
	protected Image ghostImage;
	protected Queue<Integer> next = new LinkedList<Integer>();
	protected Random rn;
	protected int[][] currStruct;
	protected boolean[] collisionFlags = new boolean[6];
	protected boolean GAMEOVER = false;
	protected String status = "";
	protected int cleared = 0;
	protected int hold = -1;
	protected int[] bag = {1,2,3,4,5,6,7};
	protected ArrayList<Integer> currentBag = new ArrayList<Integer>();

	/*
	 * INDEX:
	 * 0 - DOWN
	 * 1 - LEFT
	 * 2 - RIGHT
	 * 3 - UP
	 * 4 - FAR_LEFT
	 * 5 - FAR_RIGHT
	 */
	
	public Board(){
		this(Global.getGameParameters()[0],Global.getGameParameters()[1],Global.getGameParameters()[2],-1);
	}

	public Board(int x, int y, int u, int seed) {
		this.x = x;
		this.y = y;
		this.u = u;
		w = Global.BOARD_W*u;
		h = Global.BOARD_H*u;
		placement = new int[Global.BOARD_H][Global.BOARD_W];
		if(seed != -1)
			rn = new Random(seed);
		else
			rn = new Random();
		currentPiece = null;
		resetCollisionFlags();
		nextPiece(-1);
	}

	public void nextPiece(int piece) {
		if(next.isEmpty()){
			next.offer(randomPiece());
			next.offer(randomPiece());
			next.offer(randomPiece());
			next.offer(randomPiece());
			next.offer(randomPiece());
		}
		if(piece == -1){
			currentPiece = new Piece(next.poll(), 3, -2);
			next.offer(randomPiece());
		}else{
			currentPiece = new Piece(piece, 3, -2);
		}
		ghostImage = ImageCache.valueOf("PIECE_" + currentPiece.getPieceID()).getImage().copy();
		ghostImage.setAlpha(0.5f);
		updateGhost();
		currStruct = currentPiece.getCurrentStructure();
	}
	
	public Piece getPiece(){
		return currentPiece;
	}
	
	public void setPiece(Piece p){
		currentPiece = p;
	}
	
	protected int randomPiece() {
		if (currentBag.size() == 0)
			fillBag();
		return currentBag.remove(rn.nextInt(currentBag.size()));
	}

	private void fillBag() {
		currentBag.clear();
		for (int i = 0; i < bag.length; i++) {
			currentBag.add(bag[i]);
		}
	}

	public int getLineCount(){
		return cleared;
	}
	
	public int[][] getBoard() {
		return placement;
	}
	
	public void setBoard(int[][] board){
		this.placement = board;
	}

	public boolean isGameOver(){
		return GAMEOVER;
	}
	
	private void updateGhost(){
		ghost = currentPiece.clonePiece();
		while(!movePieceDown(ghost));
	}

	public void render(Graphics g) {
		if(Global.AI_STATE == Global.BREED)
			renderGenetic(g);
		else
			if(!next.isEmpty())
				renderGame(g);
	}
	
	private void renderGenetic(Graphics g){
		g.setColor(Piece.Colours[0]);
		g.fillRect(x,y,w,h);
		g.setColor(Color.white);
		g.drawRect(x, y, w, h);
		if (currentPiece != null) {
			for (int i = 0; i < currStruct.length; i++) {
				g.setColor(Piece.Colours[currentPiece.getPieceID()]);
				
				if(currStruct[i][1] + currentPiece.getLocation()[1] >= 0)				
					g.fillRect(x + u * (currStruct[i][0] + currentPiece.getLocation()[0]),
						y + u * (currStruct[i][1] + currentPiece.getLocation()[1]), u, u);
			}
		}
		
		for (int k = 0; k < placement.length; k++) {
			for (int i = 0; i < placement[k].length; i++) {
				if (placement[k][i] != 0){
					g.setColor(Piece.Colours[placement[k][i]]);
					g.fillRect(x + u * i, y + u * k,u,u);
				}
			}
		}
	}
	
	private void renderGame(Graphics g) {
		if(ghost != null){
			for (int i = 0; i < ghost.getCurrentStructure().length; i++) {
				
				g.drawImage(ghostImage,
						x + u * (ghost.getCurrentStructure()[i][0] + ghost.getLocation()[0]),
						y + u * (ghost.getCurrentStructure()[i][1] + ghost.getLocation()[1]));
			}
		}
		
		if (currentPiece != null) {
			for (int i = 0; i < currentPiece.getCurrentStructure().length; i++) {
				g.drawImage(ImageCache.valueOf("PIECE_" + currentPiece.getPieceID()).getImage(),
						x + u * (currentPiece.getCurrentStructure()[i][0] + currentPiece.getLocation()[0]),
						y + u * (currentPiece.getCurrentStructure()[i][1] + currentPiece.getLocation()[1]));
			}
		}
		
		if (hold != -1) {
			for (int i = 0; i < currentPiece.getCurrentStructure().length; i++) {
				g.drawImage(ImageCache.valueOf("PIECE_" + hold).getImage().getScaledCopy(24, 24),
						x - 120 + 24 * Piece.getStuct(hold)[i][0],
						y + 75 + 24 * Piece.getStuct(hold)[i][1]);
			}
		}
		try {
			Integer[] temp = next.toArray(new Integer[]{});
			for(int u = 0; u < temp.length; u++){
				for (int i = 0; i < currentPiece.getCurrentStructure().length; i++) {
					if(temp[u] != null)
						g.drawImage(ImageCache.valueOf("PIECE_" + temp[u]).getImage().getScaledCopy(24, 24),
								Global.BOARD_W*32 + x + 32 + 24 * Piece.getStuct(temp[u])[i][0],
								y + 75 + 24 * Piece.getStuct(temp[u])[i][1] + 125*u);
				}
			}
		} catch (Exception e){
			System.out.println("Collections don't like high speeds");
		}
		
		for (int k = 0; k < placement.length; k++) {
			for (int i = 0; i < placement[k].length; i++) {
				if (placement[k][i] != 0)
					g.drawImage(ImageCache.valueOf("PIECE_" + placement[k][i]).getImage(), x + u * i, y + u * k);
			}
		}
		
		g.setColor(Color.cyan);
		g.drawString(status, 0, 250);
	}

	public void drop(){
		drop(false);
	}	
	
	public void drop(boolean overrideNext) {
		if(!GAMEOVER)
		while (!tick(overrideNext));
	}

	public boolean tick(){
		return tick(false);
	}
	
	public boolean tick(boolean overrideNext) {
		if(!GAMEOVER){
			boolean place = false;
			place = movePieceDown();
			if (place) {
				place(overrideNext);
			}
			removeRows();
			return place;
		}
		return false;
	}
	
	public static boolean getPossibleBoard(int[][] current, int[][] target, Piece p, boolean overrideNext){
		if(current != target){
			for(int i = 0; i < target.length; i++){
				System.arraycopy(current[i], 0, target[i], 0, target[i].length);
			}
		}
		for(int i = 0; i < p.getCurrentStructure().length; i++){
			if(p.getLocation()[1] + p.getCurrentStructure()[i][1] < 0 ||
					p.getLocation()[1] + p.getCurrentStructure()[i][1] >= Global.BOARD_H ||
					p.getLocation()[0] + p.getCurrentStructure()[i][0] < 0 ||
					p.getLocation()[0] + p.getCurrentStructure()[i][0] >= Global.BOARD_W){
				if(!overrideNext)
					return true;
			}
		}
		for (int i = 0; i < p.getCurrentStructure().length; i++)
				target[p.getLocation()[1] + p.getCurrentStructure()[i][1]]
			          [p.getLocation()[0] + p.getCurrentStructure()[i][0]] = p.getPieceID();
		return false;
	}
	
	public void place(boolean overrideNext) {
		GAMEOVER = getPossibleBoard(placement, placement, currentPiece, overrideNext);
		/*status = "Status:\n"
		+ "Above: " + BoardEvaluator.blocksAboveHoles(placement) + "\n"
		+ "Single: " + BoardEvaluator.countSingleHoles(placement) + "\n"
		+ "Conn: " + BoardEvaluator.countConnectedHoles(placement) + "\n"
		+ "Bump: " + BoardEvaluator.bumpiness(placement) + "\n"
		+ "Rows: " + BoardEvaluator.clearedRows(placement) + "\n"
		+ "Wells: " + BoardEvaluator.countWells(placement) + "\n"
		+ "Height: " + BoardEvaluator.pileHeight(placement) + "\n";*/
		if(!overrideNext)
			nextPiece(-1);
	}
	
	public int[][] getAfterPlace(int[][] board, Piece p){
		int[][] temp = new int[board.length][board[0].length];
		for(int i = 0; i < temp.length; i++)
			System.arraycopy(board[i], 0, temp[i], 0, temp[i].length);
		for (int i = 0; i < p.getCurrentStructure().length; i++) {
			temp[p.getLocation()[1] + p.getCurrentStructure()[i][1]]
			          [p.getLocation()[0] + p.getCurrentStructure()[i][0]] = p.getPieceID();
		}
		return temp;
	}

	public void removeRows() {
		Integer[] removeThese = getFullRows();
		for (Integer i : removeThese) {
			for (int r = i; r >= 0; r--) {
				if (r < findTop())
					break;
				if (r == 0) {
					placement[r] = new int[Global.BOARD_W];
				} else {
					System.arraycopy(placement[r-1], 0, placement[r], 0, placement[r].length);
				}
			}
		}
		updateGhost();
	}

	public Integer[] getFullRows() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		row: for (int k = 0; k < placement.length; k++) {
			for (int i = 0; i < placement[k].length; i++) {
				if (placement[k][i] == 0) {
					continue row;
				}
			}
			temp.add(k);
		}
		cleared += temp.size();
		return temp.toArray(new Integer[] {});
	}

	public int findTop() {
		for (int k = 0; k < placement.length; k++) {
			for (int i = 0; i < placement[k].length; i++) {
				if (placement[k][i] != 0) {
					return k;
				}
			}
		}
		return Global.BOARD_H-1;
	}

	private void resetCollisionFlags(){
		for(int i = 0; i < collisionFlags.length; i++){
			collisionFlags[i] = false;
		}
	}
	
	private boolean rotationCollide(int[] predictedLocation, int[][] predictedStructure) {
		resetCollisionFlags();
		boolean collided = false;
		collided = checkCollision(predictedLocation, predictedStructure, NULL, 1);
		if (!collided)
			return collided;

		if (checkCollision(predictedLocation, predictedStructure, RIGHT, 1)) {
			collisionFlags[RIGHT] = true;
		}
		if (checkCollision(predictedLocation, predictedStructure, LEFT, 1)) {
			collisionFlags[LEFT] = true;
		}
		if (checkCollision(predictedLocation, predictedStructure, DOWN, 1)) {
			collisionFlags[DOWN] = true;
		}
		if (checkCollision(predictedLocation, predictedStructure, LEFT, 4)){
			collisionFlags[FAR_LEFT] = true;
		}
		if (checkCollision(predictedLocation, predictedStructure, RIGHT, 4)){
			collisionFlags[FAR_RIGHT] = true;
		}
		if (!collisionFlags[DOWN] && !collisionFlags[LEFT] && !collisionFlags[RIGHT]) {
			if (checkCollision(predictedLocation, predictedStructure, RIGHT, 2)) {
				collisionFlags[RIGHT] = true;
			}
			if (checkCollision(predictedLocation, predictedStructure, LEFT, 2)) {
				collisionFlags[LEFT] = true;
			}
			if (checkCollision(predictedLocation, predictedStructure, DOWN, 2)) {
				collisionFlags[DOWN] = true;
			}
		}
		return collided;
	}

	public boolean checkCollision(int[] predictedLocation,
			int[][] predictedStructure, int dir, int num) {
		boolean collided = false;
		boolean aboveLine = false;

		if (currentPiece == null)
			return true;
		int[] tempLocation = new int[2];
		System.arraycopy(predictedLocation, 0, tempLocation, 0, tempLocation.length);
		if(dir != NULL){	
			for (int n = 0; n < num; n++) {
				switch (dir) {
				case DOWN:
					tempLocation[1]++;
					break;
				case LEFT:
					tempLocation[0]--;
					break;
				case RIGHT:
					tempLocation[0]++;
					break;
				case UP:
					tempLocation[1]--;
					break;
				}
			}
		}
		for (int i = 0; i < predictedStructure.length; i++) {
			if (tempLocation[0] + predictedStructure[i][0] < 0) {
				collisionFlags[LEFT] = collided = true;
				collided = true;
			} else if (tempLocation[0] + predictedStructure[i][0] >= Global.BOARD_W) {
				collisionFlags[RIGHT] = collided = true;
				collided = true;
			} else if (tempLocation[1] + predictedStructure[i][1] >= Global.BOARD_H) {
				collisionFlags[DOWN] = collided = true;
				collided = true;
			} else if (tempLocation[1] + predictedStructure[i][1] < 0) {
				aboveLine = true;
			} 
			if (!collided && !aboveLine) {
				if (dir == DOWN)
					collided = placement[tempLocation[1] + predictedStructure[i][1]]
					                     [tempLocation[0] + predictedStructure[i][0]] != 0;
				else if (dir == LEFT)
					collided = placement[tempLocation[1] + predictedStructure[i][1]]
					                     [tempLocation[0] + predictedStructure[i][0]] != 0;
				else if (dir == RIGHT)
					collided = placement[tempLocation[1] + predictedStructure[i][1]]
					                     [tempLocation[0] + predictedStructure[i][0]] != 0;
				else if (dir == UP)
					collided = placement[tempLocation[1] + predictedStructure[i][1]]
					                     [tempLocation[0] + predictedStructure[i][0]] != 0;
				else {
					collided = placement[tempLocation[1]
							+ predictedStructure[i][1]][tempLocation[0]
							+ predictedStructure[i][0]] != 0;
				}
			}
		}
		return collided;
	}

	public boolean movePieceDown(){
		return movePieceDown(currentPiece);
	}
	
	public boolean movePieceUp(){
		return movePieceUp(currentPiece);
	}
	
	public boolean movePieceLeft(){
		boolean temp = movePieceLeft(currentPiece); 
		updateGhost();
		return temp;
	}
	
	public boolean movePieceRight(){
		boolean temp = movePieceRight(currentPiece); 
		updateGhost();
		return temp;
	}
	
	public boolean movePieceDown(Piece p) {
		boolean collided = checkCollision(p.getLocation(), p.getCurrentStructure(), DOWN, 1);
		if (!collided)
			p.moveDown();
		return collided;
	}

	public boolean movePieceUp(Piece p) {
		boolean collided = checkCollision(p.getLocation(), p.getCurrentStructure(), UP, 1);
		if (!collided)
			p.moveUp();
		return collided;
	}

	public boolean movePieceLeft(Piece p) {
		boolean collided = checkCollision(p.getLocation(), p.getCurrentStructure(), LEFT, 1);
		if (!collided)
			p.moveLeft();
		return collided;
	}

	public boolean movePieceRight(Piece p) {
		boolean collided = checkCollision(p.getLocation(), p.getCurrentStructure(), RIGHT, 1);
		if (!collided)
			p.moveRight();
		return collided;
	}

	public boolean rotatePiece(int dir) {
		boolean collided = false;
		boolean[] canRotate = new boolean[] { true, true, true, true};
		if (currentPiece == null)
			return collided;
		int temp[][] = currentPiece.getPieceStructure()[currentPiece.getFutureRotation(dir)];
		do {
			collided = rotationCollide(currentPiece.getLocation(), temp);
			canRotate = new boolean[] { !collisionFlags[0], !collisionFlags[1],
					!collisionFlags[2], !collisionFlags[3], false, false};
				if(collisionFlags[LEFT] && collisionFlags[RIGHT] && collisionFlags[DOWN]){
				if(currentPiece.getPieceID() == Piece.I){
					if(collisionFlags[FAR_LEFT] && !collisionFlags[FAR_RIGHT]){
						canRotate[FAR_RIGHT] = true;
						movePieceRight();
						movePieceRight();
					}else if(collisionFlags[FAR_RIGHT] && !collisionFlags[FAR_LEFT]){
						canRotate[FAR_LEFT] = true;
						movePieceLeft();
						movePieceLeft();
					}else{
						break;
					}
				}
			}
			if (collisionFlags[LEFT]) {
				canRotate[LEFT] = !movePieceRight();
			}
			if (collisionFlags[RIGHT]) {
				canRotate[RIGHT] = !movePieceLeft();
			}
			if (collisionFlags[DOWN]) {
				canRotate[DOWN] = !movePieceUp();
			}
			if (collisionFlags[UP]) {
				canRotate[UP] = !movePieceDown();
			}
		} while (collided);
		if ((canRotate[DOWN] && canRotate[LEFT] && canRotate[RIGHT]
				&& canRotate[UP]) || canRotate[FAR_LEFT] || canRotate[FAR_RIGHT]) {
			currentPiece.rotate(dir);
			currStruct = currentPiece.getCurrentStructure();
			updateGhost();
		}
		return collided;
	}
	

	public boolean rotatePieceLeft() {
		return rotatePiece(-1);
	}
	

	public boolean rotatePieceRight() {
		return rotatePiece(1);
	}
}
