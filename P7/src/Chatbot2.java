
import java.util.*;
import java.io.*;
import java.util.Random;

public class Chatbot2{
	final static int CORPUSLENGTH = 4700;
	final static int TOKENCOUNT = 228548;
    private String filename = "./corpus.txt";
    private ArrayList<Integer> corpus;
    private ArrayList<Double> multiDistri;
    private int[] corpusCount;
    private int flag;
    Random rand;
    
    public Chatbot2() {
    	this.corpus = new ArrayList<Integer>();
    	this.corpusCount = new int[CORPUSLENGTH];
    	this.multiDistri = new ArrayList<Double>();
    	rand = new Random();
    }
    
    private void readCorpus(){
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                    corpusCount[i]++;
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return;
    }
    
    private double pUni(int w) {
    	return (double)(corpusCount[w] + 1) / (CORPUSLENGTH + TOKENCOUNT);
    }
    
    private void buildUniDistri() {
    	double curSum = 0.0;
    	for (int i = 0; i < CORPUSLENGTH; i++) { //from 1 to v-1
    		curSum = pUni(i);
    		multiDistri.add(curSum);
    	}
    }
    
    private double pBi(int h, int w, boolean printHere) {
    	int pMatch = 0;
    	int wMatch = 0;
    	int previous = Integer.MAX_VALUE;
    	int next = Integer.MAX_VALUE;
    	for (int i = 0; i < TOKENCOUNT; i++) {
    		if (previous == h) {
    			pMatch++;
    			if (next == w) 
    				wMatch++;
    		}
    		previous = next;
    		next = corpus.get(i);
    	}
    	double p = (double)(wMatch + 1) / ( pMatch + CORPUSLENGTH);
    	if (printHere) {
    		stdPrint(wMatch);
    		stdPrint(pMatch);
    		stdPrint(p);
    	}
    	// stdPrint(p);
    	return p;
    }
    
    private void buildBiDistri(int h) {
    	double curSum = 0.0;
    	for (int i = 0; i < CORPUSLENGTH; i++) { //from 1 to v-1
    		curSum = pBi(h, i, false);
    		if (curSum > 1.1)
    			break;
    		multiDistri.add(curSum);
    	}
    }
    
    private double pTri(int h1, int h2, int w, boolean printHere) {
    	int pMatch = 0;
    	int wMatch = 0;
    	int previous1 = Integer.MAX_VALUE;
    	int previous2 = Integer.MAX_VALUE;
    	int next = Integer.MAX_VALUE;
    	for (int i = 1; i < TOKENCOUNT; i++) {
    		if ((previous1 == h1) && (previous2 == h2)) {
    			pMatch++;
    			if (next == w)
    				wMatch++;
    		}
    		previous1 = previous2;
    		previous2 = next;
    		next = corpus.get(i);
    	}
    	
    	double p = ((double) wMatch + 1) / (pMatch + CORPUSLENGTH);
    	if (printHere) {
    		stdPrint(wMatch);
    		stdPrint(pMatch);
    		stdPrint(p);
    	}
    	return p;
    }
    
    private void buildTriDistri(int h1, int h2) {
    	double curSum = 0.0;
    	for (int i = 0; i < CORPUSLENGTH; i++) { //from 1 to v-1
    		curSum = pTri(h1, h2, i, false);
    		if (curSum > 1.1)
    			break;
    		multiDistri.add(curSum);
    	}
    }
    
    private double[] checkSeg(double r) {
    	Iterator<Double> it = multiDistri.iterator();
    	int index = 0;
    	double leftBound = 0.0;
    	double rightBound = (double)it.next();
    	while (it.hasNext()) {
    		if (r <= rightBound) {
    			double[] rArr = {index, leftBound, rightBound};
    			return rArr;
    		}
    		leftBound = rightBound;
    		rightBound += (double)it.next();
    		index++;
    	}
    	if (r <= rightBound) {
			double[] rArr = {index, leftBound, rightBound};
			return rArr;
		}
    	return null; //Segment not found
    }
    private void stdPrint(int x) {
    	System.out.println(x);
    }
    private void stdPrint(double x) {
    	System.out.printf("%.7f\n", x);
    }
    
    public void run(String[] args) {
    	this.readCorpus();
		this.flag = Integer.valueOf(args[0]);
        
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count = 0;
            stdPrint(corpusCount[w]);
            stdPrint(pUni(w));
            return;
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            double r = (double) n1 / n2;
            buildUniDistri(); 
            double[] segArr = checkSeg(r);
            stdPrint((int)segArr[0]);
            stdPrint(segArr[1]);
            stdPrint(segArr[2]);
            return;
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            pBi(h, w, true);
            return;
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            double r = (double) n1 / n2;
            buildBiDistri(h);
            double[] segArr = checkSeg(r);
            stdPrint((int)segArr[0]);
            stdPrint(segArr[1]);
            stdPrint(segArr[2]);
            return;
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            pTri(h1, h2, w, true);
            return;
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            double r = (double) n1 / n2;
            buildTriDistri(h1, h2);
            double[] segArr = checkSeg(r);
            stdPrint((int)segArr[0]);
            stdPrint(segArr[1]);
            stdPrint(segArr[2]);
            return;
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            if (seed != -1) rand.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rand.nextDouble();
                this.buildUniDistri();
                h1 = (int) checkSeg(r)[0];
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }
                // TODO Generate second word using r
                h2 = (int) checkSeg(r)[0];
                r = rand.nextDouble();
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rand.nextDouble();
                this.buildBiDistri(h1);
                h2 = (int) checkSeg(r)[0];
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rand.nextDouble();
                int w  = 0;
                // TODO Generate new word using h1,h2
                this.buildTriDistri(h1, h2);
                w = (int) checkSeg(r)[0];
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }
        return;
    }
    public static void main(String[] args){
        Chatbot2 bot = new Chatbot2();
        ////////////FOR TESTING PURPOSES/////////////////
        //FIXME
        String argLine = "600 2 5 660 3425";
        args = argLine.split(" ");
        bot.run(args);
    }
}
