package com.nash.games.tetris.state;


import org.lwjgl.input.Keyboard;
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
import com.nash.games.tetris.gui.Button;

public class MenuState extends BasicGameState {
	public static final int ID = CustomState.MENU;

	private boolean initialized = false;
	private Button start, options;

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		if (!initialized) {
			start = new Button("Start", 350, ImageCache.BUTTON_UP.getImage(),
					ImageCache.BUTTON_DOWN.getImage());
			options = new Button("Options", 400, ImageCache.BUTTON_UP.getImage(), ImageCache.BUTTON_DOWN.getImage());
			initialized = true;
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (initialized) {
			g.drawImage(ImageCache.MENU_BACKGROUND.getImage(), 0, 0);
			start.render(g);
			options.render(g);
			g.setColor(Color.black);
			g.drawString("By Kevin Nash", 0, TetrisGame.HEIGHT-30);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (initialized) {
			start.update(container.getInput());
			options.update(container.getInput());
			
			if(container.getInput().isKeyPressed(Keyboard.KEY_ESCAPE)){
				container.exit();
			}
			
			if(start.clicked()){
				start.release();
				if(Global.AI_STATE == Global.BREED){
					game.enterState(GeneticState.ID);
				}else if(Global.AI_STATE == Global.FACEBK){
					game.enterState(FacebookState.ID, new FadeOutTransition(), new FadeInTransition());
				}else{
					game.enterState(GameState.ID, new FadeOutTransition(), new FadeInTransition());
				}
					
			}		
			
			if(options.clicked()){
				options.release();
				game.enterState(OptionState.ID);
			}
		}
	}

	public int getID() {
		return ID;
	}
}
