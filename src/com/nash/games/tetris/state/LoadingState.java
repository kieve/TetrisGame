package com.nash.games.tetris.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.nash.games.tetris.TetrisGame;
import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;

public class LoadingState extends BasicGameState {
	public static final int ID = CustomState.LOADING;

	private int totalLoaded = 0, totalResources = 0;
	private ImageCache ic;
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		totalResources = ImageCache.values().length;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		if(ImageCache.LOADING_BACKGROUND.isLoaded()){
			g.drawImage(ImageCache.LOADING_BACKGROUND.getImage(), 0, 0);
		}

		g.setColor(Color.white);
		g.drawString("Loading: ", 25, 525);
		if(ic != null){
			g.drawString(ic.getPath(), 125, 525);
		}
		
		if(totalLoaded != totalResources){
			g.setColor(Color.black);
			g.drawRect(25, TetrisGame.HEIGHT-150, TetrisGame.WIDTH-50, 25);
			g.setColor(Color.red);
			g.fillRect(26, TetrisGame.HEIGHT-149, TetrisGame.WIDTH-48, 23);
			g.setColor(Color.green);
			g.fillRect(26, TetrisGame.HEIGHT-149, ((TetrisGame.WIDTH-18)*totalLoaded)/totalResources, 23);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(totalLoaded == totalResources){
			Global.loadAnimations();
			game.enterState(IntroState.ID, new FadeOutTransition(), new FadeInTransition());
		}
		for(ImageCache ic: ImageCache.values()){
			if(!ic.isLoaded()){
				this.ic = ic;
				ic.Load();
				System.out.println(ic.getPath());
				totalLoaded++;
				break;
			}
		}
	}

	public int getID() {
		return ID;
	}

}
