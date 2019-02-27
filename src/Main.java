import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static String fileIn;
    public static void main(String[] args) throws FileNotFoundException {
        fileIn = args[0];
        int k = Integer.parseInt(args[1]);
        String anonMode = args[2];
        String buildMode = args[3];

        ArrayList<ArrayList<String>> graph = LoadData.readData(fileIn);
        ArrayList<Degree>   dist        = LoadData.getDegrees(graph);
        ArrayList<Edge>     origEdges   = LoadData.getEdges(graph);

        ArrayList<DA> das = new ArrayList<>();
        ArrayList<Degree> grouped;

        if (anonMode.equals("greedy")) {
            // Creazione lista dei gradi con algoritmo: greedy
            grouped = Grouper.greedyGrouper(k, dist);
        }
        if (anonMode.equals("dp")) {
            // Creazione lista dei gradi con algoritmo: dynamic programming
            grouped = Grouper.dpGrouper(k, dist, das);
        }


    }




}