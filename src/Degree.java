/**
 * 
 */

/**
 * @author ACGG
 *
 */
public class Degree implements Comparable<Degree>{
	
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private int degree;

	/**
	 * @param name
	 * @param degree
	 */
	public Degree(String name, int degree) {
		this.name 	= name;
		this.degree = degree;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}
	/**
	 * @param degree the degree to set
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	@Override
	public int compareTo(Degree aDegree) {
		return Integer.compare(aDegree.degree, this.degree);
	}

}
