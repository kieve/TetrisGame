package com.nash.games.tetris.state;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.nash.games.tetris.ai.Agent;
import com.nash.games.tetris.ai.MoveFinder;
import com.nash.games.tetris.facebook.FacebookBoard;
import com.nash.games.tetris.facebook.TetrisRobot;
import com.nash.games.tetris.game.Move;

public class FacebookState extends BasicGameState {
	public static final int ID = CustomState.FACEBOOK;
	public static TetrisRobot tr;
	public static FacebookBoard b;
	public static Agent agent;
	public static int totalDelta = 0, moveDelay = 200;
	public static boolean debug = false, moved = true;

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		tr = new TetrisRobot();
		agent = new Agent();
	}

	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		tr.findBoard();
		TetrisRobot.clearMsgs();
		b = new FacebookBoard();
	}

	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		FacebookBoard.BOARD_X = -1;
		FacebookBoard.BOARD_Y = -1;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString(TetrisRobot.getMsg(),0,0);
		if(b != null){
			b.render(g);
		}
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(container.getInput().isKeyPressed(Keyboard.KEY_ESCAPE)){
			game.enterState(MenuState.ID);
		}
		if(!b.isGameOver()){
			TetrisRobot.findGhost();
			totalDelta += delta;
			if(FacebookBoard.isFound()){
				if(moved){
					b.setPiece(FacebookBoard.nextPiece);
					if(b.getPiece() != null)
						moved = false;
					return;
				}
				if(!moved){
					b.setBoard(TetrisRobot.parseBoard());
					Move m = MoveFinder.getBestMove(b, b.getPiece().clonePiece(), agent);
					if(m != null){
						TetrisRobot.rotate(-m.rot);
						if(3 < m.x){
							TetrisRobot.right(m.x-3);
						}else if(3 > m.x){
							TetrisRobot.left(3-m.x);
						}
						TetrisRobot.drop();
						b.getPiece().setRoation(m.rot);
						b.getPiece().setLocation(m.x, m.y);
						b.drop(true);
						totalDelta = 0;
						moved = true;
						b.setPiece(null);
					}
				}
			}
		}
	}

	public int getID() {
		return ID;
	}

}
