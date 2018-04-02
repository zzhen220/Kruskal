/* WUGraph.java */

package src.graph;

import src.dict.*;
import src.list.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  protected HashTableChained vTable;
  protected HashTableChained eTable;
  protected List vList;
  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
	  vTable = new HashTableChained(4);
	  vTable.makeEmpty();
	  eTable = new HashTableChained(8);
	  eTable.makeEmpty();
	  vList = new DList();
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
	  return vList.length();
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
	  return eTable.size();
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
  public Object[] getVertices() {
	Object[] output = new Object[this.vertexCount()];
	ListNode cuNode = vList.front();
	Vertex cuV;
	int i = 0;
	while(cuNode.isValidNode()) {
		try {
			cuV = (Vertex) cuNode.item();
			output[i++] = cuV.outVertex;
			cuNode = cuNode.next();
		} catch (InvalidNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return output;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
	if(vTable.find(vertex)==null) {
		Vertex inner = new Vertex(vertex);
		vTable.insert(vertex, inner);
		vList.insertBack(inner);
		inner.internal = vList.back();
	}
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
	  Entry vEntry = vTable.remove(vertex);
	  if(vEntry != null) {
		  Vertex inner = (Vertex) vEntry.value();
		  List eList = inner.adjList;
		  ListNode cuNode = eList.front();
		  Edge cuEdge;
		  while(cuNode.isValidNode()) {
			  try {
				cuEdge = (Edge) cuNode.item();
				eTable.remove(new VertexPair(cuEdge.v1.outVertex,cuEdge.v2.outVertex));
				cuNode = cuNode.next();
				cuEdge.partner.remove();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  try {
			inner.internal.remove();
		} catch (InvalidNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
	  return (vTable.find(vertex)!=null);
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex) {
	  Entry vEntry = vTable.find(vertex);
	  if(vEntry!=null) {
		  Vertex inner = (Vertex) vEntry.value();
		  return inner.adjList.length();
	  } else {
		  return 0;
	  }
  }

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex) {
	  int d = this.degree(vertex);
	  if(d==0) {
		  return null;
	  } else {
		  Neighbors output = new Neighbors();
		  output.neighborList = new Object[d];
		  output.weightList = new int[d];
		  Vertex inner = (Vertex) vTable.find(vertex).value();
		  ListNode cuNode = inner.adjList.front();
		  Edge cuEdge;
		  for(int i=0; i<d;i++) {
			  try {
				cuEdge = (Edge) cuNode.item();
				output.neighborList[i] = cuEdge.v2.outVertex;
				output.weightList[i] = cuEdge.weight;
				cuNode = cuNode.next();
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  return output;
	  }
  }

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
	  if(this.isVertex(u)&&this.isVertex(v)) {
		  VertexPair uv = new VertexPair(u,v);
		  Entry eEntry = eTable.find(uv);
		  if(eEntry!=null) {
			  Edge e1 = (Edge) eEntry.value();
			  e1.weight = weight;
			  try {
				Edge e2 = (Edge) e1.partner.item();
				e2.weight = weight;
			} catch (InvalidNodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  } else {
			  if(u.equals(v)) {
				  Vertex v1 = (Vertex) vTable.find(u).value();
				  Edge e1 = new Edge(v1,v1,weight);
				  eTable.insert(uv, e1);
				  v1.adjList.insertBack(e1);
				  e1.partner = v1.adjList.back();
			  } else {
				  Vertex v1 = (Vertex) vTable.find(u).value();
				  Vertex v2 = (Vertex) vTable.find(v).value();
				  Edge e1 = new Edge(v1,v2,weight);
				  Edge e2 = new Edge(v2,v1,weight);
				  eTable.insert(uv, e1);
				  v1.adjList.insertBack(e1);
				  v2.adjList.insertBack(e2);
				  e1.partner = v2.adjList.back();
				  e2.partner = v1.adjList.back();
			  }
		  }

	  }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
	  if(this.isVertex(u)&&this.isVertex(v)) {
		  VertexPair uv = new VertexPair(u,v);
		  Entry eEntry = eTable.find(uv);
		  if(eEntry!=null) {
			  Edge e1 = (Edge) eEntry.value();
			  try {
				Edge e2 = (Edge) e1.partner.item(); 
				e1.partner.remove();
				if(e2.partner.isValidNode()) {
					e2.partner.remove();
				}
			} catch (InvalidNodeException e11) {
				// TODO Auto-generated catch block
				e11.printStackTrace();
			}
			  eTable.remove(uv);
		  }
	  }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
	  boolean result = false;
	  if(this.isVertex(u)&&this.isVertex(v)) {
		  if(eTable.find(new VertexPair(u,v))!=null) {
			  result = true;
		  }
	  }
	  return result;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v) {
	  if(this.isEdge(u, v)) {
		  Edge e = (Edge) eTable.find(new VertexPair(u,v)).value();
		  return e.weight;
	  } else {
		  return 0;
	  }
  }
}
