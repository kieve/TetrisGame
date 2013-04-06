package com.nash.games.tetris.ai;

import java.util.concurrent.CountDownLatch;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.game.Board;
import com.nash.games.tetris.game.Move;

public class AIGame extends Board implements Runnable {
	private CountDownLatch doneSignal;
	public Agent agent = new Agent();

	public AIGame(int x, int y, int u, int seed, CountDownLatch doneSignal) {
		super(x, y, u, seed);
		this.doneSignal = doneSignal;
	}

	public void run() {
		game: while (!GAMEOVER) {
			Move m = MoveFinder.getBestMove(this, currentPiece.clonePiece(), agent);
			if(m == null)
				break;
			currentPiece.setRoation(m.rot);
			if(currentPiece.getLocation()[0] < m.x){
				while(currentPiece.getLocation()[0] != m.x){
					if(movePieceRight())
						break game;
				}
				drop();
			}else if(currentPiece.getLocation()[0] > m.x){
				while(currentPiece.getLocation()[0] != m.x){
					if(movePieceLeft())
						break game;
				}
				drop();
			}else{
				drop();
			}
			if(Global.AI_STATE == Global.USE){
				try {
					Thread.sleep(Global.AI_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		agent.clearedRowsArray.add(cleared); // acessado por multiplas threads
												// na GA!
		if (doneSignal != null)
			doneSignal.countDown();
	}
}
