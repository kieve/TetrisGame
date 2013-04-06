package com.nash.games.tetris.state;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.nash.games.tetris.TetrisGame;
import com.nash.games.tetris.ai.AIGame;
import com.nash.games.tetris.ai.Agent;
import com.nash.games.tetris.cache.Global;

public class GeneticState extends BasicGameState implements Runnable {
	public static final int ID = CustomState.GENETIC;
	
	private static int w, h, u, numberOfBoards;
	private ArrayList<AIGame> boards = new ArrayList<AIGame>();
	private int[] seeds;
	private int masterSeed;
	private Random rn;
	private Thread thread;
	boolean done;

	int popSize = 48;
	int maxGens = 100;
	float probMutate = 0.2f;
	int gamesPerAgent = 1; //Greatly increases execution time!
	float eliteRate = 0.2f;
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		w = TetrisGame.SW;
		h = TetrisGame.SH;
		u = 13;
		numberOfBoards = popSize;
		
		masterSeed = 31337;
		rn = new Random(masterSeed);
		
		seeds = new int[numberOfBoards];
		
		for(int i = 0; i < seeds.length; i++){
			seeds[i] = rn.nextInt();
		}
	}

	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		//TODO
		
		//AppGameContainer a = (AppGameContainer)container;
		//a.setDisplayMode(TetrisGame.SW, TetrisGame.SH, true);
		done = false;
		thread = new Thread(this);
		thread.start();
	}

	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		done = true;
		AppGameContainer a = (AppGameContainer)container;
		a.setDisplayMode(TetrisGame.WIDTH, TetrisGame.HEIGHT, false);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//g.setColor(Color.white);
		//g.fillRect(0, 0, w, h);
		//for(int i = 0; i < boards.size(); i++){
			//boards.get(i).render(g);
		//}
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if(container.getInput().isKeyPressed(Keyboard.KEY_ESCAPE) || !thread.isAlive()){
			game.enterState(MenuState.ID);
		}
	}
	
	public void run() {
		ArrayList<Agent[]> hist = new ArrayList<Agent[]>();

		// popSize par, importante caso haja elitismo
		if (popSize % 2 == 1)
			popSize += 1;

		int elite = (int) (popSize * eliteRate);

		// elite par, importante caso haja elitismo
		if (elite % 2 == 1)
			elite += 1;

		int currentGeneration = 0;
		
		Agent[] population = new Agent[popSize];
		Agent bestAgent = null;

		// cria populacao inicial e inicia fitness
		for (int i = 0; i < population.length; i++)
			population[i] = Agent.randomAgent();

		int seed = rn.nextInt();

		do {
			CountDownLatch doneSignal = new CountDownLatch(popSize * gamesPerAgent);
			for (int i = 0; i < gamesPerAgent; i++) {
				boards.clear();
				int track = 0;
				for(int r = 0; r < 4; r++){
					for(int c = 0; c < 12; c++){
						AIGame game = new AIGame(c * u * Global.BOARD_W + 2, r * u * Global.BOARD_H + 2, u, i + seed, doneSignal);
						game.agent = population[track];
						boards.add(game);
						new Thread(game).start();
						track++;
					}
				}
			}

			try {
				doneSignal.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(done)
				break;
			// calcula fitness
			for (Agent agent : population) {
				float average = 0.0f;

				for (Integer cleared : agent.clearedRowsArray)
					average += cleared;
				agent.fitness = 1.0f * average / gamesPerAgent;
				agent.clearedRowsArray.clear();

			}

			// atualiza bestAgent
			for (Agent agent : population)
				if (bestAgent == null || agent.fitness > bestAgent.fitness)
					bestAgent = agent;

			Agent[] newPopulation = new Agent[popSize];
			java.util.Arrays.sort(population);

			// elite
			//Agent[] eliteArray = new Agent[elite];
			for (int i = 0; i < elite; i++)
				newPopulation[i] = population[popSize - i - 1];

			int newIndex = elite;
			for (int i = 0; i < (popSize - elite) / 2; i++) {
				Agent parentA = doSelection(population);
				Agent parentB = doSelection(population);

				Agent[] children = blendCrossover(parentA, parentB);
				Agent childA = children[0];
				Agent childB = children[1];

				mutateNoise(childA, probMutate);
				mutateNoise(childB, probMutate);

				newPopulation[newIndex] = childA;
				newIndex++;
				newPopulation[newIndex] = childB;
				newIndex++;
			}

			hist.add(population);
			population = newPopulation;
			currentGeneration++;

			System.out.println("Generation of the best " + currentGeneration + ": " + bestAgent.fitness);
			for (int i = 0; i < bestAgent.genes.length; i++)
				System.out.print(bestAgent.genes[i] + "f,");
			System.out.println();

		} while (currentGeneration < maxGens);
		done = true;
		GeneticState.formatResults(hist, "C:/users/nash/desktop/outputGA.csv");
	}
	
	
	
	
	public static Agent[] blendCrossover(Agent father, Agent mother) {
		Agent copy1 = father.cloneAgent();
		Agent copy2 = mother.cloneAgent();

		Random random = new Random();
		int N_GENES = father.genes.length;
		int index = random.nextInt(N_GENES);

		for (int i = index; i < N_GENES; i++) {
			float beta = random.nextFloat();
			float var1 = father.genes[i];
			float var2 = mother.genes[i];

			float newVar1 = beta * var1 + (1 - beta) * var2;
			float newVar2 = (1 - beta) * var1 + beta * var2;

			copy1.genes[i] = newVar1;
			copy2.genes[i] = newVar2;
		}

		return new Agent[] { copy1, copy2 };
	}

	public static void mutateNoise(Agent agent, float probMutate) {
		int N_GENES = agent.genes.length;
		Random random = new Random();

		for (int i = 0; i < N_GENES; i++) {
			float sort = random.nextFloat();
			if (sort < probMutate) {

				float prob = random.nextFloat();
				if (prob < 0.5f) {
					float genValue = agent.genes[i];
					float complement = 1.0f - genValue;
					float noise = random.nextFloat() * complement;
					agent.genes[i] += noise;

				} else {
					float genValue = agent.genes[i];
					float noise = random.nextFloat() * genValue;
					agent.genes[i] -= noise;
				}
			}
		}
	}

	public static Agent doSelection(Agent[] population) {
		Agent sorted = null;

		float totalFitness = 0.0f;
		for (Agent agent : population)
			totalFitness += agent.fitness;

		float frac = new Random().nextFloat();
		float cut = frac * totalFitness;

		for (Agent agent : population) {
			cut -= agent.fitness;
			if (cut <= 0.0f) {
				sorted = agent;
				break;
			}
		}
		return sorted;
	}

	/* formata output em .csv */
	public static void formatResults(ArrayList<Agent[]> generations, String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file, false));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		int gens = generations.size();

		// header
		for (int i = 0; i < gens; i++) {
			try {
				if (i == gens - 1)
					writer.write("g" + i + "\n");
				else
					writer.write("g" + i + ",");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		int pop = generations.get(0).length;
		for (int indiv = 0; indiv < pop; indiv++) {
			// dados
			for (int i = 0; i < gens; i++) {
				Agent agent = generations.get(i)[indiv];
				float fitness = agent.fitness;

				try {
					if (i == gens - 1)
						writer.write(fitness + "\n");
					else
						writer.write(fitness + ",");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		try {
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public int getID() {
		return ID;
	}
}
