import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import objects.Degree;
import objects.Edge;

public class Construct {
    public static ArrayList<Edge> priorityConstructGraph(ArrayList<Degree> newDist, ArrayList<Edge> origGraph) {
        ArrayList<Edge> edges = new ArrayList<>();
        int sum = 0;
        for (Degree d : newDist) { sum += d.getDegree(); }

        if (sum % 2 != 0) {
            // Infeasible (edges.size() == 0)
            return edges;
        }

        for (Degree selected : newDist) {
            String w;
            while (selected.getDegree() > 0) {
                w = null;
                for (Edge edge : origGraph) {
                    if (edge.getPrev().equals(selected.getName())) {
                        w = edge.getNext();
                        origGraph.remove(edge);
                        break;
                    }
                }

                if (w == null) { break; }

                Edge e1 = new Edge(selected.getName(), w);
                edges.add(e1);
                for (Degree d : newDist) {
                    if (d.getName().equals(selected.getName()) ) {
                        d.setDegree(d.getDegree() - 1);
                    }
                }
            }
        }

        Collections.sort(newDist);

        for (int i=0; i < newDist.size(); i++) {
            for (int j=0; j < newDist.size(); j++) {
                if (i != j) {
                    if (newDist.get(i).getDegree() > 0 && newDist.get(j).getDegree() > 0) {
                        Edge e1 = new Edge(newDist.get(i).getName(), newDist.get(j).getName());
                        Edge e2 = new Edge(newDist.get(j).getName(), newDist.get(i).getName());

                        if (!edges.contains(e1) && !edges.contains(e2)) {
                            edges.add(e1);
                            edges.add(e2);
                            newDist.get(i).setDegree(newDist.get(i).getDegree() - 1);
                            newDist.get(j).setDegree(newDist.get(j).getDegree() - 1);
                        }
                    }
                }
            }
        }
        return edges;
    }

    public static ArrayList<Edge> greedyConstructGraph(ArrayList<Degree> newDist) throws InterruptedException {
        ArrayList<Edge> edges = new ArrayList<>();
        int sum = 0;
        for (Degree d : newDist) { sum += d.getDegree(); }

        if (sum%2 != 0) {
            // Infeasible
            return edges;
        }

        while (sum > 0) {
            sum = 0;
            Random random = new Random();
            int k = random.nextInt(newDist.size());
            Degree v = newDist.get(k);
            //grado del nodo preso randomicamente da newdist
            if (v.getDegree() > 0) {
                //setto come max di riferimento il grado del nodo preso randomicamente
                int max = v.getDegree();
                //ne setto ora il grado a 0
                v.setDegree(0);
                //ordino newdist per grado in maniera tale da prendere quelli di grado pi˘ alto succedssivaemnte nel codice
                Collections.sort(newDist);

                for (int i = 0; i < max; i++) {
                    //fino al grado del nodo prelevato randomicamente
                    //prelevo da newdist il primo nodo ogni volta ( grado piu alto)
                    //e lo linko a quello prelevato radnomicamente
                    if (!newDist.get(i).getName().equals(v.getName())) {
                        Edge e1 = new Edge(v.getName(), newDist.get(i).getName());
                        Edge e2 = new Edge(newDist.get(i).getName(), v.getName());
                        newDist.get(i).setDegree(newDist.get(i).getDegree()-1);
                        edges.add(e1);
                        edges.add(e2);
                    }
                }
            }

            // Calcolo la somma dei gradi
            for (Degree d : newDist) { sum += d.getDegree(); }
        }
        return edges;
    }




}
