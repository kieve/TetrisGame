package com.nash.games.tetris;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.nash.games.tetris.state.*;

public class TetrisGame extends StateBasedGame {
	public static TetrisGame INSTANCE;
	public static final int WIDTH = 600, HEIGHT = 700, SW = 1920, SH = 1080;
	public static final String TITLE = "Tetris! Protected by \"Fair Use\"";

	public static TetrisGame getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TetrisGame();
		return INSTANCE;
	}

	public TetrisGame() {
		super(TITLE);
	}

	public void initStatesList(GameContainer container) throws SlickException {
		addState(new LoadingState());
		addState(new IntroState());
		addState(new MenuState());
		addState(new OptionState());
		addState(new GameState());
		addState(new GeneticState());
		addState(new FacebookState());
	}

	public static void main(String args[]) {
		try {
			AppGameContainer app = new AppGameContainer(
					TetrisGame.getInstance());
			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.setIcons(new String[] { "res/icons/32x32.png",
					"res/icons/24x24.png", "res/icons/16x16.png" });
			app.setVSync(true);
			app.setShowFPS(false);
			app.setTargetFrameRate(60);
			app.setAlwaysRender(true);
			app.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
