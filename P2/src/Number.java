import java.util.*;
import java.math.*;

public class Number{
	private  int X, Y;
	private  LinkedList<State> queue = new LinkedList<State>();
           
    public  String getStep() {
    	//expand from start
    	State curSt = new State(X, 0);
    	this.queue.add(curSt);
    	
    	do {
    		//choose the state with minimum g(s) + h(s) value to expand
    		//pop the chosen state from openStates to closesStates
    		curSt = queue.remove();
        	//append all successors of the chosen state
    		appendAllSucc(curSt, curSt.stepsTaken);
    	} while(curSt.value != Y);
    	
        return Integer.toString(curSt.stepsTaken);
    }
    
    private void appendAllSucc(State st, int curSteps) {
    	//Multiply by 3
    	queue.add(new State(st.value * 3, curSteps + 1));
    	//Square Root
    	queue.add(new State(st.value * st.value, curSteps + 1));
    	//Add 1
    	queue.add(new State(st.value + 1, curSteps + 1));
    	//Subtract 1
    	queue.add(new State(st.value - 1, curSteps + 1));
    }
    

    public static void main(String[] args) {
    	Number stage = new Number();
        if (args.length != 2) {
            return;
        }
        stage.X = Integer.parseInt(args[0]);
        stage.Y = Integer.parseInt(args[1]);
        System.out.println(stage.getStep());
    }
}

class State {
	int value;
	int stepsTaken;
	
	 State(int value, int stepsTaken) {
		this.value = value;
		this.stepsTaken = stepsTaken;
		return;
	}
	 
}