package com.nash.games.tetris.state;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;
import com.nash.games.tetris.gui.Button;
import com.nash.games.tetris.util.Error;

public class OptionState extends BasicGameState {
	public static final int ID = CustomState.OPTION;

	private boolean initialized = false;
	private Button boardSize, aiDelay, useAI, back;
	private TextField boardSizeField, aiDelayField, useAIField;

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		if (!initialized) {
			boardSize = new Button("Board Size", 350, ImageCache.BUTTON_UP.getImage(),
					ImageCache.BUTTON_DOWN.getImage());
			aiDelay = new Button("AI delay", 400,
					ImageCache.BUTTON_UP.getImage(),
					ImageCache.BUTTON_DOWN.getImage());
			useAI = new Button("Toggle AI", 450,
					ImageCache.BUTTON_UP.getImage(),
					ImageCache.BUTTON_DOWN.getImage());
			back = new Button("Back", 500,
					ImageCache.BUTTON_UP.getImage(),
					ImageCache.BUTTON_DOWN.getImage());
			Font f = new Font("Verdana", Font.BOLD, 25);
			TrueTypeFont uf = new TrueTypeFont(f,true);
			boardSizeField = new TextField(container, uf, 400, 350, 100, ImageCache.BUTTON_UP.getImage().getHeight());
			aiDelayField = new TextField(container, uf, 400, 400, 100, ImageCache.BUTTON_UP.getImage().getHeight());
			useAIField = new TextField(container, uf, 400, 450, 100, ImageCache.BUTTON_UP.getImage().getHeight());
			boardSizeField.setText(Global.BOARD_W + "x" + Global.BOARD_H);
			aiDelayField.setText("" + Global.AI_DELAY);
			useAIField.setText(Global.parseAIState());
			useAIField.deactivate();
			initialized = true;
		}
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if (initialized) {
			g.drawImage(ImageCache.MENU_BACKGROUND.getImage(), 0, 0);
			boardSize.render(g);
			aiDelay.render(g);
			useAI.render(g);
			back.render(g);
			boardSizeField.render(container, g);
			aiDelayField.render(container, g);
			useAIField.render(container, g);
		}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (initialized) {
			boardSize.update(container.getInput());
			aiDelay.update(container.getInput());
			useAI.update(container.getInput());
			back.update(container.getInput());
			
			if(boardSize.clicked()){
				boardSize.release();
				try {
					String temp[] = boardSizeField.getText().split("x");
					Global.BOARD_W = Integer.parseInt(temp[0]);
					Global.BOARD_H = Integer.parseInt(temp[1]);
				} catch (Exception e) {
					e.printStackTrace();
					Error.addError(Error.INTEGER_PARSE);
				}
			}
			if(aiDelay.clicked()){
				aiDelay.release();
				try {
					Global.AI_DELAY = Integer.parseInt(aiDelayField.getText());
				} catch (Exception e) {
					e.printStackTrace();
					Error.addError(Error.INTEGER_PARSE);
				}
			}
			
			if(useAI.clicked()){
				useAI.release();
				Global.toggleAI();
				useAIField.setText(Global.parseAIState());
			}
			
			if(back.clicked()){
				back.release();
				game.enterState(MenuState.ID);
			}
			

			Error.handleError(Error.getErrorCount());
		}
	}

	public int getID() {
		return ID;
	}
}
