import java.util.ArrayList;

public class Cluster {
	
	private int id;
	private ArrayList<ArrayList<Integer>> degrees;
	private int cost;
	
	public Cluster(Integer id, ArrayList<ArrayList<Integer>> degrees, int cost) {
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
