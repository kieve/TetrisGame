package com.nash.games.tetris.cache;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

import com.nash.games.tetris.TetrisGame;

public abstract class Global {
	public static int BOARD_W = 10, BOARD_H = 20, BOARD_UNIT_SIZE = 32;
	public static final int NONE = 0, USE = 1, BREED = 2, FACEBK = 3;
	public static int AI_STATE = 0, AI_DELAY = 200;
	
	public static Animation oneToZero, zeroToOne;
	
	public static int[] getGameParameters(){
		int temp[] = new int[3];
		temp[0] = (TetrisGame.WIDTH - Global.BOARD_W * Global.BOARD_UNIT_SIZE) / 2;
		temp[1] = (TetrisGame.HEIGHT - Global.BOARD_H * Global.BOARD_UNIT_SIZE) / 2;
		temp[2] = Global.BOARD_UNIT_SIZE;
		return temp;
	}
	
	public static void loadAnimations(){
		oneToZero = new Animation(new SpriteSheet(ImageCache.NUMBER_10.getImage(),19,30),0,0,3,0,false,100,false);
		zeroToOne = new Animation(new SpriteSheet(ImageCache.NUMBER_01.getImage(),19,30),0,0,3,0,false,100,false);
	}
	
	public static void toggleAI(){
		Global.AI_STATE = (Global.AI_STATE+1)%4;
	}
	
	public static String parseAIState(){
		switch(AI_STATE){
		case USE:
			return "USE";
		case BREED:
			return "BREED";
		case FACEBK:
			return "FaceB";
		default:
			return "NONE";
		}
	}
	
	public static void increaseAIDelay(){
		AI_DELAY+=10;
	}
	
	public static void decreaseAIDelay(){
		if(AI_DELAY >= 10)
			AI_DELAY-=10;
	}
}
