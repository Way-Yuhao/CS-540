import java.util.*;


public class WaterJug {
    public static void main(String args[]) {
    	/////FOR TESTING/////
    	String input = "100 2 1 0 0 1";
    	args = input.split(" ");
    	////////////////////
    	
    	if (args.length != 6) {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        int cap_jug1 = Integer.valueOf(args[1]);
        int cap_jug2 = Integer.valueOf(args[2]);
        int curr_jug1 = Integer.valueOf(args[3]);
        int curr_jug2 = Integer.valueOf(args[4]);
        int goal = Integer.valueOf(args[5]);

        int option = flag / 100;
        int depth = flag % 100;

        if (option < 1 || option > 5) {
            System.out.println("Invalid flag input");
            return;
        }
        if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
            System.out.println("Invalid input: 2-digit jug volumes");
            return;
        }
        if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
            System.out.println("Invalid input: negative jug volumes");
            return;
        }
        if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
            System.out.println("Invalid input: jug volume exceeds its capacity");
            return;
        }
        State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0);
        //init.printState(option); //FIXME: originally has depth as parameter
        UninformedSearch.run(init, flag);
    }
}


class UninformedSearch {
	static LinkedList<State> openSts = new LinkedList<State>();
	static LinkedList<State> closedSts = new LinkedList<State>();
    private static void bfs(State curSt, int option) {
        // TO DO: run breadth-first search algorithm
    	openSts.add(curSt);
    	do {
    		curSt = openSts.remove();
    		closedSts.add(curSt);
    		List<State> ListSucc = curSt.getSuccessors();
    		for (State succ: ListSucc) {
    			if ((!openSts.contains(succ)) && (!closedSts.contains(succ))) {
    				openSts.add(succ);
    			}
    		}
    		printAt(curSt, option, curSt.depth);
    	} while (!curSt.isGoal || openSts.isEmpty());
    }

    private static void dfs(State curSt, int option) {
        // TO DO: run depth-first search algorithm
    	openSts.add(curSt);
    	do {
    		curSt = openSts.remove(openSts.size() - 1);
    		closedSts.add(curSt);
    		List<State> ListSucc = curSt.getSuccessors();
    		for (State succ: ListSucc) {
    			if ((!openSts.contains(succ)) && (!closedSts.contains(succ))) {
    				openSts.add(succ);
    			}
    		}
    		printAt(curSt, option, curSt.depth);
    	} while (!curSt.isGoal || openSts.isEmpty());
    }

    private static void iddfs(State curSt, int maxDepth, int option) {
        for (int curMaxDepth = 0; curMaxDepth <= maxDepth ; curMaxDepth++) {
        	System.out.printf("%d:00\n", curMaxDepth);
        	openSts.add(curSt);
        	do {
        		curSt = openSts.remove(openSts.size() - 1);
        		closedSts.add(curSt);
        		List<State> ListSucc = curSt.getSuccessors();
        		for (State succ: ListSucc) {
        			if ((succ.depth <= curMaxDepth) && (!openSts.contains(succ)) && (!closedSts.contains(succ))) {
        				openSts.add(succ);
        			}
        		}
        		printAt(curSt, option, curMaxDepth);
        	} while (!curSt.isGoal || openSts.isEmpty());
        }
    }
    
    private static void printAt(State curSt, int option, int depth) {
    	String msg = curSt.printState(option);
    	switch(option) {
    		case 1:
    			break;
    		case 2:
    			break;
    		case 3: 
    		case 4:
    		case 5:
    			msg = "" + depth + ":" + msg;
    			//append openSts
    			msg += " [";
    			for (State st: openSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			msg += "] [";
    			//append closedSts
    			for (State st: closedSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			msg += "]";
    			break;	
    	}
    	if (option <= 2 && curSt.depth == 0)
    		return;
    	System.out.println(msg);
    }

    public static void run(State initSt, int globalOption) {
       if (globalOption == 100) {
    	   bfs(initSt, 1);
       } else if (globalOption == 200) {
    	   bfs(initSt, 2);
       } else if (globalOption == 300) {
    	   bfs(initSt, 3);
       } else if (globalOption == 400) {
    	   dfs(initSt, 4);
       } else if (globalOption >= 500 && globalOption < 600) {
    	   iddfs(initSt, globalOption - 500, 5);
       } else {
    	   //ERROR FIXME
       }
    }
}



class State {
    int jug1Cur;
    int jug2Cur;
    int jug1Cap;
    int jug2Cap;
    int goal;
    int depth;
    boolean isGoal = false;
    State parentPt;

    public State(int jug1Cap, int jug2Cap, int jug1Cur, int jug2Cur, int goal, int depth) {
        this.jug1Cap = jug1Cap;
        this.jug2Cap = jug2Cap;
        this.jug1Cur = jug1Cur;
        this.jug2Cur = jug2Cur;
        this.goal = goal;
        this.depth = depth;
        this.parentPt = null;
        if ((jug1Cur == goal) || (jug2Cur == goal))
        	this.isGoal = true;
        return;
    }

    public List<State> getSuccessors() {
        // TO DO: get all successors and return them in proper order
    	LinkedList<State> succList = new LinkedList<State>();
    	//fill jug 1
    	if (jug1Cur < jug1Cap) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cap, jug2Cur, goal, depth + 1));
    	}
    	//fill jug 2
    	if (jug2Cur < jug2Cap) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur, jug2Cap, goal, depth + 1));
    	}
    	
    	//empty jug 1
    	if (jug1Cur != 0) {
    		succList.add(new State(jug1Cap, jug2Cap, 0, jug2Cur, goal, depth + 1));
    	}
    	
    	//empty jug 2
    	if (jug2Cur != 0) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur, 0, goal, depth + 1));
    	}
    	
    	//pour jug 1 into jug 2
    	if ((jug1Cur != 0) && (jug2Cur < jug2Cap)) {
    		int deltaSrc = jug1Cur;
    		int deltaDest = jug2Cap - jug2Cur;
    		int delta = deltaSrc > deltaDest? deltaDest : deltaSrc;
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur - delta, jug2Cur + delta, goal, depth++));
    	}
    	//pour jug 2 into jug 1
    	if ((jug2Cur != 0) && (jug1Cur < jug1Cap)) {
    		int deltaSrc = jug2Cur;
    		int deltaDest = jug1Cap - jug1Cur;
    		int delta = deltaSrc > deltaDest? deltaDest : deltaSrc;
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur + delta, jug1Cur - delta, goal, depth++));
    	}
    	
    	//insertion sort
    	LinkedList<State> temp = new LinkedList<State>();
    	boolean stAdded = false;
    	for (State st: succList) {
    		stAdded = false;
    		if (temp.isEmpty())
    			temp.add(st);
    		else {
    			for (int index = 0; index < temp.size(); index++) {
    				if (st.getOrderedPair().compareTo(temp.get(index).getOrderedPair()) < 0) {
    					temp.add(index, st);
    					stAdded = true;
    					break;
    				}
    			}
    			if (!stAdded)	temp.add(st);
    		}
    	}
    	succList = temp;
    	temp = null;
        return succList; //successors
    }

    public String printState(int option) {
    	String msg = "";
        // TO DO: print a State based on option (flag)
    	switch(option) {
    		case 1:
    		case 3: 
    		case 4:
    		case 5:
    			msg += jug1Cur + "" + jug2Cur;
    			break;
    		case 2:
    			msg += jug1Cur + "" + jug2Cur + " " + isGoal;
    			break;
    	}
    	return msg;
    }

    public String getOrderedPair() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.jug1Cur);
        builder.append(this.jug2Cur);
        //builder.
        return builder.toString().trim();
    }

    // TO DO: feel free to add/remove/modify methods/fields

}