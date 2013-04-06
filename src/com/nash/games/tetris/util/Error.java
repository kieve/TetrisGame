package com.nash.games.tetris.util;

import java.util.LinkedList;
import java.util.Queue;

public class Error {
	public static final int LOADING_IMAGE = 1, INTEGER_PARSE = 2;

	private static Queue<Integer> ERRORS = new LinkedList<Integer>();

	public static void addError(int errorCode) {
		ERRORS.offer(errorCode);
	}

	public static int getErrorCount() {
		return ERRORS.size();
	}

	public static boolean hasErrors() {
		if (ERRORS.size() > 0)
			return true;
		return false;
	}
	
	public static void handleError(int numErrors){
		if(numErrors <= ERRORS.size()){
			for(int i = 0; i < numErrors; i++){
				switch(ERRORS.poll()){
					case LOADING_IMAGE:
						System.err.println("Error loading images.");
						System.exit(LOADING_IMAGE);
						break;
					case INTEGER_PARSE:
						System.err.println("Error parsing integer");
					default:
						System.err.println("Unknown Error Occured.");
						System.exit(-1);
				}
			}
		}
	}
}
