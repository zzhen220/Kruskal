package src.graph;

import src.list.*;

class Edge {
	protected Vertex v1;
	protected Vertex v2;
	protected ListNode partner;
	protected int weight;
	
	public Edge(Vertex u,Vertex v,int w) {
		v1 = u;
		v2 = v;
		weight = w;
	}
}
