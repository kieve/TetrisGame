package com.nash.games.tetris.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
public class CustomState extends BasicGameState {
	public static final int LOADING = 0, INTRO = 1, MENU = 2, OPTION = 3, GAME = 4, GENETIC = 5, FACEBOOK = 6;
	
	public static final int ID = CustomState.INTRO;
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
	}

	public int getID() {
		return ID;
	}
}
