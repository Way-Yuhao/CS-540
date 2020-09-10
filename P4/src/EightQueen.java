import java.util.*;
import java.lang.Math;

public class EightQueen {
	//class fields
	final static int BOARDLENGTH = 8;
	final static double ANNEAL_RATE = 0.95;
	int temperature = 100;
	int option;
	int maxIteration;
	int seed;
	State origin;
	ArrayList<State> localOptimums;
	State globalOptima;
	Random randGen;
	StringBuilder output;
	
	
	EightQueen(int option, int maxIteration, int seed, State origin) {
		this.option = option;
		this.maxIteration = maxIteration;
		this.seed = seed;
		this.origin = origin;
		this.localOptimums = new ArrayList<State>();
		this.globalOptima = null;
		this.randGen = new Random();
		if (seed != -1)	randGen.setSeed(seed);
		this.output = new StringBuilder();
		return;
	}
	
	public void run() {
		State curSt;
		switch (option) {
		case 1:
			output.append(this.origin.cost);
			break;
		case 2:
			//if origin is a goal state, no output
			if (origin.sovled)
				break;
			//print all one-step steepest successors
			//NOTE: specified order required
			List<State> optimalSuccs = getAllOptimalSucc(origin);
			for (State succ: optimalSuccs) {
				output.append(succ.printState(this.option, -1));
				output.append("\n");
			}
			output.append(origin.cost == 0? 0: optimalSuccs.get(0).cost);
			break;
		case 3:
			//append origin
			output.append(origin.printState(this.option, 0));
			output.append("\n");
			if (origin.sovled) {
				output.append("Solved");
				break;
			}
			curSt = this.origin;
			int r = 0;
			//for every step until reaching maxIteration
			for (int step = 1; step <= this.maxIteration; step++) {
				//randomly choose 1 of the successor, make it current
				List<State> succList = getAllOptimalSucc(curSt);
				r = randGen.nextInt(succList.size());
				curSt = succList.get(r);
				output.append(curSt.printState(option, step));
				output.append("\n");
				if (curSt.sovled) { //stop if solved
					output.append("Solved");
					break;
				}
			}
			break;
		case 4:
			//append origin
			output.append(origin.printState(this.option, 0));
			output.append("\n");
			if (origin.sovled) {
				output.append("Solved");
				break;
			}
			curSt = this.origin;
			//for every step until reaching maxIteration
			for (int step = 1; step <= this.maxIteration; step++) {
				//go to the first improving successor, make it current
				curSt = getFirstSucc(curSt);
				if (curSt == null) {
					output.append("Local optimum");
					break;
				}
				output.append(curSt.printState(option, step));
				output.append("\n");
				if (curSt.sovled) { //stop if solved
					output.append("Solved");
					break;
				}
				
			}
			break;
		case 5:
			int index, value;
			double prob, lowerBound;
			State succ;
			//append origin
			output.append(origin.printState(this.option, 0));
			output.append("\n");
			if (origin.sovled) {
				output.append("Solved");
				break;
			}
			curSt = this.origin;
			//for every step until reaching maxIteration
			for (int step = 1; step <= maxIteration; step++) {
				//call random to generate a new queen position and a possibility
				index = randGen.nextInt(BOARDLENGTH - 1);
				value = randGen.nextInt(BOARDLENGTH - 1);
				lowerBound = randGen.nextDouble();
				succ = genSucc(curSt, index, value);
				prob = genProb(curSt, succ);
				if (prob == 0.00) {
					break;
				} else if (prob - lowerBound >= 0) { //determine whether to go to this successor
					curSt = succ;
					output.append(curSt.printState(option, step));
					output.append("\n");
					if (succ.sovled) { //stop when solved
						output.append("Solved");
						break;
					}
				}
			}
			break;
		default:
			//ERROR
			break;
		}
		this.flush();
	}
	
	private State genSucc(State parent, int colToMod, int targetRow) {
		int[] parentBoard = parent.board;
		int[] succBoard = new int[BOARDLENGTH];
		for (int j = 0; j < BOARDLENGTH; j++) {
			if (j != colToMod) {
				succBoard[j] = parentBoard[j];
			} else { //modify given column
				succBoard[j] = targetRow;
			}
		}
		
		State succ = new State(succBoard);
		return succ;
	}
	
	private State getFirstSucc(State parent) {
		State succ = null; //temp storage for successors
		for (int col = 0; col < BOARDLENGTH; col++ ) {
			//attempt to move current queen to the top and then downwards
			for (int row = 0; row < BOARDLENGTH; row++) {
				if (parent.board[col] == row) { //if target cell is occupied
					continue;
				} else {
					succ = genSucc(parent, col, row); //generate a new successor
					if (succ.cost < parent.cost)
						return succ;
				}
			}
		}
		return null; //if no successors are found
	}
	
	private List<State> getAllOptimalSucc(State parent) {
		List<State> optimalSuccList = new LinkedList<State>();
		//points to the last successor in the list with updated optimal cost
		int curOptimalCost= parent.cost;
		State succ = null; //temp storage for successors
		//iterating through each possible successors
		//starting from the left column and moves rightwards
		for (int col = 0; col < BOARDLENGTH; col++ ) {
			//attempt to move current queen to the top and then downwards
			for (int row = 0; row < BOARDLENGTH; row++) {
				if (parent.board[col] == row) { //if target cell is occupied
					continue;
				} else {
					succ = genSucc(parent, col, row); //generate a new successor
					if (succ.cost < curOptimalCost) {
						optimalSuccList.clear();
						optimalSuccList.add(succ);
						curOptimalCost = succ.cost; //update
					} else if (succ.cost == curOptimalCost) {
						optimalSuccList.add(succ);
					}
				}
			}
		}
		return optimalSuccList;
	}
	
	private double genProb(State parent, State succ) {
		double prob;
		int delta = parent.cost - succ.cost;
		if (delta > 0) { //always go to successor
			prob = 1.00;
		} else if (this.temperature == 0) { //terminate iteration
			prob = 0.00;
		} else {
			//determine the probability to go to specified successor
			prob = Math.exp(delta / this.temperature); 
			this.temperature *= temperature * ANNEAL_RATE; //update temperature
		}
		return prob;
	}
	
	private void flush() {
		System.out.print(this.output.toString().trim());
	}
	
	
    public static void main(String args[]) {
    	
    	//TODO: delete this
    	//////////////////////FOR TESTING///////////////////////
    	//String input = "100 01234567";
    	//String input = "100 40057263";
    	//String input = "100 13572064";
    	//String input = "200 13572064";
    	//String input = "200 13572063";
    	//String input = "200 01234567";
    	//String input = "305 1 12375643";
    	//String input = "315 1 12375643";
    	//String input = "315 1 13572063";
    	//String input = "315 1 13572064";
    	//String input = "300 1 11111111";
    	//String input = "300 1 13572064";
    	//String input = "405 12375643";
    	//String input = "415 12375643";
    	//String input = "415 13572064";
    	//String input = "400 11111111";
    	//String input = "400 13572064";
    	//String input = "505 1 12375643";
    	//String input = "515 1 12375643";
    	//String input = "515 1 13572063";
    	//String input = "515 1 13572064";
    	//String input = "500 1 11111111";
    	//String input = "500 1 13572064";
    	
    	//args = input.split(" ");
    	////////////////////////////////////////////////////////
    	
    	
        if (args.length != 2 && args.length != 3) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        int[] board = new int[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i) - 48;
            int pos = board[i];
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration);
        
        //////////////STARTING INFORMED SEARCH////////////////////
        EightQueen eq = new EightQueen(option, iteration, seed, init);
        eq.run();
        
    }
}

class State {
	final static int BOARDLENGTH = 8;
    int[] board;
    int cost;
    boolean sovled;

    public State(int[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
        //assess cost and update this.solved flag
        this.assessCost();
    }
    
	private void assessCost() {
		int sumCost = 0;
		for (int i = 0; i < BOARDLENGTH; i++) {
			//assessing horizontal conflicts
			int reference = board[i];
			for (int j = (i+1); j < board.length; j++) {
				if (reference == board[j])
					sumCost++; //horizontal conflicts
			}
			//assessing diagonal conflicts
			int depth = board[i] + 1; // the row that curCh occupies
			int delta = 1; //horizontal distance deviating from center line
			//checking all the rows below depth
			while (depth < BOARDLENGTH) {
				if (((i - delta) >= 0) && board[i - delta] == board[i] + delta)
					sumCost++;
				if (((i + delta) < BOARDLENGTH) && board[i + delta] == board[i] + delta)
					sumCost++;
				depth++;
				delta++;
			}
		}
		this.cost = sumCost;
		if (this.cost == 0)
			this.sovled = true;
		else 
			this.sovled = false;
	}
	

    public String printState(int option, int step) {
    	StringBuilder msg = new StringBuilder();
        //print output based on option (flag)
    	switch(option) {
    	case 1:
    		break;
    	case 2:
    		for (int i: this.board) 
    			msg.append(i);
    		break;
    	case 3:
    	case 4:
    	case 5:
    		msg.append(step);
    		msg.append(":");
    		for (int i: this.board) 
    			msg.append(i);
    		msg.append(" ");
    		msg.append(this.cost);
    		break;
    	}
    	return msg.toString();
    }
}
