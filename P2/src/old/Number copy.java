import java.util.*;
import java.math.*;

public class Number{
	private  int X, Y;
	private  LinkedList<State> openStates = new LinkedList<State>();
	private  LinkedList<State> closedStates = new LinkedList<State>();
           
    public  String getStep() {
    	//expand from start
    	State curSt = new State(X, 0);
    	this.openStates.add(curSt);
    	
    	do {
    		//choose the state with minimum g(s) + h(s) value to expand
    		//pop the chosen state from openStates to closesStates
    		curSt = popToClosedStates();
        	//append all successors of the chosen state
    		appendAllSucc(curSt, curSt.stepsTaken);
    	} while(curSt.value != Y); //FIXME
    	
        return Integer.toString(curSt.stepsTaken); //FIXME
    }
    
    private void appendAllSucc(State st, int curSteps) {
    	//Multiply by 3
    	insertionSort(new State(st.value * 3, curSteps + 1));
    	//Square Root
    	insertionSort(new State((int)Math.sqrt(st.value), curSteps + 1));
    	//Add 1
    	insertionSort(new State(st.value + 1, curSteps + 1));
    	//Subtract 1
    	insertionSort(new State(st.value - 1, curSteps + 1));
    }
    
    private State popToClosedStates() {
    	State priSt = openStates.remove();
    	closedStates.add(priSt);
    	return priSt;
    }
    
    private void insertionSort(State st) {
    	int index = 0;
    	if (openStates.isEmpty()) {
    		openStates.add(index, st);
    		return; 
    	}
    	for (State target: openStates) {
    		if (st.expCost <= target.expCost) {
    			openStates.add(index, st);
    			return;
    		} else {
    			index++;
    		}
    	}
    	openStates.add(index, st);
    	return;
    }
    
    public static void main(String[] args) {
    	Number stage = new Number();
        if (args.length != 2) {
            return;
        }
        State master = new State();
 
        stage.X = Integer.parseInt(args[0]);
        stage.Y = Integer.parseInt(args[1]);
        master.setGoal(stage.Y);
        System.out.println(stage.getStep());
    }
    
    
}

class State {
	int value;
	int stepsTaken;
	int expCost;
	static int goal;
	
	State() {
		
	}
	
	 State(int value, int stepsTaken) {
		this.value = value;
		this.stepsTaken = stepsTaken;
		this.evaluate();
		return;
	}
	 
	 static void setGoal(int goal) {
		 State.goal = goal;
	 }
	 
	 private void evaluate() {
		 this.expCost = this.stepsTaken + Math.abs(State.goal - this.value);
	 }
	 
}