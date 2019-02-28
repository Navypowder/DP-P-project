/**
 * 
 */
package objects;

import java.util.ArrayList;
/**
 * @author ACGG
 *
 */
public class DA {

	
	/**
	 * 
	 */
	private ArrayList<ArrayList<Integer>> seq;
	/**
	 * 
	 */
	private int cost;

	/**
	 * @param seq
	 * @param cost
	 */
	public DA(ArrayList<ArrayList<Integer>> seq, int cost) {
		this.seq = seq;
		this.cost = cost;
	}
	/**
	 * @return the seq
	 */
	public ArrayList<ArrayList<Integer>> getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(ArrayList<ArrayList<Integer>> seq) {
		this.seq = seq;
	}
	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

}
