import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Only mismatches in this class
 * 1. layer
 * 2. In output layer, everything is 0
 */
public class NeuralNet {
	NeuralLayer[] neuralNet;
	
	public NeuralNet() {
		neuralNet = new NeuralLayer[3];
		neuralNet[0] = new NeuralLayer(4, 1, 1);
		neuralNet[1] = new NeuralLayer(2, 4+1, 2);
		neuralNet[2] = new NeuralLayer(1, 2+1, 3);
		return;
	}
	
	public void run(int FLAG, String[] args) {
		switch(FLAG) {
		case 100: { // forward propagation
			this.init(args);
			forwardProp(true);
			break;
		}
		//////////////////////////////////////////////
		case 200: {
			this.init(args);
			forwardProp(false);
			double y = Double.parseDouble(args[18]);
			double delta = this.neuralNet[2].d(y, 0);
			System.out.printf("%.5f\n", delta);
			break;	
		}
		//////////////////////////////////////////////
		case 300: {
			this.init(args);
			forwardProp(false);
			double y = Double.parseDouble(args[18]);
			double L3delta = this.neuralNet[2].d(y, 0);
			double[] L2deltas = new double[3];
			for (int j = 1; j <= 2; j++) {
				double w = this.neuralNet[2].weights[0][j];
				double a = this.neuralNet[1].outputs[j];
				L2deltas[j] = L3delta * w * a * (1 - a);
			}
			System.out.printf("%.5f %.5f\n", L2deltas[1], L2deltas[2]);
			break;
		}
		//////////////////////////////////////////////
		case 400: {
			this.init(args);
			forwardProp(false);
			double y = Double.parseDouble(args[18]);
			double L3delta = this.neuralNet[2].d(y, 0);
			// for L3
			double [] L3grads = new double[3];
			for (int k = 0; k <= 2; k++) {
				L3grads[k] = L3delta * this.neuralNet[1].outputs[k];
				System.out.printf("%.5f", L3grads[k]);
				if (k < 2)	System.out.print(" ");
			}
			System.out.println();
			// for L2
			double[] L2deltas = new double[3];
			double[][] L2grads = new double[3][5];
			for (int j = 1; j <= 2; j++) {
				double w = this.neuralNet[2].weights[0][j];
				double a = this.neuralNet[1].outputs[j];
				L2deltas[j] = L3delta * w * a * (1 - a);
				for (int k = 0; k < 5; k++) {
					L2grads[j][k] = L2deltas[j] * this.neuralNet[0].outputs[k];
					System.out.printf("%.5f", L2grads[j][k]);
					if (k < 4)	System.out.print(" ");
				}
				System.out.println();
			}
			break;
		}
		//////////////////////////////////////////////
		case 500: {
			ArrayList<String[]> evalTokens = this.loadEvalFile();
			double stepSize = Double.parseDouble(args[14]);
			this.initWeights(args);
			ArrayList<String[]> dataset = this.loadTrainingFile();
			for (String[] inputs: dataset) {
				this.updateInputs(inputs);
				forwardProp(false);
				double y = Double.parseDouble(inputs[4]);
				double L3delta = this.neuralNet[2].d(y, 0);
				// for L3
				double [] L3grads = new double[3];
				for (int k = 0; k <= 2; k++) {
					L3grads[k] = L3delta * this.neuralNet[1].outputs[k];
				}

				// for L2
				double[] L2deltas = new double[3];
				double[][] L2grads = new double[3][5];
				for (int j = 1; j <= 2; j++) {
					double w = this.neuralNet[2].weights[0][j];
					double a = this.neuralNet[1].outputs[j];
					L2deltas[j] = L3delta * w * a * (1 - a);
					for (int k = 0; k < 5; k++) {
						L2grads[j][k] = L2deltas[j] * this.neuralNet[0].outputs[k];
					}
				}
				this.updateWeights(L2grads, L3grads, stepSize, true);
				this.evaluate(evalTokens, true);
			}
			break;
		}
		//////////////////////////////////////////////
		case 600: {
			//training model
			ArrayList<String[]> evalTokens = this.loadEvalFile();
			double stepSize = Double.parseDouble(args[14]);
			this.initWeights(args);
			ArrayList<String[]> dataset = this.loadTrainingFile();
			for (String[] inputs: dataset) {
				this.updateInputs(inputs);
				forwardProp(false);
				double y = Double.parseDouble(inputs[4]);
				double L3delta = this.neuralNet[2].d(y, 0);
				// for L3
				double [] L3grads = new double[3];
				for (int k = 0; k <= 2; k++) {
					L3grads[k] = L3delta * this.neuralNet[1].outputs[k];
				}
				// for L2
				double[] L2deltas = new double[3];
				double[][] L2grads = new double[3][5];
				for (int j = 1; j <= 2; j++) {
					double w = this.neuralNet[2].weights[0][j];
					double a = this.neuralNet[1].outputs[j];
					L2deltas[j] = L3delta * w * a * (1 - a);
					for (int k = 0; k < 5; k++) {
						L2grads[j][k] = L2deltas[j] * this.neuralNet[0].outputs[k];
					}
				}
				this.updateWeights(L2grads, L3grads, stepSize, false);
			}
			//evaluate correctness
			CompleteEvaluate();
			break;
		}
		}
		
	}
	
	@SuppressWarnings("resource")
	private ArrayList<String[]> loadTrainingFile() {
		File file = new File("train.csv");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			ArrayList<String[]> fileInputs = new ArrayList<String[]>();
			while (sc.hasNextLine()) { 
				String[] tokens = sc.nextLine().split(",");
				fileInputs.add(tokens);
			}
			return fileInputs;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void CompleteEvaluate() {
		//load test file
		File file = new File("test.csv");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ArrayList<String[]> testcases = new ArrayList<String[]>();
		while (sc.hasNextLine()) { 
			String[] tokens = sc.nextLine().split(",");
			testcases.add(tokens);
		}
		//run evaluation
		int hitNum = 0;
		int totalNum = 0;
		int prediction = 0;
		for (String[] inputs: testcases) {
			int actualLable = Integer.parseInt(inputs[4]);
			this.updateInputs(inputs);
			forwardProp(false);
			double confidence = this.neuralNet[2].outputs[0];
			prediction = confidence > 0.5? 1: 0;
			System.out.printf("%d %d %.5f\n", actualLable, prediction, confidence);
			if (prediction == actualLable) hitNum++;
			totalNum++;
		}
		double succRate = (double) hitNum / totalNum;
		System.out.printf("%.2f\n", succRate);
	}
	
	@SuppressWarnings("resource")
	private ArrayList<String[]> loadEvalFile() {
		File file = new File("eval.csv");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			ArrayList<String[]> testcases = new ArrayList<String[]>();
			while (sc.hasNextLine()) { 
				String[] tokens = sc.nextLine().split(",");
				testcases.add(tokens);
			}
			return testcases;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	private void evaluate(ArrayList<String[]> evalCases, boolean printOuputs) {
		double sumError = 0.0;
		for (String[] inputs: evalCases) {
			double y = Double.parseDouble(inputs[4]);
			this.updateInputs(inputs);
			forwardProp(false);
			double a = this.neuralNet[2].outputs[0];
			sumError += .5 * (a - y) * (a - y);
		}
		if (printOuputs)
			System.out.printf("%.5f\n", sumError);
	}
	
	private void updateWeights(double[][] L2grads, double[] L3grads, double stepSize, boolean printOutputs) { 
		this.neuralNet[1].updateWeightsL2(L2grads, stepSize, printOutputs);
		this.neuralNet[2].updateWeightsL3(L3grads, stepSize, printOutputs);
		if (printOutputs)	System.out.println();
	}
	
	private void initWeights(String[] args) {
		this.neuralNet[1].loadWeightsFromArgs(args, 1);
		this.neuralNet[2].loadWeightsFromArgs(args, 11);
	}
	
	private void updateInputs(String[] args) {
		this.neuralNet[0].loadInputFromArgs(args, 0);
	}
	private void init(String[] args) {
		this.neuralNet[1].loadWeightsFromArgs(args, 1);
		this.neuralNet[2].loadWeightsFromArgs(args, 11);
		this.neuralNet[0].loadInputFromArgs(args, 14);
	}
	
	private double forwardProp(boolean printActivations) {
		double[] tempOutputs;
		tempOutputs = this.neuralNet[1].calcOutputs(neuralNet[0].calcOutputs());
		if (printActivations)
			System.out.printf("%.5f %.5f\n", tempOutputs[1], tempOutputs[2]);
		tempOutputs = this.neuralNet[2].calcOutputs(tempOutputs);
		if (printActivations)
			System.out.printf("%.5f\n", tempOutputs[0]);
		return tempOutputs[0];
	}
	
	
	public static void main(String[] args) {
		////////////////////FOR TESTING////////////////////
		//String argLine = "500 .1 .2 .3 .4 .5 .5 .6 .7 .8 .9 .9 .5 .2 .1"; //FIXME
		//args = argLine.split(" ");
		///////////////////////////////////////////////////
		NeuralNet neuralNet = new NeuralNet();
		int FLAG = Integer.parseInt(args[0]);
		neuralNet.run(FLAG, args);
		
	}
	
	
}
