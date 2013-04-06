package com.nash.games.tetris.ai;

import java.util.ArrayList;

import com.nash.games.tetris.cache.Global;
import com.nash.games.tetris.game.Board;
import com.nash.games.tetris.game.Move;
import com.nash.games.tetris.game.Piece;

public class MoveFinder {

	public static ArrayList<Move> getValidMoves(Board board, Piece piece, Agent agent) {
		ArrayList<Move> moves = new ArrayList<Move>();

		for (int i = 0; i < 4; i++) {
			piece.sendToTop();
			piece.setRoation(i);

			while (!board.movePieceLeft(piece));

			do {
				while (!board.movePieceDown(piece));

				int[][] newBoard = new int[Global.BOARD_H][Global.BOARD_W];
				boolean valid = !Board.getPossibleBoard(board.getBoard(), newBoard, piece, false);
				
				if(valid){
					float eval = agent.eval(newBoard);
					Move tempMove = new Move(piece.getLocation()[0], piece.getLocation()[1], piece.getCurrentRotation());
					tempMove.eval = eval;

					moves.add(tempMove);
				}
				piece.sendToTop();
				if (board.movePieceRight(piece)) {
					break;
				}
			} while (true);
		}

		return moves;
	}

	public static Move getBestMove(Board board, Piece piece, Agent agent) {
		Move best = null;
		ArrayList<Move> moves = getValidMoves(board, piece, agent);
		for (Move move : moves) {
			if (best == null)
				best = move;
			else if (move.eval > best.eval)
				best = move;
		}
		return best;
	}

}