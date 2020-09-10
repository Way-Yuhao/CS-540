import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.AbstractMap.SimpleEntry;

public class BodyVsBrain {
	/*fields*/
	ArrayList<Entry<Double, Double>> dataSet;
	Random rd;
	int n; //number of data points
	
	BodyVsBrain() {
		dataSet = new ArrayList<Map.Entry<Double, Double>>();
		rd = new Random(2);
	}
	
	private double calcMean(int option) {
		double sum = 0;
		if (option == 0) { //body weight
			for (Entry<Double, Double> d: dataSet) {
				sum += (double)d.getKey();
			}
		} else { //brain weight
			if (option == 1) {
				for (Entry<Double, Double> d: dataSet) {
					sum += (double)d.getValue();
				}
			}
		}
		sum = sum / this.n;
		return sum;
	}
	
	private double calcStd(int option, boolean ph) {
		double std = 0;
		double m = calcMean(option);
		if (option == 0) { //body weight
			for (Entry<Double, Double> d: dataSet) {
				std += (d.getKey() - m) * (d.getKey() - m);
			}
		} else { //brain weight
			for (Entry<Double, Double> d: dataSet) {
				std += (d.getValue() - m) * (d.getValue() - m);
			}
		}
		std = Math.sqrt(std / (n-1));
		//print
		if (ph) {
			System.out.printf("%.4f %.4f\n", m, std);
		}
		return std;
	}
	
	private double[] gradDecend(double b0, double b1, boolean ph, boolean approximate) {
		double g1 = 0;
		double g2 = 0;
		if (!approximate) { //standard gradient descend calculation
			double ct1 =0;
			double ct2 = 0;
			for (Map.Entry<Double, Double> e: dataSet) {
				ct1 = b0 + b1 * e.getKey() - e.getValue();
				ct2 = ct1 * e.getKey();
				g1 += ct1;
				g2 += ct2;
			}
			g1 = 2 * g1 / n;
			g2 = 2 * g2 / n;
			if (ph) //print here
				System.out.printf("%.4f\n%.4f", g1, g2);
		} else { //approximate for FLAG 800
			int t = rd.nextInt(n);
			Entry<Double, Double> e = dataSet.get(t);
			double xjt = e.getKey();
			double yjt = e.getValue();
			g1 = 2 * (b0 + b1 * xjt - yjt);
			g2 = g1 * xjt;
		}
		double[] gs = {g1, g2};
		return gs;
	}
	
	private void normalizeX() {
		double xAvg = this.calcMean(0);
		double xStd = this.calcStd(0, false);
		double x, y;
		ArrayList<Entry<Double, Double>> temp = new ArrayList<Map.Entry<Double, Double>>();
		for (Map.Entry<Double, Double> e : dataSet) {
			x = (e.getKey() - xAvg) / xStd;
			y = e.getValue();
			temp.add(new SimpleEntry<Double, Double>(x, y));
		}
		this.dataSet = temp;
	}
	
	private double mse(double b0, double b1, boolean ph) {
		double mse = 0;
		double curTerm = 0;
		for (Map.Entry<Double, Double> e: dataSet) {
			curTerm = b0 + b1 * e.getKey() - e.getValue();
			curTerm = curTerm * curTerm;
			mse += curTerm;
		}
		mse = mse / n;
		if (ph)
			System.out.printf("%.4f", mse);
		return mse;
	}
	void run(int FLAG, String[] args) {
		double b0, b1, mse_b0_b1;
		double denor, numor, avgX, avgY;
		switch (FLAG) {
		case 100:
			System.out.println(this.n);
			calcStd(0, true);
			calcStd(1, true);
			break;
		///////////////////////////////////////////////////////
		case 200:
			b0 = Double.parseDouble(args[1]);
			b1 = Double.parseDouble(args[2]);
			mse(b0, b1, true);
			break;
		///////////////////////////////////////////////////////
		case 300:
			b0 = Double.parseDouble(args[1]);
			b1 = Double.parseDouble(args[2]);
			this.gradDecend(b0, b1, true, false);
			break;
		///////////////////////////////////////////////////////
		case 700:
		case 800:
			this.normalizeX();
		case 400:
			//set the approximate flag to true for 800
			boolean approximate = FLAG == 800 ? true: false; 
			double co = Double.parseDouble(args[1]); 
			double T = Double.parseDouble(args[2]); //max iteration cap
			double b0c, b1c;  //current b0 and b1
			double b0p = 0; //past b0
			double b1p = 0; //past b1
			mse_b0_b1 = 0; 
			for (int t = 1; t <= T; t++) {
				double[] gs = this.gradDecend(b0p, b1p, false, approximate);
				b0c = b0p - co * gs[0];
				b1c = b1p - co * gs[1];
				mse_b0_b1 = mse(b0c, b1c, false);
				System.out.printf("%d %.4f %.4f %.4f\n", t, b0c, b1c, mse_b0_b1);
				b0p = b0c;
				b1p = b1c;
			}
			break;
		///////////////////////////////////////////////////////
		case 500:
			denor = 0;
			numor = 0;
			b0 = 0;
			b1 = 0;
			avgX = this.calcMean(0);
			avgY = this.calcMean(1);
			for (Map.Entry<Double, Double> e: dataSet) {
				numor += (e.getKey() - avgX) * (e.getValue() - avgY);
				denor += (e.getKey() - avgX) * (e.getKey() - avgX);
			}
			b1 = numor / denor;
			b0 = avgY - b1 * avgX;
			mse_b0_b1 = this.mse(b0, b1, false);
			System.out.printf("%.4f %.4f %.4f", b0, b1, mse_b0_b1);
			break;
		///////////////////////////////////////////////////////
		case 600:
			double bodyWeight = Double.parseDouble(args[1]);
			denor = 0;
			numor = 0;
			b0 = 0;
			b1 = 0;
			avgX = this.calcMean(0);
			avgY = this.calcMean(1);
			for (Map.Entry<Double, Double> e: dataSet) {
				numor += (e.getKey() - avgX) * (e.getValue() - avgY);
				denor += (e.getKey() - avgX) * (e.getKey() - avgX);
			}
			b1 = numor / denor;
			b0 = avgY - b1 * avgX;
			double pBrainWeight = b0 + b1 * bodyWeight;
			System.out.printf("%.4f", pBrainWeight);
			break;
		}	
	}
	
	
	public static void main(String[] args) {
		BodyVsBrain bb = new BodyVsBrain();
		// reading files
		File f = new File("./data.csv");
		Scanner sc;
		try {
			sc = new Scanner(f);
			String nl = null;
			String[] data = new String[2];
			sc.nextLine(); //skip first line
			while (sc.hasNextLine()) {
				nl = sc.nextLine();
				data = nl.split(",");
				SimpleEntry<Double, Double> e = 
						new SimpleEntry<Double, Double>(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
				bb.dataSet.add(e);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bb.n = bb.dataSet.size();
		
		////////////////FOR TESTING///////////////
		//String al = "700 0.01 5"; //FIXME
		//args = al.split(" ");
		/////////////////////////////////////////
		bb.run(Integer.parseInt(args[0]), args);
	}
}
