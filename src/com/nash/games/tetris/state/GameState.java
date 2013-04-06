package com.nash.games.tetris.state;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.nash.games.tetris.TetrisGame;
import com.nash.games.tetris.ai.AIGame;
import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;
import com.nash.games.tetris.game.Board;
import com.nash.games.tetris.game.PlayerBoard;
import com.nash.games.tetris.util.Counter;

public class GameState extends BasicGameState {
	public static final int ID = CustomState.GAME;
	
	private static final int repeatDelay = 70, repeatStart = 200;
	private static int currentBackground = 0;
	private static int vDelta = 0, hDelta = 0;
	private static Board b;
	private static Thread ai;
	private static Counter c;
	
	private boolean left, right, down, vStarted, hStarted, entered = false;
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		left = false;
		right = false;
		down = false;
		vStarted = false;
		hStarted = false;
	}
	
	

	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		
		if(Global.AI_STATE == Global.NONE){
			b = new PlayerBoard();
		}else{
			b = new AIGame(Global.getGameParameters()[0],
					Global.getGameParameters()[1],
					Global.getGameParameters()[2],
					-1,
					null);
			ai = new Thread((AIGame)b);
			ai.start();
		}
		c = new Counter(150,TetrisGame.HEIGHT-30,16);
		entered = true;
	}



	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		entered = false;
		if(ai != null && ai.isAlive()){
			ai.interrupt();
			ai = null;
		}
		c = null;
		b = null;
	}


	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if(entered){
			g.drawImage(ImageCache.BOARD_BACKGROUND.getImage(), 0, 0);
			g.drawImage(ImageCache.valueOf("BOARD_BACKGROUND_"+currentBackground).getImage(), b.x, b.y);
			
			g.setColor(Color.pink);
			for(int i = 1; i < Global.BOARD_H; i++){
				if(i == 1)
					g.setColor(Color.red.darker());
				else
					g.setColor(Color.pink);
				g.drawLine(b.x, b.y+i*b.u, b.x+Global.BOARD_W*b.u, b.y+i*b.u);
			}
			for(int i = 0; i < Global.BOARD_W; i++)
				g.drawLine(b.x+i*b.u, b.y, b.x+i*b.u, b.y+Global.BOARD_H*b.u);
			
	
			b.render(g);
			
			g.drawImage(ImageCache.BOARD_UI.getImage(), 0, 0);
			c.render(g);
			g.drawString("Lines:\n" + b.getLineCount(), 0, TetrisGame.HEIGHT-50);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Keyboard.KEY_ADD))
			Global.increaseAIDelay();
		if(container.getInput().isKeyPressed(Keyboard.KEY_SUBTRACT))
			Global.decreaseAIDelay();
		if(entered){			
			c.setNumber(b.getLineCount());
			c.update(delta);
			
			Input i = container.getInput();
			
			if(i.isKeyPressed(Keyboard.KEY_ESCAPE))
				game.enterState(MenuState.ID, new FadeOutTransition(), new FadeInTransition());
			
			if(down){
				vDelta += delta;
				if(vDelta >= repeatDelay && vStarted){
					b.movePieceDown();
					vDelta %= repeatDelay;
				}else if(!vStarted && vDelta >= repeatStart){
					vDelta = 0;
					vStarted = true;
				}
			}else{
				vDelta = 0;
				vStarted = false;
			}
			
			if(left || right){
				hDelta += delta;
				if(hDelta >= repeatDelay && hStarted){
					if(left)
						b.movePieceLeft();
					if(right)
						b.movePieceRight();
					hDelta %= repeatDelay;
				}else if(!hStarted && hDelta >= repeatStart){
					hDelta = 0;
					hStarted = true;
				}
			}else{
				hDelta = 0;
				hStarted = false;
			}
			
			if(i.isKeyPressed(Keyboard.KEY_LEFT)){
				right = false;
				left = true;
				b.movePieceLeft();
			}else if(i.isKeyPressed(Keyboard.KEY_RIGHT)){
				left = false;
				right = true;
				b.movePieceRight();
			}
			
			if(i.isKeyPressed(Keyboard.KEY_DOWN)){
				down = true;
				b.movePieceDown();
			}
			
			if(i.isKeyPressed(Keyboard.KEY_S)){
				b.rotatePieceLeft();
			}else if(i.isKeyPressed(Keyboard.KEY_D)){
				b.rotatePieceRight();
			}
			
			if(i.isKeyPressed(Keyboard.KEY_SPACE))
				b.drop();
			if(b instanceof PlayerBoard){
				if(i.isKeyPressed(Keyboard.KEY_F))
					((PlayerBoard)b).hold();
				((PlayerBoard)b).addTime(delta);
				if(((PlayerBoard)b).getTime() >= ((PlayerBoard)b).getDifficulty()){
					b.tick();
				}
			}
			if(i.isKeyPressed(Keyboard.KEY_RETURN)){
				currentBackground = (currentBackground+1)%9;
			}
					
			if(!i.isKeyDown(Keyboard.KEY_LEFT)){
				left = false;
			}
			if(!i.isKeyDown(Keyboard.KEY_RIGHT)){
				right = false;
			}
			if(!i.isKeyDown(Keyboard.KEY_DOWN)){
				down = false;
			}
		}
	}

	public int getID() {
		return ID;
	}
}