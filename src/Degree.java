
public class Degree implements Comparable<Degree>{
	private String name;
	private int degree;

	public Degree(String name, int degree) {
		this.name = name;
		this.degree = degree;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	@Override
	public int compareTo(Degree aDegree) {
		return Integer.compare(this.degree, aDegree.degree);
	}
}
