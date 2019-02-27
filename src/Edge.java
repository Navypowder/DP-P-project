
public class Edge {
	private String v;
	private String w;

	public Edge(String v, String w) {
		this.v = v;
		this.w = w;
	}
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	public String getW() {
		return w;
	}
	public void setW(String w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "[" + v + " " + w + "]";
	}
}
