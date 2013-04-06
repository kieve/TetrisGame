package com.nash.games.tetris.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.nash.games.tetris.cache.ImageCache;

public class IntroState extends BasicGameState {
	public static final int ID = CustomState.INTRO;

	private static int totalDelta = 0;
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.drawImage(ImageCache.INTRO_BACKGROUND.getImage(), 0, 0);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		totalDelta += delta;
		
		if(totalDelta >= 0)
			//TODO Make a better animation?
			game.enterState(MenuState.ID, new FadeOutTransition(), new FadeInTransition());
	}

	public int getID() {
		return ID;
	}

}
