import java.lang.reflect.Array;
import java.util.*;

public class Hanoi {
	
	/**
	 * This function generates an arrayList of Strings containing all possible successful states 
	 * that are one step away from a given state.
	 * 
	 * @param inputs
	 * @return all successor states
	 */
    public static List<String> getSuccessor(String[] inputs) {
        List<String> result = new ArrayList<>();
        
        String group;
        for (int i = 0; i < inputs.length; i++) {
        	group = inputs[i]; //contains all elements on the current rod to be swapped with
        	if (group.equals("0")) {
        		continue;
        	} else {
        		// System.out.println(i);
        		char curPiece = group.charAt(0);
        		//check left
        		if (i > 0) { //ignore the case when there is no left rods
        			//check if the current piece is deploy-able
        			if (Character.compare(inputs[i-1].charAt(0), curPiece) > 0 || inputs[i-1].equals("0")) {
        				result.add(genState(inputs, i, -1));
        			}
        		}
        		//check right
        		if (i < inputs.length - 1) { //ignore the case when there is no right rods
        			//check if the current piece is deploy-able
        			if (Character.compare(inputs[i+1].charAt(0), curPiece) > 0 || inputs[i+1].equals("0")) {
        				result.add(genState(inputs, i, 1));
        			}
        		}
        	}
        }
        return result;    
    }
    
    /**
     * Private helper function that generates one single successor state
     * 
     * @param inputs
     * @param source
     * @param direction
     * @return a string representing one successor state
     */
    private static String genState(String[] inputs, int source, int direction) {
    	int target = source + direction; //index of the target rod
    	String piece = inputs[source].substring(0, 1); //current piece
    	//modify source rod
    	String modSrc = inputs[source].length() == 1? "0": inputs[source].substring(1); 
    	//modify target rod
    	String modTarget = inputs[target].equals("0")? piece: piece.concat(inputs[target]);
    	
    	int index = 0;
    	String output = "";
    	for (String str: inputs) {
    		if (index == source) {
    			output += modSrc + " ";
    		} else if (index == target) {
    			output += modTarget + " ";
    		} else {
    			output += str + " ";
    		}
    		index++;
    	}
    	return output;
    }

    /**
     * Main method that takes in the command arguments and ouputs a list of all possible 
     * adjacent successors.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 3) {
        	return;
        } 
        
        List<String> sucessors = getSuccessor(args);
        for (int i = 0; i < sucessors.size(); i++) {
            System.out.println(sucessors.get(i));
        }    
    }
}