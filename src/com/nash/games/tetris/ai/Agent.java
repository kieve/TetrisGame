package com.nash.games.tetris.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Agent implements Comparable<Object> {
	//public float[] genes = new float[] { 0.7700537f, 0.2063281f, 0.566688f, 0.8481199f, 0.0463275f, 0.234731f, 0.1536644f }; //~20-70k
	//public float[] genes = new float[] { 0.29738644f,0.14356345f,0.9223646f,0.85449415f,0.14350979f,0.4688262f,0.29968145f }; //300k?
	public float[] genes = new float[] { 0.9744048f,0.1114008f,0.9384629f,0.8245546f,0.11661559f,0.49166977f,0.27631107f }; //600k?
	
	public List<Integer> clearedRowsArray = Collections.synchronizedList(new ArrayList<Integer>());
	public float fitness;

	public float eval(int[][] board) {
		float sum = 0.0f;

		sum += genes[0] * BoardEvaluator.clearedRows(board);
		sum -= genes[1] * BoardEvaluator.pileHeight(board);
		sum -= genes[2] * BoardEvaluator.countSingleHoles(board);
		sum -= genes[3] * BoardEvaluator.countConnectedHoles(board);
		sum -= genes[4] * BoardEvaluator.blocksAboveHoles(board);
		sum -= genes[5] * BoardEvaluator.countWells(board);
		sum -= genes[6] * BoardEvaluator.bumpiness(board);

		return sum;
	}

	public int compareTo(Object obj) {
		if (obj instanceof Agent) {
			Agent other = (Agent) obj;
			if (this.fitness > other.fitness)
				return 1;
			else if (this.fitness < other.fitness)
				return -1;
		}
		return 0;
	}

	public static Agent randomAgent() {
		Agent agent = new Agent();
		Random random = new Random();

		for (int i = 0; i < agent.genes.length; i++)
			agent.genes[i] = random.nextFloat();

		return agent;
	}

	public Agent cloneAgent() {
		Agent cloneAgent = new Agent();

		for (int i = 0; i < this.genes.length; i++)
			cloneAgent.genes[i] = this.genes[i];

		return cloneAgent;
	}
}
