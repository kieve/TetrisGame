package com.nash.games.tetris.util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.cache.ImageCache;

public class Digit {
	private boolean digit;
	private int delta;
	private Animation oneToZero;
	private Animation zeroToOne;
	
	public Digit(){
		this(false);
	}
	
	public Digit(boolean digit){
		this.digit = digit;
		oneToZero = Global.oneToZero.copy();
		zeroToOne = Global.zeroToOne.copy();
	}
	
	public void render(int x, int y, Graphics g){
		if(delta == 0)
			g.drawImage(ImageCache.NUMBER_0.getImage(), x, y);
		else if(delta > 0 && delta < 400)
			zeroToOne.draw(x, y);
		else if(delta == 400)
			g.drawImage(ImageCache.NUMBER_1.getImage(), x, y);
		else if(delta > 400)
			oneToZero.draw(x, y);
	}
	
	public void update(int delta){
		if(!digit){
			if(this.delta >= 400 && this.delta < 800){
				this.delta += delta;
				oneToZero.update(delta);
			}else{
				this.delta = 0;
				oneToZero.restart();
				zeroToOne.restart();
			}
		}else{
			if(this.delta < 400){
				this.delta += delta;
				zeroToOne.update(delta);
			}else{
				this.delta = 400;
				oneToZero.restart();
				zeroToOne.restart();
			}
		}
	}
	
	public void setDigit(boolean digit){
		this.digit = digit;
	}
	
	public boolean getDigit(){
		return digit;
	}
}
