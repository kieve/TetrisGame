package com.nash.games.tetris.game;

public class Move {
	public int x, y, rot;
	public float eval;
	
	public Move(int x, int y, int rot){
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
}
	
	