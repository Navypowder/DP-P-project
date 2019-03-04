import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import objects.*;


public class Main {
    private static String[] ANON_ALGOS      = new String[]{"greedy", "dp"};
    private static String[] CONSTR_ALGOS    = new String[]{"constr", "prio"};

    private static String fileIn, anonMode, buildMode;
    private static int k;
    private static boolean printAnonGraph;

    public static void main(String[] args) throws InterruptedException, IOException {
        parseArgs(args);

        ArrayList<ArrayList<String>> graph = LoadData.readData(fileIn);

        ArrayList<Degree> dist      = LoadData.getDegrees(graph);
        ArrayList<Edge> origGraph   = LoadData.getEdges(graph);
        Collections.sort(dist);

        ArrayList<DA> das           = new ArrayList<>();
        ArrayList<Degree> grouped   = null;
        ArrayList<Edge> edges       = null;

        boolean feasible;
        long startTime, totalTime;
        int anonCost = 0;
        
        // Inizio a misurare il tempo
        startTime = System.nanoTime();

        if (anonMode.equals(ANON_ALGOS[0])) {
            // Creazione lista dei gradi con algoritmo: greedy
            grouped     = Grouper.greedyGrouper(k, dist);
            anonCost    = computeCostGreedy(dist, grouped);
        }
        else if (anonMode.equals(ANON_ALGOS[1])) {
            // Creazione lista dei gradi con algoritmo: dynamic programming
            grouped     = Grouper.dpGrouper(k, dist, das);
            anonCost    = computeCostDP(das);
        }

        if (buildMode.equals(CONSTR_ALGOS[0])) {
            //Anonimizza con l'algoritmo greedy
            edges = Construct.greedyConstructGraph(grouped);
        }
        else if (buildMode.equals(CONSTR_ALGOS[1])) {
            //Anonimizza con l'algoritmo Priority
            edges = Construct.priorityConstructGraph(grouped, origGraph);
        }

        // Misuro il tempo impiegato a costruire il grafo anonimizzato
        totalTime   = System.nanoTime() - startTime;
        feasible    = !edges.isEmpty();

        if (printAnonGraph) {
            // Stampo il nuovo grafo anonimizzato
            printAnonymizedGraph(edges, k, fileIn);
        }

        // Stampo i risultati
        printCostToCSV(fileIn, k, anonMode, buildMode, anonCost,totalTime/10^6, feasible);
    }

    private static int computeCostGreedy(ArrayList<Degree> oldDegrees, ArrayList<Degree> groupedDegrees) {
        int initcost = 0;
        for (Degree d : oldDegrees) {
            initcost += d.getDegree();
        }

        int anoncost = 0;
        for (Degree d : groupedDegrees) {
            anoncost += d.getDegree();
        }

        return anoncost - initcost;
    }

    private static int computeCostDP(ArrayList<DA> das) {
        return das.get(das.size() - 1).getCost();
    }

    private static void printCostToCSV(String fileIn, int k, String anonMode, String buildMode, int cost, long time, boolean feasible) throws IOException {
        FileWriter fw   = new FileWriter("..//output//output_" + fileIn + ".csv", true);
        PrintWriter pw  = new PrintWriter(fw);
        String output   = fileIn + ";" + k + ";" + anonMode + ";" + buildMode + ";" + cost + ";" + time + ";" + feasible;

        pw.write(output);
        pw.println();
        pw.close();
    }

    private static void printAnonymizedGraph(ArrayList<Edge> edges, int k, String fileIn) throws IOException {
        if (edges.isEmpty()) {
            return;
        }

        Collections.sort(edges);

        FileWriter fw   = new FileWriter("Anon_" + k + "_" + fileIn, false);
        PrintWriter pw  = new PrintWriter(fw);

        int idx = 0;
        while (idx < edges.size()) {
            StringBuilder sb    = new StringBuilder();
            String first        = edges.get(idx).getPrev();
            sb.append(first);
            sb.append(';');
            while (idx < edges.size() && first.equals(edges.get(idx).getPrev())) {
                sb.append(edges.get(idx).getNext());
                sb.append(';');
                idx++;
            }
            pw.write(sb.toString());
            pw.println();
        }
        pw.close();
    }

    private static void parseArgs(String[] args) {
        fileIn = args[0];
        k = Integer.parseInt(args[1]);
        anonMode = args[2];
        buildMode = args[3];
        printAnonGraph = Boolean.parseBoolean(args[4]);
    }
}
