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
        boolean printAnonGraph = Boolean.parseBoolean(args[4]);

        ArrayList<ArrayList<String>> graph = LoadData.readData(fileIn);

        ArrayList<Degree> dist = LoadData.getDegrees(graph);
        ArrayList<Edge> origGraph = LoadData.getEdges(graph);

        Collections.sort(dist);

        ArrayList<DA> das = new ArrayList<>();
        ArrayList<Degree> grouped = null;
        ArrayList<Edge> edges = null;

        int anonCost = 0;
        if (anonMode.equals(ANON_ALGOS[0])) {
            // Creazione lista dei gradi con algoritmo: greedy
            grouped = Grouper.greedyGrouper(k, dist);
            anonCost = computeCostGreedy(dist, grouped);
        }
        else if (anonMode.equals(ANON_ALGOS[1])) {
            // Creazione lista dei gradi con algoritmo: dynamic programming
            grouped = Grouper.dpGrouper(k, dist, das);
            anonCost = computeCostDP(das);
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




        // Inizio a misurare il tempo
        long startTime = System.currentTimeMillis();


        if (buildMode.equals(CONSTR_ALGOS[0])) {
            //Anonimizza con l'algoritmo greedy
            edges = Construct.greedyConstructGraph(grouped);
        }
        else if (buildMode.equals(CONSTR_ALGOS[1])) {
            //Anonimizza con l'algoritmo Priority
            edges = Construct.priorityConstructGraph(grouped, origGraph);
        }

        long finishTime = System.currentTimeMillis();
        // Misuro il tempo impiegato a costruire il grafo anonimizzato
        long totalTime  = finishTime - startTime;

        if (printAnonGraph) {
            // Stampo il nuovo grafo anonimizzato
            printAnonymizedGraph(edges, k, fileIn);
        }

        // Stampo i risultati
        printCostToCSV(fileIn, k, anonMode, buildMode, anonCost, totalTime);
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
