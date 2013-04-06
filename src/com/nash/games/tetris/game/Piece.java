package com.nash.games.tetris.game;

import org.newdawn.slick.Color;


public class Piece {
	public static final int
		I = 1,
		Z = 2,
		S = 3,
		L = 4,
		J = 5,
		T = 6,
		O = 7;

	public static final Color[] Colours = {
		new Color(0),
		new Color(0,150,175),
		new Color(150,0,0),
		new Color(0,125,0),
		new Color(200,100,0),
		new Color(0,50,150),
		new Color(125,0,125),
		new Color(200,150,0)
	};
	
	
	/* T-Piece || Example of Representation
	 * 
	 *           X
	 *        0 1 2 3 
	 *      
	 *    0  |4|4|4|0|
	 *  Y 1  |0|4|0|0|
	 *    2  |0|0|0|0|
	 *    3  |0|0|0|0|
	 * 
	 *  [0,0]
	 *  [1,0]
	 *  [2,0]
	 *  [1,1]
	 */ 
	
	/*  ARRAY EXPLAINATION
	 *  
	 *  piece [R][B][L]
	 *  
	 *  R - Current Rotation
	 *  B - Block within the piece (4 in each piece)
	 *  L - location, x,y relative to top left corner of piece
	 */
	
	private static final int[][][] LINE = {
		//Rotation 1:
		{
			{0,1},{1,1},{2,1},{3,1}
		},
		//Rotation 2:
		{
			{2,0},{2,1},{2,2},{2,3}
		},
		//Rotation 3:(Equal to the first, included for convenience)
		{
			{0,2},{1,2},{2,2},{3,2}
		},
		//Rotation 4:
		{
			{1,0},{1,1},{1,2},{1,3}
		}
	};
	
	private static final int[][][] ZED = {
		//Rotation 1:
		{
			{0,0},{1,0},{1,1},{2,1}
		},
		//Rotation 2:
		{
			{2,0},{2,1},{1,1},{1,2}
		},
		//Rotation 3:
		{
			{0,1},{1,1},{1,2},{2,2}
		},
		//Rotation 4:
		{
			{1,0},{0,1},{1,1},{0,2}
		}
	};

	private static final int[][][] ES = {
		//Rotation 1:
		{
			{1,0},{2,0},{0,1},{1,1}
		},
		//Rotation 2:
		{
			{1,0},{1,1},{2,1},{2,2}
		},
		//Rotation 3:
		{
			{1,1},{2,1},{0,2},{1,2}
		},
		//Rotation 4:
		{
			{0,0},{0,1},{1,1},{1,2}
		}
	};
	
	private static final int[][][] EL = {
		//Rotation 1:
		{
			{2,0},{0,1},{1,1},{2,1}
		},
		//Rotation 2:
		{
			{1,0},{1,1},{1,2},{2,2}
		},
		//Rotation 3:
		{
			{0,1},{1,1},{2,1},{0,2}
		},
		//Rotation 4:
		{
			{0,0},{1,0},{1,1},{1,2}
		}
	};
	
	private static final int[][][] JAY = {
		//Rotation 1:
		{
			{0,0},{0,1},{1,1},{2,1}
		},
		//Rotation 2:
		{
			{1,0},{2,0},{1,1},{1,2}
		},
		//Rotation 3:
		{
			{0,1},{1,1},{2,1},{2,2}
		},
		//Rotation 4:
		{
			{1,0},{1,1},{0,2},{1,2}
		}
	};

	private static final int[][][] TEE = {
		//Rotation 1:
		{
			{1,0},{0,1},{1,1},{2,1}
		},
		//Rotation 2:
		{
			{1,0},{1,1},{2,1},{1,2}
		},
		//Rotation 3:
		{
			{0,1},{1,1},{2,1},{1,2}
		},
		//Rotation 4:
		{
			{1,0},{0,1},{1,1},{1,2}
		}
	};

	private static final int[][][] OH = {
		//Only rotation:
		{
			{1,0},{2,0},{1,1},{2,1}
		},
		{
			{1,0},{2,0},{1,1},{2,1}
		},
		{
			{1,0},{2,0},{1,1},{2,1}
		},
		{
			{1,0},{2,0},{1,1},{2,1}
		}
	};
	
	private static final int[][][][] allPieces = {
		null, LINE, ZED, ES, EL, JAY, TEE, OH 
	};
	
	public static int[][] getStuct(int piece){
		return allPieces[piece][0];
	}	

	
	private int 
		pieceID,
		currentRotation,
		x,y;

	public Piece(int pieceID, int x, int y){
		this.pieceID = pieceID;
		this.x = x;
		this.y = y;
		currentRotation = 0;
	}
	
	public int getPieceID(){
		return pieceID;
	}

	public int[][][] getPieceStructure(){
		return allPieces[pieceID];
	}
		
	public int[][] getCurrentStructure(){
		return allPieces[pieceID][currentRotation];
	}

	public int getCurrentRotation(){
		return currentRotation;
	}
	
	public int[] getLocation(){
		return new int[]{x,y};
	}
	
	public void hold(){
		x = -1;
		y = -1;
	}
	
	public void rotate(int dir){
		currentRotation = getFutureRotation(dir);
	}
	
	public void setRoation(int rot){
		currentRotation = rot;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getFutureRotation(int dir){
		int temp = currentRotation + dir;
		if(temp < 0)
			temp = 3;
		if(temp > 3)
			temp = 0;
		return temp;
	}
	
	public void sendToTop(){
		y = -1;
	}
	
	public void moveDown(){
		y++;
	}
	
	public void moveUp(){
		y--;
	}
	
	public void moveLeft(){
		x--;
	}
	
	public void moveRight(){
		x++;
	}
	
	public Piece clonePiece(){
		Piece p = new Piece(pieceID, x, y);
		p.setRoation(currentRotation);
		return p;
	}
}
