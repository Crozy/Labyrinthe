package Controller;

import java.util.ArrayList;

public class Node {
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> memoire;
	private String name;
	private int num;
	private int score;
	
	public Node(String name) {
		memoire = new ArrayList<>();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNum() {
		return num;
	}

	private String actionEffectue;

	public ArrayList<Integer> getMemoire() {
		return memoire;
	}

	public void setMemoire(ArrayList<Integer> memoire) {
		this.memoire = memoire;
	}

}
