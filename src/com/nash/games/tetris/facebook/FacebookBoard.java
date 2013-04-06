package com.nash.games.tetris.facebook;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;
import com.nash.games.tetris.game.Board;
import com.nash.games.tetris.game.Piece;


public class FacebookBoard extends Board {
	public static final int BOARD_W = 220, BOARD_H = 440, PIECE_SIZE = 22;
	public static int BOARD_X = -1, BOARD_Y = -1;
	public static Piece nextPiece;
	public static final int lineDelay = 500;
	
	public FacebookBoard(){
		super();
	}
	
	public void reset(){
		setBoard(new int[20][10]);
		GAMEOVER = false;
	}
	
	public static void updateNextPiece(int i){
		int temp = FacebookPieceColours.parseColour(i);
		if(temp > 0)
			nextPiece = new Piece(temp, 3, -2);
		else
			nextPiece = null;
	}
	
	public static boolean isFound() {
		return BOARD_X != -1 && BOARD_Y != -1;
	}
	
	public void render(Graphics g){
		g.setColor(Color.lightGray);
		g.fillRect(x, y, w, h);
		for (int k = 0; k < placement.length; k++) {
			for (int i = 0; i < placement[k].length; i++) {
				if (placement[k][i] != 0)
					g.drawImage(ImageCache.valueOf("PIECE_" + placement[k][i]).getImage(), x + u * i, y + u * k);
			}
		}
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
		if(removeThese.length > 0)
			try {
				Thread.sleep(lineDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}