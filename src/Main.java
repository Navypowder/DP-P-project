import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import objects.*;

public class Main {
    private static final String[] ANON_ALGOS      = new String[]{"greedy", "dp"};
    private static final String[] CONSTR_ALGOS    = new String[]{"constr", "prio"};

    private static final long ITERATIONS = 30;

    private static String fileIn, anonMode, buildMode;
    private static int k;
    private static boolean printAnonGraph;

    public static void main(String[] args) throws InterruptedException, IOException {
        parseArgs(args);

        ArrayList<ArrayList<String>> graph = LoadData.readData(fileIn);
        ArrayList<Degree> dist = LoadData.getDegrees(graph);
        ArrayList<Edge> origGraph = LoadData.getEdges(graph);

        Collections.sort(dist);

        ArrayList<DA> das = new ArrayList<>();
        ArrayList<Degree> grouped = null;
        ArrayList<Edge> edges = null;

        int  groupCost = 0;
        long groupTime = 0;
        if (anonMode.equals(ANON_ALGOS[0])) {
            // Creazione lista dei gradi con algoritmo: greedy
            for (int i = 0; i < ITERATIONS; i++) {
                long startTime = System.nanoTime();

                Grouper.greedyGrouper_1(k, dist);
                groupTime += (System.nanoTime() - startTime)/ITERATIONS;
            }
            grouped = Grouper.greedyGrouper_1(k, dist);
            groupCost = computeCostGreedy(dist, grouped);
        }
        else if (anonMode.equals(ANON_ALGOS[1])) {
            // Creazione lista dei gradi con algoritmo: dynamic programming
            for (int i = 0; i < ITERATIONS; i++) {
                das = new ArrayList<>();
                long startTime = System.nanoTime();
                Grouper.dpGrouper(k, dist, das);
                groupTime += (System.nanoTime() - startTime)/ITERATIONS;
            }
            das = new ArrayList<>();
            grouped = Grouper.dpGrouper(k, dist, das);
            groupCost = computeCostDP(das);
        }
<<<<<<< HEAD
/*
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (Degree d : grouped) {
            out.append(d.getDegree());
            out.append(", ");
        }
        out.deleteCharAt(out.length()-1);
        out.deleteCharAt(out.length()-1);
        out.append("]");

        System.out.println(out.toString());
*/



        // Inizio a misurare il tempo
        long startTime = System.currentTimeMillis();

=======

        //functionTest(grouped, k);
>>>>>>> ec7bbd2d699f87ab6067e3f5de27878637f9fcf3

       // Costruisco il grafo anonimizzato
        long constructTime = 0;
        if (buildMode.equals(CONSTR_ALGOS[0])) {
            //Anonimizza con l'algoritmo greedy
            edges = Construct.greedyConstructGraph(grouped);
            for (int  i = 0; i < ITERATIONS; i++) {
                long startTime = System.nanoTime();
                Construct.greedyConstructGraph(grouped);
                constructTime += (System.nanoTime() - startTime)/ITERATIONS;
            }

        }
        else if (buildMode.equals(CONSTR_ALGOS[1])) {
        //else if (buildMode.equals("prio")) {
            //Anonimizza con l'algoritmo Priority
            edges = Construct.priorityConstructGraph(grouped, origGraph);
            for (int i = 0; i < ITERATIONS; i++) {
                long startTime = System.nanoTime();
                Construct.priorityConstructGraph(grouped, origGraph);
                constructTime += (System.nanoTime() - startTime)/ITERATIONS;
            }
        }


        // Misuro il tempo impiegato a costruire il grafo anonimizzato
        long totalTime  = groupTime + constructTime;

        boolean feasible = (!edges.isEmpty());

        if (printAnonGraph) {
            // Stampo il nuovo grafo anonimizzato
            printAnonymizedGraph(edges, k, fileIn);
        }

        // Stampo i risultati
        printCostToCSV(fileIn, k, anonMode, buildMode, groupCost, totalTime/1000, feasible);
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

    private static int computeDiversity(ArrayList<Edge> edges) {
        int diversity = 0;
        for (Edge e : edges) {
            if (!edges.contains(e)) {
                diversity++;
            }
        }
        return diversity;
    }


    private static void printCostToCSV(String fileIn, int k, String anonMode, String buildMode, int cost, long time, boolean feasible) throws IOException {
        FileWriter fw   = new FileWriter("output.csv", true);
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


    private static void functionTest(ArrayList<Degree> grouped, int k) {
        ArrayList<Integer> deg = new ArrayList<>();
        for (Degree d : grouped) {
            deg.add(d.getDegree());
        }

        for (Integer i : deg) {
            int freq = Collections.frequency(deg, i);
            if (freq < k) {
                System.out.println("ERRORE frequenza: " + i + " freq: " + freq);
                break;
            }
            //System.out.print("[ elem: " + i + " freq: " + freq + " ], ");
        }

        StringBuilder out = new StringBuilder();
        out.append("[");
        for (Degree d : grouped) {
            out.append(d.getDegree());
            out.append(", ");
        }
        out.deleteCharAt(out.length()-1);
        out.deleteCharAt(out.length()-1);
        out.append("]");

        System.out.println(out.toString());
    }

    private static void parseArgs(String[] args) {
        fileIn = args[0];
        k = Integer.parseInt(args[1]);
        anonMode = args[2];
        buildMode = args[3];
        printAnonGraph = Boolean.parseBoolean(args[4]);
    }
}
