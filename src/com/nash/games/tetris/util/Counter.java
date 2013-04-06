package com.nash.games.tetris.util;

import org.newdawn.slick.Graphics;

public class Counter {
	private Digit[] digits;
	private int x,y,size,number;
	
	public Counter(int x, int y, int size){
		this.x = x;
		this.y = y;
		this.size = size;
		this.number = 0;
		
		digits = new Digit[size];
		for(int i = 0; i < size; i++){
			digits[i] = new Digit();
		}
	}
	
	public void render(Graphics g){
		for(int i = 0; i < size; i++){
			digits[i].render(x+i*19, y, g);
		}
	}
	
	public void update(int delta){
		for(int i = 0; i < size; i++){
			digits[i].update(delta);
		}
	}
	
	public void increase(){
		setNumber(number+1);
	}
	
	public void setNumber(int n){
		this.number = n;
		String s = Integer.toBinaryString(n);
		int track = s.length()-1;
		for(int i = size-1; i >= 0; i--){
			digits[i].setDigit(s.charAt(track) == '\u0031');
			track--;
			if(track < 0)
				break;
		}
	}
}
