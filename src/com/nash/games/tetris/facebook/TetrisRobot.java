package com.nash.games.tetris.facebook;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TetrisRobot {
	private static final long serialVersionUID = 1L;
	private static Robot r;
	private static ArrayList<String> msg = new ArrayList<String>();
	private static Rectangle bounds;
	private static final int KEY_DELAY = 40;
	
	public TetrisRobot() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		bounds = new Rectangle(0,0,d.width,d.height);
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void findBoard() {
		msg.remove("Searching for Board");
		msg.add("Searching for Board");
		new Thread(new Runnable() {
			public void run() {
				boolean foundIt = false;
				do {
					BufferedImage bi = r.createScreenCapture(bounds);
					search: for (int w = 0; w < bi.getWidth(); w++) {
						for (int h = 0; h < bi.getHeight(); h++) {
							if (FacebookPieceColours.b1 == bi.getRGB(w, h)) {
								for (int i = 0; i < 22; i++) {
									try {
										if (FacebookPieceColours.b1 != bi.getRGB(w + i, h)) {
											break;
										}
										if (FacebookPieceColours.b2 != bi.getRGB(w + i + 22, h)) {
											break;
										}
										if (i == 21) {
											foundIt = true;
										}
									} catch (ArrayIndexOutOfBoundsException e) {
										break;
									}
								}
								if (foundIt) {
									FacebookBoard.BOARD_X = w;
									FacebookBoard.BOARD_Y = h;
									msg.add("Board Location:");
									msg.add("X: " + FacebookBoard.BOARD_X);
									msg.add("Y: " + FacebookBoard.BOARD_Y);
									msg.remove("Searching for Board");
									break search;
								}
							}
						}
					}
				} while (!foundIt);
			}
		}).start();
	}
	
	public static void findGhost() {
		int temp = r.getPixelColor(FacebookBoard.BOARD_X+4*22, FacebookBoard.BOARD_Y-3).getRGB();
		if(temp != FacebookPieceColours.b1 && temp != FacebookPieceColours.b2){
			FacebookBoard.updateNextPiece(temp);
		}
	}
	
	public static int[][] parseBoard(){
		int[][] board = new int[20][10];
		BufferedImage bi = r.createScreenCapture(new Rectangle(FacebookBoard.BOARD_X, FacebookBoard.BOARD_Y, FacebookBoard.BOARD_W, FacebookBoard.BOARD_H));
		for(int r = 0; r < 20; r++){
			for(int c = 0; c < 10; c++){
				board[r][c] = FacebookPieceColours.parseBoardColour(bi.getRGB(c*22, r*22));
			}
		}
		return board;
	}
	
	public static void key(int i) {
		r.keyPress(i);
		r.delay(KEY_DELAY);
		r.keyRelease(i);
		r.delay(KEY_DELAY);
	}
	
	public static void drop(){
		key(KeyEvent.VK_SPACE);
	}
	
	public static void left(int num){
		for(int i = 0; i < num; i++)
			key(KeyEvent.VK_LEFT);
	}
	
	public static void right(int num){
		for(int i = 0; i < num; i++)
			key(KeyEvent.VK_RIGHT);
	}
	
	public static void rotate(int num){
		int temp;
		if(num > 0)
			temp = KeyEvent.VK_S;
		else {
			temp = KeyEvent.VK_D;
			num *= -1;
		}
		for(int i = 0; i < num; i++)
			key(temp);
	}
	
	public static void removeMsg(String s){
		for(int i = 0; i < msg.size(); i++){
			if(msg.get(i).contains(s)){
				msg.remove(i);
			}
		}
	}
	
	public static void addMsg(String s){
		msg.add(s);
	}
	
	public static void clearMsgs(){
		msg.clear();
	}
	
	public static String getMsg(){
		String temp = "";
		for(int i = 0; i < msg.size(); i++){
			temp+=msg.get(i) + "\n";
		}
		return temp;
	}
}

