import java.util.ArrayList;

public class Cluster {
	
	int id;
	ArrayList<ArrayList<Integer>> degrees = new ArrayList<ArrayList<Integer>>();
	int cost;
	
	public Cluster(Integer id, ArrayList<ArrayList<Integer>> degrees, int cost) {
		super();
		this.id = id;
		this.degrees = degrees;
		this.cost = cost;
	}
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public ArrayList<ArrayList<Integer>> getDegrees() {
		return degrees;
	}

	public void setDegrees(ArrayList<ArrayList<Integer>> degrees) {
		this.degrees = degrees;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	
	
	
}
