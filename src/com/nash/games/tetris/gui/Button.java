package com.nash.games.tetris.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.nash.games.tetris.TetrisGame;

public class Button {
	private String text;
	private int x = -1, y;
	private Image defaultImage, overImage;
	private boolean clicked = false, hovering = false;

	public Button(String text, int x, int y, Image defaultImage, Image overImage) {
		this(text, y, defaultImage, overImage);
		this.x = x;
	}

	public Button(String text, int y, Image defaultImage, Image overImage) {
		this.text = text;
		this.y = y;
		this.defaultImage = defaultImage;
		this.overImage = overImage;
		if (x == -1) {
			x = getCenterX();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getDefaultImage() {
		return defaultImage;
	}

	public Image getOverImage() {
		return overImage;
	}

	public void render(Graphics g) {
		if (hovering) {
			g.drawImage(overImage, x, y);
		} else {
			g.drawImage(defaultImage, x, y);
		}
		g.setColor(Color.black);
		g.drawString(text, x+defaultImage.getWidth()/2-text.length()*5, y+defaultImage.getHeight()/2-10);
	}

	public void update(Input i) {
		if (mouseOver(i.getMouseX(), i.getMouseY())) {
			if (i.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				clicked = true;
			hovering = true;
		} else {
			hovering = false;
		}
	}

	public boolean mouseOver(int mouseX, int mouseY) {
		if (mouseX > x && mouseX < x + defaultImage.getWidth() && mouseY > y
				&& mouseY < y + defaultImage.getHeight()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean clicked() {
		return clicked;
	}

	public void release() {
		clicked = false;
	}

	public int getCenterX() {
		return (TetrisGame.WIDTH - defaultImage.getWidth()) / 2;
	}
}