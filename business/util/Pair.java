package business.util;

public class Pair {
	private Integer first;
	private Integer second;
	public Pair(Integer first, Integer second) {
		this.first = first;
		this.second = second;
		
	}
	
	public String toString() {
		return "(" + first.toString() + ", " + second.toString() + ")";
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}
	
}
