/**
 * 
 */
package objects;

import java.util.ArrayList;
/**
 * @author ACGG
 *
 */
public class Cluster {

	
	/**
	 * 
	 */
	private int id;
	/**
	 * 
	 */
	private ArrayList<ArrayList<Integer>> degrees;
	/**
	 * 
	 */
	private int cost;
	/**
	 * 
	 */
	public Cluster() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param id
	 * @param degrees
	 * @param cost
	 */
	public Cluster(int id, ArrayList<ArrayList<Integer>> degrees, int cost) {
		super();
		this.id = id;
		this.degrees = degrees;
		this.cost = cost;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the degrees
	 */
	public ArrayList<ArrayList<Integer>> getDegrees() {
		return degrees;
	}
	/**
	 * @param degrees the degrees to set
	 */
	public void setDegrees(ArrayList<ArrayList<Integer>> degrees) {
		this.degrees = degrees;
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
