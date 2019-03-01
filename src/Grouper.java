/**
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import objects.Degree;
import objects.DA;
import objects.Cluster;
/**
 * @author ACGG
 *
 */
public class Grouper {

    @SuppressWarnings("Duplicates")
    public static ArrayList<Degree> greedyGrouper_3(int k, ArrayList<Degree> degrees) {
        ArrayList<Degree> newDegrees = new ArrayList<>();
        for (Degree d : degrees) {
            Degree toAdd = new Degree(d.getName(), d.getDegree());
            newDegrees.add(toAdd);
        }
        Collections.sort(degrees);

        /*int start = 0;
        int end = k;

        int firstDegree = newDegrees.get(start).getDegree();
        for (int i = start; i < end; i++) {
            newDegrees.get(i).setDegree(firstDegree);
        }

        int cMerge = (firstDegree - newDegrees.get(end).getDegree()) + computeI_2(newDegrees.subList(end + 1, 2 * end));
        int cNew = computeI_2(newDegrees.subList(end, 2 * end - 1));

        if (cMerge > cNew) {
            start = end;
            end = newDegrees.size();
        } else {
            start = end + 1;
            end += start;
        }*/

        greedyGrouper_ric(newDegrees, 0, k);


        return newDegrees;
    }

    @SuppressWarnings("Duplicates")
    public static void greedyGrouper_ric(ArrayList<Degree> degrees, int start, int end) {
        int firstDegree = degrees.get(start).getDegree();
        for (int i = start; i < end; i++) {
            degrees.get(i).setDegree(firstDegree);
        }

        if (end >= degrees.size()) {
            return;
        }

        int cMerge  = (firstDegree - degrees.get(end).getDegree()) + computeI_2(degrees.subList(end + 1, 2 * end));
        int cNew    = computeI_2(degrees.subList(end, 2 * end - 1));

        if (cMerge > cNew) {
            //start   = end;
            //end     = degrees.size();
            greedyGrouper_ric(degrees, end, degrees.size());
        } else {
            degrees.get(end).setDegree(firstDegree);
            start = end + 1;
            end += start;

            for (int i = start; i < end; i++) {
                cMerge  = (firstDegree - degrees.get(end).getDegree()) + computeI_2(degrees.subList(end + 1, 2 * end));
                cNew    = computeI_2(degrees.subList(end, 2 * end - 1));
                 if (cMerge > cNew) {
                     greedyGrouper_ric(degrees, end, degrees.size());
                 } else {

                 }
            }
        }
    }

    @SuppressWarnings("Duplicates")
    public static ArrayList<Degree> greedyGrouper_2(int k, ArrayList<Degree> degrees) {
        ArrayList<Degree> newDegrees = new ArrayList<>();
        for (Degree d : degrees) {
            Degree toAdd = new Degree(d.getName(), d.getDegree());
            newDegrees.add(toAdd);
        }
        Collections.sort(newDegrees);

        int start   = 0;
        int end     = k;

        while (end <= newDegrees.size()) {
            int firstDegree = newDegrees.get(start).getDegree();
            for (int i = start; i < end; i++) {
                newDegrees.get(i).setDegree(firstDegree);
            }

            if (end == newDegrees.size()) {
                break;
            }

            int cMerge  = (firstDegree - newDegrees.get(end).getDegree()) + computeI_2(newDegrees.subList(end + 1, 2 * end));
            int cNew    = computeI_2(newDegrees.subList(end, 2 * end - 1));

            if (cMerge > cNew) {
                start = end;
                end = newDegrees.size();
            } else {
                newDegrees.get(end).setDegree(firstDegree);
                start = end + 1;
                end += start;
                for (int i = start; i < end; i++) {
                    int cMerge2  = (newDegrees.get(start).getDegree() - newDegrees.get(end).getDegree()) + computeI_2(newDegrees.subList(end + 1, 2 * end));
                    int cNew2    = computeI_2(newDegrees.subList(end, 2 * end - 1));

                    if (cMerge2 > cNew2) {
                        break;
                    } else {
                        newDegrees.get(start).setDegree(firstDegree);
                    }
                }
            }
            // end += start;
        }
        return newDegrees;
    }

    private static int computeI_2(List<Degree> degrees) {
        int I = 0;
        int firstDegree = degrees.get(0).getDegree();
        for (Degree d : degrees) {
            I += (firstDegree - d.getDegree());
        }
        return I;
    }


	public static ArrayList<Degree> greedyGrouper(int k, ArrayList<Degree> dist) {
        ArrayList<Degree> newDist = new ArrayList<>();

        int idx = 0;
        int C1 = dist.get(idx).getDegree();
        for (int i = idx; i < k; i++) {
            Degree degree = new Degree(dist.get(i).getName(), C1 /** C1*/);
            newDist.add(degree);
        }

        idx = k;
        while (newDist.size() != dist.size()) {
            if ((idx + k) > dist.size()) {
                k = dist.size() - idx;
                for(int i = 0; i < k; i++, idx++) {
                    Degree degree = new Degree(dist.get(idx).getName(), newDist.get(idx - 1).getDegree());
                    newDist.add(degree);
                }
                break;
            }

            int Cmerge  = (dist.get(0).getDegree() - dist.get(idx).getDegree()) + computeI(idx + 1, idx + k - 1, dist);
            int Cnew    = computeI(idx, idx + k - 1, dist);

            if (Cmerge > Cnew) {
                //new cluster creation
                int ngc = dist.get(idx).getDegree();
                for(int i = idx; i<idx+k; i++) {
                    Degree degree = new Degree(dist.get(i).getName(), ngc/* * ngc*/);
                    newDist.add(degree);
                }
                idx += k;
            } else {
                //merge section
                Degree degree = new Degree(dist.get(idx).getName(), newDist.get(idx - 1).getDegree());
                newDist.add(degree);
                idx++;
            }
        }

        /*for (Degree degree : newDist) {
            degree.setDegree((int)Math.sqrt(degree.getDegree()));
        }*/

        return newDist;
    }

    public static ArrayList<Degree> dpGrouper(int k, ArrayList<Degree> dist, ArrayList<DA> das) {
        ArrayList<Degree> newDist = new ArrayList<>();

        Collections.sort(dist);

        int firstDegree = dist.get(0).getDegree();

        for (int i = 0; i < ((2 * k) - 1); i++) {
            ArrayList<ArrayList<Integer>> degrees = new ArrayList<>();

            ArrayList<Integer> deg = new ArrayList<>();
            for(int j = 0 ; j <= i; j++) {
                deg.add(firstDegree);
            }

            degrees.add(deg);

            DA da = new DA(degrees, computeI(0, i, dist));
            das.add(da);
        }

        for (int j = 2*k; j <= dist.size(); j++) {
            ArrayList<Cluster> tmp = new ArrayList<>();
            //int localId = 1;
            for (int t = k, localId = 1; t <= j-k; t++, localId++) {
                // Blocco inutile ??
//                ArrayList<Integer> d = new ArrayList<>();
//                for (int l = 0; l < t; l++) {
//                    d.add(firstDegree);
//                }
                ////////////

                Cluster onet = new Cluster(localId, das.get(t-1).getSeq(), das.get(t-1).getCost());

                ArrayList<ArrayList<Integer>> adt = new ArrayList<> ();
                ArrayList<Integer> dt = new ArrayList<>();
                for (int l = t; l < j; l++){
                    dt.add(dist.get(t).getDegree());
                }
                adt.add(dt);

                Cluster tponei = new Cluster(localId, adt, computeI(t, j-1, dist));

                int cNew = onet.getCost() + tponei.getCost();
                int cMerge = computeI(0, j-1, dist);

//				System.out.println("cmerge : " + cMerge + " cnew: " + cNew);

                // qui valuto se fare il merging sul cluster precedente o se creare un nuovo cluster
                if (cNew < cMerge) {
                    //System.out.println("NEW");
                    tmp.add(onet);
                    tmp.add(tponei);
                } else {
//					System.out.println("MERGE");
                    ArrayList<Integer> deg = new ArrayList<>();
                    for (int z = 0; z < j; z++) {
                        deg.add(firstDegree);
                    }
                    ArrayList<ArrayList<Integer>> adeg = new ArrayList<> ();
                    adt.add(deg);
                    Cluster oneinew = new Cluster(localId++, adeg, computeI(0, j, dist));
                    tmp.add(oneinew);
                }
                //localId++;
            }

//			for(Cluster cluster : tmp){
//				System.out.println(cluster.id + " " + cluster.degrees + " " + cluster.cost);
//			}

            Cluster c1 = null, c2 = null;
            //Cluster c2 = null;
            int v   = 0;
            int min = Integer.MAX_VALUE;
            while (v < tmp.size()){
                if (tmp.get(v).getCost() + tmp.get(v+1).getCost() <= min){
                    min = tmp.get(v).getCost() + tmp.get(v+1).getCost();
                    c1  = tmp.get(v);
                    c2  = tmp.get(v+1);
                }
                v += 2;
            }

            ArrayList<ArrayList<Integer>> agg = new ArrayList<> ();
            agg.addAll(c1.getDegrees());
            agg.addAll(c2.getDegrees());
            das.add(new DA(agg, (c1.getCost() + c2.getCost())));
        }

        //qui creo il newdist da DA(1, dist.size())
        int cnt = 0;
        ArrayList<ArrayList<Integer>> sol = das.get(das.size()-1).getSeq();
        for (ArrayList<Integer> arr : sol) {
            for (Integer i : arr) {
                Degree grado = new Degree(dist.get(cnt).getName(), i);
                newDist.add(grado);
                cnt++;
            }
        }
        return newDist;
    }

    private static int computeI(int i, int j, ArrayList<Degree> dist ) {
        int I = 0;
        for (int l = i; l <= j; l++) {
            I += (dist.get(i).getDegree() - dist.get(l).getDegree());
        }
        return I;
    }
}
