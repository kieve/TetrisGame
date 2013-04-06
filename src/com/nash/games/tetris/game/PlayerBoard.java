package com.nash.games.tetris.game;

import com.nash.games.tetris.cache.Global;

public class PlayerBoard extends Board {
	
	private int difficulty = 1000;
	private int dropTimer = 0;
	
	public PlayerBoard(){
		super(Global.getGameParameters()[0], //X position
				Global.getGameParameters()[1], //Y position
				Global.getGameParameters()[2], //Piece square width and height
				-1); // seed (-1 for random seed)
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	@Override
	public void drop(){
		super.drop();
		resetTime();
	}
		
	public void hold(){
		if(hold == -1){
			hold = currentPiece.getPieceID();
			nextPiece(-1);
		}else{
			int temp = hold;
			hold = currentPiece.getPieceID();
			nextPiece(temp);
		}
	}
	
	@Override
	public boolean tick(){
		resetTime();
		return super.tick();
	}
	
	public void addTime(int i){
		dropTimer += i;
	}
	
	public void resetTime(){
		dropTimer = 0;
	}
	
	public int getTime(){
		return dropTimer;
	}
	
	@Override
	public boolean movePieceDown() {
		boolean collided = checkCollision(currentPiece.getLocation(), currentPiece.getCurrentStructure(), DOWN, 1);
		if (!collided)
			currentPiece.moveDown();
		resetTime();
		return collided;
	}
}
