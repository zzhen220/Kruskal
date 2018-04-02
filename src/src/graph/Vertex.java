package src.graph;

import src.list.*;

class Vertex {
	protected Object outVertex;
	protected ListNode internal;
	protected List adjList;
	
	public Vertex(Object o) {
		outVertex = o;
		adjList = new DList();
	}
}
