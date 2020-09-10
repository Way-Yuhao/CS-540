import java.util.ArrayList;

public class HClustering {
	ArrayList<Node> nodes;
	
	public HClustering() {
		nodes = new ArrayList<Node>();
		nodes.add(new Node("BOS", 42.4, 71.1));
		nodes.add(new Node("NY", 41.7, 74.0));
		nodes.add(new Node("DC", 38.9, 77.0));
		nodes.add(new Node("MIA", 25.8, 80.2));
		nodes.add(new Node("SLC", 40.8, 111.9));
		nodes.add(new Node("SEA", 47.6, 122.3));
		nodes.add(new Node("SD", 37.8, 122.4));
		nodes.add(new Node("LA", 34.1, 118.2));
		nodes.add(new Node("DEN", 39.7, 105.0));
		nodes.add(new Node("ALT", 33.7, 84.3));
	}
	private double calcDistance(Node n1, Node n2) {
		double n1x = 0;
		double n1y = 0;
		double n2x = 0;
		double n2y = 0;
		double curD = 0;
		double minD = 10000000;
		for (int i = 0; i < n1.xVals.size(); i++) {
			n1x = n1.xVals.get(i);
			n1y = n1.yVals.get(i);
			for (int j = 0; j < n2.xVals.size(); j++) {
				n2x = n2.xVals.get(j);
				n2y = n2.yVals.get(j);
				curD = Math.sqrt((n1x - n2x)*(n1x - n2x) + (n1y - n2y)*(n1y - n2y));
				minD = curD < minD ? curD : minD;
			}
		}
		return minD;
	}
	
	private void merge(int i1, int i2) {
		Node n1 = nodes.get(i1);
		Node n2 = nodes.remove(i2);
		n1.name = n1.name + "-" + n2.name;
		for (int i = 0; i < n2.xVals.size(); i++) {
			n1.addEntry(n2.xVals.get(i), n2.yVals.get(i));
		}
	}
			
	
	private void update() {
		double minDist = 100000.00;
		double curDist = 0;
		int nodeIndex1 = 0;
		int nodeIndex2 = 0;
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				curDist = calcDistance(nodes.get(i), nodes.get(j));
				if (curDist < minDist) {
					minDist = curDist;
					nodeIndex1 = i;
					nodeIndex2 = j;
				}
			}
		}
		merge(nodeIndex1, nodeIndex2);
	}
	
	void printAllNodes() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (Node n: nodes) {
			sb.append("(");
			sb.append(n.name);
			sb.append(")");
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		System.out.println(sb);
	}
	void run() {
		do {
			printAllNodes();
			update();
		} while (nodes.size() > 1);
		printAllNodes();
		return;
	}
	
	public static void main(String[] args) {
		HClustering hc = new HClustering();
		hc.run();
		return;
	}
}

class Node {
	ArrayList<Double> xVals;
	ArrayList<Double> yVals;
	String name;
	
	Node() {
		this.xVals = new ArrayList<Double>();
		this.yVals = new ArrayList<Double>();
		this.name = "";
	}
	
	Node(String name, double x, double y) {
		this();
		xVals.add(x);
		yVals.add(y);
		this.name = name;
	}
	
	void addEntry(double x, double y) {
		xVals.add(x);
		yVals.add(y);
	}
	
}