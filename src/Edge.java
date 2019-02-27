/**
 * 
 */

/**
 * @author ACGG
 *
 */
public class Edge {

	
	/**
	 * 
	 */
	private String prev;
	/**
	 * 
	 */
	private String next;
	/**
	 * 
	 */
	public Edge() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param prev
	 * @param next
	 */
	public Edge(String prev, String next) {
		super();
		this.prev = prev;
		this.next = next;
	}
	/**
	 * @return the prev
	 */
	public String getPrev() {
		return prev;
	}
	/**
	 * @param prev the prev to set
	 */
	public void setPrev(String prev) {
		this.prev = prev;
	}
	/**
	 * @return the next
	 */
	public String getNext() {
		return next;
	}
	/**
	 * @param next the next to set
	 */
	public void setNext(String next) {
		this.next = next;
	}
	
}
