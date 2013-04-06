package com.nash.games.tetris.cache;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.nash.games.tetris.util.Error;


public enum ImageCache {
	LOADING_BACKGROUND("res/bgs/loading_bg.png"),
	INTRO_BACKGROUND("res/bgs/intro_bg.png"),
	MENU_BACKGROUND("res/bgs/menu_bg.png"),
	

	BUTTON_UP("res/buttons/button_up.png"),
	BUTTON_DOWN("res/buttons/button_down.png"),
	
	BOARD_UI("res/bgs/ui.png"),

	PIECE_0("res/pieces/0.png"),
	PIECE_1("res/pieces/1.png"),
	PIECE_2("res/pieces/2.png"),
	PIECE_3("res/pieces/3.png"),
	PIECE_4("res/pieces/4.png"),
	PIECE_5("res/pieces/5.png"),
	PIECE_6("res/pieces/6.png"),
	PIECE_7("res/pieces/7.png"),

	NUMBER_0("res/numbers/0.png"),
	NUMBER_1("res/numbers/1.png"),
	NUMBER_10("res/numbers/10.png"),
	NUMBER_01("res/numbers/01.png"),

	BOARD_BACKGROUND  ("res/bgs/board.png"),
	BOARD_BACKGROUND_0("res/bgs/board0.png"),
	BOARD_BACKGROUND_1("res/bgs/board1.png"),
	BOARD_BACKGROUND_2("res/bgs/board2.png"),
	BOARD_BACKGROUND_3("res/bgs/board3.png"),
	BOARD_BACKGROUND_4("res/bgs/board4.png"),
	BOARD_BACKGROUND_5("res/bgs/board5.png"),
	BOARD_BACKGROUND_6("res/bgs/board6.png"),
	BOARD_BACKGROUND_7("res/bgs/board7.png"),
	BOARD_BACKGROUND_8("res/bgs/board8.png");
	
	private String path;
	private Image image;
	
	private ImageCache(String path){
		this.path = path;
	}
	
	public Image getImage(){
		return image;
	}
	
	public String getPath(){
		return path;
	}
	
	public Boolean isLoaded(){
		if(image != null)
			return true;
		return false;
	}
	
	public void Load(){
		if(!isLoaded()){
			try {
				image = new Image(path);
			} catch (SlickException e) {
				e.printStackTrace();
				Error.addError(Error.LOADING_IMAGE);
			}
		}
	}
}
