import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import objects.*;


public class Main {
    private static String[] ANON_ALGOS      = new String[]{"greedy", "dp"};
    private static String[] CONSTR_ALGOS    = new String[]{"constr", "prio"};

    public static void main(String[] args) throws InterruptedException, IOException {
        String fileIn       = args[0];
        int k               = Integer.parseInt(args[1]);
        String anonMode     = args[2];
        String buildMode    = args[3];

        ArrayList<ArrayList<String>> graph = LoadData.readData(fileIn);

        ArrayList<Degree> dist = LoadData.getDegrees(graph);
        ArrayList<Edge> origGraph = LoadData.getEdges(graph);

        ArrayList<DA> das = new ArrayList<>();
        ArrayList<Degree> grouped = null;

        if (anonMode.equals(ANON_ALGOS[0])) {
            // Creazione lista dei gradi con algoritmo: greedy
            grouped = Grouper.greedyGrouper(k, dist);
        }
        if (anonMode.equals(ANON_ALGOS[1])) {
            // Creazione lista dei gradi con algoritmo: dynamic programming
            grouped = Grouper.dpGrouper(k, dist, das);
        }

        int initcost = 0;
        for (Degree d : dist) {
            initcost += d.getDegree();
        }

        int anoncost = 0;
        for (Degree d : grouped) {
            anoncost += d.getDegree();
        }

        long startTime = System.currentTimeMillis();

        ArrayList<Edge> edges = null;
        if (buildMode.equals(CONSTR_ALGOS[0])) {
            //Anonimizza con l'algoritmo greedy
            edges = Construct.greedyConstructGraph(grouped);
        }

        if (buildMode.equals(CONSTR_ALGOS[1])) {
            //Anonimizza con l'algoritmo Priority
            edges = Construct.priorityConstructGraph(grouped, origGraph);
        }

        long finishTime = System.currentTimeMillis();
        long totalTime  = finishTime - startTime;

        // Stampo inl nuovo grafo anonimizzato
        //printAnonymizedGraph(edges, k, fileIn);

        int diversity = 0;
        for (Edge e : origGraph) {
            if (!edges.contains(e)) {
                diversity++;
            }
        }

        int cost = 0;

        if (anonMode.equals(ANON_ALGOS[0])) {
            cost = initcost - anoncost;
        }

        if (anonMode.equals(ANON_ALGOS[1])) {
            cost = das.get(das.size() - 1).getCost();
        }

        printCostToCSV(fileIn, k, anonMode, buildMode, cost, totalTime);
    }


    private static void printCostToCSV(String fileIn, int k, String anonMode, String buildMode, int cost, long time) throws IOException {
        FileWriter fw   = new FileWriter("output.csv", true);
        PrintWriter pw  = new PrintWriter(fw);
        String output   = fileIn + ";" + k + ";" + anonMode + ";" + buildMode + ";" + cost + ";" + time;

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
}
