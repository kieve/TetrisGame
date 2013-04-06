package com.nash.games.tetris.ai;
public class BoardEvaluator {
	public static int clearedRows(int[][] board) {
		int cleared = 0;
		int columns = board[0].length;

		for (int y = 0; y < board.length; y++) {
			boolean ok = true;
			for (int x = 0; (x < columns) && ok; x++)
				if (board[y][x] == 0)
					ok = false;

			if (ok)
				cleared++;
		}
		return cleared;
	}

	public static int pileHeight(int[][] board) {
		int columns = board[0].length;

		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < columns; x++) {
				if (board[y][x] != 0)
					return board.length - y;
			}
		}
		return 0;
	}

	public static int countSingleHoles(int[][] board) {
		int holes = 0;
		int columns = board[0].length;

		for (int j = 0; j < columns; j++) {
			boolean swap = false;
			for (int i = 0; i < board.length; i++) {
				if (board[i][j] != 0)
					swap = true;
				else {
					if (swap)
						holes++;
				}
			}
		}
		return holes;
	}

	public static int countConnectedHoles(int[][] board) {
		int holes = 0;
		int columns = board[0].length;

		for (int j = 0; j < columns; j++) {
			boolean swap = false;
			for (int i = 0; i < board.length; i++) {
				if (board[i][j] != 0)
					swap = true;
				else {
					if (swap)
						holes++;
					swap = false;
				}
			}
		}

		return holes;
	}

	public static int blocksAboveHoles(int[][] board) {
		int blocks = 0;
		int cols = board[0].length;

		for (int c = 0; c < cols; c++) {
			boolean swap = false;
			for (int r = board.length - 1; r >= 0; r--) {
				if (board[r][c] == 0)
					swap = true;
				else {
					if (swap)
						blocks++;
				}
			}
		}
		return blocks;
	}

	public static int countWells(int[][] board) {
		int cols = board[0].length;
		int wells = 0;

		// da segunda até a penultima coluna
		for (int col = 1; col < cols - 1; col++) {
			for (int row = 0; row < board.length; row++) {
				if ((board[row][col] > 0) && (board[row][col] > 0) && (board[row][col] == 0))
					wells++;
				else if (board[row][col] > 0)
					break;
			}
		}

		// primeira coluna
		for (int row = 0; row < board.length; row++) {
			if ((board[row][1] == 0) && (board[row][2] > 0))
				wells++;
			else if (board[row][1] > 0)
				break;
		}
		// ultima coluna
		for (int row = 0; row < board.length; row++) {
			if ((board[row][cols - 2] > 0) && (board[row][cols - 1] == 0))
				wells++;
			else if (board[row][cols - 1] > 0)
				break;
		}
		return wells;
	}

	public static float bumpiness(int[][] board) {
		int cols = board[0].length;
		int[] heights = new int[cols];

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < board.length; y++) {
				if (board[y][x] != 0) {
					heights[x] = board.length - y;
					break;
				}
			}
		}

		float bmp = 0.0f;
		for (int i = 0; i < cols - 2; i++) {
			float diff = Math.abs(heights[i] - heights[i + 1]);
			bmp += diff;
		}

		return bmp;
	}
} // fim da classe BoardEvaluator
