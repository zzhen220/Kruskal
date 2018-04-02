/* Kruskal.java */

package src.graphalg;

import src.dict.HashTableChained;
import src.graph.*;
import src.set.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {
	  WUGraph t = new WUGraph();
	  Object[] vertices = g.getVertices();
	  Heapsort hs = new Heapsort(g.edgeCount());
	  HashTableChained vIntTable = new HashTableChained(g.vertexCount());
	  vIntTable.makeEmpty();
	  HashTableChained edgeTable = new HashTableChained(g.edgeCount());
	  edgeTable.makeEmpty();
	  for(int i=0;i<g.vertexCount(); i++) {
		  t.addVertex(vertices[i]);
		  vIntTable.insert(vertices[i], new Integer(i));
		  Neighbors n = g.getNeighbors(vertices[i]);
		  for(int j=0; j<n.neighborList.length;j++) {
			  	Edge e = new Edge(vertices[i],n.neighborList[j],n.weightList[j]);
			  	if(edgeTable.find(e)==null) {
			  		hs.insert(e, e.weight);
			  		edgeTable.insert(e, new Boolean(true));
			  	}
		  }
	  }
	  hs.sort();
	  DisjointSets ds = new DisjointSets(g.vertexCount());
	  int num = 0;
	  for(int i=g.edgeCount()-1; i>=0; i--) {
		  Edge e = (Edge) hs.heap[i];
		  Object o1 = e.object1;
		  Object o2 = e.object2;
		  int v1 = ((Integer) vIntTable.find(o1).value()).intValue();
		  int v2 = ((Integer) vIntTable.find(o2).value()).intValue();
		  if(ds.find(v1)!=ds.find(v2)) {
			  t.addEdge(o1, o2, e.weight);
			  ds.union(v1, v2);
			  if(++num==g.vertexCount()-1) {break;}
		  }
	  }
	return t; 
  }
}
