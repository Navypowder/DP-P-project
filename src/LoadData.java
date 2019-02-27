/**
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/**
 * @author ACGG
 *
 */
public class LoadData {
	/**
	 * 
	 */
	public static ArrayList<ArrayList<String>> readData(String fileName) throws FileNotFoundException {
        ArrayList<ArrayList<String>> graph = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            ArrayList<String> nodes = new ArrayList<>((Arrays.asList(line)));
            graph.add(nodes);
        }
        return  graph;
    }
	/**
	 * 
	 */
    public static ArrayList<Degree> getDegrees(ArrayList<ArrayList<String>> graph) {
        ArrayList<Degree> degrees = new ArrayList<>();
        for (ArrayList<String> node : graph) {
            Degree degree = new Degree(node.get(0), node.size() - 1);
            degrees.add(degree);
        }
        return degrees;
    }
    /**
	 * 
	 */
    public static ArrayList<Edge> getEdges(ArrayList<ArrayList<String>> graph) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (ArrayList<String> node : graph) {
            String orig = node.get(0);
            for (String s : node.subList(1, node.size() - 1)) {
                Edge edge = new Edge(orig, s);
                edges.add(edge);
            }
        }
        return edges;
    }
}
