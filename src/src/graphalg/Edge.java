package src.graphalg;


class Edge {
	protected Object object1;
	protected Object object2;
	protected int weight;
	
	protected Edge(Object o1, Object o2, int w) {
		object1 = o1;
		object2 = o2;
		weight = w;
	}
	
	public int hashCode() {
	    if (object1.equals(object2)) {
	      return object1.hashCode() + 1;
	    } else {
	      return object1.hashCode() + object2.hashCode();
	    }
	  }
	
	public boolean equals(Object o) {
		if (o instanceof Edge) {
		      return ((object1.equals(((Edge) o).object1)) &&
		              (object2.equals(((Edge) o).object2))) ||
		             ((object1.equals(((Edge) o).object2)) &&
		              (object2.equals(((Edge) o).object1)));
		    } else {
		      return false;
		    }
	}
}
