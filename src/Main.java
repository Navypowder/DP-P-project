import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.SecureDirectoryStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;



public class Main {
	
	static String fileIn;

	public static void main(String[] args) throws IOException, InterruptedException {
		
		ArrayList<Degree> origList = new ArrayList<Degree>();
		fileIn = args[0];
		int k = Integer.parseInt(args[1]);
		String anonMode = args[2];
		String buildMode = args[3];
		ArrayList<Degree> dist = ReadData();
		ArrayList<DA> das = new ArrayList<DA>();
		ArrayList<Degree> grouped = new ArrayList<Degree>();

		if(anonMode.equals("greedy")){
			//creazione lista dei gradi con algoritmo greedy
			grouped = grouper(k, dist);
			origList = grouper(k, dist);
		}
		
		
		else if(anonMode.equals("dp")){
		//creazione lista dei gradi con algoritmo dynamic programming
		grouped = DPGrouper(k, dist, das);
		origList = DPGrouper(k, dist, das);
		}
		
		int initcost = 0;
				
		for(Degree d : dist)
		{
//			System.out.println(d.name + " " + d.degree);
			initcost += d.getDegree();
		}
		
		//System.out.println("Gradi dei nodi da ottenere:");
		
		int anoncost = 0;
		for(Degree d : grouped)
		{
//			System.out.println(d.name + " " + d.degree);
			anoncost += d.getDegree();
		}
		
		ArrayList<Edge> origGraph = readOrigGraph();
		int origGraphEdges = origGraph.size();

		long startTime = System.currentTimeMillis();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		if(buildMode.equals("constr")){
			//Anonimizza con l'algoritmo greedy
			edges = greedyConstructGraph(grouped);
		}
		
		else if(buildMode.equals("prio")){
			//Anonimizza con l'algoritmo Priority
			edges = priorityConstructGraph(grouped, origGraph);
		}
		
		
		long finishTime = System.currentTimeMillis();
		long totalTime = (finishTime-startTime);
		
		//System.out.println("generazione file di output...");
		buildAnon(edges, k);	
		//System.out.println("Done!");
		
		if(totalTime>=1000) {
			totalTime=totalTime/1000;
			//System.out.println("Tempo di esecuzione algoritmo: " + totalTime + " s");
		}
		else {
			//System.out.println("Tempo di esecuzione algoritmo: " + totalTime + " ms");
		}
				
		int diversity=0;
		for(Edge e : origGraph) {
			if(!edges.contains(e)) {
				diversity++;
			}
		}
		
//		System.out.println("Diversità  grafo originale/anonimo: " + diversity);
//		System.out.println("Archi originali grafo non anonimo: " + origGraphEdges);
//		System.out.println("Archi totali grafo anonimizzato: " + edges.size());
		
		int cost = 0;
		if(anonMode.equals("dp")){
			cost = das.get(das.size()-1).cost;
		}
		else if(anonMode.equals("greedy")){
			cost = initcost-anoncost;
		}
		
//		System.out.println("costo anonimizzazione: " + cost);
		
		buildOutput(fileIn, k, anonMode, buildMode, cost, totalTime);
		
	}
	
	public static ArrayList<Degree> ReadData()  throws IOException
	{
		ArrayList<Degree> data = new ArrayList<Degree>();
		Scanner scanner = new Scanner(new File(fileIn));
		scanner.useDelimiter(",");
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] people = line.split(",");
			Degree deg = new Degree(people[0], people.length-1);
			data.add(deg);
		}
		return data;
	}
	
	public static ArrayList<Degree> grouper(int k , ArrayList<Degree> dist) {
		ArrayList<Degree> Newdist = new ArrayList<Degree>();
		int idx = 0;
		int C1 = dist.get(idx).getDegree();
		for(int i = idx; i<k; i++)
		{
			Degree d = new Degree(dist.get(i).name, C1*C1);
			Newdist.add(d);
		}
		idx = k;
		while(Newdist.size()!=dist.size())
		{
			int Cmerge = 0;
			int Cnew = 0;
			if(idx + k>dist.size())
			{
				k = dist.size() - idx ;
				for(int i=0; i<k;i++)
				{
					Degree d = new Degree(dist.get(idx).name, Newdist.get(idx-1).getDegree());
					Newdist.add(d);
					idx = idx+1;
					
				}
				break;
			}
			Cmerge = (dist.get(0).getDegree() - dist.get(idx).getDegree()) + ComputeI(idx+1, idx+k-1, dist);
			Cnew = ComputeI(idx, idx+k-1, dist);
			
			if(Cmerge>Cnew)
			{
				//new cluster creation
				int ngc = dist.get(idx).getDegree();
				for(int i = idx; i<idx+k; i++)
				{
					Degree d = new Degree(dist.get(i).name, ngc*ngc);
					Newdist.add(d);
				}
				idx = idx+k;
			}
			else {
				//merge section
				Degree d = new Degree(dist.get(idx).name, Newdist.get(idx-1).getDegree());
				Newdist.add(d);
				idx = idx+1;
			}
		}
		for(Degree d : Newdist)
		{
			d.setDegree((int)Math.sqrt(d.getDegree()));
		}
		return Newdist;
	}
	
	private static ArrayList<Degree> DPGrouper(Integer k, ArrayList<Degree> dist, ArrayList<DA> das){
		ArrayList<Degree> NewDist = new ArrayList<Degree>();
		
		
		Collections.sort(dist, new Comparator<Degree>(){
			@Override
	        public int compare(Degree o1, Degree o2) {
	            return o2.degree.compareTo(o1.degree);
	        }
		});
		
//		for(Degree degree : dist){
//			System.out.println(degree.name + " " + degree.degree);
//		}
		
		int firstDegree = dist.get(0).getDegree();
		
		for(int i=0; i<2*k-1; i++){
			ArrayList<Integer> degree = new ArrayList<Integer>();
			for(int j = 0 ; j<=i; j++){
				degree.add(firstDegree);
			}
			
			
			ArrayList<ArrayList<Integer>> deg = new ArrayList<ArrayList<Integer>>();
			deg.add(degree);
			DA da = new DA(deg, ComputeI(0, i, dist));
			das.add(da);
		}

		for(int j = 2*k; j<=dist.size(); j++){
			ArrayList<Cluster> tmp = new ArrayList<Cluster>();
			int localId = 1;
			
			for(int t=k ; t<=j-k; t++)
			{
				//System.out.println("J: " +j + " T: " + t);
				int cNew = 0;
				int cMerge = 0;
				ArrayList<Integer> d = new ArrayList<Integer>();
				for(int l=0; l<t;l++){
					d.add(firstDegree);
				}
				
				Cluster onet = new Cluster(localId, das.get(t-1).seq, das.get(t-1).getCost());
				
				ArrayList<Integer> dt = new ArrayList<Integer>();
				for(int l=t; l<j;l++){
					dt.add(dist.get(t).getDegree());
				}
				ArrayList<ArrayList<Integer>> adt = new ArrayList<ArrayList<Integer>> ();
				adt.add(dt);
				
				Cluster tponei = new Cluster(localId, adt, ComputeI(t, j-1, dist));
							
				cNew = onet.cost + tponei.cost;
				
				cMerge = ComputeI(0, j-1, dist);
				
//				System.out.println("cmerge : " + cMerge + " cnew: " + cNew);
				
				// qui valuto se fare il merging sul cluster precedente o se creare un nuovo cluster
				if(cNew<cMerge){
					//System.out.println("NEW");
					tmp.add(onet);
					tmp.add(tponei);
				}
				
				else {
//					System.out.println("MERGE");
					ArrayList<Integer> deg = new ArrayList<Integer>();
					for(int z = 0; z<j; z++){
						deg.add(firstDegree);
					}
					ArrayList<ArrayList<Integer>> adeg = new ArrayList<ArrayList<Integer>> ();
					adt.add(deg);
					Cluster oneinew = new Cluster(localId++, adeg, ComputeI(0, j, dist));
					tmp.add(oneinew);
				}
				localId++;
			}
			
//			for(Cluster cluster : tmp){
//				System.out.println(cluster.id + " " + cluster.degrees + " " + cluster.cost);
//			}
			
			Cluster c1 = null;
			Cluster c2 = null;
			int v = 0;
			int min = Integer.MAX_VALUE;
			while(v<tmp.size()){
				if(tmp.get(v).cost + tmp.get(v+1).cost<=min){
					min = tmp.get(v).cost + tmp.get(v+1).cost;
					c1= tmp.get(v);
					c2 = tmp.get(v+1);
				}
				v = v+2;
			}
			
			ArrayList<ArrayList<Integer>> agg = new ArrayList<ArrayList<Integer>> ();
			agg.addAll(c1.getDegrees());
			agg.addAll(c2.getDegrees());
			das.add(new DA(agg, (c1.getCost() + c2.getCost())));
			
//			System.out.println("DA: ");
//			for(DA da : das){
//				System.out.println(da.getSeq() + " " + da.getCost());
//			}
//			System.out.println("----------------------------------------");
		}
		
		//qui creo il newdist da DA(1, dist.size())
		int cnt = 0;
		ArrayList<ArrayList<Integer>> sol = das.get(das.size()-1).getSeq();
		for(ArrayList<Integer> arr : sol){
			for(Integer i: arr){
				Degree grado = new Degree(dist.get(cnt).getName(), i);
				NewDist.add(grado);
				cnt++;
			}
		}
		
		return NewDist;
	}

	private static int ComputeI(int i, int j, ArrayList<Degree> dist ) {
		int I = 0;
		for(int l = i; l<=j; l++)
		{
			I = I + dist.get(i).getDegree() - dist.get(l).getDegree();
		}
		return I;
	}
	
	private static ArrayList<Edge> greedyConstructGraph(ArrayList<Degree> Newdist) throws InterruptedException {
//		System.out.println("anonimizzazione grafo con l'algoritmo greedy");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int sum = 0;
		for(Degree d : Newdist)
		{
			sum = sum + d.getDegree();
		}
		if(sum%2 != 0)
		{
//			System.out.println("infattibile " + sum);
		}
		else 
		{
//			System.out.println("fattibile " + sum);
			
			while(sum>0)
			{
				sum = 0;
				Random random = new Random();
				int k = random.nextInt(Newdist.size());
				Degree v = Newdist.get(k);
				//grado del nodo preso randomicamente da newdist
				if(v.getDegree()>0)
				{
					//setto come max di riferimento il grado del nodo preso randomicamente
					int max = v.getDegree();
					//ne setto ora il grado a 0
					v.setDegree(0);
					//ordino newdist per grado in maniera tale da prendere quelli di grado piË˜ alto succedssivaemnte nel codice
					Collections.sort(Newdist, new Comparator<Degree>(){
						@Override
				        public int compare(Degree o1, Degree o2) {
				            return o2.degree.compareTo(o1.degree);
				        }
					});
					
					
					for(int i=0; i<max; i++)
					{
						//fino al grado del nodo prelevato randomicamente
						//prelevo da newdist il primo nodo ogni volta ( grado piu alto)
						//e lo linko a quello prelevato radnomicamente
						if(!Newdist.get(i).getName().equals(v.getName()))
						{
							Edge e = new Edge(v.getName(), Newdist.get(i).getName());
							Edge e1 = new Edge(Newdist.get(i).getName(), v.getName());
							Newdist.get(i).setDegree(Newdist.get(i).getDegree()-1);
							edges.add(e);
							edges.add(e1);
						}	
					}
				}				
				for(Degree d : Newdist)
				{
					sum = sum + d.getDegree();
				}
			}
		}
		return edges;
	}
	
	private static ArrayList<Edge> readOrigGraph() throws IOException{
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Scanner scanner = new Scanner(new File(fileIn));
		scanner.useDelimiter(",");
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] people = line.split(",");
			for(int i = 1; i<people.length-1; i++){
				Edge e = new Edge(people[0], people[i]);
				edges.add(e);
			}
		}
		return edges;
	}
	
	private static ArrayList<Edge> priorityConstructGraph(ArrayList<Degree> Newdist, ArrayList<Edge> origGraph)
	{
		
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int sum = 0;
		for(Degree d : Newdist)
		{
			sum = sum + d.getDegree();
		}
		if(sum%2 != 0)
		{
//			System.out.println("infattibile " + sum);
		}
		else {
//			System.out.println("anonimizzazione grafo in corso con l'algorimo priority...");
//			System.out.print("aggiunta nodi originali...");
			
			for(int k = 0; k<Newdist.size(); k++)
			{
				Degree selected = Newdist.get(k);
				String w = null;
				while(selected.getDegree()>0) 
				{
					w = null;

					for(Edge e : origGraph)
					{
						if(e.v.equals(selected.name))
						{
							w = e.w;
							Edge er = new Edge(w, selected.name);
							origGraph.remove(e);
							//origGraph.remove(er);
							break;
						}
					}
					
					if(w!=null) {
						Edge e1 = new Edge(selected.name, w);
						Edge e2 = new Edge(w, selected.name);
						edges.add(e1);
						//edges.add(e2);
						for(Degree d : Newdist)
						{
							if(d.name.equals(selected.name) )
							{
								d.setDegree(d.getDegree()-1);
							}
							else if( d.name.equals(w))
							{
								//d.setDegree(d.getDegree()-1);
							}
						}
						//ho trovato un amico originale e lo riaggiungo al grafo anonimizzato
					}
					else {
						break;
					}
				}	
			}
			
//			System.out.println("fatto!");
//			System.out.print("aggiunta archi per anonimizzazione...");
			
			
				
				Collections.sort(Newdist, new Comparator<Degree>(){
					@Override
			        public int compare(Degree o2, Degree o1) {
			            return o1.degree.compareTo(o2.degree);
			        }
				});
				
				for(int i=0; i<Newdist.size(); i++) {
					for(int j=0; j<Newdist.size(); j++) {
						if(i!=j) {
							if(Newdist.get(i).degree>0 && Newdist.get(j).degree>0) {
								Edge e1 = new Edge(Newdist.get(i).name, Newdist.get(j).name);
								Edge e2 = new Edge(Newdist.get(j).name, Newdist.get(i).name);
								
								if(!edges.contains(e1) && !edges.contains(e2)) {
									edges.add(e1);
									edges.add(e2);
									Newdist.get(i).setDegree(Newdist.get(i).degree-1);
									Newdist.get(j).setDegree(Newdist.get(j).degree-1);
									//System.out.println("+++ " + e1.v + " " + e1.w);
								}
							}
						}
					}
				}
				
		}
		
//		System.out.print("fatto!\n");
		return edges;
		
	}
	
	private static void buildAnon(ArrayList<Edge> edges, int k) {

		if(!edges.isEmpty())
			{
				Collections.sort(edges, new Comparator<Edge>(){
					@Override
			        public int compare(Edge o1, Edge o2) {
			            return o1.v.compareTo(o2.v);
			        }
				});
				
				FileWriter fw = null;
				try {
					fw = new FileWriter("Anon_"+k+"_"+fileIn, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PrintWriter pw = new PrintWriter(fw);
				
				
				int idx = 0;
				
				while(idx<edges.size())
			    {
					StringBuilder sb = new StringBuilder();
			    	int cellcnt = 0;
			    	String first = edges.get(idx).getV();
			    	
			    	sb.append(first);
					sb.append(';');
				    
			    	while(idx<edges.size() && first.equals(edges.get(idx).getV()))
			    	{
			    		sb.append(edges.get(idx).getW());
			    		sb.append(';');
					    idx++;
			    	}
			    	pw.write(sb.toString());
					pw.println();	
			    } 
				 pw.close();
			}
		
	}

	private static void buildOutput(String fileIn, int k ,String anonMode,String buildMode, int cost, double time){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter("output.csv", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		StringBuilder sb = new StringBuilder();
		
		sb.append(fileIn);
		sb.append(';');
		sb.append(k);
		sb.append(';');
		sb.append(anonMode);
		sb.append(';');
		sb.append(buildMode);
		sb.append(';');
		sb.append(cost);
		sb.append(';');
		sb.append(time);
		sb.append(';');		
		
		pw.write(sb.toString());
		pw.println();
        pw.close();
//        System.out.println("output CSV file printed!");	
	}

}
