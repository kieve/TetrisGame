package com.nash.games.tetris.facebook;

import com.nash.games.tetris.game.Piece;


public class FacebookPieceColours {
	
	public static final int
	b1 		= -14277082,
	b2 		= -13684945,
	ghost 	= -10066330,
	line 	= -16618362,
	tee		= -10092442,
	block 	= -6724096,
	two 	= -16622591,
	zed 	= -10092544,
	el 		= -6737152,
	rel 	= -16702346,
	
	gline 	= -14315607,
	gtee	= -7789687,
	gblock 	= -4421341,
	gtwo 	= -14319836,
	gzed 	= -7789789,
	gel 	= -4434397,
	grel 	= -14399591;
	
	public static int parseColour(int c){
		switch(c){
		case b1:
		case b2:
			return 0;
		case ghost:
			return 0;
		case gline:
		case line:
			return Piece.I;
		case gtee:
		case tee:
			return Piece.T;
		case gblock:
		case block:
			return Piece.O;
		case gzed:
		case zed:
			return Piece.Z;
		case gtwo:
		case two:
			return Piece.S;
		case gel:
		case el:
			return Piece.L;
		case grel:
		case rel:
			return Piece.J;
		default:
			return 0;
		}
	}
	
	public static int parseBoardColour(int c){
		switch(c){
		case b1:
		case b2:
			return 0;
		case ghost:
			return 0;
		case line:
			return Piece.I;
		case tee:
			return Piece.T;
		case block:
			return Piece.O;
		case zed:
			return Piece.Z;
		case two:
			return Piece.S;
		case el:
			return Piece.L;
		case rel:
			return Piece.J;
		default:
			return 0;
		}
	}
	
}
