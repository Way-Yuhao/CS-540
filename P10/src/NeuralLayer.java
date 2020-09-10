import java.math.*;
public class NeuralLayer {
	int units; //including bias unit, if any
	boolean isInputLayer;
	boolean isOutputLayer;
	double[] inputs;
	double[][] weights;
	double[] outputs;
	public NeuralLayer(int nonBiasUnits, int numInputs, int layerNum) {
		if (layerNum == 1) { //input layer
			this.inputs = new double[1];
			this.weights = null;
			this.outputs = new double[nonBiasUnits + 1];
			this.units = nonBiasUnits + 1;
			this.isInputLayer = true;
			this.isOutputLayer = false;
		} else if (layerNum == 3) { //output layer
			this.inputs = new double[numInputs];
			this.weights = new double[1][numInputs];
			this.outputs = new double[1];
			this.units = 1;
			this.isInputLayer = false;
			this.isOutputLayer = true;
		} else { //hidden layers
			this.inputs = new double[numInputs];
			this.weights = new double[nonBiasUnits + 1][numInputs];
			this.outputs = new double[nonBiasUnits + 1];
			this.units = nonBiasUnits + 1;
			this.isInputLayer = false;
			this.isOutputLayer = false;
		}		
	}
	
	public void loadInputFromArgs(String[] args, int startIndex) {
		outputs[0] = 1;
		for (int i = 1; i < this.units; i++) {
			outputs[i] = Double.parseDouble(args[startIndex]);
			startIndex++;
		}
	}
	
	public void loadWeightsFromArgs(String[] args, int startIndex) {
		if (this.isOutputLayer) {
			for (int j = 0; j < weights[0].length; j++) {
				weights[0][j] = Double.parseDouble(args[startIndex]);
				startIndex++;
			}
		} else {
			for (int i = 1; i < weights.length; i++) {
				for (int j = 0; j < weights[i].length; j++) { //skipping bias node
					weights[i][j] = Double.parseDouble(args[startIndex]);
					startIndex++;
				}
			}
		}
	}
	
	public void updateWeightsL2(double[][] L2grads, double stepSize, boolean printUpdates) {
		for (int j = 1; j < weights.length; j++) {
			for (int k = 0; k < weights[j].length; k++) {
				weights[j][k] = weights[j][k] - stepSize * L2grads[j][k];
				if (printUpdates)
					System.out.printf("%.5f ", weights[j][k]);
			}
		}
	}
	
	public void updateWeightsL3(double[] L3grads, double stepSize, boolean printUpdates) {
		for (int k = 0; k < weights[0].length; k++) {
			weights[0][k] = weights[0][k] - stepSize * L3grads[k];
			if (printUpdates) {
				System.out.printf("%.5f", weights[0][k]);
				if (k != 2)	System.out.print(" ");
			}
				
		}
	}
	
	double[] calcOutputs(double[] replacingInputs) {
		this.inputs = replacingInputs;
		return this.calcOutputs();
	}
	
	double[] calcOutputs() {
		if (this.isInputLayer) {
			return this.outputs;
		} else {
			//double[] outputs = new double[this.units];
			for (int unitNum = 0; unitNum < this.units; unitNum++) {
				outputs[unitNum] = calcOutputAt(unitNum);
			}
			return outputs;
		}
	}
	
	private double calcOutputAt(int unitNum) {
		if (!this.isOutputLayer && unitNum == 0) {
			return 1;
		} else {
			double sum = 0;
			for (int j = 0; j < this.inputs.length; j++) {
				sum += inputs[j] * weights[unitNum][j];
			}
			return g(sum);
		}
	}
	
	public double d(double y, int unitNum) {
		if (this.isOutputLayer) { //output layer
			double a = outputs[0];
			double delta = (a - y) * a * (1 - a);
			return delta;
		} else { //hidden layer
			double a = outputs[unitNum];
			double delta = (a - y) * a * (1 - a);
			return delta;
		}
	}
	
	private double g(double z) {
		return 1.0 / (1.0 + Math.exp(-z));
	}
	
}
