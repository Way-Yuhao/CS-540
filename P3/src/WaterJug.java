import java.util.*;


public class WaterJug {
    public static void main(String args[]) {
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
        //init.printState(option); 
        UninformedSearch us = new UninformedSearch();
        us.run(init, flag);
    }
}


class UninformedSearch {
	LinkedList<State> openSts = new LinkedList<State>();
	LinkedList<State> closedSts = new LinkedList<State>();
	State origin;
	int option;
	boolean goalReached = false;
	
	public UninformedSearch() {}
	
    private void bfs(State curSt) {
        //run breadth-first search algorithm
    	openSts.add(curSt);
    	printInitSt(curSt);
    	
    	do {
    		curSt = openSts.remove();
    		closedSts.add(curSt);
    		List<State> ListSucc = curSt.getSuccessors();
    		for (State succ: ListSucc) {
    			if (!isDuplicate(succ)) {
    				openSts.add(succ);
    			}
    		}
    		printAt(curSt, curSt.depth);
    	} while (terminateBfs(curSt));
    	printPathFrom(curSt);
    }
    
    private boolean isDuplicate(State st) {
    	if (!openSts.isEmpty()) {
	    	for (State target: openSts) {
	    		if (target.getOrderedPair().equals(st.getOrderedPair()))
	    			return true;
	    	}
    	}
    	if (!closedSts.isEmpty()) {
	    	for (State target: closedSts) {
	    		if (target.getOrderedPair().equals(st.getOrderedPair()))
	    			return true;
	    	}
    	}
    	return false;
    }
    
    private boolean terminateBfs(State curSt) {
    	if (option == 1 || option == 2) {
    		if (curSt.depth > 1)	return false;
    		else	return true;
    	} else { //when option is 3
    		if (curSt.isGoal || openSts.isEmpty())	return false;
    		else	return true;
    	}
    }

    private void dfs(State curSt) {
        // TO DO: run depth-first search algorithm
    	openSts.add(curSt);
    	printInitSt(curSt);
    	do {
    		curSt = openSts.remove(openSts.size() - 1);
    		closedSts.add(curSt);
    		List<State> ListSucc = curSt.getSuccessors();
    		for (State succ: ListSucc) {
    			if (!isDuplicate(succ)) {
    				openSts.add(succ);
    			}
    		}
    		printAt(curSt, curSt.depth);
    	} while (!curSt.isGoal && !openSts.isEmpty());
    	printPathFrom(curSt);
    }

    private void iddfs(State curSt, int maxDepth) {
        for (int curMaxDepth = 0; curMaxDepth <= maxDepth ; curMaxDepth++) {
        	if (goalReached)	break;
        	openSts.clear();
        	closedSts.clear();
        	curSt = this.origin;
        	System.out.printf("%d:00\n", curMaxDepth);
        	openSts.add(curSt);
        	do {
        		curSt = openSts.remove(openSts.size() - 1);
        		closedSts.add(curSt);
        		List<State> ListSucc = curSt.getSuccessors();
        		for (State succ: ListSucc) {
        			if ((succ.depth <= curMaxDepth) && !isDuplicate(succ)) {
        				openSts.add(succ);
        			}
        		}
        		printAt(curSt, curMaxDepth);
        	} while (!curSt.isGoal && !(openSts.size() == 0));
        }
        printPathFrom(curSt);
    }
    
	private void printInitSt(State curSt) {
		if (option == 3 || option == 4)
			System.out.println(curSt.getOrderedPair());
	}
    
    private void printAt(State curSt, int curMaxDepth) {
    	if (option == 1 || option == 2) {
    		if (curSt.depth > 1) return;
    	}
    	String msg = curSt.printState(option);
    	switch(option) {
    		case 1:
    			break;
    		case 2:
    			break;
    		case 3: 
    		case 4:
    			if (curSt.isGoal) {
    				msg += " Goal";
    				break;
    			}
    			//append openSts
    			msg += " [";
    			for (State st: openSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			if (!msg.contains(",")) msg += " "; //compensation
    			msg = msg.substring(0, msg.length() - 1) + "] [";
    			//append closedSts
    			for (State st: closedSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			msg = msg.substring(0, msg.length() - 1) + "]";
    			break;	
    		case 5:
    			msg = "" + curMaxDepth + ":" + msg;
    			if (curSt.isGoal) {
    				msg += " Goal";
    				this.goalReached = true;
    				break;
    			}
    			//append openSts
    			msg += " [";
    			for (State st: openSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			if (!msg.contains(",")) msg += " "; //compensation
    			msg = msg.substring(0, msg.length() - 1) + "] [";
    			//append closedSts
    			for (State st: closedSts) {
    				msg += st.getOrderedPair() + ",";
    			}
    			msg = msg.substring(0, msg.length() - 1) + "]";
    			break;	
    	}
    	if (option <= 2 && curSt.depth == 0)
    		return;
    	System.out.println(msg);
    }
    
    private void printPathFrom(State curSt) {
    	if (option < 3 || !curSt.isGoal)		return;
    	
    	
    	StringBuilder path = new StringBuilder();
    	LinkedList<State> revPath = new LinkedList<State>();
    	while (!curSt.getOrderedPair().equals(this.origin.getOrderedPair())) {
    		revPath.add(curSt);
    		curSt = curSt.parentPt;
    	}
    	path.append("Path ");
    	path.append(this.origin.getOrderedPair());
    	path.append(" ");
    	for (int i = revPath.size() - 1; i >= 0; i--) {
    		path.append(revPath.get(i).getOrderedPair());
    		path.append(" ");
    	}
    	System.out.println(path);
    }

    public void run(State initSt, int globalOption) {
    	this.origin = initSt;
       if (globalOption == 100) {
    	   option = 1;
    	   bfs(initSt);
       } else if (globalOption == 200) {
    	   option = 2;
    	   bfs(initSt);
       } else if (globalOption == 300) {
    	   option = 3;
    	   bfs(initSt);
       } else if (globalOption == 400) {
    	   option = 4;
    	   dfs(initSt);
       } else if (globalOption >= 500 && globalOption < 600) {
    	   option = 5;
    	   iddfs(initSt, globalOption - 500);
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
    
    public State(int jug1Cap, int jug2Cap, int jug1Cur, int jug2Cur, int goal, int depth, State parent) {
    	this(jug1Cap, jug2Cap, jug1Cur, jug2Cur, goal, depth);
    	this.parentPt = parent;
    }

    public List<State> getSuccessors() {
        //get all successors and return them in proper order
    	LinkedList<State> succList = new LinkedList<State>();
    	//fill jug 1
    	if (jug1Cur < jug1Cap) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cap, jug2Cur, goal, depth + 1, this));
    	}
    	//fill jug 2
    	if (jug2Cur < jug2Cap) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur, jug2Cap, goal, depth + 1, this));
    	}
    	
    	//empty jug 1
    	if (jug1Cur != 0) {
    		succList.add(new State(jug1Cap, jug2Cap, 0, jug2Cur, goal, depth + 1, this));
    	}
    	
    	//empty jug 2
    	if (jug2Cur != 0) {
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur, 0, goal, depth + 1, this));
    	}
    	
    	//pour jug 1 into jug 2
    	if ((jug1Cur != 0) && (jug2Cur < jug2Cap)) {
    		int deltaSrc = jug1Cur;
    		int deltaDest = jug2Cap - jug2Cur;
    		int delta = deltaSrc > deltaDest? deltaDest : deltaSrc;
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur - delta, jug2Cur + delta, goal, depth + 1, this));
    	}
    	//pour jug 2 into jug 1
    	if ((jug2Cur != 0) && (jug1Cur < jug1Cap)) {
    		int deltaSrc = jug2Cur;
    		int deltaDest = jug1Cap - jug1Cur;
    		int delta = deltaSrc > deltaDest? deltaDest : deltaSrc;
    		succList.add(new State(jug1Cap, jug2Cap, jug1Cur + delta, jug2Cur - delta, goal, depth + 1, this));
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
    
    public State getParentPt() {
    	return this.parentPt;
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
}