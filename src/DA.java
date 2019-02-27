import java.util.ArrayList;

public class DA {
	ArrayList<ArrayList<Integer>> seq;
	int cost;
	public DA(ArrayList<ArrayList<Integer>> seq, int cost) {
		super();
		this.seq = seq;
		this.cost = cost;
	}
	public ArrayList<ArrayList<Integer>> getSeq() {
		return seq;
	}
	public void setSeq(ArrayList<ArrayList<Integer>> seq) {
		this.seq = seq;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	
}
