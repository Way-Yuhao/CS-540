
import java.util.*;
import java.io.*;



public class Chatbot3 {
	 private static String filename = "./WARC201709_wid.txt";
	    private static ArrayList<Integer> readCorpus(){
	        ArrayList<Integer> corpus = new ArrayList<Integer>();
	        try{
	            File f = new File(filename);
	            Scanner sc = new Scanner(f);
	            while(sc.hasNext()){
	                if(sc.hasNextInt()){
	                    int i = sc.nextInt();
	                    corpus.add(i);
	                }
	                else{
	                    sc.next();
	                }
	            }
	        }
	        catch(FileNotFoundException ex){
	            System.out.println("File Not Found.");
	        }
	        return corpus;
	    }
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		ArrayList<Integer> corpus = readCorpus();
		int flag = Integer.valueOf(args[0]);
        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            int count = 0;
            //TODO count occurence of w
            File f = new File(filename);
            Scanner input=new Scanner(f);
            while(input.hasNext()) {
            	if(input.hasNextInt()){
                    int i = input.nextInt();
                    //corpus.add(i);
                    if(w==i) {
                    	count++;
                    }
                }
                else{
                    input.next();
                }
            }
            System.out.println(count);
            System.out.println(String.format("%.7f",count/(double)corpus.size()));
            input.close();
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            //TODO generate
            
            double random= (double)n1/n2;
            TreeMap<Integer,Integer> map=new TreeMap<Integer,Integer>();
            double p1=0.0;
            double p2=0.0;
            double current=0.0;
            for(int element:corpus) {
            	if(map.get(element)==null) {
            		map.put(element, 1);
            	}
            	else {
            		int value=map.get(element);
            		map.put(element,value+1);
            	}
            }
            int index=0;
            for(Map.Entry<Integer, Integer> value:map.entrySet()) {
            	double ratio=value.getValue()/(double)corpus.size();
            	//System.out.println(ratio);
            	if(current+ratio>=random) {
            		p1=current;
            		p2=current+ratio;
            		index=value.getKey();
            		break;
            	}
            	else {
            		current+=ratio;
            	}
            }
            //System.out.println(corpus.size());
            System.out.println(index);
            System.out.println(String.format("%.7f",p1));
            System.out.println(String.format("%.7f",p2));
            //System.out.print(random);
            
            
        }
        //check for the adjacent element, think adversely
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            //TODO
            int previous=Integer.MAX_VALUE;
            for(int i=0;i<corpus.size();i++) {
            	if(previous==h) {
            		words_after_h.add(corpus.get(i));
            	}
            	previous=corpus.get(i);
            }
            for(int j=0; j<words_after_h.size();j++) {
            	if(words_after_h.get(j)==w) {
            		count++;
            	}
            }
            
            //output 
            System.out.println(count);
            System.out.println(words_after_h.size());
            System.out.println(String.format("%.7f",count/(double)words_after_h.size()));
        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            double random= (double)n1/n2;
            //TODO
            TreeMap<Integer,Double> map=new TreeMap<Integer,Double>();
            double p1=0.0;
            double p2=0.0;
            double current=0.0;
            for(int element:corpus) {
            	if(map.get(element)==null) {
                    int count = 0;
                    ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                    //TODO
                    int previous=Integer.MAX_VALUE;
                    for(int i=0;i<corpus.size();i++) {
                    	if(previous==h) {
                    		words_after_h.add(corpus.get(i));
                    	}
                    	previous=corpus.get(i);
                    }
                    for(int j=0; j<words_after_h.size();j++) {
                    	if(words_after_h.get(j)==element) {
                    		count++;
                    	}
                    }
                    double value=count/(double)words_after_h.size();
            		map.put(element, value);
            	}
            }
            int index=0;
            for(Map.Entry<Integer, Double> value:map.entrySet()) {
            	double ratio=value.getValue();
            	//System.out.println(ratio);
            	if(current+ratio>=random&&current+ratio!=0) {
            		p1=current;
            		p2=current+ratio;
            		index=value.getKey();
            		break;
            	}
            	else {
            		current+=ratio;
            	}
            }
            System.out.println(index);
            System.out.println(String.format("%.7f",p1));
            System.out.println(String.format("%.7f",p2));
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count = 0;
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            //TODO
            int previous1=Integer.MAX_VALUE;
            int previous2=Integer.MIN_VALUE;
            for(int i=1;i<corpus.size();i++) {
            	if(previous1==h1&&previous2==h2) {
            		words_after_h1h2.add(corpus.get(i));
            	}
            	previous1=corpus.get(i-1);
            	previous2=corpus.get(i);
            }
            for(int j=0; j<words_after_h1h2.size();j++) {
            	if(words_after_h1h2.get(j)==w) {
            		count++;
            	}
            }
            //output 
            System.out.println(count);
            System.out.println(words_after_h1h2.size());
            if(words_after_h1h2.size() == 0)
                System.out.println("undefined");
            else
                System.out.println(String.format("%.7f",count/(double)words_after_h1h2.size()));
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            //TODO
            double random= (double)n1/n2;
            TreeMap<Integer,Double> map=new TreeMap<Integer,Double>();
            //TreeMap<Integer,Integer> map=new TreeMap<Integer,Integer>();
            double p1=0.0;
            double p2=0.0;
            double current=0.0;
            boolean hasValue=true;
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            int previous1=Integer.MAX_VALUE;
            int previous2=Integer.MIN_VALUE;
            for(int element:corpus) {
            	int count=0;
            	if(map.get(element)==null) {
                    //TODO
            		//ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
                    //int previous1=Integer.MAX_VALUE;
                    //int previous2=Integer.MIN_VALUE;
                    for(int i=1;i<corpus.size();i++) {
                    	if(previous1==h1&&previous2==h2) {
                    		words_after_h1h2.add(corpus.get(i));
                    	}
                    	previous1=previous2;
                    	previous2=corpus.get(i);
                    }
                    for(int j=0; j<words_after_h1h2.size();j++) {
                    	if(words_after_h1h2.get(j)==element) {
                    		count++;
                    	}
                    }
                    //double value=count/(double)words_after_h1h2.size();
                    if(words_after_h1h2.size() != 0) {
                    	 double value=count/(double)words_after_h1h2.size();
                    	 map.put(element, value);
                    }
                    else {
                    	 hasValue=false;
                    	System.out.println("undefined");
                    	 break;
                    }
            	}
            }
            
            	int index=0;
            	for(Map.Entry<Integer, Double> value:map.entrySet()) {
            		double ratio=value.getValue();
            		if(current+ratio>=random&&current+ratio!=0) {
            			p1=current;
            			p2=current+ratio;
            			index=value.getKey();
            			//System.out.print(value.getKey());
            			break;
            		}
            		
            			current+=ratio;
            	}
            	if(words_after_h1h2.size()!=0) {
            		System.out.println(index);
            		System.out.println(String.format("%.7f",p1));
            		System.out.println(String.format("%.7f",p2));
            	}
            	//else {
            		//System.out.println("undefined");
            	//}
        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                double r = rng.nextDouble();
                TreeMap<Integer,Integer> map=new TreeMap<Integer,Integer>();
                double p1=0.0;
                double p2=0.0;
                double current=0.0;
                for(int element:corpus) {
                	if(map.get(element)==null) {
                		map.put(element, 1);
                	}
                	else {
                		int value=map.get(element);
                		map.put(element,value+1);
                	}
                }
                
                for(Map.Entry<Integer, Integer> value:map.entrySet()) {
                	double ratio=value.getValue()/(double)corpus.size();
                	//System.out.println(ratio);
                	if(current+ratio>=r&&current+ratio!=0) {
                		p1=current;
                		p2=current+ratio;
                		h1=value.getKey();
                		break;
                	}
                	else {
                		current+=ratio;
                	}
                }
                //System.out.println(corpus.size());
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }
                
                // TODO Generate second word using r
                r = rng.nextDouble();
                TreeMap<Integer,Double> map2=new TreeMap<Integer,Double>();
                 p1=0.0;
                 p2=0.0;
                current=0.0;
                for(int element:corpus) {
                	if(map2.get(element)==null) {
                        int count = 0;
                        ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                        //TODO
                        int previous=Integer.MAX_VALUE;
                        //record all adjacent values
                        for(int i=0;i<corpus.size();i++) {
                        	if(previous==h1) {
                        		words_after_h.add(corpus.get(i));
                        	}
                        	previous=corpus.get(i);
                        }
                        //count the number of times the element appears
                        for(int j=0; j<words_after_h.size();j++) {
                        	if(words_after_h.get(j)==element) {
                        		count++;
                        	}
                        }
                        //calculate the p(w|h)
                        double value=count/(double)words_after_h.size();
                		map2.put(element, value);
                	}
                	
                }
                //generate the word
                for(Map.Entry<Integer, Double> value:map2.entrySet()) {
                	double ratio=value.getValue();
                	//generate the number
                	if(current+ratio>=r&&current+ratio!=0) {
                		p1=current;
                		p2=current+ratio;
                		h2=value.getKey();
                		break;
                	}
                	else {
                		current+=ratio;
                	}
                }
                
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rng.nextDouble();
                TreeMap<Integer,Double> map2=new TreeMap<Integer,Double>();
                double p3=0.0;
                double p4=0.0;
                double current2=0.0;
                for(int element:corpus) {
                	if(map2.get(element)==null) {
                        int count = 0;
                        ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                        //TODO
                        int previous=Integer.MAX_VALUE;
                      //record all adjacent values
                        for(int i=0;i<corpus.size();i++) {
                        	if(previous==h1) {
                        		words_after_h.add(corpus.get(i));
                        	}
                        	previous=corpus.get(i);
                        }
                      //count the number of times the element appears
                        for(int j=0; j<words_after_h.size();j++) {
                        	if(words_after_h.get(j)==element) {
                        		count++;
                        	}
                        }
                      //calculate the p(w|h)
                        double value=count/(double)words_after_h.size();
                		map2.put(element, value);
                	}
                	
                }
                //generate h2
                for(Map.Entry<Integer, Double> value:map2.entrySet()) {
                	double ratio=value.getValue();
                	
                	if(current2+ratio>=r&&current2+ratio!=0) {
                		p3=current2;
                		p4=current2+ratio;
                		h2=value.getKey();
                		break;
                	}
                	else {
                		current2+=ratio;
                	}
                }
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = 0;
                // TODO Generate new word using h1,h2
                TreeMap<Integer,Double> map=new TreeMap<Integer,Double>();
                double p1=0.0;
                double p2=0.0;
                double current=0.0;
                boolean hasValue=true;
                ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
                int previous1=Integer.MAX_VALUE;
                int previous2=Integer.MIN_VALUE;
                //record each cases of h1,h2
                for(int element:corpus) {
                	int count=0;
                	if(map.get(element)==null) {
                        //TODO
                        for(int i=1;i<corpus.size();i++) {
                        	if(previous1==h1&&previous2==h2) {
                        		words_after_h1h2.add(corpus.get(i));
                        	}
                        	previous1=previous2;
                        	previous2=corpus.get(i);
                        }
                        for(int j=0; j<words_after_h1h2.size();j++) {
                        	if(words_after_h1h2.get(j)==element) {
                        		count++;
                        	}
                        }
                        //calculate p(w|h1,h2)
                        if(words_after_h1h2.size() != 0) {
                        	 double value=count/(double)words_after_h1h2.size();
                        	 map.put(element, value);
                        }
                        else {
                        	 break;
                        }
                	}
                }
                    // if h1,h2 is not undefined, get the word according to p(w|h1,h2)
                    if(words_after_h1h2.size()!=0) {
                    	for(Map.Entry<Integer, Double> value:map.entrySet()) {
                    		double ratio=value.getValue();
                    		if(current+ratio>=r&&current+ratio!=0) {
                    			p1=current;
                    			p2=current+ratio;
                    			w=value.getKey();
                			//System.out.print(value.getKey());
                    			break;
                    		}
                		
                				current+=ratio;
                    	}
                    }
                    //if h1,h2 is undefined, instead calculate(p|h2) 
                    else {
                    	 TreeMap<Integer,Double> map2=new TreeMap<Integer,Double>();
                         double p3=0.0;
                         double p4=0.0;
                         double current2=0.0;
                         //record each cases of h2
                         for(int element:corpus) {
                         	if(map2.get(element)==null) {
                                 int count = 0;
                                 ArrayList<Integer> words_after_h = new ArrayList<Integer>();
                                 //TODO
                                 int previous=Integer.MAX_VALUE;
                                 for(int i=0;i<corpus.size();i++) {
                                	 ///significant
                                 	if(previous==h2) {
                                 		words_after_h.add(corpus.get(i));
                                 	}
                                 	previous=corpus.get(i);
                                 }
                                 // calulate the p(w|h2)
                                 for(int j=0; j<words_after_h.size();j++) {
                                 	if(words_after_h.get(j)==element) {
                                 		count++;
                                 	}
                                 }
                                 double value=count/(double)words_after_h.size();
                         		map2.put(element, value);
                         	}
                         }
                         //get the word according to p(w|h2)
                         for(Map.Entry<Integer, Double> value:map2.entrySet()) {
                         	double ratio=value.getValue();
                         	//System.out.println(ratio);
                         	if(current2+ratio>=r&&current2+ratio!=0) {
                         		p3=current2;
                         		p4=current2+ratio;
                         		w=value.getKey();
                         		break;
                         	}
                         	else {
                         		current2+=ratio;
                         	}
                         }
                    }
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }
	}
}
